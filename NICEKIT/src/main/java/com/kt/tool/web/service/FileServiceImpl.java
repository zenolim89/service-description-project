package com.kt.tool.web.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.kt.tool.web.util.CompressionUtil;
import com.kt.tool.web.util.Utils;

/**
 * file service
 * 
 * @author nbware
 *
 */
@Service
public class FileServiceImpl implements FileService {
	
	/** print log **/
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	ServletContext servletContext;
	
	@Autowired
	Utils utils;
	
	@Autowired
	CompressionUtil compressionUtil;
	
	@Value("#{config['upload.file.resources.flag']}")
	private boolean resourcesUploadFlag;

	@Value("#{config['upload.file.external.path']}")
	private String uploadExternalDir;
	
	@Value("#{config['upload.file.resources.path']}")
	private String uploadResourcesDir;
	
	
	@Override
	public int uploadFile(HttpServletRequest request, String domain, String workplace, String path, MultipartFile file) throws Exception {
		log.debug("domain : {}", domain);
		log.debug("workplace : {}", workplace);
		log.debug("path : {}", path);
		log.debug("file : {}", file);
		
		int rst = 0;
		if (!StringUtils.isEmpty(file) && !file.isEmpty()) {
			String extName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
			log.debug("upload.extName : {}", extName);
			
			if (!StringUtils.isEmpty(domain) || !StringUtils.isEmpty(workplace));
			else {
				throw new Exception("Require parameter [domain or workplace]");
			}
			
			// check file extension
			if (utils.checkUploadExtension(extName));
			else {
				throw new Exception("Invlid File Ext");
			}
			
			// check upload file size
			if (utils.checkUploadSize(file.getSize(), extName));
			else {
				throw new Exception("limit maxium file size");
			}
			
			String uploadType = utils.getUploadType(extName);
			log.debug("uploadType : {}", uploadType);
			
			switch(uploadType) {
				case "json":
					rst = uploadJsonProc(request, domain, workplace, path, file);
					break;
				case "html":
					rst = uploadHtmlProc(request, domain, workplace, path, file);
					break;
				case "images":
					rst = uploadResourceProc(request, domain, workplace, path, file);
					break;
				case "zip":
					rst = uploadZipProc(request, domain, workplace, path, file);
					break;
				case "resource":
					rst = uploadResourceProc(request, domain, workplace, path, file);
					break;
				default:
					rst = -1;
					break;
			}
		}
		else {
			throw new Exception("File Not Exists");
		}
		
		return rst;
	}
	
	private int uploadJsonProc(HttpServletRequest request, String domain, String workplace, String path, MultipartFile file) {
		int rst = 0;
		try {
			domain = !StringUtils.isEmpty(domain) ? domain : "";
			workplace = !StringUtils.isEmpty(workplace) ? workplace : "";
			path = !StringUtils.isEmpty(path) ? path : "";
			String uploadTargetPath = String.format("%s/%s/%s/%s", resourcesUploadFlag ? uploadResourcesDir : uploadExternalDir, domain, workplace, path);
			uploadTargetPath = resourcesUploadFlag ? servletContext.getRealPath(uploadTargetPath) : uploadTargetPath;
			log.debug("uploadTargetPath : {}", uploadTargetPath);
			
			File targetDir = new File(uploadTargetPath);
			if (!targetDir.exists()) {
				targetDir.mkdirs();
			}
			
			String sourceFile = String.format("%s/%s", uploadTargetPath, file.getOriginalFilename());
			log.debug("sourceFile : {}", sourceFile);
			
			byte[] bytes = file.getBytes();
			Path targetPath = Paths.get(sourceFile);
			Files.write(targetPath, bytes);
			
			rst = 1;
		}
		catch (Exception ex) {
			ex.getStackTrace();
			log.error("{}", ex);
			rst = -1;
		}
		
		return rst;
	}
	
	private int uploadHtmlProc(HttpServletRequest request, String domain, String workplace, String path, MultipartFile file) {
		int rst = 0;
		try {
			domain = !StringUtils.isEmpty(domain) ? domain : "";
			workplace = !StringUtils.isEmpty(workplace) ? workplace : "";
			path = !StringUtils.isEmpty(path) ? path : "";
			String uploadTargetPath = String.format("%s/%s/%s/%s", resourcesUploadFlag ? uploadResourcesDir : uploadExternalDir, domain, workplace, path);
			uploadTargetPath = resourcesUploadFlag ? servletContext.getRealPath(uploadTargetPath) : uploadTargetPath;
			log.debug("uploadTargetPath : {}", uploadTargetPath);
			
			File targetDir = new File(uploadTargetPath);
			if (!targetDir.exists()) {
				targetDir.mkdirs();
			}
			
			String sourceFile = String.format("%s/%s", uploadTargetPath, file.getOriginalFilename());
			log.debug("sourceFile : {}", sourceFile);
			
			byte[] bytes = file.getBytes();
			Path targetPath = Paths.get(sourceFile);
			Files.write(targetPath, bytes);
			
			rst = 1;
		}
		catch (Exception ex) {
			ex.getStackTrace();
			log.error("{}", ex);
			rst = -1;
		}
		
		return rst;
	}
	
	private int uploadResourceProc(HttpServletRequest request, String domain, String workplace, String path, MultipartFile file) {
		int rst = 0;
		try {
			domain = !StringUtils.isEmpty(domain) ? domain : "";
			workplace = !StringUtils.isEmpty(workplace) ? workplace : "";
			path = !StringUtils.isEmpty(path) ? path : "";
			String uploadTargetPath = String.format("%s/%s/%s/%s", resourcesUploadFlag ? uploadResourcesDir : uploadExternalDir, domain, workplace, path);
			uploadTargetPath = resourcesUploadFlag ? servletContext.getRealPath(uploadTargetPath) : uploadTargetPath;
			log.debug("uploadTargetPath : {}", uploadTargetPath);
			
			File targetDir = new File(uploadTargetPath);
			if (!targetDir.exists()) {
				targetDir.mkdirs();
			}
			
			String sourceFile = String.format("%s/%s", uploadTargetPath, file.getOriginalFilename());
			log.debug("sourceFile : {}", sourceFile);
			
			byte[] bytes = file.getBytes();
			Path targetPath = Paths.get(sourceFile);
			Files.write(targetPath, bytes);
			
			rst = 1;
		}
		catch (Exception ex) {
			ex.getStackTrace();
			log.error("{}", ex);
			rst = -1;
		}
	
		return rst;
	}
	
	private int uploadZipProc(HttpServletRequest request, String domain, String workplace, String path, MultipartFile file) {
		int rst = 0;
		try {
			domain = !StringUtils.isEmpty(domain) ? domain : "";
			workplace = !StringUtils.isEmpty(workplace) ? workplace : "";
			path = !StringUtils.isEmpty(path) ? path : "";
			String uploadTargetPath = String.format("%s/%s/%s/%s", resourcesUploadFlag ? uploadResourcesDir : uploadExternalDir, domain, workplace, path);
			uploadTargetPath = resourcesUploadFlag ? servletContext.getRealPath(uploadTargetPath) : uploadTargetPath;
			log.debug("uploadTargetPath : {}", uploadTargetPath);
			
			File targetDir = new File(uploadTargetPath);
			if (!targetDir.exists()) {
				targetDir.mkdirs();
			}
			
			String sourceFile = String.format("%s/%s", uploadTargetPath, file.getOriginalFilename());
			log.debug("sourceFile : {}", sourceFile);
			
			byte[] bytes = file.getBytes();
			Path targetPath = Paths.get(sourceFile);
			Files.write(targetPath, bytes);
			
			File orgFile = new File(sourceFile);
			try {
				// 압축 해제
				compressionUtil.unzip(orgFile, new File(uploadTargetPath), "UTF-8");
			}
			catch (Exception e) {
				e.getStackTrace();
				log.error("{}", e);
			}
			finally {
				// zip 파일 삭제
				boolean isDel = utils.deleteDirectory(new File(sourceFile));
				log.debug("isDel : {}", isDel);
			}
			
			rst = 1;
		}
		catch (Exception ex) {
			ex.getStackTrace();
			log.error("{}", ex);
			rst = -1;
		}
	
		return rst;
	}
	
	@Override
	public int updateFile(HttpServletRequest request, String domain, String workplace, String path, String filename, String content) throws Exception {
		log.debug("domain : {}", domain);
		log.debug("workplace : {}", workplace);
		log.debug("path : {}", path);
		log.debug("filename : {}", filename);
		log.debug("content : {}", content);
		
		int rst = 0;
		if (!StringUtils.isEmpty(domain) || !StringUtils.isEmpty(workplace));
		else {
			throw new Exception("Require parameter [domain or workplace]");
		}
		
		if (!StringUtils.isEmpty(filename));
		else {
			throw new Exception("Require parameter [filename]");
		}
		
		if (!StringUtils.isEmpty(content));
		else {
			throw new Exception("Require parameter [content]");
		}
		
		domain = !StringUtils.isEmpty(domain) ? domain : "";
		workplace = !StringUtils.isEmpty(workplace) ? workplace : "";
		path = !StringUtils.isEmpty(path) ? path : "";
		String uploadTargetPath = String.format("%s/%s/%s/%s", resourcesUploadFlag ? uploadResourcesDir : uploadExternalDir, domain, workplace, path);
		uploadTargetPath = resourcesUploadFlag ? servletContext.getRealPath(uploadTargetPath) : uploadTargetPath;
		log.debug("uploadTargetPath : {}", uploadTargetPath);
		
		File targetDir = new File(uploadTargetPath);
		if (!targetDir.exists()) {
			throw new Exception("File Not Exists");
		}
		
		String sourceFile = String.format("%s/%s", uploadTargetPath, filename);
		log.debug("sourceFile : {}", sourceFile);
		
		File f = new File(sourceFile);
		if (!f.exists()) {
			throw new Exception("File Not Exists");
		}
		
		rst = utils.updateFile(sourceFile, content);
		if (rst >= 0);
		else {
			throw new Exception("File Update Failed");
		}
		log.debug("rst : {}", rst);
		
		rst = 1;
		
		return rst;
	}

}
