package com.kt.testForCassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.SimpleStatement;
import com.datastax.driver.core.Statement;
import com.datastax.oss.driver.api.core.type.DataType;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilderDsl;
import com.datastax.oss.driver.api.querybuilder.schema.CreateTable;
import com.datastax.oss.driver.api.querybuilder.schema.CreateTableStart;

public class testForCreate {

	public static void main(String arg[]) {

		SchemaBuilderDsl builder = new SchemaBuilderDsl();

		Cluster cluster;
		Session sessionForComm;
		Session sessionForOwn;

		ResultSet results;
		Row rows;

		cluster = Cluster.builder().addContactPoint("218.145.218.216").build();

		CreateTable query = ((CreateTable) builder.createTable("commonks", "testDomain").ifNotExists())
				.withColumn("dominname", DataTypes.TEXT).withColumn("intentname", DataTypes.TEXT)
				.withPartitionKey("serviceid", DataTypes.TEXT);

		sessionForComm = cluster.connect();

		SimpleStatement stmt = new SimpleStatement(query.toString());
		sessionForComm.execute(stmt);

	}

}
