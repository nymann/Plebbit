<%@ page import="com.plebbit.dto.ListProperties" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.plebbit.dto.Item" %>
<%@ page import="com.sun.org.apache.xalan.internal.xsltc.util.IntegerArray" %><%--
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
                    ArrayList<Integer> secondsSinceLastChangeForItemLists = new ArrayList<>();
                    secondsSinceLastChangeForItemLists = (ArrayList<Integer>) request.getAttribute("secondsSinceLastChange");
                    out.println("<table>\n" +
                            "\t\t\t\t<tr>\n" +
                            "\t\t\t\t\t<th>List name</th>\n" +
                            "\t\t\t\t\t<th>No. of Items</th>\n" +
                            "\t\t\t\t\t<th>Changed</th>\n" +
                            "\t\t\t\t\t<th>Delete</th>\n" +
                            "\t\t\t\t</tr>");


                    int count = 0;
                    for (ListProperties shoppingList : shoppingLists) {
                        int lastChange = secondsSinceLastChangeForItemLists.get(count);
                        String changed = "";
                        if (lastChange < 60) {
                            if (lastChange == 0) {
                                changed = " now.";
                            }
                            else if (lastChange == 1) {
                                changed = " a second ago.";
                            }
                            else {
                                changed = lastChange + " seconds ago.";
                            }
                        }
                        else if (lastChange < 3600) {
                            int minutes = lastChange/60;

                            if (minutes == 1) {
                                changed = " a minute ago.";
                            } else {
                                changed = minutes + " minutes ago.";
                            }
                        }
                        else if (lastChange < 86400){
                            int hours = lastChange/3600;
                            if (hours == 1) {
                                changed = " an hour ago.";
                            } else {
                                changed = hours + " hours ago.";
                            }
                        }

                        else {
                            int days = lastChange/86400;
                            if (days == 1) {
                                changed = " a day ago.";
                            }
                            else {
                                changed = days + " days ago.";
                            }
                        }

                        if (shoppingList == null) {
                            continue;
                        }
                        int size = 0;
                        if (shoppingList.items != null) {
                            size = shoppingList.items.size();
                        }
                        out.println("\t\t\t\t<tr>\n" +
                                "\t\t\t\t\t<td>\n" +
                                "\t\t\t\t\t\t<form action=\"\\Servlet\" method=\"get\">\n" +
                                "\t\t\t\t\t\t\t<button name=\"shoppinglist\" type=\"submit\" value=\"" + shoppingList.listId + "\">" + shoppingList.nameOfList + "</button>\n" +
                                "\t\t\t\t\t\t</form>\n" +
                                "\t\t\t\t\t</td>\n" +
                                "\t\t\t\t\t<td>" + size + "</td>\n" +
                                "\t\t\t\t\t<td>" + changed + "</td>\n" +
                                "\t\t\t\t\t\t<td><form action=\"\\Servlet\" method=\"post\">\n" +
                                "\t\t\t\t\t\t\t<button name=\"deletelist\" type=\"submit\" value=\"" + shoppingList.listId + "\" id=\"deleteicon\">X</button>\n" +
                                "\t\t\t\t\t\t\t</form></td>" +
                                "\t\t\t\t</tr>");
                        count++;
                    }
                    out.println("\n\t\t\t</table>");

                    out.println("\t\t<form action=\"\\Servlet\" method=\"post\">\n" +
                            "\t\t\t<button name=\"createnewlist\" type=\"submit\" id=\"createnewlist\">Add new shopping list!</button>\n" +
                            "\t\t</form>");
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
