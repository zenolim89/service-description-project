package com.kt.dataManager;

import java.util.ArrayList;
import java.util.Hashtable;

import org.json.simple.JSONObject;

import com.kt.dataForms.Account;
import com.kt.dataForms.OwnServiceList;

public class JSONSerializerTo {


	public JSONObject resultMsgforAuth(String id, String pw) {
		
		JSONObject resMsg = new JSONObject();
		
		Hashtable<String, String> list = Account.getInstance().getAccountListTable();
		
		String matchingPw = list.get("id");
		
		if (matchingPw == null) {
			//resutMag
			//
		} else if (matchingPw.equals(pw)) {
			
			ArrayList<OwnServiceList> ownList = OwnServiceList.getInstance().getOwnList();
			
			resMsg.put("resCode", "sucess");
			resMsg.put("numSVC", ownList.size());
			resMsg.put("resultMsg","총" + ownList.size() + "개의 서비스가 등록되어 있습니다.");
			
			
			
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
