package com.plebbit.plebbit;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.ws.Service;

import javax.xml.namespace.QName;

public class PlebbitClient {

	public static void main(String[] args) throws MalformedURLException {
		URL plebbitUrl = new URL("http://gibbo.dk:9427/plebbit?wsdl");
        QName plebbitQName = new QName("http://plebbit.plebbit.com/", "PlebbitLogicService");
        Service plebbitService = Service.create(plebbitUrl, plebbitQName);
        IPlebbit iPlebbit = plebbitService.getPort(IPlebbit.class);
        System.out.println(iPlebbit.login("s144827", "kek"));
	}
	
}
