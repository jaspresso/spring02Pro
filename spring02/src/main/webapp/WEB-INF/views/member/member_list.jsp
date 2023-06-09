<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<%@ include file="../include/header.jsp" %>
</head>
<body>
<%@ include file="../include/menu.jsp" %>
<h2>회원목록</h2>
<input type="button" value="회원등록(일반)" 
onclick="location.href='${path}/member/write.do'">
<table border="1" style="width: 100%">
  <tr>
    <th>아이디</th>
    <th>이름</th>
    <th>이메일</th>
    <th>우편번호</th>
    <th>도로명주소</th>
    <th>상세주소</th>
    <th>가입일자</th>
    <th>회원삭제</th>
  </tr>
<c:forEach var="row" items="${list}"> 
  <tr>
    <td>${row.userid}</td>
    <td><a href="${path}/member/view.do?userid=${row.userid}">${row.name}</a></td>
    <td>${row.email}</td>
    <td>${row.zipcode}</td>
    <td>${row.address1}</td>
    <td>${row.address2}</td>
    <td><fmt:formatDate value="${row.join_date}" pattern="yyyy-MM-dd HH:mm:ss" /> </td>
    <td><input type="button" value="회원삭제" 
    onclick="if(confirm('정말 삭제하시겠습니까?')){location.href='${path}/member/delete.do?userid=${row.userid}'}"></td>
  </tr>
</c:forEach>   
</table>
</body>
</html>