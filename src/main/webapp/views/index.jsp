<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Home</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
</head>
<body>
    <jsp:include page="/templates/header.jsp"/>
    <div class="menu">
        <h3>Tennis scoreboard</h3>
        <div><a href="/new-match">NEW</a> - start new match</div>
        <div><a href="/matches">MATCHES</a> - list of finished matches</div>
    </div>
</body>
</html>