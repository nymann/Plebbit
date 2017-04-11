
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.Endpoint;

/**
 * Project name: Webserver
 * Developed by: Nymann
 * GitHub: github.com/Nymann/Webserver
 */
@WebService(targetNamespace = "http://test")
public class Webservice {
  @WebMethod
  public String sayHelloWorldFrom(String from) {
    String result = "Hello, world, from " + from;
    System.out.println(result);
    return result;
  }
  public static void main(String[] argv) {
    Object implementor = new Webservice ();
    String address = "http://localhost:9000/Webservice";
    Endpoint.publish(address, implementor);
  }
}
