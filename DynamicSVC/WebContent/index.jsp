<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h3>서비스 등록기</h3>
<form action="queryTest" method="POST">
도메인 코드 : <input type="text" name="domain"/><br/>
참고 어휘사전 : <input type="text" name="dictionary"/><br/>
서비스 제공 타입 : <input type="text" name="svcType"/><br/>
서비스 설명 : <input type="text" name="svcDescription"/><br/>
서비스 인텐트 : <input type="text" name="svcIntent"/><br/>
타켓 URL : <input type="text" name="targetURL"/><br/>
메소드 : <input type="text" name="method"/><br/>
데이터 타입 : <input type="text" name="dataType"/><br/>
데이터 상세 : <input type="text" name="dataDescription"/><br/>
<input type="SUBMIT" value="전송">
<input type="RESET" value="지우기">
</form>
</body>
</html>