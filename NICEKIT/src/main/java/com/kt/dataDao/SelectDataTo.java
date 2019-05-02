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
import com.kt.commonUtils.Constants;
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
		JSONObject resObj = null;
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

		for (Row r : rowList) {

			DiscoveredServiceDESC desc = new DiscoveredServiceDESC();

			desc.setComURL(r.getString("commurl"));
			desc.setDomainId(r.getString("domainid"));
			desc.setTestURL(r.getString("testurl"));
			desc.setMethod(r.getString("method"));
			desc.setDataType(r.getString("datatype"));

			desc.setToUrl(r.getString("servicelink"));
			desc.setComURL(r.getString("commurl"));
			desc.setTestURL(r.getString("testurl"));
			desc.setServiceType(r.getString("servicetype"));

			desc.setStrHeaderInfo(r.getString("headerinfo"));
			desc.setStrReqSpec(r.getString("requestspec"));
			desc.setStrReqStructure(r.getString("requestformat"));
			desc.setStrResSpec(r.getString("responsespec"));
			desc.setStrResStructure(r.getString("responseformat"));
			desc.setStrDicList(r.getString("diclist"));

			try {
			
			resObj = new JSONObject();
			resObj = enabler.discoverMatchingWord(desc, word);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			resObj.put("resCode", "200");
			resObj.put("resMsg", "성공");
			resObj.put("toUrl", r.getString("servicelink"));
			resObj.put("serviceType", r.getString("servicetype"));
			resObj.put("testurl", r.getString("testurl"));
			
			System.out.println(r.toString());
			
			if (resObj != null)
				return resObj;
		}

		cluster.close();

		return resObj;
	}

	public DiscoveredServiceDESC selectServiceInfo(String keySpace, String tableName, String intentName) {

		ServiceEnabler enabler = new ServiceEnabler();

		Statement query = QueryBuilder.select().from(keySpace, tableName)
				.where(QueryBuilder.eq("intentname", intentName)).allowFiltering();
		ResultSet set = session.execute(query);

		List<Row> rowList = set.all();

		ArrayList<DiscoveredServiceDESC> resList = new ArrayList<DiscoveredServiceDESC>();

		DiscoveredServiceDESC result = new DiscoveredServiceDESC();

		for (Row r : rowList) {

			DiscoveredServiceDESC desc = new DiscoveredServiceDESC();

			desc.setComURL(r.getString("commurl"));
			desc.setDomainId(r.getString("domainid"));
			desc.setTestURL(r.getString("testurl"));
			desc.setMethod(r.getString("method"));
			desc.setDataType(r.getString("datatype"));

			desc.setComURL(r.getString("commurl"));
			desc.setTestURL(r.getString("testurl"));
			desc.setServiceType(r.getString("servicetype"));

			desc.setStrHeaderInfo(r.getString("headerinfo"));
			desc.setStrReqSpec(r.getString("requestspec"));
			desc.setStrReqStructure(r.getString("requestformat"));
			desc.setStrResSpec(r.getString("responsespec"));
			desc.setStrResStructure(r.getString("responseformat"));
			desc.setStrDicList(r.getString("diclist"));

			result = desc;
		}

		cluster.close();

		return result;
	}

	/**
	 * @author : "Minwoo Ryu" [2019. 3. 21. 오후 2:27:18] desc : "voice", "touch",
	 *         "remocon" 타입안에 속하는 서비스 명세만 불러옴
	 * @version :
	 * @param :
	 * @return : List<Row>
	 * @throws :
	 * @see :
	 * 
	 * @param table
	 * @return
	 */
	public List<Row> selectGetSpecInfo(String table) {

		Statement query = QueryBuilder.select().from(Constants.CASSANDRA_KEYSPACE_VENDOR, table)
				.where(QueryBuilder.in("invoketype", "voice", "touch", "remocon")).allowFiltering();

		ResultSet set = session.execute(query);

		cluster.close();

		List<Row> resList = set.all();

		return resList;

	}

	public List<Row> selectGetSpecId(String specName) {

		Statement query = QueryBuilder.select()
				.from(Constants.CASSANDRA_KEYSPACE_COMMON, Constants.CASSANDRA_TABLE_SPECINDEXLIST)
				.where(QueryBuilder.eq("specname", specName)).allowFiltering();
		ResultSet set = session.execute(query);
		cluster.close();
		List<Row> reslist = set.all();
		return reslist;

	}

	public List<Row> selectTemplateForPath(String urlPath) {

		Statement query = QueryBuilder.select()
				.from(Constants.CASSANDRA_KEYSPACE_COMMON, Constants.CASSANDRA_TABLE_TEMPLATEINDEXLIST)
				.where(QueryBuilder.eq("dirpath", urlPath)).allowFiltering();
		ResultSet set = session.execute(query);
		cluster.close();
		List<Row> resList = set.all();
		return resList;
	}

	public List<Row> selectVendorForPath(String urlPath) {

		Statement query = QueryBuilder.select()
				.from(Constants.CASSANDRA_KEYSPACE_VENDOR, Constants.CASSANDRA_TABLE_VENDORINDEXLIST)
				.where(QueryBuilder.eq("dirpath", urlPath)).allowFiltering();
		ResultSet set = session.execute(query);
		cluster.close();
		List<Row> resList = set.all();
		return resList;
	}

	public List<Row> selectVendorForSpec(String specName) {

		Statement query = QueryBuilder.select()
				.from(Constants.CASSANDRA_KEYSPACE_VENDOR, Constants.CASSANDRA_TABLE_VENDORINDEXLIST)
				.where(QueryBuilder.eq("specname", specName)).allowFiltering();

		ResultSet set = session.execute(query);
		cluster.close();

		List<Row> resList = set.all();

		return resList;
	}

	public List<Row> selectVendorTable(String specId) {

		Statement query = QueryBuilder.select().from(Constants.CASSANDRA_KEYSPACE_VENDOR, specId).allowFiltering();

		ResultSet set = session.execute(query);
		cluster.close();
		List<Row> resList = set.all();
		return resList;
	}

	/**
	 * @author : "Minwoo Ryu" [2019. 3. 20. 오전 11:47:47] desc : TemplateList와
	 *         venderindexlist에 하나의 select로 구현하기 위하여 keySpace와 table 이름을 inbound에서
	 *         하드코딩으로 처리하였음 데모가 끝나고 변경해야함
	 * @version :
	 * @param :
	 * @return : List<Row>
	 * @throws :
	 * @see :
	 * 
	 * @param vendorName
	 * @param tableName
	 * @param keySpace
	 * @return
	 */
	public List<Row> selectUseTemplateofVendor(String vendorName) {

		Statement query = QueryBuilder.select()
				.from(Constants.CASSANDRA_KEYSPACE_VENDOR, Constants.CASSANDRA_TABLE_VENDORINDEXLIST)
				.where(QueryBuilder.eq("vendorname", vendorName)).allowFiltering();

		ResultSet set = session.execute(query);
		cluster.close();

		List<Row> resList = set.all();

		return resList;

	}

	public List<Row> selectTemplatePreView(String templateName) {

		Statement query = QueryBuilder.select()
				.from(Constants.CASSANDRA_KEYSPACE_COMMON, Constants.CASSANDRA_TABLE_TEMPLATEINDEXLIST)
				.where(QueryBuilder.eq("templatename", templateName)).allowFiltering();

		ResultSet set = session.execute(query);
		cluster.close();

		List<Row> resList = set.all();

		return resList;

	}

	public List<Row> selectVendorlistInDomain(String domainName) {

		InsertDataTo insertTo = new InsertDataTo();

		TableMetadata res = insertTo.checkExsitingTable(Constants.CASSANDRA_TABLE_VENDORINDEXLIST,
				Constants.CASSANDRA_KEYSPACE_VENDOR);

		if (res == null) {
			List<Row> reslist = null;

			return reslist;
		}

		Statement query = QueryBuilder.select()
				.from(Constants.CASSANDRA_KEYSPACE_VENDOR, Constants.CASSANDRA_TABLE_VENDORINDEXLIST)
				.where(QueryBuilder.eq("domainname", domainName)).allowFiltering();

		ResultSet set = session.execute(query);
		cluster.close();

		List<Row> reslist = set.all();

		return reslist;

	}

	public List<Row> selectTemplistInDomain(String domainName) {

		InsertDataTo insertTo = new InsertDataTo();

		TableMetadata res = insertTo.checkExsitingTable(Constants.CASSANDRA_TABLE_TEMPINDEXLIST,
				Constants.CASSANDRA_KEYSPACE_COMMON);

		if (res == null) {
			List<Row> reslist = null;

			return reslist;
		}

		Statement query = QueryBuilder.select()
				.from(Constants.CASSANDRA_KEYSPACE_COMMON, Constants.CASSANDRA_TABLE_TEMPINDEXLIST)
				.where(QueryBuilder.eq("domainname", domainName)).allowFiltering();

		ResultSet set = session.execute(query);
		cluster.close();

		List<Row> reslist = set.all();

		return reslist;

	}

	public List<Row> selectTempInfo(String vendorName, String domainName) {

		InsertDataTo insertTo = new InsertDataTo();

		TableMetadata res = insertTo.checkExsitingTable(Constants.CASSANDRA_TABLE_TEMPINDEXLIST,
				Constants.CASSANDRA_KEYSPACE_COMMON);

		if (res == null) {
			List<Row> reslist = null;

			return reslist;
		}

		Statement query = QueryBuilder.select()
				.from(Constants.CASSANDRA_KEYSPACE_COMMON, Constants.CASSANDRA_TABLE_TEMPINDEXLIST)
				.where(QueryBuilder.eq("vendorname", vendorName)).allowFiltering();

		ResultSet set = session.execute(query);
		cluster.close();

		List<Row> reslist = set.all();

		return reslist;

	}

	/**
	 * @author : "Minwoo Ryu" [2019. 3. 18. 오후 5:23:05] desc : 생성기의 요청에 따라 commonks에
	 *         저장된 speclist를 조회하는 메소스 테이블 생성 유무에 따른 에러 메시지를 정의해야하는지 여부 추후 고려 필요
	 * @version :
	 * @param :
	 * @return : List<Row>
	 * @throws :
	 * @see :
	 * 
	 * @param domainName
	 * @return
	 */
	public List<Row> selectSpecList(String domainName) {

		Statement query = QueryBuilder.select()
				.from(Constants.CASSANDRA_KEYSPACE_COMMON, Constants.CASSANDRA_TABLE_SPECINDEXLIST)
				.where(QueryBuilder.eq("domainname", domainName)).allowFiltering();

		ResultSet set = session.execute(query);
		cluster.close();

		List<Row> resList = set.all();

		return resList;
	}

	public JSONArray selectTemplateList(String domainName, ArrayList<String> serviceList) {
		JSONArray resList = new JSONArray();
		Statement query = QueryBuilder.select()
				.from(Constants.CASSANDRA_KEYSPACE_COMMON, Constants.CASSANDRA_TABLE_TEMPLATEINDEXLIST)
				.where(QueryBuilder.eq("domainname", domainName)).allowFiltering();

		ResultSet set = session.execute(query);
		cluster.close();

		List<Row> rowList = set.all();
		for (Row row : rowList) {
			JSONParser jsonParser = new JSONParser();
			JSONArray jsonArray;
			try {
				jsonArray = (JSONArray) jsonParser.parse(row.getString("servicelist"));
				if (jsonArray.containsAll(serviceList))
					resList.add(row.getString("templateName"));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return resList;

	}
	
	public String selectSpecName(String vendorName) {

		Statement query = QueryBuilder.select()
				.from(Constants.CASSANDRA_KEYSPACE_VENDOR, Constants.CASSANDRA_TABLE_VENDORINDEXLIST)
				.where(QueryBuilder.eq("vendorname", vendorName)).allowFiltering();

		ResultSet set = session.execute(query);
		cluster.close();

		List<Row> resList = set.all();

		return resList.get(0).getString("specname");

	}
	

	// public JSONArray selectDomainListToCommon() {
	//
	// Collection<TableMetadata> tables =
	// cluster.getMetadata().getKeyspace("domainks").getTables();
	//
	// List<String> tableList = tables.stream().map(tm ->
	// tm.getName()).collect(Collectors.toList());
	//
	// JSONArray arr = serializerTo.resDomainList(tableList);
	//
	// cluster.close();
	//
	// return arr;
	// }

	public JSONObject selectDomainList() {

		Statement query = QueryBuilder.select().from(Constants.CASSANDRA_KEYSPACE_COMMON,
				Constants.CASSANDRA_TABLE_DOMAINLIST);
		ResultSet res = session.execute(query);
		cluster.close();

		JSONObject resObj = serializerTo.resDomainList(res);

		return resObj;

	}

	public List<Row> selectDomainList2() {

		Statement query = QueryBuilder.select().from(Constants.CASSANDRA_KEYSPACE_COMMON,
				Constants.CASSANDRA_TABLE_DOMAINLIST);
		ResultSet res = session.execute(query);
		cluster.close();

		return res.all();

	}

	/**
	 * @author : "Minwoo Ryu" [2019. 3. 16. 오 16:54:27] desc : 요청하는 Item (i.e., 컬럼의
	 *         값)의 존재 유무를 확인하는 메소드 요청하는 Item 값이 존재할 경우 true를 반환하고, 존재하지 않을 경우 false를
	 *         반환
	 * 
	 * @version : 0.1
	 * @return : Boolean
	 * @throws :
	 * @see :
	 * 
	 * @param
	 * @return
	 */
	public Boolean isExistedItem(String keySpace, String table, String targetCn, String item) {

		Boolean res = null;

		Statement query = QueryBuilder.select().from(keySpace, table);

		ResultSet resSet = session.execute(query);

		List<Row> rowList = resSet.all();

		for (Row row : rowList) {

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
	 * @author : "Minwoo Ryu" [2019. 3. 16. 오 16:54:27] desc : 검색 대상의 테이블의 전체 row
	 *         개수를 확인하기 위한 메소드 PartitionKey를 정의하기 위한 용도로 활용되며, int형의 전체 row 개수를 반환
	 *         (0: 값이 없음)
	 * 
	 * @version :
	 * @return :
	 * @throws :
	 * @see :
	 * 
	 * @param
	 * @return
	 */

	public int selectNumberOfRows(String keySpace, String table) {

		Statement query = QueryBuilder.select().from(keySpace, table);

		ResultSet set = session.execute(query);

		List<Row> resRow = set.all();

		int allRow = resRow.size();

		return allRow;

	}

	// public ResultSet selectServiceCode (String keySpace, String table, String
	// svcType, String domainname) {
	//
	// Statement query = QueryBuilder.select().from(keySpace, table)
	// .where(QueryBuilder.eq("domainname", domainname))
	// .and(QueryBuilder.eq("servicetype", svcType))
	// .orderBy(QueryBuilder.desc("servicetype"))
	// .allowFiltering();
	//
	// ResultSet resSet = session.execute(query);
	//
	// List<Row> rowList = resSet.all();
	//
	// for(Row row : rowList) {
	//
	// System.out.println(row.getString("servicetype"));
	//
	// }
	//
	// return resSet;
	// }

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
	
	public String getSpecName(String vendorName) {
		
		//List<Row> specList = this.selectSpecList(domainName);
		
		//System.out.println(specList.toString());
		
		//String specName = specList.get(0).getString("specname");
		
		//String specId = this.selectGetSpecId(this.selectSpecName(vendorName)).get(0).getString("specid");
		
		String specName = this.selectSpecName(vendorName);
		
		return specName;
		
	}
	
	public String getSpecID(String specName) {
		
		String specId = this.selectGetSpecId(specName).get(0).getString("specid");
		
		return specId;
	}

}
