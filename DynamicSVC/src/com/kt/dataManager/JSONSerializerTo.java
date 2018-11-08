package com.kt.dataManager;

import java.util.ArrayList;
import java.util.Hashtable;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.kt.dataForms.Account;
import com.kt.dataForms.OwnServiceList;

public class JSONSerializerTo {


	public JSONObject resultMsgforAuth(String id, String pw) {
		
		JSONObject resMsg = new JSONObject();
		JSONArray serviceDesc = new JSONArray();
		Hashtable<String, String> list = Account.getInstance().getAccountListTable();
		
		
		
		String matchingPw = list.get("id");
		
		if (matchingPw == null) {
			//make json object for fail
			resMsg.put("resCode", "401");
			resMsg.put("numSVC", "0");
			//
		} else if (matchingPw.equals(pw)) {
			
			resMsg.put("resCode",  "200");
			resMsg.put("numSVC",  "0");
			resMsg.put("resMsg", "Authotification is completed");
			
			
			
		} else {
			//resultMSG
		}
		
		
		
		//verification of id and pw
		// get number of services
		// get serviceCode
		// get service description

		resMsg.put("resultMsg", "Authentication completed");

		return resMsg;
	}





}
