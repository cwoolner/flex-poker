<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!doctype html>
<html lang="en">
<head>
<jsp:include page="common-header-tags.jsp"></jsp:include>
<title>Sign Up</title>
</head>
<body class="text-center">
<jsp:include page="generic-header.jsp"></jsp:include>

<main class="container">
  <h1 class="h3 mb-3 fw-normal">Confirm your sign up</h1>

  <p>Email sent to <c:out value="${email}" /> (TODO: not working yet)</p>

  <c:url value="/sign-up-confirm?username=${username}" var="signUpConfirmUrl" />
  <p><a href="${signUpConfirmUrl}">Click here to confirm</a></p>
</main>
<jsp:include page="footer.jsp"></jsp:include>
</body>
</html>
