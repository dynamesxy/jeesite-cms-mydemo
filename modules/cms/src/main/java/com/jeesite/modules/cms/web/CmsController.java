/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.cms.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeesite.modules.cms.service.SiteService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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

/**
 * 内容管理Controller
 * @author ThinkGem
 * @version 2018-08-33
 */
@Controller
@RequestMapping(value = "${adminPath}/cms")
public class CmsController extends BaseController {

	@Autowired
	private ArticleService articleService;

	@Autowired
	private SiteService siteService;

	@RequiresPermissions("cms:view")
	@RequestMapping(value = { "index", "" })
	public String index(Model model) {
		System.out.println("用于初始化站点数据");
		//查询站点信息
		List<Site> siteList = siteService.findList(new Site());
		model.addAttribute("siteList", siteList);
		Site site = new Site();
		if(siteList.size()>0){
			// 用于初始化站点数据
			site = CmsUtils.getSite(siteList.get(0).getSiteCode());
		}else{
			// 用于初始化站点数据
			site = CmsUtils.getSite(Site.MAIN_SITE_CODE);
		}
		model.addAttribute("site", site);
		return "modules/cms/cmsIndex";
	}

	@RequiresPermissions("cms:view")
	@RequestMapping(value = "view")
	public String view(Model model) {
		model.addAttribute("canAdmin", "false");
		return index(model);
	}

	@RequiresPermissions("cms:view")
	@RequestMapping(value = "none")
	public String none() {
		return "modules/cms/cmsNone";
	}

	/**
	 * 全站搜索
	 * @param t 搜索类型(article、guestbook)
	 * @param q 搜索关键字
	 * @param qand 包含以下全部的关键词
	 * @param qnot 不包含以下关键词
	 * @param a 设置为1代表高级查询
	 * @param bd 最后更新日期范围开始
	 * @param ed 最后更新日期范围结束
	 */
	@RequiresPermissions("cms:view")
	@RequestMapping(value = "search")
	public String search(String t, String q, String qand, String qnot, String a, String bd, String ed, String siteCode, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		long start = System.currentTimeMillis();

		// 执行检索（搜索词长度必须大于或等于两个字符）
		if (q != null && q.length() >= 2) {

			// 文章检索
			if (StringUtils.isBlank(t) || "article".equals(t)) {
				Page<Map<String, Object>> page = new Page<Map<String, Object>>(request, response);
				Map<String, String> parmas = MapUtils.newHashMap();
				if (StringUtils.isNotBlank(siteCode)) {
					parmas.put("siteCode", siteCode);
				}
				page = articleService.searchPage(page, q, qand, qnot, bd, ed, parmas);
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
		return "modules/cms/cmsSearch";
	}

}
