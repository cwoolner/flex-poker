<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="common-header-tags.jsp"></jsp:include>
<title>Sign Up</title>
</head>
<body>
<jsp:include page="generic-header.jsp"></jsp:include>

<div class="container">
  <form class="standard-form" action="<c:url value='sign-up' />" method='POST'>
    <h3>Sign Up</h3>
    <c:if test="${error != null}">
      <p class=" error-message">${error}</p>
    </c:if>
    <input type="text" name="username" class="form-control" placeholder="Username" required autofocus />
    <input type="password" name="password" class="form-control" placeholder="Password" required />
    <input type="email" name="emailAddress" class="form-control" placeholder="Email" required />
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    <div class="row-top-buffer">
      <input name="submit" type="submit" class="btn btn-lg btn-primary btn-block" value="Sign Up" />
    </div>
  </form>
</div>
<jsp:include page="footer.jsp"></jsp:include>
</body>
</html>
