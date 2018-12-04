package com.kt.dataDao;

import java.util.Hashtable;

public class Account {
	
	private static Account account;
	private Hashtable<String, String> hash = new Hashtable<String, String>();
	
	private Account() {
		
		hash.put("V000001", "1234");
		hash.put("V000002", "1234");
		hash.put("V000003", "1234");
	}
	
	public static Account getInstance()
	{
		
		if (account == null) {
			account = new Account();
		}
		
		return account;		
	}
	
	public Hashtable<String, String> getAccountListTable () {
		
		return this.hash;
	}
}
