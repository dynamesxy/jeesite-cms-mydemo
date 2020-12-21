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
import com.jeesite.modules.cms.entity.VisitLog;
import com.jeesite.modules.cms.service.VisitLogService;

/**
 * 访问日志表Controller
 * @author 长春叭哥
 * @version 2018-10-15
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/visitLog")
public class VisitLogController extends BaseController {

	@Autowired
	private VisitLogService visitLogService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public VisitLog get(String id, boolean isNewRecord) {
		return visitLogService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("cms:visitLog:view")
	@RequestMapping(value = {"list", ""})
	public String list(VisitLog visitLog, Model model) {
		model.addAttribute("visitLog", visitLog);
		return "modules/cms/visitLogList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("cms:visitLog:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<VisitLog> listData(VisitLog visitLog, HttpServletRequest request, HttpServletResponse response) {
		visitLog.setPage(new Page<>(request, response));
		Page<VisitLog> page = visitLogService.findPage(visitLog); 
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("cms:visitLog:view")
	@RequestMapping(value = "form")
	public String form(VisitLog visitLog, Model model) {
		model.addAttribute("visitLog", visitLog);
		return "modules/cms/visitLogForm";
	}

	/**
	 * 保存访问日志表
	 */
	@RequiresPermissions("cms:visitLog:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated VisitLog visitLog) {
		visitLogService.save(visitLog);
		return renderResult(Global.TRUE, text("保存访问日志表成功！"));
	}
	
	/**
	 * 停用访问日志表
	 */
	@RequiresPermissions("cms:visitLog:edit")
	@RequestMapping(value = "disable")
	@ResponseBody
	public String disable(VisitLog visitLog) {
		visitLog.setStatus(VisitLog.STATUS_DISABLE);
		visitLogService.updateStatus(visitLog);
		return renderResult(Global.TRUE, text("停用访问日志表成功"));
	}
	
	/**
	 * 启用访问日志表
	 */
	@RequiresPermissions("cms:visitLog:edit")
	@RequestMapping(value = "enable")
	@ResponseBody
	public String enable(VisitLog visitLog) {
		visitLog.setStatus(VisitLog.STATUS_NORMAL);
		visitLogService.updateStatus(visitLog);
		return renderResult(Global.TRUE, text("启用访问日志表成功"));
	}
	
	/**
	 * 删除访问日志表
	 */
	@RequiresPermissions("cms:visitLog:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(VisitLog visitLog) {
		visitLogService.delete(visitLog);
		return renderResult(Global.TRUE, text("删除访问日志表成功！"));
	}
	
}