/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.cms.web;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.cms.entity.Article;
import com.jeesite.modules.cms.entity.ArticleData;
import com.jeesite.modules.cms.entity.Category;
import com.jeesite.modules.cms.entity.Site;
import com.jeesite.modules.cms.service.ArticleService;
import com.jeesite.modules.cms.service.CategoryService;
import com.jeesite.modules.cms.service.FileTempleteService;
import com.jeesite.modules.cms.utils.CmsUtils;

/**
 * 文章表Controller
 * @author 长春叭哥
 * @version 2018-10-15
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/article")
public class ArticleController extends BaseController {

	@Autowired
	private ArticleService articleService;

	@Autowired
	private CategoryService categoryService;
	@Autowired
	private FileTempleteService fileTempleteService;

	/**
	 * 获取数据
	 */
	@ModelAttribute
	public Article get(String id, boolean isNewRecord) {
		return articleService.get(id, isNewRecord);
	}

	/**
	 * 查询列表
	 * @throws IOException
	 */
	@RequiresPermissions("cms:article:view")
	@RequestMapping(value = { "list", "" })
	public String list(Article article,String siteCode, String canAdmin, Boolean isAll, Model model) throws IOException {
		model.addAttribute("siteCode", siteCode);
		if (article.getCategory() != null && StringUtils.isNotBlank(article.getCategory().getCategoryCode())
				&& !Category.ROOT_CODE.equals(article.getCategory().getId())) {
			article.setCategory(CmsUtils.getCategory(article.getCategory().getId()));
		} else {
			Category category = new Category();
			category.setCategoryName("内容列表");
			article.setCategory(category);
		}
		// 栏目展现模式，当为（2：简介类栏目，栏目第一条内容）时，自动维护第一条内容
		if ("2".equals(article.getCategory().getShowModes())) {
			// 获取文章内容
			Page<Article> page = new Page<Article>(1, 1, -1);
			// 管理栏目的时候查询全部文章
			if ("true".equals(canAdmin)) {
				article.setStatus(null);
			}
			article.setPage(page);
			page = articleService.findPage(article);
			if (page.getList().size() > 0) {
				article = page.getList().get(0);
				article.setArticleData(articleService.get(new ArticleData(article.getId())));
			}
			// 管理栏目的时候进行编辑文章
			if ("true".equals(canAdmin)) {
				return form(article,siteCode, model);
			} else {
				return view(article, model);
			}
		}
		model.addAttribute("canAdmin", canAdmin);
		model.addAttribute("isAll", isAll);
		return "modules/cms/articleList";
	}

	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("cms:article:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<Article> listData(Article article,String siteCode, HttpServletRequest request, HttpServletResponse response) {
		article.setPage(new Page<>(request, response));
		article.setSiteCode(siteCode);
		Page<Article> page = articleService.findPage(article);
		return page;
	}

	/**
	 * 查看编辑表单
	 * @throws IOException
	 */
	@RequiresPermissions("cms:article:view")
	@RequestMapping(value = "form")
	public String form(Article article, String siteCode, Model model) throws IOException {
		article.setSiteCode(siteCode);
		if (article.getCategory() != null && StringUtils.isNotBlank(article.getCategory().getCategoryCode())) {
			Category categoryParam = new Category();
			categoryParam.setSite(new Site(Site.getCurrentSiteCode()));
			categoryParam.setParentCode(article.getCategory().getCategoryCode());
			List<Category> list = categoryService.findList(categoryParam);
			if (list.size() > 0) {
				article.setCategory(null);
			} else {
				article.setCategory(categoryService.get(article.getCategory().getId()));
			}
		}
		//		if (article.getCategory()=null && StringUtils.isNotBlank(article.getCategory().getId())){
		//			Category category = categoryService.get(article.getCategory().getId());
		//		}
		if (StringUtils.isBlank(article.getId())) {
			article.setStatus(Article.STATUS_DRAFT);
		}
		model.addAttribute("contentViewList", fileTempleteService.getTempleteContentDict(Article.DEFAULT_TEMPLATE));
//		model.addAttribute("contentViewList", fileTempleteService.getTempleteContentDict(Site.TEMPLETE_BASE_DIRECTION + "/" + "default"));
		model.addAttribute("article_DEFAULT_TEMPLATE", Article.DEFAULT_TEMPLATE);
		model.addAttribute("article", article);
		CmsUtils.addViewConfigAttribute(model, article.getCategory());

		model.addAttribute("article", article);
		return "modules/cms/articleForm";
	}

	@RequiresPermissions("cms:article:view")
	@RequestMapping(value = "view")
	public String view(Article article, Model model) {
		// 文章阅读次数+1
		articleService.updateHitsAddOne(article.getId());

		model.addAttribute("article", article);
		model.addAttribute("category", article.getCategory());
		model.addAttribute("site", article.getCategory().getSite());
		CmsUtils.addViewConfigAttribute(model, article.getCategory());
		CmsUtils.addViewConfigAttribute(model, article.getViewConfig());
		return "modules/cms/articleView";
	}

	/**
	 * 保存文章表
	 */
	@RequiresPermissions("cms:article:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated Article article) {
//		article.getIsNewRecord();
		if(StringUtils.isBlank(article.getId())){
			Calendar calendar = Calendar.getInstance();
			article.setCreateYear(calendar.get(Calendar.YEAR)+"");
		}
		articleService.save(article);
		return renderResult(Global.TRUE, text("保存文章表成功！"));
	}

	/**
	 * 停用文章表
	 */
	@RequiresPermissions("cms:article:edit")
	@RequestMapping(value = "disable")
	@ResponseBody
	public String disable(Article article) {
		article.setStatus(Article.STATUS_DISABLE);
		articleService.updateStatus(article);
		return renderResult(Global.TRUE, text("停用文章表成功"));
	}

	/**
	 * 启用文章表
	 */
	@RequiresPermissions("cms:article:edit")
	@RequestMapping(value = "enable")
	@ResponseBody
	public String enable(Article article) {
		article.setStatus(Article.STATUS_NORMAL);
		articleService.updateStatus(article);
		return renderResult(Global.TRUE, text("启用文章表成功"));
	}

	/**
	 * 删除文章表
	 */
	@RequiresPermissions("cms:article:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(Article article) {
		articleService.delete(article);
		return renderResult(Global.TRUE, text("删除文章表成功！"));
	}

}