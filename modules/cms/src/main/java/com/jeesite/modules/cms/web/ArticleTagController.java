/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.cms.web;

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
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.cms.entity.ArticleTag;
import com.jeesite.modules.cms.service.ArticleTagService;

/**
 * 文章与标签关系Controller
 * @author 长春叭哥
 * @version 2018-10-15
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/articleTag")
public class ArticleTagController extends BaseController {

	@Autowired
	private ArticleTagService articleTagService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public ArticleTag get(String articleId, String tagName, boolean isNewRecord) {
		return articleTagService.get(new Class<?>[]{String.class, String.class},
				new Object[]{articleId, tagName}, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("cms:articleTag:view")
	@RequestMapping(value = {"list", ""})
	public String list(ArticleTag articleTag, Model model) {
		model.addAttribute("articleTag", articleTag);
		return "modules/cms/articleTagList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("cms:articleTag:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<ArticleTag> listData(ArticleTag articleTag, HttpServletRequest request, HttpServletResponse response) {
		articleTag.setPage(new Page<>(request, response));
		Page<ArticleTag> page = articleTagService.findPage(articleTag); 
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("cms:articleTag:view")
	@RequestMapping(value = "form")
	public String form(ArticleTag articleTag, Model model) {
		model.addAttribute("articleTag", articleTag);
		return "modules/cms/articleTagForm";
	}

	/**
	 * 保存文章与标签关系
	 */
	@RequiresPermissions("cms:articleTag:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated ArticleTag articleTag) {
		articleTagService.save(articleTag);
		return renderResult(Global.TRUE, text("保存文章与标签关系成功！"));
	}
	
	/**
	 * 删除文章与标签关系
	 */
	@RequiresPermissions("cms:articleTag:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(ArticleTag articleTag) {
		articleTagService.delete(articleTag);
		return renderResult(Global.TRUE, text("删除文章与标签关系成功！"));
	}
	
}