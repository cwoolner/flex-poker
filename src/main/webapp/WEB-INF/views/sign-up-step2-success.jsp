<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!doctype html>
<html lang="en">
<head>
<jsp:include page="common-header-tags.jsp"></jsp:include>
<title>Sign Up</title>
</head>
<body class="text-center">
<jsp:include page="generic-header.jsp"></jsp:include>

<div class="container">
  <h4 class="text-center">Confirmed!</h4>

  <c:url value="/login" var="loginUrl" />
  <p class="text-center"><a href="${loginUrl}">Go to Login</a></p>
</div>
<jsp:include page="footer.jsp"></jsp:include>
</body>
</html>
