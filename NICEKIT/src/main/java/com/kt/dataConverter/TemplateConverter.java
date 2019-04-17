package com.kt.dataConverter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.kt.commonUtils.Constants;
import com.kt.dataManager.JSONSerializerTo;


public class TemplateConverter {
	
	private HashMap<String,ArrayList<String>> htmlWordListMap;
	private JSONObject templateJSONObject;
	
	public TemplateConverter() {
		// TODO Auto-generated constructor stub
		htmlWordListMap = new HashMap<>();
		templateJSONObject = new JSONObject();
	}
	
	public ArrayList<String> getWordList(String _htmlName){
		
		return htmlWordListMap.get(_htmlName);
	}
	
	public boolean hasWordList(String _htmlName) {
		
		if( htmlWordListMap.get(_htmlName) != null) {
			return true;
		}
		
		return false;
	}
	
	public String getJSON() {
		
		return templateJSONObject.toString();
	}
	
	public void setTemplateConverter(String _templateName, String _domainName, String _specName) throws IOException, ParseException {
		
		/*
		 * 1. template json을 가져온다
		 * 2. serviceName 리스트 전체 획득
		 * 3. serviceName 가지고 명세에 있는지 체크
		 * 		3-1 만약 명세에 없다면 메인에서 해당 jsonObject 제거
		 * 		3-2 명세에 있다면 유지 & html이름와 명세리스트를 저장
		 */
		
		templateJSONObject.putAll(this.getTemplateJSON(_templateName, _specName));
		
		HashMap<String,Object> serviceFullListMap = this.getServiceFullListMap(templateJSONObject);
		
		this.setFullServiceMapValue(templateJSONObject, serviceFullListMap);
	
		this.setWordList(_domainName, _specName, serviceFullListMap);
		
	}
	
	public void setWordList(String _domainName, String _specName, HashMap<String, Object> _serviceFullListMap){
		
		ArrayList<String> wordList = new ArrayList<String>();
		//String serviceName = "객실용품";
		JSONSerializerTo serializerTo = new JSONSerializerTo();
		
		//wordList = serializerTo.resWordInfo(_domainName, _specName, serviceName);
		
		for( String key : _serviceFullListMap.keySet() ) {
			
			System.out.println(String.format("domainName : [%s] / specName : [%s] / serviceName : [%s]", _domainName, _specName, key));
			
			try {
			wordList= serializerTo.resWordInfo(_domainName, _specName, key);
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
			if( wordList.size() > 0) {
				
				htmlWordListMap.put((String) _serviceFullListMap.get(key), wordList);
				
			}
			else{
				this.removeItem(key);
			}
			
		}
		
		System.out.println("####################################");
		System.out.println(htmlWordListMap.toString());
		System.out.println("####################################");
		
	}
	
	
	public void removeItem(String _removeName) {
		
		JSONArray pageArrayList = (JSONArray) this.templateJSONObject.get("pages");
		
		for( int i = 0 ; i < pageArrayList.size(); i++) {
			
			JSONObject jsonObject = (JSONObject) pageArrayList.get(i);
			
			if( jsonObject.get("name").equals("메인")) {
				
				JSONArray componentsArray  = (JSONArray) jsonObject.get("components");
				
				for( int j = 0 ; j < componentsArray.size(); j++) {
					
					if( ((String)((JSONObject)componentsArray.get(j)).get("name")).equals(_removeName)) {
						
						componentsArray.remove(j);
					}

				}
			}
			
		}
		
	}

	public HashMap<String,Object> getServiceFullListMap(JSONObject _jsonObject){
		
		HashMap<String,Object> fullServiceMap = new HashMap<String,Object>();
		
		JSONArray pageArrayList = (JSONArray) _jsonObject.get("pages");
		
		for( int i = 0 ; i < pageArrayList.size(); i ++) {
			
			JSONObject jsonObject = (JSONObject) pageArrayList.get(i);
			
			//메인에 있는 모든 서비스명을 ArrayList로 만들어 return한다
			if( jsonObject.get("name").equals("메인")) {
				
				JSONArray componentsArray  = (JSONArray) jsonObject.get("components");
				
				for( int j = 0 ; j < componentsArray.size(); j++) {
					
					//fullServiceList.add(componentsArray.getJSONObject(j).getString("name"));
					
					fullServiceMap.put((String) ((JSONObject)componentsArray.get(j)).get("name"), 
							"1");
				}
			}
			
		}
		
		return fullServiceMap;
	}
	
	public void setFullServiceMapValue(JSONObject _jsonObject, HashMap<String,Object> _fullServiceMap) {
		
		JSONArray pageArrayList = (JSONArray) _jsonObject.get("pages");
		
		for( int i = 0 ; i < pageArrayList.size(); i ++) {
			
			JSONObject jsonObject = (JSONObject) pageArrayList.get(i);
			
			//메인에 있는 모든 서비스명을 ArrayList로 만들어 return한다
			if( _fullServiceMap.get((String)jsonObject.get("name")) != null) {
				
				_fullServiceMap.put((String)jsonObject.get("name"), (String)jsonObject.get("path"));
			}
			
		}
	}
	
	public JSONObject getTemplateJSON(String _templateName, String _specName) throws IOException, ParseException {
		
		//File sourceFile = new File(Constants.EXTERNAL_FOLDER_REALPATH + Constants.EXTERNAL_FOLDER_URLPATH_VENDORS+ File.separator + _templateName + File.separator + "template.json");
		
		File sourceFile = new File(Constants.EXTERNAL_FOLDER_REALPATH + Constants.EXTERNAL_FOLDER_URLPATH_TEMPLATE
				+ File.separator + _templateName + File.separator + _specName +File.separator + "template.json");
		
		byte[] encoded = Files.readAllBytes(Paths.get(sourceFile.getAbsolutePath()));
		
		String templateJSONString = new String(encoded, "UTF-8");
		
		return (JSONObject) new JSONParser().parse(templateJSONString);
	}
}
