package com.kt.dataManager;

import java.util.Iterator;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.kt.dataForms.OwnServiceForm;
import com.kt.dataForms.OwnServiceList;


public class JSONParsingFrom {

	JSONParser parser = new JSONParser();
	JSONSerializerTo serializer = new JSONSerializerTo();

	public JSONObject setAuth (String response) {

		JSONObject res = null;

		try {

			JSONObject jsonObj = (JSONObject) parser.parse(response);

			// check ID/PW
			String id = jsonObj.get("id").toString();
			String pw = jsonObj.get("pw").toString();

			res = serializer.resultMsgforAuth(id, pw);


		} catch (ParseException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return res;

	}

	public JSONObject regiService(String response) {

		JSONObject res = null;

		try {

			OwnServiceList svcList = OwnServiceList.getInstance();
			OwnServiceForm form = new OwnServiceForm();

			JSONObject jsonObj = (JSONObject) parser.parse(response);

			Set keyName = jsonObj.keySet();

			Iterator<String> iter = keyName.iterator();

			//test source
			//			while(iter.hasNext()) {
			//				String keyname = iter.next();
			//				
			//				//key 값과 타입 구하
			//				System.out.println("KEY NAME: " + keyname + " KEY TYPE: " + jsonObj.get(keyname).getClass().getSimpleName());
			//			}

			while(iter.hasNext()) {

				//				KEY NAME: refAPI KEY TYPE: String
				//				KEY NAME: interfaceType KEY TYPE: String
				//				KEY NAME: serviceDesc KEY TYPE: String
				//				KEY NAME: refDialog KEY TYPE: JSONObject
				//				KEY NAME: method KEY TYPE: String
				//				KEY NAME: dataDefinition KEY TYPE: String
				//				KEY NAME: serviceCode KEY TYPE: String
				//				KEY NAME: userAuth KEY TYPE: String
				//				KEY NAME: intentName KEY TYPE: String
				//				KEY NAME: dataType KEY TYPE: String
				//				KEY NAME: targetURL KEY TYPE: String

				String name = iter.next();


				if (jsonObj.get(name).getClass().getSimpleName().equals("JSONObject")) {

					JSONObject subObj = (JSONObject) parser.parse(name);
					System.out.println(name + " TYPE: " + subObj.keySet().getClass().getSimpleName());

				} 
				
//				else if (name.equals("userAuth")) {
//					form.setUserAuth(name);				
//				} else if (name.equals("interfaceType")) {
//					form.setInterfaceType(name);
//				} else if (name.equals("serviceCode")) {
//					form.setServiceCode("name");
//				} else if (name.equals("refAPI")) {
//					form.setRefAPI(name);
//				} else if (name.equals("intentName")) {
//					form.setIntentName(name);
//				} else if (name.equals("serviceDesc")) {
//					form.setServiceDesc(name);
//				} else if (name.equals("targetURL")) {
//					form.setTargetURL(name);
//				} else if (name.equals("method")) {
//					form.setMethod(name);
//				} else if (name.equals("dataType")) {
//					form.setDataType(name);
//				} else if (name.equals("dataDefinition")) {
//					form.setDataDefinition(name);
//				}
			}

			svcList.getOwnList().add(form);

		} catch (ParseException e) {
			// TODO: handle exception
		}

		return res;
	}

}
