/*
Idriftsættelse
cd /home/j/DistribueredeSystemer/DistribueredeSystemer
ant -q; rsync -a dist/* deltagere.html gmail-adgangskode.txt  javabog.dk:DistribueredeSystemer/

ssh javabog.dk
cd DistribueredeSystemer
screen -S dist java -jar "DistribueredeSystemer.jar"


screen -d -r

 */
package brugerautorisation.server;

import brugerautorisation.data.Diverse;
import brugerautorisation.data.Bruger;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Hovedprogrammet på serveren
 * @author j
 */
public class BenytBrugerdatabase {

	public static void main(String[] args) throws IOException {

		Brugerdatabase db = Brugerdatabase.getInstans();
		System.out.println("\nDer er "+db.brugere.size()+" brugere i databasen");

		brugerautorisation.transport.rmi.Brugeradminserver.main(null);
		brugerautorisation.transport.soap.Brugeradminserver.main(null);

		Scanner scanner = new Scanner(System.in); // opret scanner-objekt

		while (true) try {
			System.out.println();
			System.out.println("1 Udskriv brugere");
			System.out.println("2 Generer kommasepareret fil med brugere");
			System.out.println("3 Generer brugernavne og adgangskode");
			System.out.println("4 Send mail til alle brugere, der ikke har ændret deres kode endnu");
//			System.out.println("5 Tilføj bruger");
//			System.out.println("6 Slet bruger");
			System.out.println("9 Gem databasen og stop programmet");
			System.out.print("Skriv valg: ");
			int valg = scanner.nextInt();
			scanner.nextLine();
			if (valg==1) {
				for (Bruger b : db.brugere) {
					System.out.println(Diverse.toString(b));
				}
			} else
			if (valg==2) {
				for (Bruger b : db.brugere) {
					System.out.println(Diverse.tilCsvLinje(b));
				}
			} else
			if (valg==3) {
				for (Bruger b : db.brugere) {
					System.out.println("sudo useradd -m -p `mkpasswd '"+b.adgangskode+"'` "+b.brugernavn+"; sudo adduser "+b.brugernavn+" sudo");
				}
			} else
			if (valg==4) {
				ArrayList<Bruger> mglBru = new ArrayList<>();
				for (Bruger b : db.brugere) {
					if (b.sidstAktiv > 0) continue;
					mglBru.add(b);
				}
				System.out.println("Der er "+mglBru.size()+" brugere, der mangler at skifte deres kode.");
				System.out.println("Det er: "+mglBru);
				System.out.println("Skriv en linje med forklarende tekst");
				String forklarendeTekst = scanner.nextLine();
				System.out.println("Er du SIKKER på at du vil sende "+forklarendeTekst+" til "+mglBru.size()+" brugere?");
				System.out.print("Skriv JA: ");
				String accept = scanner.nextLine().trim();
				if (!accept.equals("JA")) {
					System.out.println("Afbrudt med "+accept);
					continue;
				}

				for (Bruger b : mglBru) {
					SendMail.sendMail("DIST: Din adgangskode skal skiftes",
							"Kære "+b.fornavn+"\n\nDu skal skifte adgangskoden som en del af kurset i Distribuerede Systemer."
							+"\n\nDit brugernavn er "+b.brugernavn+" og din midlertidige adgangskode er: "+b.adgangskode
							+"\n\nSe hvordan du skifter koden på https://goo.gl/26pBG9 \n"
							+"\n\n"+forklarendeTekst
							+"\n\nBesked sendt p.v.a. underviseren - Jacob Nordfalk (jacno@dtu.dk)",
							b.email);
					Thread.sleep(1000);
				}
			} else
/*
			if (valg==5) {
        Bruger b = new Bruger();
        System.out.println(""+Diverse.toString(b));
        java.util.Scanner tastatur = new java.util.Scanner(System.in);  // forbered
        System.out.print("brugernavn/studienummer: ");
        b.brugernavn = tastatur.nextLine();
        System.out.print("brugernavn/studienummer: ");
        b.brugernavn = tastatur.nextLine();
        // resten mangler
			} else
*/
			if (valg==9) {
				break;
			} else {
				System.out.println("Ulovligt valg");
			}
		} catch (Throwable t) { t.printStackTrace(); scanner.nextLine(); }

		//db.gemTilFil();
		System.out.println("Afslutter programmet... ");
		db.gemTilFil(true);
		System.exit(0);
	}

}
