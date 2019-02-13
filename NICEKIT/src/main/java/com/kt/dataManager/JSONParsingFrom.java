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
import com.kt.dataForms.BaseDictionarySet;
import com.kt.dataForms.BaseIntentInfoForm;
import com.kt.dataForms.BaseSvcForm;
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

	public ArrayList<BaseDictionarySet> parsingIntentInfo (JSONArray arr) {

		ArrayList<BaseDictionarySet> dicList = new ArrayList<BaseDictionarySet>();

		for (int i=0; i < arr.size(); i++) {

			BaseDictionarySet dicSet = new BaseDictionarySet();
			JSONObject obj = (JSONObject) arr.get(i);

			dicSet.setDicName(obj.get("dicName").toString());
			JSONArray wordArr = (JSONArray) obj.get("wordList");

			for (int j = 0; j < wordArr.size(); j++) {

				JSONObject wordObj = (JSONObject) wordArr.get(j);
				dicSet.getWordList().add(wordObj.get("word").toString());

			}

			dicList.add(dicSet);

		}

		return dicList;

	}


	// 현재는 full list를 주는 형태, 해당 부분을 이재동 박사 작업 내용이랑 선택에 의하여 호출하는 내용으로 변경 필요
	public String getDictionaryList(String response) {

		String res = null;

		try {

			JSONObject obj = (JSONObject) parser.parse(response);

			String doName = obj.get("domainName").toString();

			res = htmlSerializer.createHTMLForIntentInfo(doName); 			// 현재는 도메인 이름을 받고 있으나 DB Select 시 반영하지 않음

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


		InsertDataTo insertTo = new InsertDataTo();

		JSONObject res = new JSONObject();

		try {

			ArrayList<BaseIntentInfoForm> dicList = new ArrayList<BaseIntentInfoForm>();
			JSONArray arr = (JSONArray) parser.parse(response);	

			for (int i =0; i < arr.size(); i++) {

				BaseIntentInfoForm dicForm = new BaseIntentInfoForm();

				JSONObject obj = (JSONObject) arr.get(i);


				// read last number of table and then add number

				dicForm.setIntentName(obj.get("id").toString());
				dicForm.setDesc(obj.get("desc").toString());
				dicForm.setArr((JSONArray) obj.get("dicList"));

				dicList.add(dicForm);

			}

			insertTo.insertDomainIntent(dicList);

			res.put("resCode", "2001");
			res.put("resMsg", "정상적으로 등록되었습니다");


		} catch (ParseException e) {
			// TODO Auto-generated catch block

			res.put("resCode", "4000");
			res.put("resMsg", e.getMessage());

			e.printStackTrace();
		}


		return res;


	}




	/**
	 * @author	: "Minwoo Ryu" [2019. 2. 1. 오후 4:40:46]
	 * desc	: 도메인 서비스 등록을 DB에 저장하기 위하여 클라이언트로 받은 JSON 데이터를 파싱하는 함수
	 * @version	:0.1
	 * @param	: response
	 * @return 	: JSONObject 
	 * @throws 	: 
	 * @see		: BaseScvForm, InsertDataTo, ErrorCodeList

	 * @param response
	 * @return
	 */
	public JSONObject setDomainService(String response) {

		BaseSvcForm resSvcDesc = new BaseSvcForm(); //domainservice form으로 변경 필요
		InsertDataTo insData = new InsertDataTo();
		ErrorCodeList error = new ErrorCodeList();

		// need to define result value
		JSONObject res = new JSONObject();


		try {

			JSONObject resObj = (JSONObject) parser.parse(response);

			resSvcDesc.setComURL(resObj.get("comUrl").toString());
			resSvcDesc.setDataType(resObj.get("dataType").toString());
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
			resSvcDesc.setTestURL(resObj.get("testUrl").toString());
			resSvcDesc.setDomainName(resObj.get("domainName").toString());
			resSvcDesc.setDomainId(resObj.get("domainId").toString());

			insData.insertDomainSvcTo(resSvcDesc);

			res.put("resCode", "2001");
			res.put("resMsg", error.getErrorCodeList().get("2001"));


		} catch (ParseException e) {
			// TODO Auto-generated catch block

			res.put("resCode", "4000");
			res.put("resMsg", e.getMessage());

			e.printStackTrace();
		}


		return res;
	}

	public JSONObject setVenderService (String response) {

		ArrayList<BaseSvcForm> descList = new ArrayList<BaseSvcForm>();
		InsertDataTo inserTo = new InsertDataTo();

		ErrorCodeList error = new ErrorCodeList();

		// need to define result value
		JSONObject res = new JSONObject();

		try {

			JSONObject obj = (JSONObject) parser.parse(response);
			JSONArray arr = (JSONArray) obj.get("svcList");

			for (int arrRow = 0; arrRow < arr.size(); arrRow++) {

				BaseSvcForm resSvcDesc = new BaseSvcForm();

				JSONObject descObj = (JSONObject) arr.get(arrRow);

				resSvcDesc.setComURL(descObj.get("comUrl").toString());
				resSvcDesc.setDataType(descObj.get("dataType").toString());
				resSvcDesc.setIntentInfo((JSONArray) descObj.get("intentInfo"));
				resSvcDesc.setInterfaceType(descObj.get("interfaceType").toString());
				resSvcDesc.setHeaderInfo((JSONArray) descObj.get("headerInfo"));
				resSvcDesc.setMethod(descObj.get("method").toString());
				resSvcDesc.setReqSpec((JSONArray) descObj.get("reqSpec"));
				resSvcDesc.setReqStructure((JSONArray) descObj.get("reqStructure"));
				resSvcDesc.setResSpec((JSONArray) descObj.get("resSpec"));
				resSvcDesc.setResStructure((JSONArray) descObj.get("resStructure"));
				resSvcDesc.setServiceCode(descObj.get("serviceCode").toString());
				resSvcDesc.setServiceDesc(descObj.get("serviceDesc").toString());
				resSvcDesc.setTestURL(descObj.get("testUrl").toString());
				resSvcDesc.setDomainName(descObj.get("domainName").toString());
				resSvcDesc.setDomainId(descObj.get("domainId").toString());

				descList.add(resSvcDesc);
			}
			
			res.put("resCode", "2001");
			res.put("resMsg", error.getErrorCodeList().get("2001"));
			
			inserTo.insertVenderSvcTo(descList);
			
			
			
			
			
			
		}catch (ParseException e) {
			// TODO Auto-generated catch block

			res.put("resCode", "4000");
			res.put("resMsg", e.getMessage());

			e.printStackTrace();
		}

		return res;

	}



}

