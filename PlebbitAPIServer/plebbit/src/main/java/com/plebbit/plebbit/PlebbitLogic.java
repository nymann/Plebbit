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
import com.plebbit.dto.Item;
import com.plebbit.dto.ListProperties;
import com.plebbit.dto.User;
import com.plebbit.helpers.TimeTools;
import com.plebbit.helpers.WriteSomething;
import com.plebbit.rest.ETilbudsAvisREST;
import com.plebbit.rest.offer.Offer;

import brugerautorisation.transport.soap.Brugeradmin;

@WebService(endpointInterface = "com.plebbit.plebbit.IPlebbit")
public class PlebbitLogic extends UnicastRemoteObject implements IPlebbit{

	public PlebbitLogic() throws RemoteException {
		super();
	}

	@Override
	public String login(String username, String password) {
		System.out.println("START LOGIN");
		System.out.println("1"+System.currentTimeMillis());
		Brugeradmin ba;
		try {
			
			URL url = new URL("http://javabog.dk:9901/brugeradmin?wsdl");
			QName qname = new QName("http://soap.transport.brugerautorisation/", "BrugeradminImplService");
			Service service = Service.create(url, qname);
			ba = service.getPort(Brugeradmin.class);
			System.out.println("2"+System.currentTimeMillis());
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
			System.out.println("END LOGINERROR");
			return "";
		}
		try{
			System.out.println("3"+System.currentTimeMillis());
			ba.hentBruger(username, password);
			System.out.println("4"+System.currentTimeMillis());
			if(!PlebbitDatabase.db.userExists(username)){
				System.out.println("5"+System.currentTimeMillis());
				PlebbitDatabase.db.createUser(username);
				System.out.println("6"+System.currentTimeMillis());
			}
			System.out.println("7"+System.currentTimeMillis());
			Random rand = new Random();
			int num = rand.nextInt(1000);
			String token = System.nanoTime()+""+num;
			System.out.println("8"+System.currentTimeMillis());
			if(PlebbitDatabase.db.updateToken(username, token)){
				WriteSomething.writeInFile(WriteSomething.location, "User "+username+" updated token on login.");
			} else{
				WriteSomething.writeInFile(WriteSomething.location, "User "+username+" failed to update token on login.");
			}
			System.out.println("9"+System.currentTimeMillis());
			PlebbitDatabase.db.updateTime(username);
			System.out.println("10"+System.currentTimeMillis());
			WriteSomething.writeInFile(WriteSomething.location, "User "+username+" logged in!");
			System.out.println("11"+System.currentTimeMillis());
			System.out.println("END LOGIN");
			return token;
			
		} catch(IllegalArgumentException e){
			e.printStackTrace();
			WriteSomething.writeInFile(WriteSomething.location, "User "+username+" failed to log in!");
		}
		System.out.println("END LOGIN none");
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
			WriteSomething.writeInFile(WriteSomething.location, username+" request new password.");
		} catch (Exception e1) {
			e1.printStackTrace();
			WriteSomething.writeInFile(WriteSomething.location, username+" failed to request new password.");
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
			WriteSomething.writeInFile(WriteSomething.location, username+" changed password!");
		} catch (Exception e1) {
			e1.printStackTrace();
			WriteSomething.writeInFile(WriteSomething.location, username+" failed to change password!");
		}
	}

	@Override
	public int[] getListOfUser(String token) {
		String name = PlebbitDatabase.db.getUsernameFromToken(token);
		WriteSomething.writeInFile(WriteSomething.location, name+" calling method getListOfUser with properties: token="+token);
		if(!PlebbitDatabase.db.isValidToken(token)){
			return null;
		}
		String loadedTime = PlebbitDatabase.db.getTimeOnToken(token);
		if(TimeTools.isExpired(loadedTime)){
			WriteSomething.writeInFile(WriteSomething.location, name+" has expired token.");
			logout(token);
			return null;
		}
		PlebbitDatabase.db.updateTimeOnToken(token);
		WriteSomething.writeInFile(WriteSomething.location, name+" requested list from user.");
		int[] returntype = PlebbitDatabase.db.getListsForUser(name);
		if(returntype != null){
			WriteSomething.writeInFile(WriteSomething.location, name+" got list for user.");
		} else{
			WriteSomething.writeInFile(WriteSomething.location, name+" didnt get list for user.");
		}
		return returntype;
	}

	@Override
	public ListProperties getListFromId(int id, String token) {
		String name = PlebbitDatabase.db.getUsernameFromToken(token);
		WriteSomething.writeInFile(WriteSomething.location, name+" calling method getListFromId with properties: id="+id+" token="+token);
		if(!PlebbitDatabase.db.isValidToken(token)){
			return null;
		}
		String loadedTime = PlebbitDatabase.db.getTimeOnToken(token);
		if(TimeTools.isExpired(loadedTime)){
			WriteSomething.writeInFile(WriteSomething.location, name+" has expired token.");
			logout(token);
			return null;
		}
		PlebbitDatabase.db.updateTimeOnToken(token);
		WriteSomething.writeInFile(WriteSomething.location, name+" requested list from id "+id);
		ListProperties listToReturn = PlebbitDatabase.db.getList(id);;
		if(listToReturn != null){
			WriteSomething.writeInFile(WriteSomething.location, name+" finished request list from id "+id);
		} else{
			WriteSomething.writeInFile(WriteSomething.location, name+" failed to get requested list from id "+id);
		}
		return listToReturn;
	}

	@Override
	public boolean addUserToList(int listId, String token, String inviteUserName) {
		String name = PlebbitDatabase.db.getUsernameFromToken(token);
		WriteSomething.writeInFile(WriteSomething.location, name+" calling method addUserToList with properties: listId="+listId+" token="+token+" inviteUserName="+inviteUserName);
		if(!PlebbitDatabase.db.isValidToken(token)){
			return false;
		}
		String loadedTime = PlebbitDatabase.db.getTimeOnToken(token);
		if(TimeTools.isExpired(loadedTime)){
			WriteSomething.writeInFile(WriteSomething.location, name+" has expired token.");
			logout(token);
			return false;
		}
		PlebbitDatabase.db.updateTimeOnToken(token);
		boolean bool = PlebbitDatabase.db.addUserToList(inviteUserName, listId);
		PlebbitDatabase.db.updateListLastChanged(listId);
		if(bool){
			WriteSomething.writeInFile(WriteSomething.location, name+" added user "+inviteUserName+" to list with id "+listId);
		} else{
			WriteSomething.writeInFile(WriteSomething.location, name+" failed to add user "+inviteUserName+" to list with id "+listId);
		}
		return bool;
	}

	@Override
	public boolean createNewList(String token, String listname) {
		String name = PlebbitDatabase.db.getUsernameFromToken(token);
		WriteSomething.writeInFile(WriteSomething.location, name+" calling method createNewList with properties: token="+token+" listname="+listname);
		if(!PlebbitDatabase.db.isValidToken(token)){
			return false;
		}
		String loadedTime = PlebbitDatabase.db.getTimeOnToken(token);
		if(TimeTools.isExpired(loadedTime)){
			WriteSomething.writeInFile(WriteSomething.location, name+" has expired token.");
			logout(token);
			return false;
		}
		PlebbitDatabase.db.updateTimeOnToken(token);
		boolean bool = PlebbitDatabase.db.createList(listname, name);
		if(bool){
			WriteSomething.writeInFile(WriteSomething.location, name+" added a new list "+listname);
		} else{
			WriteSomething.writeInFile(WriteSomething.location, name+" failed to add a new list "+listname);
		}
		return bool;
		
	}

	@Override
	public boolean deleteList(String token, int listId) {
		String name = PlebbitDatabase.db.getUsernameFromToken(token);
		WriteSomething.writeInFile(WriteSomething.location, name+" calling method deleteList with properties: token="+token+" listId="+listId);
		if(!PlebbitDatabase.db.isValidToken(token)){
			return false;
		}
		String loadedTime = PlebbitDatabase.db.getTimeOnToken(token);
		if(TimeTools.isExpired(loadedTime)){
			WriteSomething.writeInFile(WriteSomething.location, name+" has expired token.");
			logout(token);
			return false;
		}
		PlebbitDatabase.db.updateTimeOnToken(token);
		boolean bool = PlebbitDatabase.db.deleteList(token, listId);
		if(bool){
			WriteSomething.writeInFile(WriteSomething.location, name+" deleted a list with id "+listId);
		} else{
			WriteSomething.writeInFile(WriteSomething.location, name+" failed to delete a list with id "+listId);
		}
		return bool;
	}

	@Override
	public boolean addItemToList(String token, String item, int listId) {
		String name = PlebbitDatabase.db.getUsernameFromToken(token);
		WriteSomething.writeInFile(WriteSomething.location, name+" calling method addItemToList with properties: token="+token+" item="+item+" listId="+listId);
		if(!PlebbitDatabase.db.isValidToken(token)){
			return false;
		}
		String loadedTime = PlebbitDatabase.db.getTimeOnToken(token);
		if(TimeTools.isExpired(loadedTime)){
			WriteSomething.writeInFile(WriteSomething.location, name+" has expired token.");
			logout(token);
			return false;
		}
		PlebbitDatabase.db.updateTimeOnToken(token);
		boolean bool = PlebbitDatabase.db.addItem(item, listId, PlebbitDatabase.db.getIdFromUsername(name));
		PlebbitDatabase.db.updateListLastChanged(listId);
		if(bool){
			WriteSomething.writeInFile(WriteSomething.location, name+" added item "+item+" to list with id "+listId);
		} else{
			WriteSomething.writeInFile(WriteSomething.location, name+" failed to add item "+item+" to list with id "+listId);
		}
		
		return bool;
	}

	@Override
	public boolean removeItemFromList(String token, String item, int listId) {
		String name = PlebbitDatabase.db.getUsernameFromToken(token);
		WriteSomething.writeInFile(WriteSomething.location, name+" calling method removeItemFromList with properties: token="+token+" item="+item+" listId="+listId);
		if(!PlebbitDatabase.db.isValidToken(token)){
			return false;
		}
		String loadedTime = PlebbitDatabase.db.getTimeOnToken(token);
		if(TimeTools.isExpired(loadedTime)){
			WriteSomething.writeInFile(WriteSomething.location, name+" has expired token.");
			logout(token);
			return false;
		}
		PlebbitDatabase.db.updateTimeOnToken(token);
		boolean bool = PlebbitDatabase.db.removeItem(item, listId, PlebbitDatabase.db.getIdFromUsername(name));
		PlebbitDatabase.db.updateListLastChanged(listId);
		if(bool){
			WriteSomething.writeInFile(WriteSomething.location, name+" removed item "+item+" from list with id "+listId);
		} else{
			WriteSomething.writeInFile(WriteSomething.location, name+" failed to remove item "+item+" from list with id "+listId);
		}
		return bool;
	}

	@Override
	public void logout(String token) {
		String name = PlebbitDatabase.db.getUsernameFromToken(token);
		WriteSomething.writeInFile(WriteSomething.location, name+" logged out.");
		PlebbitDatabase.db.updateToken(name, "");
	}

	@Override
	public boolean tokenStillValid(String token) {
		String name = PlebbitDatabase.db.getUsernameFromToken(token);
		if(token.isEmpty() || token.equals("")){
			WriteSomething.writeInFile(WriteSomething.location, name+" token still not valid.");
			return false;
		}
		int[] returned = getListOfUser(token);
		if(returned != null){
			WriteSomething.writeInFile(WriteSomething.location, name+" token still valid.");
			return true;
		}
		WriteSomething.writeInFile(WriteSomething.location, name+" token still not valid.");
		return false;
	}

	@Override
	public int getPassedSecondsSinceLastChange(int listid) {
		String str = PlebbitDatabase.db.getListLastChanged(listid);
		if(str.isEmpty() || str.equals("")){
			return 0;
		}
		long convertedStr;
		try{
			convertedStr = Long.parseLong(str);
		} catch(NumberFormatException e){
			e.printStackTrace();
			return 0;
		}
		long cur = System.nanoTime();
		long diff = cur - convertedStr;
		int secs = (int)(diff / 1000000000L);
		return secs;
	}

	@Override
	public boolean setBoughtItem(int listId, String itemName, boolean bought, String token) {
		String name = PlebbitDatabase.db.getUsernameFromToken(token);
		WriteSomething.writeInFile(WriteSomething.location, name+" calling method setBoughtItem with properties: token="+token+" itemName="+itemName+" listId="+listId+" bought="+bought);
		if(!PlebbitDatabase.db.isValidToken(token)){
			return false;
		}
		String loadedTime = PlebbitDatabase.db.getTimeOnToken(token);
		if(TimeTools.isExpired(loadedTime)){
			WriteSomething.writeInFile(WriteSomething.location, name+" has expired token.");
			logout(token);
			return false;
		}
		int[] lists = getListOfUser(token);
		boolean isPart = false;
		for(int i = 0; i < lists.length; i++){
			if(lists[i] == listId){
				isPart = true;
				break;
			}
		}
		if(isPart){
			if(PlebbitDatabase.db.setBoughtItem(listId, itemName, bought)){
				WriteSomething.writeInFile(WriteSomething.location, name+" set "+itemName+" in list "+listId+" to "+bought);
				return true;
			} else{
				WriteSomething.writeInFile(WriteSomething.location, name+" failed to set "+itemName+" in list "+listId+" to "+bought);
			}
		} else{
			WriteSomething.writeInFile(WriteSomething.location, name+" is not in list "+listId);
		}
		return false;
	}

	@Override
	public Item getItem(int listId, String itemName, String token) {
		String name = PlebbitDatabase.db.getUsernameFromToken(token);
		WriteSomething.writeInFile(WriteSomething.location, name+" calling method getItem with properties: token="+token+" itemName="+itemName+" listId="+listId);
		if(!PlebbitDatabase.db.isValidToken(token)){
			return null;
		}
		String loadedTime = PlebbitDatabase.db.getTimeOnToken(token);
		if(TimeTools.isExpired(loadedTime)){
			WriteSomething.writeInFile(WriteSomething.location, name+" has expired token.");
			logout(token);
			return null;
		}
		int[] lists = getListOfUser(token);
		boolean isPart = false;
		for(int i = 0; i < lists.length; i++){
			if(lists[i] == listId){
				isPart = true;
				break;
			}
		}
		if(isPart){
			Item item = PlebbitDatabase.db.getItem(listId, itemName);
			if(item != null){
				WriteSomething.writeInFile(WriteSomething.location, name+" has gotten item from list "+listId+".");
			} else{
				WriteSomething.writeInFile(WriteSomething.location, name+" has failed to get item from list "+listId+".");
			}
			return item;
		} else{
			WriteSomething.writeInFile(WriteSomething.location, name+" is not part of list "+listId+".");
			return null;
		}
	}

	@Override
	public boolean renameItemName(int listId, String itemName, String newItemName, String token) {
		String name = PlebbitDatabase.db.getUsernameFromToken(token);
		WriteSomething.writeInFile(WriteSomething.location, name+" calling method renameItemName with properties: token="+token+" itemName="+itemName+" listId="+listId);
		if(!PlebbitDatabase.db.isValidToken(token)){
			return false;
		}
		String loadedTime = PlebbitDatabase.db.getTimeOnToken(token);
		if(TimeTools.isExpired(loadedTime)){
			WriteSomething.writeInFile(WriteSomething.location, name+" has expired token.");
			logout(token);
			return false;
		}
		int[] lists = getListOfUser(token);
		boolean isPart = false;
		for(int i = 0; i < lists.length; i++){
			if(lists[i] == listId){
				isPart = true;
				break;
			}
		}
		if(isPart){
			WriteSomething.writeInFile(WriteSomething.location, name+" is part of list "+listId);
			if(PlebbitDatabase.db.setItemName(listId, itemName, newItemName)){
				WriteSomething.writeInFile(WriteSomething.location, name+" renamed "+itemName+" to "+newItemName+" in list "+listId+".");
			} else{
				WriteSomething.writeInFile(WriteSomething.location, name+" failed to rename item in list "+listId+".");
			}
		} else{
			WriteSomething.writeInFile(WriteSomething.location, name+" is not part of list "+listId+".");
		}
		return false;
	}

	@Override
	public boolean renameListName(int listId, String newListName, String token) {
		String name = PlebbitDatabase.db.getUsernameFromToken(token);
		WriteSomething.writeInFile(WriteSomething.location, name+" calling method renameListName with properties: token="+token+" listName="+newListName+" listId="+listId);
		if(!PlebbitDatabase.db.isValidToken(token)){
			return false;
		}
		String loadedTime = PlebbitDatabase.db.getTimeOnToken(token);
		if(TimeTools.isExpired(loadedTime)){
			WriteSomething.writeInFile(WriteSomething.location, name+" has expired token.");
			logout(token);
			return false;
		}
		int[] lists = getListOfUser(token);
		boolean isPart = false;
		for(int i = 0; i < lists.length; i++){
			if(lists[i] == listId){
				isPart = true;
				break;
			}
		}
		if(isPart){
			WriteSomething.writeInFile(WriteSomething.location, name+" is part of list "+listId);
			if(PlebbitDatabase.db.setListName(listId, newListName)){
				WriteSomething.writeInFile(WriteSomething.location, name+" renamed list to name "+newListName+" in list "+listId+".");
			} else{
				WriteSomething.writeInFile(WriteSomething.location, name+" failed to rename list in list "+listId+".");
			}
		} else{
			WriteSomething.writeInFile(WriteSomething.location, name+" is not part of list "+listId+".");
		}
		return false;
	}

	@Override
	public Item[] getPricesForListFromNetto(int listId, String token) {
		String name = PlebbitDatabase.db.getUsernameFromToken(token);
		WriteSomething.writeInFile(WriteSomething.location, name+" calling method getPricesForListFromNetto with properties: token="+token+" listId="+listId);
		if(!PlebbitDatabase.db.isValidToken(token)){
			return null;
		}
		String loadedTime = PlebbitDatabase.db.getTimeOnToken(token);
		if(TimeTools.isExpired(loadedTime)){
			WriteSomething.writeInFile(WriteSomething.location, name+" has expired token.");
			logout(token);
			return null;
		}
		ListProperties props = this.getListFromId(listId, token);
		String save = "";
		for(int i = 0; i < props.items.size(); i++){
			save += props.items.get(i).name+" ";
		}
		WriteSomething.writeInFile(WriteSomething.location, name+ " requested prices for : "+save);
		Item[] temp = new Item[props.items.size()];
		for(int i = 0; i < props.items.size(); i++){
			temp[i] = props.items.get(i);
		}
		Item[] doubs = ETilbudsAvisREST.getPriceFromListOfItems(temp);
		return doubs;
	}

	@Override
	public Offer[] getOffersFromItemFRomNetto(String name, String token) {
		String nameofuser = PlebbitDatabase.db.getUsernameFromToken(token);
		WriteSomething.writeInFile(WriteSomething.location, nameofuser+" calling method getOffersFromItemFRomNetto with properties: token="+token+" itemname="+name);
		if(!PlebbitDatabase.db.isValidToken(token)){
			return null;
		}
		String loadedTime = PlebbitDatabase.db.getTimeOnToken(token);
		if(TimeTools.isExpired(loadedTime)){
			WriteSomething.writeInFile(WriteSomething.location, name+" has expired token.");
			logout(token);
			return null;
		}
		WriteSomething.writeInFile(WriteSomething.location, name+ " requested prices for : "+name);
		return ETilbudsAvisREST.getOffersFromItem(name);
	}
}
