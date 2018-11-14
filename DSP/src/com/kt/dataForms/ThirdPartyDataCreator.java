package com.kt.dataForms;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ThirdPartyDataCreator {

	@SuppressWarnings("unlikely-arg-type")
	public JSONObject reqDataCreatorForJSON (ArrayList<ReqDataForm> dataForm, String userAuth, String serviceCode) {

		JSONObject resObj = new JSONObject();
		String targetVar = null;

		for (int i=0; i < dataForm.size(); i++) {

			ReqDataForm tempLine = dataForm.get(i);

			if(tempLine.getSuperVar().equals("false")) {

				if(tempLine.getJsonType().equals("JSONObject")) {
					resObj.put(tempLine.getKeyName(), tempLine.getValueName());	
				} else if (tempLine.getJsonType().equals("JSONArray")) {
					JSONArray arr = new JSONArray();

					if(tempLine.getSubArrType().equals("string")) {

						for (int j=0; j < tempLine.getSubArrStringType().size(); j++) {
							arr.add(tempLine.getSubArrStringType().get(j));
							resObj.put(tempLine.getKeyName(), arr);
						}
					} else if (tempLine.getSubArrType().equals("object")) {

						JSONObject obj = new JSONObject();


						Set keyNm = tempLine.getSubArrObjectType().keySet();
						Iterator<String> iter = keyNm.iterator();

						while(iter.hasNext()) {
							String objKey = iter.next();
							obj.put(objKey, tempLine.getSubArrObjectType().get(objKey));

							arr.add(obj);
						} resObj.put(tempLine.getKeyName(), arr);
					}
				}


			} else if (tempLine.getSuperVar() != "false" ) {
				if (tempLine.getJsonType().equals("JSONObject")) {
					JSONObject temObj = new JSONObject();
					if(tempLine.getSubArrType().equals("false")) {
						temObj.put(tempLine.getKeyName(), tempLine.getValueName());
						resObj.put(tempLine.getSuperVar(), temObj);
					} else if(tempLine.getSubArrType().equals("string")) {
						JSONObject temObj1 = new JSONObject();
						JSONArray temArr = new JSONArray();

						for (int j=0; j < tempLine.getSubArrStringType().size(); j++) {
							temArr.add(tempLine.getSubArrStringType().get(j));
							temObj1.put(tempLine.getKeyName(), temArr);
							resObj.put(tempLine.getSuperVar(), temObj1);
						}
					} else if (tempLine.getSubArrObjectType().equals("object")) {
						JSONObject temObj2 = new JSONObject();
						JSONObject temArrObj = new JSONObject();
						JSONArray temArr2 = new JSONArray();

						Set keyNm = tempLine.getSubArrObjectType().keySet();
						Iterator<String> iter = keyNm.iterator();

						while(iter.hasNext()) {
							String objKey = iter.next();
							temArrObj.put(objKey, tempLine.getSubArrObjectType().get(objKey));
						}

						temArr2.add(temArrObj);
						temObj2.put(tempLine.getKeyName(), temArr2);

						resObj.put(tempLine.getSuperVar(), temObj2);

					}
				} else if(tempLine.getJsonType().equals("JSONArray")) {
					
					JSONArray arr = new JSONArray();

					if(tempLine.getSubArrType().equals("string")) {

						for (int j=0; j < tempLine.getSubArrStringType().size(); j++) {
							arr.add(tempLine.getSubArrStringType().get(j));
							resObj.put(tempLine.getKeyName(), arr);
						}
					} else if (tempLine.getSubArrType().equals("object")) {

						JSONObject obj = new JSONObject();


						Set keyNm = tempLine.getSubArrObjectType().keySet();
						Iterator<String> iter = keyNm.iterator();

						while(iter.hasNext()) {
							String objKey = iter.next();
							obj.put(objKey, tempLine.getSubArrObjectType().get(objKey));

							arr.add(obj);
						} resObj.put(tempLine.getKeyName(), arr);
					}
				}
			}	

		}

		return resObj;
	}


}
