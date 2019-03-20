package com.kt.tool.web.model;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

public class RequestFileUploadVo implements Serializable {
	
	private static final long serialVersionUID = 8732257313273167495L;
	
	private String uploadType;
	
	private String domain;
	
	private String workplace;
	
	private String path;
	
	private MultipartFile file;
	
	private String filename;
	
	private String content;

	public String getUploadType() {
		return uploadType;
	}

	public void setUploadType(String uploadType) {
		this.uploadType = uploadType;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public String getWorkplace() {
		return workplace;
	}

	public void setWorkplace(String workplace) {
		this.workplace = workplace;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
}
