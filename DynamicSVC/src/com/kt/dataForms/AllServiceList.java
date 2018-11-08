package com.kt.dataForms;

import java.util.ArrayList;
import java.util.Hashtable;

public class AllServiceList {
	
	String serviceCode;
	String desc;
	String funcName;
	
	private static AllServiceList svcListInstance;
	
	ArrayList<AllServiceList> svcList = new ArrayList<AllServiceList>();
	
	
	private AllServiceList () {
	
	}
	
	public static AllServiceList getInstance ()
	{
		if (svcListInstance == null) {
			svcListInstance = new AllServiceList();
		}
		
		return svcListInstance;
	}
	
	public String getServiceCode() {
		return serviceCode;
	}
	
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	
	public String getDesc() {
		return desc;
	}
	
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public String getFuncName() {
		return funcName;
	}
	
	public void setFuncName(String funcName) {
		this.funcName = funcName;
	}
	
	
	
}
