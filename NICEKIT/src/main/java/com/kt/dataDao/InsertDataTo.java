package com.kt.dataDao;

import java.util.ArrayList;
import java.util.List;

import org.apache.cassandra.locator.SeedProvider;
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
import com.datastax.oss.driver.internal.core.cql.ResultSets;
import com.datastax.oss.driver.internal.core.metadata.MetadataRefresh.Result;
import com.kt.dataForms.BaseIntentInfoForm;
import com.kt.dataForms.BaseSvcForm;
import com.kt.dataForms.BaseVenderSvcForm;
import com.kt.dataForms.ReqSvcCodeForm;
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
	
	public Boolean insertDomainList (String domainName) {
		
		
		String keySpace = "commonks";
		String tableName = "domainlist";
		Boolean response = null;
		
		TableMetadata res = this.checkExsitingTable(tableName, keySpace);
		
		if (res == null) {
			
			createTable.createTableForDomain(keySpace, tableName);
			
		}
		
		Statement query = QueryBuilder.insertInto(keySpace, tableName)
				.value("domainname", domainName).ifNotExists();
		
		ResultSet resSet = session.execute(query);
		
		List<Row> resList = resSet.all();
		
		for(Row row : resList) {
			
			response = row.getBool(0);
		
		}
		
		return response;
		
	}
	
	public void insertDomainService (ReqSvcCodeForm form) {
		
		SelectDataTo selectTo = new SelectDataTo();
		
		String keySpace = "commonks";
		String table = "domainservicelist";
		
		TableMetadata res = this.checkExsitingTable(table, keySpace);
		
		if (res == null) {
			
			createTable.createDomainServiceList();
			
		}
		
		Boolean checkSvcType = selectTo.isExistedItem(keySpace, table, form.getServiceType());
		
		if (checkSvcType == false) {
			
			int code = 0001;
			
			Statement query = QueryBuilder.insertInto(keySpace, table)
					.value("servicetype", form.getServiceType())
					.value("servicecode", code)
					.value("domainname", form.getDomainName())
					.value("servicedesc", form.getServiceDesc());
			
			session.execute(query);
			
		}
		
		selectTo.selectServiceCode(keySpace, table, form.getServiceType());
		
		
		
				
		
		
	}
	
	public ResultSet insertSpecIndexTo (String specName, String domainName) {
		
		String keySpace = "commonks";
		String tableName = "specindexList";
		
		TableMetadata res = this.checkExsitingTable(tableName, keySpace);
		
		if (res == null) {
			createTable.createTableForSpecIndexList();
		}
		
		Statement query = QueryBuilder.insertInto(keySpace, tableName).ifNotExists()
				.value("specname", specName)
				.value("domainname", domainName);
				
		
		
		
		ResultSet resSet = session.execute(query);
		
		return resSet;

		
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

//		if (res == null) {
//
//			createTable.createTableFor(keySpace, desc.getDomainName());
//
//		}

		JSONObject obj = parseringFrom.convertIntentInfo(desc.getIntentInfo());


		Statement query = QueryBuilder.insertInto(keySpace, desc.getDomainName())
				.value("domainname", desc.getDomainName())
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

	public void insertVenderSvcTo (ArrayList<BaseVenderSvcForm> descList) {

		JSONParsingFrom parseringFrom = new JSONParsingFrom();

		String keySpace = "vendersvcks";

		for (BaseVenderSvcForm desc : descList) {

			TableMetadata res = this.checkExsitingTable(desc.getDomainId(), keySpace);

//			if (res == null) {
//
//				createTable.createTableFor(keySpace, desc.getDomainId());
//
//			}

			JSONObject obj = parseringFrom.convertIntentInfo(desc.getIntentInfo());


			Statement query = QueryBuilder.insertInto(keySpace, desc.getDomainId())
					.value("domainname", desc.getDomainName())
					.value("intentname", obj.get("id").toString())
					.value("domainid", desc.getDomainId())
					// added to argument for demo
					.value("tourl", desc.getToUrl())
					.value("servicetype", desc.getSvcType())
					.value("servicecode", desc.getServiceCode())
					//
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
