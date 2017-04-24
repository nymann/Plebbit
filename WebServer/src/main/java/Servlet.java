import com.plebbit.dto.ListProperties;
import com.plebbit.plebbit.IPlebbit;

import javax.mail.Session;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Project name: WebServer
 * Developed by: Nymann
 * GitHub: github.com/Nymann/WebServer
 */
@WebServlet("/Servlet")
public class Servlet extends HttpServlet {

    private URL plebbitUrl = new URL("http://gibbo.dk:9427/plebbit?wsdl");
    private QName plebbitQName = new QName("http://plebbit.plebbit.com/", "PlebbitLogicService");
    private Service plebbitService = Service.create(plebbitUrl, plebbitQName);
    private IPlebbit iPlebbit = plebbitService.getPort(IPlebbit.class);
    private String tokenId = ""; // not thread safe

    public Servlet() throws MalformedURLException {
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("logout") != null) {
            iPlebbit.logout(tokenId);
            tokenId = "";
        }

        if (request.getParameter("nameoflist") != null) {
            /* Name change of list */
            int listId = Integer.parseInt(request.getParameter("listid"));
        }

        if (request.getParameter("iteminlist") != null) {
            /* Change name of item in list*/
            int listId = Integer.parseInt(request.getParameter("listid"));
        }

        if (request.getParameter("boughtitemfromlist") != null) {
            /* Confirm item bought */
            int listId = Integer.parseInt(request.getParameter("listid"));
            System.out.println(request.getParameter("boughtitemfromlist") + " bought.");
        }

        if (request.getParameter("deleteitemfromlist") != null) {
            /* Delete an item from the current list */
            System.out.println(request.getParameter("listid"));
            int listId = Integer.parseInt(request.getParameter("listid"));
            iPlebbit.removeItemFromList(tokenId, request.getParameter("deleteitemfromlist"), listId);
            return;
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
                if (login(username, password)) {
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
            if (login(username, password)) {
                // Forward to the new page.
                /*Cookie[] cookies = null;
                cookies = request.getCookies();
                if (cookies != null) {
                    for (Cookie cookie: cookies) {
                        System.out.print("Name: " + cookie.getName() + ",");
                        System.out.println("Value: " + cookie.getValue() + ".");
                    }
                }*/
            }
            response.sendRedirect("index.jsp");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String currentPage = request.getRequestURI();
        request.setAttribute("loggedIn", isUserLoggedIn());

        if (request.getParameter("shoppinglist") != null) {
            int listId = Integer.parseInt(request.getParameter("shoppinglist"));
            ListProperties listInQuestion = iPlebbit.getListFromId(listId, tokenId);
            request.setAttribute("list", listInQuestion);
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

                    for (int id : userListIds) {
                        temp.add(iPlebbit.getListFromId(id, tokenId));
                    }

                    request.setAttribute("shoppingLists", temp.toArray(new ListProperties[temp.size()]));
                }

                break;

            case "/index.jsp":
                request.setAttribute("loggedIn", isUserLoggedIn());
                break;

            case "/":
                request.setAttribute("loggedIn", isUserLoggedIn());
                break;

            case "/logout.jsp":
                iPlebbit.logout(tokenId);
                request.setAttribute("loggedIn", isUserLoggedIn());
                break;
        }
    }

    private boolean login(String username, String password) {
        tokenId = iPlebbit.login(username, password);
        return iPlebbit.tokenStillValid(tokenId);
    }

    private boolean isUserLoggedIn() {
        //System.out.println("We valid? " + iPlebbit.tokenStillValid(tokenId) + ". Token: " + tokenId);
        return iPlebbit.tokenStillValid(tokenId) && !tokenId.isEmpty();
    }
}
