"use strict";

$(document).ready(function() {
	
	// 초기 설정
	require.config({ paths: { 'vs': '/NICEKIT/nbware/assets/js/monaco-editor/vs' }});
	controlUi("POST", "request");
	removeSpecDom("POST", "request");
	
	// 초기 parameter 세팅
	// parameters["method"] = "POST";
	parameters["dataType"] = "JSON";
	
	// GET, POST
	$(".ai_b2b_form").on("change", "input[name='rdo_type']", function() {
		var method = $(this).val();
		controlUi(method, "request");
	});
	
	// onload json editor
	// onInitEditor("", "GET", "response");
	// onInitEditor("", "POST", "request");
	// onInitEditor("", "POST", "response");
	
	// add select spec
	addSpec();
	
	// 엑셀 파일 업로드
	$("input[name=uploadExcel]").on("change", function(e) {
		var files = e.target.files;
	    var file = files[0];
        
        var fileReader = new FileReader();
        var name = file.name;
        fileReader.onload = ExcelParser.readFile;
        fileReader.onload = function(e) {
        	console.log(e.target.result);
        	ExcelParser.readFile(e);
        }
        fileReader.readAsArrayBuffer(file);
	});
	
});

function onInitEditor(str, method, type) {
	Utils.log(DEBUG, "onInitEditor.call");
	Utils.log(DEBUG, "onInitEditor.str", str);
	Utils.log(DEBUG, "onInitEditor.method", method);
	Utils.log(DEBUG, "onInitEditor.type", type);
	
	if (type == "request") {
		if (method == "GET") {
			if (!Utils.isEmpty(monacoEditor["get"]["request"])) {
				// 
			}
			else {
				// 
			}
		}
		else {
			if (!Utils.isEmpty(monacoEditor["post"]["request"])) {
				onSetEditorData(str, method, type);
			}
			else {
				removeSpecDom(method, type);
			    require(['vs/editor/editor.main'], function() {
			    	monacoEditor["post"]["request"] = monaco.editor.create(document.getElementById('ta_post_request'), {
			            value: str,
						language: 'json' // type 'json' or 'xml'
			        });
			    });
			}
		}
	}
	// response
	else {
		if (method == "GET") {
			if (!Utils.isEmpty(monacoEditor["get"]["response"])) {
				onSetEditorData(str, method, type);
			}
			else {
				removeSpecDom(method, type);
			    require(['vs/editor/editor.main'], function() {
			    	monacoEditor["get"]["response"] = monaco.editor.create(document.getElementById('ta_get_response'), {
			            value: str,
						language: 'json' // type 'json' or 'xml'
			        });
			    });
			}
		}
		else {
			if (!Utils.isEmpty(monacoEditor["post"]["response"])) {
				onSetEditorData(str, method, type);
			}
			else {
				removeSpecDom(method, type);
			    require(['vs/editor/editor.main'], function() {
			    	monacoEditor["post"]["response"] = monaco.editor.create(document.getElementById('ta_post_response'), {
			            value: str,
						language: 'json' // type 'json' or 'xml'
			        });
			    });
			}
		}
	}
}

function onChangeLanguage(lang) {
	monaco.editor.setModelLanguage(monacoEditor.getModel(), lang);
}

/**
 * textarea get value
 */
function onGetEditorData(method, type) {
	Utils.log(DEBUG, "onGetEditorData.call");
	
	var rst = null;
	try {
		return getEditorId(method, type).getValue();
	}
	catch (err) {
		// 
	}
	finally {
		rst = null;
	}
}

function onSetEditorData(str, method, type) {
	Utils.log(DEBUG, "onSetEditorData.call");
	
	getEditorId(method, type).setValue(str);
}

function getEditorId(method, type) {
	Utils.log(DEBUG, "getEditorId.call");
	
	var $id = null;
	try {
		if (type == "request") {
			if (method == "GET") {
				$id = monacoEditor["get"]["request"];
			}
			else {
				$id = monacoEditor["post"]["request"];
			}
		}
		// response
		else {
			if (method == "GET") {
				$id = monacoEditor["get"]["response"];
			}
			else {
				$id = monacoEditor["post"]["response"];
			}
		}
		
		return $id;
	}
	catch (err) {
		// 
	}
	finally {
		$id = null;
	}
}

function setScrollPosition(method, type, offset) {
	Utils.log(DEBUG, "setScrollPosition.call");
	
	getEditorId(method, type).setScrollPosition({scrollTop: offset});
}

function onEditorPretty(method, type) {
	Utils.log(DEBUG, "onPretty.call");
	
	getEditorId(method, type).getAction('editor.action.formatDocument').run();
}

/**
 * method : get, post
 * type: request, response
 */
function controlUi(method, type) {
	Utils.log(DEBUG, "controlUi.call");
	Utils.log(DEBUG, "controlUi.method", method);
	Utils.log(DEBUG, "controlUi.type", type);
	
	var $form = $("form[name=frm]");
	
	// set config
	parameters["method"] = method;
	config["method"] = method;
	config["type"] = type;
	
	var $parentDiv = null;
	$("div.ai_b2b_form").find("ul.frm ").filter(".req").find("div.tab").find("ul.tabt").find("li").removeClass("on");
	$("div[id*=Div]").hide();
	$("div.tab_cont").hide();
	
	// request
	if (type == "request") {
		$("div.ai_b2b_form").find("ul.frm ").filter(".req").find("div.tab").find("ul.tabt").find("li").eq(0).addClass("on");
		if (method == "GET") {
			$("div[id=getDiv]").show();
			$("div[id=getDiv]").find("div[id=reqDiv]").show();
			$("div[id=getDiv]").find("div[id=reqDiv]").find("div.tab_cont").eq(0).show();
		}
		else {
			$("div[id=postDiv]").show();
			$("div[id=postDiv]").find("div[id=reqDiv]").show();
			$("div[id=postDiv]").find("div[id=reqDiv]").find("div.tab_cont").eq(1).show();
			
			if (!Utils.isEmpty(monacoEditor["post"]["request"])) {
				var str = onGetEditorData(method, type);
				onInitEditor(str, method, type);
			}
			else {
				onInitEditor("", method, type);
			}
		}
	}
	// response
	else {
		$("div.ai_b2b_form").find("ul.frm ").filter(".req").find("div.tab").find("ul.tabt").find("li").eq(1).addClass("on");
		if (method == "GET") {
			// TODO: 
			// get, response --> show json editor
			$("div[id=getDiv]").show();
			$("div[id=getDiv]").find("div[id=resDiv]").show();
			$("div[id=getDiv]").find("div[id=resDiv]").find("div.tab_cont").eq(1).show();
			
			if (!Utils.isEmpty(monacoEditor["get"]["response"])) {
				var str = onGetEditorData(method, type);
				onInitEditor(str, method, type);
			}
			else {
				onInitEditor("", method, type);
			}
		}
		else {
			$("div[id=postDiv]").show();
			$("div[id=postDiv]").find("div[id=resDiv]").show();
			$("div[id=postDiv]").find("div[id=resDiv]").find("div.tab_cont").eq(1).show();
			
			if (!Utils.isEmpty(monacoEditor["post"]["response"])) {
				var str = onGetEditorData(method, type);
				onInitEditor(str, method, type);
			}
			else {
				onInitEditor("", method, type);
			}
		}
	}
}

function controlTab(type) {
	Utils.log(DEBUG, "controlTab.call");
	Utils.log(DEBUG, "controlTab.type", type);
	
	var $form = $("form[name=frm]");
	var method = config["method"];
	
	controlUi(method, type);
}

function getParentElement() {
	Utils.log(DEBUG, "getParentElement.call");
	
	var method = config["method"];
	var type = config["type"];
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
		Utils.log(ERR, "getParentElement.err", err);
	}
	finally {
		$elm = null;
	}
}

function addHeader() {
	Utils.log(DEBUG, "addHeader.call");
	
	var method = config["method"];
	var type = config["type"];
	var $elm = getParentElement().find("div.frm_tb").eq(0).find("ul").last();
	
	var setHtml = [];
	try {
		setHtml.push('<ul>');
		setHtml.push('<li>');
		setHtml.push('<input class="text" type="text" name="key" placeholder="input key" title="Key" />');
		setHtml.push('</li>');
		setHtml.push('<li>');
		setHtml.push('<input class="text" type="text" name="value" placeholder="input value" title="Value" />');
		setHtml.push('</li>');
		setHtml.push('<li class="ctrl">');
		// setHtml.push('<button type="button" class="bn btn_remove" onClick="removeHeader(\'' + method + '\', \'' + type + '\', this); return false;"><em>삭제</em></button>');
		setHtml.push('<button type="button" class="bn btn_remove" onClick="removeHeader(this); return false;"><em>삭제</em></button>');
		setHtml.push('</li>');
		setHtml.push('</ul>');
		
		$elm.after(setHtml.join(" "));
	}
	catch (err) {
		Utils.log(ERR, "addHeader.err", err);
	}
	finally {
		$elm, setHtml = null;
	}
}

function removeHeader(obj) {
	Utils.log(DEBUG, "removeHeader.call");
	
	var method = config["method"];
	var type = config["type"];
	var $elm = getParentElement().find("div.frm_tb").eq(0).find("ul");
	try {
		var len = $elm.find("li").find("button.btn_remove").length;
		var index = $elm.find("li").find("button.btn_remove").index($(obj));
		Utils.log(DEBUG, "removeHeader.len", len);
		Utils.log(DEBUG, "removeHeader.index", index);
		if (len == 1 && index == 0) {
			$(obj).parents("ul").eq(0).remove();
			addHeader();
		}
		else {
			$(obj).parents("ul").eq(0).remove();
		}
	}
	catch (err) {
		Utils.log(ERR, "removeHeader.err", err);
	}
	finally {
		$elm = null;
	}
}

function addSpec() {
	Utils.log(DEBUG, "addSpec.call");
	
	var $elm = $("li[title='Spec']");
	var setHtml = [];
    try {
        setHtml.push('<select name="spec" class="sel">');
        for (var i=0; i<specObj["list"].length; i++) {
            if (i == 0) {
                setHtml.push('<option value="' + specObj["list"][i] +'" selected="selected">' + specObj["list"][i] + '</option>');
            }
            else {
                setHtml.push('<option value="' + specObj["list"][i] +'">' + specObj["list"][i] + '</option>');
            }
        }
        setHtml.push('</select>');
        
        $elm.html(setHtml.join(" "));
    }
    catch (err) {
        Utils.log(ERR, "addSpec.err", err);
    }
    finally {
        $elm, setHtml = null;
    }
}

function addBody() {
	Utils.log(DEBUG, "addBody.call");
	
	var method = config["method"];
    var type = config["type"];
    var $elm = getParentElement().find("div.tab_cont").eq(0).find("ul").last();
    
    var setHtml = [];
	try {
		setHtml.push('<ul>');
		setHtml.push('<li>');
		setHtml.push('<input class="text" type="text" name="key" placeholder="input key" title="Key" />');
		setHtml.push('</li>');
		setHtml.push('<li>');
		setHtml.push('<input class="text" type="text" name="value" placeholder="input value" title="Value" />');
		setHtml.push('</li>');
		setHtml.push('<li>');
		setHtml.push('<select name="spec" class="sel">');
		for (var i=0; i<specObj["list"].length; i++) {
			if (i == 0) {
				setHtml.push('<option value="' + specObj["list"][i] +'" selected="selected">' + specObj["list"][i] + '</option>');
			}
			else {
				setHtml.push('<option value="' + specObj["list"][i] +'">' + specObj["list"][i] + '</option>');
			}
		}
		setHtml.push('</select>');
		setHtml.push('</li>');
		setHtml.push('<li class="ctrl">');
		// setHtml.push('<button type="button" class="bn btn_remove" onClick="removeBody(\'' + method + '\', \'' + type + '\', this); return false;"><em>삭제</em></button>');
		setHtml.push('<button type="button" class="bn btn_remove" onClick="removeBody(this); return false;"><em>삭제</em></button>');
		setHtml.push('</li>');
		setHtml.push('</ul>');
		
		$elm.after(setHtml.join(" "));
	}
	catch (err) {
		Utils.log(ERR, "addBody.err", err);
	}
	finally {
		$elm, setHtml = null;
	}
}

function removeBody(obj) {
	Utils.log(DEBUG, "removeBody.call");
	
	var method = config["method"];
    var type = config["type"];
    var $elm = getParentElement().find("div.tab_cont").eq(0).find("ul");
	try {
		var len = $elm.find("li").find("button.btn_remove").length;
		var index = $elm.find("li").find("button.btn_remove").index($(obj));
		Utils.log(DEBUG, "removeBody.len", len);
		Utils.log(DEBUG, "removeBody.index", index);
		if (len == 1 && index == 0) {
			$(obj).parents("ul").eq(0).remove();
			addBody();
		}
		else {
			$(obj).parents("ul").eq(0).remove();
		}
	}
	catch (err) {
		Utils.log(ERR, "removeBody.err", err);
	}
	finally {
		$elm = null;
	}
}

function removeSpecDom(method, type) {
	Utils.log(DEBUG, "removeSpecDom.call");
	
	isJsonFormatting = false;
	var method = config["method"];
    var type = config["type"];
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
	}
	catch (err) {
		//
	}
	finally {
		$elm, setHtml = null;
	}
}

function setMessage(str) {
	Utils.log(DEBUG, "setMessage.call");
	
	getParentElement().find("ul.post_check").find("li.check").text(str);
}

function jsonPretty() {
	Utils.log(DEBUG, "jsonPretty.call");
	
	// monacoEditor.getAction('editor.action.formatDocument').run();
	// monacoEditor.getAction('editor.action.formatDocument').run().then(() =>	setEditor() );
	// monacoEditor.getAction('editor.action.formatDocument').run();
	
	var method = config["method"];
	var type = config["type"];
	onEditorPretty(method, type);
	
	setTimeout(function() {
		setScrollPosition(method, type, 0);
		setEditor(method, type);
	}, 500);
}

var isJsonFormatting = false;
function setEditor(method, type) {
	Utils.log(DEBUG, "setEditor.call");
	
	// remove spec dom
	removeSpecDom(method, type);
	setMessage("");
	
	var jsonText = onGetEditorData(method, type);
	var jsonElm = [];
	try {
		if (!Utils.isEmpty(jsonText.trim())) {
			Utils.log(DEBUG, "jsonValidation.json.notEmpty");
			onInitEditor(jsonText, method, type);
			Utils.log(DEBUG, "jsonValidation.json.jsonText", jsonText);
			
			var isJson = jsonValidation(jsonText);
			console.log("setEditor.isJson : " + isJson);
			if (isJson) {
				var obj = JSON.parse(jsonText.trim());
				jsonElm = jsonAssign(obj);
				Utils.log(DEBUG, "setEditor.jsonElm", jsonElm);
				Utils.log(DEBUG, "setEditor.jsonElm.len", jsonElm.length);
				if (jsonElm && jsonElm.length >= 3) {
					setMessage("json validation success");
					isJsonFormatting = true;
					makeSpec();
				}
				else {
					throw "empty json data";
				}
			}
			else {
				throw "check json format";
			}
		}
		else {
			throw "empty json text";
		}
	}
	catch (err) {
		setMessage(err);
		return;
	}
	finally {
		jsonText = null;
	}
}

function jsonValidation(jsonStr) {
	Utils.log(DEBUG, "jsonValidation.call");
	Utils.log(DEBUG, "jsonValidation.jsonStr", jsonStr);
	Utils.log(DEBUG, "jsonValidation.jsonStr.typeof", typeof(jsonStr));
	Utils.log(DEBUG, "jsonValidation.jsonStr.constructor", jsonStr.constructor);
	
	var isCheck = false;
	try {
		if (!Utils.isEmpty(jsonStr)) {
			var obj = JSON.parse(jsonStr);
			if (obj.constructor === Array) {
				var len = obj.length;
				Utils.log(DEBUG, "jsonValidation.keys.len", len);
				if (len > 0) {
					isCheck = true;
				}
			}
			else if (obj.constructor === Object) {
				var len = Object.keys(obj).length;
				Utils.log(DEBUG, "jsonValidation.keys.len", len);
				if (len > 0) {
					isCheck = true;
				}
			}
		}
		
		return isCheck;
	}
	catch (err) {
		setMessage(err);
		return;
	}
	finally {
		isCheck = null;
	}
}

var jsonArrs = [];
function jsonAssign(obj) {
	Utils.log(DEBUG, "jsonAssign.call");
	Utils.log(DEBUG, "jsonAssign.obj.obj", obj);
	Utils.log(DEBUG, "jsonAssign.obj.typeof", typeof(obj));
	Utils.log(DEBUG, "jsonAssign.obj.constructor", obj.constructor);
	
	jsonArrs = [];
	var depth = 0;
	if (obj && typeof(obj) === "object") {
		jsonArrs = jsonDataToDomObject(obj, depth, obj.constructor == Array ? "array" : "object", null, null);
	}
	
	return jsonArrs;
}

function jsonDataToDomObject(obj, originDepth, parentType, parentKey, keys) {
	Utils.log(DEBUG, "jsonDataToDomObject.call");
	
	var map = {};
	var keyLen = Object.keys(obj).length;
	try {
		var nextDepth = originDepth + 1;
		var prevDepth = originDepth - 1;
		
		Utils.log(DEBUG, "jsonDataToDomObject.originDepth", originDepth);
		Utils.log(DEBUG, "jsonDataToDomObject.parentType", parentType);
		Utils.log(DEBUG, "jsonDataToDomObject.parentKey", parentKey);
		Utils.log(DEBUG, "jsonDataToDomObject.keys", keys);
		
		if (originDepth == 0) {
			if (obj.constructor === Array) {
				keys = "$";
				
            	map = {};
            	map["elmType"] = "array";
            	map["actionType"] = "open";
            	map["depth"] = originDepth;
            	map["key"] = "";
            	map["value"] = "";
            	map["specUse"] = "n";
            	map["selected"] = "n";
            	map["jsonPath"] = keys;
                jsonArrs.push(map);
            }
            else if (obj.constructor === Object) {
            	keys = "$";
            	
            	map = {};
            	map["elmType"] = "object";
            	map["actionType"] = "open";
            	map["depth"] = originDepth;
            	map["key"] = "";
            	map["value"] = "";
            	map["specUse"] = "n";
            	map["selected"] = "n";
            	map["jsonPath"] = keys;
                jsonArrs.push(map);
            }
		}
		
		if (keyLen > 0) {
			for (var key in obj) {
		        if (obj.hasOwnProperty(key)) {
		            var o = obj[key];
		            
		            Utils.log(DEBUG, "jsonDataToDomObject.o", o);
		            Utils.log(DEBUG, "jsonDataToDomObject.key", key);
		            Utils.log(DEBUG, "jsonDataToDomObject.o.typeof", typeof(o));
		            Utils.log(DEBUG, "jsonDataToDomObject.o.constructor", o.constructor);
		            
		            if (o.constructor === Array) {
		            	key = parentType == "array" ? Number(key) : key;
		            	
		            	var objKey = typeof(key) == "number" ? "[" + key + "]" : key;
		            	var tempKey = keys.concat(".", objKey);
		            	map = {};
		            	map["elmType"] = "array";
		            	map["actionType"] = "open";
		            	map["depth"] = nextDepth;
		            	map["key"] = key;
		            	map["value"] = "";
		            	map["specUse"] = "n";
		            	map["selected"] = "n";
		            	map["jsonPath"] = tempKey;
	                    jsonArrs.push(map);
	                    
	                    jsonArrs = jsonDataToDomObject(o, nextDepth, "array", key, tempKey);
	                    
	                    map = {};
		            	map["elmType"] = "array";
		            	map["actionType"] = "close";
		            	map["depth"] = nextDepth;
		            	map["key"] = "";
		            	map["value"] = "";
		            	map["specUse"] = "n";
		            	map["selected"] = "n";
	                    jsonArrs.push(map);
		            }
		            else if (o.constructor === Object) {
		            	key = parentType == "array" ? Number(key) : key;
		            	
		            	var objKey = typeof(key) == "number" ? "[" + key + "]" : key;
		            	var tempKey = keys.concat(".", objKey);
		            	map = {};
		            	map["elmType"] = "object";
		            	map["actionType"] = "open";
		            	map["depth"] = nextDepth;
		            	map["key"] = key;
		            	map["value"] = "";
		            	map["specUse"] = "n";
		            	map["selected"] = "n";
		            	map["jsonPath"] = tempKey;
		                jsonArrs.push(map);
		                
		                jsonArrs = jsonDataToDomObject(o, nextDepth, "object", key, tempKey);
		                
		                map = {};
		            	map["elmType"] = "object";
		            	map["actionType"] = "close";
		            	map["depth"] = nextDepth;
		            	map["key"] = "";
		            	map["value"] = "";
		            	map["specUse"] = "n";
		            	map["selected"] = "n";
		                jsonArrs.push(map);
		            }
		            else if (o.constructor === String || o.constructor === Number || o.constructor === Boolean) {
		            	key = parentType == "array" ? Number(key) : key;
		            	
		            	var objKey = typeof(key) == "number" ? "[" + key + "]" : key;
		            	var tempKey = keys.concat(".", objKey);
		            	map = {};
		            	map["elmType"] = typeof(o);
		            	map["actionType"] = "input";
		            	map["depth"] = nextDepth;
		            	map["key"] = key;
		            	map["specUse"] = "y";
		            	map["selected"] = parentType == "array" ? "n" : "y";
		            	map["value"] = o;
		            	map["jsonPath"] = tempKey;
		                jsonArrs.push(map);
		            }
		        }
		    }
			
			if (originDepth == 0) {
				if (obj.constructor === Array) {
	            	map = {};
	            	map["elmType"] = "array";
	            	map["actionType"] = "close";
	            	map["depth"] = originDepth;
	            	map["key"] = "";
	            	map["value"] = "";
	            	map["specUse"] = "n";
	            	map["selected"] = "n";
	                jsonArrs.push(map);
	            }
	            else if (obj.constructor === Object) {
	            	map = {};
	            	map["elmType"] = "object";
	            	map["actionType"] = "close";
	            	map["depth"] = originDepth;
	            	map["key"] = "";
	            	map["value"] = "";
	            	map["specUse"] = "n";
	            	map["selected"] = "n";
	                jsonArrs.push(map);
	            }
			}
		}
		else {
			// empty Json Date
			jsonArrs = [];
		}
		
		return jsonArrs;
	}
	catch (err) {
		Utils.log(ERR, "jsonDataToDomObject.err", err);
		setMessage(err);
		return null;
	}
	finally {
		map = null;
		jsonArrs = null;
	}
}

function makeSpec() {
	Utils.log(DEBUG, "makeSpec.call");
	
	var method = config["method"];
	var type = config["type"];
	var jsonText = onGetEditorData(method, type);
	
	var jsonElm = [];
	try {
		if (!Utils.isEmpty(jsonText.trim())) {
			onInitEditor(jsonText, method, type);
			
			var isJson = jsonValidation(jsonText);
			if (isJson) {
				var obj = JSON.parse(jsonText.trim());
				jsonElm = jsonAssign(obj);
				Utils.log(DEBUG, "setEditor.jsonElm", jsonElm);
				Utils.log(DEBUG, "setEditor.jsonElm.len", jsonElm.length);
				if (jsonElm && jsonElm.length >= 3) {
					isJsonFormatting = true;
					saveData[method][type]["isValidation"] = true;
					createDomElement(jsonElm);
				}
				else {
					throw "empty json data";
				}
			}
			else {
				throw "check json format";
			}
		}
		else {
			throw "empty json text";
		}
	}
	catch (err) {
		setMessage(err);
		return;
	}
	finally {
		jsonText = null;
	}
}

function createDomElement(obj) {
	Utils.log(DEBUG, "createDomElement.call");
	Utils.log(DEBUG, "createDomElement.obj", obj);
	
	var method = config["method"];
	var type = config["type"];
	
	var $elm = getParentElement().find("div.result").find("div.frm_tb").filter(".spec_div");
	var setHtml = [];
	try {
		setHtml.push('<ul class="hd">');
		setHtml.push('<li class="no">라인</li>');
		setHtml.push('<li style="width: 237px;">Key</li>');
		setHtml.push('<li>Spec</li>');
		setHtml.push('</ul>');
		
		var regExp = /^\d+$/;
		if (obj && obj.constructor === Array) {
			var objLen = obj.length;
			var margin = "&nbsp;";
			if (objLen > 0) {
				for (var i=0; i < objLen; i++) {
					var objData = obj[i];
					var no = i + 1;
					var elmType = objData["elmType"];
					var actionType = objData["actionType"];
					var depth = Number(objData["depth"]);
					var key = objData["key"];
					var keyIsNumber = regExp.test(key) ? true : false;
					var value = objData["value"];
					var specUse = objData["specUse"];
					var selected = objData["selected"];
					var jsonPath = objData["jsonPath"];
					
					setHtml.push('<ul data-elmType="' + elmType + '" data-actionType="' + actionType + '" data-depth="' + depth + '" data-specUse="' + specUse + '" data-selected="' + selected + '" data-key="' + key + '" data-keyType="' + typeof(key) + '" data-value="' + value + '" data-jsonPath="' + jsonPath + '" title="' + key +'">');
					
					setHtml.push('<li class="no">');
					setHtml.push(no);
					setHtml.push('</li>');
					
					setHtml.push('<li style="width: 237px; text-align: left; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">');
					for (var t=0; t<depth; t++) {
						setHtml.push(margin);
					}
					
					if (actionType == "open") {
						if (elmType == "array") {
							if (Utils.isEmpty(key) && Utils.isEmpty(value)) {
								setHtml.push('[');
							}
							else {
								if (typeof(key) == "number") {
									setHtml.push('[');
								}
								else {
									setHtml.push('"' + key + '" : [');
								}
							}
						}
						else if (elmType == "object") {
							if (Utils.isEmpty(key) && Utils.isEmpty(value)) {
								setHtml.push('{');
							}
							else {
								if (typeof(key) == "number") {
									setHtml.push('{');
								}
								else {
									setHtml.push('"' + key + '" : {');
								}
							}
						}
					}
					else if (actionType == "close") {
						if (elmType == "array") {
							setHtml.push(']');
						}
						else if (elmType == "object") {
							setHtml.push('}');
						}
					}
					else if (actionType == "input") {
						if (typeof(key) == "number") {
							setHtml.push('&nbsp;');
						}
						else {
							setHtml.push('"' + key + '" : ');
						}
					}
					setHtml.push('</li>');
					
					/*setHtml.push('<li>');
					if ((elmType == "string" || elmType == "number" || elmType == "boolean") && actionType == "input") {
						setHtml.push(value);
					}
					else {
						setHtml.push('&nbsp;');
					}
					setHtml.push('</li>');*/
					
					setHtml.push('<li>');
					if (specUse == "y") {
						if (selected == "y") {
							setHtml.push('<select name="spec" class="sel">');
							for (var y=0; y<specObj["list"].length; y++) {
					            if (y == 0) {
					                setHtml.push('<option value="' + specObj["list"][y] +'" selected="selected">' + specObj["list"][y] + '</option>');
					            }
					            else {
					                setHtml.push('<option value="' + specObj["list"][y] +'">' + specObj["list"][y] + '</option>');
					            }
					        }
							setHtml.push('</select>');
						}
						else {
							setHtml.push('<select name="spec" class="sel" disabled>');
							setHtml.push('<option value="' + specObj["undefined"] + '">' + specObj["undefined"] + '</option>');
							setHtml.push('</select>');
						}
					}
					else {
						setHtml.push('<select name="spec" class="sel" disabled style="visibility:hidden;">');
						setHtml.push('<option value="' + specObj["undefined"] + '">' + specObj["undefined"] + '</option>');
						setHtml.push('</select>');
					}
					setHtml.push('</li>');
					
					setHtml.push('</ul>');
				}
			}
		}
		
		$elm.html(setHtml.join(" "));
		setMessage("json validation success");
	}
	catch (err) {
		setMessage(err);
		return;
	}
	finally {
		$elm, setHtml = null;
	}
}

function saveSpec() {
	Utils.log(DEBUG, "saveSpec.call");
	
	var method = config["method"];
	var type = config["type"];
	var $elm = getParentElement().find("div.result").find("div.frm_tb").filter(".spec_div").find("ul:not(.hd)");
	var originJson = null;
	var specJson = null;
	try {
		var elmLen = $elm.length;
		if (isJsonFormatting && elmLen >= 3) {
			var jsonText = onGetEditorData(method, type);
			var obj = JSON.parse(jsonText);
			if ("object" != typeof obj) {
				throw "check json format : " + String(typeof obj);
			}
			
			var isJson = jsonValidation(jsonText);
			console.log("saveSpec.isJson", isJson);
			if (isJson) {
				originJson = obj;
				specJson = JSON.parse(jsonText);
				
				var $specElm = $elm.filter("[data-specuse='y']");
				var specLen = $specElm.length;
				console.log("saveSpec.specLen", specLen);
				
				if (specLen > 0) {
					for (var i = 0; i < specLen; i++) {
						var spectype = $elm.filter("[data-specuse='y']").eq(i).find("select[name=spec]").val();
						var jsonpath = $elm.filter("[data-specuse='y']").eq(i).data("jsonpath");
						
						var paths = [];
						paths = getJsonPath(jsonpath);
						Utils.log(DEBUG, "saveSpec.spectype", spectype);
						Utils.log(DEBUG, "saveSpec.paths", paths);
						
						changeValueStringProperty(specJson, paths, spectype);
					}
					
					setMessage("save spec success");
				}
				Utils.log(DEBUG, "saveSpec.specJson", specJson);
				console.log("[DEBUG] saveSpec.specJson : " + JSON.stringify(specJson, undefined, 3));
				// alert(JSON.stringify(specJson, undefined, 3));
				Utils.log(DEBUG, "saveSpec.originJson", originJson);
				Utils.log(DEBUG, "saveSpec.method", method);
				Utils.log(DEBUG, "saveSpec.type", type);
				Utils.log(DEBUG, "saveSpec.obj", obj);
				
				saveData[method][type]["isData"] = true;
				saveData[method][type]["originData"] = originJson;
				saveData[method][type]["specData"] = specJson;
			}
			else {
				throw "check json format";
			}
		}
		else {
			throw "please json validation";
		}
	}
	catch (err) {
		Utils.log(ERR, "saveSpec.err", err);
		setMessage(err);
		return;
	}
	finally {
		originJson = null;
		specJson = null;
	}
}

function getJsonPath(s) {
	Utils.log(DEBUG, "getJsonPath.call");
	
	var arrs = [];
	try {
		var regExp3 = /\[/gi;
		if (!Utils.isEmpty(s)) {
			s = s.replace("$.", "");	// remove $.
			// s = s.replace(/\[(\w+)\]/g, "$1");
			s = s.replace(/^\./, "");
			var a = s.split(".");
			for (var i = 0; i < a.length; i++) {
				var k = a[i];
				Utils.log(DEBUG, "getJsonPath.k1", k);
				
				var regExp1 = /\[(\w+)\]/g;
				if (regExp1.test(k)) {
					k = k.replace(/\[(\w+)\]/g, "$1");
					Utils.log(DEBUG, "getJsonPath.k2", k);
					
					var regExp2 = /^\d+$/;
					if (regExp2.test(k)) {
						Utils.log(DEBUG, "getJsonPath.k2", k);
						k = Number(k);
					}
				}
				else {
					Utils.log(DEBUG, "getJsonPath.k3", k);
					k = a[i];
				}
				
				arrs.push(k);
			}
		}
		
		return arrs;
	}
	catch (err) {
		Utils.log(ERR, "getJsonPath.err", err);
		return;
	}
	finally {
		arrs = null;
	}
}

function changeValueStringProperty(o, p, v) {
	Utils.log(DEBUG, "changeValueStringProperty.call");
	
	try {
		if (p.length === 1) {
			o[p] = v;
			return;
		}
		
		return changeValueStringProperty(o[p[0]], p.slice(1), v);
	}
	catch (err) {
		Utils.log(ERR, "changeValueStringProperty.err", err);
	}
}

function validationData() {
    Utils.log(DEBUG, "validationData.call");
    
    var method = config["method"];
    var type = config["type"];
    Utils.log(DEBUG, "validationData.method", method);
    Utils.log(DEBUG, "validationData.type", type);
    
    var $form = $("form[name=frm]");
    var $elm = getParentElement();
    try {
    	// check comUrl, validation comUrl
        var comUrl = $form.find("input[name=comUrl]").val();
        var testUrl = $form.find("input[name=testUrl]").val();
        if (!Utils.isEmpty(comUrl.trim())) {
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
        if (!Utils.isEmpty(testUrl.trim())) {
            var regExp = /^http(s)?:\/\/(www\.)?[a-z0-9]+([\-\.]{1}[a-z0-9]+)*\.[a-z]{2,5}(:[0-9]{1,5})?(\/.*)?$/;
            if (!regExp.test(testUrl)) {
                alert("test url 형식을 확인해 주세요.");
                return;
            }
        }
        
        // check Header
        var $reqHeader = method == "GET" ? $("div[id=getDiv]").find("div[id=reqDiv]").find("div.frm_tb").eq(0).find("ul:not(.hd)") : $("div[id=postDiv]").find("div[id=reqDiv]").find("div.frm_tb").eq(0).find("ul:not(.hd)");
        var $resHeader = method == "GET" ? $("div[id=getDiv]").find("div[id=resDiv]").find("div.frm_tb").eq(0).find("ul:not(.hd)") : $("div[id=postDiv]").find("div[id=resDiv]").find("div.frm_tb").eq(0).find("ul:not(.hd)");
        
        var reqHeaders = [];
        var resHeaders = [];
        
        var reqHeaderLen = $reqHeader.length;
        var resHeaderLen = $resHeader.length;
        Utils.log(DEBUG, "validationData.reqHeaderLen", reqHeaderLen);
        Utils.log(DEBUG, "validationData.resHeaderLen", resHeaderLen);
        
        // check request header
        if (reqHeaderLen >= 1) {
            for (var i = 0; i < reqHeaderLen; i++) {
                var key = $reqHeader.eq(i).find("li").find("input[name=key]").val();
                var value = $reqHeader.eq(i).find("li").find("input[name=value]").val();
                if (!Utils.isEmpty(key.trim()) && !Utils.isEmpty(value.trim())) {
                    var headerMap = {};
                    headerMap[key] = value;
                    reqHeaders.push(headerMap);
                }
                else {
                    if (Utils.isEmpty(key)) {
                        alert("request header의 key를 입력해 주세요.");
                        return;
                    }
                    else {
                        alert("request header의 value를 입력해 주세요.");
                        return;
                    }
                    break;
                    return;
                }
            }
            Utils.log(DEBUG, "validationData.reqHeaders", reqHeaders);
        }
        else {
            alert("request header를 입력해 주세요.");
            return;
        }
        
     // check response header
        if (resHeaderLen >= 1) {
            for (var i = 0; i < resHeaderLen; i++) {
                var key = $resHeader.eq(i).find("li").find("input[name=key]").val();
                var value = $resHeader.eq(i).find("li").find("input[name=value]").val();
                if (!Utils.isEmpty(key.trim()) && !Utils.isEmpty(value.trim())) {
                    var headerMap = {};
                    headerMap[key] = value;
                    resHeaders.push(headerMap);
                }
                else {
                    if (Utils.isEmpty(key)) {
                        alert("response header의 key를 입력해 주세요.");
                        return;
                    }
                    else {
                        alert("response header의 value를 입력해 주세요.");
                        return;
                    }
                    break;
                    return;
                }
            }
            Utils.log(DEBUG, "validationData.resHeaders", resHeaders);
        }
        else {
            alert("response header를 입력해 주세요.");
            return;
        }
        
        if (method == "GET") {
            // check body
            var requestBody = [];
            var requestSpec = [];
            var $body = $("div[id=getDiv]").find("div[id=reqDiv]").find("div.frm_tb").eq(1).find("ul:not(.hd)");
            var bodyLen = $body.length;
            if (bodyLen >= 1) {
                for (var i = 0; i < bodyLen; i++) {
                    var key = $body.eq(i).find("li").find("input[name=key]").val();
                    var value = $body.eq(i).find("li").find("input[name=value]").val();
                    var spec = $body.eq(i).find("li").find("select[name=spec]").val();
                    if (!Utils.isEmpty(key.trim()) && !Utils.isEmpty(value.trim()) && !Utils.isEmpty(spec.trim())) {
                        var bodyMap = {};
                        bodyMap[key] = value;
                        requestBody.push(bodyMap);
                        
                        var bodySpec = {};
                        bodySpec[key] = spec;
                        requestSpec.push(bodySpec);
                    }
                    else {
                        if (Utils.isEmpty(key)) {
                            alert("request body의 key를 입력해 주세요.");
                            return;
                        }
                        else if (Utils.isEmpty(value)) {
                            alert("request body의 value를 입력해 주세요.");
                            return;
                        }
                        else {
                            alert("request body의 spec을 선택해 주세요.");
                            return;
                        }
                        break;
                        return;
                    }
                }
                Utils.log(DEBUG, "validationData.requestBody", requestBody);
                Utils.log(DEBUG, "validationData.requestSpec", requestSpec);
                
                saveData[method]["request"]["isValidation"] = true;
                saveData[method]["request"]["isData"] = true;
                saveData[method]["request"]["originData"] = requestBody;
                saveData[method]["request"]["specData"] = requestSpec;
            }
            else {
                alert("request body를 입력해 주세요.");
                return;
            }
        }
        
        //check body
        var reqIsValidation = saveData[method]["request"]["isValidation"];
        var reqIsData = saveData[method]["request"]["isData"];
        var reqOriginData = saveData[method]["request"]["originData"];
        var reqSpecData = saveData[method]["request"]["specData"];
        Utils.log(DEBUG, "validationData.reqIsValidation", reqIsValidation);
        Utils.log(DEBUG, "validationData.reqIsData", reqIsData);
        Utils.log(DEBUG, "validationData.reqOriginData", reqOriginData);
        Utils.log(DEBUG, "validationData.reqSpecData", reqSpecData);
        
        if (reqIsValidation && reqIsData && (!Utils.isEmptyParams(reqOriginData) && !Utils.isEmptyParams(reqSpecData))) {
            //
        }
        else if (reqIsValidation && !reqIsData) {
        	alert("request body의 Spec 저장 여부를 확인해 주세요.");
            return;
        }
        else {
        	alert("request body의 Validaton 여부를 확인해 주세요.");
            return;
        }
        
        var resIsValidation = saveData[method]["response"]["isValidation"];
        var resIsData = saveData[method]["response"]["isData"];
        var resOriginData = saveData[method]["response"]["originData"];
        var resSpecData = saveData[method]["response"]["specData"];
        Utils.log(DEBUG, "validationData.resIsValidation", resIsValidation);
        Utils.log(DEBUG, "validationData.resIsData", resIsData);
        Utils.log(DEBUG, "validationData.resOriginData", resOriginData);
        Utils.log(DEBUG, "validationData.resSpecData", resSpecData);
        
        if (resIsValidation && resIsData && (!Utils.isEmptyParams(resOriginData) && !Utils.isEmptyParams(resSpecData))) {
        	//
        }
        else if (resIsValidation && !resIsData) {
        	alert("response body의 Spec 저장 여부를 확인해 주세요.");
            return;
        }
        else {
        	alert("response body의 Validaton 여부를 확인해 주세요.");
            return;
        }
        
        Utils.log(DEBUG, "validationData.saveData", saveData);
        parameters["testUrl"] = !Utils.isEmpty(testUrl.trim()) ? testUrl : "";
        parameters["comUrl"] = !Utils.isEmpty(comUrl.trim()) ? comUrl : "";
        parameters["method"] = method;
        
        parameters["headerInfo"] = reqHeaders;
        parameters["authInfo"] = [];
        parameters["dataType"] = parameters["dataType"];
        parameters["reqStructure"] = reqOriginData;
        parameters["reqSpec"] = reqSpecData;
        parameters["resStructure"] = resOriginData;
        parameters["resSpec"] = resSpecData;
        
        Utils.log(DEBUG, "validationData.parameters", parameters);
        
        // call api
        Connection.send(parameters);
    }
    catch (err) {
    	Utils.log(ERR, "validationData.err", err);
    	alert("전송 요청 중 오류가 발생하였습니다.");
    	return;
    }
    finally {
    	$form, $elm = null;
    }
}