<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<%@ include file="../include/header.jsp" %>
<script src="https://cdn.iamport.kr/v1/iamport.js"></script>
<script type="text/javascript">
function payment(pg_provider, mode, payment_method){
	//STEP 2
	IMP.init('imp83425713');
	var pg_mid;
	
	if(pg_provider=='inicis'){
		if(mode=='test'){
			pg_mid = 'html5_inicis.INIBillTst';
		}else{
			pg_mid = 'html5_inicis.MOI123123';
		}
	}else if(pg_provider=='kcp'){
		if(mode=='test'){
			pg_mid = 'kcp.A52CY';
		}else{
			pg_mid = 'kcp.IOfds34';
		}
	}
	
	alert(pg_mid);
	
	//STEP 3
	const data = {
			pg : pg_mid,
	    pay_method : payment_method,
	    merchant_uid: "57008833-33004", 
	    name : '당근 10kg',
	    amount : 1004,
	    buyer_email : 'Iamport@chai.finance',
	    buyer_name : '포트원 기술지원팀',
	    buyer_tel : '010-1234-5678',
	};
	
	IMP.request_pay(data, response => {
		alert('callback!: '+JSON.stringify(response));
		
		//STEP 4
		jQuery.ajax({
			url: "${path}/shop/cart/pay.do",
			method: "POST",
			headers: { "Content-Type": "application/json" },
			data: JSON.stringify(response)
		}).done(function (data){
			//STEP 6
			alert("Please, Check your payment result page!!");
		});
	});
}

$(function() {
	$("#btnList").click(function(){
		//장바구니 목록으로 이동
		location.href="${path}/shop/product/list.do";
	});
	$("#btnDelete").click(function(){
		if(confirm("장바구니를 비우시겠습니까?")){
			location.href="${path}/shop/cart/deleteAll.do";
		}
	});
});
</script>
</head>
<body>
<%@ include file="../include/menu.jsp" %>
<h2>장바구니</h2>
<c:choose>
 <c:when test="${map.count == 0}">
   장바구니가 비었습니다.
 </c:when>
 <c:otherwise>
  <form name="form1" method="post" action="${path}/shop/cart/update.do">
   <table border="1" style="width: 100%;">
	  <tr>
	    <th>상품명</th>
	    <th>단가</th>
	    <th>수량</th>
	    <th>금액</th>
	    <th>&nbsp;</th>
	  </tr>
	<c:forEach var="row" items="${map.list}"> 
	  <tr>
	    <td>${row.product_name}</td>
	    <td>${row.price}</td>
	    <td> 
	     <input type="number" name="amount" value="${row.amount}">
	     <input type="hidden" name="cart_id" value="${row.cart_id}">
	    </td>
	    <td>${row.money}</td>
	    <td>
	     <c:if test="${sessionScope.userid != null}">
	      <a href="${path}/shop/cart/delete.do?cart_id=${row.cart_id}">삭제</a>
	     </c:if>
	    </td>
	  </tr>
	</c:forEach>  
	  <tr> 
	   <td colspan="5" align="right"> 
	        장바구니 금액 합계 : ${map.sumMoney} <br>
	        배송료 : ${map.fee} <br>
	        총합계 : ${map.sum}
	   </td>
	  </tr>
	</table>
	<button id="btnUpdate">수정</button>
	<button type="button" id="btnDelete">장바구니 비우기</button>
	<button type="button" onclick="payment('kcp','test','card')">kcp test 결제하기</button> <!-- 결제하기 버튼 생성 -->
  <button type="button" onclick="payment('inicis','test','card')">inicis test 결제하기</button>
  </form>
 </c:otherwise>
</c:choose>
<button type="button" id="btnList">상품목록</button>

</body>
</html>