package com.kt.dataForms;

public class HttpParam {
	
	private String Parameter;
	private String Value;
	private String Note;
	
	
	public String getParameter() {
		return Parameter;
	}
	public void setParameter(String parameter) {
		Parameter = parameter;
	}
	public String getValue() {
		return Value;
	}
	public void setValue(String value) {
		Value = value;
	}
	public String getNote() {
		return Note;
	}
	public void setNote(String note) {
		Note = note;
	}
	@Override
	public String toString() {
		return "HttpParam [Parameter=" + Parameter + ", Value=" + Value + ", Note=" + Note + "]";
	}
	
	

}
