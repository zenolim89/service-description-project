package com.kt.dataConverter;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.kt.commonUtils.Constants;

public class TempConverter {
	
	private File sourceFile;

	public void updateMenuTitle(String _vendorName, String _htmlFileName, String _titleText) {
		
		try {
			JSONObject jsonObject = this.setTitle(_vendorName, _htmlFileName, _titleText);
			
			if( jsonObject != null ) {
				
				rewriteJSON(jsonObject);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void rewriteJSON(JSONObject _jsonObejct) throws Exception {
		
		InputStream fileIn = null;
		FileOutputStream fileOut = null;
		
		fileIn = new ByteArrayInputStream(_jsonObejct.toString().getBytes("UTF-8"));
		
		fileOut = new FileOutputStream(sourceFile);
		byte[] b = new byte[4096];

		int cnt = 0;

		while ((cnt = fileIn.read(b)) != -1) {

			fileOut.write(b, 0, cnt);
		}
		
		fileIn.close();
		fileOut.close();
		
	}
	
	private JSONObject setTitle(String _vendorName, String _htmlFileName, String _titleText) throws Exception {
		
		JSONObject jsonObject = this.getTemplateJSON(_vendorName);
		
		JSONArray pageArrayList = (JSONArray) jsonObject.get("pages");
		
		for( int i = 0 ; i < pageArrayList.size(); i ++) {
			
			JSONObject jsonObjectItem = (JSONObject) pageArrayList.get(i);
					
			//메인에 있는 모든 서비스명을 ArrayList로 만들어 return한다
			if( ((String)jsonObjectItem.get("path")).equals(_htmlFileName)) {
				jsonObjectItem.put("name", _titleText);
				//return jsonObjectItem;
			}
			
		}
		
		return jsonObject;
		
	}
	
	private JSONObject getTemplateJSON(String _vendorName) throws IOException, ParseException {
		
		//File sourceFile = new File(Constants.EXTERNAL_FOLDER_REALPATH + Constants.EXTERNAL_FOLDER_URLPATH_VENDORS+ File.separator + _templateName + File.separator + "template.json");
		
		sourceFile = new File(Constants.EXTERNAL_FOLDER_REALPATH + Constants.EXTERNAL_FOLDER_URLPATH_TEMP
				+ File.separator + _vendorName + File.separator + "template.json");
		
		System.out.println(String.format("sourceFile : [%s]", sourceFile.getAbsolutePath()));
		
		byte[] encoded = Files.readAllBytes(Paths.get(sourceFile.getAbsolutePath()));
		
		String templateJSONString = new String(encoded, "UTF-8");
		
		return (JSONObject) new JSONParser().parse(templateJSONString);
	}
}
