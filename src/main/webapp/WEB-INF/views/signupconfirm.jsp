<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache" />
<title>Signup Confirm</title>
</head>
<body>

<c:if test="${error == null}">
  <p>Sweet</p>
</c:if>

<c:if test="${error != null}">
  <p><c:out value="${error}" /><br /><br /></p>
</c:if>

</body>
</html>
