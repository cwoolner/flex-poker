<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache" />

<script src="//cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.0.3/sockjs.min.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/babel-core/4.7.16/browser.js"></script>
<script src="/resources/scripts/es6-module-loader-dev.js"></script>
<script>
  System.transpiler = 'babel';
  System.import('resources/scripts/main.js');
</script>

<link rel="stylesheet" type="text/css" href="/resources/css/main.css" />
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
