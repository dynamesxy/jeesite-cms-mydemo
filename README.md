# jeesite4-cms

#### 项目介绍

JeeSite4 CMS 内容管理模块.

##### 已完成
1. 站点管理
2. 栏目管理
3. 在线模板管理
4. 内容发布
5. 自定义函数、自定义标签
6. CMS模块权限管理
7. CMS模板站 首页 列表页 详情页

##### 未完成

1. 站内统计
2. 站内搜索 
3. 标签管理
4. Demo模板站
5. 留言管理
6. 面包屑导航



 **项目目前完成度80%, 即将上线内测。。。** 

#### 软件架构
软件架构说明


#### 安装教程

1. IDE导入Maven工程jeesite4-cms
2. 配置application.yml 数据库连接字符串（备注：目前，仅支持mysql，有其他需求者可以自行转化。）
3. 执行com.jeesite.test.InitCoreData中initCoreData()方法  运行时追加-Djeesite.initdata=true参数
4. 执行InitCmsData中initCMSData()方法安装 unInstallCms 方法卸载


#### 视频教程

1.JeeSite-CMS模块安装教程
        https://www.bilibili.com/video/av78047896/

2.模块安装请参考 B站视频教程 ：JeeSite4.0 如何创建新模块？
	https://space.bilibili.com/383413957/channel/index




# JeeSite 4.0 企业信息化快速开发平台

## 项目地址

https://gitee.com/thinkgem/jeesite4-cms

## 配套视频教程

https://ke.qq.com/course/376588?tuin=c55da0ad

视频教程QQ群1：866607936


## Beetl 模板文档

http://ibeetl.com/guide/#/beetl/basic

## JeeSite-module-cms UI

https://adminlte.io/
https://www.pikeadmin.com/demo/charts.html

## JeeSite-module-cms Bug
01. sql bug。
mssql：
Reserved word is used as the column name. Table name : js_cms_site 。
Reserved word is used as the column name. Table name : js_cms_article。
postgresql：
Reserved word is used as the column name. Table name : js_cms_guestbook。


## SiteMesh 3.0.1
http://wiki.sitemesh.org/wiki/display/sitemesh3/Getting+Started+with+SiteMesh+3
## Bootstrap UI  
MIT license

Themes for Bootstrap https://bootswatch.com
https://bootswatch.com/