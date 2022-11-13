<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!doctype html>
<html lang="en">
<head>
<jsp:include page="common-header-tags.jsp"></jsp:include>
<title>Login Page</title>
</head>
<body class="text-center">
<jsp:include page="generic-header.jsp"></jsp:include>

<div class="container">
  <form class="standard-form" action="/login" method="POST">
    <h4>Log in to Flex Poker</h4>

    <c:if test="${param.error != null}">
      <p class="alert alert-danger">Invalid username and password.</p>
    </c:if>

    <input type="text" name="username" class="form-control" placeholder="Username" required autofocus />
    <input type="password" name="password" class="form-control" placeholder="Password" required />

    <div style="margin-top: 10px;">
      <label><input type="checkbox" value="remember-me" style="margin-right: 6px;">Remember me</label>
    </div>

    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    <input type="submit" class="btn btn-lg btn-primary btn-block" value="Log In" />
  </form>

  <div class="row row-top-buffer">
    <div class="text-center" style="margin: 0 auto 0 auto;">
        Don't have an account? <a href="<c:url value='sign-up' />">Sign Up!</a>
    </div>
  </div>

</div>
<jsp:include page="footer.jsp"></jsp:include>
</body>
</html>
