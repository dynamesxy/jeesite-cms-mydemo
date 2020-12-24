package com.jeesite.modules.cms.utils;

import com.jeesite.common.lang.StringUtils;
import com.jeesite.modules.cms.entity.Article;
import com.jeesite.modules.cms.entity.Category;
import com.jeesite.modules.cms.entity.Site;
import com.jeesite.modules.cms.entity.VisitLog;
import com.jeesite.modules.cms.service.ArticleService;
import com.jeesite.modules.cms.service.CategoryService;
import com.jeesite.modules.cms.service.SiteService;
import com.jeesite.modules.cms.service.VisitLogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;

@Aspect
@Component
@Slf4j
public class VisitLogAspect {
    @Autowired
    private VisitLogService visitLogService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SiteService siteService;
    @Autowired
    private ArticleService articleService;

    @Pointcut("@annotation(com.jeesite.modules.cms.utils.VisitLogAnnotation)")
    public void visitLogPointCut(){

    }

    @AfterReturning(value="visitLogPointCut()")
    public void saveVisitLog(JoinPoint joinPoint){
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = (HttpServletRequest)requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
        //获取ip地址
        String ip = getIPAddress(request);
        String categoryCode = "";
        String contentId = "";
        String siteCode = "";
        String q = "";
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        //获取参数列表
        Object[] args = joinPoint.getArgs();
        //获取参数名称列表
        String[] parameterNames = signature.getParameterNames();
        if(parameterNames!=null&&parameterNames.length>0){
            for(int i = 0;i<parameterNames.length;i++){
                if(parameterNames[i].equals("categoryCode")){
                    categoryCode = (String) args[i];
                }
                if(parameterNames[i].equals("contentId")){
                    contentId = (String) args[i];
                }
                if(parameterNames[i].equals("siteCode")){
                    siteCode = (String) args[i];
                }
                if(parameterNames[i].equals("q")){
                    q = (String) args[i];
                }
            }
        }
        VisitLog visitLog = new VisitLog();
        visitLog.setRemoteAddr(ip);
        visitLog.setRequestUrl(request.getRequestURI());
        if(StringUtils.isNotBlank(siteCode)){
            if(siteCode.equals("main")){
                visitLog.setSiteCode("1333981114867834880");
                Site site = siteService.get("1333981114867834880");
                visitLog.setSiteName(site.getSiteName());
                visitLog.setCategoryCode("A1020");
                visitLog.setCategoryName("首页");
            }else{
                Site site = siteService.get(siteCode);
                visitLog.setSiteCode(siteCode);
                visitLog.setSiteName(site.getSiteName());
                visitLog.setCategoryCode("A1020");
                visitLog.setCategoryName("首页");
            }
        }
        if(StringUtils.isNotBlank(categoryCode)){
            Category category = categoryService.get(categoryCode);
            visitLog.setCategoryCode(categoryCode);
            visitLog.setCategoryName(category.getCategoryName());
            visitLog.setSiteCode(category.getSite().getSiteCode());
            Site site = siteService.get(category.getSite().getSiteCode());
            visitLog.setSiteName(site.getSiteName());
        }
        if(StringUtils.isNotBlank(contentId)){
            Article article = articleService.get(contentId);
            visitLog.setContentId(contentId);
            visitLog.setContentTitle(article.getTitle());
        }
        if(StringUtils.isNotBlank(q)){
            visitLog.setSearchWord(q);
        }
        //保存
        visitLogService.save(visitLog);
        //获取方法
        //Method method = signature.getMethod();
    }



    public static String getIPAddress(HttpServletRequest request) {
        String ip = null;

        //X-Forwarded-For：Squid 服务代理
        String ipAddresses = request.getHeader("X-Forwarded-For");

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //Proxy-Client-IP：apache 服务代理
            ipAddresses = request.getHeader("Proxy-Client-IP");
        }

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //WL-Proxy-Client-IP：weblogic 服务代理
            ipAddresses = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //HTTP_CLIENT_IP：有些代理服务器
            ipAddresses = request.getHeader("HTTP_CLIENT_IP");
        }

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //X-Real-IP：nginx服务代理
            ipAddresses = request.getHeader("X-Real-IP");
        }

        //有些网络通过多层代理，那么获取到的ip就会有多个，一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
        if (ipAddresses != null && ipAddresses.length() != 0) {
            ip = ipAddresses.split(",")[0];
        }

        //还是不能获取到，最后再通过request.getRemoteAddr();获取
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
