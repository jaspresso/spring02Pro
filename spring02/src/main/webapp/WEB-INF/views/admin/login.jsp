<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<%@ include file="../include/header.jsp" %>
<script>
$(function(){
	$("#btnLogin").click(function(){
		var userid=$("#userid").val();
		var passwd=$("#passwd").val();
		if(userid==""){
			alert("아이디를 입력하세요.");
			$("#userid").focus();
			return;
		}
		if(passwd==""){
			alert("비밀번호를 입력하세요.");
			$("#passwd").focus();
			return;
		}
		document.form1.action="${path}/admin/login_check.do";
		document.form1.submit();
	});
});
</script>
</head>
<body>
<%@ include file="../include/admin_menu.jsp" %>
<h2>관리자 로그인</h2>
<form name="form1" method="post">
<table border="1" style="width: 100%;">
	<tr>
		<td>아이디</td>
		<td><input name="userid" id="userid"></td>
	</tr>
	<tr>
		<td>비밀번호</td>
		<td><input type="password" name="passwd" id="passwd"></td>
	</tr>
	<tr>
		<td colspan="2" align="center">
			<input type="button" id="btnLogin" value="로그인">
			<!-- 여기서 쓰인 param은  querystring으로 값이 들어올때 요청매개변수인 message앞에 써야 한다.
			ex)"/member/login.do?message=nologin" -->
			<c:if test="${param.message == 'nologin' }">
				<div style="color:red;">
					로그인 하신 후 사용하세요.
				</div>				
			</c:if>
			<!-- 위처럼 param을 안쓰고 요청매개변수만 쓰는 경우는 ModelAndView의 addObject등을 통해
			들어온 경우이다. ex)mav.addObject("message", "error");-->
			<c:if test="${message == 'error' }">
				<div style="color:red;">
					아이디 또는 비밀번호가 일치하지 않습니다.
				</div>
			</c:if>
			<c:if test="${message == 'logout' }">
				<div style="color:blue;">
					로그아웃 처리되었습니다.
				</div>
			</c:if>
		</td>
	</tr>
</table>
</form>
</body>
</html>
