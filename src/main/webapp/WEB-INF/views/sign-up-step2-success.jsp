<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache" />
<title>Signup Success</title>
</head>
<body>

<p>Success!</p>

<c:url value="/login" var="loginUrl" />
<p><a href="${loginUrl}">Go to Login</a></p>

</body>
</html>
