<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache" />
<title>Flex Poker</title>
</head>
<body>
    <c:forEach items="${allGames}" var="game">
       <p>${game.totalPlayers}</p>
    </c:forEach>
</body>
</html>