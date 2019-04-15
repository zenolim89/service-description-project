package com.kt.dataDao;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.Clause;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.kt.commonUtils.Constants;
import com.kt.dataManager.JSONSerializerTo;

public class DeleteDataTo {

	ConnectToCanssandra connDB = new ConnectToCanssandra();
	JSONSerializerTo serializerTo = new JSONSerializerTo();

	Cluster cluster = connDB.getCluster();
	Session session = cluster.connect();

	public void deleteRowForTempIndexList(String vendorName, String domainName) {

		Clause vendorClause = QueryBuilder.eq("vendorname", vendorName);
		Clause domainClause = QueryBuilder.eq("domainname", domainName);
		Statement delete = QueryBuilder.delete()
				.from(Constants.CASSANDRA_KEYSPACE_COMMON, Constants.CASSANDRA_TABLE_TEMPINDEXLIST).where(vendorClause)
				.and(domainClause).ifExists();

		session.execute(delete);
		cluster.close();

	}

}
