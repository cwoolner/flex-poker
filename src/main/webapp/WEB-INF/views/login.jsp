<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!doctype html>
<html lang="en">
<head>
<jsp:include page="common-header-tags.jsp"></jsp:include>
<title>Login Page</title>
</head>
<body class="text-center">
<jsp:include page="generic-header.jsp"></jsp:include>

<main class="container">
  <form class="standard-form" action="<c:url value='login' />" method="POST">
    <h1 class="h3 mb-3 fw-normal">Log in to Flex Poker</h1>

    <c:if test="${param.error != null}">
      <p class="alert alert-danger">Invalid username and password.</p>
    </c:if>

    <div class="form-floating">
      <input type="text" name="username" id="flaotingUsername" class="form-control" placeholder="Username" required autofocus />
      <label for="floatingUsername">Username</label>
    </div>
    <div class="form-floating mb-3">
      <input type="password" name="password" id="floatingPassword" class="form-control" placeholder="Password" required />
      <label for="floatingPassword">Password</label>
    </div>

    <div class="checkbox mb-3">
      <label><input type="checkbox" value="remember-me" style="margin-right: 6px;">Remember me</label>
    </div>

    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    <button type="submit" class="w-100 btn btn-lg btn-primary">Log In</button>
  </form>

  <div class="row row-top-buffer">
    <div class="text-center" style="margin: 0 auto 0 auto;">
        Don't have an account? <a href="<c:url value='sign-up' />">Sign Up!</a>
    </div>
  </div>

</main>
<jsp:include page="footer.jsp"></jsp:include>
</body>
</html>
