package com.kt.commonUtils;

public class Constants {
	/** 카산드라 DB 관련 */
	public static final String CASSANDRA_PROPERTY_HOST = "222.107.124.9";

	public static final String CASSANDRA_KEYSPACE_COMMON = "commonks";
	public static final String CASSANDRA_TABLE_DOMAINLIST = "domainlist";
	public static final String CASSANDRA_TABLE_DOMAINSVCLIST = "domainservicelist";
	public static final String CASSANDRA_TABLE_INTENTINFO = "intentinfo";
	public static final String CASSANDRA_TABLE_SPECINDEXLIST = "specindexlist";
	public static final String CASSANDRA_TABLE_TEMPLATEINDEXLIST = "templateindexlist";
	public static final String CASSANDRA_TABLE_TEMPINDEXLIST = "tempindexlist";

	public static final String CASSANDRA_KEYSPACE_DOMAIN = "domainks";
	public static final String CASSANDRA_TABLE_RESORT = "resort";

	public static final String CASSANDRA_KEYSPACE_VENDOR = "vendorsvcks";
	public static final String CASSANDRA_TABLE_VENDORINDEXLIST = "vendorindexlist";

	/** 외부 폴더 경로 */
	public static final String EXTERNAL_FOLDER_REALPATH = "/opt/nicekit";
	public static final String EXTERNAL_FOLDER_URLPATH_SAMPLE = "/docbase/sample";
	public static final String EXTERNAL_FOLDER_URLPATH_TEMP = "/docbase/temp";
	public static final String EXTERNAL_FOLDER_URLPATH_TEMPLATE = "/docbase/template";
	public static final String EXTERNAL_FOLDER_URLPATH_VENDORS = "/docbase/vendors";
}
