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
import com.jeesite.modules.cms.entity.CategoryRole;
import com.jeesite.modules.cms.service.CategoryRoleService;

/**
 * 栏目与角色关联表Controller
 * @author 长春叭哥
 * @version 2018-10-15
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/categoryRole")
public class CategoryRoleController extends BaseController {

	@Autowired
	private CategoryRoleService categoryRoleService;

	/**
	 * 获取数据
	 */
	@ModelAttribute
	public CategoryRole get(String categoryCode, String roleCode, boolean isNewRecord) {
		return categoryRoleService.get(new Class<?>[] { String.class, String.class }, new Object[] { categoryCode, roleCode }, isNewRecord);
	}

	/**
	 * 查询列表
	 */
	@RequiresPermissions("cms:categoryRole:view")
	@RequestMapping(value = { "list", "" })
	public String list(CategoryRole categoryRole, Model model) {
		model.addAttribute("categoryRole", categoryRole);
		return "modules/cms/categoryRoleList";
	}

	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("cms:categoryRole:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<CategoryRole> listData(CategoryRole categoryRole, HttpServletRequest request, HttpServletResponse response) {
		categoryRole.setPage(new Page<>(request, response));
		Page<CategoryRole> page = categoryRoleService.findPage(categoryRole);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("cms:categoryRole:view")
	@RequestMapping(value = "form")
	public String form(CategoryRole categoryRole, Model model) {
		model.addAttribute("categoryRole", categoryRole);
		return "modules/cms/categoryRoleForm";
	}

	/**
	 * 保存栏目与角色关联表
	 */
	@RequiresPermissions("cms:categoryRole:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated CategoryRole categoryRole) {
		categoryRoleService.save(categoryRole);
		return renderResult(Global.TRUE, text("保存栏目与角色关联表成功！"));
	}

	/**
	 * 删除栏目与角色关联表
	 */
	@RequiresPermissions("cms:categoryRole:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(CategoryRole categoryRole) {
		categoryRoleService.delete(categoryRole);
		return renderResult(Global.TRUE, text("删除栏目与角色关联表成功！"));
	}

}