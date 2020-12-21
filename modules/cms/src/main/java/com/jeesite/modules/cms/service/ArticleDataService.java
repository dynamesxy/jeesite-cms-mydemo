/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.cms.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.cms.dao.ArticleDataDao;
import com.jeesite.modules.cms.entity.ArticleData;
import com.jeesite.modules.file.utils.FileUploadUtils;

/**
 * 文章详情表Service
 * @author 长春叭哥
 * @version 2018-10-15
 */
@Service
@Transactional(readOnly = true)
public class ArticleDataService extends CrudService<ArticleDataDao, ArticleData> {

	/**
	 * 获取单条数据
	 * @param articleData
	 * @return
	 */
	@Override
	public ArticleData get(ArticleData articleData) {
		return super.get(articleData);
	}

	/**
	 * 查询分页数据
	 * @param articleData 查询条件
	 * @param articleData.page 分页对象
	 * @return
	 */
	@Override
	public Page<ArticleData> findPage(ArticleData articleData) {
		return super.findPage(articleData);
	}

	/**
	 * 保存数据（插入或更新）
	 * @param articleData
	 */
	@Override
	@Transactional(readOnly = false)
	public void save(ArticleData articleData) {
		super.save(articleData);
		// 保存上传图片
		FileUploadUtils.saveFileUpload(articleData.getId(), "articleData_image");
		// 保存上传附件
		FileUploadUtils.saveFileUpload(articleData.getId(), "articleData_file");
	}

	/**
	 * 更新状态
	 * @param articleData
	 */
	@Override
	@Transactional(readOnly = false)
	public void updateStatus(ArticleData articleData) {
		super.updateStatus(articleData);
	}

	/**
	 * 删除数据
	 * @param articleData
	 */
	@Override
	@Transactional(readOnly = false)
	public void delete(ArticleData articleData) {
		super.delete(articleData);
	}

}