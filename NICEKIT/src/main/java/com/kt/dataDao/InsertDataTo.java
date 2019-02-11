package com.kt.dataDao;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.TableMetadata;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilderDsl;
import com.datastax.oss.driver.internal.core.metadata.MetadataRefresh.Result;
import com.kt.dataForms.BaseIntentInfoForm;
import com.kt.dataForms.BaseSvcForm;
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
	
	public TableMetadata checkExsitingTable ( String tableName, String ksName )

	{
		String tn = tableName;

		KeyspaceMetadata ks = cluster.getMetadata().getKeyspace(ksName);
		TableMetadata table = ks.getTable(tn);

		return table;

	}
	
	
	/**
	 * @author	: "Minwoo Ryu" [2019. 2. 1. 오후 6:05:54]
	 * desc	: 기존 프로토콜에서 usrAuth는 제외, 그리고 클라이언트에서 hardcording으로
	 * domainname과 domainid를 추가하였음
	 * @version	:
	 * @param	: 
	 * @return 	: void 
	 * @throws 	: 
	 * @see		: 
	
	 * @param desc
	 */
	public void insertDomainSvcTo (BaseSvcForm desc) {
		
		JSONParsingFrom parseringFrom = new JSONParsingFrom();
		
		String keySpace = "domainks";
						
		TableMetadata res = this.checkExsitingTable(desc.getDomainName(), keySpace);
		
		if (res == null) {
			
			createTable.createTableForCommonDomain(desc.getDomainName());
			
		}
		
		JSONObject obj = parseringFrom.convertIntentInfo(desc.getIntentInfo());
		
		
		Statement query = QueryBuilder.insertInto(keySpace, desc.getDomainName())
				.value("intentname", obj.get("id").toString())
				.value("domainid", desc.getDomainId())
				.value("servicecode", desc.getServiceCode())
				.value("commURL", desc.getComURL())
				.value("testURL", desc.getTestURL())
				.value("method", desc.getMethod())
				.value("datatype", desc.getDataType())
				.value("requestformat", desc.getReqStructure().toString())
				.value("requestspec", desc.getReqSpec().toString())
				.value("responseFormat", desc.getResStructure().toString())
				.value("responsespec", desc.getResSpec().toString())
				.value("dicList", obj.get("dicList").toString()).ifNotExists();
		
		ResultSet resSet = session.execute(query);
		
		System.out.println(resSet.toString());
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
	public void insertDomainIntent (ArrayList<BaseIntentInfoForm> listData) {
		
		SelectDataTo selectTo = new SelectDataTo();
		
		String keySpace = "commonks";
		String targetTable = "intentInfo";
		
		TableMetadata res = this.checkExsitingTable(targetTable, keySpace);
		
		int idx = 0;
		Statement query;
		
		if (res == null) {
			
			createTable.createTableForDictionary();
		}
		
		for (BaseIntentInfoForm data : listData) {
		
			ResultSet rs = selectTo.getLastRowForDicList(keySpace, targetTable);
			
			Row row = rs.one();
						
//			if (row == null) {
//				
//				idx = 1;
//				
//			} else {		
//				idx = (row.getInt("seqnum")) + 1;
//			}
			
			query = QueryBuilder.insertInto(keySpace, targetTable)
//					.value("seqNum", idx)
					.value("intentname", data.getIntentName())
					.value("intentDesc", data.getDesc())
					.value("dicList", data.getArr().toString());
			
			session.execute(query);
			
		}
		
		
		cluster.close();
		 
		
		
		
		
	}

}
