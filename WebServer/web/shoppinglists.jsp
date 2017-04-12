<%--
  Created by IntelliJ IDEA.
  User: Nymann
  Date: 11/04/2017
  Time: 16:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Plebbit.dk | The Online Shopping Helper</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<div id="wrap">
    <header>
        <div id="logo">
            <img src="img/plebbit.png" alt="Plebbit" id="logopic">
            <h1>Plebbit</h1>
        </div>
    </header>

    <nav>
        <!--Menu stuff here-->
        <ul>
            <li><a href="Shoppinglists.jsp">SHOPPING LISTS</a></li>
            <li><a href="#">FIND FRIENDS</a></li>
            <li><a href="#">ABOUT</a></li>
            <li><a href="#">ACCOUNT</a></li>
        </ul>
    </nav>

    <div id="content">
        <div id="left">

        </div>

        <div id="mid">
            <div id="login">
                <form action="Servlet" class="login-form" method="post">
                    <input type="text" name="username" placeholder="username" id="username" autofocus="autofocus"/>
                    <input type="password" name="password" placeholder="password" id="password"/>
                    <input type="submit" value="login" id="login-button">
                </form>
            </div>
            <div id="alter-password">
                <form action="Forgot.html" method="post">
                    <button name="forgot-password">Forgot password?</button>
                </form>

                <form action="Change.html" method="post">
                    <button name="change-password">Change password</button>
                </form>

            </div>
        </div>

        <div id="right">

        </div>
    </div>

    <footer>
        <h4>©Plebbit.dk</h4>
    </footer>

</div>
</body>
</html>
