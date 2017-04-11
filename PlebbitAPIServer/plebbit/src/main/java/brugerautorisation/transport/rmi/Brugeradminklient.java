package brugerautorisation.transport.rmi;

import brugerautorisation.data.Diverse;
import brugerautorisation.data.Bruger;
import java.rmi.Naming;

public class Brugeradminklient {
	
	public static final String mortenLogin = "s144827";
	public static final String mortenPassword = "kek";
	
	public static void main(String[] arg) throws Exception {
		
		
		
//		Brugeradmin ba =(Brugeradmin) Naming.lookup("rmi://localhost/brugeradmin");
		Brugeradmin ba = (Brugeradmin) Naming.lookup("rmi://javabog.dk/brugeradmin");

    //ba.sendGlemtAdgangskodeEmail("jacno", "Dette er en test, husk at skifte kode");
		//ba.ændrAdgangskode("jacno", "kodenj4gvs", "xxx");
		Bruger b = ba.hentBruger(Brugeradminklient.mortenLogin, Brugeradminklient.mortenPassword);
		System.out.println("Fik bruger = " + b);
		System.out.println("Data: " + Diverse.toString(b));
		
		
		
		//ba.ændrAdgangskode("s144827", "kodet751yd", "kek");
		// ba.sendEmail("jacno", "xxx", "Hurra det virker!", "Jeg er så glad");
/*
		Object ekstraFelt = ba.getEkstraFelt("jacno", "xxx", "s123456_testfelt");
		System.out.println("Fik ekstraFelt = " + ekstraFelt);

		ba.setEkstraFelt("jacno", "xxx", "s123456_testfelt", "Hej fra Jacob"); // Skriv noget andet her

		String webside = (String) ba.getEkstraFelt("jacno", "xxx", "webside");
		System.out.println("webside = " + webside);
		*/
	}
}
