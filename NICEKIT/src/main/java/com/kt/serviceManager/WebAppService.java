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
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.kt.controller.model.ResReqService;
import com.kt.dataDao.SelectDataTo;
import com.kt.dataForms.DiscoveredServiceDESC;
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

		/* [Step3] Header 변경 */
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/json;charset=utf-8");

		/* [Step4] HTTP Request 전송 */
		StringBuilder urlBuilder = new StringBuilder(
				"http://api.visitkorea.or.kr/openapi/service/rest/KorService/detailIntro"); /* URL */

		try {

			urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "="
					+ param.get("ServiceKey").toString());/* Service Key */
			urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "="
					+ URLEncoder.encode(param.get("numOfRows").toString(), "UTF-8")); /* 한 페이지 결과 수 */
			urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "="
					+ URLEncoder.encode(param.get("pageNo").toString(), "UTF-8")); /* 현재 페이지 번호 */
			urlBuilder.append("&" + URLEncoder.encode("MobileOS", "UTF-8") + "=" + URLEncoder
					.encode(param.get("MobileOS").toString(), "UTF-8")); /* IOS(아이폰),AND(안드로이드),WIN(원도우폰),ETC */
			urlBuilder.append("&" + URLEncoder.encode("MobileApp", "UTF-8") + "="
					+ URLEncoder.encode(param.get("MobileApp").toString(), "UTF-8")); /* 서비스명=어플명 */
			urlBuilder.append("&" + URLEncoder.encode("contentTypeId", "UTF-8") + "="
					+ URLEncoder.encode(param.get("contentTypeId").toString(), "UTF-8")); /* 관광타입(관광지, 숙박 등) ID */
			urlBuilder.append("&" + URLEncoder.encode("_type", "UTF-8") + "="
					+ URLEncoder.encode(param.get("_type").toString(), "UTF-8")); /* 기본정보 조회여부 */
			urlBuilder.append("&" + URLEncoder.encode("contentId", "UTF-8") + "="
					+ URLEncoder.encode(param.get("contentId").toString(), "UTF-8")); /* 콘텐츠ID */

			URL url = new URL(urlBuilder.toString());

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod("GET");

			conn.setRequestProperty("Content-type", "application/json;charset=utf-8");
			BufferedReader rd;

			if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
				rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			} else {
				rd = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"));
			}
			StringBuilder sb = new StringBuilder();

			String line;
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}

			rd.close();

			conn.disconnect();

			/* [Step5] Res Data 중 해당 데이터 추출 */
			JsonSpecSvc jsonSpecSvc = new JsonSpecSvc();
			List<Map<String, String>> _msg = jsonSpecSvc.selectResMsg(sb.toString(), desc.getStrResSpec(), "설정 값");

			/* [Step6] 값 반환 */
			JsonParserSvc jsonParserSvc = new JsonParserSvc();
			JSONArray jsonMsg = jsonParserSvc.getJsonArrayFromList(_msg);
			ResReqService resMsg = new ResReqService();
			resMsg.setSource(jsonParserSvc.getJsonObject(sb.toString()));
			resMsg.setData(jsonMsg);

			return resMsg;

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
