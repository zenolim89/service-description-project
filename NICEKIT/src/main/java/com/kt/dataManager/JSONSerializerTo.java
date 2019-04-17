package com.kt.dataManager;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.kt.commonUtils.Constants;
import com.kt.dataDao.Account;
import com.kt.dataDao.ErrorCodeList;
import com.kt.dataDao.SelectDataTo;
import com.kt.dataForms.KeyValueFormatForJSON;
import com.kt.dataForms.BaseDictionarySet;
import com.kt.dataForms.BaseIntentInfoForm;
import com.kt.dataForms.BaseSvcForm;
import com.kt.dataForms.GetSpecInfoDataForm;
import com.kt.dataForms.ReqDataForm;
import com.kt.dataForms.SubValueArrKeyValeTypeFormat;

public class JSONSerializerTo {

	/**
	 * @author : "Minwoo Ryu" [2019. 3. 18. 오후 3:24:25] desc : 생성기로부터 요청 받은
	 *         venderlist 값을 JSON return 규격에 맞춘 포멧을 생성 이를 위하여 venderlist 값을 DB를 통하여
	 *         검색하기 위한 메소드를 호출
	 * @version :
	 * @param :
	 * @return : JSONObject
	 * @throws :
	 * @see : SelectDataTo.venderlistInDomain(String domainName)
	 * 
	 * @param domainName
	 * @return
	 */
	public JSONObject resVendorList(String domainName) {
		SelectDataTo selectTo = new SelectDataTo();
		JSONObject resObj = new JSONObject();
		JSONObject dataObj = new JSONObject();
		JSONArray arr = new JSONArray();
		List<Row> list = selectTo.selectVendorlistInDomain(domainName);
		if (list == null) {
			resObj.put("resCode", "400");
			resObj.put("resMsg", "등록된 사업장 정보가 없습니다");
			dataObj.put("vendorList", arr);
			resObj.put("resData", dataObj);
			return resObj;
		} else {
			resObj.put("resCode", "200");
			resObj.put("resMsg", "성공");
			for (Row row : list)
				arr.add(row.getString("vendorname"));
			dataObj.put("vendorList", arr);
			resObj.put("resData", dataObj);
			return resObj;
		}
	}

	public JSONObject resTempList(String domainName) {
		SelectDataTo selectTo = new SelectDataTo();
		JSONObject resObj = new JSONObject();
		JSONObject dataObj = new JSONObject();
		JSONArray arr = new JSONArray();
		List<Row> list = selectTo.selectTemplistInDomain(domainName);
		if (list == null) {
			resObj.put("resCode", "400");
			resObj.put("resMsg", "편집 할 사업장 정보가 없습니다");
			dataObj.put("tempList", arr);
			resObj.put("resData", dataObj);
			return resObj;
		} else {
			resObj.put("resCode", "200");
			resObj.put("resMsg", "성공");
			for (Row row : list) {
				JSONObject tempObj = new JSONObject();
				tempObj.put("vendorName", row.getString("vendorname"));
				tempObj.put("specName", row.getString("specname"));
				arr.add(tempObj);
			}
			dataObj.put("tempList", arr);
			resObj.put("resData", dataObj);
			return resObj;
		}
	}

	public JSONObject resDescription(String code, String msg) {
		JSONObject obj = new JSONObject();
		obj.put("resCode", code);
		obj.put("resMsg", msg);
		return obj;
	}

	// return value 만들어야 함 2019.03.20
	public JSONObject resSpecInfo(String domainName, String specName) {
		SelectDataTo selectTo = new SelectDataTo();
		ArrayList<GetSpecInfoDataForm> list = new ArrayList<GetSpecInfoDataForm>();
		GetSpecInfoToSupportTool tool = new GetSpecInfoToSupportTool();
		JSONObject res = new JSONObject();
		JSONObject resData = new JSONObject();
		JSONObject specDesc = new JSONObject();
		JSONArray svcList = new JSONArray();
		try {
			res = this.resDescription("200", "도메인 목록 조회 성공");
			list = tool.resSpecData(specName);
			for (GetSpecInfoDataForm form : list) {
				JSONObject desc = new JSONObject();
				JSONObject intentInfoObj = new JSONObject();
				JSONArray intentInfoArr = new JSONArray();
				desc.put("serviceName", form.getServiceName());
				desc.put("invokeType", form.getInvokeType());
				desc.put("serviceType", form.getServiceType());
				desc.put("serviceLink", form.getServiceLink());
				desc.put("serviceDesc", form.getServiceDesc());
				desc.put("serviceCode", form.getServiceCode());
				JSONArray dicList = form.getWordList();
				intentInfoObj.put("id", form.getIntenName());
				intentInfoObj.put("dicList", dicList);
				intentInfoArr.add(intentInfoObj);
				desc.put("intentInfo", intentInfoArr);
				svcList.add(desc);
			}
			specDesc.put("specDesc", svcList);
			res.put("resData", specDesc);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return res;
	}

	public ArrayList<String> resWordInfo(String domainName, String specName, String serviceName) {
		JSONParser parser = new JSONParser();
		ArrayList<GetSpecInfoDataForm> list = new ArrayList<GetSpecInfoDataForm>();
		GetSpecInfoToSupportTool tool = new GetSpecInfoToSupportTool();
		ArrayList<String> res = new ArrayList<String>();
		JSONArray rawdata = new JSONArray();
		JSONArray tempdata = new JSONArray();
		JSONObject tempObj = new JSONObject();
		JSONArray result = new JSONArray();
		try {
			list = tool.resSpecData(specName);
			for (GetSpecInfoDataForm form : list) {
				if (serviceName.equals(form.getServiceName()))
					rawdata = form.getWordList();
			}
			tempdata = (JSONArray) rawdata.get(0);
			tempObj = (JSONObject) tempdata.get(0);
 			result = (JSONArray) parser.parse(tempObj.get("wordList").toString());
			
			for(int i=0;i < result.size();i++){
			    res.add(result.get(i).toString());
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return res;
	}

	public JSONObject resNotFoundTemplate(String msg) {
		JSONObject res = new JSONObject();
		JSONObject dataObj = new JSONObject();
		JSONArray arr = new JSONArray();
		res = this.resDescription("400", msg);
		res.put("resData", arr);
		return res;
	}

	public JSONObject resBadRequest() {
		JSONObject res = new JSONObject();
		JSONObject dataObj = new JSONObject();
		JSONArray arr = new JSONArray();
		res = this.resDescription("400", "잘못된 요청");
		res.put("resData", arr);
		return res;
	}

	public JSONObject resultMsgforAuth(String id, String pw) {
		ErrorCodeList errorList = new ErrorCodeList();
		JSONObject resMsg = new JSONObject();
		JSONObject serviceDesc = new JSONObject();
		JSONArray serviceList = new JSONArray();
		Hashtable<String, String> list = Account.getInstance().getAccountListTable();
		String authPw = list.get(id);
		if (authPw == null) {
			resMsg.put("resCode", errorList.getErrorCodeList().get("notFoundAuth").toString());
			resMsg.put("ServiceNum", null);
			resMsg.put("resMeg", "해당 계정을 찾을 수 없습니다.");
			resMsg.put("regiServiceInfo", null);
		} else if (!(authPw.equals(pw))) {
			resMsg.put("resCode", errorList.getErrorCodeList().get("authFailed").toString());
			resMsg.put("ServiceNum", null);
			resMsg.put("resMeg", "비밀번호가 일치하지 않습니다.");
			resMsg.put("regiServiceInfo", null);
		} else if (authPw.equals(pw)) {

		}
		return resMsg;
	}

	public JSONObject resSpecList(String domainName) {
		SelectDataTo selectTo = new SelectDataTo();
		JSONObject obj = new JSONObject();
		JSONObject dataObj = new JSONObject();
		JSONArray arr = new JSONArray();
		List<Row> list = selectTo.selectSpecList(domainName);
		if (list == null) {
			obj.put("resCode", "404");
			obj.put("resMsg", "요청하신 도메인에 사용가능한 규격이 없습니다");
			obj.put("resData", arr);
			return obj;
		} else {
			for (Row row : list) {
				arr.add(row.getString("specname"));
			}
			obj.put("resCode", "200");
			obj.put("resMSg", "성공");
			dataObj.put("specList", arr);
			obj.put("resData", dataObj);
			return obj;
		}
	}

	public JSONObject resTemplateList(String domainName) {
		SelectDataTo selectTo = new SelectDataTo();
		JSONObject dataObj = new JSONObject();
		JSONArray arr = new JSONArray();
		List<Row> list = selectTo.selectTemplateList(domainName);
		if (list == null) {
			JSONObject res = this.resDescription("404", "사용가능한 UI 템플릿이 없습니다");
			res.put("resData", arr);
			return res;
		} else {
			for (Row row : list) {
				arr.add(row.getString("templatename"));
			}
			JSONObject res = this.resDescription("200", "성공");
			dataObj.put("templateList", arr);
			res.put("resData", dataObj);
			return res;
		}
	}

	public JSONObject resCreateDirPath(String code, String path, String specName) {
		JSONObject res = new JSONObject();
		JSONObject dataObj = new JSONObject();
		res.put("resCode", code);
		res.put("resMsg", "성공");
		dataObj.put("urlPath", path);
		dataObj.put("specName", specName);
		res.put("resData", dataObj);
		return res;
	}

	public JSONObject resTempDirPath(String code, String path) {
		JSONObject res = new JSONObject();
		JSONObject dataObj = new JSONObject();
		res.put("resCode", code);
		res.put("resMsg", "성공");
		dataObj.put("urlPath", path);
		res.put("resData", dataObj);
		return res;
	}

	public JSONObject resPreView(String path) {
		JSONObject obj = this.resDescription("200", "성공");
		obj.put("resData", path);
		return obj;
	}

	public JSONObject resConflict(String code, String msg) {
		JSONObject obj = new JSONObject();
		JSONArray arr = new JSONArray();
		obj.put("resCode", code);
		obj.put("resMsg", msg);
		obj.put("resData", arr);
		return obj;
	}

	public JSONObject resSuccess() {
		JSONObject obj = new JSONObject();
		obj.put("resCode", "200");
		obj.put("resMsg", "성공");
		return obj;
	}

	public JSONObject resServiceCodeToCreate(String code) {
		JSONObject obj = new JSONObject();
		JSONObject subObj = new JSONObject();
		obj.put("resCode", "201");
		obj.put("resMsg", "서비스 코드 발행 성공");
		subObj.put("serviceCode", code);
		obj.put("resData", subObj);
		return obj;
	}

	public JSONObject resDomanIntentNameList() throws ParseException {
		SelectDataTo selectTo = new SelectDataTo();
		ArrayList<String> resList = selectTo.selectIntentNameList(Constants.CASSANDRA_KEYSPACE_COMMON,
				Constants.CASSANDRA_TABLE_INTENTINFO);
		JSONArray arr = new JSONArray();
		JSONObject obj = new JSONObject();
		for (String name : resList) {
			arr.add(name);
		}
		obj.put("intentList", arr);
		return obj;
	}

	@SuppressWarnings("unchecked")
	public JSONObject resDomainList(ResultSet resSet) {
		JSONArray resArr = new JSONArray();
		JSONObject res = new JSONObject();
		JSONObject resData = new JSONObject();
		List<Row> resList = resSet.all();
		for (Row domain : resList) {
			resArr.add(domain.getString("domainname"));
		}
		resData.put("domainList", resArr);
		if (resArr.size() == 0) {
			res.put("resCode", "404");
			res.put("resMsg", "도메인 정보가 없습니다");
			res.put("resData", resData);
			return res;
		}
		res.put("resCode", "200");
		res.put("resMsg", "성공");
		res.put("resData", resData);
		return res;
	}

	public JSONObject createJSONForIntentInfo(String intentName) {
		SelectDataTo selectTo = new SelectDataTo();
		JSONParsingFrom parsingFrom = new JSONParsingFrom();
		JSONObject obj = new JSONObject();
		String ksName = "commonks";
		String targetTable = "intentInfo";
		String reqIntentName = intentName;
		String responseHTML = "";
		ArrayList<BaseIntentInfoForm> resList;
		try {
			resList = selectTo.selectIntentInfo(ksName, targetTable, reqIntentName);
			JSONArray list = new JSONArray();
			for (BaseIntentInfoForm form : resList) {
				ArrayList<BaseDictionarySet> dicList = parsingFrom.parsingIntentInfo(form.getArr());
				JSONObject dicListObj = new JSONObject();
				for (BaseDictionarySet set : dicList) {
					JSONArray wordList = new JSONArray();
					for (String word : set.getWordList()) {
						wordList.add(word);
					}
					dicListObj.put("dicName", set.getDicName());
					dicListObj.put("wordList", wordList);
				}
				list.add(dicListObj);
				obj.put("dicList", list);
			}
		} catch (Exception e) {
			e.getMessage();
		}
		System.out.println("[DEBUG 동적 HTML코드 (도메인 사전) 생성 결과]: \n" + responseHTML + "\n");
		return obj;
	}

	public JSONArray getDialog(BaseSvcForm form, String userAuth) {
		// 기존 hash table에서 사전을 불러온 내용을
		// DB에서 가지고 오는 내용으로 변경 필요
		// Hashtable<String, ArrayList<String>> tempList = new Hashtable<String,
		// ArrayList<String>>();
		//
		JSONArray resArr = new JSONArray();
		//
		//
		//
		// tempList = form.getRefDialog();
		//
		// Set <String> keys = tempList.keySet();
		//
		// Iterator<String> iter = keys.iterator();
		//
		// while (iter.hasNext()) {
		//
		// JSONObject tempObj = new JSONObject();
		//
		// String keyName = iter.next();
		// ArrayList<String> tempValueList = tempList.get(keyName);
		// JSONArray tempArr = new JSONArray();
		//
		// for (int i=0; i < tempValueList.size(); i++) {
		// tempArr.add(tempValueList.get(i));
		// }
		// tempObj.put("dicName", keyName);
		// tempObj.put("word", tempArr);
		// resArr.add(tempObj);
		//
		// System.out.println("[DEBUG: 검색된 사용자 단어사전] 사전명 : " + keyName + " 단어: " +
		// tempList.get(keyName));
		//
		// }
		return resArr;
	}

	// 등록 시 사전 처리
	public Hashtable<String, ArrayList<String>> regiDialog(JSONObject obj) {
		Hashtable<String, ArrayList<String>> tempList = new Hashtable<String, ArrayList<String>>();
		Set<String> listKeys = obj.keySet();
		Iterator<String> iter = listKeys.iterator();
		while (iter.hasNext()) {
			String curKeyName = iter.next();
			JSONArray arr = (JSONArray) obj.get(curKeyName);
			ArrayList<String> arrVar = new ArrayList<String>();
			for (int i = 0; i < arr.size(); i++) {
				arrVar.add(arr.get(i).toString());
			}
			tempList.put(curKeyName, arrVar);
			System.out.println("[DEBUG: 등록된 사용자 단어사전] 사전명: " + curKeyName + ", 단어: " + arrVar.toString());
		}
		return tempList;
	}

	// 등록 시 jsonArray 처리
	public ReqDataForm regiJSONArrayforObjType(ReqDataForm dataLine, JSONArray subArr) {
		KeyValueFormatForJSON arrObj = new KeyValueFormatForJSON();
		for (int i = 0; i < subArr.size(); i++) {
			JSONObject idxObj = (JSONObject) subArr.get(i);
			Set<String> idxObjKey = idxObj.keySet();
			Iterator<String> iter = idxObjKey.iterator();
			while (iter.hasNext()) {
				String curKeyName = iter.next();
				// subArr object 타입이 key + value[string] 타입일 경우,
				if (idxObj.get(curKeyName).getClass().getSimpleName().equals("String")) {
					arrObj.setKey(curKeyName);
					arrObj.setValue(idxObj.get(curKeyName).toString());
					dataLine.getSubArrObjectType().add(arrObj);
					// dataLine.getSubArrObjectType().put(curKeyName,
					// idxObj.get(curKeyName).toString());
					// subArr object 타입이 key + value[object] 타입일 경우,
				} else if (idxObj.get(curKeyName).getClass().getSimpleName().equals("JSONArray")) {
					JSONArray idxVarArr = (JSONArray) idxObj.get(curKeyName);
					SubValueArrKeyValeTypeFormat subVar = new SubValueArrKeyValeTypeFormat();
					for (int j = 0; j < idxVarArr.size(); j++) {
						Hashtable<String, String> temp = new Hashtable<String, String>();
						JSONObject idxVarObj = (JSONObject) idxVarArr.get(j);
						Set<String> curVarKeys = idxVarObj.keySet();
						Iterator<String> subIter = curVarKeys.iterator();
						while (subIter.hasNext()) {
							String curVarKeyName = subIter.next();
							temp.put(curVarKeyName, idxVarObj.get(curVarKeyName).toString());
						}
						subVar.getVarObjList().add(temp);
					}
					subVar.setKey(curKeyName);
					dataLine.getSubValueArrObjType().add(subVar);
				}
			}
		}
		return dataLine;
	}
}
