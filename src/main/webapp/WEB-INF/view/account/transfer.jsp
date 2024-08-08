<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!-- header.jsp  -->
<%@ include file="/WEB-INF/view/layout/header.jsp"%>

<!-- start of content.jsp(xxx.jsp)   -->
<div class="col-sm-8">
	<h2>이체 요청(인증)</h2>
	<h5>Bank App에 오신걸 환영합니다</h5>

	<form action="/account/transfer" method="post">
	
		<div class="form-group">
			<label for="a">이체 금액 :</label>
			<input class="form-control" type="text" id="a" name="amount" value="1000">
		
			<label for="b">출금 계좌 번호 : </label>
			<input class="form-control" type="text" id="b" name="wAccountNumber" value="1002-1234">
		
			<label for="c">출금 계좌 비밀 번호 : </label>
			<input class="form-control" type="password" id="c" name="password" value="1234">
		
			<label for="d">입금(이체) 계좌번호 : </label>
			<input class="form-control" type="text" id="d" name="dAccountNumber" value="1111">
		</div>	
		
		<div class="text-right">	
			<button type="submit" class="btn btn-primary">이체하기</button>
		</div>
		
    </form>
</div>
<!-- end of col-sm-8  -->
</div>
</div>
<!-- end of content.jsp(xxx.jsp)   -->

<!-- footer.jsp  -->
<%@ include file="/WEB-INF/view/layout/footer.jsp"%>