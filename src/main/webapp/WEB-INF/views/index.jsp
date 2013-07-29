<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache" />
<title>Flex Poker</title>
</head>
<body>
	<form name='f' action="<c:url value='j_spring_security_check' />" method='POST'>
        <div class="form-item">
            <label>User:</label>
            <input type='text' name='j_username' />
        </div>
        <div class="form-item">
            <label>Password:</label>
            <input type='password' name='j_password' />
        </div>
        <div class="form-item">
            <input name="submit" type="submit" value="submit" />
        </div>
	</form>
</body>
</html>
