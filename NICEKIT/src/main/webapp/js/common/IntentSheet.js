var rABS = true; // T : 바이너리, F : 어레이 버퍼

var tempInfo = new Array();
var IntentInfo = new Array();
var dicList = new Array();

// 어레이 버퍼를 처리한다 ( 오직 readAsArrayBuffer 데이터만 가능하다 )
function fixdata(data) {
	var o = "", l = 0, w = 10240;
	for (; l < data.byteLength / w; ++l)
		o += String.fromCharCode.apply(null, new Uint8Array(data.slice(l * w, l * w + w)));
	o += String.fromCharCode.apply(null, new Uint8Array(data.slice(l * w)));
	return o;
}

// 데이터를 바이너리 스트링으로 얻는다.
function getConvertDataToBin($data) {
	var arraybuffer = $data;
	var data = new Uint8Array(arraybuffer);
	var arr = new Array();
	for (var i = 0; i != data.length; ++i)
		arr[i] = String.fromCharCode(data[i]);
	var bstr = arr.join("");
	return bstr;
}

function handleFile(e) {
	var files = e.target.files;
	var i, f;
	for (i = 0; i != files.length; ++i) {
		f = files[i];
		var reader = new FileReader();
		var name = f.name;
		reader.onload = function(e) {
			var data = e.target.result;
			var workbook;
			if (rABS) {
				/* if binary string, read with type 'binary' */
				workbook = XLSX.read(data, {
					type : 'binary'
				});
			}
			else {
				/* if array buffer, convert to base64 */
				var arr = fixdata(data);
				workbook = XLSX.read(btoa(arr), {
					type : 'base64'
				});
			}// end. if

			/* 워크북 처리 */
			workbook.SheetNames.forEach(function(item, index, array) {

				var json = XLSX.utils.sheet_to_json(workbook.Sheets[item]);
// var csv = XLSX.utils.sheet_to_csv(workbook.Sheets[item]);
// var html = XLSX.utils.sheet_to_html(workbook.Sheets[item]);
// var formulae = XLSX.utils .sheet_to_formulae(workbook.Sheets[item]);

				if (index == 1)
					setIntentSheet(json);
				else if (index == 2)
					setIntentDataforReg(json);

				$("#my_file_output").html(JSON.stringify(IntentInfo));
			});// end. forEach
		}; // end onload

		if (rABS)
			reader.readAsBinaryString(f);
		else
			reader.readAsArrayBuffer(f);

	}// end. for
}

function setIntentSheet(json) {
	for (var i = 0; i < json.length; i++) {
		var intentObj = new Object();
		var from = json[i]['Example'].indexOf('{') + 1;
		var to = json[i]['Example'].indexOf('}');
		var dicNames = (from == 0 || to == -2) ? dicNames = json[i]['Example']
					: dicNames = json[i]['Example'].substring(from, to);

		intentObj["desc"] = json[i]['Function'];
		intentObj["id"] = json[i]['Intent'];
		intentObj["ex"] = dicNames;
		tempInfo.push(intentObj);
	}
	//console.log(JSON.stringify(tempInfo));
}

function getDicList(extra, dicjson) {
	var owndicList = new Array();
	var pivot = extra.split('|');
	
	for (var i = 0; i < pivot.length; i++) {
		var dicObj = new Object();
		dicObj['dicName'] = pivot[i];
		var wordList = new Array();
		for (var j = 0; j < dicjson.length; j++) {
			if (pivot[i] == dicjson[j]['Parameter']) {
				var wordObj = new Object();
				wordObj['word'] = dicjson[j]['식별값'];
				wordList.push(wordObj);
			}
			dicObj['wordList'] = wordList;
		}
		owndicList.push(dicObj);
	}

	return owndicList;
}

function setIntentDataforReg(json) {
	for (var i = 0; i < tempInfo.length; i++) {
		var IntentObj = new Object();
		IntentObj['id'] = tempInfo[i]['id'];
		IntentObj['desc'] = tempInfo[i]['desc'];
		IntentObj['dicList'] = getDicList(tempInfo[i]['ex'], json);
		IntentInfo.push(IntentObj);
	}
	//console.log(JSON.stringify(IntentInfo));
}

function getIntentDataforReg() {
	return JSON.stringify(IntentInfo);
}

var input_dom_element;
$(function() {
	input_dom_element = document.getElementById('my_file_input');
	if (input_dom_element.addEventListener) {
		input_dom_element.addEventListener('change', handleFile, false);
	}
});
