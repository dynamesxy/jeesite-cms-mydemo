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
import com.jeesite.modules.cms.entity.ArticleData;
import com.jeesite.modules.cms.service.ArticleDataService;

/**
 * 文章详情表Controller
 * @author 长春叭哥
 * @version 2018-10-15
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/articleData")
public class ArticleDataController extends BaseController {

	@Autowired
	private ArticleDataService articleDataService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public ArticleData get(String id, boolean isNewRecord) {
		return articleDataService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("cms:articleData:view")
	@RequestMapping(value = {"list", ""})
	public String list(ArticleData articleData, Model model) {
		model.addAttribute("articleData", articleData);
		return "modules/cms/articleDataList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("cms:articleData:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<ArticleData> listData(ArticleData articleData, HttpServletRequest request, HttpServletResponse response) {
		articleData.setPage(new Page<>(request, response));
		Page<ArticleData> page = articleDataService.findPage(articleData); 
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("cms:articleData:view")
	@RequestMapping(value = "form")
	public String form(ArticleData articleData, Model model) {
		model.addAttribute("articleData", articleData);
		return "modules/cms/articleDataForm";
	}

	/**
	 * 保存文章详情表
	 */
	@RequiresPermissions("cms:articleData:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated ArticleData articleData) {
		articleDataService.save(articleData);
		return renderResult(Global.TRUE, text("保存文章详情表成功！"));
	}
	
	/**
	 * 删除文章详情表
	 */
	@RequiresPermissions("cms:articleData:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(ArticleData articleData) {
		articleDataService.delete(articleData);
		return renderResult(Global.TRUE, text("删除文章详情表成功！"));
	}
	
}