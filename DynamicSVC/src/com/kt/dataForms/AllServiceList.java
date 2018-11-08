package com.kt.dataForms;

import java.util.ArrayList;

public class AllServiceList {
	
	private static AllServiceList svcListInstance;
	
	ArrayList<AllServiceListDataFormat> svcList = new ArrayList<AllServiceListDataFormat>();
	
	
	private AllServiceList () {
	
	}
	
	public static AllServiceList getInstance ()
	{
		if (svcListInstance == null) {
			svcListInstance = new AllServiceList();
		}
		
		return svcListInstance;
	}

	public ArrayList<AllServiceListDataFormat> getSvcList() {
		return svcList;
	}
	
}
