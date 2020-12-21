/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.cms.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.jeesite.common.lang.StringUtils;


import org.springframework.core.io.Resource;

import com.jeesite.common.io.FileUtils;
import com.jeesite.common.io.ResourceUtils;
import com.jeesite.modules.cms.entity.FileTemplete;

/**
 * 模板文件公共类库
 * @author 长春叭哥
 * @version 2018年10月15日
 */
public class FilesTempleteUtils {

	/**
	 * 获取模版文件
	 * @param fileName
	 * @return
	 * @throws IOException
	 * @author 长春叭哥
	 */
	public static FileTemplete getFileTempleteByResource(String fileName) throws IOException {
		Resource resource = ResourceUtils.getResource(fileName);
		File f = resource.getFile();
		if (f.exists()) {
			return new FileTemplete(f);
		} else {
			return null;
		}
	}

	/**
	 * 获取模版文件
	 * @param context ServletContext
	 * @param fileName 文件名
	 * @param applicationPath 应用程序路径
	 * @return FileTemplete
	 * @author 长春叭哥
	 */
	public static FileTemplete getFileTemplete(String path) {
		File f = new File(path);
		if (f.exists()) {
			return new FileTemplete(f);
		} else {
			return null;
		}
	}

	/**
	 * 获取模版文件文件名
	 * @param file
	 * @param applicationPath
	 * @return 获取模版文件文件名
	 * @author 长春叭哥
	 */
	public static String getFileTempleteName(File file) {
		int index = file.getParentFile().getPath().indexOf("classes");
		String tempName = file.getAbsolutePath().substring(index);
		tempName = tempName.replace(File.separatorChar, '/').replace("classes", "");
		// 在resin里root的结尾是带'/'的，这样会导致getName返回的名称不以'/'开头。
		if (!tempName.startsWith("/")) {
			tempName = "/" + tempName;
		}
		return tempName;
	}

	/**
	 * 获取模版文件源代码
	 * @param file
	 * @return 获取模版文件源代码
	 * @author 长春叭哥
	 */
	public static String getTempleteCodeSource(File file) {
		if (file.isDirectory()) {
			return null;
		}
		try {
			return FileUtils.readFileToString(file, "UTF-8");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 递归调用文件目录
	 * @param context ServletContext
	 * @param result List<FileTemplete> 模板文件集合
	 * @param list 模板文件集合
	 * @author 长春叭哥
	 * @throws IOException
	 */
	public static void getAllDirectory(List<FileTemplete> result, List<FileTemplete> list) throws IOException {
		for (FileTemplete fileTemplete : list) {
			result.add(fileTemplete);
			if (fileTemplete.isDirectory()) {
				FilesTempleteUtils.getAllDirectory(result, FilesTempleteUtils.getFileTempleteListByPath(fileTemplete.getFileName(), true));
			}
		}
	}

	/**
	 * 获取模板文件集合
	 * @param context ServletContext
	 * @param path 路径
	 * @param directory 是否是目录
	 * @return List<FileTemplete> 模板文件集合
	 * @author 长春叭哥
	 * @throws IOException
	 */
	public static List<FileTemplete> getFileTempleteListByPath(String path, boolean directory) throws IOException {
		Resource[] resources = ResourceUtils.getResources(path);

		for (Resource resource : resources) {
//			System.out.println("=================----------------==============resource:"+resource);
//			System.out.println("=================----------------==============resource.getFile():"+resource.getFile());
			path = resource.getFile().getPath();
//			System.out.println("=================----------------==============path:"+path);

		}
		File f = new File(path);
		if (f.exists()) {
			File[] files = f.listFiles();
			if (files != null) {
				List<FileTemplete> list = new ArrayList<FileTemplete>();
				for (File file : files) {
					if (file.isFile() || directory)
						list.add(new FileTemplete(file));
				}
				return list;
			} else {
				return new ArrayList<FileTemplete>(0);
			}
		} else {
			return new ArrayList<FileTemplete>(0);
		}
	}

	/**
	 * 获取当前模版文件上级目录
	 * @param file
	 * @param applicationPath
	 * @return 获取当前模版文件上级目录
	 * @author 长春叭哥
	 */
	public static String getFileTempleteParent(File file) {
		int index = file.getParentFile().getPath().indexOf("classes");
		String fileParent = (file.getParent().substring(index)).replace(File.separatorChar, '/').replace("classes", "");
		// 在resin里root的结尾是带'/'的，这样会导致getName返回的名称不以'/'开头。
		if (!fileParent.startsWith("/")) {
			fileParent = "/" + fileParent;
		}
		return fileParent;
	}

	/**
	 * 通过前缀获取文件名
	 * @param context ServletContext
	 * @param path 路径
	 * @return List<String> 文件路径集合
	 * @author 长春叭哥
	 * @throws IOException
	 */
	public static List<String> getFileTempleteNameListByPrefix(String path) throws IOException {
		List<FileTemplete> list = getFileTempleteListByPath(path, false);
		List<String> result = new ArrayList<String>(list.size());
		for (FileTemplete fileTemplete : list) {
			result.add(fileTemplete.getFileName());
		}
		return result;
	}

	/**
	 * 获取模板文件相关属性
	 * @param context ServletContext
	 * @param path 路径
	 * @return List<FileTemplete> 模板文件集合
	 * @author 长春叭哥
	 * @throws IOException
	 */
	public static List<FileTemplete> getFileTempleteListForEdit(String path) throws IOException {
		//ResourceUtils.get

		List<FileTemplete> list = getFileTempleteListByPath(path, true);
		List<FileTemplete> result = new ArrayList<FileTemplete>();

		result.add(getFileTempleteByResource(path));
		getAllDirectory(result, list);
		return result;
	}

	/**
	 * 去除模板前缀
	 * @param list 模板列表
	 * @param prefix 模板前缀 例如“frontViewArticle”
	 * @param include 需包含的模板 例如“/WEB-INF/views/modules/cmsfront/themes/jeesite/articleSelectList.jsp”
	 * @param excludes 需去除的模板 例如“frontViewArticle”
	 * @return
	 */
	public static List<String> templeteTrim(List<String> list, String prefix, String include, String... excludes) {
		List<String> result = new ArrayList<String>();
		if (!StringUtils.isBlank(include) && !list.contains(include)) {
			if (!tplContain(excludes, include)) {
				int start = include.lastIndexOf("/");
				int end = include.lastIndexOf(".");
				if (start == -1 || end == -1) {
					throw new RuntimeException("include not contain '/' or '.':" + include);
				}
				result.add(include.substring(start + 1, end));
			}
		}
		for (String t : list) {
			if (!tplContain(excludes, t)) {
				int start = t.lastIndexOf("/");
				int end = t.lastIndexOf(".");
				if (start == -1 || end == -1) {
					throw new RuntimeException("name not contain '/' or '.':" + t);
				}
				t = t.substring(start + 1, end);
				if (t.contains(prefix)) {
					result.add(t);
				}
			}
		}
		return result;
	}

	/**
	 * 检查tpl是否存在于excludes里面。
	 * @param excludes
	 * @param tpl
	 * @return
	 */
	private static boolean tplContain(String[] excludes, String tpl) {
		int start = tpl.lastIndexOf("/");
		int end = tpl.lastIndexOf(".");
		if (start == -1 || end == -1) {
			throw new RuntimeException("tpl not contain '/' or '.':" + tpl);
		}
		String name = tpl.substring(start + 1, end);
		for (String e : excludes) {
			if (e.equals(name)) {
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) {
		File file = new File("D:\\temp\\src\\main\\java\\com\\jeesite\\modules\\cms\\dao\\CmsSiteDao.java");
		System.out.println(FileUtils.getContentType(file.getName()));
		System.out.println(FileUtils.getFileExtension(file.getName()));

	}
}
