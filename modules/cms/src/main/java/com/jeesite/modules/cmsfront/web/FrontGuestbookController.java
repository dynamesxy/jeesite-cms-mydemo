///**
// * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
// */
//package com.jeesite.modules.cmsfront.web;
//
//import java.util.Date;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import com.jeesite.common.config.Global;
//import com.jeesite.common.entity.Page;
//import com.jeesite.common.lang.ObjectUtils;
//import com.jeesite.common.lang.StringUtils;
//import com.jeesite.common.web.BaseController;
//import com.jeesite.modules.cms.entity.Guestbook;
//import com.jeesite.modules.cms.entity.Site;
//import com.jeesite.modules.cms.service.GuestbookService;
//import com.jeesite.modules.cms.utils.CmsUtils;
//import com.jeesite.modules.sys.utils.ValidCodeUtils;
//
///**
// * 留言板Controller
// * @author ThinkGem
// * @version 2018-08-33
// */
//@Controller
//@RequestMapping(value = "${frontPath}/guestbook")
//public class FrontGuestbookController extends BaseController{
//
//	@Autowired
//	private GuestbookService guestbookService;
//
//	/**
//	 * 留言板
//	 */
//	@RequestMapping(value = "", method=RequestMethod.GET)
//	public String guestbook(@RequestParam(required=false, defaultValue="1") Integer pageNo,
//			@RequestParam(required=false, defaultValue="30") Integer pageSize, Model model) {
//		Site site = CmsUtils.getSite(Site.MAIN_SITE_CODE);
//		model.addAttribute("site", site);
//
//		Page<Guestbook> page = new Page<Guestbook>(pageNo, pageSize);
//		Guestbook guestbook = new Guestbook();
//		guestbook.setStatus(Guestbook.STATUS_NORMAL);
//		page = guestbookService.findPage(page, guestbook);
//		model.addAttribute("page", page);
//		return "modules/cmsfront/themes/"+site.getTheme()+"/frontGuestbook";
//	}
//
//	/**
//	 * 留言板-保存留言信息
//	 */
//	@RequestMapping(value = "", method=RequestMethod.POST)
//	public String guestbookSave(Guestbook guestbook, String validCode, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
//		if (StringUtils.isNotBlank(validCode)){
//			if (ValidCodeUtils.validate(request, validCode)){
//				guestbook.setIp(request.getRemoteAddr());
//				guestbook.setCreateDate(new Date());
//				Boolean isAudit = ObjectUtils.toBoolean(Global.getConfig("cms.guestbook.isAudit"));
//				guestbook.setStatus(isAudit ? Guestbook.STATUS_AUDIT : Guestbook.STATUS_NORMAL);
//				guestbookService.save(guestbook);
//				addMessage(redirectAttributes, "提交成功，谢谢！");
//			}else{
//				addMessage(redirectAttributes, "验证码不正确。");
//			}
//		}else{
//			addMessage(redirectAttributes, "验证码不能为空。");
//		}
//		return "redirect:"+Global.getFrontPath()+"/guestbook";
//	}
//
//}
