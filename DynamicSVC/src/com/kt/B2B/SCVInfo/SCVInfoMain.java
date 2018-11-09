package com.kt.B2B.SCVInfo;

import com.google.gson.Gson;

public class SCVInfoMain {

	public static void main(String[] args) {

		// (jdlee) java SCVInfo class to Json (use Gson developed by Google)
		SCVInfo scvinfo = new SCVInfo();
		scvinfo.setInterfaceType("voice");
		scvinfo.setSCVCode("RV010001");
		
		VoiceIntents Intents = new VoiceIntents();
		scvinfo.setIntent(Intents);
		
		Gson gson = new Gson();
		String json = gson.toJson(scvinfo);
		
		System.out.println(json);
		
	}

}
