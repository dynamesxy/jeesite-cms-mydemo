/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.cms.service;

import java.util.List;

import org.apache.commons.text.StringEscapeUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.TreeService;
import com.jeesite.modules.cms.dao.CategoryDao;
import com.jeesite.modules.cms.entity.Category;
import com.jeesite.modules.cms.utils.CmsUtils;
import com.jeesite.modules.file.utils.FileUploadUtils;
import com.jeesite.modules.sys.entity.Role;

/**
 * 栏目表Service
 * @author 长春叭哥
 * @version 2018-10-15
 */
@Service
@Transactional(readOnly = true)
public class CategoryService extends TreeService<CategoryDao, Category> {

	/**
	 * 获取单条数据
	 * @param category
	 * @return
	 */
	@Override
	public Category get(Category category) {
		return super.get(category);
	}

	/**
	 * 查询列表数据
	 * @param category
	 * @return
	 */
	@Override
	public List<Category> findList(Category category) {
		return super.findList(category);
	}


	public List<Category> findByParent(Category category) {
		return dao.findByParent(category);
	}

	/**
	 * 查询授权的栏目
	 */
	public List<Category> findList(Category category, boolean isDataScopeFilter) {
		if (isDataScopeFilter && !category.getCurrentUser().isAdmin()) {
			List<String> roleCodeList = ListUtils.newArrayList();
			List<Role> roleList = category.getCurrentUser().getRoleList();
			for (Role role : roleList) {
				if (role != null) {
					roleCodeList.add(role.getRoleCode());
				}
			}
			category.setRoleCodeList(roleCodeList);
		}
		return super.findList(category);
	}

	/**
	 * 保存数据（插入或更新）
	 * @param category
	 */
	@Override
	@Transactional(readOnly = false)
	public void save(Category category) {
		if (StringUtils.isNotBlank(category.getViewConfig())){
            category.setViewConfig(StringEscapeUtils.unescapeHtml4(category.getViewConfig()));
        }
		super.save(category);
		CmsUtils.removeCache("mainNavList_"+category.getSite().getId());
		// 保存上传图片
		FileUploadUtils.saveFileUpload(category.getId(), "category_image");
	}

	/**
	 * 更新状态
	 * @param category
	 */
	@Override
	@Transactional(readOnly = false)
	public void updateStatus(Category category) {
		super.updateStatus(category);
	}

	/**
	 * 删除数据
	 * @param category
	 */
	@Override
	@Transactional(readOnly = false)
	public void delete(Category category) {
		super.delete(category);
	}

	/**
	 * 通过编号获取栏目列表(返回列表，保留categoryCodes的顺序)
	 */
	public List<Category> findListByCodes(String categoryCodes) {
		List<Category> list = ListUtils.newArrayList();
		String[] codes = StringUtils.split(categoryCodes, ",");
		if (codes != null && codes.length > 0) {
			Category category = new Category();
			category.setCategoryCodeList(ListUtils.newArrayList(codes));
			List<Category> categoryList = findList(category);
			for (String code : codes) {
				for (Category e : categoryList) {
					if (e.getCategoryCode().equals(code)) {
						list.add(e);
						break;
					}
				}
			}
		}
		return list;
	}

}