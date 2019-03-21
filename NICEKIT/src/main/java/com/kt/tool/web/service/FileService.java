package com.kt.tool.web.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

	int uploadFile(HttpServletRequest request, String uploadType, String domain, String workplace, MultipartFile file) throws Exception;

	int updateFile(HttpServletRequest request, String domain, String workplace, String path, String filename, String content) throws Exception;

}
