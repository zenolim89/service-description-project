package com.kt.dataCreator_temp;

import java.util.ArrayList;

import com.kt.dataDao.DictionaryList;
import com.kt.dataForms.DictionaryForm;

public class DicCreator {
	DictionaryForm form = new DictionaryForm();

	public void createResortDic() {
		form.setDomainName("ViewINFRM");
		ArrayList<String> fa = new ArrayList<String>();
		ArrayList<String> webCam = new ArrayList<String>();
		ArrayList<String> intro = new ArrayList<String>();

		fa.add("식음업장");
		fa.add("레저/스포츠");
		fa.add("쇼핑/편의");
		fa.add("문화시설");
		fa.add("의무실");
		fa.add("교회/성당");
		fa.add("제휴업");

		webCam.add("A슬로프");
		webCam.add("B슬로프");
		webCam.add("H슬로프");
		webCam.add("I슬로프");
		webCam.add("G슬로프");
		webCam.add("스키장전경");
		webCam.add("마운틴정상");
		intro.add("리조트소개");
		intro.add("객실안내");

		form.getDictionaryList().put("부대시설항목", fa);
		form.getDictionaryList().put("실시간웹캠항목", webCam);
		form.getDictionaryList().put("리조트소개항목", intro);

		DictionaryList.getInstance().getDicList().add(form);
	}

	public void createShopDic() {
		DictionaryForm form = new DictionaryForm();

		form.setDomainName("Order");

		ArrayList<String> proc = new ArrayList<String>();

		proc.add("새우깡");
		proc.add("바나나우유");
		proc.add("짱구");
		proc.add("오징어땅콩");
		proc.add("뺴뺴로");
		proc.add("미닛메이드");
		proc.add("삼각김밥");
		proc.add("신라면");
		proc.add("진라면");
		proc.add("삼양라면");

		form.getDictionaryList().put("상품정보", proc);
		DictionaryList.getInstance().getDicList().add(form);
	}

	public void creatHospitalDic() {
		DictionaryForm form = new DictionaryForm();

		form.setDomainName("Concierge");

		ArrayList<String> info = new ArrayList<String>();
		ArrayList<String> body = new ArrayList<String>();

		info.add("가정의학과");
		info.add("방사선과");
		info.add("영상의학과");
		info.add("소아과");
		info.add("내분비과");
		info.add("감염내과");
		info.add("내분비내과");
		info.add("마취통증의학과");
		info.add("병리과");
		info.add("산부인과");
		info.add("외과");
		info.add("안과");
		info.add("신장내과");

		body.add("신장");
		body.add("몸무게");
		body.add("혈압");
		body.add("체지방");
		body.add("골격근");
		body.add("유전자");
		form.getDictionaryList().put("병원소개", info);
		form.getDictionaryList().put("신체정보", body);
		DictionaryList.getInstance().getDicList().add(form);
	}

}
