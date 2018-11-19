var authRequest;
var authJsonInfo;
var svcRegRequest;
var svcJsonInfo;

// Ajax 인증 요청 초기화
function createXMLHttpAuthReq() {
	if (window.ActiveXObject) {
		authRequest = new ActiveXObject("Microsoft.XMLHTTP");
	} else if (window.XMLHttpRequest) {
		authRequest = new XMLHttpRequest();
	}
}

// Ajax 인증 요청 처리(Json)
function authReqFunction() {
	createXMLHttpAuthReq();
	AuthReqParam();
	authRequest.open('POST', 'http://localhost:8080/DSP/auth');
	authRequest.setRequestHeader('Content-Type', 'application/json'); // 컨텐츠타입
	authRequest.send(authJsonInfo);
	authRequest.onreadystatechange = AuthRespProcess;
}

// 계정 정보 Json으로 변환
function AuthReqParam() {
	var authInfo = new Object();
	authInfo.id = encodeURIComponent(document.getElementById("id").value);
	authInfo.pw = encodeURIComponent(document.getElementById("pw").value);
	authJsonInfo = JSON.stringify(authInfo); // 요청 데이터를 stringify
	console.log(authJsonInfo);
}

// Ajax 인증 응답 처리(Json)
function AuthRespProcess() {
	if (authRequest.readyState == 4 && authRequest.status == 200) {
		alert(authRequest.responseText);
		console.log(authRequest.responseText);
		/*
		 * document.getElementById("loginForm").style.display = "none"; // 로그인
		 * document.getElementById("loginCompl").style.display = "block"; // 로그인
		 * RegSVCList();
		 */
	}
}

// 계정에 등록된 서비스 목록
function RegSVCList() {
	var ul = document.getElementById("svcList");
	ul.innerHTML = "";
	var li1 = document.createElement("li");
	li1.appendChild(document.createTextNode("- (서비스 명)"));
	ul.appendChild(li1);

	var li2 = document.createElement("li");
	li2.appendChild(document.createTextNode("- (서비스 명)"));
	ul.appendChild(li2);

	var li3 = document.createElement("li");
	li3.appendChild(document.createTextNode("- (서비스 명)"));
	ul.appendChild(li3);

	var li4 = document.createElement("li");
	li4.appendChild(document.createTextNode("- (서비스 명)"));
	ul.appendChild(li4);
}

/*
 * { "userAuth": "V000001", "interfaceType": "Voice", "serviceCode": "RSVC001",
 * "refAPI": "pageMoveAPI", "intentName": "ViewIFRM", "serviceDesc": "리조트 정보
 * 화면으로 전환", "refDialog": { "facilitiesItems": [ "food", "sports", "shopping",
 * "cultural", "dispensary", "church", "affiliate" ], "liveWebcamsItems": [
 * "a_slopes", "b_slopes", "h_slopes", "i_slopes", "g_slopes", "mountain_view",
 * "mountain_summit" ], "resortItems": [ "rateGuide", "roomInfo" ] },
 * "targetURL": "http://", "method": "POST", "dataType": "JSON",
 * "dataDefinition": "OWN", "dataFormat": [ { "keyName": "propertyId",
 * "valueName": "htshcles", "superVar": "false", "type": "JSONObject",
 * "userDefine": "true", "subArrType": "false", "subArr": "false" }, {
 * "keyName": "roomNo", "valueName": "1101", "superVar": "false", "type":
 * "JSONObject", "userDefine": "true", "subArrType": "false", "subArr": "false" }, {
 * "keyName": "reqData", "valueName": "false", "superVar": "false", "type":
 * "JSONArray", "userDefine": "true", "subArrType": "object", "subArr": [ {
 * "svcType": "st00001101" }, { "svcevtPrprtys": [ { "svcevtPrprtyCd":
 * "pp00001101", "svcevtPrprtyVal": "G0001" }, { "svcevPrprtyCd": "pp00001102",
 * "svcevtPrprtyVal": "1" } ] } ] } ] }
 */

//데이터포맷 테이블 행 추가
function addRow(id) {
	var tbody = document.getElementById(id);
	var row = document.createElement("tr");
	row.setAttribute("name", "dataFormatArr");

	var td1 = document.createElement("td");
	td1.innerHTML = "<input type=text placeholder='keyName' name='keyName' size=12 align=absmiddle >";

	var td2 = document.createElement("td");
	td2.innerHTML = "<input type=text placeholder='valueName' name='valueName' size=12 align=absmiddle >";

	var td3 = document.createElement("td");
	td3.innerHTML = "<input type=text placeholder='superVar' name='superVar' size=12 align=absmiddle >";

	var td4 = document.createElement("td");
	td4.innerHTML = "<select name='type'> <option value='' disabled>데이터 타입 선택</option> <option value='JSONObject'>JSONObject</option> <option value='JSONArray'>JSONArray</option> </select>";

	var td5 = document.createElement("td");
	td5.innerHTML = "<input type=text placeholder='userDefine' name='userDefine' size=12 align=absmiddle >";

	var td6 = document.createElement("td");
	td6.innerHTML = "<select name='subArrType'> <option value='' disabled>데이터 타입 선택</option> <option value='JSONObject'>JSONObject</option> <option value='JSONArray'>JSONArray</option> </select>";

	var td7 = document.createElement("td");
	td7.innerHTML = "<input type=text placeholder='subArr' name='subArr' size=12 align=absmiddle >";

	row.appendChild(td1);
	row.appendChild(td2);
	row.appendChild(td3);
	row.appendChild(td4);
	row.appendChild(td5);
	row.appendChild(td6);
	row.appendChild(td7);
	tbody.appendChild(row);
}

//서비스 등록 정보 Json으로 변환
function SVCReqParam() {
	var svcInfo = new Object();
	svcInfo.userAuth = document.getElementsByName('userAuth')[0].value;

	var interfaceType = document.getElementsByName("interfaceType");
	for (var i = 0; i < interfaceType.length; i++) {
		if (interfaceType[i].checked == true)
			svcInfo.interfaceType = interfaceType[i].value;
	}

	var serviceCode = document.getElementsByName("serviceCode")[0];
	svcInfo.serviceCode = serviceCode.options[serviceCode.selectedIndex].value;

	svcInfo.refAPI = document.getElementsByName('refAPI')[0].value;

	var intentName = document.getElementsByName("intentName")[0];
	svcInfo.intentName = intentName.options[intentName.selectedIndex].value;

	svcInfo.serviceDesc = document.getElementsByName('serviceDesc')[0].value;
	svcInfo.targetURL = document.getElementsByName('targetURL')[0].value;

	var dialogArray1 = new Array();
	var dialogArray2 = new Array();
	var dialogArray3 = new Array();

	var facilitiesItems = document.getElementsByName("facilitiesItems");
	for (var i = 0; i < facilitiesItems.length; i++) {
		if (facilitiesItems[i].checked == true)
			dialogArray1.push(facilitiesItems[i].value);
	}
	if (dialogArray1.length)
		svcInfo.facilitiesItems = dialogArray1;

	var liveWebcamsItems = document.getElementsByName("liveWebcamsItems");
	for (var i = 0; i < liveWebcamsItems.length; i++) {
		if (liveWebcamsItems[i].checked == true)
			dialogArray2.push(liveWebcamsItems[i].value);
	}
	if (dialogArray2.length)
		svcInfo.liveWebcamsItems = dialogArray2;

	var resortItems = document.getElementsByName("resortItems");
	for (var i = 0; i < resortItems.length; i++) {
		if (resortItems[i].checked == true)
			dialogArray3.push(resortItems[i].value);
	}
	if (dialogArray3.length)
		svcInfo.resortItems = dialogArray3;

	var method = document.getElementsByName("method")[0];
	svcInfo.method = method.options[method.selectedIndex].value;

	var dataType = document.getElementsByName("dataType")[0];
	svcInfo.dataType = dataType.options[dataType.selectedIndex].value;

	var dataDefinition = document.getElementsByName("dataDefinition")[0];
	svcInfo.dataDefinition = dataDefinition.options[dataDefinition.selectedIndex].value;

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
	svcInfo.dataFormat = DFInfoArr;
	svcJsonInfo = JSON.stringify(svcInfo);
	console.log(svcJsonInfo);
}

//Ajax 서비스등록 요청 초기화
function createXMLHttpSVCReq() {
	if (window.ActiveXObject) {
		svcRegRequest = new ActiveXObject("Microsoft.XMLHTTP");
	} else if (window.XMLHttpRequest) {
		svcRegRequest = new XMLHttpRequest();
	}
}

// Ajax 서비스등록 요청 처리(Json)
function svcReqFunction() {
	createXMLHttpSVCReq();
	SVCReqParam();
	svcRegRequest.open('POST', 'http://localhost:8080/DSP/registration');
	svcRegRequest.setRequestHeader('Content-Type', 'application/json'); // 컨텐츠타입
	svcRegRequest.send(svcJsonInfo);
	svcRegRequest.onreadystatechange = svcRespProcess;
}

//Ajax 서비스등록 응답 처리(Json)
function svcRespProcess() {
	if (svcRegRequest.readyState == 4 && svcRegRequest.status == 200) {
		alert(svcRegRequest.responseText);
		console.log(svcRegRequest.responseText);
		/*
		 * document.getElementById("loginForm").style.display = "none"; // 로그인
		 * document.getElementById("loginCompl").style.display = "block"; // 로그인
		 * RegSVCList();
		 */
	}
}


