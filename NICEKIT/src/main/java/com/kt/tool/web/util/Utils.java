package com.kt.tool.web.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class Utils {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Value("#{config['upload.file.extension']}")
	private String[] suffixName;
	
	@Value("#{config['upload.file.size']}")
	private long MAX_ALLOWED_FILE_SIZE;
	
	@Value("#{config['code.200']}")
	private String successMessage;
	
	@Value("#{config['code.204']}")
	private String emptyMessage;
	
	@Value("#{config['zip.file.extension']}")
	private String[] zipExt;
	
	@Value("#{config['images.file.extension']}")
	private String[] imagesExt;
	
	@Value("#{config['json.file.extension']}")
	private String[] jsonExt;
	
	@Value("#{config['template.file.extension']}")
	private String[] templateExt;
	
	@Value("#{config['resource.file.extension']}")
	private String[] resourceExt;
	
	/**
	 * 
	 * @param s
	 * @return
	 */
	public boolean isEmpty(Object s) {
		if(s == null) {
			return true;
		}
		
		
		if ((s instanceof String) && (((String)s).trim().length() == 0)) {
			return true;
		}
		
		if((s instanceof String) && (((String)s).trim().length() > 0)) {
			/*
			Pattern pattern = Pattern.compile("^\\s*[a-z]\\s+[A-Za-z][0-9]$");
			Matcher matcher = pattern.matcher((String) s);
			if(matcher.matches())
				return true;
				
			return false;
			*/
		}
		
		if ((s instanceof Integer) && ((Integer) s <= 0)) {
			return true;
		}
		
		if (s instanceof HashMap) {
			return ((HashMap<?, ?>)s).isEmpty();
		}
		
		if (s instanceof ArrayList) {
			return ((ArrayList<?>)s).isEmpty();
		}
		
		if (s instanceof Object[]) {
			return (((Object[])s).length == 0);
		}
		
		return false;
	}
	
	public boolean isNotEmpty(Object s) {
		return !isEmpty(s);
	}
	
	/**
	 * API 결과 메시지 설정 (200, 204, etc)
	 * 
	 * @param code
	 * @return
	 */
	public String getResultMessage(String code) {
		String message = null;
		
		try {
			if (!StringUtils.isEmpty(code));
			else {
				return "UnKnown Message";
			}
			
			switch(code) {
				case "200":
					message = successMessage;
					break;
				case "204":
					message = emptyMessage;
					break;
				default:
					message = "UnKnown Message";
					break;
			}
		}
		catch (Exception ex) {
			
		}
		
		return message;
	}
	
	/**
	 * 업로드 타입 리턴 (내부 로직에서 사용)
	 * 
	 * @param extName
	 * @return
	 */
	public String getUploadType(String extName) {
		String uploadType = null;
		
		if (!StringUtils.isEmpty(extName));
		else {
			return null;
		}
		
		try {
			isExt:
			for (String ext1:zipExt) {
				if (extName.equalsIgnoreCase(ext1)) {
					uploadType = "zip";
					break isExt;
				}
				
				for (String ext2:imagesExt) {
					if (extName.equalsIgnoreCase(ext2)) {
						uploadType = "images";
						break isExt;
					}
					
					for (String ext3:jsonExt) {
						if (extName.equalsIgnoreCase(ext3)) {
							uploadType = "json";
							break isExt;
						}
						
						for (String ext4:templateExt) {
							if (extName.equalsIgnoreCase(ext4)) {
								uploadType = "html";
								break isExt;
							}
							
							for (String ex5:resourceExt) {
								if (extName.equalsIgnoreCase(ex5)) {
									uploadType = "resource";
									break isExt;
								}
							}
						}
					}
				}
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
			uploadType = null;
		}
		
		return uploadType;
	}
	
	/**
	 * check upload file extension. (업로드 대상 확장자, 허용 타켓 확장자)
	 * 
	 * @param extName
	 * @param targetExt
	 * @return
	 */
	public boolean checkUploadExtension(String extName) {
		boolean exeFlag = false;
		
		if (!StringUtils.isEmpty(extName));
		else {
			return false;
		}
		
		if (!StringUtils.isEmpty(suffixName)) {
			isFlag: 
			for (String name:suffixName) {
				if (extName.equalsIgnoreCase(name)) {
					exeFlag = true;
					break isFlag;
				}
			}
		}
		
		return exeFlag;
	}
	
	/**
	 * check upload file extension. (업로드 대상 확장자, 허용 타켓 확장자)
	 * 
	 * @param extName
	 * @param targetExt
	 * @return
	 */
	public boolean checkUploadExtension(String extName, String targetExt) {
		boolean exeFlag = false;
		
		if (!isEmpty(extName));
		else {
			return false;
		}
		
		if (!isEmpty(suffixName)) {
			if (!isEmpty(targetExt)) {
				if (targetExt.equalsIgnoreCase(extName));
				else {
					return false;
				}
			}
			
			isFlag: 
			for (String name:suffixName) {
				if (extName.equalsIgnoreCase(name)) {
					exeFlag = true;
					break isFlag;
				}
			}
		}
		
		return exeFlag;
	}
	
	/**
	 * check upload file size
	 * 
	 * @param size
	 * @param targetFile
	 * @return
	 */
	public boolean checkUploadSize(long size, String targetFile) {
		
		boolean isAccess = false;
		
		if (size > 0 && !StringUtils.isEmpty(targetFile));
		else {
			return false;
		}
		
		long limit = MAX_ALLOWED_FILE_SIZE;
		
		log.debug("current file size : {}", size);
		log.debug("limit file size : {}", limit);
		if (size <= limit) {
			isAccess = true;
		}
		
		return isAccess;
	}
	
	/**
	 * read file. (원본 파일, 인코딩)
	 * 
	 * @param _file
	 * @param _encoding
	 * @return
	 */
	public synchronized String getFileData(String _file, String _encoding) {
		StringBuffer sbResult      = null;
		FileInputStream	fiStream   = null;
		InputStreamReader isReader = null;
		BufferedReader bfReader	   = null;
		
		String strTemp     = null;
		String strReturn   = null;
		String strFileName = null;
		
		strFileName = _file.trim();
		
		if(strFileName != null && (new File(strFileName)).exists()) {
			try {
				fiStream = new FileInputStream(strFileName);
				isReader = new InputStreamReader(fiStream, _encoding);
				bfReader = new BufferedReader(isReader);
				
				sbResult = new StringBuffer ("");
				do {
					strTemp = bfReader.readLine();
					if(strTemp != null) {
						sbResult.append(strTemp + "\n");
					}
					
				} while(strTemp != null);
				strReturn = sbResult.toString();
			}
			catch (Exception ex) {
				ex.printStackTrace();
				strReturn = null;
				// System.gc();
			} finally {
				if(bfReader != null) {
					try {
						bfReader.close();
					}
					catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				
				if(isReader != null) {
					try {
						isReader.close();
					}
					catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				
				if(fiStream != null) {
					try {
						fiStream.close();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		} else {
			strReturn = null;
		}
		
		return strReturn;
	}
	
	/**
	 * move file (원본 경로, 원본 파일명, 타켓 경로)
	 * 
	 * @param sourcePath
	 * @param originFile
	 * @param targetPath
	 * @return
	 */
	public int moveFile(String sourcePath, String originFile, String targetPath) {
		int rst = -1;
		
		try {
			String sourceFile = String.format("%s/%s", sourcePath, originFile);
			
			File orgFile = new File(sourceFile);
			if (orgFile.exists()) {
				File targetDir = new File(targetPath);
				if (!targetDir.exists()) {
					targetDir.mkdirs();
				}
				
				String targetFile = String.format("%s/%s", targetPath, originFile);
				File newFile = new File(targetFile);
				
				byte[] buf = new byte[1024];
				FileInputStream fin = null;
				FileOutputStream fout = null;
				
				try {
					buf = new byte[1024];
					fin = new FileInputStream(orgFile);
					fout =  new FileOutputStream(newFile);
					
					int read = 0;
					while ((read=fin.read(buf,0,buf.length)) != -1) {
						fout.write(buf, 0, read);
					}
					
					rst = 1;
				}
				catch(IOException e) {
					e.getStackTrace();
					log.error("{}", e);
					rst = -1;
				}
				finally {
					try {
						if (null != fin)
							fin.close();
						if (null != fout)
							fout.close();
					}
					catch (IOException e) {}
					orgFile.delete();
				}
			}
		}
		catch (Exception e) {
			e.getStackTrace();
			log.error("{}", e);
			rst = -1;
		}
		
		return rst;
		
	}
	
	/**
	 * delete directory or delete file
	 * 
	 * @param path
	 * @return
	 */
	public boolean deleteDirectory(File path) {
		
		try {
			if (!isEmpty(path));
			else {
				return false;
			}
			
			if (!path.exists()) {
				return false;
			}
			
			File[] tempFile = path.listFiles();
			if (!isEmpty(tempFile) && tempFile.length > 0) {
				for (File file:tempFile) {
					if (file.isDirectory()) {
						deleteDirectory(file);
					}
					else {
						file.delete();
					}
				}
			}
		}
		catch (Exception e) {
			e.getStackTrace();
			log.error("{}", e);
		}
		
		return path.delete();
		
	}
	
	/**
	 * copy directory (원본, 대상)
	 * 
	 * @param sourcePath
	 * @param newPath
	 * @return
	 */
	public boolean copyDirectory(File sourcePath, File newPath) {
		
		boolean isCopy = false;
		
		try {
			if (!sourcePath.exists()) {
				return false;
			}
			
			if (!isEmpty(newPath.getAbsolutePath()) && !newPath.exists()) {
				if (newPath.mkdirs());
				else {
					return false;
				}
			}
			
			File[] fileList = sourcePath.listFiles();
			if (!isEmpty(fileList) && fileList.length > 0) {
				for (File result:fileList) {
					
					File originPath = new File(result.getAbsolutePath());
					File targetPath = new File(newPath.getAbsolutePath() + File.separator + result.getName());
					
					if (result.isDirectory()) {
						copyDirectory(originPath, targetPath);
					}
					else if (result.isFile()) {
						copyFile(result, targetPath);
					}
					
				}
				
				isCopy = true;
			}
			
		}
		catch (Exception e) {
			e.getStackTrace();
			log.error("{}", e);
			
			isCopy = false;
		}
		
		return isCopy;
	}
	
	/**
	 * copy file (원본, 대상)
	 * 
	 * @param oldFile
	 * @param newDir
	 * @return
	 */
	public boolean copyFile(File oldFile, File newDir) {
		
		boolean isCopy = false;
		
		if (oldFile.exists()) {
			
			FileInputStream fis = null;
			FileOutputStream fos = null;
			try {
				fis = new FileInputStream(oldFile.getAbsolutePath());
				fos = new FileOutputStream(newDir.getAbsolutePath());
				
				int input = 0;
				while ((input = fis.read()) != -1 ) {
					fos.write(input);
				}
				
				isCopy = true;
			}
			catch (Exception e) {
				e.getStackTrace();
				log.error("{}", e);
				isCopy = false;
			}
			finally {
				try {
					if (null != fis) fis.close();
					if (null != fos) fos.close();
				}
				catch (Exception e) {
					e.getStackTrace();
					log.error("{}", e);
				}
			}
		}
		
		return isCopy;
	}
	
	/**
	 * 파일 내용 변경 (경로, 변경할 내용)
	 * 
	 * @param filePath
	 * @param text
	 * @return
	 */
	public int updateFile(String filePath, String text) {
		int rst = 0;
		
		if (!StringUtils.isEmpty(filePath) && !StringUtils.isEmpty(text));
		else {
			rst = -1;
			return rst;
		}
		
		BufferedWriter bw = null;
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				rst = -1;
				return rst;
			}
			
			bw = new BufferedWriter(new FileWriter(file));
			bw.write(text);
			bw.close();
			
			rst = 1;
		}
		catch (Exception ex) {
			ex.getStackTrace();
			log.error("{}", ex);
			rst = -1;
		}
		finally {
			if (null != bw) {
				try {
					bw.close();
				}
				catch (Exception ex) {
					ex.getStackTrace();
					log.error("{}", ex);
				}
			}
		}
		
		return rst;
	}
	
}
