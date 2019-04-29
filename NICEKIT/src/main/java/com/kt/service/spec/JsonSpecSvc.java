package com.kt.service.spec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kt.dataForms.HttpParam;
import com.kt.service.model.JsonElementInfo;
import com.kt.sevice.jsonparser.JsonParserSvc;

public class JsonSpecSvc {

	/**
	 * 
	 * @param _target
	 * @param _spec
	 * @param word
	 * @param property
	 * @return
	 */
	public JsonNode createReqFormat(String _target, String _spec, String word, String property) {

		if(_target == null || _target =="") return null;
		
		JsonParserSvc svc = new JsonParserSvc();

		JsonNode targetNode = svc.getJsonNode(_target);
		
		JSONArray spec = svc.getJsonArray(_spec);

		return this.createReqFormat(targetNode, spec, word, property, _spec);
	}

	/**
	 * 
	 * @param reqFormat
	 * @param reqSpec
	 * @param word
	 * @param property
	 * @return
	 */
	public JsonNode createReqFormat(JsonNode targetNode, JSONArray spec, String word, String property, String _spec) {

		JsonParserSvc svc = new JsonParserSvc();

		//JsonNode targetNode = svc.getJsonNode(target.toString());
		List<JsonElementInfo> targetEleInfos = svc.getJsonElementList(targetNode.toString());
		List<JsonElementInfo> specEleInfos = svc.getJsonElementList(spec.toString());

		/* [Step1] spec과 property(ex.발화 어휘)를 이용하여 변경할 FieldName 목록 추출 */
		List<String> targetFieldNames = new ArrayList<String>();
		ObjectMapper mapper = new ObjectMapper();
		try {

			List<HttpParam> specList = mapper.readValue(_spec, new TypeReference<List<HttpParam>>() {});

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

		return targetNode;
	}

	/**
	 * 3rd Party System 응답 메시지 데이터 추출
	 * 
	 * @param _target
	 * @param _spec
	 * @param property
	 * @return
	 */
	public Map<String, String> selectResMsg(String resMsg, String spec, String property) {

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
		Map<String, String> filterData = new HashMap<String, String>();
		if (!targetFieldNames.isEmpty()) {
			filterData = svc.searchForEntitys(resMsg, targetFieldNames);
		}

		return filterData;
	}

}
