<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Log detail page</title>
</head>
<body>
        <%@include file="navBar.jsp"%>

        <!-- For each result:
            - Display all questions with all options and user questions
         -->
        <h1>Quiz Detail</h1>
        <div class="question_choices">
            <!-- Mark it with green if correct, red if incorrect with an answer -->
            <!-- Required: User Answers by Questions, Question, Options(Correct and incorrect),-->
            <c:set var="questionList" value="${optionSetByQuestion.keySet()}" scope="page"/>
            <c:forEach items="${questionList}" var="question" varStatus="loop">
                <div class="question" style="border: 1px solid black">
                    <h3>${question.questionContent}</h3>
                    <c:set var="userAnswerIndex" value="${selectedAnswers[loop.index]}" scope="page"/>
                    <c:forEach items="${optionSetByQuestion.get(question)}"
                               var="option"
                               varStatus="status">
                        <%-- User has matched the answer --%>
                        <c:if test="${option.isAnswer() && !status.index.equals(userAnswerIndex)}">
                            <p style="background-color: #e17d7d">
                                    ${status.index + 1}: ${option.optionContent} - ${option.answer}
                            </p>
                        </c:if>
                        <c:if test="${status.index.equals(userAnswerIndex) && !option.isAnswer()}">
                            <p style="background-color: #c58765">
                                    ${status.index + 1}: ${option.optionContent} - ${option.answer}
                            </p>
                        </c:if>
                        <c:if test="${status.index.equals(userAnswerIndex) && option.isAnswer()}">
                            <p style="background-color: #87c249">
                                    ${status.index + 1}: ${option.optionContent} - ${option.answer}
                            </p>
                        </c:if>
                        <c:if test="${!option.isAnswer() && !status.index.equals(userAnswerIndex) }">
                            <p>${status.index + 1}: ${option.optionContent} - ${option.answer} </p>
                        </c:if>
                    </c:forEach>
                </div>
            </c:forEach>
        </div>
        <button onclick="history.back()">
            Go Back
        </button>
</body>
</html>
