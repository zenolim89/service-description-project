package com.kt.dataManager;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.kt.dataDao.ErrorCodeList;
import com.kt.dataDao.InsertDataTo;
import com.kt.dataDao.OwnServiceList;
import com.kt.dataDao.SelectDataTo;
import com.kt.dataForms.DictionaryForm;
import com.kt.dataForms.OwnServiceForm;
import com.kt.dataForms.ReqDataForm;

public class JSONParsingFrom {

	JSONParser parser = new JSONParser();
	JSONSerializerTo serializer = new JSONSerializerTo();
	HTMLSerializerTo htmlSerializer = new HTMLSerializerTo();

	
	
	public JSONObject getDomainList () {
		
		SelectDataTo selectTo = new SelectDataTo();
		JSONObject res = new JSONObject();
		
		JSONArray arr = selectTo.selectDomainListToCommon();
		
		res.put("domains", arr);
		
		return res;
		
	}
	
	// 현재는 full list를 주는 형태, 해당 부분을 이재동 박사 작업 내용이랑 선택에 의하여 호출하는 내용으로 변경 필요
	public String getDictionaryList(String response) {

		String res = null;

		try {

			JSONObject obj = (JSONObject) parser.parse(response);

			String doName = obj.get("domainName").toString();

			res = htmlSerializer.createHTMLForDic(doName);

		} catch (ParseException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return res;

	}
	
	public JSONObject convertIntentInfo (JSONArray intentInfo) {
		
		JSONObject obj = new JSONObject();
				
		for (int i=0; i < intentInfo.size(); i++) {
			
			obj = (JSONObject) intentInfo.get(i);
			
		}
		
		return obj;
		
	}
	
	
	public JSONObject setAuth(String response) {
		JSONObject res = null;
		try {
			JSONObject jsonObj = (JSONObject) parser.parse(response);
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
	
	
	/**
	 * @author	: "Minwoo Ryu" [2019. 2. 7. 오전 10:35:27]
	 * desc	: 도메인 사전 등록을 위한 Parser
	 * @version	: 0.1
	 * @param	: 
	 * @return 	: JSONObject 
	 * @throws 	: 
	 * @see		: 
	
	 * @param response
	 * @return
	 */
	public JSONObject setDomainDictionary (String response) {
		
		DictionaryForm dicForm = new DictionaryForm();
		InsertDataTo insertTo = new InsertDataTo();
		
		JSONObject res = new JSONObject();
		
		try {
			
			ArrayList<DictionaryForm> dicList = new ArrayList<DictionaryForm>();
			JSONArray arr = (JSONArray) parser.parse(response);	
			
			for (int i =0; i < arr.size(); i++) {
				
				JSONObject obj = (JSONObject) arr.get(i);
				
				
				// read last number of table and then add number
								
				dicForm.setIntentName(obj.get("id").toString());
				dicForm.setDesc(obj.get("desc").toString());
				dicForm.setArr((JSONArray) obj.get("dicList"));
				
				dicList.add(dicForm);
				
			}
			
			insertTo.insertIntents(dicList);
		
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return res;
		
		
	}
	

	/**
	 * @author	: "Minwoo Ryu" [2019. 2. 1. 오후 4:40:46]
	 * desc	: 현재는 데모용으로 하기의 데이터를 공통 도메인으로 보냄, 이후에는 내용 명칭등을 변경해주어야 함
	 * @version	:
	 * @param	: 
	 * @return 	: JSONObject 
	 * @throws 	: 
	 * @see		: 
	
	 * @param response
	 * @return
	 */
	public JSONObject setDomainService(String response) {
		
		OwnServiceForm resSvcDesc = new OwnServiceForm(); //domainservice form으로 변경 필요
		InsertDataTo insData = new InsertDataTo();
		ErrorCodeList error = new ErrorCodeList();
		
		// need to define result value
		JSONObject res = null;
		
		
		try {
			
			JSONObject resObj = (JSONObject) parser.parse(response);
			
			resSvcDesc.setComURL(resObj.get("comURL").toString());
			resSvcDesc.setDataType(resObj.get("datatype").toString());
			resSvcDesc.setIntentInfo((JSONArray) resObj.get("intentInfo"));
			resSvcDesc.setInterfaceType(resObj.get("interfaceType").toString());
			resSvcDesc.setHeaderInfo((JSONArray) resObj.get("headerInfo"));
			resSvcDesc.setMethod(resObj.get("method").toString());
//			resSvcDesc.setProtType(resObj.get("protocolType").toString());
			resSvcDesc.setReqSpec((JSONArray) resObj.get("reqSpec"));
			resSvcDesc.setReqStructure((JSONArray) resObj.get("reqStructure"));
			resSvcDesc.setResSpec((JSONArray) resObj.get("resSpec"));
			resSvcDesc.setResStructure((JSONArray) resObj.get("resStructure"));
			resSvcDesc.setServiceCode(resObj.get("serviceCode").toString());
			resSvcDesc.setServiceDesc(resObj.get("serviceDesc").toString());
			resSvcDesc.setTestURL(resObj.get("testURL").toString());
			resSvcDesc.setUserAuth(resObj.get("userAuth").toString());
			
			insData.insertSVCDesc(resSvcDesc);
			
			
			
			res.put("resCode", "2001");
			res.put("resMsg", error.getErrorCodeList().get("2001"));
			
			// send to db interface
			
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return res;
	}

}
