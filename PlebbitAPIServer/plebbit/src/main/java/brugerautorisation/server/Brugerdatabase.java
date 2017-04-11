/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brugerautorisation.server;

import brugerautorisation.data.Diverse;
import brugerautorisation.data.Bruger;
import brugerautorisation.server.Serialisering;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Scanner;

/**
 *
 * @author j
 */
public class Brugerdatabase implements Serializable {
	// Vigtigt: Sæt versionsnummer så objekt kan læses selvom klassen er ændret!
	private static final long serialVersionUID = 12345; // bare et eller andet nr.
	private static Brugerdatabase instans;
	private static final String SERIALISERET_FIL = "brugere.ser";
	private static final Path SIKKERHEDSKOPI = Paths.get("sikkerhedskopi");
	private static long filSidstGemt;

	public ArrayList<Bruger> brugere = new ArrayList<>();
	public transient HashMap<String,Bruger> brugernavnTilBruger = new HashMap<>();

	public static Brugerdatabase getInstans()
	{
		if (instans!=null) return instans;

		try {
			instans = (Brugerdatabase) Serialisering.hent(SERIALISERET_FIL);
			instans.brugernavnTilBruger = new HashMap<>();
			System.out.println("Indlæste serialiseret Brugerdatabase: "+instans);
		} catch (Exception e) {
			instans = new Brugerdatabase();
			Path path = Paths.get("D://download//Deltagerliste.html");
			Scanner scanner = new Scanner(System.in);
			try {
				String data = new String(Files.readAllBytes(path));
				System.out.println("Det ser ud til at du ikke har en brugerdatabase endnu.");
				System.out.println("Jeg læser nu filen "+path+" og opretter en brugerdatabase fra den\n");
				indlæsDeltagerlisteFraCampusnetHtml(data, instans.brugere);
        if (instans.brugere.size()==0) throw new IllegalStateException("Der blev ikke fundet nogen brugere i filen");
			} catch (IOException e2) {
				e2.printStackTrace();
				System.err.println("Deltagerlisten mangler vist. Du kan oprette den ved at hente\n"
						+ "https://cn.inside.dtu.dk/cnnet/participants/default.aspx?ElementID=535237&sort=fname&order=ascending&pos=0&lastPos=0&lastDisplay=listWith&cache=false&display=listWith&groupby=rights&interval=10000&search="
						+ "\nog gemme indholdet i filen "+path.toAbsolutePath());
				System.err.println("\nDer oprettes nu en enkelt bruger du kan teste med\n(tryk Ctrl-C for at annullere)");
				Bruger b = new Bruger();
				System.err.print("Brugernavn: "); b.brugernavn = scanner.nextLine();
				System.err.print("Adgangskode: "); b.adgangskode = scanner.nextLine();
				System.err.print("Fornavn: "); b.fornavn = scanner.nextLine();
				System.err.print("Email: "); b.email = scanner.nextLine();
				instans.brugere.add(b);
				System.err.println("Fortsætter, med Brugerdatabase med en enkelt bruger: "+Diverse.toString(b));
				try {	Thread.sleep(2000); } catch (InterruptedException ex) {}
			}
		}
		// Gendan de transiente felter
		for (Bruger b : instans.brugere) {
			instans.brugernavnTilBruger.put(b.brugernavn, b);
		}
		return instans;
	}



	public static void indlæsDeltagerlisteFraCampusnetHtml(String data, ArrayList<Bruger> brugere) {
		//System.out.println("data="+data);
		for (String tr : data.split("<tr")) {
			if (tr.contains("context_header")) continue;
			String td[] = tr.split("<td");
			if (td.length!=6) continue; // Der er 6 kolonner i det, vi er interesserede i
			System.out.println("tr="+tr.replace('\n', ' '));
			for (String tde : td) {
				System.out.println("td="+tde.replace('\n', ' '));
      }
			System.out.flush();
			/*
			0 td= valign="top" class="context_alternating">
			1 td= height="76" valign="top" rowspan="2"><a href="/cnnet/participants/showperson.aspx?campusnetId=190186" class="link"><img src="/cnnet/UserPicture.ashx?x=56&amp;UserId=190186" style="border: 0; width: 56px" alt="" /></a></td>
			2 td=><p><a href="/cnnet/participants/showperson.aspx?campusnetId=190186" class="link">Thor Jørgensen</a> <a href="/cnnet/participants/showperson.aspx?campusnetId=190186" class="link">Mortensen</a></p></td>
			3 td=>                 </td>
			4 td=><p><a href="mailto:s140241@student.dtu.dk" class="link">s140241@student.dtu.dk</a><br /><br /></p></td>
			5 td=>STADS-tilmeldt<br /><br /><br />diploming. IT elektronik</td></tr>
			*/
			Bruger b = new Bruger();
			b.campusnetId = td[1].split("id=")[1].split("\"")[0].split("&")[0];
			b.ekstraFelter.put("webside", td[1].split("href=\"")[1].split("\"")[0]);
			b.fornavn = td[2].split("class=\"link\">")[1].split("<")[0];
			b.efternavn = td[2].split("class=\"link\">")[2].split("<")[0];
			b.email = td[4].split("mailto:")[1].split("\"")[0];
			if (b.email.contains("flhan@dtu.dk") || b.email.contains("phso@dtu.dk")) continue; // drom adm personale
			b.brugernavn = b.email.split("@")[0];
			b.studeretning = td[5].substring(1).replaceAll("<[^>]+>", " ")
              .replace("STADS-tilmeldt","")
              .replace("Afdelingen for Uddannelse og Studerende", "")
              .replace("Center for Diplomingeniøruddannelse ", "")
              .replace("diploming. ","").replaceAll("[ \n]+", " ").trim();
			if (b.studeretning.isEmpty()) b.studeretning = "IT-Økonomi"; // Hvorfor ITØ'ernes er tom ved jeg ikke....
			b.adgangskode = "kode"+Integer.toString((int)(Math.random()*Integer.MAX_VALUE), Character.MAX_RADIX);

			System.out.println("Oprettet:" + Diverse.toString(b));
			brugere.add(b);
		}
	}



	public static void indlæsDeltagerlisteFraCampusnetHtml2(String data, ArrayList<Bruger> brugere) {
		//System.out.println("data="+data);
		for (String tr : data.split("<div class=\"ui-participant\">")) {
			String td[] = tr.split("<div class=\"ui-participant-");
      HashMap<String,String> map = new LinkedHashMap<>();
      for (String lin : td) {
        int n = lin.indexOf('"');
        if (n==-1) continue;
        String nøgle = lin.substring(0, n);
        String værdi = lin.substring(n+2).replaceAll("[\n\r ]+", " ");
        if (nøgle.equals("infobox")) {
          String[] x = værdi.split("</div>");
          nøgle = x[0].replaceAll("<.+?>", " ").trim();
          værdi = x[1];
        }
        if (nøgle.equals("img")) {
          værdi = værdi.split("\"")[1];
        }
        String værdi2 = værdi.replaceAll("<.+?>", " ").replaceAll("[ ]+", " ").trim();
        map.put(nøgle, værdi2);
      }
      if (!map.containsKey("name")) continue;
      System.out.println("map="+map);
			System.out.flush();
			/*
            <div class="ui-participant">
                <div class="ui-participant-img">
                    <a href="https://www.inside.dtu.dk/da/dtuinside/generelt/telefonbog/person?id=87340&tab=0">
                        <img src="/cnnet/userpicture/userpicture.ashx?x=150&userId=179992" />
                    </a>
                </div>
                <div class="ui-participant-name">
                    <span>Sacha Nørskov Behrend</span>
                </div>


                <div class="ui-participant-email">
                    <span class="hiddenOnIcons">
                        <a href="mailto:s132970@student.dtu.dk">
                            s132970@student.dtu.dk
                        </a>
                    </span>
                    <span class="shownOnIcons">
                        s132970@student.dtu.dk
                    </span>
                </div>


                <div class="ui-participants-arrow" id="participantarrow179992" onclick="ToggleAdditionalParticipantInformation(179992)">
                </div>

                <div class="ui-participant-additional user-information">
                    <span>s132970</span>
                </div>

            </div>
            <div class="ui-participant-informationbox" id="participantinformation179992">
                <div class="ui-participant-placeholder">

                    <div class="ui-participant-infobox">
                        <div class="info-header">
                            <span>Brugernavn</span>
                        </div>
                        <div>
                            s132970
                        </div>
                    </div>

                    <div class="ui-participant-infobox">
                        <div class="info-header">
                            <span>Email</span>
                        </div>
                        <div>
                            <a href="mailto:s132970@student.dtu.dk">
                                s132970@student.dtu.dk
                            </a>
                        </div>
                    </div>

                    <div class="ui-participant-infobox">
                        <div class="info-header">
                            <span>Uddannelse</span>
                        </div>
                        <div class="ui-participants-infolist">

                            <p>diploming. Softwaretek.</p>

                        </div>
                    </div>


                </div>
            </div>

map={img=, name=Jacob Nordfalk, email=jacno@dtu.dk jacno@dtu.dk, additional user-information=jacno, informationbox=id="participantinformation162858">, placeholder=, Brugernavn=jacno, Email=jacno@dtu.dk, Institutter=Center for Diplomingeniøruddannelse}
map={img=, name=Pia Holm Søeborg, email=phso@dtu.dk phso@dtu.dk, additional user-information=phso, informationbox=id="participantinformation163058">, placeholder=, Brugernavn=phso, Email=phso@dtu.dk, Institutter=Center for Diplomingeniøruddannelse DIPL-Sekretariatet, categorybar=Forfattere (2), sortrow=Sortér efter Fornavn Efternavn Adresse Email}
map={img=, name=Sune Thomas Bernth Nielsen, email=stbn@dtu.dk stbn@dtu.dk, additional user-information=stbn, informationbox=id="participantinformation179622">, placeholder=, Brugernavn=stbn, Email=stbn@dtu.dk, Adresse=Skodsborggade 17,4 th, 2200 København N, Uddannelse=diploming., Institutter=Center for Diplomingeniøruddannelse}
map={img=, name=Bhupjit Singh, email=bhsi@dtu.dk bhsi@dtu.dk, additional user-information=bhsi, informationbox=id="participantinformation89428">, placeholder=, Brugernavn=bhsi, Email=bhsi@dtu.dk, Institutter=Center for Diplomingeniøruddannelse, categorybar=Brugere (115), sortrow=Sortér efter Fornavn Efternavn Adresse Email}
map={img=, name=Giuseppe Abbate, email=s153516@student.dtu.dk s153516@student.dtu.dk, additional user-information=s153516, informationbox=id="participantinformation220426">, placeholder=, Brugernavn=s153516, Email=s153516@student.dtu.dk, Uddannelse=diploming. Softwaretek.}
map={img=, name=Burim Abdulahi, email=s143591@student.dtu.dk s143591@student.dtu.dk, additional user-information=s143591, informationbox=id="participantinformation199640">, placeholder=, Brugernavn=s143591, Email=s143591@student.dtu.dk, Uddannelse=diploming. Softwaretek.}
map={img=, name=Ibrahim Al-Bacha, email=s118016@student.dtu.dk s118016@student.dtu.dk, additional user-information=s118016, informationbox=id="participantinformation182196">, placeholder=, Brugernavn=s118016, Email=s118016@student.dtu.dk, Uddannelse=diploming. Softwaretek.}
map={img=, name=Amer Ali, email=s145224@student.dtu.dk s145224@student.dtu.dk, additional user-information=s145224, informationbox=id="participantinformation203190">, placeholder=, Brugernavn=s145224, Email=s145224@student.dtu.dk, Uddannelse=diploming. Softwaretek.}
map={img=, name=Ahmad Mohammad Hassan Almajedi, email=s153317@student.dtu.dk s153317@student.dtu.dk, additional user-information=s153317, informationbox=id="participantinformation220040">, placeholder=, Brugernavn=s153317, Email=s153317@student.dtu.dk, Uddannelse=diploming. Softwaretek.}

			*/
			Bruger b = new Bruger();
			b.fornavn = map.get("name");
      int n = b.fornavn.indexOf(" ");
			b.efternavn = b.fornavn.substring(n+1);
      b.fornavn = b.fornavn.substring(0,n);
			b.email = map.get("Email");
			if (b.email.contains("flhan@dtu.dk") || b.email.contains("phso@dtu.dk")) continue; // drom adm personale
			b.brugernavn = b.email.split("@")[0];

      b.studeretning = map.get("Uddannelse");
      b.ekstraFelter.put("webside", map.get("img"));
			if (b.studeretning == null) b.studeretning = "Underviser";
      else if (b.studeretning.isEmpty()) b.studeretning = "IT-Økonomi"; // Hvorfor ITØ'ernes er tom ved jeg ikke....
      else b.studeretning = b.studeretning.replace("diploming. ","").replaceAll("[ \n]+", " ").trim();
			b.adgangskode = "kode"+Integer.toString((int)(Math.random()*Integer.MAX_VALUE), Character.MAX_RADIX);

			System.out.println("Oprettet:" + Diverse.toString(b));
			brugere.add(b);
		}
	}



	public void gemTilFil(boolean tvingSkrivning) {
		if (!tvingSkrivning && filSidstGemt>System.currentTimeMillis()-60000) return; // Gem højst 1 gang per minut
		// Lav en sikkerhedskopi - i fald der skal rulles tilbage eller filen blir beskadiget
		try {
			if (!Files.exists(SIKKERHEDSKOPI)) Files.createDirectories(SIKKERHEDSKOPI);
      if (Files.exists(Paths.get(SERIALISERET_FIL))) {
        Files.move(Paths.get(SERIALISERET_FIL), SIKKERHEDSKOPI.resolve(SERIALISERET_FIL+new Date()));
      }
		} catch (IOException e) { e.printStackTrace(); }
		try {
			Serialisering.gem(this, SERIALISERET_FIL);
			filSidstGemt = System.currentTimeMillis();
			System.out.println("Gemt brugerne pr "+new Date());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public Bruger hentBruger(String brugernavn, String adgangskode) {
		Bruger b = brugernavnTilBruger.get(brugernavn);
		System.out.println("hentBruger "+brugernavn+" gav "+b);
		if (b!=null) {
			if (b.adgangskode.equals(adgangskode)) {
				b.sidstAktiv = System.currentTimeMillis();
				return b;
			}
			System.out.println("        forkert kode: '"+adgangskode+"' - korrekt kode er '"+b.adgangskode+"'");
		}
		// Forkert adgangskode - vent lidt for at imødegå bruge force angreb
		try { Thread.sleep((int)(Math.random()*1000));	} catch (Exception ex) { }
		throw new IllegalArgumentException("Forkert brugernavn eller adgangskode");
	}
}
