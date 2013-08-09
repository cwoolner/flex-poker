<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache" />
<script type="text/javascript" src="http://code.jquery.com/jquery-2.0.3.min.js"></script>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/angularjs/1.0.7/angular.min.js"></script>
<script type="text/javascript" src="<c:url value='/resources/scripts/main.js' />"></script>
<script type="text/javascript" src="http://cdn.sockjs.org/sockjs-0.3.4.min.js"></script>
<script type="text/javascript" src="<c:url value='/resources/scripts/3rdparty/stomp.js' />"></script>
<script type="text/javascript" src="<c:url value='/resources/scripts/websocket-connect.js' />"></script>

<script type="text/javascript">
    var rootUrl = "<c:url value='/' />";
    $(function() {
        var socket = new SockJS(rootUrl + '<c:url value="application" />');
        var stompClient = Stomp.over(socket);
        var stompConnection = new StompConnection(stompClient);
        stompConnection.connect();
    });
</script>

<title>Home</title>
</head>
<body ng-app="flexpoker">

<div ng-controller='HelloController'>
    <p>{{greeting.text}}, World</p>
</div>

<div id="tournament-registering" ng-controller='TournamentRegisteringController'>
  <table>
    <thead>
      <tr>
        <th>Name</th>
        <th>Players</th>
        <th>Per Table</th>
      </tr>
    </thead>
    <tr ng-repeat="game in games">
        <td>{{game.name}}</td>
        <td>{{game.numberOfRegisteredPlayers}}/{{game.maxNumberOfPlayers}}</td>
        <td>{{game.maxPlayersPerTable}}</td>
    </tr>
  </table>
</div>

</body>
</html>
