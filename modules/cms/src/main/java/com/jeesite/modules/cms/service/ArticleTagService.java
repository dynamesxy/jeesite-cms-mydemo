/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.cms.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.cms.dao.ArticleTagDao;
import com.jeesite.modules.cms.entity.ArticleTag;

/**
 * 文章与标签关系Service
 * @author 长春叭哥
 * @version 2018-10-15
 */
@Service
@Transactional(readOnly=true)
public class ArticleTagService extends CrudService<ArticleTagDao, ArticleTag> {
	
	/**
	 * 获取单条数据
	 * @param articleTag
	 * @return
	 */
	@Override
	public ArticleTag get(ArticleTag articleTag) {
		return super.get(articleTag);
	}
	
	/**
	 * 查询分页数据
	 * @param articleTag 查询条件
	 * @param articleTag.page 分页对象
	 * @return
	 */
	@Override
	public Page<ArticleTag> findPage(ArticleTag articleTag) {
		return super.findPage(articleTag);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param articleTag
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(ArticleTag articleTag) {
		super.save(articleTag);
	}
	
	/**
	 * 更新状态
	 * @param articleTag
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(ArticleTag articleTag) {
		super.updateStatus(articleTag);
	}
	
	/**
	 * 删除数据
	 * @param articleTag
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(ArticleTag articleTag) {
		super.delete(articleTag);
	}
	
}