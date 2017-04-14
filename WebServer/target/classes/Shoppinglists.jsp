<%@ page import="com.plebbit.dto.ListProperties" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.plebbit.dto.Item" %><%--
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
                    } else {
                        out.print("<li><a href=\"about.jsp\">ABOUT</a></li>");
                    }
                %>
            </ul>
        </nav>
    </div>

    <div id="content">
        <div id="shoppinglist">

            <%
                if (!loggedIn) {
                    out.println("<h2>You are not logged in!</h2>");
                    return;
                }

                ListProperties[] shoppingLists = (ListProperties[]) request.getAttribute("shoppingLists");
                if (shoppingLists == null) {
                    out.println("<h2>Are you even logged in?</h2>");
                }

                else if (shoppingLists.length < 1) {
                    out.println("<h2>You don't have any shopping lists yet.</h2>");
                } else {
                    out.println("<table>\n" +
                            "\t\t\t\t<tr>\n" +
                            "\t\t\t\t\t<th>List name</th>\n" +
                            "\t\t\t\t\t<th>No. of Items</th>\n" +
                            "\t\t\t\t\t<th>Changed</th>\n" +
                            "\t\t\t\t</tr>");

                    for (ListProperties shoppingList : shoppingLists) {
                        if (shoppingList == null) {
                            continue;
                        }
                        out.println("\t\t\t\t<tr>\n" +
                                "\t\t\t\t\t<td>\n" +
                                "\t\t\t\t\t\t<form action=\"\\Servlet\" method=\"get\">\n" +
                                "\t\t\t\t\t\t\t<button name=\"shoppinglist\" type=\"submit\" value=\"" + shoppingList.listId + "\">" + shoppingList.nameOfList + "</button>\n" +
                                "\t\t\t\t\t\t</form>\n" +
                                "\t\t\t\t\t</td>\n" +
                                "\t\t\t\t\t<td>2</td>\n" +
                                "\t\t\t\t\t<td>5 min. ago</td>\n" +
                                "\t\t\t\t</tr>");
                    }
                    out.println("\t\t\t</table>");
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
