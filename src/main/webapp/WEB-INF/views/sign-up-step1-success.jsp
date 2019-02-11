<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="common-header-tags.jsp"></jsp:include>
<title>Sign Up</title>
</head>
<body>
<jsp:include page="generic-header.jsp"></jsp:include>

<div class="container">
  <h4 class="text-center">Confirm your sign up</h4>

  <p class="text-center">Email sent to <c:out value="${email}" /> (TODO: not working yet)</p>

  <c:url value="/sign-up-confirm?username=${username}" var="signUpConfirmUrl" />
  <p class="text-center"><a href="${signUpConfirmUrl}">Click here to confirm</a></p>
</div>
<jsp:include page="footer.jsp"></jsp:include>
</body>
</html>
