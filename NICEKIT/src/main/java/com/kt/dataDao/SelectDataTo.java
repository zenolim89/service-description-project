package com.kt.dataDao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.TableMetadata;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.oss.driver.shaded.guava.common.math.Quantiles;
import com.kt.dataForms.BaseIntentInfoForm;
import com.kt.dataForms.DiscoveredServiceDESC;
import com.kt.dataManager.JSONSerializerTo;
import com.kt.serviceManager.ServiceEnabler;

public class SelectDataTo {

	ConnectToCanssandra connDB = new ConnectToCanssandra();
	JSONSerializerTo serializerTo = new JSONSerializerTo();

	Cluster cluster = connDB.getCluster();
	Session session = cluster.connect();


	public JSONObject selectMatchingService (String intentName, String word, String name, String keySpace) {

		JSONParser parser = new JSONParser();
		JSONObject resObj = new JSONObject();
		ServiceEnabler enabler = new ServiceEnabler();

		Statement query = QueryBuilder.select().from(keySpace, name)
				.where(QueryBuilder.eq("intentname", intentName))
				.allowFiltering();
		ResultSet set = session.execute(query);

		List<Row> rowList = set.all();
		
		if( rowList.size() == 0) {
			resObj.put("resCode", "404");
			resObj.put("resMsg", "요청하신 " + word + "관련 서비스를 찾을 수 없습니다 확인 후 다시 말씀해주세요");
			
			System.out.println("[DEBUG] : 어휘 검색결과가 없음");
		}

		ArrayList<DiscoveredServiceDESC> resList = new ArrayList<DiscoveredServiceDESC>();

		try {

			for (Row r : rowList) {

				DiscoveredServiceDESC desc = new DiscoveredServiceDESC();


				desc.setComURL(r.getString("commurl"));
				desc.setDomainId(r.getString("domainid"));
				desc.setTestURL(r.getString("testurl"));
				desc.setMethod(r.getString("method"));
				desc.setDataType(r.getString("datatype"));
				
				
				desc.setToUrl(r.getString("tourl"));
				desc.setServiceType(r.getString("servicetype"));
				
				
				desc.setReqStructure( (JSONArray) parser.parse(r.getString("requestformat")));
				desc.setReqSpec( (JSONArray) parser.parse(r.getString("requestspec")));
				desc.setResStructure( (JSONArray) parser.parse(r.getString("responseformat")));
				desc.setResSpec( (JSONArray) parser.parse(r.getString("responsespec")));
				desc.setDicList( (JSONArray) parser.parse(r.getString("diclist")));

				resObj = enabler.discoverMatchingWord(desc, word);
				resObj.put("toUrl", r.getString("tourl"));
				resObj.put("serviceType", r.getString("servicetype"));

			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();


		};


		return resObj;
	}



	public JSONArray selectDomainListToCommon () {

		Collection<TableMetadata> tables = cluster.getMetadata()
				.getKeyspace("domainks")
				.getTables();

		List<String> tableList = tables.stream()
				.map(tm -> tm.getName())
				.collect(Collectors.toList());

		JSONArray arr = serializerTo.resDomainList(tableList);

		cluster.close();

		return arr;	
	}

	public ResultSet getLastRowForDicList (String ksName, String tbName) {

		Statement query = QueryBuilder.select().from(ksName, tbName);
		ResultSet res = session.execute(query);

		return res;


	}

	public ArrayList<String> selectIntentNameList (String ksName, String tableName) throws ParseException {

		ArrayList<String> intentList = new ArrayList<String>();

		Statement query = QueryBuilder.select().from(ksName, tableName);
		ResultSet res = session.execute(query);

		List<Row> resList = res.all();

		for(Row row : resList) {

			String name = row.getString("intentname");
			String desc = row.getString("intentDesc");

			intentList.add(desc + " (" + name +")");

		}

		return intentList;

	}

	public ArrayList<BaseIntentInfoForm> selectIntentInfo (String ksName, String tableName, String intentName) throws ParseException {

		JSONParser parser = new JSONParser();
		ArrayList<BaseIntentInfoForm> intentInfoList = new ArrayList<BaseIntentInfoForm>();

		Statement query = QueryBuilder.select().from(ksName, tableName).where(QueryBuilder.eq("intentname", intentName));

		ResultSet res = session.execute(query);

		List<Row> resList = res.all();

		for (Row row : resList) {

			BaseIntentInfoForm intentForm = new BaseIntentInfoForm();

			//			intentForm.setSeqNum(row.getInt("seqNum"));
			intentForm.setIntentName(row.getString("intentname"));
			intentForm.setDesc(row.getString("intentDesc"));
			intentForm.setArr((JSONArray) parser.parse(row.getString("dicList")));


			intentInfoList.add(intentForm);

		}

		return intentInfoList;

	}


}
