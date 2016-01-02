<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="common-header-tags.jsp"></jsp:include>
<title>Login Page</title>
</head>
<body>

<div class="container">
  <form class="form-signin" action="/login" method="POST">
    <h2 class="form-signin-heading">Please sign in</h2>
    <c:if test="${param.error != null}">
        <p class="error-message">Invalid username and password.</p>
    </c:if>

    <label for="inputEmail" class="sr-only">Email address</label>
    <input name="username" class="form-control" placeholder="Username" required autofocus>

    <label for="inputPassword" class="sr-only">Password</label>
    <input type="password" name="password" class="form-control" placeholder="Password" required>

    <div class="checkbox">
      <label><input type="checkbox" value="remember-me">Remember me</label>
    </div>

    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

    <button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
  </form>

  <br />

  <div class="row">
    <div class="center-block text-center">
        <a href="<c:url value='sign-up' />">Don't have an account?  Sign Up!</a>
    </div>
  </div>

</div>

</body>
</html>
