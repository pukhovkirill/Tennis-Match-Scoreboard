<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Match score</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/newScore.css">
</head>
<body>
    <c:set var="matchScore" value="${requestScope.match}" scope="request"/>
    <jsp:include page="/templates/header.jsp"/>
    <div class="menu" style="width: 850px; display: table; padding: 15px">
        <h3>Match score</h3>
        <table>
            <tr>
                <th style="width: 300px">Player</th>
                <th style="width: 150px">Set</th>
                <th style="width: 150px">Games</th>
                <th style="width: 150px">Points</th>
            </tr>
            <tr>
                <td><c:out value="${matchScore.match.firstPlayer.name}"/></td>
                <td><c:out value="${matchScore.gameScore.setScore.firstPlayerSet}"/></td>
                <td><c:out value="${matchScore.gameScore.firstPlayerGame}"/></td>
                <td><c:out value="${matchScore.firstPlayerScore}"/></td>
            </tr>
            <tr>
                <td><c:out value="${matchScore.match.secondPlayer.name}"/></td>
                <td><c:out value="${matchScore.gameScore.setScore.secondPlayerSet}"/></td>
                <td><c:out value="${matchScore.gameScore.secondPlayerGame}"/></td>
                <td><c:out value="${matchScore.secondPlayerScore}"/></td>
            </tr>
        </table>
        <div style="display: flex; justify-content: space-between; text-align: center; margin-top: 10px">
            <form method="POST" action="/match-score?uuid=${matchScore.uuid}">
                <input type="hidden" name="winner" value="${matchScore.match.firstPlayer.id}">
                <button type="submit" class="award" style="width: 100%">Player1 wins point</button>
            </form>
            <form method="POST" action="/match-score?uuid=${matchScore.uuid}">
                <input type="hidden" name="winner" value="${matchScore.match.secondPlayer.id}">
                <button type="submit" class="award" style="width: 100%">Player2 wins point</button>
            </form>
        </div>
    </div>
</body>
</html>