package com.kt.dataDao;

import org.dom4j.datatype.DatatypeElement;
import org.json.simple.JSONObject;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.ResultSet;
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

/**
 * @author	: "Minwoo Ryu" [2019. 3. 15. 오후 8:11:38]
 * @version : 
 * desc	:
 */
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
	 * @author	: "Minwoo Ryu" [2019. 3. 15. 오후 9:32:43]
	 * desc	: 카산드라에서 한글로 테이블 이름을 생성할 수 없어 Index 테이블과 동일한 개념의 테이블을 commonks에 생성
	 * 		   이후, 등록기로부터 엑셀로 입력 받은 서비스 명세의 전체 내용이 입력 받았을 때, 해당 포멧 중 "specName"의 값을 기반으로
	 *        현재 테이블에서 검색한 후 수신 받은 값의 protocolname을 추가함
	 *        최종적으로 protocolname을 기반으로 domainks에 실제 spec table을 만듦
	 * @version	:
	 * @param	: 
	 * @return 	: ResultSet 
	 * @throws 	: 
	 * @see		: 
	
	 * @param ksName
	 * @param tableName
	 * @return
	 */
	public void createTableForSpecIndexList () {
		
		CreateTable create = ((CreateTable) builder.createTable("commonks", "specindexlist").ifNotExists())
				.withPartitionKey("sepcid", DataTypes.TEXT)
				.withColumn("specname", DataTypes.TEXT)
				.withColumn("domainname", DataTypes.TEXT);
		
		SimpleStatement query = new SimpleStatement(create.toString());
		session.execute(query);
		
		cluster.close();
		
	}
	
	public void createTableForTemplateList () {
		
		CreateTable create = ((CreateTable) builder.createTable("commonks", "templateList").ifNotExists())
				.withPartitionKey("templatename", DataTypes.TEXT)
				.withColumn("dirpath", DataTypes.TEXT);
		
		SimpleStatement query = new SimpleStatement(create.toString());
		session.execute(query);
		
		cluster.close();
		
		
		
	}
	
	public void createTableForVenderList () {
		
		CreateTable create = ((CreateTable) builder.createTable("vendersvcks", "venderindexlist").ifNotExists())
				.withPartitionKey("vendername", DataTypes.TEXT)
				.withClusteringColumn("domainname", DataTypes.TEXT)
				.withColumn("templateuipath", DataTypes.TEXT)
				.withColumn("commercialuipath", DataTypes.TEXT);
		
		SimpleStatement query = new SimpleStatement(create.toString());
		session.execute(query);
		
		cluster.close();
		
	}
	
	public void createDomainServiceList () {
		
		CreateTable create = ((CreateTable) builder.createTable("commonks", "domainservicelist").ifNotExists())
				.withPartitionKey("seqnum", DataTypes.INT)
				.withClusteringColumn("domainname", DataTypes.TEXT)
				.withColumn("servicetype", DataTypes.TEXT)
				.withColumn("serviceCode", DataTypes.TEXT)
				.withColumn("servicename", DataTypes.TEXT)
				.withColumn("invoketype", DataTypes.TEXT)
				.withColumn("servicedesc", DataTypes.TEXT);
		
		SimpleStatement query = new SimpleStatement(create.toString());
		session.execute(query);
		
		cluster.close();
		
	}


	
	/**
	 * @author	: "Minwoo Ryu" [2019. 3. 15. 오후 8:11:42]
	 * desc	: 등록기로부터 신규 규격 생성 시 신규 규격 테이블 생성; 실제 데이터는 입력되지 않음
	 * @version	: 0.1
	 * @param	: 
	 * @return 	: ResultSet 
	 * @throws 	: 
	 * @see		: InBoundInterFace.setSpec()
	
	 * @param ksName
	 * @param tableName
	 * @return
	 */
	public ResultSet createTableForSpec (String ksName, String tableName) {

		CreateTable create = ((CreateTable) builder.createTable(ksName, tableName).ifNotExists())
				.withClusteringColumn("intentname", DataTypes.TEXT)
				.withColumn("domainname", DataTypes.TEXT)
				.withColumn("domainid", DataTypes.TEXT)
				.withColumn("invoketype", DataTypes.TEXT)
				.withColumn("servicelink", DataTypes.TEXT)
				.withPartitionKey("servicecode", DataTypes.TEXT)
				.withColumn("commURL", DataTypes.TEXT)
				.withColumn("testURL", DataTypes.TEXT)
				.withColumn("method", DataTypes.TEXT)
				.withColumn("datatype", DataTypes.TEXT)
				.withColumn("servicetype", DataTypes.TEXT)
				.withColumn("tourl", DataTypes.TEXT)
				.withColumn("requestformat", DataTypes.TEXT)
				.withColumn("requestspec", DataTypes.TEXT)
				.withColumn("responseFormat", DataTypes.TEXT)
				.withColumn("responsespec", DataTypes.TEXT)
				.withColumn("dicList", DataTypes.TEXT);

		SimpleStatement query = new SimpleStatement(create.toString());
		ResultSet resSet = session.execute(query);
		
		return resSet;

	}
	
	public void createTableForDomain (String ksName, String tableName) {
		
		CreateTable create = ((CreateTable) builder.createTable(ksName, tableName ))
				.withPartitionKey("domainname", DataTypes.TEXT);
		
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
				//added to argument for demo
				.withColumn("tourl", DataTypes.TEXT)
				.withColumn("servicetype", DataTypes.TEXT)
				//
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
