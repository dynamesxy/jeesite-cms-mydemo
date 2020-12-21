/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.test;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.jeesite.common.tests.BaseInitDataTests;
import com.jeesite.common.utils.SpringUtils;
import com.jeesite.modules.Application;
import com.jeesite.modules.cms.db.InitCmsData;

/**
 * 初始化数据表
 * @author ThinkGem
 * @version 2019-12-30
 */
@ActiveProfiles("test")
@SpringBootTest(classes=Application.class)
public class InitDataForCms extends BaseInitDataTests {

	/**
	 * 安装cms模块
	 * @throws Exception
	 */
	@Test
	public void initCmsData() throws Exception {
		InitCmsData data = SpringUtils.getBean(InitCmsData.class);
		data.createTable();
		data.initCms();
	}

	/**
	 * 卸载cms模块
	 */
//	@Test
//	public void unInstallCms() throws Exception {
//		InitCmsData data = SpringUtils.getBean(InitCmsData.class);
//		data.unInstallCms();
//	}

}
