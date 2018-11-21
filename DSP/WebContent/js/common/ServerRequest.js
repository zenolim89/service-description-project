/**
 * Created by zenolim on 2018
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

// Ajax 사용자 인증 요청 초기화
function createXMLHttpAuthReq() {
	if (window.ActiveXObject) {
		AuthRequest = new ActiveXObject("Microsoft.XMLHTTP");
	} else if (window.XMLHttpRequest) {
		AuthRequest = new XMLHttpRequest();
	}
}

// Ajax 사용자 인증 요청 처리(Json)
function authReqFunction() {
	createXMLHttpAuthReq();
	AuthRequest.open('POST', './auth');
	AuthRequest.setRequestHeader('Content-Type', 'application/json'); // 컨텐츠타입
	AuthRequest.send(getAuthReqParam());
	AuthRequest.onreadystatechange = authRespProcess;
}

// Ajax 사용자 인증 응답 처리(Json)
function authRespProcess() {
	if (AuthRequest.readyState == 4 && AuthRequest.status == 200) {
		console.log("[로그인 성공] \n" + "Detail : " + AuthRequest.responseText);
		LoginDisplay(AuthRequest.responseText);
	}
}

// Ajax 어휘사전 요청 초기화
function createXMLHttpDctnrReq() {
	if (window.ActiveXObject) {
		DictionaryRequest = new ActiveXObject("Microsoft.XMLHTTP");
	} else if (window.XMLHttpRequest) {
		DictionaryRequest = new XMLHttpRequest();
	}
}

// Ajax 어휘사전 요청 처리(Json)
function dctnrReqFunction() {
	createXMLHttpDctnrReq();
	DictionaryRequest.open('POST', './dictionary');
	DictionaryRequest.setRequestHeader('Content-Type', 'application/json'); // 컨텐츠타입
	DictionaryRequest.send(getIntentSelectedParam());
	DictionaryRequest.onreadystatechange = dctnrRespProcess;
}

// Ajax 어휘사전 응답 처리(Json)
function dctnrRespProcess() {
	if (DictionaryRequest.readyState == 4 && DictionaryRequest.status == 200) {
		console.log("[어휘사전 호출] \n" + "Detail : "
				+ DictionaryRequest.responseText);
		$('#mainTable > tbody > tr').not('.fixrow')
				.remove();
		$('#mainTable').find('tr:eq(4)').after(DictionaryRequest.responseText);
	}
}

// Ajax 서비스등록 요청 초기화
function createXMLHttpSVCRegReq() {
	if (window.ActiveXObject) {
		serviceRegRequest = new ActiveXObject("Microsoft.XMLHTTP");
	} else if (window.XMLHttpRequest) {
		serviceRegRequest = new XMLHttpRequest();
	}
}

// Ajax 서비스등록 요청 처리(Json)
function svcRegReqFunction() {
	createXMLHttpSVCRegReq();
	serviceRegRequest.open('POST', './registration');
	serviceRegRequest.setRequestHeader('Content-Type', 'application/json'); // 컨텐츠타입
	serviceRegRequest.send(getServiceRegReqParam());
	serviceRegRequest.onreadystatechange = svcRegRespProcess;
}

// Ajax 서비스등록 응답 처리(Json)
function svcRegRespProcess() {
	if (serviceRegRequest.readyState == 4 && serviceRegRequest.status == 200) {
		alert(serviceRegRequest.responseText);
		console
				.log("[등록 성공] \n" + "Detail : "
						+ serviceRegRequest.responseText);
	}
}
