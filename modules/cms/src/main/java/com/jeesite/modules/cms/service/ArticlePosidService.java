/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.cms.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.cms.entity.ArticlePosid;
import com.jeesite.modules.cms.dao.ArticlePosidDao;

/**
 * 文章推荐位Service
 * @author 长春叭哥
 * @version 2018-10-15
 */
@Service
@Transactional(readOnly=true)
public class ArticlePosidService extends CrudService<ArticlePosidDao, ArticlePosid> {
	
	/**
	 * 获取单条数据
	 * @param articlePosid
	 * @return
	 */
	@Override
	public ArticlePosid get(ArticlePosid articlePosid) {
		return super.get(articlePosid);
	}
	
	/**
	 * 查询分页数据
	 * @param articlePosid 查询条件
	 * @param articlePosid.page 分页对象
	 * @return
	 */
	@Override
	public Page<ArticlePosid> findPage(ArticlePosid articlePosid) {
		return super.findPage(articlePosid);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param articlePosid
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(ArticlePosid articlePosid) {
		super.save(articlePosid);
	}
	
	/**
	 * 更新状态
	 * @param articlePosid
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(ArticlePosid articlePosid) {
		super.updateStatus(articlePosid);
	}
	
	/**
	 * 删除数据
	 * @param articlePosid
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(ArticlePosid articlePosid) {
		super.delete(articlePosid);
	}
	
}