package com.kt.tool.web.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

	int uploadFile(HttpServletRequest request, String uploadType, String domain, String workplace, MultipartFile file);

	int uploadResource(HttpServletRequest request, String uploadType, String domain, String workplace, String string, MultipartFile file);

	int uploadZip(HttpServletRequest request, String uploadType, String domain, String workplace, String path, MultipartFile file);

	int uploadAnyFile(HttpServletRequest request, String uploadType, String domain, String workplace, String path, MultipartFile file);

	int updateFile(HttpServletRequest request, String domain, String workplace, String path, String filename, String content);

}
