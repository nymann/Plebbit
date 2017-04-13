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

                if (shoppingLists.length < 1) {
                    out.println("<h2>You don't have any shopping lists yet.</h2>");
                } else {
                    out.println("<table>");
                    out.println("<tr>");
                    out.println("<th>List name</th>");
                    out.println("<th>No. of Items</th>");
                    /*out.println("<th>Changed</th>");*/
                    out.println("</tr>");
                    out.println("</table>");
                    for (ListProperties shoppingList : shoppingLists) {
                        out.println("<table>");
                        out.println("<tr>");
                        out.println("<td><button onclick=\"myFunction" + shoppingList.listId + "()\">" + shoppingList.nameOfList + "</button></td>");

                        int itemsInList;
                        if (shoppingList.items == null) {
                            itemsInList = 0;
                        } else {
                            itemsInList = shoppingList.items.size();
                        }
                        out.println("<td>" + itemsInList + "</td>");
                        out.println("</tr>");
                        out.println("</table>");
                        out.println("<table id=\"" + shoppingList.listId + "\"></table>");
                        //String innerHtmlContent = "<th>Item</th><th>User</th>\n";

                        String innerHtmlContent = "<tr><th>Item</th><th>User</th></tr>";
                        if (shoppingList.items != null) {
                            for (Item item : shoppingList.items) {
                                innerHtmlContent += "<tr><td>" + item.name + "</td><td>" + item.user.name + "</td></tr>";
                                //innerHtmlContent += "Name:" + item.name + ", User:" + item.user + "\n";
                            }
                        }
                        else {
                            innerHtmlContent += "<tr><td>no items added</td><td></td></tr>";
                        }
                        out.print("<br />");
                        out.println("<script>\n" +
                                "var n = 0;\n" +
                                "function myFunction" + shoppingList.listId + "() {\n" +
                                "if(n%2==0){" +
                                "document.getElementById(\"" + shoppingList.listId + "\").innerHTML = \"" + innerHtmlContent + "\";" +
                                "} else {" +
                                "    document.getElementById(\"" + shoppingList.listId + "\").innerHTML = \"\";" +
                                "}" +
                                "n++;" +
                                "}\n" +
                                "</script>");
                    }
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
