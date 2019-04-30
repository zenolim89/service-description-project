package com.kt.service.httpclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.util.List;
import org.json.simple.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import net.sf.json.JSON;
import com.kt.dataForms.HttpParam;
import com.kt.sevice.jsonparser.JsonParserSvc;

public class RestClient {

	public String get(String _url, List<HttpParam> _header, JsonNode _param) {

//
//		StringBuilder urlBuilder = new StringBuilder(_url);
//		
//		if(_param != null) {
//			JsonParserSvc svc = new JsonParserSvc();
//			JSONObject param = svc.getJsonObject(_param.toString());
//			
//			Iterator<String> fieldNames = _param.fieldNames();
//		}
//		
//		try {
//
//			
//			
//			
//						
//			urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=" + param.get("ServiceKey").toString()); /*Service Key*/  
//			urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode(param.get("numOfRows").toString(), "UTF-8")); /*한 페이지 결과 수*/
//	        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode(param.get("pageNo").toString(), "UTF-8")); /*현재 페이지 번호*/
//	        urlBuilder.append("&" + URLEncoder.encode("MobileOS","UTF-8") + "=" + URLEncoder.encode(param.get("MobileOS").toString(), "UTF-8")); /*IOS(아이폰),AND(안드로이드),WIN(원도우폰),ETC*/
//	        urlBuilder.append("&" + URLEncoder.encode("MobileApp","UTF-8") + "=" + URLEncoder.encode(param.get("MobileApp").toString(), "UTF-8")); /*서비스명=어플명*/
//	        urlBuilder.append("&" + URLEncoder.encode("contentTypeId","UTF-8") + "=" + URLEncoder.encode(param.get("contentTypeId").toString(), "UTF-8")); /*관광타입(관광지, 숙박 등) ID*/
//	        urlBuilder.append("&" + URLEncoder.encode("_type","UTF-8") + "=" + URLEncoder.encode(param.get("_type").toString(), "UTF-8")); /*기본정보 조회여부*/
//	        urlBuilder.append("&" + URLEncoder.encode("contentId","UTF-8") + "=" + URLEncoder.encode(param.get("contentId").toString(), "UTF-8")); /*콘텐츠ID*/
//	        
//	        
//	        System.out.println(urlBuilder.toString());
//			
//	        URL url = new URL(urlBuilder.toString());
//			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//
//			conn.setRequestMethod("GET");
//			
//			for (HttpParam httpParam : _header) {
//				if(!(httpParam.getParameter().equals("")||httpParam.getValue().equals(""))) {
//					conn.setRequestProperty(httpParam.getParameter(), httpParam.getValue());
//				}
//			}
//			
//			BufferedReader rd;
//
//			if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
//				rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
//			} else {
//				rd = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"));
//			}
//
//			StringBuilder sb = new StringBuilder();
//
//			String line;
//			while ((line = rd.readLine()) != null) {
//				sb.append(line);
//			}
//
//			rd.close();
//			conn.disconnect();
//
//			return sb.toString();
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

		return null;
	}

	public String post(String _url, List<HttpParam> _header, String param) {

		StringBuilder urlBuilder = new StringBuilder(_url);

		try {

			URL url = new URL(urlBuilder.toString());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod("POST");

			for (HttpParam httpParam : _header) {
				if (!(httpParam.getParameter().equals("") || httpParam.getValue().equals(""))) {
					conn.setRequestProperty(httpParam.getParameter(), httpParam.getValue());
				}
			}

			conn.setDoOutput(true);

			byte[] input = param.getBytes("utf-8");
			conn.getOutputStream().write(input, 0, input.length);

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

			return sb.toString();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public String getForEncode(String _url, List<HttpParam> _header, JsonNode _param) {

		StringBuilder urlBuilder = new StringBuilder(_url);

		try {

			JsonParserSvc svc = new JsonParserSvc();
			JSONObject param = svc.getJsonObject(_param.toString());
			urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "="
					+ param.get("ServiceKey").toString()); /* Service Key */
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

			System.out.println(urlBuilder.toString());

			URL url = new URL(urlBuilder.toString());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod("GET");

			for (HttpParam httpParam : _header) {
				if (!(httpParam.getParameter().equals("") || httpParam.getValue().equals(""))) {
					conn.setRequestProperty(httpParam.getParameter(), httpParam.getValue());
				}
			}

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

			return sb.toString();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public JSONObject doJSONBodyPost(String _url, HashMap<String, String> _headerMap,
			HashMap<String, String> _paramMap) {

		StringBuilder urlBuilder = new StringBuilder(_url);

		try {

			URL url = new URL(urlBuilder.toString());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod("POST");

			this.setHeader(conn, _headerMap);
			this.setJSONBody(conn.getOutputStream(), _paramMap);

			JSONObject jsonObject = this.getResponseJSON(conn);

			conn.disconnect();

			return jsonObject;

		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	private void setHeader(HttpURLConnection _connection, HashMap<String, String> _headerMap) {

		_connection.setDoOutput(true);

		for (String key : _headerMap.keySet()) {

			_connection.setRequestProperty(key, _headerMap.get(key));

		}

	}

	private void setJSONBody(OutputStream _outputStream, HashMap<String, String> _headerMap) {

		JSONObject jsonObejct = new JSONObject(_headerMap);

		try {
			_outputStream.write(jsonObejct.toString().getBytes("UTF-8"));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				_outputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private JSONObject getResponseJSON(HttpURLConnection _connection) {

		String resultString = null;
		JSONObject jsonObject = null;

		try {
			BufferedReader rd;
			rd = new BufferedReader(new InputStreamReader(_connection.getInputStream(), "UTF-8"));

			StringBuilder sb = new StringBuilder();

			String line;
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}

			rd.close();
			resultString = sb.toString();
			jsonObject = (JSONObject) new JSONParser().parse(resultString);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return jsonObject;
	}
}
