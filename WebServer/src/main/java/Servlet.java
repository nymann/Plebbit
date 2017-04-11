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

    public Servlet() throws MalformedURLException {
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (request.getParameter("email") != null) {
            /*
             * Forgot password
             *
             */

            //iPlebbit.forgotMail(request.getParameter(" email"));
        }

        if (request.getParameter("password-again") != null) {
            /*
             * Change password
             */

            //iPlebbit.forgotPassword(

            String newPassword = request.getParameter("new-password");
            String newPasswordAgain = request.getParameter("new-password-again");

            if (!newPassword.equals(newPasswordAgain)) {
                /*
                 * Check if the two new passwords match, if they don't alert the user.
                 */
                request.getRequestDispatcher("Change.html").forward(request, response);

            } else {
                /*
                 * Check if the old password is correct.
                 */

                String username = request.getParameter("username");
                String password = request.getParameter("password");
                if (login(username, password)) {
                    // Forward to the new page.
                    request.getRequestDispatcher("index.html").forward(request, response);
                }

                else {
                    request.getRequestDispatcher("Change.html").forward(request, response);
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
                request.getRequestDispatcher("index.html").forward(request, response);
            }

            else {
                request.getRequestDispatcher("index.html").forward(request, response);
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    private boolean login(String username, String password) {
        // If the token we get back is empty, the login failed, if it's not empty we are logged in.
        String token = iPlebbit.login(username, password);
        return !token.isEmpty();
    }
}
