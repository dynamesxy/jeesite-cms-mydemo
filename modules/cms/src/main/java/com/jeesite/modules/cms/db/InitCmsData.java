package com.jeesite.modules.cms.db;

import javax.annotation.PostConstruct;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.jeesite.common.tests.BaseInitDataTests;
import com.jeesite.modules.sys.db.InitCoreData;

@Component
@ConditionalOnProperty(name="jeesite.initdata", havingValue="true", matchIfMissing=false)
public class InitCmsData extends BaseInitDataTests {

	@PostConstruct
	public void initialize() {
		super.initialize(InitCoreData.class);
	}
	
	/**
	 * 建表语句执行
	 */
	public void createTable() throws Exception {
		runScript("cms.sql");
	}

	/**
	 * cms初始数据
	 */
	public void initCms() throws Exception {
//		clearTable(Article.class);
//		clearTable(ArticleData.class);
//		clearTable(ArticlePosid.class);
//		clearTable(ArticleTag.class);
//		clearTable(Category.class);
//		clearTable(CategoryRole.class);
//		clearTable(Comment.class);
//		clearTable(Report.class);
//		clearTable(Site.class);
//		clearTable(Tag.class);
//		clearTable(VisitLog.class);
		runScript("cms-data.sql");

//		initExcelData(Article.class, new MethodCallback() {
//			@Override
//			public Object execute(Object... params) {
//				String action = (String)params[0];
//				if("save".equals(action)){
//					Article entity = (Article)params[1];
//					entity.setIsNewRecord(true);
//					articleService.save(entity);
//					return null;
//				}
//				return null;
//			}
//		});
	}

	/**
	 * 卸载cms模块代码
	 * 
	 * @throws Exception
	 */
	public void unInstallCms() throws Exception {
		runScript("uninstall-cms.sql");
	}
	
}
