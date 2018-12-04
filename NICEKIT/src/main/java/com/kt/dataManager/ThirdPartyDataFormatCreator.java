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
}


