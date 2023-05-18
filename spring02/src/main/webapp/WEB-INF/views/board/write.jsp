<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<%@ include file="../include/header.jsp" %>
<script src="${path}/include/js/common.js"></script>
<!-- ckeditor의 라이브러리 -->
<script src="${path}/ckeditor/ckeditor.js"></script>

<script type="text/javascript">
$(function() {
	$("#btnSave").click(function(){
		var str="";
		//uploadedList영역에 클래스 이름이 file인 히든타입의 태그를 각각 반복
		$("#uploadedList .file").each(function(i){
			console.log(i);
			//hidden태그 구성
			str += "<input type='hidden' name='files["+i+"]' value='" 
			+ $(this).val()+"'>";
		});
		//폼에 hidden 태그를 붙임
		$("#form1").append(str);
		document.form1.submit();
	});
	//파일을 마우스로 드래그해서 업로드 영역에 올릴때 파일이 열리는 기본효과 막는 처리
	$(".fileDrop").on("dragenter dragover", function(e) {
		e.preventDefault();
	});
	//마우스로 파일을 드롭할 때 파일이 열리는 기본 효과 막음
	$(".fileDrop").on("drop", function(e){
		e.preventDefault();
		//첫번째 첨부 파일
		var files=e.originalEvent.dataTransfer.files;
		var file=files[0];
		//폼 데이터에 첨부파일 추가
		var formData=new FormData();
		formData.append("file",file);
		$.ajax({
			url: "${path}/upload/uploadAjax",
			data: formData,
			dataType: "text",
			processData: false,
			contentType: false,
			type: "post",
			success: function(data){
				//data: 업로드한 파일 정보와 Http 상태 코드
				var fileInfo=getFileInfo(data);
			  console.log(fileInfo);
			  var html="<a href='"+fileInfo.getLink+"'>"+fileInfo.fileName+"</a><br>";
			  html += "<input type='hidden' class='file' value='"+fileInfo.fullName+"'>";
			  $("#uploadedList").append(html);
			}
		});
	});
});
</script>
<style type="text/css">
.fileDrop {
	width: 600px;
	height: 100px;
	border: 1px dotted gray;
	background-color: gray;
}
</style>
</head>
<body>
<%@ include file="../include/menu.jsp" %>
<h2>글쓰기</h2>
<form id="form1" name="form1" method="post" action="${path}/board/insert.do">
 <div>제목 <input name="title" id="title" size="80" placeholder="제목을 입력하세요"></div>
 <div style="width: 800px">
   내용 <textarea id="content" name="content" rows="3" cols="80" 
   placeholder="내용을 입력하세요"></textarea>
   <script>
   //ckeditor 적용
   CKEDITOR.replace("content", {
	   filebrowserUploadUrl: "${path}/imageUpload.do"
   });// ImageUploadController.java에서 처리
   </script>
 </div>
 <div> 첨부파일을 등록하세요
  <div class="fileDrop"></div>
  <div id="uploadedList"></div>
 </div>
 <div style="width: 700px; text-align: center;">
  <button type="button" id="btnSave">확인</button>
 </div>
</form>

</body>
</html>