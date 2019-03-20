package com.kt.dataDao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.ws.ResponseWrapper;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.TableMetadata;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.oss.driver.internal.core.metadata.MetadataRefresh.Result;
import com.datastax.oss.driver.shaded.guava.common.math.Quantiles;
import com.datastax.oss.protocol.internal.request.Query;
import com.kt.dataForms.BaseIntentInfoForm;
import com.kt.dataForms.DiscoveredServiceDESC;
import com.kt.dataManager.JSONSerializerTo;
import com.kt.serviceManager.ServiceEnabler;

public class SelectDataTo {

	ConnectToCanssandra connDB = new ConnectToCanssandra();
	JSONSerializerTo serializerTo = new JSONSerializerTo();

	Cluster cluster = connDB.getCluster();
	Session session = cluster.connect();

	public JSONObject selectMatchingService(String intentName, String word, String name, String keySpace) {

		JSONParser parser = new JSONParser();
		JSONObject resObj = new JSONObject();
		ServiceEnabler enabler = new ServiceEnabler();

		Statement query = QueryBuilder.select().from(keySpace, name).where(QueryBuilder.eq("intentname", intentName))
				.allowFiltering();
		ResultSet set = session.execute(query);
		

		List<Row> rowList = set.all();

		if (rowList.size() == 0) {
			resObj.put("resCode", "404");
			resObj.put("resMsg", "요청하신 " + word + " 관련 서비스를 찾을 수 없습니다. ");

			System.out.println("[DEBUG] : 어휘 검색결과가 없음");
		}

		ArrayList<DiscoveredServiceDESC> resList = new ArrayList<DiscoveredServiceDESC>();

		try {

			for (Row r : rowList) {

				DiscoveredServiceDESC desc = new DiscoveredServiceDESC();

				desc.setComURL(r.getString("commurl"));
				desc.setDomainId(r.getString("domainid"));
				desc.setTestURL(r.getString("testurl"));
				desc.setMethod(r.getString("method"));
				desc.setDataType(r.getString("datatype"));

				desc.setToUrl(r.getString("tourl"));
				desc.setServiceType(r.getString("servicetype"));

				desc.setReqStructure((JSONArray) parser.parse(r.getString("requestformat")));
				desc.setReqSpec((JSONArray) parser.parse(r.getString("requestspec")));
				desc.setResStructure((JSONArray) parser.parse(r.getString("responseformat")));
				desc.setResSpec((JSONArray) parser.parse(r.getString("responsespec")));
				desc.setDicList((JSONArray) parser.parse(r.getString("diclist")));

				resObj = enabler.discoverMatchingWord(desc, word);
				resObj.put("toUrl", r.getString("tourl"));
				resObj.put("serviceType", r.getString("servicetype"));

			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		
		cluster.close();

		return resObj;
	}
	
	public List<Row> selectGetSpecId (String specName) {
		
		String keySpace = "commonks";
		String table ="specindexlist";
		
		Statement query = QueryBuilder.select().from(keySpace, table)
				.where(QueryBuilder.eq("specname", specName))
				.allowFiltering();
		
		ResultSet set = session.execute(query);
		cluster.close();
		
		List<Row> reslist = set.all();
		
		return reslist;
		
	}
	
	public List<Row> selectTemplate (String urlPath) {
		
		String keySpace = "commonks";
		String table = "templateList";
		
		Statement query = QueryBuilder.select().from(keySpace, table)
				.where(QueryBuilder.eq("dirpath", urlPath))
				.allowFiltering();
		
		ResultSet set = session.execute(query);
		cluster.close();
		
		List<Row> resList = set.all();
		
		return resList;
	}
	
	/**
	 * @author	: "Minwoo Ryu" [2019. 3. 20. 오전 11:47:47]
	 * desc	: TemplateList와 venderindexlist에 하나의 select로 구현하기 위하여
	 * keySpace와 table 이름을 inbound에서 하드코딩으로 처리하였음
	 * 데모가 끝나고 변경해야함
	 * @version	:
	 * @param	: 
	 * @return 	: List<Row> 
	 * @throws 	: 
	 * @see		: 
	
	 * @param vendorName
	 * @param tableName
	 * @param keySpace
	 * @return
	 */
	public List<Row> selectUseTemplateofVendor (String vendorName) {
		
		String keySpace = "vendersvcks";
		String table = "venderindexlist";
		
		Statement query = QueryBuilder.select().from(keySpace, table)
				.where(QueryBuilder.eq("vendername", vendorName))
				.allowFiltering();

		
		ResultSet set = session.execute(query);
		cluster.close();
		
		List<Row> resList = set.all();
		
		
		return resList;
				
	}
	
public List<Row> selectTemplatePreView (String templateName) {
		
		String keySpace = "commonks";
		String table = "templatelist";
		
		Statement query = QueryBuilder.select().from(keySpace, table)
				.where(QueryBuilder.eq("templatename", templateName))
				.allowFiltering();

		
		ResultSet set = session.execute(query);
		cluster.close();
		
		List<Row> resList = set.all();
		
		
		return resList;
				
	}
	
	
	
	public List<Row> selectVenderlistInDomain (String domainName) {
		
		String keySpace = "vendersvcks";
		String table = "venderindexlist";
		
		InsertDataTo insertTo = new InsertDataTo();
		
		TableMetadata res = insertTo.checkExsitingTable(table, keySpace);
		
		if (res == null)
		{
			List<Row> reslist = null;
			
			return reslist;
		}
		
		
		Statement query = QueryBuilder.select().from(keySpace, table)
				.where(QueryBuilder.eq("domainname", domainName))
				.allowFiltering();
		
		ResultSet set = session.execute(query);
		cluster.close();
		
		List<Row> reslist = set.all();
		
		return reslist;
		
	}
	
	/**
	 * @author	: "Minwoo Ryu" [2019. 3. 18. 오후 5:23:05]
	 * desc	: 생성기의 요청에 따라 commonks에 저장된 speclist를 조회하는 메소스
	 *        테이블 생성 유무에 따른 에러 메시지를 정의해야하는지 여부 추후 고려 필요
	 * @version	:
	 * @param	: 
	 * @return 	: List<Row> 
	 * @throws 	: 
	 * @see		: 
	
	 * @param domainName
	 * @return
	 */
	public List<Row> selectSpecList (String domainName) {
		
				
		String keySpace = "commonks";
		String table = "specindexlist";
		
		Statement query = QueryBuilder.select().from(keySpace, table)
				.where(QueryBuilder.eq("domainname", domainName))
				.allowFiltering();
		
		ResultSet set = session.execute(query);
		cluster.close();
		
		List<Row> resList = set.all();
						
		
		return resList;
	}
	
	public List<Row> selectTemplateList (String domainName) {
		
		String keySpace = "commonks";
		String table = "templatelist";
		
		Statement query = QueryBuilder.select().from(keySpace, table)
				.where(QueryBuilder.eq("domainname", domainName))
				.allowFiltering();
		
		ResultSet set = session.execute(query);
		cluster.close();
		
		List<Row> resList = set.all();
		
		return resList;
		
	}
	

	

//	public JSONArray selectDomainListToCommon() {
//		
//		Collection<TableMetadata> tables = cluster.getMetadata().getKeyspace("domainks").getTables();
//
//		List<String> tableList = tables.stream().map(tm -> tm.getName()).collect(Collectors.toList());
//
//		JSONArray arr = serializerTo.resDomainList(tableList);
//
//		cluster.close();
//
//		return arr;
//	}
	

	public JSONObject selectDomainList() {

		Statement query = QueryBuilder.select().from("commonks", "domainlist");
		ResultSet res = session.execute(query);
		cluster.close();
		
		JSONObject resObj = serializerTo.resDomainList(res);

		return resObj;

	}
	
	/**
	 * @author	: "Minwoo Ryu" [2019. 3. 16. 오 16:54:27]
	 * desc	: 요청하는 Item (i.e., 컬럼의 값)의 존재 유무를 확인하는 메소드  
	 *        요청하는 Item 값이 존재할 경우 true를 반환하고, 존재하지 않을 경우 false를 반환 
	 *                
	 * @version	: 0.1
	 * @return 	: Boolean
	 * @throws 	: 
	 * @see		: 

	 * @param 
	 * @return
	 */
	public Boolean isExistedItem (String keySpace, String table, String targetCn, String item) {
		
		Boolean res = null;
		
		Statement query = QueryBuilder.select().from(keySpace, table);
		
		ResultSet resSet = session.execute(query);
		
		List<Row> rowList = resSet.all();
		
		for(Row row : rowList) {
			
			System.out.println(row.getString(targetCn).toString());
			
			if ((row.getString(targetCn).toString()).equals(item)) {
				
				res = true;
				
				return res;
				
			} 
		}
		
		res = false;
		
		
		return res;
		
	}
	
	/**
	 * @author	: "Minwoo Ryu" [2019. 3. 16. 오 16:54:27]
	 * desc	: 검색 대상의 테이블의 전체 row 개수를 확인하기 위한 메소드 
	 *        PartitionKey를 정의하기 위한 용도로 활용되며, int형의 전체 row 개수를 반환 (0: 값이 없음)
	 *                
	 * @version	: 
	 * @return 	: 
	 * @throws 	: 
	 * @see		: 

	 * @param 
	 * @return
	 */
	
	public int selectNumberOfRows (String keySpace, String table) {
		
		Statement query = QueryBuilder.select().from(keySpace, table);
		
		ResultSet set = session.execute(query);
		
		
		List<Row> resRow = set.all();
		
		int allRow = resRow.size();
		
		return allRow;
		
		
	}
	
	
//	public ResultSet selectServiceCode (String keySpace, String table, String svcType, String domainname) {
//		
//		Statement query = QueryBuilder.select().from(keySpace, table)
//				.where(QueryBuilder.eq("domainname", domainname))
//				.and(QueryBuilder.eq("servicetype", svcType))
//				.orderBy(QueryBuilder.desc("servicetype"))
//				.allowFiltering();
//		
//		ResultSet resSet = session.execute(query);
//		
//		List<Row> rowList = resSet.all();
//		
//		for(Row row : rowList) {
//			
//			System.out.println(row.getString("servicetype"));
//			
//		}
//		
//		return resSet;
//	}

	public ArrayList<String> selectIntentNameList(String ksName, String tableName) throws ParseException {

		ArrayList<String> intentList = new ArrayList<String>();

		Statement query = QueryBuilder.select().from(ksName, tableName);
		ResultSet res = session.execute(query);
		
		cluster.close();

		List<Row> resList = res.all();

		for (Row row : resList) {

			String name = row.getString("intentname");
			String desc = row.getString("intentDesc");

			intentList.add(desc + " (" + name + ")");

		}

		return intentList;

	}

	public ArrayList<BaseIntentInfoForm> selectIntentInfo(String ksName, String tableName, String intentName)
			throws ParseException {

		JSONParser parser = new JSONParser();
		ArrayList<BaseIntentInfoForm> intentInfoList = new ArrayList<BaseIntentInfoForm>();

		Statement query = QueryBuilder.select().from(ksName, tableName)
				.where(QueryBuilder.eq("intentname", intentName));

		ResultSet res = session.execute(query);
		
		cluster.close();

		List<Row> resList = res.all();

		for (Row row : resList) {

			BaseIntentInfoForm intentForm = new BaseIntentInfoForm();

			// intentForm.setSeqNum(row.getInt("seqNum"));
			intentForm.setIntentName(row.getString("intentname"));
			intentForm.setDesc(row.getString("intentDesc"));
			intentForm.setArr((JSONArray) parser.parse(row.getString("dicList")));

			intentInfoList.add(intentForm);

		}

		return intentInfoList;

	}

}
