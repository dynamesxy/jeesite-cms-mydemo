/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.cms.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.cms.dao.CategoryRoleDao;
import com.jeesite.modules.cms.entity.CategoryRole;

/**
 * 栏目与角色关联表Service
 * @author 长春叭哥
 * @version 2018-10-15
 */
@Service
@Transactional(readOnly=true)
public class CategoryRoleService extends CrudService<CategoryRoleDao, CategoryRole> {
	
	/**
	 * 获取单条数据
	 * @param categoryRole
	 * @return
	 */
	@Override
	public CategoryRole get(CategoryRole categoryRole) {
		return super.get(categoryRole);
	}
	
	/**
	 * 查询分页数据
	 * @param categoryRole 查询条件
	 * @param categoryRole.page 分页对象
	 * @return
	 */
	@Override
	public Page<CategoryRole> findPage(CategoryRole categoryRole) {
		return super.findPage(categoryRole);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param categoryRole
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(CategoryRole categoryRole) {
		super.save(categoryRole);
	}
	
	/**
	 * 更新状态
	 * @param categoryRole
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(CategoryRole categoryRole) {
		super.updateStatus(categoryRole);
	}
	
	/**
	 * 删除数据
	 * @param categoryRole
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(CategoryRole categoryRole) {
		super.delete(categoryRole);
	}
	
}