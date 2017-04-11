package brugerautorisation.transport.soap;

import brugerautorisation.server.Brugerdatabase;
import javax.xml.ws.Endpoint;

public class Brugeradminserver {
	public static void main(String[] args) {
		Brugerdatabase db = Brugerdatabase.getInstans();
		System.out.println("Publicerer Brugeradmin over SOAP");
		BrugeradminImpl impl = new BrugeradminImpl();
		impl.db = db;
    // Ipv6-addressen [::] svarer til Ipv4-adressen 0.0.0.0, der matcher alle maskinens netkort og 
		Endpoint.publish("http://[::]:9901/brugeradmin", impl);
		System.out.println("Brugeradmin publiceret over SOAP");
	}
}
