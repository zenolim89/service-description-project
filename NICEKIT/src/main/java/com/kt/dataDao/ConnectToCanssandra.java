package com.kt.dataDao;

import com.datastax.driver.core.Cluster;
import com.kt.commonUtils.Constants;

public class ConnectToCanssandra {

	Cluster clu;
	// Session session;
	//
	// ResultSet resSet;
	// Row row;

	public ConnectToCanssandra() {

		clu = Cluster.builder().addContactPoint(Constants.CASSANDRA_PROPERTY_HOST).build();

	}

	public Cluster getCluster() {

		return this.clu;

	}

}
