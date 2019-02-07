package com.kt.dataDao;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.TableMetadata;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilderDsl;
import com.kt.dataForms.DictionaryForm;
import com.kt.dataForms.OwnServiceForm;
import com.kt.dataManager.JSONParsingFrom;

public class InsertDataTo {
	
	ConnectToCanssandra connDB = new ConnectToCanssandra();
	SchemaBuilderDsl builder = new SchemaBuilderDsl();
	CreateTableFor createTable = new CreateTableFor();
	
	Cluster cluster;
	Session session;
	
	public InsertDataTo() {
		
		cluster = connDB.getCluster();
		session = cluster.connect();
	}
	
	public TableMetadata checkExsitingTable ( String usrAuth )

	{
		String tableName = usrAuth;

		KeyspaceMetadata ks = cluster.getMetadata().getKeyspace("domainks");
		TableMetadata table = ks.getTable(tableName);

		return table;

	}
	
	
	/**
	 * @author	: "Minwoo Ryu" [2019. 2. 1. 오후 6:05:54]
	 * desc	: auth 정보 미입력, 추가적으로 postman 프로토콜에서 비어있는 부분찾아서 여기와, 테이블 생성에 반영
	 * @version	:
	 * @param	: 
	 * @return 	: void 
	 * @throws 	: 
	 * @see		: 
	
	 * @param desc
	 */
	public void insertSVCDesc (OwnServiceForm desc) {
		
		JSONParsingFrom parseringFrom = new JSONParsingFrom();
						
		TableMetadata res = this.checkExsitingTable(desc.getUserAuth());
		
		if (res == null) {
			
			createTable.createTableForCommonDomain(desc.getUserAuth());
			
		}
		
		JSONObject obj = parseringFrom.convertIntentInfo(desc.getIntentInfo());
		
		Statement query = QueryBuilder.insertInto(desc.getUserAuth())
				.value("intentname", obj.get("id").toString())
				.value("servicecode", desc.getServiceCode())
				.value("commURL", desc.getComURL())
				.value("testURL", desc.getTestURL())
				.value("method", desc.getMethod())
				.value("datatype", desc.getDataType())
				.value("requestformat", desc.getReqStructure().toString())
				.value("requestspec", desc.getReqSpec().toString())
				.value("responseFormat", desc.getResStructure().toString())
				.value("responsespec", desc.getResSpec().toString())
				.value("dicList", obj.get("dicList").toString());
	}
	
	/**
	 * @author	: "Minwoo Ryu" [2019. 2. 7. 오후 6:17:39]
	 * desc	: 엑셀 양식에 따른 Intent 정보 저장
	 * 해당 Intent는 별도 table 구분 없이 모두 저장됨
	 * 현재는 table 명이 hardcoding 되어 있으며, 향후 변수로 처리 해야함
	 * @version	:
	 * @param	: 
	 * @return 	: void 
	 * @throws 	: 
	 * @see		: 
	
	 * @param listData
	 */
	public void insertIntents (ArrayList<DictionaryForm> listData) {
		
		SelectDataTo selectTo = new SelectDataTo();
		
		TableMetadata res = this.checkExsitingTable("intentInfo");
		
		int idx = 0;
		Statement query;
		
		if (res == null) {
			
			createTable.createTableForDictionary();
		}
		
		for (DictionaryForm data : listData) {
		
			if (selectTo.getLastRowForDicList() == 0) {
				
				idx = 1;
			} else {
				idx = selectTo.getLastRowForDicList() + 1;
			}
			
			query = QueryBuilder.insertInto("intentInfo")
					.value("seqNum", idx)
					.value("intentname", data.getIntentName())
					.value("intentDesc", data.getDesc())
					.value("dicList", data.getArr().toString());
			
			session.execute(query);
			
		}
		
		
		cluster.close();
		 
		
		
		
		
	}

}
