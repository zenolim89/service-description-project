package com.kt.dataManager;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.kt.dataForms.KeyValueFormatForJSON;
import com.kt.dataForms.ReqDataForm;
import com.kt.dataForms.SubValueArrKeyValeTypeFormat;

public class ThirdPartyDataFormatCreator {

	JSONObject resJSON = new JSONObject();
	@SuppressWarnings("unlikely-arg-type")
	//	@SuppressWarnings("unused")
	public JSONObject createDataformatForJOSN (ArrayList<ReqDataForm> dataForm) {

		for (int i=0; i < dataForm.size(); i++ ) {

			ReqDataForm tempLine = dataForm.get(i);

			if (tempLine.getJsonType().equals("JSONObject")) {
				this.createJSONObject(tempLine);

			} else if (tempLine.getJsonType().equals("JSONArray")) {
				this.createJSONArray(tempLine);

			}
		}

		return resJSON;
	}

	private JSONObject createJSONArray(ReqDataForm idxDataForm) {



		if (idxDataForm.getSuperVar().equals("false")) {
			if (idxDataForm.getSubArrType().equals("object")) {			
				JSONArray subArr = this.createSubArrForObjectType(idxDataForm);
				resJSON.put(idxDataForm.getKeyName(), subArr);

			} else if (idxDataForm.getSubArrType().equals("string")) {		
				JSONArray subArr = this.createSubArrForStringType(idxDataForm.getSubArrStringType());
				resJSON.put(idxDataForm.getKeyName(), subArr);
			}

		} else if (idxDataForm.getSuperVar() != "false") {

			JSONObject obj = new JSONObject();

			if (idxDataForm.getSubArrType().equals("object")) {

				JSONArray subArr = this.createSubArrForObjectType(idxDataForm);
				obj.put(idxDataForm.getKeyName(), subArr);
				resJSON.put(idxDataForm.getSuperVar(), obj);

			} else if (idxDataForm.getSubArrType().equals("string")) {

				JSONArray subArr = this.createSubArrForStringType(idxDataForm.getSubArrStringType());
				obj.put(idxDataForm.getKeyName(), subArr);
				resJSON.put(idxDataForm.getSuperVar(), obj);

			}

		}
		
		return resJSON;

	}

	private void createJSONObject(ReqDataForm idxDataForm) {

		if (idxDataForm.getSuperVar().equals("false")) {
			if (idxDataForm.getSubArrType().equals("false")) {
				resJSON.put(idxDataForm.getKeyName(), idxDataForm.getValueName());
			} else if (idxDataForm.getSubArrType() != "false") {

				if (idxDataForm.getSubArrType().equals("object")) {
					JSONArray subArr = this.createSubArrForObjectType(idxDataForm);
					resJSON.put(idxDataForm.getKeyName(), subArr);
				} else if (idxDataForm.getSubArrType().equals("string")) {
					JSONArray subArr = this.createSubArrForStringType(idxDataForm.getSubArrStringType());
					resJSON.put(idxDataForm.getKeyName(), subArr);
				}

			}

		} else if (idxDataForm.getSuperVar() != "false") {

			JSONObject obj = new JSONObject();

			if (idxDataForm.getSubArrType().equals("false")) {
				obj.put(idxDataForm.getKeyName(), idxDataForm.getValueName());
				resJSON.put(idxDataForm.getSuperVar(), obj);
			} else if (idxDataForm.getSubArrType() != "false") {
				if (idxDataForm.getSubArrType().equals("object")) {
					JSONArray subArr = this.createSubArrForObjectType(idxDataForm);
					obj.put(idxDataForm.getKeyName(), subArr);
					resJSON.put(idxDataForm.getSuperVar(), obj);
				} else if (idxDataForm.getSubArrType().equals("string")) {
					JSONArray subArr = this.createSubArrForStringType(idxDataForm.getSubArrStringType());
					obj.put(idxDataForm.getKeyName(), subArr);
					resJSON.put(idxDataForm.getSuperVar(), obj);
				}		
			}
		}
	}



	private JSONArray createSubArrForObjectType(ReqDataForm form) {

		JSONArray subArr = new JSONArray();

		if (form.getSubArrObjectType().size() > 0 && form.getSubValueArrObjType().size() > 0) {	
			subArr = this.createKeyStringValueObjTypeforSubArr(form.getSubArrObjectType(), subArr);
			subArr = this.createKeyArrValueObjTypeforSubArr(form.getSubValueArrObjType(), subArr);
		} else if (form.getSubArrObjectType().size() <= 0 && form.getSubValueArrObjType().size() > 0) {
			subArr = this.createKeyArrValueObjTypeforSubArr(form.getSubValueArrObjType(), subArr);
		} else if (form.getSubArrObjectType().size() > 0 && form.getSubValueArrObjType().size() <= 0) {
			subArr = this.createKeyStringValueObjTypeforSubArr(form.getSubArrObjectType(), subArr);
		}

		return subArr;
	}

	private JSONArray createKeyStringValueObjTypeforSubArr(ArrayList<KeyValueFormatForJSON> objList, JSONArray arr) {

		JSONObject objType = new JSONObject();

		//obj type에서 값 추출 
		for (int i = 0; i < objList.size(); i ++) {
			objType.put(objList.get(i).getKey(), objList.get(i).getValue());
		}
		
		arr.add(objType);

		return arr;
	}

	private JSONArray createKeyArrValueObjTypeforSubArr(ArrayList<SubValueArrKeyValeTypeFormat> objList, JSONArray arr) {

		JSONObject objType = new JSONObject();
		
		
		
		for (int i=0; i < objList.size(); i++) {
			JSONArray subVarArr = new JSONArray();
			String keyName = objList.get(i).getKey();
			
			for (int j=0; j < objList.get(i).getVarObjList().size(); j++) {
				
				JSONObject varObjType = new JSONObject();
				
				Hashtable<String, String> temp = objList.get(i).getVarObjList().get(j);
								
				Set<String> subKeySets = temp.keySet();
				
				Iterator<String> subIter = subKeySets.iterator();
				
				while (subIter.hasNext()) {
					
					String valueKey = subIter.next();
					varObjType.put(valueKey, temp.get(valueKey));
					
				}
				subVarArr.add(varObjType);
			}
			
			objType.put(keyName, subVarArr);
		}

		arr.add(objType);

		return arr;

	}



	private JSONArray createSubArrForStringType(ArrayList<String> list) {

		JSONArray arr = new JSONArray();

		for (int i=0; i < list.size(); i++) {

			arr.add(list.get(i));
		}

		return arr;
	}



	//	public JSONObject reqDataCreatorForJSON (ArrayList<ReqDataForm> dataForm, String userAuth, String serviceCode) {
	//
	//		JSONObject resObj = new JSONObject();
	//		String targetVar = null;
	//
	//		for (int i=0; i < dataForm.size(); i++) {
	//
	//			ReqDataForm tempLine = dataForm.get(i);
	//
	//			if(tempLine.getSuperVar().equals("false")) {
	//				
	//				// JSON 타입이 JSONObject 일때 
	//				if(tempLine.getJsonType().equals("JSONObject")) {
	//					resObj.put(tempLine.getKeyName(), tempLine.getValueName());	
	//				
	//				// JSON 타입이 JSONArray 일때 
	//				} else if (tempLine.getJsonType().equals("JSONArray")) {
	//					JSONArray arr = new JSONArray();
	//
	//					if(tempLine.getSubArrType().equals("string")) {
	//
	//						for (int j=0; j < tempLine.getSubArrStringType().size(); j++) {
	//							arr.add(tempLine.getSubArrStringType().get(j));
	//							resObj.put(tempLine.getKeyName(), arr);
	//						}
	//					} else if (tempLine.getSubArrType().equals("object")) {
	//
	//						if (tempLine.getSubValueArrObjType().size() > 0) {
	//							JSONObject subObj = new JSONObject();
	//							JSONArray temArr3 = new JSONArray();
	//							JSONObject temp = new JSONObject();
	//
	//							//sub Array 형태 == key + value array 타입  
	//							Set keyObjNm = tempLine.getSubValueArrObjType().keySet();
	//							Iterator<String> iter = keyObjNm.iterator();
	//
	//							while (iter.hasNext()) {
	//
	//								String subKeyNm = iter.next();
	//								Hashtable<String, String> tempHash = tempLine.getSubValueArrObjType().get(subKeyNm);
	//
	//								Set subObjKeyNm = tempHash.keySet();
	//
	//								Iterator<String> subIter = subObjKeyNm.iterator();
	//
	//
	//								while (subIter.hasNext()) {
	//									String subSubKeyNm = subIter.next();
	//									subObj.put(subSubKeyNm, tempHash.get(subSubKeyNm));
	//									temArr3.add(subObj);	
	//								}
	//								
	//							}
	//							resObj.put(tempLine.getKeyName(), temArr3);
	//						} else {
	//
	//							JSONObject obj = new JSONObject();
	//
	//							Set keyNm = tempLine.getSubArrObjectType().keySet();
	//							Iterator<String> iter = keyNm.iterator();
	//
	//							while(iter.hasNext()) {
	//								String objKey = iter.next();
	//								obj.put(objKey, tempLine.getSubArrObjectType().get(objKey));
	//
	//								arr.add(obj);
	//							} 
	//							resObj.put(tempLine.getKeyName(), arr);
	//						}
	//
	//
	//					}
	//				}
	//
	//
	//			} else if (tempLine.getSuperVar() != "false" ) {
	//				if (tempLine.getJsonType().equals("JSONObject")) {
	//					JSONObject temObj = new JSONObject();
	//					if(tempLine.getSubArrType().equals("false")) {
	//						temObj.put(tempLine.getKeyName(), tempLine.getValueName());
	//						resObj.put(tempLine.getSuperVar(), temObj);
	//					} else if(tempLine.getSubArrType().equals("string")) {
	//						JSONObject temObj1 = new JSONObject();
	//						JSONArray temArr = new JSONArray();
	//
	//						for (int j=0; j < tempLine.getSubArrStringType().size(); j++) {
	//							temArr.add(tempLine.getSubArrStringType().get(j));
	//							temObj1.put(tempLine.getKeyName(), temArr);
	//							resObj.put(tempLine.getSuperVar(), temObj1);
	//						}
	//					} else if (tempLine.getSubArrObjectType().equals("object")) {
	//						JSONObject temObj2 = new JSONObject();
	//						JSONObject temArrObj = new JSONObject();
	//						JSONArray temArr2 = new JSONArray();
	//
	//						//Sub Array 형태 확인 (key + value 타입 or key + value array 타입 
	//
	//						if (tempLine.getSubValueArrObjType().size() > 0) {
	//							JSONObject subObj = new JSONObject();
	//							JSONArray temArr3 = new JSONArray();
	//							JSONObject temp = new JSONObject();
	//
	//							//sub Array 형태 == key + value array 타입  
	//							Set keyObjNm = tempLine.getSubValueArrObjType().keySet();
	//							Iterator<String> iter = keyObjNm.iterator();
	//
	//							while (iter.hasNext()) {
	//
	//
	//								String subKeyNm = iter.next();
	//								Hashtable<String, String> tempHash = tempLine.getSubValueArrObjType().get(subKeyNm);
	//
	//								Set subObjKeyNm = tempHash.keySet();
	//
	//								Iterator<String> subIter = subObjKeyNm.iterator();
	//
	//
	//								while (subIter.hasNext()) {
	//									String subSubKeyNm = subIter.next();
	//									subObj.put(subSubKeyNm, tempHash.get(subSubKeyNm));
	//								}
	//								temArr3.add(subObj);	
	//								temp.put(subObjKeyNm, temArr3);
	//							}
	//							temArr2.add(temp);
	//							temObj2.put(tempLine.getKeyName(), temArr2);
	//							resObj.put(tempLine.getSuperVar(), temObj2);
	//
	//
	//
	//
	//
	//						} else {
	//							// Sub Array 형태 == key + vaule 타입 
	//							Set keyNm = tempLine.getSubArrObjectType().keySet();
	//							Iterator<String> iter = keyNm.iterator();
	//
	//							while(iter.hasNext()) {
	//								String objKey = iter.next();
	//								temArrObj.put(objKey, tempLine.getSubArrObjectType().get(objKey));
	//							}
	//
	//							temArr2.add(temArrObj);
	//							temObj2.put(tempLine.getKeyName(), temArr2);
	//
	//							resObj.put(tempLine.getSuperVar(), temObj2);
	//						}
	//
	//					}
	//				} else if(tempLine.getJsonType().equals("JSONArray")) {
	//
	//					JSONArray arr = new JSONArray();
	//
	//					if(tempLine.getSubArrType().equals("string")) {
	//
	//						for (int j=0; j < tempLine.getSubArrStringType().size(); j++) {
	//							arr.add(tempLine.getSubArrStringType().get(j));
	//							resObj.put(tempLine.getKeyName(), arr);
	//						}
	//					} else if (tempLine.getSubArrType().equals("object")) {
	//
	//						JSONObject obj = new JSONObject();
	//
	//						Set keyNm = tempLine.getSubArrObjectType().keySet();
	//						Iterator<String> iter = keyNm.iterator();
	//
	//						while(iter.hasNext()) {
	//							String objKey = iter.next();
	//
	//							obj.put(objKey, tempLine.getSubArrObjectType().get(objKey));
	//
	//							arr.add(obj);
	//						} resObj.put(tempLine.getKeyName(), arr);
	//					}
	//				}
	//			}	
	//
	//		}
	//
	//		return resObj;
	//	}

}


