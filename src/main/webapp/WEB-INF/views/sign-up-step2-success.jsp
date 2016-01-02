<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="common-header-tags.jsp"></jsp:include>
<title>Sign Up</title>
</head>
<body>

<p>Success!</p>

<c:url value="/login" var="loginUrl" />
<p><a href="${loginUrl}">Go to Login</a></p>

</body>
</html>
