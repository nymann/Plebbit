<%--
  Created by IntelliJ IDEA.
  User: Nymann
  Date: 12/04/2017
  Time: 15:46
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
<jsp:include page="/Servlet"/>
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
                <%
                    boolean loggedIn = (boolean) request.getAttribute("loggedIn");
                    if (loggedIn) {
                        out.println("\t\t<li><a href=\"shoppinglists.jsp\">SHOPPING LISTS</a></li>");
                        out.println("\t\t<li><a href=\"about.jsp\">ABOUT</a></li>");
                        out.println("\t\t<li><a href=\"logout.jsp\">LOGOUT</a></li>");
                    } else {
                        out.print("<li><a href=\"about.jsp\">ABOUT</a></li>");
                    }
                %>

            </ul>
        </nav>
    </div>

    <div id="content">
        <div id="left">

        </div>

        <div id="mid">
            <h3>Lorem ipsum</h3>
            <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et
                dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip
                ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu
                fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia
                deserunt mollit anim id est laborum.</p>
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