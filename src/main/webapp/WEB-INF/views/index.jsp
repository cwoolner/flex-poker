<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />
<jsp:include page="common-header-tags.jsp"></jsp:include>

<script src="//cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.0.3/sockjs.min.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/babel-core/5.8.34/browser.min.js"></script>
<script src="//rawgit.com/ModuleLoader/es6-module-loader/v0.17.10/dist/es6-module-loader-dev.js"></script>

<script defer>
  System.transpiler = 'babel';
  System.import('resources/index.js').then(
      x => window.dispatchEvent(new Event('hashchange')));
</script>
<script src="/resources/bundle.js" defer></script>

<link rel="import" href="/resources/modules/common/chat.html" />
<link rel="import" href="/resources/modules/game/gamelist.html" />
<link rel="import" href="/resources/modules/table/seat.html" />
<link rel="import" href="/resources/modules/game/game-page.html" />
<link rel="import" href="/resources/modules/game/fp-create-game-dialog.html" />
<link rel="import" href="/resources/modules/game/fp-join-game-dialog.html" />
<link rel="import" href="/resources/modules/home/fp-main-page.html" />
<link rel="import" href="/resources/modules/home/fp-main-tabs.html" />
<link rel="import" href="/resources/modules/table/fp-table-page.html" />

<title>Home</title>
</head>

<body>

<div id="app"></div>

<p>Logged in as: <span class="username"><sec:authentication property="principal.username" /></span></p>

<p><a href="/#logout">Logout</a></p>

<fp-main-tabs></fp-main-tabs>

<div id="view-area"></div>

</body>
</html>
