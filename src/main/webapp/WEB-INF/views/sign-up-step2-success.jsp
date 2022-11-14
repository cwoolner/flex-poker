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
  <h1 class="h3 mb-3 fw-normal">Confirmed!</h1>
  <c:url value="/login" var="loginUrl" />
  <p><a href="${loginUrl}">Go to Login</a></p>
</main>
<jsp:include page="footer.jsp"></jsp:include>
</body>
</html>
