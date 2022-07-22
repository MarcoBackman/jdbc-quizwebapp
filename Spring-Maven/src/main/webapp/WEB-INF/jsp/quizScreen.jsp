<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
    <title>Taking Quiz...</title>
</head>
<body>
    <%@include file="navBar.jsp"%>
    <br>
    <div class="question_block" <c:if test="${selectedAnswer != -1}"> style="background-color: beige" </c:if>>
        <h3>${questionNumber + 1}. ${question.questionContent}</h3>
        <%-- Assume index starts from 0 to 9 --%>
        <c:set var="currentQuestionIndex" value="${questionNumber}" scope="page"/>

        <%-- Create option input fields index from 0 to 4 --%>
        <c:forEach items="${options}" var="option" varStatus="status">
            <p><%-- selectedAnswer and status.index Value range 0 to 4 --%>
                <input type="radio" name="is_answer" value="${status.index}" form="save"
                <c:if test="${selectedAnswer == status.index}">
                       checked="checked" style="background-color: aquamarine"
                </c:if>
                >
                    ${status.index + 1}. ${option.optionContent}
            </p>
        </c:forEach>
        <br>
    </div>
    <div>
        <form id="save" method="post">
            <div style="margin: 1em 0 1em 0">
                <%-- question numbering from 0 to 9 --%>
                <c:forEach begin="0" end="9" varStatus="loop">
                    <button style="display:inline-block;width:25px;height:25px"
                            formaction="/take-quiz/${topic}/${loop.index + 1}?questionNumber=${currentQuestionIndex}">
                            ${loop.index + 1}
                    </button>
                </c:forEach>
            </div>
            <div style="margin: 1em 0 1em 0">
                <c:if test="${questionNumber > 0}">
                    <button formaction="/take-quiz/${topic}/${questionNumber}?questionNumber=${currentQuestionIndex}"
                            style="width: 100px; height: 50px">
                        Previous
                    </button>
                </c:if>
                <c:if test="${questionNumber < 9}">
                    <button formaction="/take-quiz/${topic}/${questionNumber + 2}?questionNumber=${currentQuestionIndex}"
                            style="width: 100px; height: 50px">
                        Next
                    </button>
                </c:if>
            </div>
            <button type="submit" formaction="/submit_quiz/${topic}?questionNumber=${currentQuestionIndex}">
                Submit Quiz
            </button>
        </form>
    </div>
    <br>
</body>
</html>
