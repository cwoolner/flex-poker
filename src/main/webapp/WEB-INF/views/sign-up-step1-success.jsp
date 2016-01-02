<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="common-header-tags.jsp"></jsp:include>
<title>Sign Up</title>
</head>
<body>

<p>Email sent to <c:out value="${email}" /> (TODO: not working yet)</p>

<c:url value="/sign-up-confirm?username=${username}" var="signUpConfirmUrl" />
<p><a href="${signUpConfirmUrl}">Click here to confirm</a></p>

</body>
</html>
