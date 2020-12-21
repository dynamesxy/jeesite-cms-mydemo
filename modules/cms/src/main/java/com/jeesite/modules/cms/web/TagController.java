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
import com.jeesite.modules.cms.entity.Tag;
import com.jeesite.modules.cms.service.TagService;

/**
 * 内容标签Controller
 * @author 长春叭哥
 * @version 2018-10-15
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/tag")
public class TagController extends BaseController {

	@Autowired
	private TagService tagService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public Tag get(String tagName, boolean isNewRecord) {
		return tagService.get(tagName, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("cms:tag:view")
	@RequestMapping(value = {"list", ""})
	public String list(Tag tag, Model model) {
		model.addAttribute("tag", tag);
		return "modules/cms/tagList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("cms:tag:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<Tag> listData(Tag tag, HttpServletRequest request, HttpServletResponse response) {
		tag.setPage(new Page<>(request, response));
		Page<Tag> page = tagService.findPage(tag); 
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("cms:tag:view")
	@RequestMapping(value = "form")
	public String form(Tag tag, Model model) {
		model.addAttribute("tag", tag);
		return "modules/cms/tagForm";
	}

	/**
	 * 保存内容标签
	 */
	@RequiresPermissions("cms:tag:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated Tag tag) {
		tagService.save(tag);
		return renderResult(Global.TRUE, text("保存内容标签成功！"));
	}
	
	/**
	 * 删除内容标签
	 */
	@RequiresPermissions("cms:tag:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(Tag tag) {
		tagService.delete(tag);
		return renderResult(Global.TRUE, text("删除内容标签成功！"));
	}
	
}