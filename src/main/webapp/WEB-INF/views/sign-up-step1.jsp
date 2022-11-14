<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!doctype html>
<html lang="en">
<head>
<jsp:include page="common-header-tags.jsp"></jsp:include>
<title>Sign Up</title>
</head>
<body class="text-center">
<jsp:include page="generic-header.jsp"></jsp:include>

<main class="container">
  <form class="standard-form" action="<c:url value='sign-up' />" method="POST">
    <h1 class="h3 mb-3 fw-normal">Sign Up</h1>

    <c:if test="${error != null}">
      <p class=" error-message">${error}</p>
    </c:if>

    <div class="form-floating">
      <input type="text" name="username" id="floatingUsername" class="form-control" placeholder="Username" required autofocus />
      <label for="floatingUsername">Username</label>
    </div>
    <div class="form-floating">
      <input type="password" name="password" id="floatingPassword" class="form-control" placeholder="Password" required />
      <label for="floatingPassword">Password</label>
    </div>
    <div class="form-floating mb-3">
      <input type="email" name="emailAddress" id="floatingEmail" class="form-control" placeholder="Email" required />
      <label for="floatingEmail">Email</label>
    </div>

    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    <button type="submit" class="w-100 btn btn-lg btn-primary">Sign Up</button>
  </form>
</main>
<jsp:include page="footer.jsp"></jsp:include>
</body>
</html>
