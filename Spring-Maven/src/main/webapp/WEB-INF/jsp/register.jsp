<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Register</title>
</head>
<body>
    <%@include file="navBar.jsp"%>
    <div class="register_box">
        <h2>Register</h2>
        <form method="post" action="register">
            <fieldset class="input_field">
                <legend>Please Type In:</legend>
                <label>
                    user account name:
                    <input type="text" name="user_id">
                </label> <br>
                <label>
                    password:
                    <input type="password" name="password">
                </label> <br>
                <label>
                    first name:
                    <input type="text" name="first_name">
                </label> <br>
                <label>
                    last name:
                    <input type="text" name="last_name">
                </label> <br>
                <label>
                    email:
                    <input type="text" name="email">
                </label> <br>
                <button type="submit">Register</button>
                <c:if test="${not empty message}">
                    Error: ${message}
                </c:if>
            </fieldset>
        </form>
    </div>
</body>
</html>
