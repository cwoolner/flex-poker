<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="common-header-tags.jsp"></jsp:include>
<title>Sign Up</title>
</head>
<body>

<div class="container">
  <h3 class="text-center">Confirmed!</h3>

  <c:url value="/login" var="loginUrl" />
  <p class="text-center"><a href="${loginUrl}">Go to Login</a></p>
</div>
<jsp:include page="footer.jsp"></jsp:include>
</body>
</html>
