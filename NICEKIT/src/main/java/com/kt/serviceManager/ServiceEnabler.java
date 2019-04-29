package com.kt.serviceManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.kt.dataForms.DiscoveredServiceDESC;
import com.kt.dataForms.ExtractionKeynTypeForJSON;
import com.kt.service.spec.JsonSpecSvc;

public class ServiceEnabler {

	public JSONObject discoverMatchingWord (DiscoveredServiceDESC desc, String word) {
		
		JSONObject res = new JSONObject();
		
		//JSONArray arr = (JSONArray) desc.getDicList(); //
		
		JSONArray arr = null;
		try {
			arr = (JSONArray) new JSONParser().parse(desc.getStrDicList());
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//JSONArray arr = (JSONArray) desc.getDicList().get(0);  //org
		
		System.out.println(arr.toString());

		for (int i=0; i < arr.size(); i++) {

			JSONObject obj = (JSONObject) arr.get(i);
			
			//JSONArray wordArr = (JSONArray) obj.get("wordList");
			
			JSONArray wordArr = null;
			
			try {
				wordArr = (JSONArray) new JSONParser().parse((String) obj.get("wordList"));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			for (int j = 0; j < wordArr.size(); j++) {
				
				//JSONObject wordObj = (JSONObject) wordArr.get(j);
				
				String wordObj = (String) wordArr.get(j);
				
				//if (word.equals(wordObj.get("word").toString())) {
				if(word.equals(wordObj)) {
					res = this.createRequestData(desc, word);
					
					return res;

				} else {

					res.put("resCode", "404");
					res.put("resMsg", "요청하신 " + word + "관련 서비스를 찾을 수 없습니다 확인 후 다시 말씀해주세요");
					
					System.out.println("[DEBUG] : 어휘 검색결과가 없음");
					
				}
				
			}

			

		}
		
		return res;


	}
	
	/**
	 * @author	: "Minwoo Ryu" [2019. 2. 12. 오후 2:00:31]
	 * desc	: 최종적으로 해당 method는 out-bound로 정의되어야 함
	 * @version	:
	 * @param	: 
	 * @return 	: JSONObject 
	 * @throws 	: 
	 * @see		: 
	
	 * @param desc
	 * @param word
	 * @return
	 */
	public JSONObject createRequestData (DiscoveredServiceDESC desc, String word) {
		
		JSONObject obj = new JSONObject();
		JSONObject res = new JSONObject();
		
		String host = desc.getComURL();
		String method = desc.getMethod();
		
		// set을 통한 스펙값과 전송 규격값 확인 및 데이터 변환
		 
		obj.put("resCode", "201");
		obj.put("resMsg", "요청하신 " + word + "서비스를 요청하였습니다");
		
		/*TODO SERA 규격 관련 변경*/
		JsonSpecSvc svc = new JsonSpecSvc();
		//obj = svc.createReqFormatForArrey(desc.getReqStructure(), desc.getReqSpec(), word, "spec3");
		//obj = this.createReqFormat(desc.getReqStructure(), desc.getReqSpec(), word);
		System.out.println("변경된 JSON");
		System.out.println(obj.toString());
		
		
		res.put("resCode", "201");
		res.put("resMsg", word + "서비스를 요청하였습니다");
		
		return res;
		
		
	}
	
	public JSONObject createReqFormat (JSONArray reqFormat, JSONArray reqSpec, String word) {
		
		JSONObject formatObj = new JSONObject();
		
		for (int specRowNum = 0; specRowNum < reqSpec.size(); specRowNum++ ) {
			
			JSONObject specObj  = (JSONObject) reqSpec.get(specRowNum);
			
			Set<String> keys = specObj.keySet(); 
			
			Iterator<String> iter = keys.iterator();
			
			while (iter.hasNext()) {
				
				String resKey = iter.next();
				
				String jsonType = specObj.get(resKey).getClass().getSimpleName();
				
				System.out.println( "[DEBUG] " + specRowNum + "행 Type은: " + jsonType );
				
				if ( jsonType.equals("String")) {
					
					String targetKey = this.extractValueSpecForString(specObj, resKey);
					
					if( targetKey != null) {
						
						for (int formatRowNum=0; formatRowNum < reqFormat.size(); formatRowNum++) {
							
							formatObj = (JSONObject) reqFormat.get(specRowNum);

							formatObj.replace(targetKey, word);
							System.out.println("[DEBUG 포멧 스펙: " + specObj.toString());
							System.out.println("[DEBUG] 변경된 규격: " + formatObj.toString());
							
						}
					} else {
						formatObj = (JSONObject) reqFormat.get(specRowNum);
					
						System.out.println("[DEBUG 포멧 스펙: " + specObj.toString());
					}
					
				} else if (jsonType.equals("JSONArray")) {
					
//					JSONArray arr = (JSONArray) specObj.get(resKey);
//					JSONArray resArr = new JSONArray();
//					
//					for (int arrRowNum=0; arrRowNum < arr.size(); arrRowNum++) {
//						
//						JSONObject obj = (JSONObject) arr.get(arrRowNum);
//						
//						resArr = this.extractValueSpecForArr(obj, word);
//						
//					}
					
				}
				
			}
			
			
		}
		
		System.out.println("[NOTIFICATION] 최종 생성된 규격 :" + formatObj.toJSONString());
		
		return formatObj;
		
		
	}
	
	public ArrayList<ExtractionKeynTypeForJSON> extractKeynTypeForJSON (JSONObject jsonObj) {
		
		ArrayList<ExtractionKeynTypeForJSON> extractList = new ArrayList<ExtractionKeynTypeForJSON>();
				
		JSONObject obj = jsonObj;
		Set<String> keys = obj.keySet();
		Iterator<String> iter = keys.iterator();
		
		while (iter.hasNext()) {
			
			ExtractionKeynTypeForJSON knt = new ExtractionKeynTypeForJSON();
			
			knt.setKey(iter.next());
			knt.setType(obj.get(iter.next()).getClass().getSimpleName());
			
			extractList.add(knt);
			
		}
				
		
		return extractList;
		 		
	}
	
	
	
	public String extractValueSpecForString (JSONObject specObj, String key) {
		
		String valType = "spec2";
		String derivedKey = null;
		
		if (valType.equals(specObj.get(key).toString())) {
			
			derivedKey = key;
		} 
		
		return derivedKey;
	}
	
	public JSONArray extractValueSpecForArr (JSONArray specObj, String key) {
		// arry 된 spec json이 들어오고, 해당 키값으로 안에를 다시 분석
		
		JSONArray arr = new JSONArray();
		
//		Set<String> keys = 
		
		
		return arr;
		
		
	}
	


}
