package com.kt.serviceManager;

import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.kt.commonUtils.Constants;
import com.kt.controller.model.ResReqService;
import com.kt.data.model.HotelWordId;
import com.kt.dataDao.SelectDataTo;
import com.kt.dataForms.DiscoveredServiceDESC;
import com.kt.dataForms.HttpParam;
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
			String _word, String token) {
		property = "발화어휘";
		System.out.println("[DEBUG] 수신된 인텐트명: " + intentName + " 요청된 어휘: " + _word + " 서비스 사업장 구분자:" + tableName);

		/* [Step1] DB 해당 조회 */
		SelectDataTo selectTo = new SelectDataTo();
		DiscoveredServiceDESC desc = selectTo.selectServiceInfo(Constants.CASSANDRA_KEYSPACE_VENDOR, tableName,
				intentName);

		/* [Step2] Request 파라메터 변경 */
		String word = "";
		String word_id = "";

		if (_word.equals("을숙도")) {
			word = "을숙도";
			word_id = "2507834";
		} else if (_word.equals("영동난계")) {
			word = "영동난계";
			word_id = "1118680";
		} else if (_word.equals("가락몰")) {
			word = "가락몰";
			word_id = "2509701";
		} else if (_word.equals("부여국악")) {
			word = "부여국악";
			word_id = "1258353";
		}
		else {
			
			HotelWordId hotelWordId = new HotelWordId();
			word_id = hotelWordId.getWordId(_word);
			word = _word;
		}

		JsonSpecSvc svc = new JsonSpecSvc();
		JsonNode temp = svc.createReqFormat(desc.getStrReqStructure(), desc.getStrReqSpec(), word, "발화 어휘");
		JsonNode param = svc.createReqFormat(temp.toString(), desc.getStrReqSpec(), word_id, "설정 값");

		System.out.println("[ 변경된 Request Message ]");
		System.out.println(param.toString());

		/* [Step3] REST */
		System.out.println("[method]" + desc.getMethod());
		RestClient client = new RestClient();
		String response = "";
		
		/*[Step3-1] Header 토큰 삽입*/
		List<HttpParam> header = desc.getHeader();
		for (HttpParam el : header) {
			if(el.getNote().equals("token")) {
				el.setValue(token);
			}
		}
		
		/*[Step3-2] HTTP 전송 */
		if (desc.getMethod().equals("POST")) {
			response = client.post(desc.getComURL(), header, param.toString());
		} else if (desc.getMethod().equals("GET")) {
			if(desc.getComURL().equals("http://api.visitkorea.or.kr/openapi/service/rest/KorService/detailIntro")) 
			{
				response = client.getForEncode(desc.getComURL(), header, param);
			}else {
				response = client.get(desc.getComURL(), header, param);
			}
		} else {
			System.out.println("NOT Support Method");
			return null;
		}

		/* [Step4] Res Data 중 해당 데이터 추출 */
		JsonSpecSvc jsonSpecSvc = new JsonSpecSvc();
		Map<String, String> _msg = jsonSpecSvc.selectResMsg(response, desc.getStrResSpec(), "설정 값");

		/* [Step5] 값 반환 */
		JsonParserSvc jsonParserSvc = new JsonParserSvc();
		JSONObject jsonMsg = jsonParserSvc.getJsonStringFromMap(_msg);
		ResReqService resMsg = new ResReqService();
		resMsg.setSource(jsonParserSvc.getJsonObject(response));
		resMsg.setData(jsonMsg);

		return resMsg;

	}
}
