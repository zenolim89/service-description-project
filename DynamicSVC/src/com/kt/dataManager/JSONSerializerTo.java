package com.kt.dataManager;

import java.util.ArrayList;
import java.util.Hashtable;

import org.apache.log4j.spi.ErrorCode;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.kt.dataForms.Account;
import com.kt.dataForms.ErrorCodeList;
import com.kt.dataForms.OwnServiceList;

public class JSONSerializerTo {


	public JSONObject resultMsgforAuth(String id, String pw) {
		
		ErrorCodeList errorList = new ErrorCodeList();
		
		JSONObject resMsg = new JSONObject();
		JSONObject serviceDesc = new JSONObject();
		JSONArray serviceList = new JSONArray();
		Hashtable<String, String> list = Account.getInstance().getAccountListTable();
		
		String authPw = list.get("id");

//		API format
//		{
//		 "resCode": "success"
//		 "numSVC" "4"
//		 "resultMsg" : "mg"
//		 "SVCDesc" : 
//		   {
//		     "svcName1": "name"
//		    "svcName2": "name"
//		    "svcName3": "name"
//		    "svcName4": "name"
//		     }
//		}
		
		if (authPw == null) {
			
			resMsg.put("resCode", errorList.getErrorCodeList().get("notFoundAuth").toString());
			resMsg.put("numSVC", null);
			resMsg.put("resultMsg", "해당 계정을 찾을 수 없습니다.");
			resMsg.put("svcDesc", null);
			
		} else if (authPw.equals(pw)) {
			
			ArrayList<OwnServiceList> ownlist = OwnServiceList.getInstance().getOwnList();
			ArrayList<OwnServiceList> authList = new ArrayList<OwnServiceList>();
			
			for (int i=0; i < ownlist.size(); i++) {
				if (ownlist.get(i).getAuthId().equals(id)) {
					authList.add(ownlist.get(i));
				}
			}
			
			resMsg.put("resCode", errorList.getErrorCodeList().get("ok").toString());
			resMsg.put("numSVC", authList.size());
			resMsg.put("resultMsg","총" + authList.size() + "개의 서비스가 등록되어 있습니다.");
			
			for (int j=0; j < authList.size(); j++) {
				
				serviceDesc.put(authList.get(j).getServiceCode(), authList.get(j).getServiceDesc());
			}
			
			serviceList.add(serviceDesc);
			
			resMsg.put("SVCDesc", serviceList);	
		}
		
		return resMsg;
	}
}
