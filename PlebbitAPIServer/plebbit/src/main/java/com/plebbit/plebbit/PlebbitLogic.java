package com.plebbit.plebbit;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;

import javax.jws.WebService;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import com.plebbit.database.DatabaseConnector;
import com.plebbit.database.PlebbitDatabase;

import brugerautorisation.transport.soap.Brugeradmin;

@WebService(endpointInterface = "plebbit.IPlebbit")
public class PlebbitLogic extends UnicastRemoteObject implements IPlebbit{

	protected PlebbitLogic() throws RemoteException {
		super();
	}

	@Override
	public String login(String username, String password) {
		Brugeradmin ba;
		try {
			URL url = new URL("http://javabog.dk:9901/brugeradmin?wsdl");
			QName qname = new QName("http://soap.transport.brugerautorisation/", "BrugeradminImplService");
			Service service = Service.create(url, qname);
			ba = service.getPort(Brugeradmin.class);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
			return "";
		}
		try{
			ba.hentBruger(username, password);
			PlebbitDatabase base = new PlebbitDatabase();
			if(!base.userExists(username)){
				base.createUser(username);
			}
			Random rand = new Random();
			int num = rand.nextInt(1000);
			String token = System.nanoTime()+username.charAt(0)+""+num;
			base.updateToken(username, token);
			return token;
			
		} catch(IllegalArgumentException e){
			e.printStackTrace();
		}
		return "";
	}

}
