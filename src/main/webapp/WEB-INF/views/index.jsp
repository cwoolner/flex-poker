<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />
<jsp:include page="common-header-tags.jsp"></jsp:include>

<script src="/resources/vendor.bundle.js" defer></script>
<script src="/resources/bundle.js" defer></script>
<script>window.username = '<sec:authentication property="principal.username" />'</script>

<title>Home</title>
</head>

<body>
  <div id="app"></div>
  <jsp:include page="footer.jsp"></jsp:include>
</body>
</html>
