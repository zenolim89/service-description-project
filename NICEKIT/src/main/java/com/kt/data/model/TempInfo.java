package com.kt.data.model;

public class TempInfo {

	private String vendorName;
	private String specName;

	public TempInfo(String vendorName, String specName) {
		this.vendorName = vendorName;
		this.specName = specName;
	}
	
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	public String getSpecName() {
		return specName;
	}
	public void setSpecName(String specName) {
		this.specName = specName;
	}
	
	
}
