package com.kt.dataManager;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.http.protocol.RequestConnControl;
import org.json.simple.JSONObject;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.datastax.driver.core.Row;
import com.kt.dataConverter.HtmlConverter;
import com.kt.dataDao.SelectDataTo;

public class UITemplateController {
	
		
	public String getAttribute () {

		
				
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder
				.currentRequestAttributes();
		String realPath = attr.getRequest().getRealPath("/");
				
//				.getContextPath();
		
//		contextPath = contextPath.getClass().getResource("/").getPath();
		
		System.out.println(realPath);
		
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
		
		String realComPath = server.get("context").toString()
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
		
		File sourceFile = new File (realPath + File.separator + "WEB-INF" + File.separator + "resources" + File.separator + "template" + File.separator + templateName);
		
		System.out.println("AAAA" + realPath + File.separator + "WEB-INF" + File.separator + "resources" + File.separator + "template" + File.separator + templateName);
		
		File targetFile = new File (realPath + File.separator + "WEB-INF" + File.separator + "resources" + File.separator + "vendors" + File.separator + dn);
		
		
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
			
			realComPath = realComPath + dn;
			
			obj.put("code", "201");
			obj.put("comPath", realComPath);
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
		
		HtmlConverter htmlConverter = new HtmlConverter();
		
		File[] fileList = sourceFile.listFiles();
		
		for (File file : fileList ) {
			
			File temp = new File (targetFile.getAbsolutePath() + File.separator + file.getName());
			
			if (file.isDirectory()) {
				
				temp.mkdirs();
				
				this.copyTemplate(file , temp);
								
			} else {
				
				//주석대상 1)
				FileInputStream fileIn = null;
				//InputStream fileIn = null;
				
				FileOutputStream fileOut = null;
				
				try {
					
					//DB처리 이후에 해당 리스트 이곳에 대입
					//처리 후 주석대상 1), 2), 3) 번 주석처리 필요
					//fileIn = new ByteArrayInputStream(htmlConverter.removeItm(file, "UTF-8", _selectorMap, _itmMap),"UTF-8");
					
					//주석대상 2)
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
						
						//주석대상 3)
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
