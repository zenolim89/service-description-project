package com.kt.dataManager;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.kt.dataDao.DictionaryList;
import com.kt.dataForms.DictionaryForm;

public class HTMLSerializerTo {
	
	JSONParser parser = new JSONParser();
	
	public String createHTMLForDic (String doName) {
		
		DictionaryList list = DictionaryList.getInstance();
		DictionaryForm doList = new DictionaryForm(); 
		String responseHTML = "";
		
		try {
			
			for (int i=0; i < list.getDicList().size(); i++) {
				if(list.getDicList().get(i).getDomainName().equals(doName)) {
					doList = list.getDicList().get(i);
					Hashtable<String, ArrayList<String>> temp = doList.getDictionaryList();
					
					Set <String> keys = temp.keySet();
					
					Iterator<String> iter = keys.iterator();
					
					while (iter.hasNext()) {
						String keyName = iter.next();
						
						responseHTML += "<tr> \n";
						responseHTML += "<th>" + keyName + "</th> \n"
								+ "<div class=\"form-check\"> \n";
						
						for (int j=0; j < temp.get(keyName).size(); j++) {
							responseHTML += "<label> <input class=\"option-input checkbox\""
									+ "type=\"checkbox\" name=" + keyName + "value=\"food\">"
									+ "<span class=\"label-text\">" + temp.get(keyName).get(j) + "</span></label> \n";
						}
						responseHTML += "</div> \n"
								+ "</td> \n"
								+ "</tr> \n";
					}
					
				}
			}
			
			
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		System.out.println("[DEBUG 동적 HTML코드 (도메인 사전) 생성 결과]: \n" + responseHTML + "\n");
		return responseHTML;
		
	}

}
