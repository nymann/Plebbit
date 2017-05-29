package com.plebbit.test;

import java.net.URL;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.xml.namespace.QName;
import javax.xml.ws.Endpoint;
import javax.xml.ws.Service;

import com.plebbit.database.PlebbitDatabase;
import com.plebbit.dto.ListProperties;
import com.plebbit.plebbit.IPlebbit;
import com.plebbit.plebbit.PlebbitLogic;
import com.plebbit.rest.ETilbudsAvisREST;

public class TestMain {

	public static void main(String[] args) {
		System.out.println(System.currentTimeMillis());
		PlebbitDatabase db = new PlebbitDatabase();
		System.out.println(System.currentTimeMillis());
		/*
		
		try {
			db.checkTables();
			System.out.println("Database checking fine...");
		} catch (SQLException e1) {
			e1.printStackTrace();
			System.exit(1);
		}
		PlebbitLogic spil = null;
		try {
			spil = new PlebbitLogic();
			Endpoint.publish("http://[::]:9427/plebbit", spil);
			System.out.println("Server hosting fine...");
		} catch (RemoteException e) {
			e.printStackTrace();
			System.exit(1);
		}
		String token = spil.login("s144833", "kek");
		if(!db.userExists("s144827")){
			System.out.println("Failed creating user from login...");
		}
		spil.createNewList(token, "wtffffffff");
		System.out.println(spil.tokenStillValid(token));
		/*
		System.out.println("Token received back: "+token);
		String token2 = spil.login("s144833", "kek");
		if(!db.userExists("s144833")){
			System.out.println("Failed creating user from login2...");
		}
		spil.createNewList(token, "test");
		int[] lists = spil.getListOfUser(token);
		System.out.println("Length of lists: "+lists.length);
		ArrayList<ListProperties> listpros = new ArrayList<ListProperties>();
		for(int i = 0; i < lists.length; i++){
			ListProperties probs = spil.getListFromId(lists[i], token);
			System.out.println("Size of list: #"+i+" : "+probs.users.size());
			listpros.add(probs);
		}
		for(int i = 0; i < listpros.size(); i++){
			System.out.println(listpros.get(i).nameOfList);
		}
		int idWithTest = 0;
		for(int i = 0; i < listpros.size(); i++){
			if(listpros.get(i).nameOfList.equals("test")){
				spil.addItemToList(token, "testitem", listpros.get(i).listId);
				idWithTest = listpros.get(i).listId;
				break;
				
			}
		}
		spil.deleteList(token, idWithTest);
		*/
		System.exit(1);
	}

}
