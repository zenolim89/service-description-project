"use strict";

var excelJson = {
	"request": {
		"method": null,
		"comUrl": null,
		"testUrl": null,
		"headers": [],
		"bodys": []
	},
	"response": {
		"method": null,
		"comUrl": null,
		"testUrl": null,
		"headers": [],
		"bodys": []
	}
}

var ExcelParser =  new function(){
	
	/**
	 * 엑셀 파서
	 * convert format (excel --> json array)
	 */
	this.readFile = function(e) {
		Utils.log(DEBUG, "ExcelParser.readFile call");
		
		// reset excel jsonData
		ExcelParser.resetJsonData();
		
		var data = e.target.result;
		Utils.log(DEBUG, "ExcelParser.data", data);
		
		var workbook;
		var arr = ExcelParser.setArrData(data);
		workbook = XLSX.read(btoa(arr), {type: 'base64'});
		workbook.SheetNames.forEach(function(item, index, array) {
			try {
				var uploadType = array[index];
				if (uploadType == "request" || uploadType == "response") {
					var json = XLSX.utils.sheet_to_json(workbook.Sheets[item]);
					
					var headers = [];
					var bodys = [];
					var jsonMap = {};
					
					jsonMap["header"] = {};
					jsonMap["body"] = {};
					for(var i = 0; i < json.length; i++) {
						if (i == 0) {
							jsonMap["requestType"] = uploadType;
							if (!Utils.isEmptyParams(json[i]["method"])) {
								excelJson[uploadType]["method"] = json[i]["method"].toUpperCase();
							}
							else {
								excelJson[uploadType]["method"] = null;
							}
							
							if (!Utils.isEmptyParams(json[i]["com_url"])) {
								excelJson[uploadType]["comUrl"] = json[i]["com_url"];
							}
							else {
								excelJson[uploadType]["comUrl"] = null;
							}
							
							if (!Utils.isEmptyParams(json[i]["test_url"])) {
								excelJson[uploadType]["testUrl"] = json[i]["test_url"];
							}
							else {
								excelJson[uploadType]["testUrl"] = null;
							}
						}
						
						// 헤더 체크
						if (!Utils.isEmptyParams(json[i]["header"])) {
							headers.push(json[i]["header"])
						}
						
						// 바디 체크
						if (!Utils.isEmptyParams(json[i]["body"])) {
							bodys.push(json[i]["body"])
						}
					}
					excelJson[uploadType]["headers"] = headers;
					excelJson[uploadType]["bodys"] = bodys;
				}
			}
			catch (err) {
				Utils.log(ERR, "ExcelParser.readFile.err", err);
				alert("엑셀 업로드 처리 중 오류가 발생하였습니다.");
				return;
			}
		});
		ExcelParser.validationExcelData(excelJson);
	};
	
	/**
	 * convert format (excel --> array)
	 */
	this.setArrData = function(data) {
		var o = "", l = 0, w = 10240;
		for(; l<data.byteLength/w; ++l) o+=String.fromCharCode.apply(null,new Uint8Array(data.slice(l*w,l*w+w)));
		o+=String.fromCharCode.apply(null, new Uint8Array(data.slice(l*w)));
		return o;
	};
	
	/**
	 * reset excelJson
	 */
	this.resetJsonData = function() {
		Utils.log(DEBUG, "ExcelParser.resetJsonData call");
		
		excelJson["request"]["method"] = null;
		excelJson["request"]["comUrl"] = null;
		excelJson["request"]["testUrl"] = null;
		excelJson["request"]["headers"] = [];
		excelJson["request"]["bodys"] = [];
		
		excelJson["response"]["method"] = null;
		excelJson["response"]["comUrl"] = null;
		excelJson["response"]["testUrl"] = null;
		excelJson["response"]["headers"] = [];
		excelJson["response"]["bodys"] = [];
	};
	
	/**
	 * validation jsonData
	 */
	this.validationExcelData = function(excelJson) {
		Utils.log(DEBUG, "ExcelParser.validationExcelData call");
		
		try {
			var method = excelJson["request"]["method"];
			var comUrl = excelJson["request"]["comUrl"];
			var testUrl = excelJson["request"]["testUrl"];
			var reqHeaders = excelJson["request"]["headers"];
			var reqBodys = excelJson["request"]["bodys"];
			var resBodys = excelJson["response"]["bodys"];
			
			// validation method
			if (!Utils.isEmpty(method)) {
				if (method != "POST" && method != "GET") {
					alert("method는 GET 또는 POST만 입력해 주세요.");
		            return;
				}
			}
			else {
				alert("method를 입력해 주세요.");
	            return;
			}
			
			// validation comUrl
			if (!Utils.isEmpty(comUrl)) {
	            var regExp = /^http(s)?:\/\/(www\.)?[a-z0-9]+([\-\.]{1}[a-z0-9]+)*\.[a-z]{2,5}(:[0-9]{1,5})?(\/.*)?$/;
	            if (!regExp.test(comUrl)) {
	                alert("com url 형식을 확인해 주세요.");
	                return;
	            }
	        }
	        else {
	            alert("com url을 입력해 주세요.");
	            return;
	        }
	        
	        // validation testUrl
	        if (!Utils.isEmpty(testUrl)) {
	            var regExp = /^http(s)?:\/\/(www\.)?[a-z0-9]+([\-\.]{1}[a-z0-9]+)*\.[a-z]{2,5}(:[0-9]{1,5})?(\/.*)?$/;
	            if (!regExp.test(testUrl)) {
	                alert("test url 형식을 확인해 주세요.");
	                return;
	            }
	        }
	        
	        // validation header
	        if (!Utils.isEmpty(reqHeaders) && reqHeaders.length > 0) {
	        	for(var t = 0; t < reqHeaders.length; t++) {
	        		var headerStr = reqHeaders[t].split(",");
		        	if (headerStr.length < 2 || (Utils.isEmpty(headerStr[0]) || Utils.isEmpty(headerStr[1]))) {
		        		alert("request header " + Number(t + 1) + "번째 행이 key, value 형식으로 입력되어 확인해 주세요.");
			            return;
		        	}
	        	}
	        }
	        else {
	        	alert("request header를 입력해 주세요.");
	            return;
	        }
	        
	        // validation request body
	        if (!Utils.isEmpty(reqBodys) && reqBodys.length > 0) {
	        	if (method == "GET") {
	        		// GET, request body
	        		for(var t = 0; t < reqBodys.length; t++) {
	        			var bodyStr = reqBodys[t].split(",");
			        	if (bodyStr.length < 3 || (Utils.isEmpty(bodyStr[0]) || Utils.isEmpty(bodyStr[1]) || Utils.isEmpty(bodyStr[0]))) {
			        		alert("request body " + Number(t + 1) + "번째 행이  key, value, spec 형식으로 입력되어 확인해 주세요.");
				            return;
			        	}
	        		}
	        	}
	        	else {
	        		if (reqBodys.length > 1) {
	        			alert("method POST 방식에서는 request body 필드의 복수 입력을 허용하지 않습니다.");
	        			return;
	        		}
	        		
	        		// POST, request body
	        		// check json format
	        		var isJson = ExcelParser.jsonValidation(reqBodys[0]);
	        		if (isJson) {
	        			// 
	        		}
	        		else {
	        			alert("request body의 json format을 확인해 주세요.");
	    	            return;
	        		}
	        	}
        	}
        	else {
        		alert("request body를 입력해 주세요.");
	            return;
        	}
	        
	        // validation response body
	        if (!Utils.isEmpty(resBodys) && resBodys.length > 0) {
        		if (resBodys.length > 1) {
        			alert(" response body 필드의 복수 입력을 허용하지 않습니다.");
        			return;
        		}
        		
        		// POST, resBodys body
        		// check json format
        		var isJson = ExcelParser.jsonValidation(resBodys[0]);
        		if (isJson) {
        			// 
        		}
        		else {
        			alert("response body의 json format을 확인해 주세요.");
    	            return;
        		}
        	}
        	else {
        		alert("response body를 입력해 주세요.");
	            return;
        	}
	        
	        // validation success
	        ExcelParser.setExcelDomElement(excelJson);
		}
		catch (err) {
			Utils.log(ERR, "ExcelParser.validationExcelData.err", err);
			alert("엑셀 입력 처리 중 오류가 발생하였습니다.");
			return;
		}
		finally {
			// reset file input field 
			$("input[name=uploadExcel]").val("");
		}
	};
	
	/**
	 * validation json format
	 */
	this.jsonValidation = function(jsonStr) {
		Utils.log(DEBUG, "ExcelParser.jsonValidation.call");
		
		var isCheck = false;
		try {
			if (!Utils.isEmpty(jsonStr)) {
				var obj = JSON.parse(jsonStr);
				if (obj.constructor === Array) {
					var len = obj.length;
					if (len > 0) {
						var keyLen = Object.keys(obj).length;
						isCheck = true;
					}
				}
				else if (obj.constructor === Object) {
					var len = Object.keys(obj).length;
					if (len > 0) {
						var keyLen = Object.keys(obj).length;
						isCheck = true;
					}
				}
			}
			
			return isCheck;
		}
		catch (err) {
			Utils.log(ERR, "ExcelParser.readFile.err", err);
			// alert("json 유효성 확인 중 오류가 발생하였습니다.");
			return false;
		}
		finally {
			isCheck = null;
		}
	};
	
	/**
	 * validataion success, json data --> dom element
	 */
	this.setExcelDomElement = function(excelJson) {
		Utils.log(DEBUG, "ExcelParser.setExcelDomElement call");
		
		var $form = $("form[name=frm]");
		try {
			var method = excelJson["request"]["method"];
			var comUrl = excelJson["request"]["comUrl"];
			var testUrl = excelJson["request"]["testUrl"];
			var reqHeaders = excelJson["request"]["headers"];
			var reqBodys = excelJson["request"]["bodys"];
			var resBodys = excelJson["response"]["bodys"];
			
			// show UI
			controlUi(method, "request");
			// controlTab("request")
			
			// set method
			// $form.find("radio[name=rdo_type]").filter("[value=" + method + "]").prop("checked", true);
			$form.find("input[name='rdo_type'][value='" + method + "']").prop('checked', true);
			
			//set comUrl, testUrl
			$form.find("input[name=comUrl]").val(comUrl);
			$form.find("input[name=testUrl]").val(testUrl);
			
			// var $elm = getParentElement().find("div.frm_tb").eq(0).find("ul").last();
			
			// set header
			ExcelParser.addHeader(method, "request", reqHeaders);
			
			
			// set body
			if (method == "GET") {
				ExcelParser.removeSpecDom(method, "response");
				controlUi(method, "response");
				ExcelParser.addJson(method, "response", resBodys);
				
				ExcelParser.removeSpecDom(method, "request");
				controlUi(method, "request");
				ExcelParser.addBody(method, "request", reqBodys);
			}
			else {
				ExcelParser.removeSpecDom(method, "response");
				controlUi(method, "response");
				ExcelParser.addJson(method, "response", resBodys);
				
				ExcelParser.removeSpecDom(method, "request");
				controlUi(method, "request");
				ExcelParser.addJson(method, "request", reqBodys);
			}
			
			alert("엑셀 업로드가 완료되었습니다.");
			return;
		}
		catch (err) {
			Utils.log(ERR, "ExcelParser.validationExcelData.err", err);
			alert("엑셀 입력 처리 중 오류가 발생하였습니다.");
			return;
		}
		finally {
			$form = null;
		}
	};
	
	this.getParentElement = function(method, type) {
		Utils.log(DEBUG, "ExcelParser.getParentElement call");
		
		var method = method;
		var type = type;
		var $elm = null;
		try {
			if (type == "request") {
				if (method == "GET") {
					$elm = $("div[id=getDiv]").find("div[id=reqDiv]");
				}
				else {
					$elm = $("div[id=postDiv]").find("div[id=reqDiv]");
				}
			}
			// response
			else {
				if (method == "GET") {
					$elm = $("div[id=getDiv]").find("div[id=resDiv]");
				}
				else {
					$elm = $("div[id=postDiv]").find("div[id=resDiv]");
				}
			}
			
			return $elm;
		}
		catch (err) {
			Utils.log(ERR, "ExcelParser.getParentElement.err", err);
		}
		finally {
			$elm = null;
		}
	};
	
	this.removeSpecDom = function(method, type) {
		Utils.log(DEBUG, "ExcelParser.removeSpecDom call");
		
		isJsonFormatting = false;
		var method = method;
	    var type = type;
	    var $elm = null;
	    if (type == "request") {
	        if (method == "GET") {
	            $elm = $("div[id=getDiv]").find("div[id=reqDiv]").find("div.result").find("div.frm_tb").filter(".spec_div");
	        }
	        else {
	            $elm = $("div[id=postDiv]").find("div[id=reqDiv]").find("div.result").find("div.frm_tb").filter(".spec_div");
	        }
	    }
	    // response
	    else {
	        if (method == "GET") {
	            $elm = $("div[id=getDiv]").find("div[id=resDiv]").find("div.result").find("div.frm_tb").filter(".spec_div");
	        }
	        else {
	            $elm = $("div[id=postDiv]").find("div[id=resDiv]").find("div.result").find("div.frm_tb").filter(".spec_div");
	        }
	    }
	    
		var setHtml = [];
		try {
			setHtml.push('<ul class="hd">');
			setHtml.push('<li class="no">라인</li>');
			setHtml.push('<li style="width: 237px;">Key</li>');
			setHtml.push('<li>Spec</li>');
			setHtml.push('</ul>');
			
			$elm.html(setHtml.join(" "));
			ExcelParser.resetSaveData(method, type);
		}
		catch (err) {
			//
		}
		finally {
			$elm, setHtml = null;
		}
	};
	
	this.resetSaveData = function(method, type) {
		Utils.log(DEBUG, "ExcelParser.resetSaveData call");
		
		saveData[method][type]["isValidation"] = false;
		saveData[method][type]["isData"] = false;
		saveData[method][type]["originData"] = null;
		saveData[method][type]["specData"] = null;
	};
	
	this.addHeader = function(method, type, headers) {
		Utils.log(DEBUG, "ExcelParser.addHeader call");
		
		var method = method;
		var type = type;
		var $elm = ExcelParser.getParentElement(method, type).find("div.frm_tb").eq(0).find("ul");
		var $targetElm = ExcelParser.getParentElement(method, "response").find("div.frm_tb").eq(0).find("ul");
		
		var setHtml = [];
		try {
			if (!Utils.isEmpty(headers) && headers.length > 0) {
				setHtml = [];
				for(var i = 0; i < headers.length; i++) {
					var headerStr = headers[i].split(",");
					var key = headerStr[0].trim();
					var value = headerStr[1].trim();
					
					setHtml.push('<ul>');
					setHtml.push('<li>');
					setHtml.push('<input class="text" type="text" name="key" placeholder="input key" title="Key" value="' + key + '" />');
					setHtml.push('</li>');
					setHtml.push('<li>');
					setHtml.push('<input class="text" type="text" name="value" placeholder="input value" title="Value" value="' + value + '" />');
					setHtml.push('</li>');
					setHtml.push('<li class="ctrl">');
					// setHtml.push('<button type="button" class="bn btn_remove" onClick="removeHeader(\'' + method + '\', \'' + type + '\', this); return false;"><em>삭제</em></button>');
					setHtml.push('<button type="button" class="bn btn_remove" onClick="removeHeader(this); return false;"><em>삭제</em></button>');
					setHtml.push('</li>');
					setHtml.push('</ul>');
				}
				$elm.filter(":not(.hd)").remove();
				$elm.after(setHtml.join(" "));
				
				setHtml = [];
				for(var i = 0; i < headers.length; i++) {
					var headerStr = headers[i].split(",");
					var key = headerStr[0].trim();
					var value = headerStr[1].trim();
					
					setHtml.push('<ul>');
					setHtml.push('<li>');
					setHtml.push('<input class="text" type="text" name="key" placeholder="input key" title="Key" value="' + key + '" disabled="disabled" />');
					setHtml.push('</li>');
					setHtml.push('<li>');
					setHtml.push('<input class="text" type="text" name="value" placeholder="input value" title="Value" value="' + value + '" disabled="disabled" />');
					setHtml.push('</li>');
					setHtml.push('<li class="ctrl">');
					// setHtml.push('<button type="button" class="bn btn_remove" onClick="removeHeader(\'' + method + '\', \'' + type + '\', this); return false;"><em>삭제</em></button>');
					setHtml.push('<button type="button" class="bn btn_remove" onClick="alert(\'헤더 삭제는 request 탭에서만 가능합니다.\'); return false;"><em>삭제</em></button>');
					setHtml.push('</li>');
					setHtml.push('</ul>');
				}
				$targetElm.filter(":not(.hd)").remove();
				$targetElm.after(setHtml.join(" "));
			}
		}
		catch (err) {
			Utils.log(ERR, "ExcelParser.addHeader.err", err);
		}
		finally {
			$elm, $targetElm, setHtml = null;
		}
	};
	
	this.addBody = function(method, type, bodys) {
		Utils.log(DEBUG, "ExcelParser.addBody call");
		
		var method = method;
		var type = type;
		var $elm = ExcelParser.getParentElement(method, type).find("div.tab_cont").eq(0).find("ul");
		
		var setHtml = [];
		try {
			if (!Utils.isEmpty(bodys) && bodys.length > 0) {
				for(var i = 0; i < bodys.length; i++) {
					var bodyStr = bodys[i].split(",");
					var key = bodyStr[0].trim();
					var value = bodyStr[1].trim();
					var spec = bodyStr[2].trim();
					
					setHtml.push('<ul>');
					setHtml.push('<li>');
					setHtml.push('<input class="text" type="text" name="key" placeholder="input key" title="Key" value="' + key + '" />');
					setHtml.push('</li>');
					setHtml.push('<li>');
					setHtml.push('<input class="text" type="text" name="value" placeholder="input value" title="Value" value="' + value + '" />');
					setHtml.push('</li>');
					setHtml.push('<li>');
					setHtml.push('<select name="spec" class="sel">');
					for (var t=0; t<specObj["list"].length; t++) {
						if (specObj["list"][t] == spec) {
							setHtml.push('<option value="' + specObj["list"][t] +'" selected="selected">' + specObj["list"][t] + '</option>');
						}
						else {
							setHtml.push('<option value="' + specObj["list"][t] +'">' + specObj["list"][t] + '</option>');
						}
					}
					setHtml.push('</select>');
					setHtml.push('</li>');
					setHtml.push('<li class="ctrl">');
					// setHtml.push('<button type="button" class="bn btn_remove" onClick="removeBody(\'' + method + '\', \'' + type + '\', this); return false;"><em>삭제</em></button>');
					setHtml.push('<button type="button" class="bn btn_remove" onClick="removeBody(this); return false;"><em>삭제</em></button>');
					setHtml.push('</li>');
					setHtml.push('</ul>');
				}
			}
			$elm.filter(":not(.hd)").remove();
			$elm.after(setHtml.join(" "));
		}
		catch (err) {
			Utils.log(ERR, "ExcelParser.addBody.err", err);
		}
		finally {
			$elm, setHtml = null;
		}
	};
	
	this.addJson = function(method, type, bodys) {
		Utils.log(DEBUG, "ExcelParser.addJson call");
		
		var method = method;
		var type = type;
		try {
			onInitEditor(bodys[0], method, type);
		}
		catch (err) {
			Utils.log(ERR, "ExcelParser.addJson.err", err);
		}
	};
};