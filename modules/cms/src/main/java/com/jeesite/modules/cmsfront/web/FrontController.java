/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.cmsfront.web;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeesite.common.shiro.realms.A;
import com.jeesite.modules.cms.service.SiteService;
import com.jeesite.modules.cms.utils.VisitLogAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.ObjectUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.cms.entity.Article;
import com.jeesite.modules.cms.entity.ArticleData;
import com.jeesite.modules.cms.entity.Category;
import com.jeesite.modules.cms.entity.Comment;
import com.jeesite.modules.cms.entity.Site;
import com.jeesite.modules.cms.service.ArticleService;
import com.jeesite.modules.cms.service.CategoryService;
import com.jeesite.modules.cms.service.CommentService;
import com.jeesite.modules.cms.utils.CmsUtils;
import com.jeesite.modules.sys.utils.ValidCodeUtils;

/**
 * 网站Controller
 * 
 * @author ThinkGem,三片叶子,长春八哥
 * @version 2018-09-16
 */
@Controller
@RequestMapping(value = "${frontPath}")
public class FrontController extends BaseController {

	@Autowired
	private CategoryService categoryService;
	@Autowired
	private ArticleService articleService;
	@Autowired
	private CommentService commentService;
	@Autowired
	private SiteService siteService;

	/**
	 * 主站首页
	 */
	@RequestMapping(value = { "", "index", "index.html" })
	public String index(Model model) {
		return REDIRECT + frontPath + "/index-" + Site.MAIN_SITE_CODE + ".html";
	}

	/**
	 * 站点首页
	 */
	@VisitLogAnnotation()
	@RequestMapping(value = { "index-{siteCode}", "index-{siteCode}.html" })
	public String index(@PathVariable String siteCode, Model model) {
		// 如果是主站，获取主站信息并进入主页
		if (Site.isMainSite(siteCode)) {
			//查询站点信息
			List<Site> siteList = siteService.findList(new Site());
			Site site = new Site();
			if(siteList.size()>0){
				site = siteList.get(0);
			}else{
				site = CmsUtils.getSite(Site.MAIN_SITE_CODE);
			}
			Category category = new Category();
			category.setSite(site);
			category.setInMenu("1");
			List<Category> list = categoryService.findList(category);
			site.setCategoryList(list);
			category.setCategoryName("首页");
			Category indexCategory= categoryService.findList(category).get(0);
			String images = indexCategory.getImage();
			List<String> imageUrlList = new ArrayList<>();
			if(StringUtils.isNotBlank(images)){
				String[] split = images.split("\\|");
				imageUrlList = Arrays.asList(split.clone());
			}
			//获取自然遗产第一篇文章
			Category zlyc = new Category();
			zlyc.setCategoryCode("A1016");
			Article zlycArticle = new Article();
			zlycArticle.setCategory(zlyc);
			List<Article> zlycList = articleService.findList(zlycArticle);
			Article zlycShow = new Article();
			if(zlycList!=null&&zlycList.size()>0){
				zlycShow = zlycList.get(0);
				zlycShow.setArticleData(articleService.get(new ArticleData(zlycShow.getId())));
			}
			model.addAttribute("zlycShow", zlycShow);
			//获取文化遗产第一篇文章
			Category whyc = new Category();
			whyc.setCategoryCode("A1017");
			Article whycArticle = new Article();
			whycArticle.setCategory(whyc);
			List<Article> whycList = articleService.findList(whycArticle);
			Article whycShow = new Article();
			if(whycList!=null&&whycList.size()>0){
				whycShow = whycList.get(0);
				whycShow.setArticleData(articleService.get(new ArticleData(whycShow.getId())));
			}
			model.addAttribute("whycShow", whycShow);
			//获取空间技术第一篇文章
			Category kjjs = new Category();
			kjjs.setCategoryCode("A1015");
			Article kjjsArticle = new Article();
			kjjsArticle.setCategory(kjjs);
			List<Article> kjjsList = articleService.findList(kjjsArticle);
			Article kjjsShow = new Article();
			if(kjjsList!=null&&kjjsList.size()>0){
				kjjsShow = kjjsList.get(0);
				kjjsShow.setArticleData(articleService.get(new ArticleData(kjjsShow.getId())));
			}
			model.addAttribute("kjjsShow", kjjsShow);

			model.addAttribute("site", site);
			model.addAttribute("isIndex", true);
			model.addAttribute("category",indexCategory);
			model.addAttribute("imageUrlList",imageUrlList);
			return "modules/cmsfront/themes/" + site.getTheme() + "/index";
		}

		// 不是主站，则获取子站点信息
		Site site = CmsUtils.getSite(siteCode);
		model.addAttribute("site", site);

		// 子站有独立页面，则显示独立页面
		if (StringUtils.isNotBlank(site.getCustomIndexView())) {
			model.addAttribute("isIndex", true);
			return "modules/cmsfront/themes/" + site.getTheme() + "/" + site.getCustomIndexView();
		}

		// 否则显示子站第一个栏目
		List<Category> mainNavList = CmsUtils.getMainNavList(siteCode);
		if (mainNavList.size() > 0) {
			String firstCategoryCode = CmsUtils.getMainNavList(siteCode).get(0).getId();
			return REDIRECT + frontPath + "/list-" + firstCategoryCode + ".html";
		}

		// 站点中无栏目，则显示栏目分类空白页
		else {
			return "modules/cmsfront/themes/" + site.getTheme() + "/" + Category.DEFAULT_TEMPLATE + "Category";
		}
	}

	/**
	 * 内容列表
	 */
	@VisitLogAnnotation()
	@RequestMapping(value = { "list-{categoryCode}", "list-{categoryCode}.html" })
	public String list(@PathVariable String categoryCode,@RequestParam(required = false, defaultValue = "2020") String createYear,
			@RequestParam(required = false, defaultValue = "1") Integer pageNo,
			@RequestParam(required = false, defaultValue = "5") Integer pageSize, Model model,
			HttpServletRequest request) {

		// 获取栏目信息
		Category category = CmsUtils.getCategory(categoryCode);
		if (category == null || !Category.STATUS_NORMAL.equals(category.getStatus())) {
			Site site = CmsUtils.getSite(Site.MAIN_SITE_CODE);
			model.addAttribute("site", site);
			return "error/404";
		}

		// 获取站点信息
		Site site = CmsUtils.getSite(category.getSite().getId());
		Category categoryLm = new Category();
		categoryLm.setSite(site);
		categoryLm.setInMenu("1");
		List<Category> lmList = categoryService.findList(categoryLm);
		site.setCategoryList(lmList);
		model.addAttribute("site", site);

		// 当前栏目展现方式为：2：简介类栏目，栏目第一条内容
		if ("2".equals(category.getShowModes())) {
			return view(categoryCode, null, model, request);
		}

		// 当前展现方式为：0：默认，或者，1：栏目列表
		else {

			// 当前栏目的子栏目列表
			List<Category> categoryList = null;

			// 如果有子节点，则查询子栏目列表
			if (!category.getIsTreeLeaf()) {
				Category categoryParam = new Category();
				categoryParam.setSite(new Site(site.getSiteCode()));
				categoryParam.setParentCode(category.getId());
				categoryList = categoryService.findList(categoryParam);
				model.addAttribute("categoryList", categoryList);
			} else {
				categoryList = ListUtils.newArrayList();
			}

			// 当前栏目展现方式为：1 、无子栏目或公共模型，显示栏目内容列表；0：无子栏目或一个子栏目，显示栏目内容列表
			if ("1".equals(category.getShowModes()) || categoryList.size() <= 1) {

				// 有子栏目并展现方式为1，则获取第一个子栏目；无子栏目，则获取同级分类列表。
//				if (categoryList.size() > 0) {
//					category = categoryList.get(0);
//				}

//				// 没有子栏目列表，则再获取一次当前级别的栏目列表
//				else{
//					if (category.getIsRoot()){
//						categoryList.add(category);
//					}else{
//						categoryList = categoryService.findListByParentCode(category.getParentCode(), category.getSite().getId());
//						categoryList = CmsUtils.getCategoryList(category.getSite().getSiteCode(), category.getParentCode(), -1, null);
//					}
//					model.addAttribute("categoryList", categoryList);
//				}

				// 如果第一个子栏目为简介类栏目，则获取该栏目第一篇文章并展现
				if ("2".equals(category.getShowModes())) {
					return view(category.getCategoryCode(), null, model, request);
				}

				// 否则，获取内容列表信息
				else {
					// 文章模型
					if ("article".equals(category.getModuleType())) {
						//获取年份列表（每年更新）工作情况和新闻使用
						if(category.getCustomListView().equals("listWork")||category.getCustomListView().equals("listNew")){
							Calendar calendar = Calendar.getInstance();
							int nowYear = calendar.get(Calendar.YEAR);
							List<String> yearList = new ArrayList<>();
							for(int i=0;i<8;i++){
								int year = nowYear-i;
								yearList.add(year+"");
							}
							model.addAttribute("yearList", yearList);
							Page<Article> page = new Page<Article>(pageNo, pageSize);
							Article searchArticle= new Article(category);
							searchArticle.setPage(page);
							//传递年份查询条件
							if(StringUtils.isNotBlank(createYear)){
								searchArticle.setCreateYear(createYear);
							}else{
								searchArticle.setCreateYear("2020");
							}
							model.addAttribute("createYear", createYear);
							page = articleService.findPage(searchArticle);
							List<Article> list = page.getList();
							//显示部分内容
							if(list!=null&&list.size()>0){
								for(Article item: list){
									item.setArticleData(articleService.get(new ArticleData(item.getId())));
								}
							}
							model.addAttribute("page", page);
							//判断是否是新闻栏目，获取工作动态
							if(category.getCategoryCode().equals("A1009")){
								Category workCategory = new Category();
								workCategory.setCategoryCode("A1023");
								Article workArticle= new Article(workCategory);
								Page<Article> workPage = new Page<Article>(pageNo, pageSize);
								workArticle.setPage(workPage);
								workPage = articleService.findPage(workArticle);
								model.addAttribute("workPage", workPage);
							}
						}else if(category.getCustomListView().equals("list")){
							Category parentCategory = new Category();
							parentCategory.setParentCode(category.getCategoryCode());
							List<Category> childList = categoryService.findByParent(parentCategory);
							if(childList==null||childList.size()==0){
								childList.add(category);
							}
							model.addAttribute("childList", childList);
							Page<Article> page = new Page<Article>(pageNo, pageSize);
							Category searchCategory = new Category();
							//点击子栏目查询文章
							if(!createYear.equals("2020")){
								searchCategory.setCategoryCode(createYear);
							}else{
								searchCategory.setCategoryCode(childList.get(0).getCategoryCode());
								createYear = childList.get(0).getCategoryCode();
							}
							Article searchArticle= new Article(searchCategory);
							searchArticle.setPage(page);
							model.addAttribute("createYear", createYear);
							page = articleService.findPage(searchArticle);
							List<Article> list = page.getList();
							//显示部分内容
							if(list!=null&&list.size()>0){
								for(Article item: list){
									item.setArticleData(articleService.get(new ArticleData(item.getId())));
								}
							}
							model.addAttribute("page", page);
						}else if(category.getCustomListView().equals("listCategory")){
							Category parentCategory = new Category();
							parentCategory.setParentCode(category.getCategoryCode());
							List<Category> childList = categoryService.findByParent(parentCategory);
							if(childList==null||childList.size()==0){
								childList.add(category);
							}
							model.addAttribute("childList", childList);
							Page<Article> page = new Page<Article>(pageNo, pageSize);
							Category searchCategory = new Category();
							//点击子栏目查询文章
							if(!createYear.equals("2020")){
								searchCategory.setCategoryCode(createYear);
							}else{
								searchCategory.setCategoryCode(childList.get(0).getCategoryCode());
								createYear = childList.get(0).getCategoryCode();
							}
							Article searchArticle= new Article(searchCategory);
							searchArticle.setPage(page);
							model.addAttribute("createYear", createYear);
							page = articleService.findPage(searchArticle);
							List<Article> list = page.getList();
							//显示部分内容
							if(list!=null&&list.size()>0){
								for(Article item: list){
									item.setArticleData(articleService.get(new ArticleData(item.getId())));
								}
								model.addAttribute("listArticle", list.get(0));
							}else{
								Article listArticle =  new Article();
								listArticle.setArticleData(new ArticleData());
								model.addAttribute("listArticle", listArticle);
							}

						}
					}
				}

				// 将数据信息传递到视图
				model.addAttribute("category", category);
				CmsUtils.addViewConfigAttribute(model, category);
				String view = Category.DEFAULT_TEMPLATE;
				if (StringUtils.isNotBlank(category.getCustomListView())) {
					view = category.getCustomListView();
				}
				return "modules/cmsfront/themes/" + site.getTheme() + "/" + view;
			}

			// 当前栏目展现方式为：0：默认时，有子栏目：显示子栏目列表
			else {
				model.addAttribute("category", category);
				CmsUtils.addViewConfigAttribute(model, category);
				String view = Category.DEFAULT_TEMPLATE + "Category";
				if (StringUtils.isNotBlank(category.getCustomListView())) {
					view = category.getCustomListView();
				}
				return "modules/cmsfront/themes/" + site.getTheme() + "/" + view;
			}
		}
	}

	/**
	 * 内容列表（通过url自定义视图）
	 */
	@RequestMapping(value = { "list-{categoryCode}-{customView}", "listc-{categoryCode}-{customView}.html" })
	public String listCustom(@PathVariable String categoryCode, @PathVariable String customView,
			@RequestParam(required = false, defaultValue = "1") Integer pageNo,
			@RequestParam(required = false, defaultValue = "30") Integer pageSize, Model model,
			HttpServletRequest request) {

		// 获取栏目信息
		Category category = CmsUtils.getCategory(categoryCode);
		if (category == null || !Category.STATUS_NORMAL.equals(category.getStatus())) {
			Site site = CmsUtils.getSite(Site.MAIN_SITE_CODE);
			model.addAttribute("site", site);
			return "error/404";
		}

		// 获取站点信息
		Site site = CmsUtils.getSite(category.getSite().getId());
		model.addAttribute("site", site);

		// 将数据信息传递到视图
		model.addAttribute("category", category);
		CmsUtils.addViewConfigAttribute(model, category);
		return "modules/cmsfront/themes/" + site.getTheme() + "/" + Category.DEFAULT_TEMPLATE + customView;
	}

	/**
	 * 显示内容
	 */
	@VisitLogAnnotation()
	@RequestMapping(value = { "view-{categoryCode}-{contentId}", "view-{categoryCode}-{contentId}.html" })
	public String view(@PathVariable String categoryCode, @PathVariable String contentId, Model model,
			HttpServletRequest request) {

		// 获取栏目信息
		Category category = CmsUtils.getCategory(categoryCode);
		if (category == null || !Category.STATUS_NORMAL.equals(category.getStatus())) {
			Site site = CmsUtils.getSite(Site.MAIN_SITE_CODE);
			model.addAttribute("site", site);
			return "error/404";
		}

		// 文章模型
		if ("article".equals(category.getModuleType())) {

//			// 获取当前级别的栏目列表
//			List<Category> categoryList = Lists.newArrayList();
////			if (category.getIsRoot()){
////				categoryList.add(category);
////			}else{
////				categoryList = categoryService.findListByParentCode(category.getParentCode(), category.getSite().getId());
//				categoryList = CmsUtils.getCategoryList(category.getSite().getSiteCode(), category.getParentCode(), -1, null);
////			}
//			model.addAttribute("categoryList", categoryList);

			// 获取文章
			Article article = null;
			// 设置内容ID，则获取文章内容
			if (StringUtils.isNotBlank(contentId)) {
				article = articleService.get(new Article(contentId));
			}

			// 如果没有设置内容ID则获取栏目里的第一篇文章
			else {
				Page<Article> page = new Page<Article>(1, 1, -1);
				Article entity = new Article(category);
				entity.setPage(page);
				page = articleService.findPage(entity);
				if (page.getList().size() > 0) {
					article = page.getList().get(0);
					article.setArticleData(articleService.get(new ArticleData(article.getId())));
				}
			}

			// 如果没有取到文章，则抛到404页面
			if (article == null || !Article.STATUS_NORMAL.equals(article.getStatus())) {
				return "error/404";
			}

			// 文章阅读次数+1
			articleService.updateHitsAddOne(contentId);

			// 如果设置了外部链接，则跳转到指定链接
			if (StringUtils.isNotBlank(article.getHref())) {
				if (article.getHref().startsWith(request.getContextPath())) {
					article.setHref(article.getHref().replaceFirst(request.getContextPath(), ""));
				}
				return "redirect:" + article.getHref();
			}

			model.addAttribute("article", article);

			// 获取文章归属栏目的全信息
			article.setCategory(CmsUtils.getCategory(article.getCategory().getId()));
			model.addAttribute("category", article.getCategory());

			// 获取栏目所在站点全信息
			Site site = CmsUtils.getSite(category.getSite().getId());
			Category categoryLm = new Category();
			categoryLm.setSite(site);
			categoryLm.setInMenu("1");
			List<Category> lmList = categoryService.findList(categoryLm);
			site.setCategoryList(lmList);
			model.addAttribute("site", site);

			// 获取推荐文章列表
			List<Object[]> relationList = articleService.findByIds(article.getArticleData().getRelation());
			model.addAttribute("relationList", relationList);

			// 将数据信息传递到视图
			CmsUtils.addViewConfigAttribute(model, article.getCategory());
			CmsUtils.addViewConfigAttribute(model, article.getViewConfig());
			return "modules/cmsfront/themes/" + site.getTheme() + "/" + CmsUtils.getArticleView(article);
		}
		return "error/404";
	}

	/**
	 * 内容评论
	 */
	@RequestMapping(value = "comment", method = RequestMethod.GET)
	public String comment(Comment comment, String theme, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		Page<Comment> page = new Page<Comment>(request, response);
		Comment c = new Comment();
		c.setCategory(comment.getCategory());
		c.setArticleId(comment.getArticleId());
		c.setStatus(Comment.STATUS_NORMAL);
		c.setPage(page);
		page = commentService.findPage(c);
		model.addAttribute("page", page);
		model.addAttribute("comment", comment);
		return "modules/cmsfront/themes/" + theme + "/comment";
	}

	/**
	 * 内容评论保存
	 */
	@RequestMapping(value = "comment", method = RequestMethod.POST)
	@ResponseBody
	public String commentSave(Comment comment, String validCode, @RequestParam(required = false) String replyId,
			HttpServletRequest request) {
		if (StringUtils.isNotBlank(validCode)) {
			if (ValidCodeUtils.validate(request, validCode)) {
				if (StringUtils.isNotBlank(replyId)) {
					Comment replyComment = commentService.get(replyId);
					if (replyComment != null) {
						comment.setContent("<div class=\"reply\">" + replyComment.getName() + ":<br/>"
								+ replyComment.getContent() + "</div>" + comment.getContent());
					}
				}
				comment.setIp(request.getRemoteAddr());
				comment.setCreateDate(new Date());
				Boolean isAudit = ObjectUtils.toBoolean(Global.getConfig("cms.comment.isAudit"));
				comment.setStatus(isAudit ? Comment.STATUS_AUDIT : Comment.STATUS_NORMAL);
				comment.setStatus(Comment.STATUS_AUDIT);
				commentService.save(comment);
				return "{result:1, message:'提交成功" + (isAudit ? "，请等待审核" : "") + "。'}";
			} else {
				return "{result:2, message:'验证码不正确。'}";
			}
		} else {
			return "{result:2, message:'验证码不能为空。'}";
		}
	}

	/**
	 * 站点地图
	 */
	@RequestMapping(value = { "map-{siteCode}", "map-{siteCode}.html" })
	public String map(@PathVariable String siteCode, Model model) {
		Site site = CmsUtils.getSite(siteCode);
		model.addAttribute("site", site);
		return "modules/cmsfront/themes/" + site.getTheme() + "/map";
	}

}
