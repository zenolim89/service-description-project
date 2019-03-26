/**
 * Register LOGIN
 */

function login(id,pw) {
	
	if( !(id.length > 0)){
	
		alert('아이디를 입력해주세요.');
	}
	else if(!(pw.length > 0)){
	
		alert('패스워드를 입력해주세요.');
	}
	else{
		
		if( id == "V000001" && pw =="1234"){
			$(location).attr('href', 'http://localhost:8080/NICEKIT/step1');
		}
		else{
			
			alert('아이디 혹은 비밀번호가 일치하지 않습니다.');
		}
	}
}