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
import com.datastax.driver.core.SimpleStatement;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.TableMetadata;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilderDsl;
import com.datastax.oss.driver.internal.core.cql.ResultSets;
import com.datastax.oss.driver.internal.core.metadata.MetadataRefresh.Result;
import com.kt.dataForms.BaseIntentInfoForm;
import com.kt.dataForms.BaseSvcForm;
import com.kt.dataForms.BaseVenderSvcForm;
import com.kt.dataForms.ReqCreateVender;
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
	
	public String insertVenderToIndexList (ReqCreateVender vender) {
		
		SelectDataTo selectTo = new SelectDataTo();
		
		String keySpace = "vendersvcks";
		String table = "venderindexlist";
		
		String resCode;
		
		TableMetadata res = this.checkExsitingTable(table, keySpace);
		
		if (res == null) {
			
			createTable.createTableForVenderList();
		}
		
		Boolean existed = selectTo.isExistedItem(keySpace, table, "vendername", vender.getVender());
		
		if (existed == true) {
			
			resCode = "409";
			
			return resCode;
			
		} else {
			
			Statement query = QueryBuilder.insertInto(keySpace, table).ifNotExists()
					.value("vendername", vender.getVender())
					.value("domainname", vender.getDomainName())
					.value("templatedicpath", vender.getTemplateUrl());
			
			session.execute(query);
			
			resCode = "201";
			
			return resCode;
			
			
		}
		
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
	
	
	/**
	 * @author	: "Minwoo Ryu" [2019. 3. 16. 오 16:54:27]
	 * desc	: 서비스 코드 요청을 위한 서비스 간단 명세를 수신 후 해당 값을 기반으로 commonks 내 domainservicelist 테이블에 명세를 저장하고
	 *        서비스 코드를 발급, 이때 발급되는 코드는 본 수신 명세와 함께 domainservicelist에 함께 저장된 후 코드 값을 리턴
	 *        수행절차: (1) commonks 내 domainservicelist 테이블 존재여부 확인 후 존재하지 않을 경우 생성 (참고: CreateTableTo.createDomainServiceList()
	 *                (2-1) domainservicelist 테이블에 동일한 서비스명을 가지는 Row가 있는지 확인 후 해당 값이 존재할 경우 serviceCode를 409로 리턴 
	 *                (2-2) domainservicelist 테이블에 동일한 서비스명을 가지는 Row가 없을 경우 수신 받은 명세 값을 등록, 이때 seqnum은 domainservicelist
	 *                      테이블의 전체 row size의 + 1로 정의되며, 서비스 코드 발급 규칙은 "도메인 이름_서비스타입_일련번호"로 정의
	 *                      일련번호의 경우 int 타입으로 정의되며 1부터 시작
	 *                
	 * @version	: 0.1
	 * @return 	: String code (409 또는 발행된 service code 값)
	 * @throws 	: 
	 * @see		: SelectDataTo.isExistedItem(); SelectDataTo.selectNumberOfRows

	 * @param 
	 * @return
	 */
	public String createServiceCodeNinsertService (ReqSvcCodeForm form) {
		
		SelectDataTo selectTo = new SelectDataTo();
		
		String keySpace = "commonks";
		String table = "domainservicelist";
		
		String code;
		
		TableMetadata res = this.checkExsitingTable(table, keySpace);
		
		if (res == null) {
			
			createTable.createDomainServiceList();
			
		}
		
		Boolean checkItem = selectTo.isExistedItem(keySpace, table, "servicename", form.getServiceName());
		
		if (checkItem == true) {
			
			code = "409";
			
			return code;
			
		}
		
		
		int num = selectTo.selectNumberOfRows(keySpace, table) + 1;
		
		Statement query = QueryBuilder.insertInto(keySpace, table)
				.value("seqnum", num)
				.value("domainname", form.getDomainName())
				.value("servicetype", form.getServiceType())
				.value("servicecode", form.getDomainName() + "_" + form.getInvokeType() + "_" + num)
				.value("servicename", form.getServiceName())
				.value("invoketype", form.getInvokeType())
				.value("servicedesc", form.getServiceDesc());
		
		session.execute(query);
		
		code = form.getDomainName() + "_" + form.getServiceType() + "_" + num;
		
		return code;
		
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
