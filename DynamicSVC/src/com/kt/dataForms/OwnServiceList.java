package com.kt.dataForms;

import java.util.ArrayList;
import java.util.Hashtable;

public class OwnServiceList {
	
	private static OwnServiceList ownServiceList;
	ArrayList<OwnServiceList> ownList = new ArrayList<OwnServiceList>();
	
	private OwnServiceList () {
		
	}
	
	public static OwnServiceList getInstance ()
	{
		if (ownServiceList == null) {
			ownServiceList = new OwnServiceList();
		}
		
		return ownServiceList;
	}
	
	public ArrayList<OwnServiceList> getOwnList() {
		return this.ownList;
	}
	
	
	

}
