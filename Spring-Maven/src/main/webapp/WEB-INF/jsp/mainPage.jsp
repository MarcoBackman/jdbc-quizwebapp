<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Main page</title>
</head>
<body>
    <!-- Navbar location -->
    <%@include file="navBar.jsp"%>
    <!-- Main section -->
    <h2>Welcome! ${user.firstName}.</h2>
    <!-- Display quiz types -->
    <c:forEach items="${quiz_types}" var="quiz_type" varStatus="loop">
        <form method="get" action="takeQuiz">
            <label style="visibility: hidden">
                <input type="text" name="quiz_type" value="${quiz_type.quizTypeNumber}" style="visibility: hidden">
            </label>
            <a href="/take-quiz/${quiz_type.quizTypeNumber}" type="submit">
                <div class="quiz_type_block">
                    <h1 class="quiz_type_name">
                            ${quiz_type.quizDescription}
                    </h1>
                    <p>Press to take a quiz</p>
                </div>
            </a>
        </form>
    </c:forEach>
</body>
</html>
