package com.kt.service.spec;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.fasterxml.jackson.databind.JsonNode;
import com.kt.service.model.JsonElementInfo;
import com.kt.sevice.jsonparser.JsonParserSvc;


public class JsonSpecSvc {
	
	public JSONObject createReqFormatForArrey(JSONArray reqFormat, JSONArray reqSpec, String word, String property) {
		
		JSONObject obj = new JSONObject();

		for (int i = 0; i < reqFormat.size(); i++) {
			for (int j = 0; j < reqSpec.size(); j++) {
				
				JSONObject format = (JSONObject) reqFormat.get(i);
				JSONObject spec = (JSONObject) reqSpec.get(j);
				obj  = this.createReqFormat(format, spec, word, property);
			}
		}
		return obj;
	}
	
	/**
	 * 
	 * @param reqFormat
	 * @param reqSpec
	 * @param word
	 * @param property
	 * @return
	 */
	public JSONObject createReqFormat(JSONObject target, JSONObject spec, String word, String property) {
		
		JsonParserSvc svc = new JsonParserSvc();
		
		JsonNode targetNode = svc.getJsonNode(target.toString());
		List<JsonElementInfo> targetEleInfos = svc.getJsonElementList(targetNode.toString());
		List<JsonElementInfo> specEleInfos = svc.getJsonElementList(spec.toString());

		/* [Step1] spec과 property(ex.발화 어휘)를 이용하여 변경할 FieldName 목록 추출*/
		List<String> targetFieldNames = new ArrayList<String>();
		for (JsonElementInfo specEleInfo : specEleInfos) {
			if(specEleInfo.isValueNode()) {
				if(specEleInfo.getValue().equals(property)) {
					targetFieldNames.add(specEleInfo.getKey());
				}
			}
		}
		
		/* [Step2] target과 변경할 FieldName을 이용하여 변경할 JsonPrtExpr(JSON 경로) 목록 추출*/
		List<String> jsonPrtExprList = new ArrayList<String>();
		for (String targetFieldName : targetFieldNames) {
			for (JsonElementInfo targetEleInfo : targetEleInfos) {
				if(targetEleInfo.getKey().equals(targetFieldName)) {
					jsonPrtExprList.add(targetEleInfo.getPath());
				}
			}
		}
			
		/* [Step3] 각 경로에 대한 Value 변경 수행*/
		for (String jsonPrtExpr : jsonPrtExprList) 
		{
			svc.putAt(targetNode, jsonPrtExpr, word);
		}

		return svc.getJsonObject(targetNode.toString());
	}
}
