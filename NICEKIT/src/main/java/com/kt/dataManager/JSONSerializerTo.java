package com.kt.dataManager;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.kt.dataDao.Account;
import com.kt.dataDao.ErrorCodeList;
import com.kt.dataDao.SelectDataTo;
import com.kt.dataForms.BaseDictionarySet;
import com.kt.dataForms.BaseIntentInfoForm;
import com.kt.dataForms.BaseSvcForm;
import com.kt.dataForms.KeyValueFormatForJSON;
import com.kt.dataForms.ReqDataForm;
import com.kt.dataForms.SubValueArrKeyValeTypeFormat;

public class JSONSerializerTo {


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

//			ArrayList<OwnServiceForm> ownList = OwnServiceList.getInstance().getOwnList();
//			ArrayList<OwnServiceForm> userList = new ArrayList<OwnServiceForm>();
//
//			if (ownList.size() <=0 ) {
//
//				resMsg.put("regiServiceInfo", null);
//
//			} else {
//
//				for (int i=0; i < ownList.size(); i++) {
//
//					JSONArray regiListInfo = new JSONArray();
//					
//
//					if (ownList.get(i).getUserAuth().equals(id)) {
//						
//						userList.add(ownList.get(i));
//
//						for (int j=0; j< userList.size(); j++) {
//
//							JSONArray regiDialgoList = new JSONArray();
//							JSONObject regiData = new JSONObject();
//							
//							JSONObject data = new JSONObject();
//							ThirdPartyDataFormatCreator creator = new ThirdPartyDataFormatCreator();
//
//							regiData.put("userAuth", userList.get(j).getUserAuth());
//							regiData.put("interfaceType", userList.get(j).getInterfaceType());
//							regiData.put("serviceCode", userList.get(j).getServiceCode());
//							regiData.put("refAPI", userList.get(j).getRefAPI());
//							regiData.put("intentName", userList.get(j).getIntentName());
//							regiData.put("serviceDesc", userList.get(j).getServiceDesc());
//							regiData.put("targetURL", userList.get(j).getTargetURL());
//							regiData.put("method", userList.get(j).getMethod());
//							regiData.put("dataType",userList.get(j).getDataType());
//							regiData.put("dataDefinition", userList.get(j).getDataDefinition());
//
//							data = creator.createDataformatForJOSN(userList.get(j).getDataFormat());
//							regiData.put("dataFormat", data);
//							//dialog 
//							regiData.put("refDialog", this.getDialog(userList.get(j), userList.get(j).getUserAuth()));
//							regiListInfo.add(regiData);
//						}
//					}
//					resMsg.put("regiServiceInfo", regiListInfo);
//				}
//
//			}
//			resMsg.put("resCode", errorList.getErrorCodeList().get("ok").toString());
//			resMsg.put("ServiceNum", userList.size());
//			resMsg.put("resMeg","총" + userList.size() + "개의 서비스가 등록되어 있습니다.");	
		}

		return resMsg;
	}
	
	
	public JSONObject resDomanIntentNameList () throws ParseException {
		
		SelectDataTo selectTo = new SelectDataTo();
		
		String ksName = "commonks";
		String targetTable = "intentInfo";
		
		ArrayList<String> resList = selectTo.selectIntentNameList(ksName, targetTable);
		JSONArray arr = new JSONArray();
		JSONObject obj = new JSONObject();
		
		for (String name : resList) {
			
			arr.add(name);	
		}
		
		obj.put("intentList", arr);
		
		return obj;
	}
	
	
	@SuppressWarnings("unchecked")
	public JSONArray resDomainList (List<String> domainList) {
		
		JSONArray resArr = new JSONArray();
		
		for (String table : domainList) {
			
			resArr.add(table);
			
		}
		
		return resArr;
		
	}
	
	public JSONObject createJSONForIntentInfo (String intentName) {

		SelectDataTo selectTo = new SelectDataTo();


		JSONParsingFrom parsingFrom = new JSONParsingFrom();
		JSONObject obj = new JSONObject();


		String ksName = "commonks";
		String targetTable = "intentInfo";

		String reqIntentName = intentName;
		String responseHTML ="";
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
	
	
	
	public JSONArray getDialog (BaseSvcForm form, String userAuth) {
		
		// 기존 hash table에서 사전을 불러온 내용을
		// DB에서 가지고 오는 내용으로 변경 필요
		
//		Hashtable<String, ArrayList<String>> tempList = new Hashtable<String, ArrayList<String>>();
//		
		JSONArray resArr = new JSONArray();
//		
//		
//		
//		tempList = form.getRefDialog();
//		
//		Set <String> keys = tempList.keySet();
//		
//		Iterator<String> iter = keys.iterator();
//		
//		while (iter.hasNext()) {
//			
//			JSONObject tempObj = new JSONObject();
//			
//			String keyName = iter.next();
//			ArrayList<String> tempValueList = tempList.get(keyName);
//			JSONArray tempArr = new JSONArray();
//			
//			for (int i=0; i < tempValueList.size(); i++) {
//				tempArr.add(tempValueList.get(i));
//			}
//			tempObj.put("dicName",  keyName);
//			tempObj.put("word", tempArr);
//			resArr.add(tempObj);
//			
//			System.out.println("[DEBUG: 검색된 사용자 단어사전] 사전명 : " + keyName + " 단어: " + tempList.get(keyName));
//			
//		}
		
		return resArr;
	}

	// 등록 시 사전 처리 
	public Hashtable<String, ArrayList<String>> regiDialog (JSONObject obj) {

		Hashtable<String, ArrayList<String>> tempList = new Hashtable<String, ArrayList<String>>();

		Set<String> listKeys = obj.keySet();

		Iterator<String> iter = listKeys.iterator();

		while (iter.hasNext()) {
			String curKeyName = iter.next();
			JSONArray arr = (JSONArray) obj.get(curKeyName);

			ArrayList<String> arrVar = new ArrayList<String>();
			
			for (int i=0; i < arr.size(); i++) {
				
				arrVar.add(arr.get(i).toString());
			}
			
			tempList.put(curKeyName, arrVar);
			
			System.out.println("[DEBUG: 등록된 사용자 단어사전] 사전명: " + curKeyName + ", 단어: " + arrVar.toString());
		}

		return tempList;


	}

	// 등록 시 jsonArray 처리  
	public ReqDataForm regiJSONArrayforObjType (ReqDataForm dataLine, JSONArray subArr) {

		KeyValueFormatForJSON arrObj = new KeyValueFormatForJSON();

		for (int i=0; i <subArr.size(); i++) {

			JSONObject idxObj = (JSONObject) subArr.get(i);

			Set<String> idxObjKey = idxObj.keySet();

			Iterator<String> iter = idxObjKey.iterator();

			while (iter.hasNext()) {

				String curKeyName = iter.next();

				//subArr object 타입이 key + value[string] 타입일 경우, 
				if (idxObj.get(curKeyName).getClass().getSimpleName().equals("String")) {

					arrObj.setKey(curKeyName);
					arrObj.setValue(idxObj.get(curKeyName).toString());
					dataLine.getSubArrObjectType().add(arrObj);

					// 					dataLine.getSubArrObjectType().put(curKeyName, idxObj.get(curKeyName).toString());

					//subArr object 타입이 key + value[object] 타입일 경우, 
				} else if (idxObj.get(curKeyName).getClass().getSimpleName().equals("JSONArray")) {

					JSONArray idxVarArr = (JSONArray) idxObj.get(curKeyName);
					SubValueArrKeyValeTypeFormat subVar = new SubValueArrKeyValeTypeFormat();

					for (int j=0; j < idxVarArr.size(); j++) {

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