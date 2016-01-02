<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache" />
<title>Signup Confirm</title>
</head>
<body>

<form action="<c:url value='sign-up-confirm' />" method='POST'>

  <input type="hidden" name="signUpCode" value="${signUpCode}" />

  <div>
    <label>User:</label> <input type="text" name="username" />
  </div>

  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

  <div>
    <input name="submit" type="submit" value="Login" />
  </div>

</form>

</body>
</html>
