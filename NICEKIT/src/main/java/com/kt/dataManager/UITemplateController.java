package com.kt.dataManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.http.protocol.RequestConnControl;
import org.json.simple.JSONObject;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.datastax.driver.core.Row;
import com.kt.dataDao.SelectDataTo;

public class UITemplateController {
	
		
	public String getAttribute () {
		
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder
				.currentRequestAttributes();
		String realPath = attr.getRequest().getRealPath("/");
				
//				.getContextPath();
		
//		contextPath = contextPath.getClass().getResource("/").getPath();
		
		return realPath;
		
		}
	
	/**
	 * @author	: "Minwoo Ryu" [2019. 3. 20. 오후 1:55:40]
	 * desc	: 아래 리턴 규격 JSON이 아닌 다른 형태로 전달 방법 고민
	 * @version	:
	 * @param	: 
	 * @return 	: JSONObject 
	 * @throws 	: 
	 * @see		: 
	
	 * @param venderName
	 * @param urlPath
	 * @return
	 */
	public JSONObject createVenderDir (String venderName, String urlPath) {
		
		SelectDataTo selectTo = new SelectDataTo();
		JSONParsingFrom parsingFrom = new JSONParsingFrom();
		JSONObject obj = new JSONObject();
		
		JSONObject server = parsingFrom.getServerInfo();
		
		String resPathHeader = "http://" + server.get("context").toString()
		+ "/resources/vendors/";
				
		String templateName = null;
		String dn = venderName;
		
		String sourcePath = this.extractURL(urlPath);
		
		List<Row>list = selectTo.selectTemplate(sourcePath);
				
		for (Row row : list) {
			
			templateName = row.getString("templatename");
			
		}
				
//		String sourcePath = this.extractURL(urlPath);
		
		
		String realPath = this.getAttribute();
		
		File sourceFile = new File (realPath + File.separator + "resources" + File.separator + "template" + File.separator + templateName);
		File targetFile = new File (realPath + File.separator + "resources" + File.separator + "vendors" + File.separator + dn);
		
		
		System.out.println((!sourceFile.exists()));
		
		
		if (!sourceFile.exists()) {
			
			obj.put("code", "400");
						
			return obj;
			
		} else if (targetFile.exists()) {
			
			obj.put("code", "409");
			return obj;
			
		} else {
			
			targetFile.mkdirs();
			this.copyTemplate(sourceFile, targetFile);
			
			resPathHeader = resPathHeader + dn;
			
			obj.put("code", "201");
			obj.put("comPath", resPathHeader);
			obj.put("temPath", urlPath);
			
			return obj;
			
		}
		
		
	}
	
	public String extractURL (String urlPath) {
		
		String extractPath = null;
		
		try {
			
			URL aURL = new URL(urlPath);
			
			extractPath = aURL.getPath();
			
			
			
		} catch (MalformedURLException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();

			
		}
		
		return extractPath;
		
	}
	
	
	public void copyTemplate(File sourceFile, File targetFile) {
		
		File[] fileList = sourceFile.listFiles();
		
		for (File file : fileList ) {
			
			File temp = new File (targetFile.getAbsolutePath() + File.separator + file.getName());
			
			if (file.isDirectory()) {
				
				temp.mkdirs();
				
				this.copyTemplate(file , temp);
								
			} else {
				
				FileInputStream fileIn = null;
				FileOutputStream fileOut = null;
				
				try {
					
					fileIn = new FileInputStream(file);
					fileOut = new FileOutputStream(temp);
					
					byte[] b = new byte[4096];
					
					int cnt = 0;
					
					while ((cnt = fileIn.read(b)) != -1 ) {
						
						fileOut.write(b,  0,  cnt);
					}
					
				} catch (Exception e) {
					
					System.out.println(e.getMessage());
					
				} finally  {
					
					try {
						
						fileIn.close();
						fileOut.close();
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						
						e.printStackTrace();
						System.out.println(e.getMessage());
					}
					
				}
			}
			
			
		}
	}

}
