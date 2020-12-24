/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.cmsfront.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeesite.common.shiro.realms.S;
import com.jeesite.modules.cms.entity.Article;
import com.jeesite.modules.cms.entity.ArticleData;
import com.jeesite.modules.cms.entity.Category;
import com.jeesite.modules.cms.service.CategoryService;
import com.jeesite.modules.cms.service.SiteService;
import com.jeesite.modules.cms.utils.VisitLogAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.lang.TimeUtils;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.cms.entity.Site;
import com.jeesite.modules.cms.service.ArticleService;
import com.jeesite.modules.cms.utils.CmsUtils;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 网站搜索Controller
 * @author ThinkGem
 * @version 2018-08-33
 */
@Controller
@RequestMapping(value = "${frontPath}/search")
public class FrontSearchController extends BaseController{
	
	@Autowired
	private ArticleService articleService;
	@Autowired
	private SiteService siteService;
	@Autowired
	private CategoryService categoryService;
//	@Autowired
//	private GuestbookService guestbookService;
	
	/**
	 * 全站搜索
	 * @param t 搜索类型(article、guestbook)
	 * @param q 搜索关键字
	 * @param qand 包含以下全部的关键词
	 * @param qnot 不包含以下关键词
	 * @param a  设置为1代表高级查询
	 * @param bd 最后更新日期范围开始
	 * @param ed 最后更新日期范围结束
	 */
	@VisitLogAnnotation
	@RequestMapping(value = "")
	public String search(String t, String q, String qand, String qnot, String a, String bd,
			String ed, String siteCode,@RequestParam(required = false, defaultValue = "1") Integer pageNo,
						 @RequestParam(required = false, defaultValue = "30") Integer pageSize, HttpServletRequest request, HttpServletResponse response, Model model) {
		long start = System.currentTimeMillis();
		Site site = new Site();
		if(StringUtils.isNotBlank(siteCode)){
			Site thisSite = new Site();
			thisSite.setSiteCode(siteCode);
			site = siteService.get(thisSite);
			Category category = new Category();
			category.setSite(site);
			category.setInMenu("1");
			List<Category> list = categoryService.findList(category);
			site.setCategoryList(list);
		}else{
			site = siteService.findList(new Site()).get(0);
			siteCode = site.getSiteCode();
		}
		model.addAttribute("site", site);
		Category searchCategory = new Category();
		searchCategory.setCategoryCode("A1024");
		Category category = categoryService.get(searchCategory);
		model.addAttribute("category", category);

		// 执行检索（搜索词长度必须大于或等于两个字符）
		if (q != null && q.length() >= 2){
			
			// 文章检索
			if (StringUtils.isBlank(t) || "article".equals(t)){
				Page<Article> page = new Page<Article>(pageNo, pageSize);
				Map<String, String> parmas = MapUtils.newHashMap();
				if (StringUtils.isNotBlank(siteCode)){
					parmas.put("siteCode", siteCode);
				}
				Article searchArticle= new Article(category);
				searchArticle.setKeywords(q);
				searchArticle.setPage(page);
				List<Article> articles = articleService.searchList(searchArticle);
				if(articles!=null&&articles.size()>0){
					for(Article item: articles){
						item.setArticleData(articleService.get(new ArticleData(item.getId())));
					}
				}
				page.setList(articles);
				page.setPageInfo("匹配结果，共耗时 " + TimeUtils.formatDateAgo(System.currentTimeMillis() - start) + "。");
				model.addAttribute("page", page);
			}
			
//			// 留言检索
//			else if ("guestbook".equals(t)){
//				Page<Guestbook> page = new Page<Guestbook>(request, response);
//				page = guestbookService.searchPage(page, q, bd, ed);
//				page.setPageInfo("匹配结果，共耗时 " + TimeUtils.formatDateAgo(System.currentTimeMillis() - start) + "。");
//				model.addAttribute("page", page);
//			}
		}
		
		model.addAttribute("t", t);// 搜索类型
		model.addAttribute("q", q);// 搜索关键字
		model.addAttribute("qand", qand);// 包含以下全部的关键词
		model.addAttribute("qnot", qnot);// 不包含以下关键词
//		return "modules/cmsfront/themes/"+site.getTheme()+"/search";
		return "modules/cmsfront/themes/default/listSearch";
	}
	
}
