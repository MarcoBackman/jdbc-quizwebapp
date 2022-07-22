<%--
  Created by IntelliJ IDEA.
  User: Tony
  Date: 7/15/2022
  Time: 2:44 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
</head>
<body>
    <%@include file="navBar.jsp"%>
    <h2>Login</h2>
    <form method="post" action="login">
        <fieldset class="input_field">
            <legend>Please Type In:</legend>
            <label>
                user id:
                <input type="text" name="user_id">
            </label> <br>
            <label>
                password:
                <input type="password" name="password">
            </label> <br>
        </fieldset>
        <button type="submit">login</button>
    </form>
</body>
</html>
