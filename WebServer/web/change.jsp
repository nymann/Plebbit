<%--
  Created by IntelliJ IDEA.
  User: Nymann
  Date: 12/04/2017
  Time: 15:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en" xmlns:jsp="http://java.sun.com/JSP/Page">
<head>
    <meta charset="UTF-8">
    <title>Plebbit.dk | Change Password</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
    <meta name="msapplication-TileColor" content="#ffffff">
    <meta name="msapplication-TileImage" content="/ms-icon-144x144.png">
    <meta name="theme-color" content="#ffffff">
</head>
<body>
<div id="wrap">
    <header>
        <div id="logo">
            <a href="index.jsp"><img src="img/plebbit.png" alt="Plebbit" id="logopic"></a>
            <h1><a href="index.jsp">Plebbit</a></h1>
        </div>
    </header>

    <div id="navwrap">
        <nav>
            <!--Menu stuff here-->
            <ul>
                <li><a href="about.jsp">ABOUT</a></li>
            </ul>
        </nav>
    </div>

    <div id="content">
        <div id="left">

        </div>

        <div id="mid">
            <!--Change password form-->
            <div id="login">
                <form action="Servlet" class="login-form" method="post">
                    <input type="text" name="username" placeholder="username" id="username" autofocus="autofocus"/>
                    <input type="password" name="password" placeholder="old password" id="password"/>
                    <input type="password" name="new-password" placeholder="new password" id="new-password"/>
                    <input type="password" name="new-password-again" placeholder="new password" id="new-password-again"/>
                    <input type="submit" value="Change password" id="password-button">
                </form>
            </div>
        </div>

        <div id="right">

        </div>
    </div>

    <footer>
        <h4><a href="index.jsp">©Plebbit.dk</a></h4>
    </footer>

</div>
</body>
</html>
