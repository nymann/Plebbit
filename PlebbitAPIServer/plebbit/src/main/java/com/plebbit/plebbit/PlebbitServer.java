package com.plebbit.plebbit;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.xml.ws.Endpoint;

public class PlebbitServer {
	public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException{
		PlebbitLogic spil = new PlebbitLogic();
		Endpoint.publish("http://[::]:9427/plebbit", spil);
		
	}
}
