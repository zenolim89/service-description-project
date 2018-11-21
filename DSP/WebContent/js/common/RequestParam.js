/**
 * Created by zenolim on 2018
 */

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
	console.log("인텐트 선택 = " + SelectedText + ":" + SelectedValue);
	var IntentInfo = new Object();
	IntentInfo.domainName = SelectedValue;
	var DctnrJsonInfo = JSON.stringify(IntentInfo);
	return DctnrJsonInfo;
}

// 작성된 서비스 등록 정보를 json 형식으로 반환
function getServiceRegReqParam() {
	var ServiceInfo = new Object();
	var DictionaryInfo = new Object();

	ServiceInfo.userAuth = document.getElementsByName('userAuth')[0].value;

	var interfaceType = document.getElementsByName("interfaceType");
	for (var i = 0; i < interfaceType.length; i++) {
		if (interfaceType[i].checked == true)
			ServiceInfo.interfaceType = interfaceType[i].value;
	}

	var serviceCode = document.getElementsByName("serviceCode")[0];
	ServiceInfo.serviceCode = serviceCode.options[serviceCode.selectedIndex].value;

	ServiceInfo.refAPI = document.getElementsByName('refAPI')[0].value;

	var intentName = document.getElementsByName("intentName")[0];
	ServiceInfo.intentName = intentName.options[intentName.selectedIndex].value;

	ServiceInfo.serviceDesc = document.getElementsByName('serviceDesc')[0].value;
	ServiceInfo.targetURL = document.getElementsByName('targetURL')[0].value;

	var dialogArray1 = new Array();
	var dialogArray2 = new Array();
	var dialogArray3 = new Array();

	var facilitiesItems = document.getElementsByName("facilitiesItems");
	for (var i = 0; i < facilitiesItems.length; i++) {
		if (facilitiesItems[i].checked == true)
			dialogArray1.push(facilitiesItems[i].value);
	}
	if (dialogArray1.length)
		DictionaryInfo.facilitiesItems = dialogArray1;

	var liveWebcamsItems = document.getElementsByName("liveWebcamsItems");
	for (var i = 0; i < liveWebcamsItems.length; i++) {
		if (liveWebcamsItems[i].checked == true)
			dialogArray2.push(liveWebcamsItems[i].value);
	}
	if (dialogArray2.length)
		DictionaryInfo.liveWebcamsItems = dialogArray2;

	var resortItems = document.getElementsByName("resortItems");
	for (var i = 0; i < resortItems.length; i++) {
		if (resortItems[i].checked == true)
			dialogArray3.push(resortItems[i].value);
	}
	if (dialogArray3.length)
		DictionaryInfo.resortItems = dialogArray3;
	ServiceInfo.refDialog = DictionaryInfo;

	var method = document.getElementsByName("method")[0];
	ServiceInfo.method = method.options[method.selectedIndex].value;

	var dataType = document.getElementsByName("dataType")[0];
	ServiceInfo.dataType = dataType.options[dataType.selectedIndex].value;

	var dataDefinition = document.getElementsByName("dataDefinition")[0];
	ServiceInfo.dataDefinition = dataDefinition.options[dataDefinition.selectedIndex].value;

	var DFInfoArr = new Array();
	var dataFormatArr = document.getElementsByName("dataFormatArr");
	for (var i = 0; i < dataFormatArr.length; i++) {
		var DFInfobj = new Object();
		DFInfobj.keyName = document.getElementsByName("keyName")[i].value;
		DFInfobj.valueName = document.getElementsByName("valueName")[i].value;
		DFInfobj.superVar = document.getElementsByName("superVar")[i].value;
		var type = document.getElementsByName("type")[i];
		DFInfobj.type = type.options[type.selectedIndex].value;
		DFInfobj.userDefine = document.getElementsByName("userDefine")[i].value;
		var subArrType = document.getElementsByName("subArrType")[i];
		DFInfobj.subArrType = subArrType.options[subArrType.selectedIndex].value;
		DFInfobj.subArr = document.getElementsByName("subArr")[i].value;
		DFInfoArr.push(DFInfobj);
	}
	ServiceInfo.dataFormat = DFInfoArr;
	var svcJsonInfo = JSON.stringify(ServiceInfo);
	return svcJsonInfo;
}
