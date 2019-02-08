package com.kt.dataDao;

import java.util.ArrayList;
import java.util.Hashtable;

import com.kt.dataForms.BaseSvcForm;

public class OwnServiceList {

	private static OwnServiceList ownServiceList;

	ArrayList<BaseSvcForm> ownList = new ArrayList<BaseSvcForm>();

	private OwnServiceList () {

	}

	public static OwnServiceList getInstance ()
	{
		if (ownServiceList == null) {
			ownServiceList = new OwnServiceList();
		}

		return ownServiceList;
	}

	

	public ArrayList<BaseSvcForm> getOwnList() {
		
		return this.ownList;
		
	}
	
	public void setOwnList(BaseSvcForm form) {
		
		this.ownList.add(form);
	}
}
