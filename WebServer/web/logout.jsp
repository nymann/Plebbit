<%--
  Created by IntelliJ IDEA.
  User: Nymann
  Date: 12/04/2017
  Time: 15:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
                    boolean loggedIn = (boolean) session.getAttribute("loggedIn");
                    if (loggedIn) {
                        out.println("\t\t<li><a href=\"shoppinglists.jsp\">SHOPPING LISTS</a></li>");
                        out.println("\t\t<li><a href=\"about.jsp\">ABOUT</a></li>");
                        out.println("\t\t<li><a href=\"logout.jsp\">LOGOUT</a></li>");
                    }
                    else {
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

            <%
                if (loggedIn) {
                    /* User is logged in, what should we display then? */
                    out.println("<h3>You are logged in</h3>");
                } else {
                    out.println("<div id=\"login\">");
                    out.println("<form action=\"Servlet\" class=\"login-form\" method=\"post\">");
                    out.println("<input type=\"text\" name=\"username\" placeholder=\"username\" id=\"username\" autofocus=\"autofocus\"/>");
                    out.println("<input type=\"password\" name=\"password\" placeholder=\"password\" id=\"password\"/>");
                    out.println("<input type=\"submit\" value=\"login\" id=\"login-button\">");
                    out.println("</form>");
                    out.println("</div>");
                    out.println("<div id=\"alter-password\">");
                    out.println("<form action=\"forgot.jsp\" method=\"post\">");
                    out.println("<button name=\"forgot-password\">Forgot password?</button>");
                    out.println("</form>");
                    out.println("<form action=\"change.jsp\" method=\"post\">");
                    out.println("<button name=\"change-password\">Change password</button>");
                    out.println("</form>");
                    out.println("</div >");
                }
            %>

        </div>

        <div id="right">

        </div>
    </div>

    <footer>
        <h4><a href="index.jsp">©Plebbit.dk</a></h4>
    </footer>

</div>
</body>
