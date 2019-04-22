package com.kt.service.httpclient;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class RestClient {
	
	  private String server = "http://api.visitkorea.or.kr";
	  private RestTemplate rest;
	  private HttpHeaders headers;
	  private HttpStatus status;

	  public RestClient() {
		    this.rest = new RestTemplate();
		    this.headers = new HttpHeaders();
		    headers.add("Content-Type", "application/json");
		    headers.add("Accept", "*/*");
		  }
	 
	  public String get(String url_a) {
		  
		  HttpEntity entity = new HttpEntity(headers);

//		  url = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/detailIntro";
//		  
//		  UriComponentsBuilder  uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
//          .queryParam("ServiceKey", "BUkvEL0CbHrEg0Al/w+Kmn+9TFX2N51WYnGicon2kT/bUbxFeAvHmJJ900cUGKu6TA7V4o/fMtP1qpm41urzXw==")
//					  .queryParam("MobileOS", "ETC")
//					  .queryParam("MobileApp", "AppTest")
//					  .queryParam("contentId", "1118680")
//					  .queryParam("numOfRows", "1")
//					  .queryParam("pageNo", "1")
//					  .queryParam("contentTypeId", "15")
//					  .queryParam("_type", "json");
//		  
//		  HttpEntity<String> response;
//		  response = rest.exchange(uriBuilder.build().encode().toUriString(), HttpMethod.GET, entity, String.class);
//		  return response.getBody();
		  
		  
		  
//		  
//		  try {
//			String str = URLDecoder.decode("BUkvEL0CbHrEg0Al%2Fw%2BKmn%2B9TFX2N51WYnGicon2kT%2FbUbxFeAvHmJJ900cUGKu6TA7V4o%2FfMtP1qpm41urzXw%3D%3D", "UTF-8");
//			System.out.println(str);
//			
//			
//			
//		} catch (UnsupportedEncodingException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		  
//		  
//		  
			
//		  StringBuilder urlBuilder = new StringBuilder("http://api.visitkorea.or.kr/openapi/service/rest/KorService/detailIntro"); /*URL*/
//	        try {
//				urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=" + URLEncoder.encode("BUkvEL0CbHrEg0Al/w+Kmn+9TFX2N51WYnGicon2kT/bUbxFeAvHmJJ900cUGKu6TA7V4o/fMtP1qpm41urzXw==", "UTF-8"));/*공공데이터포털에서*/
//	        urlBuilder.append("&" + URLEncoder.encode("MobileOS","UTF-8") + "=" + URLEncoder.encode("ETC", "UTF-8")); /*IOS(아이폰),AND(안드로이드),WIN(원도우폰),ETC*/
//	        urlBuilder.append("&" + URLEncoder.encode("MobileApp","UTF-8") + "=" + URLEncoder.encode("AppTest", "UTF-8")); /*서비스명=어플명*/
//	        urlBuilder.append("&" + URLEncoder.encode("contentId","UTF-8") + "=" + URLEncoder.encode("1118680", "UTF-8")); /*콘텐츠ID*/
//	        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*한 페이지 결과 수*/
//	        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*현재 페이지 번호*/
//	        urlBuilder.append("&" + URLEncoder.encode("contentTypeId","UTF-8") + "=" + URLEncoder.encode("15", "UTF-8")); /*관광타입(관광지, 숙박 등) ID*/
//	        urlBuilder.append("&" + URLEncoder.encode("_type","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*기본정보 조회여부*/
//	        URL url = new URL(urlBuilder.toString());
//			  HttpEntity<String> response;
//			  response = rest.exchange(url.toString(), HttpMethod.GET, entity, String.class);
//			  return response.getBody();
//			} catch (UnsupportedEncodingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (MalformedURLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			return null;
//		  

		  

			
			
        
        
		  String temp = "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty?sidoName=서울&pageNo=1&numOfRows=10&ServiceKey=서비스키&ver=1.3&_returnType=json";
		  HttpEntity<String> response = rest.exchange(temp,
				    HttpMethod.GET,
				    entity,
				    String.class
				);
		  return response.getBody();
		  
//		  String temp = "http://tour.chungnam.net/_prog/openapi/?func=experience&start=1&end=2";
//		  HttpEntity<String> response = rest.exchange(temp,
//				    HttpMethod.GET,
//				    entity,
//				    String.class
//				);
		  
		  
		  

		  }
	  
	  public String post(String uri, String json) {   
		    HttpEntity<String> requestEntity = new HttpEntity<String>(json, headers);
		    ResponseEntity<String> responseEntity = rest.exchange(server + uri, HttpMethod.POST, requestEntity, String.class);
		    this.setStatus(responseEntity.getStatusCode());
		    return responseEntity.getBody();
		  }
	  
	  public HttpStatus getStatus() {
		    return status;
		  }

		  public void setStatus(HttpStatus status) {
		    this.status = status;
		  } 
		  
	  
	  
}
