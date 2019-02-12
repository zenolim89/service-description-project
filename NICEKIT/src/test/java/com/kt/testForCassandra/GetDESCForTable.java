package com.kt.testForCassandra;

import java.util.Collection;
import java.util.List;

import org.apache.cassandra.thrift.Column;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ColumnMetadata;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.TableMetadata;
import com.kt.dataDao.ConnectToCanssandra;

public class GetDESCForTable {
	
	
	public static void main (String arg[]) {
	
		ConnectToCanssandra connDB = new ConnectToCanssandra();
		
		Cluster cluster = connDB.getCluster();
		Session session = cluster.connect();
		
		ResultSet res = null;
		
		TableMetadata tb = cluster.getMetadata()
				.getKeyspace("commonks")
				.getTable("intentinfo");
		
		
			
		List<ColumnMetadata> dataList = tb.getColumns();
		List<ColumnMetadata> keyList = tb.getPartitionKey();
		
		
		for (ColumnMetadata key : keyList) {
			
			System.out.println("KEYS: " + key.getName());
		}
				
		
		for (ColumnMetadata data : dataList) {
			
			System.out.println("TYPE: " + data.getType());
			System.out.println("NAME: " + data.getName());
				
			
		}
		
		
		// selec로 전체 검색 후 set을 이용해서 key 값 확인 후 각 키의 타입 확인 시도 
		
		
		
	}
	
	
	
	
	
			
	
	

}
