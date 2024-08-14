<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!-- header.jsp  -->
<%@ include file="/WEB-INF/view/layout/header.jsp"%>

<!-- start of content.jsp(xxx.jsp)   -->
<div class="col-sm-8">
	<h2>회원 가입</h2>
	<h5>Bank App에 오신걸 환영합니다.</h5>
	<!--  주소 설계 : http://localhost:8080/user/sign-up -->

	<form action="/user/sign-up" method="post" enctype="multipart/form-data">
	
		<div class="form-group">
			<label for="username">username :</label>
			<input type="text" class="form-control" placeholder="Enter username" id="username" name="username" value="야스오1">
		</div>
		
		<div class="form-group">
			<label for="pwd">Password :</label>
			<input type="password" class="form-control" placeholder="Enter password" id="pwd" name="password" value="1234">
		</div>
		
		<div class="form-group form-check">
			<label class="form-check-label">
			<input class="form-check-input" type="checkbox"> Remember me
			</label>
		</div>
		
		<div class="form-group">
			<label for="fullname">fullname :</label>
			<input type="text" class="form-control" placeholder="Enter fullname" id="fullname" name="fullname" value="구름">
		</div>
		
		<br>
		
		<div class="custom-file">
    		<input type="file" class="custom-file-input" id="customFile" name="mFile">
    		<label class="custom-file-label" for="customFile">Choose file</label>
  		</div>
  		
  		<br>

		<div class="d-flex justify-content-end">
			<button type="submit" class="btn btn-primary mt-md-4 ">회원가입</button>
		</div>
		
		<div>
			 <a href="https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=4338cd3d11df8f8f1654fff2e37bd659
&redirect_uri=http://localhost:8080/user/kakao
			 "><img alt="소셜로그인 이미지" src="/images/kakao_login_small.png"></a>
		</div>
		
	</form>

</div>

</div>
</div>
<!-- end of content.jsp(xxx.jsp)   -->

<script>
// Add the following code if you want the name of the file appear on select
$(".custom-file-input").on("change", function() {
  let fileName = $(this).val().split("\\").pop();
  $(this).siblings(".custom-file-label").addClass("selected").html(fileName);
});
</script>

<!-- footer.jsp  -->
<%@ include file="/WEB-INF/view/layout/footer.jsp"%>
