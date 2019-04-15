package com.kt.dataDao;

import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONObject;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.TableMetadata;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.kt.commonUtils.Constants;
import com.kt.dataForms.BaseIntentInfoForm;
import com.kt.dataForms.BaseSvcForm;
import com.kt.dataForms.BaseVenderSvcForm;
import com.kt.dataForms.ExcelUploadForm;
import com.kt.dataForms.ReqCreateVendor;
import com.kt.dataForms.ReqSvcCodeForm;
import com.kt.dataManager.JSONParsingFrom;
import com.kt.dataManager.JSONSerializerTo;

public class InsertDataTo {

	ConnectToCanssandra connDB = new ConnectToCanssandra();
	CreateTableFor createTable = new CreateTableFor();

	Cluster cluster;
	Session session;

	public InsertDataTo() {

		cluster = connDB.getCluster();
		session = cluster.connect();
	}

	public TableMetadata checkExsitingTable(String tableName, String ksName)

	{
		String tn = tableName;

		KeyspaceMetadata ks = cluster.getMetadata().getKeyspace(ksName);
		TableMetadata table = ks.getTable(tn);

		return table;

	}

	public String insertTempToIndexList(ReqCreateVendor vendor) {

		SelectDataTo selectTo = new SelectDataTo();
		String resCode;

		TableMetadata res = this.checkExsitingTable(Constants.CASSANDRA_TABLE_TEMPINDEXLIST,
				Constants.CASSANDRA_KEYSPACE_COMMON);

		if (res == null) {

			createTable.createTableForTempList();
		}

		Boolean existed = selectTo.isExistedItem(Constants.CASSANDRA_KEYSPACE_COMMON,
				Constants.CASSANDRA_TABLE_TEMPINDEXLIST, "vendorname", vendor.getVendorName());

		Statement query = QueryBuilder
				.insertInto(Constants.CASSANDRA_KEYSPACE_COMMON, Constants.CASSANDRA_TABLE_TEMPINDEXLIST).ifNotExists()
				.value("vendorname", vendor.getVendorName()).value("domainname", vendor.getDomainName())
				.value("dirpath", vendor.getDirPath()).value("specname", vendor.getSpecName());

		session.execute(query);
		cluster.close();

		resCode = "201";

		return resCode;

		// }

	}

	/**
	 * @author : "Minwoo Ryu" [2019. 3. 19. 오후 5:21:03] desc : 엑셀에서 파싱한 값을 본 메소드를
	 *         통하여 테이블에 저장
	 * @version :
	 * @param :
	 * @return : void
	 * @throws :
	 * @see :
	 * 
	 */
	/*
	 * public void insetServiceDesc () {
	 * 
	 * SelectDataTo selectTo = new SelectDataTo();
	 * 
	 * String keySpace = "vendersvcks"; String table = null;
	 * 
	 * // List<Row> list = selectTo.selectGetSpecId(specName); //엑셀 객체에서 "specName"
	 * 객체 값을 arg로
	 * 
	 * // for (Row row : list) { // // table = row.getString("specid"); // }
	 * 
	 * 
	 * 
	 * TableMetadata res = this.checkExsitingTable(table, keySpace);
	 * 
	 * if (res == null) {
	 * 
	 * createTable.createTableForSpec(keySpace, table); }
	 * 
	 * //insert문 시작
	 * 
	 * Statement query = QueryBuilder.insertInto(keySpace, table).ifNotExists()
	 * .value("", ""); // 앞의 값은 table의 attribute 명, 뒤의 값은 입력 값
	 * 
	 * 
	 * session.execute(query);
	 * 
	 * 
	 * 
	 * 
	 * 
	 * }
	 */

	public void insertTemplateinfo(String template, String path, String domain) {

		TableMetadata res = this.checkExsitingTable(Constants.CASSANDRA_TABLE_TEMPLATEINDEXLIST,
				Constants.CASSANDRA_KEYSPACE_COMMON);

		if (res == null) {

			createTable.createTableForTemplateList();

		}

		Statement query = QueryBuilder
				.insertInto(Constants.CASSANDRA_KEYSPACE_COMMON, Constants.CASSANDRA_TABLE_TEMPLATEINDEXLIST)
				.ifNotExists().value("domainname", domain).value("templatename", template).value("dirpath", path);

		session.execute(query);
		cluster.close();
	}

	public Boolean insertDomainList(String domainName) {
		Boolean response = null;

		TableMetadata res = this.checkExsitingTable(Constants.CASSANDRA_TABLE_DOMAINLIST,
				Constants.CASSANDRA_KEYSPACE_COMMON);

		if (res == null) {
			createTable.createTableForDomain(Constants.CASSANDRA_KEYSPACE_COMMON, Constants.CASSANDRA_TABLE_DOMAINLIST);
		}

		Statement query = QueryBuilder
				.insertInto(Constants.CASSANDRA_KEYSPACE_COMMON, Constants.CASSANDRA_TABLE_DOMAINLIST)
				.value("domainname", domainName).ifNotExists();

		ResultSet resSet = session.execute(query);
		cluster.close();

		List<Row> resList = resSet.all();

		for (Row row : resList) {

			response = row.getBool(0);

		}

		return response;

	}

	/**
	 * @author : "Minwoo Ryu" [2019. 3. 16. 오 16:54:27] desc : 서비스 코드 요청을 위한 서비스 간단
	 *         명세를 수신 후 해당 값을 기반으로 commonks 내 domainservicelist 테이블에 명세를 저장하고 서비스
	 *         코드를 발급, 이때 발급되는 코드는 본 수신 명세와 함께 domainservicelist에 함께 저장된 후 코드 값을 리턴
	 *         수행절차: (1) commonks 내 domainservicelist 테이블 존재여부 확인 후 존재하지 않을 경우 생성
	 *         (참고: CreateTableTo.createDomainServiceList() (2-1) domainservicelist
	 *         테이블에 동일한 서비스명을 가지는 Row가 있는지 확인 후 해당 값이 존재할 경우 serviceCode를 409로 리턴
	 *         (2-2) domainservicelist 테이블에 동일한 서비스명을 가지는 Row가 없을 경우 수신 받은 명세 값을 등록,
	 *         이때 seqnum은 domainservicelist 테이블의 전체 row size의 + 1로 정의되며, 서비스 코드 발급
	 *         규칙은 "도메인 이름_서비스타입_일련번호"로 정의 일련번호의 경우 int 타입으로 정의되며 1부터 시작
	 * 
	 * @version : 0.1
	 * @return : String code (409 또는 발행된 service code 값)
	 * @throws :
	 * @see : SelectDataTo.isExistedItem(); SelectDataTo.selectNumberOfRows
	 * 
	 * @param
	 * @return
	 */
	public String createServiceCodeNinsertService(ReqSvcCodeForm form) {

		SelectDataTo selectTo = new SelectDataTo();

		String code;

		TableMetadata res = this.checkExsitingTable(Constants.CASSANDRA_TABLE_DOMAINSVCLIST,
				Constants.CASSANDRA_KEYSPACE_COMMON);

		if (res == null) {

			createTable.createDomainServiceList();

		}

		Boolean checkItem = selectTo.isExistedItem(Constants.CASSANDRA_KEYSPACE_COMMON,
				Constants.CASSANDRA_TABLE_DOMAINSVCLIST, "servicename", form.getServiceName());

		if (checkItem == true) {

			code = "409";

			return code;

		}

		int num = selectTo.selectNumberOfRows(Constants.CASSANDRA_KEYSPACE_COMMON,
				Constants.CASSANDRA_TABLE_DOMAINSVCLIST) + 1;

		Statement query = QueryBuilder
				.insertInto(Constants.CASSANDRA_KEYSPACE_COMMON, Constants.CASSANDRA_TABLE_DOMAINSVCLIST)
				.value("seqnum", num).value("domainname", form.getDomainName())
				.value("servicetype", form.getServiceType())
				.value("servicecode", form.getDomainName() + "_" + form.getInvokeType() + "_" + num)
				.value("servicename", form.getServiceName()).value("invoketype", form.getInvokeType())
				.value("servicedesc", form.getServiceDesc());

		session.execute(query);
		cluster.close();

		code = form.getDomainName() + "_" + form.getServiceType() + "_" + num;

		return code;

	}

	public JSONObject insertSpecIndexTo(String specName, String domainName) {

		SelectDataTo selectTo = new SelectDataTo();

		TableMetadata res = this.checkExsitingTable(Constants.CASSANDRA_TABLE_SPECINDEXLIST,
				Constants.CASSANDRA_KEYSPACE_COMMON);

		if (res == null) {

			createTable.createTableForSpecIndexList();
		}

		Boolean existed = selectTo.isExistedItem(Constants.CASSANDRA_KEYSPACE_COMMON,
				Constants.CASSANDRA_TABLE_SPECINDEXLIST, "specname", specName);

		if (existed) {

			JSONSerializerTo serializerTo = new JSONSerializerTo();

			JSONObject obj = serializerTo.resConflict("409", "요청하신 규격이 이미 등록되어 있습니다");

			return obj;

		} else {

			int num = selectTo.selectNumberOfRows(Constants.CASSANDRA_KEYSPACE_COMMON,
					Constants.CASSANDRA_TABLE_SPECINDEXLIST) + 1;

			Statement query = QueryBuilder
					.insertInto(Constants.CASSANDRA_KEYSPACE_COMMON, Constants.CASSANDRA_TABLE_SPECINDEXLIST)
					.ifNotExists().value("specid", domainName + "_" + Integer.toString(num)).value("specname", specName)
					.value("domainname", domainName);

			session.execute(query);

			JSONSerializerTo serializerTo = new JSONSerializerTo();

			JSONObject obj = serializerTo.resSuccess();

			cluster.close();

			return obj;
		}

	}

	public String insertVendorToIndexList(ReqCreateVendor vendor) {

		SelectDataTo selectTo = new SelectDataTo();
		String resCode;

		TableMetadata res = this.checkExsitingTable(Constants.CASSANDRA_TABLE_VENDORINDEXLIST,
				Constants.CASSANDRA_KEYSPACE_VENDOR);

		if (res == null) {

			createTable.createTableForVendorList();
		}

		Statement query = QueryBuilder
				.insertInto(Constants.CASSANDRA_KEYSPACE_VENDOR, Constants.CASSANDRA_TABLE_VENDORINDEXLIST)
				.ifNotExists().value("vendorname", vendor.getVendorName()).value("domainname", vendor.getDomainName())
				.value("dirpath", vendor.getDirPath()).value("specname", vendor.getSpecName());

		session.execute(query);
		cluster.close();

		resCode = "201";

		return resCode;

		// }

	}

	/**
	 * @author : "Minwoo Ryu" [2019. 2. 1. 오후 6:05:54] desc : 기존 프로토콜에서 usrAuth는 제외,
	 *         그리고 클라이언트에서 hardcording으로 domainname과 domainid를 추가하였음
	 * @version :
	 * @param :
	 * @return : void
	 * @throws :
	 * @see :
	 * 
	 * @param desc
	 */
	public void insertDomainSvcTo(BaseSvcForm desc) {

		JSONParsingFrom parseringFrom = new JSONParsingFrom();

		TableMetadata res = this.checkExsitingTable(desc.getDomainName(), Constants.CASSANDRA_KEYSPACE_DOMAIN);

		// if (res == null) {
		//
		// createTable.createTableFor(keySpace, desc.getDomainName());
		//
		// }

		JSONObject obj = parseringFrom.convertIntentInfo(desc.getIntentInfo());

		Statement query = QueryBuilder.insertInto(Constants.CASSANDRA_KEYSPACE_DOMAIN, desc.getDomainName())
				.value("domainname", desc.getDomainName()).value("intentname", obj.get("id").toString())
				.value("domainid", desc.getDomainId()).value("servicecode", desc.getServiceCode())
				.value("commURL", desc.getComURL()).value("testURL", desc.getTestURL())
				.value("method", desc.getMethod()).value("datatype", desc.getDataType())
				.value("requestformat", desc.getReqStructure().toString())
				.value("requestspec", desc.getReqSpec().toString())
				.value("responseFormat", desc.getResStructure().toString())
				.value("responsespec", desc.getResSpec().toString()).value("dicList", obj.get("dicList").toString())
				.ifNotExists();

		ResultSet resSet = session.execute(query);
		cluster.close();

	}

	public void insertVenderSvcTo(ArrayList<BaseVenderSvcForm> descList) {

		JSONParsingFrom parseringFrom = new JSONParsingFrom();

		for (BaseVenderSvcForm desc : descList) {

			TableMetadata res = this.checkExsitingTable(desc.getDomainId(), Constants.CASSANDRA_KEYSPACE_VENDOR);

			// if (res == null) {
			//
			// createTable.createTableFor(keySpace, desc.getDomainId());
			//
			// }

			JSONObject obj = parseringFrom.convertIntentInfo(desc.getIntentInfo());

			Statement query = QueryBuilder.insertInto(Constants.CASSANDRA_KEYSPACE_VENDOR, desc.getDomainId())
					.value("domainname", desc.getDomainName()).value("intentname", obj.get("id").toString())
					.value("domainid", desc.getDomainId())
					// added to argument for demo
					.value("tourl", desc.getToUrl()).value("servicetype", desc.getSvcType())
					.value("servicecode", desc.getServiceCode())
					//
					.value("commURL", desc.getComURL()).value("testURL", desc.getTestURL())
					.value("method", desc.getMethod()).value("datatype", desc.getDataType())
					.value("requestformat", desc.getReqStructure().toString())
					.value("requestspec", desc.getReqSpec().toString())
					.value("responseFormat", desc.getResStructure().toString())
					.value("responsespec", desc.getResSpec().toString()).value("dicList", obj.get("dicList").toString())
					.ifNotExists();

			ResultSet resSet = session.execute(query);
			cluster.close();
		}

	}

	/**
	 * @author : "Minwoo Ryu" [2019. 2. 7. 오후 6:17:39] desc : 엑셀 양식에 따른 Intent 정보 저장
	 *         해당 Intent는 별도 table 구분 없이 모두 저장됨 현재는 table 명이 hardcoding 되어 있으며, 향후
	 *         변수로 처리 해야함
	 * @version :
	 * @param :
	 * @return : void
	 * @throws :
	 * @see :
	 * 
	 * @param listData
	 */
	public void insertDomainIntent(ArrayList<BaseIntentInfoForm> listData) {

		SelectDataTo selectTo = new SelectDataTo();

		TableMetadata res = this.checkExsitingTable(Constants.CASSANDRA_TABLE_INTENTINFO,
				Constants.CASSANDRA_KEYSPACE_COMMON);

		int idx = 0;
		Statement query;

		if (res == null) {
			createTable.createTableForDictionary();
		}

		for (BaseIntentInfoForm data : listData) {
			query = QueryBuilder.insertInto(Constants.CASSANDRA_KEYSPACE_COMMON, Constants.CASSANDRA_TABLE_INTENTINFO)
					// .value("seqNum", idx)
					.value("intentname", data.getIntentName()).value("intentDesc", data.getDesc())
					.value("dicList", data.getArr().toString());
			session.execute(query);

		}
		cluster.close();
	}

	/**
	 * @author SERA
	 * @param key
	 * @param list
	 */
	public void insertExcelData(List<ExcelUploadForm> dataList, String specName) {

		SelectDataTo selectTo = new SelectDataTo();

		String table = null;

		List<Row> list = selectTo.selectGetSpecId(specName);

		for (Row row : list) {
			table = row.getString("specid");
		}

		TableMetadata res = this.checkExsitingTable(table, Constants.CASSANDRA_KEYSPACE_VENDOR);

		if (res == null) {
			createTable.createTableForSpec(Constants.CASSANDRA_KEYSPACE_VENDOR, table);
		}

		Statement query;

		for (ExcelUploadForm data : dataList) {
			query = QueryBuilder.insertInto(Constants.CASSANDRA_KEYSPACE_VENDOR, table)
					.value("intentname", data.getIntentInfo()).value("domainname", data.getDomainName())
					.value("servicedesc", data.getServiceDesc()).value("servicename", data.getServiceName())
					.value("domainid", data.getDomainId()).value("specname", data.getSpecName())
					.value("invoketype", data.getInvokeType()).value("servicelink", data.getServiceLink())
					.value("servicecode", data.getServiceCode()).value("headerinfo", data.getToJsonFormatHeader())
					.value("commURL", data.getCommonURL()).value("testURL", data.getTestURL())
					.value("method", data.getTransMethod()).value("datatype", data.getDataType())
					.value("servicetype", data.getServiceType()).value("requestformat", data.getReqEx())
					.value("requestspec", data.getToJsonFormatReqParam()).value("responseFormat", data.getResEx())
					.value("responsespec", data.getToJsonFormatResParam())
					.value("dicList", data.getToJsonFormatDicList());
			session.execute(query);
		}
		cluster.close();
	}

	public void insertSpecCopyData(String preSpecName, String newSpecName, String comUrl, String testUrl) {

		SelectDataTo selectToNewSpecId = new SelectDataTo();
		SelectDataTo selectToPreSpecId = new SelectDataTo();
		SelectDataTo selectToCopyTable = new SelectDataTo();
		String newTable = null;
		String preTable = null;

		List<Row> list = selectToNewSpecId.selectGetSpecId(newSpecName);
		for (Row row : list) {
			newTable = row.getString("specid");
		}

		List<Row> prelist = selectToPreSpecId.selectGetSpecId(preSpecName);
		for (Row row : prelist) {
			preTable = row.getString("specid");
		}

		TableMetadata res = this.checkExsitingTable(newTable, Constants.CASSANDRA_KEYSPACE_VENDOR);

		if (res == null) {
			createTable.createTableForSpec(Constants.CASSANDRA_KEYSPACE_VENDOR, newTable);
		}

		Statement query;

		List<Row> datalist = selectToCopyTable.selectVendorTable(preTable);

		for (Row row : datalist) {
			System.out.println(row.getString("intentname"));

			query = QueryBuilder.insertInto(Constants.CASSANDRA_KEYSPACE_VENDOR, newTable)
					.value("intentname", row.getString("intentname")).value("domainname", row.getString("domainname"))
					.value("servicedesc", row.getString("servicedesc"))
					.value("servicename", row.getString("servicename")).value("domainid", row.getString("domainid"))
					.value("specname", row.getString("specname")).value("invoketype", row.getString("invoketype"))
					.value("servicelink", row.getString("servicelink"))
					.value("servicecode", row.getString("servicecode")).value("headerinfo", row.getString("headerinfo"))
					.value("commURL", comUrl).value("testURL", testUrl).value("method", row.getString("method"))
					.value("datatype", row.getString("datatype")).value("servicetype", row.getString("servicetype"))
					.value("requestformat", row.getString("requestformat"))
					.value("requestspec", row.getString("requestspec"))
					.value("responseFormat", row.getString("responseFormat"))
					.value("responsespec", row.getString("responsespec")).value("dicList", row.getString("dicList"));
			session.execute(query);

		}
		cluster.close();
	}

}
