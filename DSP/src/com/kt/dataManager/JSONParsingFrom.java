package com.kt.dataManager;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class JSONParsingFrom {
	
	public JSONObject setAuth (String response) {
		
		JSONObject res = null;
		JSONParser parser = new JSONParser();
		JSONSerializerTo serializer = new JSONSerializerTo();
		
		try {
			
			Object obj = parser.parse(response);
			JSONObject jsonObj = (JSONObject) obj;
			
			// check ID/PW
			
			String id = jsonObj.get("id").toString();
			String pw = jsonObj.get("pw").toString();
			
			res = serializer.resultMsgforAuth(id, pw);
			
			
		} catch (ParseException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return res;
		
	}

}
