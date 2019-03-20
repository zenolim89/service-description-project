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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.kt.tool.web.exception.UploadException;
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
	public int uploadFile(HttpServletRequest request, String domain, String workplace, String path, MultipartFile file) {
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
				throw new UploadException(HttpStatus.UNAUTHORIZED, "Require parameter [domain or workplace]");
			}
			
			// check file extension
			if (utils.checkUploadExtension(extName));
			else {
				throw new UploadException(HttpStatus.UNAUTHORIZED, "Invalid FileExtension [.json, .html, .zip, .images]");
			}
			
			// check upload file size
			if (utils.checkUploadSize(file.getSize(), extName));
			else {
				throw new UploadException(HttpStatus.UNAUTHORIZED, "Exceeds the upload maximum size");
			}
			
			String uploadType = utils.getUploadType(extName);
			log.debug("uploadType : {}", uploadType);
			try {
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
			catch (Exception ex) {
				ex.getStackTrace();
				log.error("{}", ex);
				rst = -1;
			}
		}
		
		return rst;
	}
	
	@Override
	public int uploadAnyFile(HttpServletRequest request, String uploadType, String domain, String workplace, String path, MultipartFile file) {
		log.debug("uploadType : {}", uploadType);
		log.debug("domain : {}", domain);
		log.debug("workplace : {}", workplace);
		log.debug("file : {}", file);
		
		int rst = 0;
		if (!StringUtils.isEmpty(file) && !file.isEmpty()) {
			if (!StringUtils.isEmpty(domain) || !StringUtils.isEmpty(workplace));
			else {
				throw new UploadException(HttpStatus.UNAUTHORIZED, "Require parameter [domain or workplace]");
			}
			
			String extName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
			log.debug("upload.extName : {}", extName);
			
			// check uploadType
			if ("json".equalsIgnoreCase(uploadType) || "html".equalsIgnoreCase(uploadType) || "zip".equalsIgnoreCase(uploadType));
			else {
				throw new UploadException(HttpStatus.UNAUTHORIZED, "Invalid UploadType [json or html or zip]");
			}
			
			// check file extension
			if (utils.checkUploadExtension(extName));
			else {
				throw new UploadException(HttpStatus.UNAUTHORIZED, "Invalid FileExtension [.json or .html or images]");
			}
			
			// check upload file size
			if (utils.checkUploadSize(file.getSize(), extName));
			else {
				throw new UploadException(HttpStatus.UNAUTHORIZED, "Exceeds the upload maximum size");
			}
			
			try {
				path = !StringUtils.isEmpty(path) ? path : "";
				String uploadTargetPath = String.format("%s/%s/%s/%s", uploadExternalDir, uploadType, domain, workplace);
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
		}
		
		return rst;
	}

	@Override
	public int uploadResource(HttpServletRequest request, String uploadType, String domain, String workplace, String path, MultipartFile file) {
		log.debug("uploadType : {}", uploadType);
		log.debug("domain : {}", domain);
		log.debug("workplace : {}", workplace);
		log.debug("path : {}", path);
		log.debug("file : {}", file);
		
		int rst = 0;
		if (!StringUtils.isEmpty(file) && !file.isEmpty()) {
			if (!StringUtils.isEmpty(domain) || !StringUtils.isEmpty(workplace));
			else {
				throw new UploadException(HttpStatus.UNAUTHORIZED, "Require parameter [domain or workplace]");
			}
			
			String extName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
			log.debug("upload.extName : {}", extName);
			
			// check uploadType
			if ("images".equalsIgnoreCase(uploadType));
			else {
				throw new UploadException(HttpStatus.UNAUTHORIZED, "Invalid UploadType [images]");
			}
			
			// check file extension
			if (utils.checkUploadExtension(extName));
			else {
				throw new UploadException(HttpStatus.UNAUTHORIZED, "Invalid FileExtension [.png or .jpg]");
			}
			
			// check upload file size
			if (utils.checkUploadSize(file.getSize(), extName));
			else {
				throw new UploadException(HttpStatus.UNAUTHORIZED, "Exceeds the upload maximum size");
			}
			
			try {
				path = !StringUtils.isEmpty(path) ? path : "";
				String uploadTargetPath = String.format("%s/%s/%s/%s/%s", uploadExternalDir, "html", domain, workplace, path);
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
		}
		
		return rst;
	}
	
	@Override
	public int uploadZip(HttpServletRequest request, String uploadType, String domain, String workplace, String path, MultipartFile file) {
		log.debug("uploadType : {}", uploadType);
		log.debug("domain : {}", domain);
		log.debug("workplace : {}", workplace);
		log.debug("path : {}", path);
		log.debug("file : {}", file);
		
		int rst = 0;
		if (!StringUtils.isEmpty(file) && !file.isEmpty()) {
			if (!StringUtils.isEmpty(domain) || !StringUtils.isEmpty(workplace));
			else {
				throw new UploadException(HttpStatus.UNAUTHORIZED, "Require parameter [domain or workplace]");
			}
			
			String extName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
			log.debug("upload.extName : {}", extName);
			
			// check uploadType
			if ("zip".equalsIgnoreCase(uploadType));
			else {
				throw new UploadException(HttpStatus.UNAUTHORIZED, "Invalid UploadType [zip]");
			}
			
			// check file extension
			if (utils.checkUploadExtension(extName));
			else {
				throw new UploadException(HttpStatus.UNAUTHORIZED, "Invalid FileExtension [.zip]");
			}
			
			// check upload file size
			if (utils.checkUploadSize(file.getSize(), extName));
			else {
				throw new UploadException(HttpStatus.UNAUTHORIZED, "Exceeds the upload maximum size");
			}
			
			try {
				path = !StringUtils.isEmpty(path) ? path : "";
				String uploadTargetPath = String.format("%s/%s/%s/%s/%s", uploadExternalDir, "html", domain, workplace, path);
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
		}
		
		return rst;
	}
	
	private int uploadJsonProc(HttpServletRequest request, String domain, String workplace, String path, MultipartFile file) {
		int rst = 0;
		try {
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
	public int updateFile(HttpServletRequest request, String domain, String workplace, String path, String filename, String content) {
		log.debug("domain : {}", domain);
		log.debug("workplace : {}", workplace);
		log.debug("path : {}", path);
		log.debug("filename : {}", filename);
		log.debug("content : {}", content);
		
		int rst = 0;
		if (!StringUtils.isEmpty(domain) || !StringUtils.isEmpty(workplace));
		else {
			throw new UploadException(HttpStatus.UNAUTHORIZED, "Require parameter [domain or workplace]");
		}
		
		if (!StringUtils.isEmpty(filename));
		else {
			throw new UploadException(HttpStatus.UNAUTHORIZED, "Require parameter [filename]");
		}
		
		if (!StringUtils.isEmpty(content));
		else {
			throw new UploadException(HttpStatus.UNAUTHORIZED, "Require parameter [content]");
		}
		
		try {
			path = !StringUtils.isEmpty(path) ? path : "";
			String uploadTargetPath = String.format("%s/%s/%s/%s", resourcesUploadFlag ? uploadResourcesDir : uploadExternalDir, domain, workplace, path);
			uploadTargetPath = resourcesUploadFlag ? servletContext.getRealPath(uploadTargetPath) : uploadTargetPath;
			log.debug("uploadTargetPath : {}", uploadTargetPath);
			
			File targetDir = new File(uploadTargetPath);
			if (!targetDir.exists()) {
				throw new UploadException(HttpStatus.UNAUTHORIZED, "Path Not Exists");
			}
			
			String sourceFile = String.format("%s/%s", uploadTargetPath, filename);
			log.debug("sourceFile : {}", sourceFile);
			
			File f = new File(sourceFile);
			if (!f.exists()) {
				throw new UploadException(HttpStatus.UNAUTHORIZED, "File Not Exists");
			}
			
			rst = utils.updateFile(sourceFile, content);
			log.debug("rst : {}", rst);
		}
		catch (Exception ex) {
			ex.getStackTrace();
			log.error("{}", ex);
			rst = -1;
		}
		
		return rst;
	}

}
