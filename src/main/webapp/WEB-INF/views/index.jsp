<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache" />
<script type="text/javascript">
var rootUrl = "<c:url value='/' />";
</script>
<script type="text/javascript" src="http://code.jquery.com/jquery-2.0.3.min.js"></script>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/angularjs/1.0.7/angular.min.js"></script>
<script type="text/javascript" src="http://cdnjs.cloudflare.com/ajax/libs/ng-grid/2.0.7/ng-grid.min.js"></script>
<script type="text/javascript" src="http://cdn.sockjs.org/sockjs-0.3.4.min.js"></script>
<script type="text/javascript" src="<c:url value='/resources/scripts/3rdparty/stomp.js' />"></script>
<script type="text/javascript" src="<c:url value='/resources/scripts/3rdparty/angular-stomp.js' />"></script>
<script type="text/javascript" src="<c:url value='/resources/scripts/main.js' />"></script>
<script type="text/javascript" src="<c:url value='/resources/scripts/routes.js' />"></script>

<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/ng-grid.min.css' />"></link>
<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/main.css' />"></link>

<title>Home</title>
</head>

<body ng-app="flexpoker">

<div ng-view></div>

</body>
</html>
