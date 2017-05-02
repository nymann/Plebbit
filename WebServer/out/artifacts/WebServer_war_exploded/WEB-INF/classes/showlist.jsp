<%@ page import="com.plebbit.dto.Item" %>
<%@ page import="com.plebbit.dto.ListProperties" %>
<%--
  Created by IntelliJ IDEA.
  User: Nymann
  Date: 11/04/2017
  Time: 16:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Plebbit.dk | The online shopping helper</title>
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
                <%
                    boolean loggedIn = (boolean) session.getAttribute("loggedIn");
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
                ListProperties shoppingList = (ListProperties) session.getAttribute("list");

                if (shoppingList == null) {
                    out.println("<h2>Are you even logged in?</h2>");
                } else {
                    out.println("<form action=\"\\Servlet\" method=\"post\">\n" +
                            "\t\t\t\t<input name=\"nameoflist\" type=\"text\" value=\"" + shoppingList.nameOfList + "\" id=\"listname\"/>\n" +
                            "\t\t\t\t<input name=\"listid\" type=\"hidden\" value=\"" + shoppingList.listId + "\">\n" +
                            "\t\t\t</form>");

                    if (shoppingList.items == null) ;
                    else if (shoppingList.items.isEmpty()) ;
                    else {
                        out.println("\t\t\t<table>\n" +
                                "\t\t\t\t<tr>\n" +
                                "\t\t\t\t\t<th>Item</th>\n" +
                                "\t\t\t\t\t<th>Added by</th>\n" +
                                "\t\t\t\t\t<th>Bought</th>\n" +
                                "\t\t\t\t\t<th>Delete</th>\n" +
                                "\t\t\t\t</tr>");


                        for (Item item : shoppingList.items) {
                            if (item.bought) {
                                out.println("<tr>\n" +
                                        "\t\t\t\t\t<td>\n" +
                                        "\t\t\t\t\t\t<form action=\"\\Servlet\" method=\"post\">\n" +
                                        "\t\t\t\t\t\t\t<input name=\"iteminlist\" type=\"text\" value=\"" + item.name + "\" id=\"bought\">\n" +
                                        "\t\t\t\t\t\t\t<input name=\"listid\" type=\"hidden\" value=\"" + shoppingList.listId + "\">\n" +
                                        "\t\t\t\t\t\t\t<input name=\"olditemname\" type=\"hidden\" value=\"" + item.name + "\" id=\"bought\">\n" +
                                        "\t\t\t\t\t\t</form>\n" +
                                        "\t\t\t\t\t</td>\n" +
                                        "\t\t\t\t\t<td>\n" +
                                        "\t\t\t\t\t\t<span id=\"bought\">" + item.user.name + "</span>\n" +
                                        "\t\t\t\t\t</td>\n" +
                                        "\t\t\t\t\t<td>\n" +
                                        "\t\t\t\t\t\t<form action=\"\\Servlet\" method=\"post\">\n" +
                                        "\t\t\t\t\t\t\t<button name=\"boughtitemfromlist\" type=\"submit\" value=\"" + item.name + "\" id=\"bought\">✓</button>\n" +
                                        "\t\t\t\t\t\t\t<input name=\"listid\" type=\"hidden\" value=\"" + shoppingList.listId + "\">\n" +
                                        "\t\t\t\t\t\t</form>\n" +
                                        "\t\t\t\t\t</td>\n" +
                                        "\t\t\t\t\t<td>\n" +
                                        "\t\t\t\t\t\t<form action=\"\\Servlet\" method=\"post\">\n" +
                                        "\t\t\t\t\t\t\t<button name=\"deleteitemfromlist\" type=\"submit\" value=\"" + item.name + "\" id=\"bought\">X</button>\n" +
                                        "\t\t\t\t\t\t\t<input name=\"listid\" type=\"hidden\" value=\"" + shoppingList.listId + "\">\n" +
                                        "\t\t\t\t\t\t</form>\n" +
                                        "\t\t\t\t\t</td>\n" +
                                        "\t\t\t\t</tr>");
                            }
                            else {
                                out.println("<tr>\n" +
                                        "\t\t\t\t\t<td>\n" +
                                        "\t\t\t\t\t\t<form action=\"\\Servlet\" method=\"post\">\n" +
                                        "\t\t\t\t\t\t\t<input name=\"iteminlist\" type=\"text\" value=\"" + item.name + "\" id=\"notbought\">\n" +
                                        "\t\t\t\t\t\t\t<input name=\"listid\" type=\"hidden\" value=\"" + shoppingList.listId + "\">\n" +
                                        "\t\t\t\t\t\t\t<input name=\"olditemname\" type=\"hidden\" value=\"" + item.name + "\" id=\"notbought\">\n" +
                                        "\t\t\t\t\t\t</form>\n" +
                                        "\t\t\t\t\t</td>\n" +
                                        "\t\t\t\t\t<td>\n" +
                                        "\t\t\t\t\t\t<span id=\"notbought\">" + item.user.name + "</span>\n" +
                                        "\t\t\t\t\t</td>\n" +
                                        "\t\t\t\t\t<td>\n" +
                                        "\t\t\t\t\t\t<form action=\"\\Servlet\" method=\"post\">\n" +
                                        "\t\t\t\t\t\t\t<button name=\"boughtitemfromlist\" type=\"submit\" value=\"" + item.name + "\" id=\"notbought\">Confirm</button>\n" +
                                        "\t\t\t\t\t\t\t<input name=\"listid\" type=\"hidden\" value=\"" + shoppingList.listId + "\">\n" +
                                        "\t\t\t\t\t\t</form>\n" +
                                        "\t\t\t\t\t</td>\n" +
                                        "\t\t\t\t\t<td>\n" +
                                        "\t\t\t\t\t\t<form action=\"\\Servlet\" method=\"post\">\n" +
                                        "\t\t\t\t\t\t\t<button name=\"deleteitemfromlist\" type=\"submit\" value=\"" + item.name + "\" id=\"notbought\">X</button>\n" +
                                        "\t\t\t\t\t\t\t<input name=\"listid\" type=\"hidden\" value=\"" + shoppingList.listId + "\">\n" +
                                        "\t\t\t\t\t\t</form>\n" +
                                        "\t\t\t\t\t</td>\n" +
                                        "\t\t\t\t</tr>");
                            }
                        }
                    }
                    out.println("</table>");
                    /* Add new list */
                    out.println("<form action=\"\\Servlet\" method=\"post\">");
                    out.println("\t<input name=\"additem\" type=\"text\" placeholder=\"add new item here\" id=\"additem\" autofocus=\"autofocus\"/>");
                    out.println("\t<input name=\"listid\" type=\"hidden\" value=\"" + shoppingList.listId + "\"/>");
                    out.println("</form>");

                    /* Invite user */
                    out.println("<br/>");
                    out.println("<form action=\"\\Servlet\" method=\"post\">");
                    out.println("\t<input name=\"inviteuser\" type=\"text\" placeholder=\"Invite a friend\" id=\"additem\"/>");
                    out.println("\t<input name=\"listid\" type=\"hidden\" value=\"" + shoppingList.listId + "\"/>");
                    out.println("</form>");
                }


            %>
        </div>
    </div>
    <footer>
        <h4><a href="index.jsp">©Plebbit.dk</a></h4>
    </footer>

</div>
</body>