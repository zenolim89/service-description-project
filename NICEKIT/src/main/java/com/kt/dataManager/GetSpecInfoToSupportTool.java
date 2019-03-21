package com.kt.dataManager;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.datastax.driver.core.Row;
import com.kt.dataDao.SelectDataTo;
import com.kt.dataForms.GetSpecInfoDataForm;

public class GetSpecInfoToSupportTool {

	SelectDataTo selectTo = new SelectDataTo();
	JSONParser parser = new JSONParser();
	JSONObject obj = new JSONObject();

	public String getSpecTable (String specName) {

		String targetTable = null;

		List<Row> list = selectTo.selectGetSpecId(specName);

//		if (list == null) {
//
//			obj = this.resNotFoundTemplate("요청하신 규격정보와 일치하는 규격을 찾을 수 없습니다");
//
//			return obj;
//
//		}

		for (Row table : list ) {

			targetTable = table.getString("specid");
		}

		return targetTable;

	}

	public GetSpecInfoDataForm resSpecData (String specName) throws ParseException {

		GetSpecInfoDataForm form = new GetSpecInfoDataForm();
		
		String table = this.getSpecTable(specName);

		List<Row> list = selectTo.selectGetSpecInfo(table);

		for(Row info : list) {

			form.setIntenName(info.getString("intentname"));
			form.setInvokeType(info.getString("invoketype"));
			form.setServiceCode(info.getString("servicecode"));
			form.setServiceDesc(info.getString("servicedesc"));
			form.setServiceLink(info.getString("servicelink"));
			form.setServiceName(info.getString("servicename"));
			form.setServiceType(info.getString("servicetype"));
			form.setWordList((JSONArray) parser.parse(info.getString("dicList")));
					
		}
		
		return form;

	}

}
