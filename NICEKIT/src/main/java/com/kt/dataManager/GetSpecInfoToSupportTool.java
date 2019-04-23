package com.kt.dataManager;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.datastax.driver.core.Row;
import com.kt.dataDao.SelectDataTo;
import com.kt.dataForms.GetSpecInfoDataForm;

public class GetSpecInfoToSupportTool {

	JSONParser parser = new JSONParser();
	JSONSerializerTo serializerTo = new JSONSerializerTo();
	JSONObject obj = new JSONObject();

	public String getSpecTable(String specName) {
		SelectDataTo selectTo = new SelectDataTo();
		String targetTable = null;
		List<Row> list = selectTo.selectGetSpecId(specName);
		for (Row table : list) {
			targetTable = table.getString("specid");
		}
		return targetTable;
	}

	public ArrayList<GetSpecInfoDataForm> resSpecData(String specName) throws ParseException {
		SelectDataTo selectTo = new SelectDataTo();
		ArrayList<GetSpecInfoDataForm> specList = new ArrayList<GetSpecInfoDataForm>();
		String table = this.getSpecTable(specName);
		try {
			List<Row> list = selectTo.selectGetSpecInfo(table);
			for (Row info : list) {
				GetSpecInfoDataForm form = new GetSpecInfoDataForm();
				form.setIntenName(info.getString("intentname"));
				form.setInvokeType(info.getString("invoketype"));
				form.setServiceCode(info.getString("servicecode"));
				form.setServiceDesc(info.getString("servicedesc"));
				form.setServiceLink(info.getString("servicelink"));
				form.setServiceName(info.getString("servicename"));
				form.setServiceType(info.getString("servicetype"));
				form.setWordList((JSONArray) parser.parse(info.getString("dicList")));
				specList.add(form);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return specList;
	}

	public ArrayList<GetSpecInfoDataForm> resServiceList(String specName) throws ParseException {
		SelectDataTo selectTo = new SelectDataTo();
		ArrayList<GetSpecInfoDataForm> serviceList = new ArrayList<GetSpecInfoDataForm>();
		String table = this.getSpecTable(specName);
		try {
			List<Row> list = selectTo.selectGetSpecInfo(table);
			for (Row info : list) {
				GetSpecInfoDataForm form = new GetSpecInfoDataForm();
				form.setServiceName(info.getString("servicename"));
				serviceList.add(form);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return serviceList;
	}
}
