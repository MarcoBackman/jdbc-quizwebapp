<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Quiz Result</title>
</head>
<body>
    <div class="tester_info_block">
        <!-- Quiz Name -->
        <p>Quiz Result ${quizName.toString()}</p>
        <!-- User Name -->
        <p>${user.userName}</p>
        <!-- Start/End Time -->
        <p>Started Time: ${startTime.toString()}</p>
        <p>Ended Time: ${endTime.toString()}</p>
    </div>

    <!-- Display all questions -->
    <div class="question_choices" style="height: 500px;overflow-y: auto">
        <!-- Mark it with green if correct, red if incorrect with an answer -->
        <!-- Required: User Answers by Questions, Question, Options(Correct and incorrect),-->
        <c:set var="questionList" value="${optionSetByQuestion.keySet()}" scope="page"/>
        <c:forEach items="${questionList}" var="question" varStatus="loop">
            <div class="question" style="border: 1px solid black">
                <h3>${question.questionContent}</h3>
            <c:set var="answerIndex" value="${actualAnswers[loop.index]}" scope="page"/>
            <c:set var="userAnswerIndex" value="${selectedAnswers[loop.index]}" scope="page"/>
            <c:forEach items="${optionSetByQuestion.get(question)}"
                       var="option"
                       varStatus="status">
                <%-- User has matched the answer --%>
                <c:if test="${status.index.equals(answerIndex) && !status.index.equals(userAnswerIndex)}">
                    <p style="background-color: #e17d7d">
                            ${status.index + 1}: ${option.optionContent} - ${option.answer}
                    </p>
                </c:if>
                <c:if test="${status.index.equals(userAnswerIndex) && !status.index.equals(answerIndex)}">
                    <p style="background-color: #c58765">
                            ${status.index + 1}: ${option.optionContent} - ${option.answer}
                    </p>
                </c:if>
                <c:if test="${status.index.equals(userAnswerIndex) && status.index.equals(answerIndex)}">
                    <p style="background-color: #87c249">
                            ${status.index + 1}: ${option.optionContent} - ${option.answer}
                    </p>
                </c:if>
                <c:if test="${!status.index.equals(answerIndex) && !status.index.equals(userAnswerIndex) }">
                    <p>${status.index + 1}: ${option.optionContent} - ${option.answer} </p>
                </c:if>
            </c:forEach>
            </div>
        </c:forEach>
    </div>

<!-- Pass or fail -->
<div class="test_result">
    <h3>${totalScore} / 100 </h3>
    <c:if test="${totalScore < 40}">
        <h4>You have failed!</h4>
    </c:if>
    <c:if test="${totalScore >= 40}">
        <h4>You have passed!</h4>
    </c:if>
</div>

<!-- Retry Button -->
<a href="/home"><button>Take another Quiz</button></a>
</body>
</html>
