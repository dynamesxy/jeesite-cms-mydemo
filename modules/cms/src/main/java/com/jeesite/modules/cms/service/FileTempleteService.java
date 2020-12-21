/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.cms.service;

import java.io.IOException;
import java.util.List;

import com.jeesite.modules.cms.dao.SiteDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beust.jcommander.internal.Lists;
import com.jeesite.modules.cms.entity.FileTemplete;
import com.jeesite.modules.cms.entity.Site;
import com.jeesite.modules.cms.utils.CmsUtils;
import com.jeesite.modules.cms.utils.FilesTempleteUtils;
import com.jeesite.modules.sys.entity.DictData;

/**
 * 模版文件Service
 * @author 长春叭哥
 * @version 2018年10月16日
 */
@Service
@Transactional(readOnly = true)
public class FileTempleteService {

	@Autowired
	private SiteDao siteDao;

	public List<String> getTempleteContent(String prefix) throws IOException {
		//查询站点信息
		List<Site> siteList = siteDao.findList(new Site());
		Site site = new Site();
		if(siteList.size()>0){
			// 用于初始化站点数据
			site = CmsUtils.getSite(siteList.get(0).getSiteCode());
		}else{
			// 用于初始化站点数据
			site = CmsUtils.getSite(Site.MAIN_SITE_CODE);
		}
		List<String> tplList = getFileTempleteNameListByPrefix(site.getSolutionPath());
		tplList = FilesTempleteUtils.templeteTrim(tplList, prefix, "");
		return tplList;
	}

	/**
	 * 为了兼容select控件不支持List<String>功能添加
	 * @param prefix
	 * @return
	 * @throws IOException
	 * @author 长春叭哥
	 */
	public List<DictData> getTempleteContentDict(String prefix) throws IOException {
		List<DictData> listSite = Lists.newArrayList();
		for (String fileName : getTempleteContent(prefix)) {
			listSite.add(new DictData(fileName));
		}
		return listSite;
	}

	/**
	 * 通过前缀获取文件名集合
	 * @param filePath
	 * @return List<String> 文件名集合
	 * @author 长春叭哥
	 * @throws IOException
	 */
	public List<String> getFileTempleteNameListByPrefix(String filePath) throws IOException {
		return FilesTempleteUtils.getFileTempleteNameListByPrefix(filePath);
	}

	/**
	 * 获取模版文件列表
	 * @param path 路径
	 * @param directory
	 * @return List<FileTemplete> 模版文件集合
	 * @author 长春叭哥
	 * @throws IOException
	 */
	public List<FileTemplete> getFileTempleteListByPath(String path, boolean directory) throws IOException {
		return FilesTempleteUtils.getFileTempleteListByPath(path, directory);
	}

	/**
	 * 获取编辑文件列表
	 * @param path 路径
	 * @return List<FileTemplete> 模版文件集合
	 * @author 长春叭哥
	 * @throws IOException
	 */
	public List<FileTemplete> getFileTempleteListForEdit(String path) throws IOException {
		return FilesTempleteUtils.getFileTempleteListForEdit(path);
	}

	/**
	 * 获取模版文件
	 * @param name
	 * @return
	 * @author 长春叭哥
	 * @throws IOException
	 */
	public FileTemplete getFileTemplete(String name) throws IOException {
		return FilesTempleteUtils.getFileTempleteByResource(name);
	}
}
