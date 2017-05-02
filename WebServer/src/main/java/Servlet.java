import com.plebbit.dto.ListProperties;
import com.plebbit.plebbit.IPlebbit;

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

        if (session.getAttribute("tokenId") != null) {
            System.out.println("tokenId is not null, it's: " + session.getAttribute("tokenId").toString());
        }

        if (request.getParameter("logout") != null) {
            session = request.getSession(false);
            if (session != null) {
                session.invalidate();
                iPlebbit.logout(tokenId);
                session.removeAttribute("tokenId");
            }
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

            // Changing the name of the list.
            /* Implement: iPlebbit.renameList(String newName, int lidtId, String tokenId) here, when it's added to the API. */

            // Redirect the user back to the previous page.
            response.sendRedirect("Servlet?shoppinglist=" + listId);
        }

        if (request.getParameter("iteminlist") != null) {
            /* Change name of item in list*/
            String oldItemName = request.getParameter("olditemname");
            String newItemName = request.getParameter("iteminlist");
            int listId = Integer.parseInt(request.getParameter("listid"));
            System.out.println("Rename requested, old-name: " + oldItemName + ", new-name: " + newItemName + ", list-id: " + listId + ".");

            iPlebbit.renameItemName(listId, oldItemName, newItemName, tokenId); // todo: not working on API end.
            response.sendRedirect("Servlet?shoppinglist=" + listId);
        }

        if (request.getParameter("boughtitemfromlist") != null) {
            /* Confirm item bought */
            int listId = Integer.parseInt(request.getParameter("listid"));
            String itemName = request.getParameter("boughtitemfromlist");
            iPlebbit.setBoughtItem(listId, itemName, true, tokenId);

            response.sendRedirect("Servlet?shoppinglist=" + listId);
        }

        if (request.getParameter("deletelist") != null) {
            int listId = Integer.parseInt(request.getParameter("deletelist"));
            System.out.println("User wanted to delete list, with list-id: " + listId + ".");
            if (iPlebbit.deleteList(tokenId, listId)) {
                System.out.println("delete succeeded.");
            } else {
                System.out.println("couldn't delete list.");
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
            System.out.println("WE REALLY IN HERE.");
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            if (login(username, password, session)) {
                // Forward to the new page.
                /*Cookie[] cookies = null;
                cookies = request.getCookies();
                if (cookies != null) {
                    for (Cookie cookie: cookies) {
                        System.out.print("Name: " + cookie.getName() + ",");
                        System.out.println("Value: " + cookie.getValue() + ".");
                    }
                }*/
                System.out.println("Logged in success.");
            }
            response.sendRedirect("index.jsp");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        String tokenId = (String) session.getAttribute("tokenId");
        request.setCharacterEncoding("UTF-8");
        String currentPage = request.getRequestURI();
        if (tokenId == null) {
            session.setAttribute("loggedIn", false);
            tokenId = "";
            session.setAttribute("tokenId", tokenId);
        }
        else {
            session.setAttribute("loggedIn", isUserLoggedIn(tokenId));
        }

        if (request.getParameter("shoppinglist") != null) {
            int listId = Integer.parseInt(request.getParameter("shoppinglist"));
            ListProperties listInQuestion = iPlebbit.getListFromId(listId, tokenId);
            session.setAttribute("list", listInQuestion);
            request.getRequestDispatcher("showlist.jsp").forward(request, response);
        }

        switch (currentPage) {
            case "/shoppinglists.jsp":
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
        String tokenId = iPlebbit.login(username, password);
        System.out.println("login tokenId: " + tokenId);
        session.setAttribute("tokenId", tokenId);
        System.out.println("Is token still valid? ");
        return iPlebbit.tokenStillValid(tokenId);
    }

    private boolean isUserLoggedIn(String tokenId) {
        //System.out.println("We valid? " + iPlebbit.tokenStillValid(tokenId) + ". Token: " + tokenId);
        System.out.println("isUserLoggedIn tokenId: " + tokenId);
        return iPlebbit.tokenStillValid(tokenId) && !tokenId.isEmpty();
    }
}
