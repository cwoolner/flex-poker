<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache" />
<script type="text/javascript">
var rootUrl = "<c:url value='/' />";
</script>
<script type="text/javascript" src="//code.jquery.com/jquery-2.0.3.min.js"></script>
<script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/angularjs/1.0.7/angular.min.js"></script>
<script type="text/javascript" src="//cdnjs.cloudflare.com/ajax/libs/ng-grid/2.0.7/ng-grid.min.js"></script>
<script type="text/javascript" src="//cdn.sockjs.org/sockjs-0.3.4.min.js"></script>
<script type="text/javascript" src="//codeorigin.jquery.com/ui/1.10.3/jquery-ui.min.js"></script>
<script type="text/javascript" src="<c:url value='/resources/scripts/3rdparty/stomp.js' />"></script>
<script type="text/javascript" src="<c:url value='/resources/scripts/3rdparty/angular-stomp.js' />"></script>

<script type="text/javascript" src="<c:url value='/resources/scripts/main.js' />"></script>
<script type="text/javascript" src="<c:url value='/resources/scripts/controllers/gameController.js' />"></script>
<script type="text/javascript" src="<c:url value='/resources/scripts/controllers/tableController.js' />"></script>
<script type="text/javascript" src="<c:url value='/resources/scripts/controllers/mainController.js' />"></script>
<script type="text/javascript" src="<c:url value='/resources/scripts/controllers/tournamentRegisteringController.js' />"></script>
<script type="text/javascript" src="<c:url value='/resources/scripts/controllers/logoutController.js' />"></script>
<script type="text/javascript" src="<c:url value='/resources/scripts/routes.js' />"></script>

<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/ng-grid.min.css' />"></link>
<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/main.css' />"></link>
<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/smoothness/jquery-ui-1.10.3.custom.min.css' />"></link>

<title>Home</title>
</head>

<body ng-app="flexpoker">

<p>Logged in as: <sec:authentication property="principal.username" /></p>

<p ng-controller="LogoutController"><a href='<c:url value="/logout" />' ng-click="logout()">Logout</a></p>

<div ng-controller="MainController">
  <ul class="game-tab-container">
    <li><a href="#/">Home</a></li>
    <li ng-repeat="gameTab in gameTabs" class="game-tab-{{gameTab.gameStage | lowercase}}">
      <a href="#/game/{{gameTab.gameId}}">{{gameTab.name}}</a>
    </li>
  </ul>
</div>

<div ng-view></div>

</body>
</html>
