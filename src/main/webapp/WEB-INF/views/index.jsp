<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache" />

<script type="text/javascript" src="<c:url value='/resources/js/libs/sockjs.js' />"></script>
<script type="text/javascript" src="<c:url value='/resources/js/libs/stomp.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/resources/js/libs/bundle.js' />"></script>

<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/dist/bundle.css' />" />

<link rel="import" href="/resources/fp-chat/chat.html" />
<link rel="import" href="/resources/fp-gamelist/gamelist.html" />
<link rel="import" href="/resources/fp-seat/seat.html" />
<link rel="import" href="/resources/game-page/game-page.html" />
<link rel="import" href="/resources/fp-create-game-dialog.html" />
<link rel="import" href="/resources/fp-join-game-dialog.html" />
<link rel="import" href="/resources/fp-main-page.html" />
<link rel="import" href="/resources/fp-main-tabs.html" />
<link rel="import" href="/resources/fp-table-page.html" />

<title>Home</title>
</head>

<body>

<p>Logged in as: <span class="username"><sec:authentication property="principal.username" /></span></p>

<p><a href="/#logout">Logout</a></p>

<fp-main-tabs></fp-main-tabs>

<div id="view-area"></div>

</body>
</html>
