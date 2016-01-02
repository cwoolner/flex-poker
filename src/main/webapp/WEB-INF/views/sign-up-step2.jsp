<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="common-header-tags.jsp"></jsp:include>
<title>Sign Up</title>
</head>
<body>

<form action="<c:url value='sign-up-confirm' />" method='POST'>

  <input type="hidden" name="signUpCode" value="${signUpCode}" />

  <div>
    <label>User:</label> <input type="text" name="username" />
  </div>

  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

  <div>
    <input name="submit" type="submit" value="Confirm Sign Up" />
  </div>

</form>

</body>
</html>
