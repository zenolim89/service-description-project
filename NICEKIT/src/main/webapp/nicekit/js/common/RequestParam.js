$.getScript('./nbware/js/InterStandards.js', function() {
	console.log('InterStandards.js loading...');
});

// 사용자 계정 정보 json 형식으로 반환
function getAuthReqParam() {
	var AuthInfo = new Object();
	AuthInfo.id = encodeURIComponent(document.getElementById('id').value);
	AuthInfo.pw = encodeURIComponent(document.getElementById('pw').value);
	var AuthJsonInfo = JSON.stringify(AuthInfo); // 요청 데이터를 stringify
	console.log(AuthJsonInfo);
	return AuthJsonInfo;
}

// 선택된 인텐트명을 json 형식으로 반환
function getIntentSelectedParam() {
	var IntentName = document.getElementById('intentName');
	// select element에서 선택된 option의 value가 저장된다.
	var SelectedValue = IntentName.options[IntentName.selectedIndex].value;
	// select element에서 선택된 option의 text가 저장된다.
	var SelectedText = IntentName.options[IntentName.selectedIndex].text;
	var IntentInfo = new Object();
	IntentInfo.domainName = SelectedValue;
	var DctnrJsonInfo = JSON.stringify(IntentInfo);
	return DctnrJsonInfo;
}

// 작성된 서비스 등록 정보를 json 형식으로 반환
function getServiceRegReqParam() {
	var ServiceInfo = new Object();
	var DictionaryInfo = new Object();

	ServiceInfo["domainId"] = document.getElementsByName('domainId')[0].value;

	var domainName = document.getElementsByName("domainName")[0];
	ServiceInfo["domainName"] = "RESORT";
	//ServiceInfo["domainName"] = domainName.options[domainName.selectedIndex].value;

	var interfaceType = document.getElementsByName("interfaceType");
	for (var i = 0; i < interfaceType.length; i++) {
		if (interfaceType[i].checked == true)
			ServiceInfo["interfaceType"] = interfaceType[i].value;
	}

	var serviceCode = document.getElementsByName("serviceCode")[0];
	ServiceInfo["serviceCode"] = serviceCode.options[serviceCode.selectedIndex].value;
	ServiceInfo["serviceDesc"] = document.getElementsByName('serviceDesc')[0].value;

	var intentInfo = new Array();
	var intentObj = new Object();
	var dicList = new Array();
	var dicListTemp = new Array();

	var dicName = document.getElementsByClassName("option-input checkbox");
	for (var i = 0; i < dicName.length; i++) {
		dicListTemp.push(dicName[i].getAttribute("name"));
	}
	dicListTemp = $.unique(dicListTemp); // 배열 중복제거

	for (var i = 0; i < dicListTemp.length; i++) {
		var dicObj = new Object();
		var wordList = new Array();
		dicObj.dicName = dicListTemp[i]; // 선택된 단어의 사전명
		var dicParam = document.getElementsByName(dicListTemp[i]);
		for (var j = 0; j < dicParam.length; j++) {
			var wordObj = new Object();
			if (dicParam[j].checked == true) {
				wordObj.word = dicParam[j].value;
				wordObj.similarWord = [ dicParam[j].value ];
				wordList.push(wordObj);
			}
		}
		if (wordList.length != 0) {
			dicObj.wordList = wordList;
			dicList.push(dicObj);
		}
	}

	var intentName = document.getElementsByName("intentName")[0];
	intentObj.id = intentName.options[intentName.selectedIndex].value;
	intentObj.dicList = dicList;

	intentInfo.push(intentObj);
	ServiceInfo["intentInfo"] = intentInfo;

	var InterStandards = validationData();
	ServiceInfo["testUrl"] = InterStandards["testUrl"];
	ServiceInfo["comUrl"] = InterStandards["comUrl"];
	ServiceInfo["method"] = InterStandards["method"];
	ServiceInfo["headerInfo"] = InterStandards["headerInfo"];
	ServiceInfo["dataType"] = InterStandards["dataType"];

	var reqStructureArr = new Array(InterStandards["reqStructure"]);
	var reqSpecArr = new Array(InterStandards["reqSpec"]);
	var resStructureArr = new Array(InterStandards["resStructure"]);
	var resSpecArr = new Array(InterStandards["resSpec"]);

	ServiceInfo["reqStructure"] = reqStructureArr;
	ServiceInfo["reqSpec"] = reqSpecArr;
	ServiceInfo["resStructure"] = resStructureArr;
	ServiceInfo["resSpec"] = resSpecArr;

	var svcJsonInfo = JSON.stringify(ServiceInfo);
	console.log(svcJsonInfo);
	return svcJsonInfo;
}
