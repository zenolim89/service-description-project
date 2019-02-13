package com.kt.dataDao;

import org.json.simple.JSONObject;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.SimpleStatement;
import com.datastax.driver.core.TableMetadata;
import com.datastax.oss.driver.api.core.metadata.schema.ClusteringOrder;
import com.datastax.oss.driver.api.core.type.DataType;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilderDsl;
import com.datastax.oss.driver.api.querybuilder.schema.CreateTable;
import com.typesafe.config.ConfigException.Parse;

import ch.qos.logback.core.pattern.parser.Parser;

public class CreateTableFor {

	ConnectToCanssandra connDB = new ConnectToCanssandra();
	SchemaBuilderDsl builder = new SchemaBuilderDsl();

	Cluster cluster;
	Session session;

	public CreateTableFor() {

		cluster = connDB.getCluster();
		session = cluster.connect();
	}


	/**
	 * @author	: "Minwoo Ryu" [2019. 2. 1. 오후 3:42:10]
	 * desc	: 테이블 생성 메소드
	 * 현재는 정의된 컬럼명으로 테이블을 생성하지만, 이후 동적으로 테이블 생성이 필요함
	 * 예를 들면 기존 사업장을 기준으로 확장 하는 것등
	 * @version	:0.1
	 * @param	: 
	 * @return 	: void 
	 * @throws 	: 
	 * @see		: 

	 * @param ks(keySpace 이름)
	 * @param id(도메인 아이디)
	 */
	public void createTableFor (String ksName, String tableName) {

		CreateTable create = ((CreateTable) builder.createTable(ksName, tableName).ifNotExists())
				.withClusteringColumn("intentname", DataTypes.TEXT)
				.withColumn("domainname", DataTypes.TEXT)
				.withColumn("domainid", DataTypes.TEXT)
				.withPartitionKey("servicecode", DataTypes.TEXT)
				.withColumn("commURL", DataTypes.TEXT)
				.withColumn("testURL", DataTypes.TEXT)
				.withColumn("method", DataTypes.TEXT)
				.withColumn("datatype", DataTypes.TEXT)
				.withColumn("requestformat", DataTypes.TEXT)
				.withColumn("requestspec", DataTypes.TEXT)
				.withColumn("responseFormat", DataTypes.TEXT)
				.withColumn("responsespec", DataTypes.TEXT)
				.withColumn("dicList", DataTypes.TEXT);

		SimpleStatement query = new SimpleStatement(create.toString());
		session.execute(query);

		cluster.close();

	}

//	public void createTableForVender (String id) {
//
//		CreateTable create = ((CreateTable) builder.createTable("vendersvcks", id).ifNotExists())
//				.withClusteringColumn("intentname", DataTypes.TEXT)
//				.withColumn("domainid", DataTypes.TEXT)
//				.withPartitionKey("servicecode", DataTypes.TEXT)
//				.withColumn("commURL", DataTypes.TEXT)
//				.withColumn("testURL", DataTypes.TEXT)
//				.withColumn("method", DataTypes.TEXT)
//				.withColumn("datatype", DataTypes.TEXT)
//				.withColumn("requestformat", DataTypes.TEXT)
//				.withColumn("requestspec", DataTypes.TEXT)
//				.withColumn("responseFormat", DataTypes.TEXT)
//				.withColumn("responsespec", DataTypes.TEXT)
//				.withColumn("dicList", DataTypes.TEXT);
//
//		SimpleStatement query = new SimpleStatement(create.toString());
//		session.execute(query);
//
//		cluster.close();
//
//	}



	/**
	 * @author	: "Minwoo Ryu" [2019. 2. 1. 오후 3:54:00]
	 * desc	: 사업장 서비스 테이블 생성
	 * 위와 동일하게 프로파일 등 (공통에서 사용하는것) 불러오는 형태로 개발 변경되어야 할 것 같음
	 * common table은 venertable의 intent 저장 방식 참고하여 수정 필요
	 * 향후 NBware와 병합시 사용, 현재는 등록기로만 사용하는 걸로 연동
	 * @version	:
	 * @param	: 
	 * @return 	: void 
	 * @throws 	: 
	 * @see		: 

	 * @param id
	 */
	public void createTableForVenderService (String venderId) {

		CreateTable create = ((CreateTable) builder.createTable("ownserviceks", venderId).ifNotExists())

				.withColumn("vendername", DataTypes.TEXT)
				.withColumn("intentname", DataTypes.TEXT)
				.withPartitionKey("servicecode", DataTypes.TEXT)
				.withColumn("commURL", DataTypes.TEXT)
				.withColumn("testURL", DataTypes.TEXT)
				.withColumn("method", DataTypes.TEXT)
				.withColumn("datatype", DataTypes.TEXT)
				.withColumn("requestformat", DataTypes.TEXT)
				.withColumn("requestspec", DataTypes.TEXT)
				.withColumn("responseFormat", DataTypes.TEXT)
				.withColumn("responsespec", DataTypes.TEXT)
				.withColumn("dicList", DataTypes.TEXT);


		SimpleStatement query = new SimpleStatement(create.toString());
		session.execute(query);

		cluster.close();



	}

	/**
	 * @author	: "Minwoo Ryu" [2019. 2. 7. 오전 11:10:53]
	 * desc	: 도메인별 어휘 사전 테이블 생성
	 * @version	: 0.1
	 * @param	: diclist
	 * @return 	: void 
	 * @throws 	: 
	 * @see		: 

	 * @param dicList
	 */
	public void createTableForDictionary () {


		CreateTable create = (CreateTable) builder.createTable("commonks","intentInfo")
				.ifNotExists()
				.withPartitionKey("intentname", DataTypes.TEXT)
				//				.withClusteringColumn("seqnum", DataTypes.INT)
				.withColumn("intentDesc", DataTypes.TEXT)
				.withColumn("dicList", DataTypes.TEXT);

		//				.withClusteringOrder("seqnum", ClusteringOrder.DESC);


		SimpleStatement query = new SimpleStatement(create.toString());
		session.execute(query);

		cluster.close();

	}








}
