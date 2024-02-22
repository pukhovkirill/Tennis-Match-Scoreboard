<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>New match</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
</head>
<body>
    <jsp:include page="/templates/header.jsp"/>
    <div class="menu" style="height: 235px">
        <h3>New match</h3>
        <form action="/new-match" method="post">
            <input name="player1" type="text" class="input" placeholder="player1" style="margin-bottom: 10px" required/>
            <input name="player2" type="text" class="input" placeholder="player2" style="margin-bottom: 10px" required/>
            <input type="submit" class="submit" value="START"/>
        </form>
    </div>
</body>
</html>