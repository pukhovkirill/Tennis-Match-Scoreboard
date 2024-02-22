<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Finished matches</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/newScore.css">
</head>
<body>
    <jsp:include page="/templates/header.jsp"/>
    <div class="menu" style="width: 850px; display: table; padding: 15px;">
        <h3>Finished matches</h3>
        <form style="text-align:center; display:flex; margin-bottom: 15px;" method="get" action="/matches">
            <input name="filter_by_player_name" type="text" class="input" placeholder="Player" style="width:630px;" value="${player}" required/>
            <input type="submit" class="award" style="width:100px; margin-left: 10px" value="Search">
            <a href="/matches" class="award" style="width:100px; margin-left: 10px; color:#003d57; padding-top: 12px">Clear</a>
        </form>
        <table>
            <tr>
                <th>ID</th>
                <th>Player1</th>
                <th>Player2</th>
                <th>Winner</th>
            </tr>
            <c:forEach var="match" items="${matches}">
                <tr>
                    <th><c:out value="${match.id}"/></th>
                    <th><c:out value="${match.firstPlayer.name}"/></th>
                    <th><c:out value="${match.secondPlayer.name}"/></th>
                    <th><c:out value="${match.winner.name}"/></th>
                </tr>
            </c:forEach>
        </table>
        <c:set var="isFirstPage" value="${page == 1}" />
        <c:set var="isLastPage" value="${lastPage == page}" />
        <div style="text-align:center; display:flex; justify-content: center; margin-top: 10px;">
            <a href="${uri}${page-1}" style="height:20px; width: 50px; color:#003d57;" class="award" disabled="${isFirstPage}">prev</a>
            <label style="width:5%; margin-left: 5px; margin-right: 5px"><c:out value="${page}"/></label>
            <a href="${uri}${page+1}" style="height:20px; width: 50px; color:#003d57;" class="award" disabled="${isLastPage}">next</a>
        </div>
    </div>
</body>
</html>