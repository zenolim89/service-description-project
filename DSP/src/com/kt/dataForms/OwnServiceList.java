package com.kt.dataForms;

import java.util.ArrayList;
import java.util.Hashtable;

public class OwnServiceList {

	private static OwnServiceList ownServiceList;

	ArrayList<OwnServiceForm> ownList = new ArrayList<OwnServiceForm>();

	private OwnServiceList () {

	}

	public static OwnServiceList getInstance ()
	{
		if (ownServiceList == null) {
			ownServiceList = new OwnServiceList();
		}

		return ownServiceList;
	}

	

	public ArrayList<OwnServiceForm> getOwnList() {
		System.out.println("반환된 서비스의 개수: " + ownList.size());
		
		return this.ownList;
		
	}
	
	public void setOwnList(OwnServiceForm form) {
		
		this.ownList.add(form);
		
		System.out.println("서비스 개수: " + ownList.size());
	}
}
