package brugerautorisation.server;
import java.io.*;
public class Serialisering
{
	public static void gem(Serializable obj, String filnavn) throws IOException
	{
		System.out.println("Gemmer filen "+filnavn);
		FileOutputStream datastrøm = new FileOutputStream(filnavn);
		ObjectOutputStream objektstrøm = new ObjectOutputStream(datastrøm);
		objektstrøm.writeObject(obj);
		objektstrøm.close();
	}

	public static Serializable hent(String filnavn) throws Exception
	{
		System.out.println("Indlæser filen "+filnavn);
		FileInputStream datastrøm = new FileInputStream(filnavn);
		ObjectInputStream objektstrøm = new ObjectInputStream(datastrøm);
		Serializable obj = (Serializable) objektstrøm.readObject();
		objektstrøm.close();
		return obj;
	}
}