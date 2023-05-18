<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:if test="${sessionScope.admin_userid == null}">
 <script>
  alert("로그인 하신 후 사용하세요.");
  location.href="${path}/admin/login.do";
 </script>
</c:if>
