package com.kt.serviceManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import com.kt.controller.model.ResReqService;
import com.kt.dataDao.SelectDataTo;
import com.kt.dataForms.DiscoveredServiceDESC;
import com.kt.service.httpclient.RestClient;
import com.kt.service.spec.JsonSpecSvc;
import com.kt.sevice.jsonparser.JsonParserSvc;

@Service
public class WebAppService {

	/**
	 * 
	 * @param keySpace
	 * @param tableName
	 * @param intentName
	 * @param property
	 * @param word
	 */
	public ResReqService executeService(String keySpace, String tableName, String intentName, String property,
			String word) {

		keySpace = "vendorsvcks";
		property = "발화어휘";
		System.out.println("[DEBUG] 수신된 인텐트명: " + intentName + " 요청된 어휘: " + word + " 서비스 사업장 구분자:" + tableName);

		/* [Step1] DB 해당 조회 */
		SelectDataTo selectTo = new SelectDataTo();
		DiscoveredServiceDESC desc = selectTo.selectServiceInfo(keySpace, tableName, intentName);

		
		/* [Step2] Request 파라메터 변경 */
		String word_id = "";
		
		if (word.equals("을숙도")) {
			word_id = "2507834";
		} else if (word.equals("영동난계")) {
			word_id = "1118680";
		} else if (word.equals("가락몰")) {
			word_id = "2509701";
		} else if (word.equals("부여국악")) {
			word_id = "1258353";
		}

		JsonSpecSvc svc = new JsonSpecSvc();
		JSONObject temp = svc.createReqFormat(desc.getStrReqStructure(), desc.getStrReqSpec(), word, "발화 어휘");
		JSONObject param = svc.createReqFormat(temp.toString(), desc.getStrReqSpec(), word_id, "설정 값");

		System.out.println("[ 변경된 Request Message ]");
		System.out.println(param.toString());

		
		/* [Step3] REST */
		System.out.println("[method]" + desc.getMethod());
		RestClient client = new RestClient();
		String response = "";
				
		if(desc.getMethod().equals("POST")) {
			System.out.println("NOT Support Method");
		}else if(desc.getMethod().equals("GET")) {
			response = client.get(desc.getComURL(), desc.getHeaderInfo(), param);
		}else {
			System.out.println("NOT Support Method");
			return null;
		}
		
		
		/* [Step4] Res Data 중 해당 데이터 추출 */
		JsonSpecSvc jsonSpecSvc = new JsonSpecSvc();
		List<Map<String, String>> _msg = jsonSpecSvc.selectResMsg(response, desc.getStrResSpec(), "설정 값");

		
		/* [Step5] 값 반환 */
		JsonParserSvc jsonParserSvc = new JsonParserSvc();
		JSONArray jsonMsg = jsonParserSvc.getJsonArrayFromList(_msg);
		ResReqService resMsg = new ResReqService();
		resMsg.setSource(jsonParserSvc.getJsonObject(response));
		resMsg.setData(jsonMsg);

		return resMsg;

	}
}
