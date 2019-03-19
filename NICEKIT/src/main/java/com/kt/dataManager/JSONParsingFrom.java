package com.kt.dataManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.kt.dataDao.ErrorCodeList;
import com.kt.dataDao.InsertDataTo;
import com.kt.dataDao.OwnServiceList;
import com.kt.dataDao.SelectDataTo;
import com.kt.dataForms.BaseDictionarySet;
import com.kt.dataForms.BaseExcelForm;
import com.kt.dataForms.BaseIntentInfoForm;
import com.kt.dataForms.BaseSvcForm;
import com.kt.dataForms.BaseVenderSvcForm;
import com.kt.dataForms.ReqCreateVender;
import com.kt.dataForms.ReqDataForm;
import com.kt.dataForms.ReqSvcCodeForm;
import com.typesafe.config.ConfigException.Parse;

public class JSONParsingFrom {

	JSONParser parser = new JSONParser();
	JSONSerializerTo serializer = new JSONSerializerTo();
	HTMLSerializerTo htmlSerializer = new HTMLSerializerTo();

	public JSONObject parsingCreateDomain(String response) {

		InsertDataTo insertTo = new InsertDataTo();
		JSONObject res = new JSONObject();

		try {

			JSONObject obj = (JSONObject) parser.parse(response);

			String dn = obj.get("domainName").toString();

			insertTo.insertDomainList(dn);

			res.put("resCode", "2001");
			res.put("resMsg", "정상적으로 등록되었습니다");

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return res;

	}

	public JSONObject parsingCreateVenderTemplate(String response) {

		ReqCreateVender venderInfo = new ReqCreateVender();
		UITemplateController uiController = new UITemplateController();
		JSONParsingFrom parsingFrom = new JSONParsingFrom();
		JSONSerializerTo serializerTo = new JSONSerializerTo();

		InsertDataTo insertTo = new InsertDataTo();

		JSONObject res = new JSONObject();
		String resCode;

		try {

			JSONObject obj = (JSONObject) parser.parse(response);

			JSONObject resObj = uiController.createVenderDir(obj.get("vendor").toString(), obj.get("urlPath").toString());

			if (resObj.get("code").toString() == "409") {

				res = serializerTo.resConflict("409");

				return res;

			} else if (resObj.get("code").toString() == "400") {

				res = serializerTo.resNotFoundTemplate("복사할 원본 디렉토리가 존재하지 않습니다");

				return res;

			} else {
				
				String temPath = uiController.extractURL(resObj.get("temPath").toString());
				String comPath = uiController.extractURL(resObj.get("comPath").toString());

				venderInfo.setDomainName(obj.get("domainName").toString());
				venderInfo.setVender(obj.get("vendor").toString());
				venderInfo.setTemplateUrl(temPath);
				venderInfo.setVenderUrl(comPath);


				resCode = insertTo.insertVenderToIndexList(venderInfo);
				
				JSONObject server = parsingFrom.getServerInfo();

				res = serializerTo.resCreateVenderPath(resCode, server.get("serverIp").toString()
						+ ":" + server.get("port").toString() + comPath);

			}




		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return res;

	}

	public void parsingTemplateInfo (String templateName) {

		InsertDataTo insertTo = new InsertDataTo();
		
		JSONObject obj = this.getServerInfo();

		String finalPath = obj.get("context").toString() + "/resources/template/" + templateName; 
				
		insertTo.insertTemplateinfo(templateName, finalPath);


	}

	public JSONObject getServerInfo () {

		JSONObject obj = new JSONObject();

		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder
				.currentRequestAttributes();

		String serverIp = attr.getRequest().getServerName();
		String port = Integer.toString(attr.getRequest().getServerPort());
		String contextRoot = attr.getRequest().getContextPath();
		
		obj.put("port", port);
		obj.put("serverIp", serverIp);
		obj.put("context", contextRoot);
		
		
		return obj;


	}





	public ArrayList<BaseDictionarySet> parsingIntentInfo(JSONArray arr) {

		ArrayList<BaseDictionarySet> dicList = new ArrayList<BaseDictionarySet>();

		for (int i = 0; i < arr.size(); i++) {

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
	public JSONObject getDictionaryList(String response) {

		JSONObject res = new JSONObject();
		JSONSerializerTo jsonSerializerTo = new JSONSerializerTo();

		try {

			JSONObject obj = (JSONObject) parser.parse(response);

			String doName = obj.get("domainName").toString();

			res = jsonSerializerTo.createJSONForIntentInfo(doName); // 현재는 도메인 이름을 받고 있으나 DB Select 시 반영하지 않음

		} catch (ParseException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return res;

	}

	/**
	 * @author : "Minwoo Ryu" [2019. 3. 16. 오 16:54:27] desc : 등록기로부터 서비스 명세를 수신 후
	 *         해당 명세를 commonks 내의 domainservicelist 테이블에 저장하고 서비스 코드 발급을 요청하기 전 등록기로
	 *         부터 수신 받은 데이터를 파싱하는 함수 파싱 후 InsertDataTo로 파싱 결과를 전달하여 serviceCode를 수신
	 * @version : 0.1
	 * @return : JSONObject
	 * @throws :
	 * @see : InsertDataTo.createServiceCodeNinsertService;
	 *      JSONSerializerTo.resConflict; JSONSerializerTo.reqServiceCodeToCreate
	 *      >>>>>>> branch 'master' of
	 *      https://github.com/zenolim89/service-description-project.git
	 * 
	 *      /**
	 * @author : "Minwoo Ryu" [2019. 3. 16. 오 16:54:27] desc : 등록기로부터 서비스 명세를 수신 후
	 *         해당 명세를 commonks 내의 domainservicelist 테이블에 저장하고 서비스 코드 발급을 요청하기 전 등록기로
	 *         부터 수신 받은 데이터를 파싱하는 함수 파싱 후 InsertDataTo로 파싱 결과를 전달하여 serviceCode를 수신
	 * @version : 0.1
	 * @return : JSONObject
	 * @throws :
	 * @see : InsertDataTo.createServiceCodeNinsertService;
	 *      JSONSerializerTo.resConflict; JSONSerializerTo.reqServiceCodeToCreate
	 * 
	 * @param response (등록기로 받은 데이터, String 타입)
	 * @return
	 */
	public JSONObject getServiceCodeTo(String response) {

		InsertDataTo insertTo = new InsertDataTo();
		JSONObject res = new JSONObject();
		JSONSerializerTo jsonSerializerTo = new JSONSerializerTo();

		ReqSvcCodeForm form = new ReqSvcCodeForm();

		try {

			JSONObject obj = (JSONObject) parser.parse(response);

			form.setDomainName(obj.get("domainName").toString());
			form.setServiceDesc(obj.get("serviceDesc").toString());
			form.setServiceName(obj.get("serviceName").toString());
			form.setServiceType(obj.get("serviceType").toString());

			String serviceCode = insertTo.createServiceCodeNinsertService(form);

			if (serviceCode == "409") {

				res = jsonSerializerTo.resConflict(serviceCode);

				return res;

			} else {

				res = jsonSerializerTo.resServiceCodeToCreate(serviceCode);

				return res;

			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	public JSONObject convertIntentInfo(JSONArray intentInfo) {

		JSONObject obj = new JSONObject();

		for (int i = 0; i < intentInfo.size(); i++) {

			obj = (JSONObject) intentInfo.get(i);

		}

		return obj;

	}

	public JSONObject setAuth(String response) {

		JSONObject res = new JSONObject();

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
	 * @author : "Minwoo Ryu" [2019. 2. 7. 오전 10:35:27] desc : 도메인 사전 등록을 위한 Parser
	 * @version : 0.1
	 * @param :
	 * @return : JSONObject
	 * @throws :
	 * @see :
	 * 
	 * @param response
	 * @return
	 */
	public JSONObject setDomainDictionary(String response) {

		InsertDataTo insertTo = new InsertDataTo();

		JSONObject res = new JSONObject();

		try {

			ArrayList<BaseIntentInfoForm> dicList = new ArrayList<BaseIntentInfoForm>();
			JSONArray arr = (JSONArray) parser.parse(response);

			for (int i = 0; i < arr.size(); i++) {

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
	 * @author : "Minwoo Ryu" [2019. 2. 1. 오후 4:40:46] desc : 도메인 서비스 등록을 DB에 저장하기
	 *         위하여 클라이언트로 받은 JSON 데이터를 파싱하는 함수
	 * @version :0.1
	 * @param : response
	 * @return : JSONObject
	 * @throws :
	 * @see : BaseScvForm, InsertDataTo, ErrorCodeList
	 * 
	 * @param response
	 * @return
	 */
	public JSONObject setDomainService(String response) {

		BaseSvcForm resSvcDesc = new BaseSvcForm(); // domainservice form으로 변경 필요
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
			// resSvcDesc.setProtType(resObj.get("protocolType").toString());
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

	public JSONObject setVenderService(String response) {

		ArrayList<BaseVenderSvcForm> descList = new ArrayList<BaseVenderSvcForm>();
		InsertDataTo inserTo = new InsertDataTo();

		ErrorCodeList error = new ErrorCodeList();

		// need to define result value
		JSONObject res = new JSONObject();

		try {

			JSONObject obj = (JSONObject) parser.parse(response);
			JSONArray arr = (JSONArray) obj.get("svcList");

			for (int arrRow = 0; arrRow < arr.size(); arrRow++) {

				BaseVenderSvcForm resSvcDesc = new BaseVenderSvcForm();

				JSONObject descObj = (JSONObject) arr.get(arrRow);

				resSvcDesc.setComURL(descObj.get("comUrl").toString());
				resSvcDesc.setDataType(descObj.get("dataType").toString());
				resSvcDesc.setIntentInfo((JSONArray) descObj.get("intentInfo"));
				resSvcDesc.setInterfaceType(descObj.get("interfaceType").toString());
				resSvcDesc.setHeaderInfo((JSONArray) descObj.get("headerInfo"));
				resSvcDesc.setMethod(descObj.get("method").toString());
				resSvcDesc.setToUrl(descObj.get("toUrl").toString());
				resSvcDesc.setSvcType(descObj.get("svcType").toString());
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

		} catch (ParseException e) {
			// TODO Auto-generated catch block

			res.put("resCode", "4000");
			res.put("resMsg", e.getMessage());

			e.printStackTrace();
		}

		return res;

	}

	public JSONObject setExcelForm(String response, String FilePath) {

		ArrayList<BaseExcelForm> svcList = new ArrayList<BaseExcelForm>();
		ErrorCodeList error = new ErrorCodeList();

		// need to define result value
		JSONObject res = new JSONObject();

		try {

			JSONObject obj = (JSONObject) parser.parse(response);
			JSONArray arr = (JSONArray) obj.get("svcList");
			System.out.println(arr);

			for (int arrRow = 0; arrRow < arr.size(); arrRow++) {

				BaseExcelForm BaseExcelForm = new BaseExcelForm();
				JSONObject descObj = (JSONObject) arr.get(arrRow);

				BaseExcelForm.setServiceName(descObj.get("serviceName").toString());
				BaseExcelForm.setInvokeType(descObj.get("invokeType").toString());
				BaseExcelForm.setServiceType(descObj.get("serviceType").toString());
				BaseExcelForm.setServiceLink(descObj.get("serviceLink").toString());
				BaseExcelForm.setServiceDesc(descObj.get("serviceDesc").toString());
				BaseExcelForm.setServiceCode(descObj.get("serviceCode").toString());

				if (descObj.containsKey("intentInfo")) {
					BaseExcelForm.setIntentInfo((JSONArray) descObj.get("intentInfo"));
					JSONObject intentInfoObj = (JSONObject) BaseExcelForm.getIntentInfo().get(0);
					BaseExcelForm.setId(intentInfoObj.get("id").toString());
					BaseExcelForm.setDicList((JSONArray) intentInfoObj.get("dicList"));
					JSONArray dicListArr = (JSONArray) BaseExcelForm.getDicList();
					String dicStr = "";
					for (int dicList = 0; dicList < dicListArr.size(); dicList++) {
						JSONObject dicListObj = (JSONObject) BaseExcelForm.getDicList().get(dicList);
						dicStr += dicListObj.get("dicName").toString() + "  ";
						// dicStr += dicListObj.get("wordList").toString().trim();
					}
					System.out.println(dicStr);
					BaseExcelForm.setDicInfo(dicStr);
				}

				svcList.add(BaseExcelForm);
			}

			res.put("resCode", "2001");
			res.put("resMsg", error.getErrorCodeList().get("2001"));
			res.put("urlPath", "/resources/download/workbook.xlsx");

			CreateExcelForm ExcelForm = new CreateExcelForm(svcList, FilePath);
			ExcelForm.createSheet();
			ExcelForm.createExcelFile();

			//			inserTo.insertVenderSvcTo(descList);

		} catch (

				ParseException e) {
			// TODO Auto-generated catch block
			res.put("resCode", "4000");
			res.put("resMsg", e.getMessage());
			e.printStackTrace();
		}
		return res;
	}

}
