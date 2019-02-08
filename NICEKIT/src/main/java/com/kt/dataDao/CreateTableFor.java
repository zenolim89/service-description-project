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
	 * desc	: 도메인 정보에 따른 신규 테이블 생성 명렁
	 * 또한, 해당 명렁을 도메인 공통 테이블 생성으로할지, 범용으로 할지 고려해야함 (To do)
	 * 현재는 지정하여 각 컬럼을 정의하였으나, 이후에는 범용적 생성 방법을 고려해야함
	 * 예를 들면 프로파일을 불러와서 각각의 컬럼을 정의
	 * 이유는 도메인마다 가지는 특이한 컬럼이 있을 것이기 때문 (예: 호텔-방번호, 타입 등)
	 * @version	:0.1
	 * @param	: 
	 * @return 	: void 
	 * @throws 	: 
	 * @see		: 
	
	 * @param ks(keySpace 이름)
	 * @param id(도메인 아이디)
	 */
	public void createTableForCommonDomain (String domainname) {
		
		CreateTable create = ((CreateTable) builder.createTable("domainks", domainname).ifNotExists())
				.withColumn("intentname", DataTypes.TEXT)
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
