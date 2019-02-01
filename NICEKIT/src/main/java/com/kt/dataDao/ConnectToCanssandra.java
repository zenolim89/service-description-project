package com.kt.dataDao;

import org.apache.cassandra.cql3.statements.CreateTableStatement;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.TableMetadata;
import com.datastax.driver.core.schemabuilder.*;
import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.type.DataType;
import com.datastax.oss.driver.api.querybuilder.*;

public class ConnectToCanssandra {

	Cluster clu;
	//	Session session;
	//	
	//	ResultSet resSet;
	//	Row row;


	public ConnectToCanssandra() {

		clu = Cluster.builder()
				.addContactPoint("218.145.218.216")
				.build();

	}

	public Cluster getCluster () {

		return this.clu;

	}

}





