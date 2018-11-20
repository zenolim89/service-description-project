/*
 * Created by zenolim on 2018
 */

var authRequest;
var authJsonInfo;
var svcRegRequest;
var svcJsonInfo;
var svcListInfo;
var NERequest;
var NEJsonInfo;
var NEListInfo;
var selectValue;

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
	authRequest.open('POST', './auth');
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
		alert("[로그인 성공] \n" + "Detail : " + authRequest.responseText);
		console.log(authRequest.responseText);
		LoginDisplay();
		RegSVCList(authRequest.responseText);
	}
}

// 로그인 성공 시 화면 UI 변경
function LoginDisplay() {
	var loginForm = document.getElementById("loginForm");
	var loginCompl = document.getElementById("loginCompl");

	if (loginForm.style.display == 'none') {
		loginForm.style.display = 'block';
		loginCompl.style.display = 'none';
	} else {
		loginForm.style.display = 'none';
		loginCompl.style.display = 'block';
	}

	var ul = document.getElementById('svcListInfo');
	if (ul)
		while (ul.firstChild) {
			ul.removeChild(ul.firstChild);
		}
}

// 계정에 등록된 서비스 목록
function RegSVCList(loginResult) {
	var jsonObj = JSON.parse(loginResult);
	var ul = document.getElementById("svcListInfo");
	for (var i = 0; i < jsonObj.ServiceNum; i++) {
		var svcCodeName = jsonObj.regiServiceInfo[i].serviceCode;
		var li = document.createElement("li");
		li.innerHTML = "<li class='nav-item'><a class='nav-link'>"
				+ svcCodeName + "</a></li>";
		ul.appendChild(li);
	}
}

// 데이터포맷 테이블 행 추가
function addRow(id) {
	var tbody = document.getElementById(id);
	var row = document.createElement("tr");
	row.setAttribute("name", "dataFormatArr");

	var td1 = document.createElement("td");
	td1.innerHTML = "<input type=text class='form-control' placeholder='keyName' name='keyName' size=12 align=absmiddle >";

	var td2 = document.createElement("td");
	td2.innerHTML = "<input type=text class='form-control' placeholder='valueName' name='valueName' size=12 align=absmiddle >";

	var td3 = document.createElement("td");
	td3.innerHTML = "<input type=text class='form-control'placeholder='superVar' name='superVar' size=12 align=absmiddle >";

	var td4 = document.createElement("td");
	td4.innerHTML = "<select name='type' class='form-control'> <option value='' selected disabled>데이터 타입 선택</option> <option value='JSONObject'>JSONObject</option> <option value='JSONArray'>JSONArray</option> </select>";

	var td5 = document.createElement("td");
	td5.innerHTML = "<input type=text class='form-control'placeholder='userDefine' name='userDefine' size=12 align=absmiddle >";

	var td6 = document.createElement("td");
	td6.innerHTML = "<select name='subArrType'class='form-control'> <option value='' selected disabled>Sub데이터 타입 선택</option> <option value='object'>Object</option> <option value='string'>string</option> <option value='false'>none</option> </select>";

	var td7 = document.createElement("td");
	td7.innerHTML = "<input type=text class='form-control' placeholder='subArr' name='subArr' size=12 align=absmiddle >";

	var td8 = document.createElement("td");
	td8.innerHTML = "<input class='btn btn-danger' type='button' onclick='deleteLine(this);' value='삭제'>";

	row.appendChild(td1);
	row.appendChild(td2);
	row.appendChild(td3);
	row.appendChild(td4);
	row.appendChild(td5);
	row.appendChild(td6);
	row.appendChild(td7);
	row.appendChild(td8);
	tbody.appendChild(row);
}

// 데이터포맷 테이블 행 삭제
function deleteLine(obj) {
	var tr = $(obj).parent().parent();
	tr.remove();
}

// 서비스 등록 정보 Json으로 변환
function SVCReqParam() {
	var svcInfo = new Object();
	var NEInfo = new Object();

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
		NEInfo.facilitiesItems = dialogArray1;

	var liveWebcamsItems = document.getElementsByName("liveWebcamsItems");
	for (var i = 0; i < liveWebcamsItems.length; i++) {
		if (liveWebcamsItems[i].checked == true)
			dialogArray2.push(liveWebcamsItems[i].value);
	}
	if (dialogArray2.length)
		NEInfo.liveWebcamsItems = dialogArray2;

	var resortItems = document.getElementsByName("resortItems");
	for (var i = 0; i < resortItems.length; i++) {
		if (resortItems[i].checked == true)
			dialogArray3.push(resortItems[i].value);
	}
	if (dialogArray3.length)
		NEInfo.resortItems = dialogArray3;
	svcInfo.refDialog = NEInfo;

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

// Ajax 서비스등록 요청 초기화
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
	svcRegRequest.open('POST', './registration');
	svcRegRequest.setRequestHeader('Content-Type', 'application/json'); // 컨텐츠타입
	svcRegRequest.send(svcJsonInfo);
	svcRegRequest.onreadystatechange = svcRespProcess;
}

// Ajax 서비스등록 응답 처리(Json)
function svcRespProcess() {
	if (svcRegRequest.readyState == 4 && svcRegRequest.status == 200) {
		alert(svcRegRequest.responseText);
		console.log("[등록 성공] \n" + "Detail : " + svcRegRequest.responseText);
	}
}

// Ajax 인텐트 어휘사전 UI 변경
function chageIntentSelect() {
	var langSelect = document.getElementById("intentName");
	// select element에서 선택된 option의 value가 저장된다.
	selectValue = langSelect.options[langSelect.selectedIndex].value;
	// select element에서 선택된 option의 text가 저장된다.
	var selectText = langSelect.options[langSelect.selectedIndex].text;
	console.log("선택 = " + selectText + ":" + selectValue);
	var IntentInfo = new Object();
	IntentInfo.domainName = selectValue;
	NEJsonInfo = JSON.stringify(IntentInfo);
	NEReqFunction();
}

// Ajax 어휘사전 요청 초기화
function createXMLHttpNEReq() {
	if (window.ActiveXObject) {
		NERequest = new ActiveXObject("Microsoft.XMLHTTP");
	} else if (window.XMLHttpRequest) {
		NERequest = new XMLHttpRequest();
	}
}

// Ajax 어휘사전 요청 처리(Json)
function NEReqFunction() {
	createXMLHttpNEReq();
	NERequest.open('POST', './dictionary');
	NERequest.setRequestHeader('Content-Type', 'application/json'); // 컨텐츠타입
	NERequest.send(NEJsonInfo);
	NERequest.onreadystatechange = NERespProcess;
}

// Ajax 어휘사전 응답 처리(Json)
function NERespProcess() {
	if (NERequest.readyState == 4 && NERequest.status == 200) {
		console.log(NERequest.responseText);
		NEListInfo = NERequest.responseText;
		var table = document.getElementById("mainTable");
		table.innerHTML = "";
		table.innerHTML = "<tbody>"
				+ "<tr><th>사용자 계정</th>"
				+ "<td><input class='form-control' type='text' value='V000001'name='userAuth' readonly></td></tr>"
				+ "<tr><th>인터페이스 타입</th><td><div class='form-check'>"
				+ "<label><input type='radio' name='interfaceType' value='touch'><span class='label-text'>터치</span></label>"
				+ "<label><input type='radio' name='interfaceType' value='voice' checked='checked'><span class='label-text'>음성</span></label>"
				+ "<label><input type='radio' name='interfaceType' value='remocon'><span class='label-text'>리모컨</span></label></div></td></tr>"
				+ "<tr><th>서비스 코드</th><td><select class='form-control' name='serviceCode'>"
				+ "<option value='' selected disabled>서비스 코드 선택</option>"
				+ "<option value='REOAKSVC001'>REOAKSVC001</option>"
				+ "<option value='REOAKSVC002'>REOAKSVC002</option>"
				+ "<option value='REOAKSVC003'>REOAKSVC003</option>"
				+ "<option value='REOAKSVC004'>REOAKSVC004</option>"
				+ "<option value='REOAKSVC005'>REOAKSVC005</option>"
				+ "<option value='REOAKSVC006'>REOAKSVC006</option>"
				+ "</select></td></tr>"
				+ "<tr><th>참조 API</th><td><input class='form-control' type='text' name='refAPI'></td></tr>"
				+ "<tr><th>인텐트 명</th><td><select class='form-control' name='intentName' id='intentName' onchange='chageIntentSelect()'>"
				+ "<option value='' disabled>인텐트 명 선택</option>"
				+ "<option value='ViewINFRM'>ViewINFRM</option>"
				+ "<option value='Concierge'>Concierge</option>"
				+ "<option value='Amenity'>Amenity</option>"
				+ "<option value='Order'>Order</option>"
				+ "<option value='CheckOut'>CheckOut</option></select></td></tr>"
				+ NEListInfo
				+ "<tr><th>서비스 개요</th><td><textarea class='form-control' rows='5' cols='30' name='serviceDesc'></textarea></td></tr>"
				+ "<tr><th>타깃 URL</th><td><input class='form-control' type='text' name='targetURL' value='http://'></td></tr>"
				+ "<tr><th>전송 메소드</th><td><select class='form-control' name='method'>"
				+ "<option value='' selected disabled>전송 방식 선택</option>"
				+ "<option value='POST'>POST</option>"
				+ "<option value='GET'>GET</option></select></td></tr>"
				+ "<tr><th>데이터 타입</th><td><select class='form-control' name='dataType'>"
				+ "<option value='' selected disabled>데이터 타입 선택</option>"
				+ "<option value='json'>JSON</option>"
				+ "<option value='xml'>XML</option></select></td></tr>"
				+ "<tr><th>데이터 정의</th><td><select class='form-control' name='dataDefinition'>"
				+ "<option value='' selected disabled>데이터 정의 선택</option>"
				+ "<option value='ktown'>ktown</option>"
				+ "<option value='3rdparty'>3rd party</option>"
				+ "<option value='3rdpaty'>3rd party</option>"
				+ "<option value='3rdpaty'>3rd party</option></select></td></tr>"
				+ "<tr><th>데이터 포맷</th><td><table class='subType' id='DFtable'><tr><td colspan='8'>"
				+ "<input class='btn btn-primary' type='button' size='20' value='추가' onClick=\"addRow('DFtable');\"></td></tr></table></td></tr></tbody>"
				+ "<tfoot><tr><td colspan='2'>"
				+ "<input class='btn btn-warning' type='button' onClick='svcReqFunction();' value='등록' />"
				+ "<input class='btn btn-warning' type='button' value='파일업로드' />"
				+ "<input class='btn btn-warning' type='reset' value='취소' />"
				+ "</td></tr></tfoot>";

		$("#intentName").val(selectValue).prop("selected", true);
	}
}
