package com.kt.B2B.SCVInfo;

import java.util.ArrayList;

// (jdlee)
public class VoiceIntents {
	
	private ArrayList<ArrayList<slot>> Intents = new ArrayList<ArrayList<slot>>();
	//private ArrayList<slot> IntentSlots = new ArrayList<slot>();
	
	public ArrayList<ArrayList<slot>> getIntents() {
		return Intents;
	}
	public void setIntents(ArrayList<ArrayList<slot>> intents) {
		Intents = intents;
	}
}

class slot {
	private String NE;
	private String PR;
	
	
	public slot() {
		super();

	}

	public slot(String nE, String pR) {
		super();
		NE = nE;
		PR = pR;
	}

	public String getNE() {
		return NE;
	}
	
	public void setNE(String nE) {
		NE = nE;
	}
	
	public String getPR() {
		return PR;
	}
	
	public void setPR(String pR) {
		PR = pR;
	}
	
	
	
}