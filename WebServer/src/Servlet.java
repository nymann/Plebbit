import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.io.IOException;
import java.net.URL;

/**
 * Project name: WebServer
 * Developed by: Nymann
 * GitHub: github.com/Nymann/WebServer
 */
@WebServlet("/Servlet")
public class Servlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (request.getParameter("username") != null) {
            /*
             * We login by called Plebbit's SOAP API
             *  If login is successful we get a token.
             *  If not, we get a empty token.
             *  A token is a String in this instance.
             */

            String username = request.getParameter("username");
            String password = request.getParameter("password");

            URL plebbitUrl = new URL("localhost:9427");
            QName plebbitQName = new QName("http://"); // needs to be changed
            Service plebbitService = Service.create(plebbitUrl, plebbitQName);

        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
