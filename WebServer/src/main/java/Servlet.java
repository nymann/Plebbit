import com.plebbit.dto.ListProperties;
import com.plebbit.plebbit.IPlebbit;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    private String tokenId = "";

    public Servlet() throws MalformedURLException {
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("logout") != null) {
            iPlebbit.logout(tokenId);
            tokenId = "";
        }

        if (request.getParameter("forgot-password") != null) {
            /*
             * Forgot password
             */

            iPlebbit.forgotPassword("forgot-password");
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
                response.sendRedirect("index.jsp");
            } else {
                response.sendRedirect("index.jsp");
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println(request.getRequestURI());
        String currentPage = request.getRequestURI();
        request.setAttribute("loggedIn", isUserLoggedIn());

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

                    //ListProperties[] shoppingLists = (ListProperties[]) temp.toArray();

                    //request.setAttribute("shoppingLists", shoppingLists);
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
                request.setAttribute("loggedIn", /*isUserLoggedIn()*/ false);
                break;

            case "/forgot.jsp":
                request.setAttribute("loggedIn", isUserLoggedIn());
                break;

            case "/change.jsp":
                request.setAttribute("loggedIn", isUserLoggedIn());
        }
    }

    private boolean login(String username, String password) {
        tokenId = iPlebbit.login(username, password);
        return iPlebbit.tokenStillValid(tokenId);
    }

    private boolean isUserLoggedIn() {
        return iPlebbit.tokenStillValid(tokenId) && !tokenId.isEmpty();
    }
}
