<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache" />

<script type="text/javascript" src="<c:url value='/resources/js/libs/jquery.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/resources/js/libs/angular.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/resources/js/libs/angular-route.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/resources/js/libs/ng-grid-2.0.14.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/resources/js/libs/sockjs.js' />"></script>
<script type="text/javascript" src="<c:url value='/resources/js/libs/jquery-ui/core.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/resources/js/libs/jquery-ui/widget.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/resources/js/libs/jquery-ui/position.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/resources/js/libs/jquery-ui/dialog.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/resources/js/libs/jquery-ui/button.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/resources/js/libs/jquery-ui/mouse.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/resources/js/libs/jquery-ui/draggable.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/resources/js/libs/stomp.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/resources/js/libs/bundle.js' />"></script>

<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/bundle.css' />"></link>

<title>Home</title>
</head>

<body ng-app="flexpoker">

<p>Logged in as: <span class="username"><sec:authentication property="principal.username" /></span></p>

<p ng-controller="LogoutController"><a href='<c:url value="/logout" />' ng-click="logout()">Logout</a></p>

<div ng-controller="MainController">
  <ul class="game-tab-container">
    <li><a href="#/">Home</a></li>
    <li ng-repeat="gameTab in gameTabs" class="game-tab-{{gameTab.gameStage | lowercase}}">
      <a href="#/game/{{gameTab.gameId}}">{{gameTab.name}}</a>
    </li>
  </ul>
</div>

<button ng-click='clearCache()'>Clear cache</button>

<div ng-view></div>

</body>
</html>
