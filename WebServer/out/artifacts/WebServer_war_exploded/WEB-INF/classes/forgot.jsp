<%--
  Created by IntelliJ IDEA.
  User: Nymann
  Date: 12/04/2017
  Time: 15:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en" xmlns:jsp="http://java.sun.com/JSP/Page">
<head>
    <meta charset="UTF-8">
    <title>Plebbit.dk | Forgot Password</title>
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
            <!--Forgot password form-->
            <div id="login">
                <form action="Servlet" class="login-form" method="post">
                    <input type="text" name="forgot-password" placeholder="username" id="username" autofocus="autofocus"/>
                    <input type="submit" value="send" id="forgot-button">
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
