package com.kt.data.model;

import java.util.HashMap;

public class HotelWordId {

	private HashMap<String,String> wordIdMap;
	
	public HotelWordId() {
		
		this.init();
	}
	
	public String getWordId(String _word) {
		
		return this.wordIdMap.get(_word);
	}
	
	private void init() {
		
		if( wordIdMap == null) {
			this.wordIdMap = new HashMap<>();
		}
		
		this.wordIdMap.clear();
		
		this.wordIdMap.put("얼음","G0001");
		this.wordIdMap.put("슬리퍼","G0002");
		this.wordIdMap.put("아이용 슬리퍼","G0003");
		this.wordIdMap.put("가습기","G0004");
		this.wordIdMap.put("빈박스","G0005");
		this.wordIdMap.put("박스 테잎","G0006");
		this.wordIdMap.put("가위","G0007");
		this.wordIdMap.put("런드리 백","G0008");
		this.wordIdMap.put("런드리 리스트","G0009");
		this.wordIdMap.put("옷걸이","G0010");
		this.wordIdMap.put("바지 옷걸이","G0011");
		this.wordIdMap.put("체중계","G0012");
		this.wordIdMap.put("종이컵/뚜껑","G0013");
		this.wordIdMap.put("쇼핑백","G0014");
		this.wordIdMap.put("머그컵","G0015");
		this.wordIdMap.put("와인잔","G0016");
		this.wordIdMap.put("하이볼 잔","G0017");
		this.wordIdMap.put("각 티슈","G0018");
		this.wordIdMap.put("두루마리 휴지","G0019");
		this.wordIdMap.put("손톱깎기","G0020");
		this.wordIdMap.put("볼펜","G0021");
		this.wordIdMap.put("연필","G0022");
		this.wordIdMap.put("커피머신","G0023");
		this.wordIdMap.put("금고","G0024");
		this.wordIdMap.put("A4 용지","G0025");
		this.wordIdMap.put("편지지","G0026");
		this.wordIdMap.put("봉투","G0027");
		this.wordIdMap.put("포스트잇","G0028");
		this.wordIdMap.put("러기지 체어","G0029");
		this.wordIdMap.put("무료 생수","G0030");
		this.wordIdMap.put("바디워시","G0031");
		this.wordIdMap.put("바디로션","G0032");
		this.wordIdMap.put("샴푸","G0033");
		this.wordIdMap.put("린스","G0034");
		this.wordIdMap.put("비누","G0035");
		this.wordIdMap.put("칫솔/치약","G0036");
		this.wordIdMap.put("샤워캡","G0037");
		this.wordIdMap.put("반짇고리","G0038");
		this.wordIdMap.put("위생백","G0039");
		this.wordIdMap.put("빗","G0040");
		this.wordIdMap.put("면봉세트","G0041");
		this.wordIdMap.put("면봉","G0042");
		this.wordIdMap.put("면도기","G0043");
		this.wordIdMap.put("배스타월","G0044");
		this.wordIdMap.put("핸드타월","G0045");
		this.wordIdMap.put("페이스타월","G0046");
		this.wordIdMap.put("욕실매트","G0047");
		this.wordIdMap.put("담요","G0048");
		this.wordIdMap.put("이불","G0049");
		this.wordIdMap.put("솜베게","G0050");
		this.wordIdMap.put("거위털베게","G0051");
		this.wordIdMap.put("보드와베게","G0052");
		this.wordIdMap.put("가운","G0053");
		this.wordIdMap.put("베드시트","G0054");
		this.wordIdMap.put("공기청정기","G0055");
		this.wordIdMap.put("선풍기","G0056");
		this.wordIdMap.put("라디에이터","G0057");

	}
}
