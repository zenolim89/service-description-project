/**
 * @fileOverview ServerRequest
 * @author zenolim <zenolim89@gmail.com>
 * @version 1.0.0
 */

$.getScript('http://svcapi.gigagenie.ai/sdk/v1.0/js/gigagenie.js', function() {
	console.log('gigagenie.js loading...');
});

$.getScript('/NICEKIT/nicekit/js/common/RequestParam.js', function() {
	console.log('RequestParam.js loading...');
});

$.getScript('/NICEKIT/nicekit/js/common/ChangeView.js', function() {
	console.log('ChangeView.js loading...');
});

var DomainRequest;
var IntentRequest;
var AuthRequest;
var DictionaryRequest;
var serviceRegRequest;
var intentRegRequest;
var SvcRequest;

/**
 * @file 서비스 등록 단계에서 도메인 리스트를 얻기 위한 도메인 리스트 요청 API.
 * @module ServerRequest/getDomainProc
 */

/**
 * @method createXMLHttpDomainReq
 * @param {undefined}
 * @returns {undefined}
 * @description Ajax 방식의 도메인 리스트 요청을 위한 XMLHttpRequest 객체를 생성하는 메소드로, 요청 전송 전에
 *              호출된다.
 * @example if (window.ActiveXObject) { DomainRequest = new
 *          ActiveXObject("Microsoft.XMLHTTP"); } else if
 *          (window.XMLHttpRequest) { DomainRequest = new XMLHttpRequest(); }
 */
function createXMLHttpDomainReq() {
	if (window.ActiveXObject) {
		DomainRequest = new ActiveXObject("Microsoft.XMLHTTP");
	}
	else if (window.XMLHttpRequest) {
		DomainRequest = new XMLHttpRequest();
	}
}

/**
 * @method domainReqFunction
 * @param {undefined}
 * @returns {undefined}
 * @description 도메인 리스트 요청 전송을 위한 메소드로, 요청 파라미터를 설정하는 부분과 서버로부터 응답이 도착했을 때 특정한
 *              자바스크립트 함수를 호출하는 부분을 포함하고 있다. 페이지가 로딩 됐을 때, 해당 함수가 호출된다.
 * @example
 */
function domainReqFunction() {
	createXMLHttpDomainReq();
	DomainRequest.open('GET', './getDomain');
	DomainRequest.setRequestHeader('Content-Type', 'application/json');
	DomainRequest.send();
	DomainRequest.onreadystatechange = domainRespProcess;
}

/**
 * @method domainRespProcess
 * @param {undefined}
 * @returns {undefined}
 * @description 도메인 리스트 요청에 대한 응답 처리를 위한 메소드로, 서버로부터 응답이 도착하면 요청을 전송한
 *              XMLHttpRequest 객체로부터 호출된다. 이때, 사용되는 Property는 XMLHttpRequest 객체의
 *              onreadystatechange 이다.
 * @example DomainRequest.onreadystatechange = domainRespProcess;
 */
function domainRespProcess() {
	if (DomainRequest.readyState == 4 && DomainRequest.status == 200) {
		console.log("[도메인 리스트] \n" + "Detail : " + DomainRequest.responseText);
		var domainObj = JSON.parse(DomainRequest.responseText);
		addDomainList(domainObj['resData']['domainList']);
	}
}

/**
 * @file 서비스 등록 단계에서 인텐트 리스트를 얻기 위한 인텐트 리스트 요청 API.
 * @module ServerRequest/getIntentListProc
 */

/**
 * @method createXMLHttpIntentReq
 * @param {undefined}
 * @returns {undefined}
 * @description Ajax 방식의 도메인 리스트 요청을 위한 XMLHttpRequest 객체를 생성하는 메소드로, 요청 전송 전에
 *              호출된다.
 * @example if (window.ActiveXObject) { IntentRequest = new
 *          ActiveXObject("Microsoft.XMLHTTP"); } else if
 *          (window.XMLHttpRequest) { IntentRequest = new XMLHttpRequest(); }
 */
function createXMLHttpIntentReq() {
	if (window.ActiveXObject) {
		IntentRequest = new ActiveXObject("Microsoft.XMLHTTP");
	}
	else if (window.XMLHttpRequest) {
		IntentRequest = new XMLHttpRequest();
	}
}

/**
 * @method intentReqFunction
 * @param {undefined}
 * @returns {undefined}
 * @description 인텐트 리스트 요청 전송을 위한 메소드로, 요청 파라미터를 설정하는 부분과 서버로부터 응답이 도착했을 때 특정한
 *              자바스크립트 함수를 호출하는 부분을 포함하고 있다. 페이지가 로딩 됐을 때, 해당 함수가 호출된다.
 * @example
 */
function intentReqFunction() {
	createXMLHttpIntentReq();
	IntentRequest.open('GET', './getIntentList');
	IntentRequest.setRequestHeader('Content-Type', 'application/json');
	IntentRequest.send();
	IntentRequest.onreadystatechange = intentRespProcess;
}

/**
 * @method intentRespProcess
 * @param {undefined}
 * @returns {undefined}
 * @description 도메인 리스트 요청에 대한 응답 처리를 위한 메소드로, 서버로부터 응답이 도착하면 요청을 전송한
 *              XMLHttpRequest 객체로부터 호출된다. 이때, 사용되는 Property는 XMLHttpRequest 객체의
 *              onreadystatechange 이다.
 * @example IntentRequest.onreadystatechange = intentRespProcess;
 */
function intentRespProcess() {
	if (IntentRequest.readyState == 4 && IntentRequest.status == 200) {
		console.log("[인텐트 리스트] \n" + "Detail : " + IntentRequest.responseText);
		var intentObj = JSON.parse(IntentRequest.responseText);
		var intentList = intentObj.intentList;
		addIntentList(intentList);
	}
}

/**
 * @file 서비스 등록 단계에서 사용자 로그인을 위한 사용자 인증 요청 API.
 * @module ServerRequest/AuthProc
 */

/**
 * @method createXMLHttpAuthReq
 * @param {undefined}
 * @returns {undefined}
 * @description Ajax 방식의 사용자 인증 요청을 위한 XMLHttpRequest 객체를 생성하는 메소드로, 요청 전송 전에
 *              호출된다.
 * @example if (window.ActiveXObject) { AuthRequest = new
 *          ActiveXObject("Microsoft.XMLHTTP"); } else if(window.XMLHttpRequest) {
 *          AuthRequest = new XMLHttpRequest(); }
 */
function createXMLHttpAuthReq() {
	if (window.ActiveXObject) {
		AuthRequest = new ActiveXObject("Microsoft.XMLHTTP");
	}
	else if (window.XMLHttpRequest) {
		AuthRequest = new XMLHttpRequest();
	}
}

/**
 * @method authReqFunction
 * @param {undefined}
 * @returns {undefined}
 * @description 사용자 인증 요청 전송을 위한 메소드로, 요청 파라미터를 설정하는 부분과 서버로부터 응답이 도착했을 때 특정한
 *              자바스크립트 함수를 호출하는 부분을 포함하고 있다. 사용자가 로그인 버튼을 클릭했을 때, 해당 함수가 호출된다.
 * @example <button class="btn btn-default" type="button"
 *          onclick="authReqFunction();">로그인</button>
 */
function authReqFunction() {
	createXMLHttpAuthReq();
	AuthRequest.open('POST', './auth/TEST');
	AuthRequest.setRequestHeader('Content-Type', 'application/json');
	AuthRequest.send(getAuthReqParam());
	AuthRequest.onreadystatechange = authRespProcess;
}

/**
 * @method authRespProcess
 * @param {undefined}
 * @returns {undefined}
 * @description 사용자 인증 요청에 대한 응답 처리를 위한 메소드로, 서버로부터 응답이 도착하면 요청을 전송한
 *              XMLHttpRequest 객체로부터 호출된다. 이때, 사용되는 Property는 XMLHttpRequest 객체의
 *              onreadystatechange 이다.
 * @example AuthRequest.onreadystatechange = authRespProcess;
 */
function authRespProcess() {
	if (AuthRequest.readyState == 4 && AuthRequest.status == 200) {
		console.log("[로그인 성공] \n" + "Detail : " + AuthRequest.responseText);
		LoginDisplay(AuthRequest.responseText);
	}
}

/**
 * @file 서비스 등록 단계에서 선택한 인텐트의 어휘사전 정보를 불러오기 위한 어휘사전 정보 요청 API.
 * @module ServerRequest/DictionaryProc
 */
/**
 * @method createXMLHttpDctnrReq
 * @param {undefined}
 * @returns {undefined}
 * @description Ajax 방식의 어휘사전 정보 요청을 위한 XMLHttpRequest 객체를 생성하는 메소드로, 요청 전송 전에
 *              호출된다.
 * @example if (window.ActiveXObject) { DictionaryRequest = new
 *          ActiveXObject("Microsoft.XMLHTTP"); } else if
 *          (window.XMLHttpRequest) { DictionaryRequest = new XMLHttpRequest(); }
 */
function createXMLHttpDctnrReq() {
	if (window.ActiveXObject) {
		DictionaryRequest = new ActiveXObject("Microsoft.XMLHTTP");
	}
	else if (window.XMLHttpRequest) {
		DictionaryRequest = new XMLHttpRequest();
	}
}

/**
 * @method dctnrReqFunction
 * @param {undefined}
 * @returns {undefined}
 * @description 인텐트 별 어휘사전 정보를 요청하기 위한 메소드로, 요청 파라미터를 설정하는 부분과 서버로부터 응답이 도착했을 때
 *              특정한 자바스크립트 함수를 호출하는 부분을 포함하고 있다. 사용자가 인텐트 항목을 선택했을 때, 해당 함수가
 *              호출된다.
 * @example
 *              <tr class='fixrow'>
 *              <th>인텐트 명</th>
 *              <td> <select class="form-control" name="intentName"
 *              id="intentName" onchange="dctnrReqFunction()"> <option value=""
 *              selected disabled>인텐트 명 선택</option> <option
 *              value="ViewINFRM">ViewINFRM</option> <option
 *              value="Concierge">Concierge</option> <option
 *              value="Amenity">Amenity</option> <option value="Order">Order</option>
 *              <option value="CheckOut">CheckOut</option> </select></td>
 *              </tr>
 */
function dctnrReqFunction() {
	createXMLHttpDctnrReq();
	DictionaryRequest.open('POST', './getDictionary');
	DictionaryRequest.setRequestHeader('Content-Type', 'application/json');
	DictionaryRequest.send(getIntentSelectedParam());
	DictionaryRequest.onreadystatechange = dctnrRespProcess;
}

/**
 * @method dctnrRespProcess
 * @param {undefined}
 * @returns {undefined}
 * @description 인텐트 별 어휘사전 정보 요청에 대한 응답 처리를 위한 메소드로, 서버로부터 응답이 도착하면 요청을 전송한
 *              XMLHttpRequest 객체로부터 호출된다. 이때, 사용되는 Property는 XMLHttpRequest 객체의
 *              onreadystatechange 이다.
 * @example DictionaryRequest.onreadystatechange = dctnrRespProcess;
 */
function dctnrRespProcess() {
	if (DictionaryRequest.readyState == 4 && DictionaryRequest.status == 200) {
		console.log("[어휘사전 호출] \n" + "Detail : " + DictionaryRequest.responseText);
		$('#mainTable > tbody > tr').not('.fixrow').remove();
		$('#mainTable').find('tr:eq(5)').after(DictionaryRequest.responseText);
	}
}
/**
 * @file 서비스 등록 단계에서 입력한 서비스 정보를 등록하기 위한 서비스 등록 요청 API.
 * @module ServerRequest/SvcRegProc
 */

/**
 * @method createXMLHttpSVCRegReq
 * @param {undefined}
 * @returns {undefined}
 * @description Ajax 방식의 서비스 등록 요청을 위한 XMLHttpRequest 객체를 생성하는 메소드로, 요청 전송 전에
 *              호출된다.
 * @example if (window.ActiveXObject) { serviceRegRequest = new
 *          ActiveXObject("Microsoft.XMLHTTP"); } else if
 *          (window.XMLHttpRequest) { serviceRegRequest = new XMLHttpRequest(); }
 */
function createXMLHttpSVCRegReq() {
	if (window.ActiveXObject) {
		serviceRegRequest = new ActiveXObject("Microsoft.XMLHTTP");
	}
	else if (window.XMLHttpRequest) {
		serviceRegRequest = new XMLHttpRequest();
	}
}

/**
 * @method svcRegReqFunction
 * @param {undefined}
 * @returns {undefined}
 * @description 서비스 등록 요청 전송을 위한 메소드로, 요청 파라미터를 설정하는 부분과 서버로부터 응답이 도착했을 때 특정한
 *              자바스크립트 함수를 호출하는 부분을 포함하고 있다. 사용자가 등록 버튼을 클릭했을 때, 해당 함수가 호출된다.
 * @example <input class="btn btn-warning" type="button"
 *          onClick="svcRegReqFunction();" value="등록" />
 */
function svcRegReqFunction() {
	createXMLHttpSVCRegReq();
	serviceRegRequest.open('POST', './domainRegistration');
	serviceRegRequest.setRequestHeader('Content-Type', 'application/json');
	serviceRegRequest.send(getServiceRegReqParam());
	serviceRegRequest.onreadystatechange = svcRegRespProcess;
}

/**
 * @method svcRegRespProcess
 * @param {undefined}
 * @returns {undefined}
 * @description 서비스 등록 요청에 대한 응답 처리를 위한 메소드로, 서버로부터 응답이 도착하면 요청을 전송한
 *              XMLHttpRequest 객체로부터 호출된다. 이때, 사용되는 Property는 XMLHttpRequest 객체의
 *              onreadystatechange 이다.
 * @example serviceRegRequest.onreadystatechange = svcRegRespProcess;
 */
function svcRegRespProcess() {
	if (serviceRegRequest.readyState == 4 && serviceRegRequest.status == 200) {
		alert(serviceRegRequest.responseText);
		console.log("[등록 성공] \n" + "Detail : " + serviceRegRequest.responseText);
	}
}

/**
 * @file 서비스 등록 단계에서 업로드한 인텐트 및 어휘 정보를 등록하기 위한 요청 API.
 * @module ServerRequest/IntentRegProc
 */

/**
 * @method createXMLHttpIntentRegReq
 * @param {undefined}
 * @returns {undefined}
 * @description Ajax 방식의 서비스 등록 요청을 위한 XMLHttpRequest 객체를 생성하는 메소드로, 요청 전송 전에
 *              호출된다.
 * @example if (window.ActiveXObject) { intentRegRequest = new
 *          ActiveXObject("Microsoft.XMLHTTP"); } else if
 *          (window.XMLHttpRequest) { intentRegRequest = new XMLHttpRequest(); }
 */
function createXMLHttpIntentRegReq() {
	if (window.ActiveXObject) {
		intentRegRequest = new ActiveXObject("Microsoft.XMLHTTP");
	}
	else if (window.XMLHttpRequest) {
		intentRegRequest = new XMLHttpRequest();
	}
}

/**
 * @method intentRegReqFunction
 * @param {undefined}
 * @returns {undefined}
 * @description 인텐트 및 어휘 등록 요청 전송을 위한 메소드로, 업로드한 인텐트 및 어휘 정보를 전달한다.
 * @example <input class="btn btn-warning" type="button"
 *          onClick="intentRegReqFunction();" value="등록" />
 */
function intentRegReqFunction(json) {
	createXMLHttpIntentRegReq();
	intentRegRequest.open('POST', '/NICEKIT/setDictionary');
	intentRegRequest.setRequestHeader('Content-Type', 'application/json');
	intentRegRequest.send(json);
	console.log(json);
	intentRegRequest.onreadystatechange = intentRegRespProcess;
}

/**
 * @method intentRegRespProcess
 * @param {undefined}
 * @returns {undefined}
 * @description 서비스 등록 요청에 대한 응답 처리를 위한 메소드로, 서버로부터 응답이 도착하면 요청을 전송한
 *              XMLHttpRequest 객체로부터 호출된다. 이때, 사용되는 Property는 XMLHttpRequest 객체의
 *              onreadystatechange 이다.
 * @example intentRegRequest.onreadystatechange = intentRegRespProcess;
 */
function intentRegRespProcess() {
	if (intentRegRequest.readyState == 4 && intentRegRequest.status == 200) {
		alert(intentRegRequest.responseText);
		console.log("[등록 성공] \n" + "Detail : " + intentRegRequest.responseText);
	}
}

/**
 * @file 서비스 실행 단계에서 서비스 요청 API.
 * @module ServerRequest/SvcProc
 */

/**
 * @method createXMLHttpSvcReq
 * @param {undefined}
 * @returns {undefined}
 * @description Ajax 방식의 서비스 요청을 위한 XMLHttpRequest 객체를 생성하는 메소드로, 요청 전송 전에 호출된다.
 * @example if (window.ActiveXObject) { SvcRequest = new
 *          ActiveXObject("Microsoft.XMLHTTP"); } else if
 *          (window.XMLHttpRequest) { SvcRequest = new XMLHttpRequest(); }
 */
function createXMLHttpSvcReq() {
	if (window.ActiveXObject) {
		SvcRequest = new ActiveXObject("Microsoft.XMLHTTP");
	}
	else if (window.XMLHttpRequest) {
		SvcRequest = new XMLHttpRequest();
	}
}

/**
 * @method svcReqFunction
 * @param {String} appId - 해당 앱에 대한 G-Box 의 ContainerId
 * @param {String} intent - 발화 Intent
 * @param {String} parameter - 대화서버에서 전달하는 parameter JSON 값
 * @returns {undefined}
 * @description 사용자 요청 전송을 위한 메소드로, 요청 파라미터를 설정하는 부분과 서버로부터 응답이 도착했을 때 특정한
 *              자바스크립트 함수를 호출하는 부분을 포함하고 있다. 사용자의 발화명령 및 서비스 버튼을 클릭했을 때, 해당 함수가
 *              호출된다.
 * @example function svcReqFunction(appId, intent, parameter) {
 *          createXMLHttpSvcReq(); SvcRequest.open('POST', './svc/TEST');
 *          SvcRequest.setRequestHeader('Content-Type', 'application/json'); var
 *          SvcInfo = new Object(); SvcInfo.id = appId; SvcInfo.intent = intent;
 *          SvcInfo.param = parameter; var SvcJsonInfo =
 *          JSON.stringify(SvcInfo); SvcRequest.send(getSvcReqParam());
 *          SvcRequest.onreadystatechange = svcRespProcess; }
 */
function svcReqFunction(appId, intent, parameter) {
	createXMLHttpSvcReq();
	SvcRequest.open('GET', '/NICEKIT/reqService' + "?" + "intentName" + "=" + intent + "&" + "word"
				+ "=" + parameter + "&" + "name" + "=" + appId);
	SvcRequest.setRequestHeader('Content-Type', 'application/json');
	SvcRequest.send(null);
	SvcRequest.onreadystatechange = svcRespProcess;
}

/**
 * @method svcRespProcess
 * @param {undefined}
 * @returns {undefined}
 * @description 서비스 요청에 대한 응답 처리를 위한 메소드로, 서버로부터 응답이 도착하면 요청을 전송한 XMLHttpRequest
 *              객체로부터 호출된다. 이때, 사용되는 Property는 XMLHttpRequest 객체의
 *              onreadystatechange 이다.
 * @example SvcRequest.onreadystatechange = svcRespProcess;
 */
function svcRespProcess() {
	if (SvcRequest.readyState == 4 && SvcRequest.status == 200) {
		alert("[DEBUG] 응답 메시지 : " + SvcRequest.responseText);
		var svcObj = JSON.parse(SvcRequest.responseText);
		var resCode = svcObj.obj['resCode'];
		var resMsg = svcObj.obj['resMsg'];
		var resUrl = svcObj.obj['resUrl'];
		
		var text = resMsg[0];
		
		//200일경우 tts
		if( resCode == '200'){
			sendTTS(text['eventplace'], resCode, resUrl);
		}
		else if( resCode == '201'){
			
			var hostName = location.hostname;
			var pathName = location.pathname;
			var vendorNameSplit = pathName.split("/");
			var vendorName = decodeURI(vendorNameSplit[vendorNameSplit.length-2]);
			
			var newUrl = window.location.protocol + "//" + window.location.host + "/docbase/vendors/" + vendorName + "/"+ resUrl;
			
			alert(newUrl);
			
			window.location.href = newUrl;
		}
		
		
	}
}

function setReqDataforXls() {
	var reqData = new Object();
	var svcList = new Array();

	reqData["domainId"] = "RSRT001";
	reqData["domainName"] = "resort";
	reqData["specName"] = "오크밸리";

	$('#tableServiceBody').each(function() {

		var tr = $(this).children();
		tr.each(function() {
			var svcObj = new Object();
			var td = $(this).children();
			svcObj["serviceName"] = td.eq(1).text();
			svcObj["invokeType"] = td.eq(2).text();
			svcObj["serviceType"] = td.eq(3).text();
			svcObj["serviceLink"] = "resources/template/serviceName.html";
			svcObj["serviceDesc"] = td.eq(4).text();
			svcObj["serviceCode"] = td.eq(5).text();

			var wordList = new Array();
			var wordSplit = td.eq(8).text().split(',');
			for ( var i in wordSplit)
				wordList.push(wordSplit[i]);

			var dicObj = new Object();
			dicObj["dicName"] = td.eq(7).text();
			dicObj["wordList"] = wordList;
			var dicList = new Array();
			dicList.push(dicObj);
			var intentObj = new Object();
			intentObj["id"] = td.eq(6).text();
			intentObj["dicList"] = dicList;

			var intentInfo = new Array();
			intentInfo.push(intentObj);
			svcObj["intentInfo"] = intentInfo;
			svcList.push(svcObj);
		});
	});
	reqData["svcList"] = svcList;
	console.log(JSON.stringify(reqData));
	return JSON.stringify(reqData);
}
