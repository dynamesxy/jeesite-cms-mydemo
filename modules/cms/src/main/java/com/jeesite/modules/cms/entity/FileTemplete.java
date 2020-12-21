/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.cms.entity;

import java.io.File;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import com.jeesite.common.codec.Md5Utils;
import com.jeesite.common.io.FileUtils;
import com.jeesite.modules.cms.utils.FilesTempleteUtils;

/**
 * CMS模块-模版文件实体
 * @author 长春叭哥、ThinkGem
 * @version 2018年10月15日
 */
public class FileTemplete implements Serializable {

	private static final long serialVersionUID = 1L;
	private File file;
	
	public FileTemplete(File file) {
		super();
		this.file = file;

	}

	/**
	 * 模版文件获取
	 */
	public File getFile() {
		return file;
	}

	/**
	 * 设置模版文件
	 */
	public void setFile(File file) {
		this.file = file;
	}

	/**
	 * 文件名
	 */
	public String getFileName() {
		return FilesTempleteUtils.getFileTempleteName(file);
	}

	/**
	 * 上级文件
	 */
	public String getFileTempleteParent() {
		return FilesTempleteUtils.getFileTempleteParent(getFile());
	}

	/**
	 * 文件路径
	 */
	public String getFilePath() {
		return getFileName().substring(0, getFileName().lastIndexOf('/'));
	}

	/**
	 * 文件的源码
	 */
	public String getFileSource() {
		return FilesTempleteUtils.getTempleteCodeSource(getFile());
	}

	/**
	 * 获取最后修改的时间戳
	 */
	public long getLastModified() {
		return file.lastModified();
	}

	/**
	 * 最后一次修改时间
	 */
	public Date getLastModifiedDate() {
		return new Timestamp(getLastModified());
	}

	/**
	 * 文件字节数
	 */
	public Long getLength() {
		return file.length();
	}

	/**
	 * M单位大小
	 */
	public Long getSize() {
		return (getLength() / 1024) + 1;
	}

	/**
	 * 是否为文件夹
	 */
	public boolean isDirectory() {
		return file.isDirectory();
	}

	/**
	 * MD5字符串
	 */
	public String getMd5() {
		return Md5Utils.md5File(file);
	}

	/**
	 * 文件类型
	 */
	public String getContentType() {
		return FileUtils.getContentType(file.getName());
	}

	/**
	 * 文件扩展类型
	 */
	public String getFileExtension() {
		return FileUtils.getFileExtension(file.getName());
	}

}
