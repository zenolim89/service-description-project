package com.kt.dataManager;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.datastax.driver.core.Row;
import com.kt.dataDao.DeleteDataTo;
import com.kt.dataDao.ErrorCodeList;
import com.kt.dataDao.InsertDataTo;
import com.kt.dataDao.SelectDataTo;
import com.kt.dataForms.BaseDictionarySet;
import com.kt.dataForms.BaseExcelForm;
import com.kt.dataForms.BaseIntentInfoForm;
import com.kt.dataForms.BaseSvcForm;
import com.kt.dataForms.BaseVenderSvcForm;
import com.kt.dataForms.ReqCreateVendor;
import com.kt.dataForms.ReqSvcCodeForm;

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

	public JSONObject parsingCreateWithTemplate(String response) {
		ReqCreateVendor vendorInfo = new ReqCreateVendor();
		UITemplateController uiController = new UITemplateController();
		JSONParsingFrom parsingFrom = new JSONParsingFrom();
		JSONSerializerTo serializerTo = new JSONSerializerTo();
		InsertDataTo insertTo = new InsertDataTo();
		JSONObject resObj = new JSONObject();
		String resCode;
		try {
			JSONObject obj = (JSONObject) parser.parse(response);
			JSONObject createResObj = uiController.createWithTemplate(obj.get("domainName").toString(),
					obj.get("vendorName").toString(), obj.get("urlPath").toString(), obj.get("specName").toString());
			if (createResObj.get("code").toString() == "409") {
				resObj = serializerTo.resConflict("409", "요청하신 사업장의 디렉토리가 이미 존재합니다");
				return resObj;
			} else if (createResObj.get("code").toString() == "400") {
				resObj = serializerTo.resNotFoundTemplate("복사할 원본 디렉토리가 존재하지 않습니다");
				return resObj;
			} else {
				vendorInfo.setDomainName(obj.get("domainName").toString());
				vendorInfo.setVendorName(obj.get("vendorName").toString());
				vendorInfo.setSpecName(obj.get("specName").toString());
				vendorInfo.setDirPath(createResObj.get("tempPath").toString());
				resCode = insertTo.insertTempToIndexList(vendorInfo);
				JSONObject server = parsingFrom.getServerInfo();
				resObj = serializerTo
						.resCreateDirPath(
								resCode, "http://" + server.get("serverIp").toString() + ":"
										+ server.get("port").toString() + createResObj.get("tempPath").toString(),
								obj.get("specName").toString());
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resObj;
	}

	public JSONObject parsingCreateWithVendor(String response) {
		ReqCreateVendor vendorInfo = new ReqCreateVendor();
		UITemplateController uiController = new UITemplateController();
		JSONParsingFrom parsingFrom = new JSONParsingFrom();
		JSONSerializerTo serializerTo = new JSONSerializerTo();
		InsertDataTo insertToSpecList = new InsertDataTo();
		InsertDataTo insertToTempList = new InsertDataTo();
		JSONObject res = new JSONObject();
		String resCode;
		try {
			JSONObject obj = (JSONObject) parser.parse(response);
			// insert to commonks.specindexlist
			JSONObject specResObj = insertToSpecList.insertSpecIndexTo(obj.get("vendorName").toString(),
					obj.get("domainName").toString());
			if (specResObj.get("resCode").toString() == "200") {
				JSONObject createResObj = uiController.createWithVendor(obj.get("vendorName").toString(),
						obj.get("urlPath").toString(), obj.get("comUrl").toString(), obj.get("testUrl").toString());
				if (createResObj.get("code").toString() == "409") {
					res = serializerTo.resConflict("409", "요청하신 사업장의 디렉토리가 이미 존재합니다");
					return res;
				} else if (createResObj.get("code").toString() == "400") {
					res = serializerTo.resNotFoundTemplate("복사할 원본 디렉토리가 존재하지 않습니다");
					return res;
				} else {
					vendorInfo.setDomainName(obj.get("domainName").toString());
					vendorInfo.setVendorName(obj.get("vendorName").toString());
					vendorInfo.setSpecName(obj.get("vendorName").toString());
					vendorInfo.setDirPath(createResObj.get("tempPath").toString());
					resCode = insertToTempList.insertTempToIndexList(vendorInfo);
					JSONObject server = parsingFrom.getServerInfo();
					res = serializerTo
							.resCreateDirPath(resCode,
									"http://" + server.get("serverIp").toString() + ":" + server.get("port").toString()
											+ createResObj.get("tempPath").toString(),
									obj.get("vendorName").toString());
				}
			} else
				return specResObj;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	public JSONObject resTempSaveToVendor(String response) {
		JSONSerializerTo serializerTo = new JSONSerializerTo();
		JSONParsingFrom parsingFrom = new JSONParsingFrom();
		JSONObject res = new JSONObject();
		SelectDataTo selectTo = new SelectDataTo();
		JSONObject resObj = new JSONObject();
		String tempPath = null;
		try {
			JSONObject obj = (JSONObject) parser.parse(response);
			List<Row> list = selectTo.selectTempInfo(obj.get("vendorName").toString(),
					obj.get("domainName").toString());
			if (list == null) {
				resObj.put("resCode", "400");
				resObj.put("resMsg", "저장 할 사업장 정보가 없습니다");
				return resObj;
			} else {
				for (Row row : list)
					tempPath = row.getString("dirpath");
				JSONObject server = parsingFrom.getServerInfo();
				res = serializerTo.resTempDirPath("200",
						"http://" + server.get("serverIp").toString() + ":" + server.get("port").toString() + tempPath);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	public JSONObject resTempMoveToVendor(String response) {
		ReqCreateVendor vendorInfo = new ReqCreateVendor();
		UITemplateController uiController = new UITemplateController();
		JSONParsingFrom parsingFrom = new JSONParsingFrom();
		JSONSerializerTo serializerTo = new JSONSerializerTo();
		JSONObject res = new JSONObject();
		String resCode;
		SelectDataTo selectTo = new SelectDataTo();
		InsertDataTo insertTo = new InsertDataTo();
		DeleteDataTo deleteTo = new DeleteDataTo();
		JSONObject resObj = new JSONObject();
		String tempPath = null;
		String specName = null;
		try {
			JSONObject obj = (JSONObject) parser.parse(response);
			List<Row> list = selectTo.selectTempInfo(obj.get("vendorName").toString(),
					obj.get("domainName").toString());
			if (list == null) {
				resObj.put("resCode", "400");
				resObj.put("resMsg", "배포 할 사업장 정보가 없습니다");
				return resObj;
			} else {
				for (Row row : list) {
					tempPath = row.getString("dirpath");
					specName = row.getString("specname");
				}
				resObj = uiController.moveToVendor(obj.get("vendorName").toString(), tempPath);
				if (resObj.get("code").toString() == "409") {
					res = serializerTo.resConflict("409", "요청하신 사업장의 디렉토리가 이미 존재합니다");
					return res;
				} else if (resObj.get("code").toString() == "400") {
					res = serializerTo.resNotFoundTemplate("복사할 원본 디렉토리가 존재하지 않습니다");
					return res;
				} else {
					vendorInfo.setDomainName(obj.get("domainName").toString());
					vendorInfo.setVendorName(obj.get("vendorName").toString());
					vendorInfo.setSpecName(specName);
					vendorInfo.setDirPath(resObj.get("vendorPath").toString());
					resCode = insertTo.insertVendorToIndexList(vendorInfo);
					deleteTo.deleteRowForTempIndexList(obj.get("vendorName").toString(),
							obj.get("domainName").toString());
					JSONObject server = parsingFrom.getServerInfo();
					res = serializerTo.resTempDirPath(resCode, "http://" + server.get("serverIp").toString() + ":"
							+ server.get("port").toString() + resObj.get("vendorPath").toString());
				}
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	public void parsingTemplateInfo(String templateName, String domainName) {
		InsertDataTo insertTo = new InsertDataTo();
		JSONObject obj = this.getServerInfo();
		String finalPath = obj.get("context").toString() + "/resources/template/" + templateName;
		insertTo.insertTemplateinfo(templateName, finalPath, domainName);
	}

	/**
	 * @author : "Minwoo Ryu" [2019. 3. 20. 오후 1:55:21] desc : 현재는 데이터를 JSON으로 만들고
	 *         있는데 향후 데이터 폼으로 변경 필요
	 * @version :
	 * @param :
	 * @return : JSONObject
	 * @throws :
	 * @see :
	 * 
	 * @return
	 */
	public JSONObject getServerInfo() {
		JSONObject obj = new JSONObject();
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
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
				res = jsonSerializerTo.resConflict(serviceCode, "이미 동일한 항목의 서비스가 존재합니다");
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

	public JSONObject setExcelForm(String response, String realPath, String filePath) {
		ArrayList<BaseExcelForm> svcList = new ArrayList<BaseExcelForm>();
		JSONObject res = new JSONObject();
		try {
			JSONObject obj = (JSONObject) parser.parse(response);
			String fileName = "/" + obj.get("specName").toString();
			JSONArray arr = (JSONArray) obj.get("svcList");
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
				}
				svcList.add(BaseExcelForm);
			}
			CreateExcelForm createExcelForm = new CreateExcelForm(svcList, realPath, filePath, fileName);
			res = createExcelForm.createSheet();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			res.put("resCode", "4000");
			res.put("resMsg", e.getMessage());
			e.printStackTrace();
		}
		return res;
	}
}
