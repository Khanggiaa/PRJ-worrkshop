<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login Page</title>
    </head>
    <body>
        <h1>Login</h1>
        <form action="MainController" method="POST">
            UserID: <input type="text" name="userID" required/><br/>
            Password: <input type="password" name="password" required/><br/>
            <input type="submit" name="action" value="Login"/>
        </form>
        
        <%-- Hiển thị lỗi --%>
        <%
            String error = (String) request.getAttribute("ERROR");
            if (error != null) {
        %>
            <h3 style="color: red"><%= error %></h3>
        <%
            }
        %>
    </body>
</html>