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
  <form class="standard-form" action="<c:url value='sign-up-confirm' />" method="POST">
    <h1 class="h3 mb-3 fw-normal">Sign Up</h1>

    <c:if test="${error != null}">
      <p class=" error-message">${error}</p>
    </c:if>

    <div class="form-floating mb-3">
      <input type="text" name="username" id="floatingUsername" class="form-control" placeholder="Username" required autofocus />
      <label for="floatingUsername">Username</label>
    </div>

    <input type="hidden" name="signUpCode" value="${signUpCode}" />
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

    <button name="submit" type="submit" class="w-100 btn btn-lg btn-primary">Confirm Sign Up</button>
  </form>
</main>
<jsp:include page="footer.jsp"></jsp:include>
</body>
</html>
