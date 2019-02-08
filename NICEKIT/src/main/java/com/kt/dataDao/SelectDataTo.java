package com.kt.dataDao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
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
import com.kt.dataManager.JSONSerializerTo;

public class SelectDataTo {

	ConnectToCanssandra connDB = new ConnectToCanssandra();
	JSONSerializerTo serializerTo = new JSONSerializerTo();

	Cluster cluster = connDB.getCluster();
	Session session = cluster.connect();


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

	public int getLastRowForDicList (String ksName, String tbName) {
		
		Statement query = QueryBuilder.select("seqNum").from(ksName, tbName).orderBy(QueryBuilder.desc("seqNum"));
		ResultSet res = session.execute(query);
		
		int lastNum = Integer.parseInt(res.one().toString());
		
		return lastNum;
		
	}
	
	public ArrayList<String> selectIntentNameList (String ksName, String tableName) throws ParseException {
		
		ArrayList<String> intentList = new ArrayList<String>();
		
		Statement query = QueryBuilder.select().from(ksName, tableName).orderBy(QueryBuilder.asc("seqNum"));
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
			
			intentForm.setSeqNum(row.getInt("seqNum"));
			intentForm.setIntentName(row.getString("intentname"));
			intentForm.setDesc(row.getString("intentDesc"));
			intentForm.setArr((JSONArray) parser.parse(row.getString("dicList")));
			
			
			intentInfoList.add(intentForm);
						
		}
		
		return intentInfoList;
		
	}


}
