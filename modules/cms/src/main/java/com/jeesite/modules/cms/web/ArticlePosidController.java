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
import com.jeesite.modules.cms.entity.ArticlePosid;
import com.jeesite.modules.cms.service.ArticlePosidService;

/**
 * 文章推荐位Controller
 * @author 长春叭哥
 * @version 2018-10-15
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/articlePosid")
public class ArticlePosidController extends BaseController {

	@Autowired
	private ArticlePosidService articlePosidService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public ArticlePosid get(String articleId, String postid, boolean isNewRecord) {
		return articlePosidService.get(new Class<?>[]{String.class, String.class},
				new Object[]{articleId, postid}, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("cms:articlePosid:view")
	@RequestMapping(value = {"list", ""})
	public String list(ArticlePosid articlePosid, Model model) {
		model.addAttribute("articlePosid", articlePosid);
		return "modules/cms/articlePosidList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("cms:articlePosid:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<ArticlePosid> listData(ArticlePosid articlePosid, HttpServletRequest request, HttpServletResponse response) {
		articlePosid.setPage(new Page<>(request, response));
		Page<ArticlePosid> page = articlePosidService.findPage(articlePosid); 
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("cms:articlePosid:view")
	@RequestMapping(value = "form")
	public String form(ArticlePosid articlePosid, Model model) {
		model.addAttribute("articlePosid", articlePosid);
		return "modules/cms/articlePosidForm";
	}

	/**
	 * 保存文章推荐位
	 */
	@RequiresPermissions("cms:articlePosid:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated ArticlePosid articlePosid) {
		articlePosidService.save(articlePosid);
		return renderResult(Global.TRUE, text("保存文章推荐位成功！"));
	}
	
	/**
	 * 删除文章推荐位
	 */
	@RequiresPermissions("cms:articlePosid:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(ArticlePosid articlePosid) {
		articlePosidService.delete(articlePosid);
		return renderResult(Global.TRUE, text("删除文章推荐位成功！"));
	}
	
}