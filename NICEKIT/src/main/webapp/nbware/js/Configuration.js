"use strict";

var printLog = true; // 로그 출력
var DEBUG = "DEBUG";
var ERR = "ERROR";

// api info
var api = {
		"domain" : "http://kt.com",
		"url" : "/setSvcDesc"
};

// config
var config = {
		"method" : null,
		"type" : null
};

// json editor
var monacoEditor = {
		"get" : {
				"request" : null,
				"response" : null
		},
		"post" : {
				"request" : null,
				"response" : null
		}
};

// json 임시 저장용
var saveData = {
		"GET" : {
				"request" : {
						"isData" : false,
						"originData" : null,
						"specData" : null
				},
				"response" : {
						"isData" : false,
						"originData" : null,
						"specData" : null
				}
		},
		"POST" : {
				"request" : {
						"isData" : false,
						"originData" : null,
						"specData" : null
				},
				"response" : {
						"isData" : false,
						"originData" : null,
						"specData" : null
				}
		}
};

// api 연동용
var parameters = {
		"testUrl" : null,
		"comUrl" : null,
		"method" : null,
		"headerInfo" : {
				"GET" : {
						"request" : null,
						"response" : null
				},
				"POST" : {
						"request" : null,
						"response" : null
				}
		},
		"authInfo" : {
				"GET" : {
						"request" : null,
						"response" : null
				},
				"POST" : {
						"request" : null,
						"response" : null
				}
		},
		"dataType" : null,
		"reqStructure" : {
				"GET" : {
					"request" : null
				},
				"POST" : {
					"request" : null
				}
		},
		"reqSpec" : {
				"GET" : {
					"request" : null
				},
				"POST" : {
					"request" : null
				}
		},
		"resStructure" : {
				"GET" : {
					"response" : null
				},
				"POST" : {
					"response" : null
				}
		},
		"resSpec" : {
				"GET" : {
					"response" : null
				},
				"POST" : {
					"response" : null
				}
		}
};

var Configuraton = new function() {
	// 
};