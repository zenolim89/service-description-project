/*
 * Created by zenolim on 2018
 */

// 로그인 성공 시 헤더 UI 변경
function LoginDisplay(loginResult) {
	var loginForm = document.getElementById("loginForm");
	var loginCompl = document.getElementById("loginCompl");

	if (loginForm.style.display == 'none') {
		loginForm.style.display = 'block';
		loginCompl.style.display = 'none';
	}
	else {
		loginForm.style.display = 'none';
		loginCompl.style.display = 'block';
	}
	// 계정에 등록된 서비스 목록으로 네비게이션 영역 변경 
	var ul = document.getElementById('svcListInfo');
	if (ul)
		while (ul.firstChild) {
			ul.removeChild(ul.firstChild);
		}

	var jsonObj = JSON.parse(loginResult);
	for (var i = 0; i < jsonObj.ServiceNum; i++) {
		var svcCodeName = jsonObj.regiServiceInfo[i].serviceCode;
		var li = document.createElement("li");
		li.innerHTML = "<li class='nav-item'><a class='nav-link'>" + svcCodeName + "</a></li>";
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
