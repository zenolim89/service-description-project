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

public class JSONDataCreatorForRegi {

	public Hashtable<String, String> regiDialog (JSONObject obj) {

		Hashtable<String, String> tempList = new Hashtable<String, String>();

		Set<String> listKeys = obj.keySet();

		Iterator<String> iter = listKeys.iterator();

		while (iter.hasNext()) {
			String curKeyName = iter.next();
			JSONArray arr = (JSONArray) obj.get(curKeyName);

			for (int i=0; i < arr.size(); i++) {
				tempList.put(curKeyName, arr.get(i).toString());
			}
		}

		return tempList;


	}

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
