package com.kt.dataManager;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.kt.dataForms.IntentInfoForm;

public class HTMLSerializerTo {
	
	JSONParser parser = new JSONParser();
	
	/**
	 * @author	: "Minwoo Ryu" [2019. 2. 7. 오후 6:19:04]
	 * desc	: 현재의 저장된 Intent 정보를 가지고 오는 메소드
	 * 향후 이재동 박사 개발 내용과 병합 필요
	 * @version	:
	 * @param	: 
	 * @return 	: String 
	 * @throws 	: 
	 * @see		: 
	
	 * @param doName
	 * @return
	 */
	public String createHTMLForDic (String doName) {
		
		IntentInfoForm doList = new IntentInfoForm(); 
		String responseHTML = "";
		
//		try {
//			
//			for (int i=0; i < list.getDicList().size(); i++) {
//				if(list.getDicList().get(i).getDomainName().equals(doName)) {
//					doList = list.getDicList().get(i);
//					Hashtable<String, ArrayList<String>> temp = doList.getDictionaryList();
//					
//					Set <String> keys = temp.keySet();
//					
//					Iterator<String> iter = keys.iterator();
//					
//					while (iter.hasNext()) {
//						String keyName = iter.next();
//						
//						responseHTML += "<tr> \n";
//						responseHTML += "<th>" + keyName + "</th><td> \n"
//								+ "<div class=\"form-check\"> \n";
//						
//						for (int j=0; j < temp.get(keyName).size(); j++) {
//							responseHTML += "<label> <input class=\"option-input checkbox\""
//									+ "type=\"checkbox\" name=" +"\""+ keyName +"\"" + "value="+"\""+temp.get(keyName).get(j)+"\""+">"
//									+ "<span class=\"label-text\">" + temp.get(keyName).get(j) + "</span></label> \n";
//						}
//						responseHTML += "</div> \n"
//								+ "</td> \n"
//								+ "</tr> \n";
//					}
//					
//				}
//			}
//			
//			
//		}catch (Exception e) {
//			// TODO: handle exception
//		}
		
		System.out.println("[DEBUG 동적 HTML코드 (도메인 사전) 생성 결과]: \n" + responseHTML + "\n");
		return responseHTML;
		
	}

}
