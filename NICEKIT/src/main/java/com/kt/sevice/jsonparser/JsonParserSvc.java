package com.kt.sevice.jsonparser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.kt.service.model.JsonElementInfo;

public class JsonParserSvc {

	/**
	 * 해당 인자값이 json 형식인지 확인
	 * @param str
	 * @return
	 */
	public boolean isValidJson(String str) {
		boolean valid = false;
		
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			JsonParser parser = mapper.getFactory().createParser(str);
			while (parser.nextToken() != null) {}
			valid = true;
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return valid;
	}
	
	/**
	 * 해당 인자값이 Json Object 인지 확인
	 * @param str
	 * @return
	 */
	public boolean isJsonObject(String str) {
		boolean result = false;
		if(this.isValidJson(str)) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				JsonNode rootNode = mapper.readTree(str);
				return rootNode.isObject();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * 해당 인자값이 JsonArray 인지 확인
	 * @param str
	 * @return
	 */
	public boolean isJsonArray(String str) {
		boolean result = false;
		if(this.isValidJson(str)) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				JsonNode rootNode = mapper.readTree(str);
				return rootNode.isArray();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * JsonObject 가져오기
	 * @param str
	 * @return JsonObject가 아닐경우 JsonObect / JsonObject가 아닐경우 NULL
	 */
	public JSONObject getJsonObject(String str) {
		JSONParser parser = new JSONParser();
		Object obj = null;
		
		if(this.isJsonObject(str)) {
			try {
				obj = parser.parse(str);
				return (JSONObject) obj;
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * JSONArray 가져오기
	 * @param str
	 * @return JSONArray 경우 JSONArray / JSONArray가 아닐경우 NULL
	 */
	public JSONArray getJsonArray(String str) {
		JSONParser parser = new JSONParser();
		Object obj = null;
		
		if(this.isJsonArray(str)) {
			try {
				obj = parser.parse(str);
				return (JSONArray) obj;
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * JsonNode 가져오기
	 * @param str
	 * @return
	 */
	public JsonNode getJsonNode(String str) {
		if(this.isValidJson(str)) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				return mapper.readTree(str);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	
	/**
	 * Json Element 정보 가져오기
	 * @param str
	 * @return
	 */
	public List<JsonElementInfo> getJsonElementList(String str)
	{
		List<JsonElementInfo> list = new ArrayList<JsonElementInfo>();
		
		if(this.isValidJson(str)) {
			
			Stack<String> path = new Stack<String>();
			ObjectMapper mapper = new ObjectMapper();
			
			try {
				JsonNode rootNode = mapper.readTree(str);
				
				if(rootNode.isArray()) {
					path.push("");
					this.saveArrayNode("root", rootNode, list, path);
					arrayParser("", rootNode, list, path);
				}else if(rootNode.isObject()) {
					path.push("");
					this.saveObjectNode("root", rootNode, list, path);
					objectParser(rootNode, list, path);
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return list;
	}
	
	/**
	 * 동일한 fieldName을 가진 요소의 Path 가져오기
	 * @param str
	 * @param targetName
	 * @return
	 */
	public List<String> getMatchFieldNameList(String str, String targetName){
		
		List<String> result = new ArrayList<String>();
		
		List<JsonElementInfo> infos = this.getJsonElementList(str);
		
		for (JsonElementInfo el : infos) {
			if(el.getKey().equals(targetName)) {
				result.add(el.getPath());
			}
		}
		
		return result;
	}

	/**
	 * 
	 * @param rootNode
	 * @param jsonPrtExpr
	 * @param value
	 * @return
	 */
	public JsonNode putAt(JsonNode rootNode, String jsonPrtExpr, String value){
		
		Stack<JsonNode> jsonNodeStack = new Stack<JsonNode>();
		
		jsonNodeStack.push(rootNode);
		String[] paths = jsonPrtExpr.split("/");
		
		for (int i = 1; i < paths.length; i++) {
			
			String el = paths[i];
			JsonNode node = jsonNodeStack.lastElement();
			JsonNode child = null;
			
			if(node.isArray()) {
				child = node.get(Integer.parseInt(el));
				jsonNodeStack.push(child);
			}else {
				child = node.get(el);
				jsonNodeStack.push(child);
			}
		}
		
 		//System.out.println("jsonNodeStack Input End");
		
		int point = jsonNodeStack.size();
		JsonNode changeNode = jsonNodeStack.pop(); point--; 
		String fieldName = paths[point];
		JsonNode parentNode = jsonNodeStack.pop(); point--;
		((ObjectNode)parentNode).replace(fieldName, new TextNode(value));
		
		
		while(jsonNodeStack.size() > 0) {
			
			changeNode = parentNode;
			fieldName = paths[point];
			parentNode = jsonNodeStack.pop(); point--;
			
			if(parentNode.isArray()) {
				((ArrayNode)parentNode).set(Integer.parseInt(fieldName), changeNode);
			}
			else {
				((ObjectNode)parentNode).replace(fieldName, changeNode);
			} 
		}
		
		return parentNode;
	}
	
	
	/**
	 * 해당 JSON Source에서 동일한 fieldName를 가진 데이터 추출
	 * @param source JSON
	 * @param entityNames Json Key 리스트
	 */
	public List<Map<String, String>> searchForEntitys(String source, List<String> fieldNames) {
		
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		
		for (String fieldName : fieldNames) {
			List<Map<String, String>> temp = this.searchForEntity(source, fieldName);
			if(!temp.isEmpty()) {
				result.addAll(temp);
			}
		}

		/* FOR TEST */
		if(!result.isEmpty()) {
			for (Map<String, String> map : result) {
				System.out.println(map.toString());
			}
		}
		
		return result;
	}
	
	/**
	 * 해당 JSON Source에서 동일한 fieldName를 가진 데이터 추출
	 * @param source JSON
	 * @param entityNames Json Key 리스트
	 */
	public List<Map<String, String>> searchForEntity(String source, String fieldName){
		
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		
		List<JsonElementInfo> infos = this.getJsonElementList(source);
		
		for (JsonElementInfo el : infos) {
			if(el.getKey().equals(fieldName)) {
				Map<String, String> temp = new HashMap<String, String>();
				temp.put(el.getKey(), el.getValue());
				result.add(temp);
			}
		}
		
		/* FOR TEST */
//		if(!result.isEmpty()) {
//			for (Map<String, String> map : result) {
//				System.out.println(map.toString());
//			}
//		}
		
		return result;
	}
	
	
    /**
     * Map을 json으로 변환한다.
     *
     * @param map Map<String, Object>.
     * @return JSONObject.
     */
    public JSONObject getJsonStringFromMap( Map<String, String> map )
    {
        JSONObject jsonObject = new JSONObject();
        
        for( Map.Entry<String, String> entry : map.entrySet() ) {
        	String key = entry.getKey();
            Object value = entry.getValue();
            jsonObject.put(key, value);
        }
        
        return jsonObject;
    }
	
    /**
     * List<Map>을 jsonArray로 변환한다.
     *
     * @param _msg List<Map<String, Object>>.
     * @return JSONArray.
     */
    public JSONArray getJsonArrayFromList( List<Map<String, String>> _msg )
    {
        JSONArray jsonArray = new JSONArray();
        for( Map<String, String> map : _msg ) {
            jsonArray.add( this.getJsonStringFromMap( map ) );
        }
        
        return jsonArray;
    }
	
	
	/***
	 * ========= Start [ getJsonElementList(String str) ] ======================
	 */

	private void objectParser(JsonNode jsonNode, List<JsonElementInfo> list, Stack<String> path) {
		
		Iterator<String> rootNames = jsonNode.fieldNames();
		
		while (rootNames.hasNext()) {
			String key = rootNames.next();
			JsonNode subNode = jsonNode.get(key);
			
			this.appendPath(path,key);
			
			if(subNode.isObject()) {
				this.saveObjectNode(key, subNode, list, path);
				objectParser(subNode, list, path);
				path.pop();
			}else if(subNode.isArray()) {
				this.saveArrayNode(key, subNode, list, path);
				this.arrayParser(key, subNode, list, path);
				path.pop();
			}else if(subNode.isValueNode()) {
				this.saveValueNode(key, subNode, list, path);
			}
		}
		
	}
	
	private void arrayParser(String key, JsonNode node, List<JsonElementInfo> list, Stack<String> path) {
		for (int i = 0; i < node.size(); i++) {
			JsonNode child = node.get(i);
			this.appendPath(path, Integer.toString(i));
			if(child.isObject()) {
				objectParser(child, list, path);
				path.pop();
			} else if(child.isArray()) {
				arrayParser(key, child, list, path);
				path.pop();
			} else if (child.isValueNode()) {
				this.saveValueNode(key, child, list, path);
			}
		}
	}
	
	
	private void saveObjectNode(String key, JsonNode node, List<JsonElementInfo> list, Stack<String> path) {
		JsonElementInfo info = new JsonElementInfo();
		info.setKey(key);
		info.setNodeType(node.getNodeType().toString());
		info.setValueNode(node.isValueNode());
		info.setObject(node.isObject());
		info.setArray(node.isArray());
		info.setPath(path.lastElement());
		list.add(info);
	}
	
	private void saveArrayNode(String key, JsonNode node, List<JsonElementInfo> list, Stack<String> path) {
		JsonElementInfo info = new JsonElementInfo();
		info.setKey(key);
		info.setNodeType(node.getNodeType().toString());
		info.setValueNode(node.isValueNode());
		info.setObject(node.isObject());
		info.setArray(node.isArray());
		info.setPath(path.lastElement());
		list.add(info);
	}
	
	private void saveValueNode(String key, JsonNode node, List<JsonElementInfo> list, Stack<String> path) {
		
		JsonElementInfo info = new JsonElementInfo();
		info.setKey(key);
		info.setValue(node.asText());
		info.setNodeType(node.getNodeType().toString());
		info.setValueNode(node.isValueNode());
		info.setObject(node.isObject());
		info.setArray(node.isArray());
		info.setPath(path.pop());
		list.add(info);
	}
	
	
	private void appendPath (Stack<String> path, String str) {
		
		String base = path.lastElement();
		path.push(base + "/" + str );
	}

	
	/***
	 * ========= END [ getJsonElementList(String str) ] ======================
	 */
	
	
	
}
