import com.plebbit.dto.Item;
import com.plebbit.dto.ListProperties;
import com.plebbit.plebbit.IPlebbit;
import com.sun.xml.internal.ws.client.ClientTransportException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Project name: WebServer
 * Developed by: Nymann
 * GitHub: github.com/Nymann/Plebbit
 */
@WebServlet("/Servlet")
public class Servlet extends HttpServlet {
    private URL plebbitUrl = new URL("http://gibbo.dk:9427/plebbit?wsdl");
    private QName plebbitQName = new QName("http://plebbit.plebbit.com/", "PlebbitLogicService");
    private Service plebbitService = Service.create(plebbitUrl, plebbitQName);
    private IPlebbit iPlebbit = plebbitService.getPort(IPlebbit.class);

    public Servlet() throws MalformedURLException {
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        session.setMaxInactiveInterval(15 * 60); // 15 minutes

        String tokenId = (String) session.getAttribute("tokenId");
        request.setCharacterEncoding("UTF-8");

        if (request.getParameter("logout") != null) {
            session = request.getSession(false);
            if (session != null) {
                session.invalidate();
                iPlebbit.logout(tokenId);
                session.removeAttribute("tokenId");
            }
        }

        if (request.getParameter("getPrices") != null) {
            String priceSetting = request.getParameter("getPrices");
            boolean priceSet;
            priceSet = priceSetting.equals("yes");
            if (session != null) {
                session.setAttribute("priceFromNetto", priceSet);
            }
            response.sendRedirect("index.jsp");
        }

        if (request.getParameter("inviteuser") != null) {
            String invitedUser = request.getParameter("inviteuser");
            int listId = Integer.parseInt(request.getParameter("listid"));
            assert session != null;
            System.out.print(session.getAttribute("username") + " wants to invite " + invitedUser + ", to list " + listId + ",");
            if (iPlebbit.addUserToList(listId, tokenId, invitedUser)) {
                System.out.println(" it succeeded.");
            } else {
                System.out.println(" it failed.");
            }
            response.sendRedirect("Servlet?shoppinglist=" + listId);
        }

        if (request.getParameter("createnewlist") != null) {
            String listName = request.getParameter("createnewlist");
            iPlebbit.createNewList(tokenId, listName);
            System.out.println("new list, with the name " + listName + " added!");
            response.sendRedirect("shoppinglists.jsp");
        }

        if (request.getParameter("additem") != null) {
            int listId = Integer.parseInt(request.getParameter("listid"));
            String itemToAdd = request.getParameter("additem");
            iPlebbit.addItemToList(tokenId, itemToAdd, listId);

            // Redirect the user back to the previous page.
            response.sendRedirect("Servlet?shoppinglist=" + listId);
        }

        if (request.getParameter("nameoflist") != null) {
            /* Name change of list */

            int listId = Integer.parseInt(request.getParameter("listid"));
            String newListName = request.getParameter("nameoflist");

            // Changing the name of the list.
            iPlebbit.renameListName(listId, newListName, tokenId);

            // Redirect the user back to the previous page.
            response.sendRedirect("Servlet?shoppinglist=" + listId);
        }

        if (request.getParameter("iteminlist") != null) {
            /* Change name of item in list*/
            String oldItemName = request.getParameter("olditemname");
            String newItemName = request.getParameter("iteminlist");
            int listId = Integer.parseInt(request.getParameter("listid"));
            iPlebbit.renameItemName(listId, oldItemName, newItemName, tokenId);
            response.sendRedirect("Servlet?shoppinglist=" + listId);
        }

        if (request.getParameter("boughtitemfromlist") != null) {
            /* Confirm or Cancel, Item bought status */
            int listId = Integer.parseInt(request.getParameter("listid"));
            String itemName = request.getParameter("boughtitemfromlist");
            Item item = iPlebbit.getItem(listId, itemName, tokenId);
            iPlebbit.setBoughtItem(listId, itemName, !item.bought, tokenId);

            response.sendRedirect("Servlet?shoppinglist=" + listId);
        }

        if (request.getParameter("deletelist") != null) {
            int listId = Integer.parseInt(request.getParameter("deletelist"));

            assert session != null;
            System.out.print(session.getAttribute("username").toString() + " wanted to delete list, with list-id: " + listId + ",");

            if (iPlebbit.deleteList(tokenId, listId)) {
                System.out.println("it succeeded.");
            } else {
                System.out.println("it failed.");
            }

            response.sendRedirect("shoppinglists.jsp");
        }

        if (request.getParameter("deleteitemfromlist") != null) {
            /* Delete an item from the current list */
            int listId = Integer.parseInt(request.getParameter("listid"));
            iPlebbit.removeItemFromList(tokenId, request.getParameter("deleteitemfromlist"), listId);

            response.sendRedirect("Servlet?shoppinglist=" + listId);
        }

        if (request.getParameter("forgot-password") != null) {
            /*
             * Forgot password
             */
            String username = request.getParameter("forgot-password");
            iPlebbit.forgotPassword(username);
            response.sendRedirect("emailsent.jsp");
        }

        if (request.getParameter("password-again") != null) {
            /*
             * Change password
             */

            String newPassword = request.getParameter("new-password");
            String newPasswordAgain = request.getParameter("new-password-again");

            if (!newPassword.equals(newPasswordAgain)) {
                /*
                 * Check if the two new passwords match, if they don't alert the user.
                 */
                request.getRequestDispatcher("change.jsp").forward(request, response);

            } else {
                /*
                 * Check if the old password is correct.
                 */

                String username = request.getParameter("username");
                String password = request.getParameter("password");
                if (login(username, password, session)) {
                    iPlebbit.changePassword(username, password, newPassword);
                    request.getRequestDispatcher("index.jsp").forward(request, response);
                } else {
                    request.getRequestDispatcher("change.jsp").forward(request, response);
                }
            }
        }

        if (request.getParameter("password") != null && request.getParameter("username") != null) {
            /*
             * We login by calling Plebbit's SOAP API
             *  If login is successful we get a token.
             *  If not, we get a empty token.
             *  A token is a String in this instance.
             */
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            if (login(username, password, session)) {
                System.out.println(username + " logged in successfully.");
            } else {
                System.out.println("wrong username/password.");
            }
            response.sendRedirect("index.jsp");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        String tokenId = (String) session.getAttribute("tokenId");
        request.setCharacterEncoding("UTF-8");
        String username = (String) session.getAttribute("username");
        String currentPage = request.getRequestURI();
        session.setAttribute("loggedIn", isUserLoggedIn(tokenId));


        if (request.getParameter("shoppinglist") != null) {
            double nanoTime = System.nanoTime();

            int listId = Integer.parseInt(request.getParameter("shoppinglist"));
            ListProperties listInQuestion = iPlebbit.getListFromId(listId, tokenId);
            System.out.println(username + ": Got the list from PlebbitAPI after " + (int) ((System.nanoTime() - nanoTime) * Math.pow(10, -6)) + " ms.");
            boolean getNettoPrices = true;
            if (session.getAttribute("priceFromNetto") != null) {
                getNettoPrices = (boolean) session.getAttribute("priceFromNetto");
            }

            if (listInQuestion.items != null && listInQuestion.items.size() > 0 && getNettoPrices) {
                listInQuestion.items = Arrays.asList(iPlebbit.getPricesForListFromNetto(listId, tokenId));
                System.out.println(username + ": Now we have the prices too, from start to finish it took " + (int) ((System.nanoTime() - nanoTime) * Math.pow(10, -6)) + " ms.");
            }

            session.setAttribute("list", listInQuestion);
            request.getRequestDispatcher("showlist.jsp").forward(request, response);
        }

        switch (currentPage) {
            case "/shoppinglists.jsp":
                double elapsedTime = System.nanoTime();

                int[] userListIds = iPlebbit.getListOfUser(tokenId);
                if (userListIds == null || !iPlebbit.tokenStillValid(tokenId)) {
                    /* we are no longer logged in */
                    request.getRequestDispatcher("index.jsp").forward(request, response);
                } else {
                    ArrayList<ListProperties> temp = new ArrayList<>();
                    ArrayList<Integer> secondsSinceLastChange = new ArrayList<>();

                    for (int id : userListIds) {
                        temp.add(iPlebbit.getListFromId(id, tokenId));
                        secondsSinceLastChange.add(iPlebbit.getPassedSecondsSinceLastChange(id));
                    }

                    session.setAttribute("secondsSinceLastChange", secondsSinceLastChange);
                    session.setAttribute("shoppingLists", temp.toArray(new ListProperties[temp.size()]));

                }

                elapsedTime = (System.nanoTime() - elapsedTime) * Math.pow(10, -6);
                System.out.println(session.getAttribute("username") + ": requested shoppinglists.jsp, It loaded in " + (int) elapsedTime);
                break;

            case "/index.jsp":
                session.setAttribute("loggedIn", isUserLoggedIn(tokenId));
                break;

            case "/":
                session.setAttribute("loggedIn", isUserLoggedIn(tokenId));
                break;

            case "/logout.jsp":
                iPlebbit.logout(tokenId);
                session.setAttribute("loggedIn", isUserLoggedIn(tokenId));
                break;

            case "/showlist.jsp":
                session.setAttribute("loggedIn", isUserLoggedIn(tokenId));
                break;
        }
    }

    private boolean login(String username, String password, HttpSession session) {
        String tokenId = "";
        try {
            tokenId = iPlebbit.login(username, password);
            session.setAttribute("username", username);
            session.setAttribute("tokenId", tokenId);
        } catch (ClientTransportException cte) {
            System.out.println("Someone is trying to login as, username: " + username + ", password: " + password);
        }

        return iPlebbit.tokenStillValid(tokenId);
    }

    private boolean isUserLoggedIn(String tokenId) {
        return tokenId != null && iPlebbit.tokenStillValid(tokenId) && !tokenId.isEmpty();
    }
}
