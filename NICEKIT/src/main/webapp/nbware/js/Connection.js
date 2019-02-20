"use strict";

var xhr = null;
var Connection =  new function(){

	// ajax 환경 설정
	this.method = "POST",
	this.dataType = "JSON",
	this.async = true,
	this.networkTimeout = 5000,
	this.crossDomain = true,
	this.cache = false,
	this.contentType = "application/json;charset=UTF-8",
	this.userAgent = null;
	this.isEnter = false;	// 요청 중..
	this.isJob = false;	// 작업 중..
	
	this.send = function(params) {
		Utils.log(DEBUG,"Connection.send call");

		if (!Utils.isEmpty(xhr)) {
			xhr.abort();
			xhr = null;
		}

		var send = api["domain"] + api["url"];
		var dataParams = {};
		dataParams["testUrl"] = params["testUrl"];
		dataParams["comUrl"] = params["comUrl"];
		dataParams["method"] = params["method"];
		dataParams["headerInfo"] = params["headerInfo"];
		dataParams["authInfo"] = params["authInfo"];
		dataParams["dataType"] = params["dataType"];
		dataParams["reqStructure"] = params["reqStructure"];
		dataParams["reqSpec"] = params["reqSpec"];
		dataParams["resStructure"] = params["resStructure"];
		dataParams["resSpec"] = params["resSpec"];

		Utils.log(DEBUG,"Connection.dataParams", dataParams);

		xhr = jQuery.ajax({
			  url: send
			, data: JSON.stringify(dataParams)
			, type: Connection.method
			, dataType: Connection.dataType
			, contentType : Connection.contentType
			, headers: {
				"Content-Type": "application/json"
			}
			, async: Connection.async
			, timeout: Connection.networkTimeout
			, crossDomain: Connection.crossDomain
			, cache: Connection.cache
			, beforeSend: function(xhr) {
				// 
			}
			, success: function(rst) {
				Utils.log(DEBUG,"Connection.send.rst", rst);
				Utils.log(DEBUG,"Connection.send.rst", JSON.stringify(rst));

				alert("전송 요청을 완료하였습니다.");
				return;
			}
			,error: function(xhr, excp){
				Utils.log(ERR, "xhr.status : "+xhr.status+", msg : "+xhr.responseText+", excp.message : "+excp);
				
				// 접속 장애
				alert("전송 요청 중 오류가 발생하였습니다.");
				return;
			}
		});

	};
};