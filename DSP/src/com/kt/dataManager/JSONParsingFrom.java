package com.kt.dataManager;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.kt.dataForms.OwnServiceForm;
import com.kt.dataForms.OwnServiceList;
import com.kt.dataForms.ReqDataForm;


public class JSONParsingFrom {

	JSONParser parser = new JSONParser();
	JSONSerializerTo serializer = new JSONSerializerTo();

	public JSONObject setAuth (String response) {

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

	public JSONObject regiService(String response) {

		JSONObject res = null;
		
		OwnServiceForm form = new OwnServiceForm();
		OwnServiceList list = OwnServiceList.getInstance();
		JSONDataCreatorForRegi regi = new JSONDataCreatorForRegi();
		
		try {
			
			JSONObject receObj = (JSONObject) parser.parse(response);
			
			//set ref dialog data
			form.setRefDialog(regi.regiDialog(((JSONObject) receObj.get("refDialog"))));
			
			//set other service description
			form.setUserAuth(receObj.get("userAuth").toString());
			form.setInterfaceType(receObj.get("interfaceType").toString());
			form.setServiceCode(receObj.get("serviceCode").toString());
			form.setRefAPI(receObj.get("refAPI").toString());
			form.setIntentName(receObj.get("intentName").toString());
			form.setServiceDesc(receObj.get("serviceDesc").toString());
			form.setTargetURL(receObj.get("targetURL").toString());
			form.setMethod(receObj.get("method").toString());
			form.setDataType(receObj.get("dataType").toString());
			form.setDataDefinition(receObj.get("dataDefinition").toString());
			
			//set data format
			JSONArray listArr = (JSONArray) receObj.get("dataFormat");
			for (int i=0; i < listArr.size(); i++) {
				ReqDataForm dataLine = new ReqDataForm();
				JSONObject idxListObj = (JSONObject) listArr.get(i);
				
				dataLine.setKeyName(idxListObj.get("keyName").toString());
				dataLine.setValueName(idxListObj.get("valueName").toString());
				dataLine.setSuperVar(idxListObj.get("superVar").toString());
				dataLine.setJsonType(idxListObj.get("type").toString());
				dataLine.setIsUserDefine(idxListObj.get("userDefine").toString());
				dataLine.setSubArrType(idxListObj.get("subArrType").toString());
				
				// Data format의 subArr의 타입이 object인 경우, 
				if (dataLine.getSubArrType().equals("object")) {
					JSONArray subArr = (JSONArray) idxListObj.get("subArr");
					
					dataLine = regi.regiJSONArrayforObjType(dataLine, subArr);
				// Data format의 subArr의 타입이 String인 경우,
				} else if (dataLine.getSubArrType().equals("string")) {
				
					JSONArray subArr = (JSONArray) idxListObj.get("subArr");
					for (int j = 0; j < subArr.size(); j++) {
						dataLine.getSubArrStringType().add(subArr.get(j).toString());
					}
					
				}
				
				form.getDataFormat().add(dataLine);
				
			}
			
			list.setOwnList(form);
					
			ThirdPartyDataFormatCreator creator = new ThirdPartyDataFormatCreator();
			
			for(int k=0;  k < list.getInstance().getOwnList().size(); k++) {
				
				res = creator.createDataformatForJOSN(list.getInstance().getOwnList().get(k).getDataFormat());
				
				
//				res = creator.reqDataCreatorForJSON(list.getInstance().getOwnList().get(k).getDataFormat(), form.getUserAuth(), form.getServiceCode());
				
			}
				
			System.out.println("REQUEST FORMAT: " + res);	

		} catch (ParseException e) {
			// TODO: handle exception
		}
		return res;
	}

}
