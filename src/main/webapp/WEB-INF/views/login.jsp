<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta name="viewport" content="width=device-width, initial-scale=1">

<script src="//cdnjs.cloudflare.com/ajax/libs/jquery/2.1.4/jquery.min.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.6/js/bootstrap.min.js"></script>

<link rel="shortcut icon" href="/resources/img/favicon.png" />
<link rel="stylesheet" type="text/css" href="//cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.6/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" href="/resources/css/main.css" />

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
