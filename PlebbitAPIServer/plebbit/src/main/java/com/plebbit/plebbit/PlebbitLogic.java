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
import com.plebbit.dto.ListProperties;
import com.plebbit.helpers.TimeTools;

import brugerautorisation.transport.soap.Brugeradmin;

@WebService(endpointInterface = "com.plebbit.plebbit.IPlebbit")
public class PlebbitLogic extends UnicastRemoteObject implements IPlebbit{

	public PlebbitLogic() throws RemoteException {
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
			if(!PlebbitDatabase.db.userExists(username)){
				PlebbitDatabase.db.createUser(username);
			}
			Random rand = new Random();
			int num = rand.nextInt(1000);
			String token = System.nanoTime()+""+num;
			PlebbitDatabase.db.updateToken(username, token);
			PlebbitDatabase.db.updateTime(username);
			return token;
			
		} catch(IllegalArgumentException e){
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public void forgotPassword(String username) {
		Brugeradmin ba = null;
		try {
			URL url = new URL("http://javabog.dk:9901/brugeradmin?wsdl");
			QName qname = new QName("http://soap.transport.brugerautorisation/", "BrugeradminImplService");
			Service service = Service.create(url, qname);
			ba = service.getPort(Brugeradmin.class);
			ba.sendGlemtAdgangskodeEmail(username, "Sent from plebbit server");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void changePassword(String username, String oldPassword, String newPassword) {
		Brugeradmin ba = null;
		try {
			URL url = new URL("http://javabog.dk:9901/brugeradmin?wsdl");
			QName qname = new QName("http://soap.transport.brugerautorisation/", "BrugeradminImplService");
			Service service = Service.create(url, qname);
			ba = service.getPort(Brugeradmin.class);
			ba.ændrAdgangskode(username, oldPassword, newPassword);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public int[] getListOfUser(String token) {
		if(!PlebbitDatabase.db.isValidToken(token)){
			return null;
		}
		String loadedTime = PlebbitDatabase.db.getTimeOnToken(token);
		if(TimeTools.isExpired(loadedTime)){
			logout(token);
			return null;
		}
		PlebbitDatabase.db.updateTimeOnToken(token);
		return PlebbitDatabase.db.getListsForUser(PlebbitDatabase.db.getUsernameFromToken(token));
	}

	@Override
	public ListProperties getListFromId(int id, String token) {
		if(!PlebbitDatabase.db.isValidToken(token)){
			return null;
		}
		String loadedTime = PlebbitDatabase.db.getTimeOnToken(token);
		if(TimeTools.isExpired(loadedTime)){
			logout(token);
			return null;
		}
		PlebbitDatabase.db.updateTimeOnToken(token);
		return PlebbitDatabase.db.getList(id);
	}

	@Override
	public boolean addUserToList(int listId, String token, String inviteUserName) {
		if(!PlebbitDatabase.db.isValidToken(token)){
			return false;
		}
		String loadedTime = PlebbitDatabase.db.getTimeOnToken(token);
		if(TimeTools.isExpired(loadedTime)){
			logout(token);
			return false;
		}
		PlebbitDatabase.db.updateTimeOnToken(token);
		boolean bool = PlebbitDatabase.db.addUserToList(inviteUserName, listId);
		PlebbitDatabase.db.updateListLastChanged(listId);
		return bool;
	}

	@Override
	public boolean createNewList(String token, String listname) {
		if(!PlebbitDatabase.db.isValidToken(token)){
			System.out.println("WUTFL");
			return false;
		}
		String loadedTime = PlebbitDatabase.db.getTimeOnToken(token);
		if(TimeTools.isExpired(loadedTime)){
			logout(token);
			System.out.println("YEOKINICEGAMEJAMFELX");
			return false;
		}
		PlebbitDatabase.db.updateTimeOnToken(token);
		return PlebbitDatabase.db.createList(listname, PlebbitDatabase.db.getUsernameFromToken(token));	
		
	}

	@Override
	public boolean deleteList(String token, int listId) {
		if(!PlebbitDatabase.db.isValidToken(token)){
			return false;
		}
		String loadedTime = PlebbitDatabase.db.getTimeOnToken(token);
		if(TimeTools.isExpired(loadedTime)){
			logout(token);
			return false;
		}
		PlebbitDatabase.db.updateTimeOnToken(token);
		return PlebbitDatabase.db.deleteList(token, listId);
	}

	@Override
	public boolean addItemToList(String token, String item, int listId) {
		if(!PlebbitDatabase.db.isValidToken(token)){
			return false;
		}
		String loadedTime = PlebbitDatabase.db.getTimeOnToken(token);
		if(TimeTools.isExpired(loadedTime)){
			logout(token);
			return false;
		}
		PlebbitDatabase.db.updateTimeOnToken(token);
		boolean bool = PlebbitDatabase.db.addItem(item, listId, PlebbitDatabase.db.getIdFromUsername(PlebbitDatabase.db.getUsernameFromToken(token)));
		PlebbitDatabase.db.updateListLastChanged(listId);
		return bool;
	}

	@Override
	public boolean removeItemFromList(String token, String item, int listId) {
		if(!PlebbitDatabase.db.isValidToken(token)){
			return false;
		}
		String loadedTime = PlebbitDatabase.db.getTimeOnToken(token);
		if(TimeTools.isExpired(loadedTime)){
			logout(token);
			return false;
		}
		PlebbitDatabase.db.updateTimeOnToken(token);
		boolean bool = PlebbitDatabase.db.removeItem(item, listId, PlebbitDatabase.db.getIdFromUsername(PlebbitDatabase.db.getUsernameFromToken(token)));
		PlebbitDatabase.db.updateListLastChanged(listId);
		return bool;
	}

	@Override
	public void logout(String token) {
		PlebbitDatabase.db.updateToken(PlebbitDatabase.db.getUsernameFromToken(token), "");
	}

	@Override
	public boolean tokenStillValid(String token) {
		if(token.isEmpty() || token.equals("")){
			return false;
		}
		int[] returned = getListOfUser(token);
		if(returned != null){
			return true;
		}
		return false;
	}

	@Override
	public int getPassedSecondsSinceLastChange(int listid) {
		String str = PlebbitDatabase.db.getListLastChanged(listid);
		long convertedStr = Long.parseLong(str);
		long cur = System.nanoTime();
		long diff = cur - convertedStr;
		int secs = (int)(diff / 1000000000L);
		return secs;
	}
}
