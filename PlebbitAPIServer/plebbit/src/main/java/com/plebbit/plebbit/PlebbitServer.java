package com.plebbit.plebbit;

import java.rmi.RemoteException;
import java.sql.SQLException;

import javax.xml.ws.Endpoint;

import com.plebbit.database.PlebbitDatabase;
import com.plebbit.helpers.WriteSomething;


public class PlebbitServer {

	public static void main(String[] args) {
		PlebbitDatabase db = new PlebbitDatabase();
		PlebbitDatabase.db = db;
		try {
			db.checkTables();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		PlebbitLogic spil;
		try {
			spil = new PlebbitLogic();
			Endpoint.publish("http://[::]:9427/plebbit", spil);
			WriteSomething.writeInFile(WriteSomething.location, "Started the server.");
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
}
