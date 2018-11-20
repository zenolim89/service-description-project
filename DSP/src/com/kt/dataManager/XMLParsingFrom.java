package com.kt.dataManager;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;

public class XMLParsingFrom {
	
	public void requestServiceFromDialog (String response) {
		
	String res = null;
	
		try {
			Document doc = DocumentHelper.parseText(response);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
