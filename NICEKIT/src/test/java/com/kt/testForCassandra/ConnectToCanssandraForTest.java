package com.kt.testForCassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.TableMetadata;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.kt.dataForms.OwnServiceForm;

public class ConnectToCanssandraForTest {
	
	public static void main (String arg[]) {
		
		Cluster cluster;
		Session sessionForComm;
		Session sessionForOwn;
		
		ResultSet results;
		Row rows;
		
		cluster = Cluster.builder()
				.addContactPoint("218.145.218.216")
				.build();
		
		KeyspaceMetadata ks = cluster.getMetadata().getKeyspace("commonks");
		TableMetadata table = ks.getTable("common");
		
		String talNm = table.getName();
		
		System.out.println("aAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		System.out.println(talNm);
		
		
		
		
	}
	
	
	
//	public ConnectToCanssandra() {
//		
//		cluster = Cluster.builder()
//				.addContactPoint("192.168.0.22")
//				.build();
//	}
	
	
	// insert desc of svc
	// we have to consider that type of desc is array or not
	public void insertDescToComm (OwnServiceForm desc)
	{
//		sessionForComm = cluster.connect("commonks");
//		
//		Statement query = QueryBuilder.insertInto("common").value("name", "value")
//				.value("name", "value");
//		
//		
//		sessionForComm.execute(query);
//		
//		cluster.close();
		
//		KeyspaceMetadata ks = cluster.getMetadata().getKeyspace("commonks");
//		TableMetadata table = ks.getTable("common");
//		
//		System.out.println(table);
		
	}

}
