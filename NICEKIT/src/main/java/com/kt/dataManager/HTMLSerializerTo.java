package com.kt.dataManager;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.kt.dataDao.SelectDataTo;
import com.kt.dataForms.BaseDictionarySet;
import com.kt.dataForms.BaseIntentInfoForm;

public class HTMLSerializerTo {
	
	JSONParser parser = new JSONParser();
	
	/**
	 * @author	: "Minwoo Ryu" [2019. 2. 7. 오후 6:19:04]
	 * desc	: commonks에서 등록된 Intent의 모든 정보 중 특정 intentName과 관련된 사전 정보를 가지고 옴
	 * 향후 이재동 박사가 개발한 내용과 병합하여, 관련 키워드로 검색하여 변경하는 방침도 고민 필요
	 * @version	: 0.1
	 * @param	: intent name
	 * @return 	: String 
	 * @throws 	: 
	 * @see		: 
	
	 * @param doName
	 * @return
	 */
	public String createHTMLForIntentInfo (String intentName) {
		
		SelectDataTo selectTo = new SelectDataTo();
		JSONParsingFrom parsingFrom = new JSONParsingFrom();
		
		
		String ksName = "commonks";
		String targetTable = "intentInfo";
		
		String reqIntentName = intentName;
		String responseHTML ="";
		ArrayList<BaseIntentInfoForm> resList;
						
		try {
			
			resList = selectTo.selectIntentInfo(ksName, targetTable, reqIntentName);
			
			for (BaseIntentInfoForm form : resList) {
				
				ArrayList<BaseDictionarySet> dicList = parsingFrom.parsingIntentInfo(form.getArr());
				
				for (BaseDictionarySet set : dicList) {
					
					responseHTML += "<tr> \n";
					responseHTML += "<th>" + set.getDicName() + "</th><td> \n"
							+ "<div class=\"form-check\"> \n";
					
					for (String word : set.getWordList()) {
						
						responseHTML += "<label> <input class=\"option-input checkbox\""
								+ "type=\"checkbox\" name=" +"\""+ set.getDicName() +"\"" + "value="+"\""+ word +"\""+">"
								+ "<span class=\"label-text\">" + word + "</span></label> \n";
						
					}
					
					responseHTML += "</div> \n"
							+ "</td> \n"
							+ "</tr> \n";
					
				}
								
				
				
			}
			
		} catch (Exception e) {
			
			e.getMessage();
			
		}
					
		System.out.println("[DEBUG 동적 HTML코드 (도메인 사전) 생성 결과]: \n" + responseHTML + "\n");
		return responseHTML;
		
	}

}
