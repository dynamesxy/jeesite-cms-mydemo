/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.cms.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.cms.dao.ArticleDao;
import com.jeesite.modules.cms.dao.ArticleDataDao;
import com.jeesite.modules.cms.entity.Article;
import com.jeesite.modules.cms.entity.ArticleData;
import com.jeesite.modules.file.utils.FileUploadUtils;

/**
 * 文章表Service
 * @author 长春叭哥
 * @version 2018-10-15
 */
@Service
@Transactional(readOnly = true)
public class ArticleService extends CrudService<ArticleDao, Article> {

	@Autowired
	private ArticleDataDao articleDataDao;

	/**
	 * 获取单条数据
	 * @param article
	 * @return
	 */
	@Override
	public Article get(Article article) {
		Article entity = super.get(article);
		if (entity != null && StringUtils.isNotBlank(article.getId())) {
			entity.setArticleData(get(new ArticleData(article.getId())));
		}
		return entity;
	}

	/**
	 * 获取文章详情内容
	 */
	public ArticleData get(ArticleData articleData) {
		return articleDataDao.get(articleData);
	}

	/**
	 * 查询分页数据
	 * @param article 查询条件
	 * @param article.page 分页对象
	 * @return
	 */
	@Override
	public Page<Article> findPage(Article article) {
		return super.findPage(article);
	}

	/**
	 * 保存数据（插入或更新）
	 * @param article
	 */
	@Override
	@Transactional(readOnly = false)
	public void save(Article article) {
		super.save(article);
		if (article.getIsNewRecord()) {
			article.getArticleData().setId(article.getId());
			articleDataDao.insert(article.getArticleData());
		} else {
			articleDataDao.update(article.getArticleData());
		}
		// 保存上传图片
		FileUploadUtils.saveFileUpload(article.getId(), "article_image");
		//		// 保存上传附件
		//		FileUploadUtils.saveFileUpload(article.getId(), "article_file");
	}

	/**
	 * 更新状态
	 * @param article
	 */
	@Override
	@Transactional(readOnly = false)
	public void updateStatus(Article article) {
		super.updateStatus(article);
	}

	/**
	 * 删除数据
	 * @param article
	 */
	@Override
	@Transactional(readOnly = false)
	public void delete(Article article) {
		super.delete(article);
	}

	@Transactional(readOnly = false)
	public void updateHitsAddOne(String id) {
		dao.updateHitsAddOne(id);
	}

	/**
	 * 文章高级搜索
	 * @param page 分页对象
	 * @param qStr 搜索字符串
	 * @param qand 包含的字符串
	 * @param qnot 不包含的字符串
	 * @param bd 开始日期
	 * @param ed 结束日期
	 */
	public Page<Map<String, Object>> searchPage(Page<Map<String, Object>> page, String qStr, String qand, String qnot, String bd, String ed,
			Map<String, String> params) {
//		Article article = new Article();
//		article.setKeywords(qStr);
//		List<Article> list = dao.findList(article);
//		page.setList(list);
		return page;
	}

	public List<Article> searchList(Article article){
		List<Article> articles = dao.searchList(article);
		return articles;
	}
	/**
	 * 根据条件重建索引（目前支持：按栏目、按站点、全部数据）<br>
	 * 按栏目：Article article = new Article(new Category(category.getCategoryCode()));<br>
	 * 按站点： Article article = new Article(new Category());<br>
	 * article.getCategory().setSite(new Site(site.getSiteCode())); 重建所有：其它情况下
	 * @param isDelete 执行删除索引
	 * @param isUpdate 执行更新索引
	 * @author ThinkGem
	 */
	public void rebulidIndex(Article article, boolean isDelete, boolean isUpdate) {
		//扩展功能
	}
	
	/**
	 * 通过编号获取内容标题
	 * @return new Object[]{栏目Id,内容Id,内容标题}
	 */
	public List<Object[]> findByIds(String ids) {
		if (ids == null) {
			return new ArrayList<Object[]>();
		}
		List<Object[]> list = ListUtils.newArrayList();
		String[] idss = StringUtils.split(ids, ",");
		Article where = new Article();
		where.setId_in(idss);
		List<Article> l = dao.findList(where);
		for (Article e : l) {
			list.add(new Object[] { e.getCategory().getId(), e.getId(), StringUtils.abbr(e.getTitle(), 50) });
		}
		return list;
	}

}