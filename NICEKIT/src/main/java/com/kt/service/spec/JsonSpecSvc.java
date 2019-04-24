package com.kt.service.spec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kt.dataForms.HttpParam;
import com.kt.service.model.JsonElementInfo;
import com.kt.sevice.jsonparser.JsonParserSvc;

public class JsonSpecSvc {

//	public JSONObject createReqFormatForArrey(JSONArray reqFormat, JSONArray reqSpec, String word, String property) {
//		
//		JSONObject obj = new JSONObject();
//
//		for (int i = 0; i < reqFormat.size(); i++) {
//			for (int j = 0; j < reqSpec.size(); j++) {
//				
//				JSONObject format = (JSONObject) reqFormat.get(i);
//				JSONObject spec = (JSONObject) reqSpec.get(j);
//				obj  = this.createReqFormat(format, spec, word, property);
//			}
//		}
//		return obj;
//	}

	/**
	 * 
	 * @param _target
	 * @param _spec
	 * @param word
	 * @param property
	 * @return
	 */
	public JSONObject createReqFormat(String _target, String _spec, String word, String property) {

		JsonParserSvc svc = new JsonParserSvc();

		JSONObject target = svc.getJsonObject(_target);
		JSONArray spec = svc.getJsonArray(_spec);

		return this.createReqFormat(target, spec, word, property, _spec);
	}

	/**
	 * 
	 * @param reqFormat
	 * @param reqSpec
	 * @param word
	 * @param property
	 * @return
	 */
	public JSONObject createReqFormat(JSONObject target, JSONArray spec, String word, String property, String _spec) {

		JsonParserSvc svc = new JsonParserSvc();

		JsonNode targetNode = svc.getJsonNode(target.toString());
		List<JsonElementInfo> targetEleInfos = svc.getJsonElementList(targetNode.toString());
		List<JsonElementInfo> specEleInfos = svc.getJsonElementList(spec.toString());

		/* [Step1] spec과 property(ex.발화 어휘)를 이용하여 변경할 FieldName 목록 추출 */
		List<String> targetFieldNames = new ArrayList<String>();
//		for (JsonElementInfo specEleInfo : specEleInfos) {
//			if(specEleInfo.isValueNode()) {
//				if(specEleInfo.getValue().equals(property)) {
//					targetFieldNames.add(specEleInfo.getKey());
//				}
//			}
//		}

		ObjectMapper mapper = new ObjectMapper();
		try {

			List<HttpParam> specList = mapper.readValue(_spec, new TypeReference<List<HttpParam>>() {
			});

			for (HttpParam httpParam : specList) {
				if (httpParam.getValue().equals(property)) {
					targetFieldNames.add(httpParam.getParameter());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		/* [Step2] target과 변경할 FieldName을 이용하여 변경할 JsonPrtExpr(JSON 경로) 목록 추출 */
		List<String> jsonPrtExprList = new ArrayList<String>();
		for (String targetFieldName : targetFieldNames) {
			for (JsonElementInfo targetEleInfo : targetEleInfos) {
				if (targetEleInfo.getKey().equals(targetFieldName)) {
					jsonPrtExprList.add(targetEleInfo.getPath());
				}
			}
		}

		/* [Step3] 각 경로에 대한 Value 변경 수행 */
		for (String jsonPrtExpr : jsonPrtExprList) {
			svc.putAt(targetNode, jsonPrtExpr, word);
		}

		return svc.getJsonObject(targetNode.toString());
	}

	/**
	 * 3rd Party System 응답 메시지 데이터 추출
	 * 
	 * @param _target
	 * @param _spec
	 * @param property
	 * @return
	 */
	public List<Map<String, String>> selectResMsg(String resMsg, String spec, String property) {

		JsonParserSvc svc = new JsonParserSvc();

		/* [Step1] spec과 property(ex.발화 어휘)를 이용하여 변경할 FieldName 목록 추출 */
		List<String> targetFieldNames = new ArrayList<String>();
		ObjectMapper mapper = new ObjectMapper();
		try {

			List<HttpParam> specList = mapper.readValue(spec, new TypeReference<List<HttpParam>>() {
			});

			for (HttpParam httpParam : specList) {
				if (httpParam.getValue().equals(property)) {
					targetFieldNames.add(httpParam.getParameter());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		/* [Step2] target과 변경할 FieldName을 이용하여 Data 추출 */
		List<Map<String, String>> mapList = new ArrayList<Map<String, String>>();
		if (!targetFieldNames.isEmpty()) {
			mapList = svc.searchForEntitys(resMsg, targetFieldNames);
		}

		return mapList;
	}

}
