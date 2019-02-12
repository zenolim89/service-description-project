package com.kt.serviceManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.kt.dataForms.DiscoveredServiceDESC;

public class ServiceEnabler {

	public JSONObject discoverMatchingWord (DiscoveredServiceDESC desc, String word) {
		
		JSONObject res = new JSONObject();
		
		JSONArray arr = desc.getDicList();

		for (int i=0; i < arr.size(); i++) {

			JSONObject obj = (JSONObject) arr.get(i);
			
			JSONArray wordArr = (JSONArray) obj.get("wordList");
			
			for (int j = 0; j < wordArr.size(); j++) {
				
				JSONObject wordObj = (JSONObject) wordArr.get(j);
				
				if (word.equals(wordObj.get("word").toString())) {

					res = this.createRequestData(desc, word);
					
					return res;

				} else {

					res.put("resCode", "404");
					res.put("resMsg", "해당 서비스를 찾을 수 없습니다");
					
				}
				
			}

			

		}
		
		return res;


	}
	
	/**
	 * @author	: "Minwoo Ryu" [2019. 2. 12. 오후 2:00:31]
	 * desc	: 최종적으로 해당 method는 out-bound로 정의되어야 함
	 * @version	:
	 * @param	: 
	 * @return 	: JSONObject 
	 * @throws 	: 
	 * @see		: 
	
	 * @param desc
	 * @param word
	 * @return
	 */
	public JSONObject createRequestData (DiscoveredServiceDESC desc, String word) {
		
		JSONObject obj = new JSONObject();
		
		String host = desc.getComURL();
		String method = desc.getMethod();
		
		// set을 통한 스펙값과 전송 규격값 확인 및 데이터 변환
		 
		obj.put("resCode", "201");
		obj.put("resMsg", "해당 요청을 수행하였습니다");
		
		
		return obj;
		
		
	}
	
	public void createRequestFormat (JSONArray reqFormat, JSONArray reqSpec, String word) {
		
		JSONObject specObj = new JSONObject();
		
		
		
		for (int i = 0; i < reqFormat.size(); i++ ) {
			
			specObj = (JSONObject) reqFormat.get(i);
			
			Set<String> keys = specObj.keySet(); 
			
			Iterator<String> iter = keys.iterator();
			
			while (iter.hasNext()) {
				
				String resKey = iter.next();
				
				if ( (specObj.get(resKey).getClass().getName()).equals("JSONObject")) {
					
					
					
					
				}
				
				
				
			}
			
			
		}
		
		
		
	}
	
	
	public String extractValueSpecForObj (JSONObject specObj, String key) {
		
		String valType = "spec1";
		String derivedKey = null;
		
		if (valType.equals(specObj.get(key).toString())) {
			
			derivedKey = key;
		} 
		
		return derivedKey;
	}
	


}
