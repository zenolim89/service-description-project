package com.kt.dataDao;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.TableMetadata;
import com.datastax.driver.core.querybuilder.QueryBuilder;
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

	public int getLastRowForDicList () {
		
		Statement query = QueryBuilder.select("seqNum").from("commonks", "intetntInfo").orderBy(QueryBuilder.desc("seqNum"));
		ResultSet res = session.execute(query);
		
		int lastNum = Integer.parseInt(res.one().toString());
		
		return lastNum;
		
	}


}
