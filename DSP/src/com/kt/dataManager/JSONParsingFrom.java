package com.kt.dataManager;

import java.util.Iterator;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.kt.dataForms.OwnServiceForm;
import com.kt.dataForms.OwnServiceList;
import com.kt.dataForms.ReqDataForm;
import com.kt.dataForms.ThirdPartyDataCreator;


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

		try {

			OwnServiceForm form = new OwnServiceForm();
			OwnServiceList list = OwnServiceList.getInstance();

			JSONObject jsonObj = (JSONObject) parser.parse(response);

			Set keyName = jsonObj.keySet();

			Iterator<String> iter = keyName.iterator();

			while(iter.hasNext()) {

				String name = iter.next();

				// parsing Dialog
				if (jsonObj.get(name).getClass().getSimpleName().equals("JSONObject")) {

					JSONObject subObj = (JSONObject) jsonObj.get(name);
					
					Set subKeyName = subObj.keySet();
					
					Iterator<String> subIter = subKeyName.iterator();
					
					while (subIter.hasNext()) {
						
						String subName = subIter.next();
						JSONArray arr = (JSONArray) subObj.get(subName);
						for (int i=0; i < arr.size(); i++) {
							form.getRefDialog().put(subName, arr.get(i).toString());			
						}
								
					}
					
				// parsing Data format of 3rd party which is defined by something.
				} else if (jsonObj.get(name).getClass().getSimpleName().equals("JSONArray")) {
					
					
					JSONObject formObj = new JSONObject();
					JSONArray listObj = (JSONArray) jsonObj.get(name);
				
					for (int i=0; i < listObj.size(); i++) {
						
						ReqDataForm dataLine = new ReqDataForm();
						
						JSONObject obj = (JSONObject) listObj.get(i);
						
						dataLine.setKeyName(obj.get("keyName").toString());
						dataLine.setValueName(obj.get("valueName").toString());
						dataLine.setSuperVar(obj.get("superVar").toString());
						dataLine.setJsonType(obj.get("type").toString());
						dataLine.setIsUserDefine(obj.get("userDefine").toString());
						dataLine.setSubArrType(obj.get("subArrType").toString());
						
						//subArr 형태가 object 형태 일때 
						if (dataLine.getSubArrType().equals("object")) {
							JSONArray arr = (JSONArray) obj.get("subArr");
							for (int j=0; j < arr.size();j++) {
								
								JSONObject arrObj = (JSONObject) arr.get(j);
								
								Set arrKeyName = arrObj.keySet();
								
								Iterator<String> arrIter = arrKeyName.iterator();
								
								while (arrIter.hasNext()) {
									String arrKey = arrIter.next();
									dataLine.getSubArrObjectType().put(arrKey, arrObj.get(arrKey).toString());
									 
								}
								
							}
						//subArr 형태가 value로만 이루어진 배열 일때 
						} else if (dataLine.getSubArrType().equals("string")) {
							
							JSONArray arr = (JSONArray) obj.get("subArr");
							
							for (int k=0; k < arr.size(); k++) {
								
								dataLine.getSubArrStringType().add(arr.get(k).toString());
								
							}
						//subArr 형태가 object + array 형태 일때 								
						}
						
						form.getDataFormat().add(dataLine);
					}
				}
					
					// key, value, subfile, variable, user define
					
					
				 else if (name.equals("userAuth")) {
					form.setUserAuth(jsonObj.get(name).toString());				
				} else if (name.equals("interfaceType")) {
					form.setInterfaceType(jsonObj.get(name).toString());
				} else if (name.equals("serviceCode")) {
					form.setServiceCode(jsonObj.get(name).toString());
				} else if (name.equals("refAPI")) {
					form.setRefAPI(jsonObj.get(name).toString());
				} else if (name.equals("intentName")) {
					form.setIntentName(jsonObj.get(name).toString());
				} else if (name.equals("serviceDesc")) {
					form.setServiceDesc(jsonObj.get(name).toString());
				} else if (name.equals("targetURL")) {
					form.setTargetURL(jsonObj.get(name).toString());
				} else if (name.equals("method")) {
					form.setMethod(jsonObj.get(name).toString());
				} else if (name.equals("dataType")) {
					form.setDataType(jsonObj.get(name).toString());
				} else if (name.equals("dataDefinition")) {
					form.setDataDefinition(jsonObj.get(name).toString());
				}
				
			}

			list.setOwnList(form);
			
			ThirdPartyDataCreator creator = new ThirdPartyDataCreator();
			
			for(int k=0;  k < list.getInstance().getOwnList().size(); k++) {
				
				res = creator.reqDataCreatorForJSON(list.getInstance().getOwnList().get(k).getDataFormat(), form.getUserAuth(), form.getServiceCode());
				
			}
			
			
			
			System.out.println("REQUEST FORMAT: " + res);
			
		
			
			

		} catch (ParseException e) {
			// TODO: handle exception
		}
		return res;
	}

}
