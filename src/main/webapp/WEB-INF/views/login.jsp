<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="common-header-tags.jsp"></jsp:include>
<title>Login Page</title>
</head>
<body>
<jsp:include page="generic-header.jsp"></jsp:include>

<div class="container">
  <form class="standard-form" action="/login" method="POST">
    <h3>Log in to Flex Poker</h3>

    <c:if test="${param.error != null}">
      <p class="alert alert-danger">Invalid username and password.</p>
    </c:if>

    <input type="text" name="username" class="form-control" placeholder="Username" required autofocus />
    <input type="password" name="password" class="form-control" placeholder="Password" required />

    <div class="checkbox">
      <label><input type="checkbox" value="remember-me">Remember me</label>
    </div>

    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    <input type="submit" class="btn btn-lg btn-primary btn-block" value="Log In" />
  </form>

  <div class="row row-top-buffer">
    <div class="center-block text-center">
        Don't have an account? <a href="<c:url value='sign-up' />">Sign Up!</a>
    </div>
  </div>

</div>
<jsp:include page="footer.jsp"></jsp:include>
</body>
</html>
