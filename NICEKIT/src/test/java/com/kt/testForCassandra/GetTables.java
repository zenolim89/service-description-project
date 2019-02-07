package com.kt.testForCassandra;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.TableMetadata;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.kt.dataDao.ConnectToCanssandra;

public class GetTables {
	
	public static void main (String arg[]) {
		
		ConnectToCanssandra connDB = new ConnectToCanssandra();
		
		Cluster cluster = connDB.getCluster();
		Session session = cluster.connect();
		
		ResultSet res = null;
		
		
		Collection<TableMetadata> tables = cluster.getMetadata()
				.getKeyspace("ownserviceks")
				.getTables();
		
		List<String> tableName = tables.stream()
				.map(tm -> tm.getName())
				.collect(Collectors.toList());
		
		for (String t : tableName) {
			
			Statement query = QueryBuilder.select("appid").from("ownserviceks", t);
			
			res = session.execute(query);
						
			System.out.println(res.one());
			
			
		}

				
	}
	
	
	
	public void getDomainNameinTables (String tableName, Session session) {
		
		ArrayList<String> tableList = new ArrayList<String>();
		
		Statement query = QueryBuilder.select("domainname").from("domainks", tableName);
		
		ResultSet res = session.execute(query);
		
	}

	
	
	
}
