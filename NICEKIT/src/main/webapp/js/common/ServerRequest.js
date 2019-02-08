/**
 * @fileOverview ServerRequest
 * @author zenolim <zenolim89@gmail.com>
 * @version 1.0.0
 */

$.getScript('./js/common/RequestParam.js', function() {
	console.log('RequestParam.js loading...');
});

$.getScript('./js/common/ChangeView.js', function() {
	console.log('ChangeView.js loading...');
});

var AuthRequest;
var DictionaryRequest;
var serviceRegRequest;
var intentRegRequest;
var SvcRequest;

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
	DictionaryRequest.open('POST', './dictionary');
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
		$('#mainTable').find('tr:eq(4)').after(DictionaryRequest.responseText);
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
	serviceRegRequest.open('POST', './registration');
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
 *          onClick="svcRegReqFunction();" value="등록" />
 */
function intentRegReqFunction() {
	createXMLHttpSVCRegReq();
	intentRegRequest.open('POST', './registration');
	intentRegRequest.setRequestHeader('Content-Type', 'application/json');
	intentRegRequest.send(getServiceRegReqParam());
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
	SvcRequest.open('POST', './svc/TEST');
	SvcRequest.setRequestHeader('Content-Type', 'application/json');
	var SvcInfo = new Object();
	SvcInfo.id = appId;
	SvcInfo.intent = intent;
	SvcInfo.param = parameter;
	var SvcJsonInfo = JSON.stringify(SvcInfo);
	SvcRequest.send(getSvcReqParam());
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
		console.log("[서비스 요청] \n" + "Detail : " + SvcRequest.responseText);
		LoginDisplay(SvcRequest.responseText);
	}
}
