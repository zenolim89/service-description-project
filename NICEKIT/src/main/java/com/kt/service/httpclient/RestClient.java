package com.kt.service.httpclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import com.fasterxml.jackson.databind.JsonNode;
import org.json.simple.JSONArray;

public class RestClient {

	
	public String get(String _url, JSONArray header, JsonNode param) {

		StringBuilder urlBuilder = new StringBuilder(_url);

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

			return sb.toString();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	
	public String post(String _url, JSONArray header, String param) {

		StringBuilder urlBuilder = new StringBuilder(_url);

		try {

			URL url = new URL(urlBuilder.toString());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod("POST");

			
			conn.setRequestProperty("Content-type", "application/json");
			conn.setRequestProperty("svcAccsToken", "eyJhbGciOiJIUzI1NiJ9.eyJiMmJTZXEiOjEsInN2Y1RndFNlcSI6MjAsImlmVHlwZUNkIjoiUkNVIiwicm9vbU5vIjoiMTE2IiwiaWF0IjoxNTU2MDY2MjcxLCJzdWIiOiJzdmNBY2NzVG9rZW4iLCJleHAiOjE1NTg0ODU0NzF9.tRK_P6etW6OhWlhzQ9zEfzZ8mHHdG3Lj38eRgAGPL-Y");
			conn.setRequestProperty("langCd", "ko");
			
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

}
