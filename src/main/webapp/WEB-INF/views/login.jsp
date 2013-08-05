<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache" />
<title>Login Page</title>
</head>
<body>

<c:if test="${param.error != null}">
    <font color="red"> Your login attempt was not successful, try again.<br /><br />
    Reason: <c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}" />.</font>
</c:if>

	<h3>Login with Username and Password</h3>
	<form action="<c:url value='login' />" method='POST'>
		User: <input type='text' name='username'>
		Password: <input type='password' name='password' />
		<input name="submit" type="submit" value="Login" />
	</form>
</body>
</html>
