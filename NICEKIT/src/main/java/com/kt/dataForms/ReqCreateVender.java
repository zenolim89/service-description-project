package com.kt.dataForms;

public class ReqCreateVender {
	
	String vender;
	String domainName;
	String templateUrl;
	String venderUrl;
	
	public String getVenderUrl () {
		
		return venderUrl;
	}
	
	public void setVenderUrl (String url) {
		this.venderUrl = url;
	}
	
	public String getVender() {
		return vender;
	}
	public void setVender(String vender) {
		this.vender = vender;
	}
	public String getDomainName() {
		return domainName;
	}
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	public String getTemplateUrl() {
		return templateUrl;
	}
	public void setTemplateUrl(String templateUrl) {
		this.templateUrl = templateUrl;
	}

}
