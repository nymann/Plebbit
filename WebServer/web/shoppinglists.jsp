<%@ page import="com.plebbit.dto.ListProperties" %>
<%@ page import="java.util.ArrayList" %><%--
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
    <title>Plebbit.dk | Change Password</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
    <meta name="msapplication-TileColor" content="#ffffff">
    <meta name="msapplication-TileImage" content="/ms-icon-144x144.png">
    <meta name="theme-color" content="#ffffff">
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
                    }
                    else {
                        out.print("<li><a href=\"about.jsp\">ABOUT</a></li>");
                    }
                %>
            </ul>
        </nav>
    </div>

    <div id="content">
        <div id="shoppinglist">
        <!--Change password form-->
        <%
            if (!loggedIn) {
                out.println("<h2>You are not logged in!</h2>");
                return;
            }

            ListProperties[] shoppingLists = (ListProperties[]) request.getAttribute("shoppingLists");

            if (shoppingLists.length < 1) {
                out.println("<h2>You don't have any shopping lists yet.</h2>");
            } else {
                out.println("<table>");
                out.println("<tr>");
                out.println("<th>List name</th>");
                out.println("<th>No. of Items</th>");
                /*out.println("<th>Latest change</th>");*/
                out.println("</tr>");
                for (ListProperties shoppingList : shoppingLists) {
                    out.println("<tr>");
                    out.println("<td>" + shoppingList.nameOfList + "</td>");
                    int itemsInList;
                    if (shoppingList.items == null) {
                        itemsInList = 0;
                    }
                    else {
                        itemsInList = shoppingList.items.size();
                    }
                    out.println("<td>" + itemsInList + "</td>");
                    out.println("</tr>");
                }
                out.println("</table>");
            }
        %>
        </div>
    </div>
    <footer>
        <h4><a href="index.jsp">©Plebbit.dk</a></h4>
    </footer>

</div>
</body>
</html>
