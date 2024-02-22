<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Finished match</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/newScore.css">
</head>
<body>
    <c:set var="matchScore" value="${requestScope.match}" scope="request"/>
    <jsp:include page="/templates/header.jsp"/>
    <div class="menu" style="height: 270px; width: 850px">
        <h3><c:out value="${matchScore.match.winner.name}"/> wins!</h3>
        <table>
            <tr>
                <th style="width: 50%">Player</th>
                <th style="width: 50%">Sets</th>
            </tr>
            <tr>
                <td><c:out value="${matchScore.match.firstPlayer.name}"/></td>
                <td><c:out value="${matchScore.gameScore.setScore.firstPlayerSet}"/></td>
            </tr>
            <tr>
                <td><c:out value="${matchScore.match.secondPlayer.name}"/></td>
                <td><c:out value="${matchScore.gameScore.setScore.secondPlayerSet}"/></td>
            </tr>
        </table>
        <a href="/"><button class="award" style="margin-top: 10px">Home</button></a>
    </div>
</body>
</html>