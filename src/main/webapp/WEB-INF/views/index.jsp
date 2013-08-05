<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html ng-app>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache" />
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/angularjs/1.0.7/angular.min.js"></script>
<script type="text/javascript" src="<c:url value='/resources/scripts/controllers.js' />"></script>
<title>Home</title>
</head>
<body>

<div ng-controller='HelloController'>
    <p>{{greeting.text}}, World</p>
</div>

</body>
</html>
