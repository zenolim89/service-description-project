$.getScript('http://svcapi.gigagenie.ai/sdk/v1.0/js/gigagenie.js', function() {
	console.log('gigagenie.js loading...');
});

$.getScript('/NICEKIT/nicekit/js/common/ServerRequest.js', function() {
	console.log('ServerRequst.js loading...');
});

/**
 * @fileOverview ServiceInterface
 * @author zenolim <zenolim89@gmail.com>
 * @version 1.0.0
 */

/**
 * @file
 * 
 * <pre>
 * GiGA Genie에 탑재된 음성, 통화, 데이터 관리 기능을 활용하기 위한 API. 모든 API는 callback 방식으로 호출 결과를 전달한다. 
 * GiGA Genie API는 다음과 같이 호출한다. 
 * gigagenie.[영역].[함수](함수파라미터, callback)으로 호출한다.
 * [영역] 은 API 를 Grouping 하는 단위로 이후 namespace 로 설명한다.
 * callback 함수는 다음과 같이 정의 된다. 
 * 	function callback(result_cd,result_msg,extra) 
 * 	result_cd: 결과 코드, 숫자 
 * 	result_msg: 결과 메시지, 문자 
 * 	extra : 호출 결과로 전달되는 데이터들(Javascript Object) 
 * 	각각의 API 에 따라서 전달되는 데이터가 다르다.
 * 	예: getSelectedIndex 의 경우 extra 에 selected 라는 숫자 값이 설정되어 전달된다.
 * </pre>
 * 
 * @module SvcInterface/GiGAGenieAPI
 */

/**
 * @method init
 * @param {String} [keytype="GBOXDEVM"] - 개발(GBOXDEVM) 또는 상용(GBOXCOMM) 키 종류 입력
 * @param {String} apikey - 개발자 포털에서 발급받은 키 입력
 * @returns {undefined}
 * @description GiGA Genie Service API를 초기화 한다. GiGA Genie Service API를 이용하기
 *              위해서는 API 초기화가 반드시 진행되어야 한다. API 사용전에 callback에서 result_cd 가
 *              200으로 리턴했는지 확인해야 한다.
 * 
 * <pre>
 * result_cd가 200인 경우 다음의 값이 전달된다.
 * 	extra.sdkversion : String, Mandatory로 이용가이드 버전이 전달된다.
 * 	extra.devicetype : String, Mandatory로 디바이스 유형이 전달된다.
 * 디바이스 유형은 다음과 같다. (sdkversion 1.4에서 지원)
 * 	extra.devicetype === ”GGENIE” : 기가지니
 * 	extra.devicetype === &quot;GGENIE2&quot; : 기가지니2
 * 	extra.devicetype === &quot;GGENIELTE&quot; : 기가지니LTE 
 * 	extra.displayflag : String, Mandatory로 해당 디바이스가 화면을 지원하는지 여부를 전달한다.
 * 	“Y” 의 경우 display 가 지원되며, “N”의 경우 display가 지원되지 않는다.
 * </pre>
 * 
 * @example function init(keytype, apikey) { options = {}; options.keytype =
 *          keytype; options.apikey = apikey; gigagenie.init(options,
 *          function(result_cd, result_msg, extra) { if (result_cd == 200) {
 *          console.log('Initialize Success'); alert(&quot;init 실행 완료&quot;);
 *          sendTTSAPI(&quot;음성인식 서비스를 실행합니다. &quot;); } }); }
 */

var options = {};
var keyType = "GBOXDEVM";
var apiKey = "VDUwMDI1ODZ8R0JPWERFVk18MTU0NTAxMzgxOTYwMw==";
function init() {

	var pathName = location.pathname;
	var vendorNameSplit = pathName.split("/");
	var vendorName = decodeURI(vendorNameSplit[vendorNameSplit.length - 2]);

	console.log('pathName : ' + pathName);
	console.log('vendorNameSplit : ' + vendorNameSplit);
	console.log('vendorName : ' + vendorName);

	var specId;

	$.ajax({
			url : 'http://222.107.124.9:8080/NICEKIT/getSpecId?vendorName=' + vendorName,
			type : 'GET',
			async : false,
			success : function(data) {

				console.log(data);

				specId = data['specId'];
			}
	});

	console.log('specId : ' + specId);

	options = {};
	options.keytype = keytype;
	options.apikey = apiKey;
	gigagenie.init(options, function(result_cd, result_msg, extra) {
		if (result_cd == 200) {
			console.log('Initialize Success');
			alert("[DEBUG] init 실행 완료");
			SpeechINTRC(specId);
		}
	});
}

/**
 * @method sendTTS
 * @param {String} ttstext - 사용자에게 음성으로 전달하고자 하는 Text를 입력.
 * @returns {undefined}
 * @description 입력 Text 를 사용자에게 음성으로 전달한다. stopTTS API에 의해서 중지된다.
 * 
 * <pre>
 * result_cd 에 다음의 값이 전달된다.
 * 	200: TTS 재생 성공
 * 	409: sendTTS로 TTS 재생중임
 * 	501: KWS(KeyWord Spotting)에 의해 정지됨
 * 	502: 재생 실패
 * 	503: 다른 App에서 TTS 중지 메시지를 보냄(stopTTS에 의한 종료)
 * 	504: mute 상태로 TTS 재생 불가
 * </pre>
 * 
 * @example function sendTTSAPI(ttstext) { var options = {}; alert("startTTS")
 *          options.ttstext = ttstext; gigagenie.voice.sendTTS(options,
 *          function(result_cd, result_msg, extra) { if (result_cd == 200) { }
 *          else { } alert(result_cd); }); }
 */
function sendTTS(ttstext, resCode, resUrl) {
	var options = {};
	options.ttstext = ttstext;
	gigagenie.voice.sendTTS(options, function(result_cd, result_msg, extra) {
		if (result_cd == 200 || result_cd == 409) {
			pageTrans(resCode, resUrl);
		}
		else {
		}
	});
}

/**
 * @method stopTTS
 * @param {undefined}
 * @returns {undefined}
 * @description TTS 를 중단한다. 음성인식도 중지된다.
 * 
 * <pre>
 * esult_cd에 다음의 값이 전달된다.
 * 	200 : 중단 성공
 * 	404 : TTS, 음성인식 실행중이 아님
 * 	500 : 실행 오류
 * </pre>
 * 
 * @example function stopTTS() { var options = {}; alert("stopTTS");
 *          gigagenie.voice.stopTTS(options, function(result_cd, result_msg,
 *          extra) { if (result_cd == 200) { } else { } }); }
 */
function stopTTS() {
	var options = {};
	gigagenie.voice.stopTTS(options, function(result_cd, result_msg, extra) {
		if (result_cd == 200) {
		}
		else {
			;
		}
	});
}

/**
 * @method startVoice
 * @param {String} [ttstext=null] - 음성인식 전 사용자에게 음성으로 전달하고자 하는 Text를 입력.
 * @returns {undefined}
 * @description 사용자에게 음성 안내 후 음성 인식을 한다. TTS 출력 중에 stopTTS를 수신하면 TTS와 음성인식 모두
 *              종료된다. 음성인식이 진행되면 해당 음성인식을 종료할 수 없다. (Timeout으로 처리)
 * 
 * <pre>
 * result_cd 에 다음의 값이 전달된다.
 * 	200: 음성인식 성공, extra.voicetext에 인식된 결과가 전달된다.
 * 	500: Timeout 발생 등으로 음성 인식 실패, extra.voicetext에는 “ASRsession timeout” 가 전달된다
 * 	501: KWS(KeyWord Spotting)에 의해 정지됨
 * 	502: 재생 실패
 * 	503: stopTTS에 의한 TTS 종료
 * 	504: mute 상태로 TTS 재생 불가
 * 	505: 처리 용량 초과(다국어 음성인식 중 서버 용량 초과시 발생)
 * extra 값에 다음이 설정되어 전달된다.
 * 	extra.voicetext : String, Mandatory 로 음성인식된 Text 가 전달된다.
 * 	extra.accuracy : Number, Optional로 인식된 결과의 정확도이다. (options.text가 설정되어 있고
 * 	options.voicelanguage가 1인 경우에만 설정되어 리턴됨)
 * </pre>
 * 
 * @example function startVoice(ttstext) { var options = {}; options.voicemsg =
 *          ttstext; gigagenie.voice.getVoiceText(options, function(result_cd,
 *          result_msg, extra) { if (result_cd === 200) { } }); }
 */
function startVoice(ttstext) {
	alert(ttstext);
	var options = {};
	options.voicemsg = ttstext;
	gigagenie.voice.getVoiceText(options, function(result_cd, result_msg, extra) {
		if (result_cd === 200) {
		}
	});
}

/**
 * @method getContainerId
 * @param {undefined}
 * @returns {String} appId
 * @description 해당 앱에 대한 G-Box 의 ContainerId 를 조회한다.
 * 
 * <pre>
 * result_cd 는 다음과 같이 정의된다.
 * 	200: 성공
 * 	500: 시스템 Error 
 * extra 값에 다음이 설정되어 전달된다.
 * 	extra.containerid: String, Mandatory 로 Container 의 ID 값이다.
 * </pre>
 * 
 * @example function getContainerId() { var options = {};
 *          gigagenie.appinfo.getContainerId(options, function(result_cd,
 *          result_msg, extra) { if (result_cd === 200) { console.log("The
 *          container id is " + extra.containerid); } else {
 *          console.log("getContainerId is fail."); } }); }
 */
function getContainerId() {
	var options = {};
	var appId;
	gigagenie.appinfo.getContainerId(options, function(result_cd, result_msg, extra) {
		if (result_cd === 200) {
			console.log("The container id is " + extra.containerid);
			appId = extra.containerid;
		}
		else {
			console.log("getContainerId is fail.");
			appId = null;
		}
	});
	return appId;
}
// 서비스 종료 명령 수신 API
// 음성명령(종료, 닫기 발화)과, 리모콘 나가기 버튼 클릭시 서비스 종료 이벤트 전달
gigagenie.voice.onRequestClose = function() {
	options = {};
	gigagenie.voice.svcFinished(options, function(result_cd, result_msg, extra) {
	});
};

/**
 * @file
 * 
 * <pre>
 * 서비스 실행 단계에서 음성대화, 터치, 리모컨 등의 사용자 인터랙션을 통한 이벤트 제어 API. 
 * </pre>
 * 
 * @module SvcInterface/SvcActionEvent
 */

/**
 * @method SpeechINTRC
 * @param {String} appId - 해당 앱에 대한 G-Box 의 ContainerId
 * @returns {undefined}
 * @description 등록된 Action 에 대한 수신 Event 이다. 등록된 Action 은 대화 SDK 상의 ActionCode
 *              (대화 SDK: Intent명)와 동일하며, 현재 실행중인 appId를 파라미터에 포함하여 서비스 요청 API를
 *              호출한다.
 * 
 * <pre>
 * gigagenie.voice.onActionEvent 콜백 구현
 * 콜백은 function callback(extra) 로 구현해야 하며 extra 에는 actioncode 와 actionpath 및 parameter 가 전달된다.
 * 	actioncode : 발화 Intent 와 동일
 * 	actionpath : 등록한 이동 Path
 * 	voiceid : 화자인식된 ContainerID 값(인식 안된 경우 “UNKNOWN”, 화자식별등록이 안되어 있으면 “NONE” 전달)
 * 	uword : 발화한 문구
 * 	parameter : 대화서버에서 전달하는 parameter JSON 값, extra.parameter['NE-어휘사전']으로 정보 추출, 각 사전 대표단어가 출력됨.
 * onActionEvent 콜백이 정의되지 않으면 해당 actionpath 로 자동 이동한다.
 * </pre>
 * 
 * @example function SpeechINTRC() { gigagenie.voice.onActionEvent =
 *          function(extra) { var word = extra.uword; alert(word); switch
 *          (extra.actioncode) { case 'HELLOGENIE':
 *          document.getElementById('test').innerText = "대표 어휘 : " +
 *          extra.parameter['NE-HELLO']; break; case 'GOODBYEGENIE':
 *          document.getElementById('test').innerText = "대표 어휘 : " +
 *          extra.parameter['NE-GOODBYE']; break; default: break; } } }
 */

function SpeechINTRC(appId) {
	gigagenie.voice.onActionEvent = function(extra) {
		var sentence = extra.uword;
		alert("[DEBUG] 인식문장 : " + sentence); // 발화 문장
		switch (extra.actioncode) {
			case 'HotelAmenityItem':
				svcReqFunction(appId, extra.actioncode, extra.parameter['NE-AMENITY']);
				alert("[DEBUG] 구문 해석 : " + JSON.stringify(extra.parameter));
				break;
			case 'HotelCheckout':
				svcReqFunction(appId, extra.actioncode, extra.parameter['NE-CHECKOUT']);
				alert("[DEBUG] 구문 해석 : " + JSON.stringify(extra.parameter));
				break;
			case 'HotelHelp':
				svcReqFunction(appId, extra.actioncode, extra.parameter['NE-QUESTIONS']);
				alert("[DEBUG] 구문 해석 : " + JSON.stringify(extra.parameter));
				break;
			case 'HotelViewPage':
				var parameter;
				if (extra.parameter.hasOwnProperty('NE-SERVICEMENU'))
					parameter = extra.parameter['NE-SERVICEMENU'];
				else if (extra.parameter.hasOwnProperty('NE-FACILITIES'))
					parameter = extra.parameter['NE-FACILITIES'];
				else if (extra.parameter.hasOwnProperty('NE-RESTAURANT'))
					parameter = extra.parameter['NE-RESTAURANT'];
				else if (extra.parameter.hasOwnProperty('NE-LEISURE'))
					parameter = extra.parameter['NE-LEISURE'];
				else if (extra.parameter.hasOwnProperty('NE-SHOPPING'))
					parameter = extra.parameter['NE-SHOPPING'];
				else if (extra.parameter.hasOwnProperty('NE-CULTURAL'))
					parameter = extra.parameter['NE-CULTURAL'];
				else if (extra.parameter.hasOwnProperty('NE-MEDICAL'))
					parameter = extra.parameter['NE-MEDICAL'];
				else if (extra.parameter.hasOwnProperty('NE-RELIGION'))
					parameter = extra.parameter['NE-RELIGION'];
				else if (extra.parameter.hasOwnProperty('NE-PARTNERSHIP'))
					parameter = extra.parameter['NE-PARTNERSHIP'];
				svcReqFunction(appId, extra.actioncode, parameter);
				alert("[DEBUG] 구문 해석 : " + JSON.stringify(extra.parameter));
				break;
			case 'HotelWebCam':
				svcReqFunction(appId, extra.actioncode, extra.parameter['NE-WEBCAM']);
				alert("[DEBUG] 구문 해석 : " + JSON.stringify(extra.parameter));
				break;
			case 'HotelTourInfo':
				var parameter;
				if (extra.parameter.hasOwnProperty('NE-PERIPHERAL'))
					parameter = extra.parameter['NE-PERIPHERAL'];
				else if (extra.parameter.hasOwnProperty('NE-TOURSPOT'))
					parameter = extra.parameter['NE-TOURSPOT'];
				svcReqFunction(appId, extra.actioncode, extra.parameter);
				alert("[DEBUG] 구문 해석 : " + JSON.stringify(extra.parameter));
				break;
			default:
				sendTTS("전송실패");
				break;
		}
	}
}

/**
 * @method BtnClickINTRC
 * @param {String} appId - 해당 앱에 대한 G-Box 의 ContainerId
 * @param {String} intent - 발화 Intent
 * @param {String} parameter - 대화서버에서 전달하는 parameter JSON 값
 * @returns {undefined}
 * @description 등록된 Action 에 대한 수신 Event 이다. 등록된 Action 은 대화 SDK 상의 ActionCode
 *              (대화 SDK: Intent명)와 동일하며, 현재 실행중인 appId를 파라미터에 포함하여 서비스 요청 API를
 *              호출한다.
 * 
 * <pre>
 * 해당 버튼의 onclick 이벤트 구현
 * &lt;input class=&quot;btn btn-warning&quot; type=&quot;button&quot; onClick=&quot;svcReqFunction('appId','intent','parameter');&quot; value=&quot;서비스명&quot; /&gt;
 * </pre>
 * 
 * @example function BtnClickINTRC(appId, intent, param) { var extra = {};
 *          extra.appid = appid; extra.actioncode = intent; extra.parameter =
 *          param; svcReqFunction(extra.appid, extra.actioncode,
 *          extra.parameter); }
 */

function BtnClickINTRC(appId, intent, param) {
	var extra = {};
	extra.appid = appid;
	extra.actioncode = intent;
	extra.parameter = param;
	svcReqFunction(extra.appid, extra.actioncode, extra.parameter);
}
