package com.plebbit.rest;

import javax.ws.rs.POST;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;


public class RestTtest {

	public static final String secret = "00j278fc1wdew9pbvoqud1buaeh7vx3n";
	public static final String normal = "00j278fc1wyfnymdnvb0sm8rnz4qpc4h";
	
	public static void main(String[] args) {
		com.sun.jersey.api.client.Client client = com.sun.jersey.api.client.Client.create();
		WebResource webResource = client.resource("https://api.etilbudsavis.dk");///v2/sessions");
		
		
		ClientResponse response = webResource.queryParam("api_key", normal).type("application/json")
				
				.post(ClientResponse.class);
		
		System.out.println("RES: "+response.getLength());
		System.out.println("RES: "+response.getLanguage());
		System.out.println("RES: "+response.getStatus());
		System.out.println("RES: "+response.getClient().toString());
		System.out.println("RES: "+response.getHeaders().toString());
		System.out.println("RES: "+response.getClient().getProperties().toString());
		System.out.println("RES: "+response.getEntity(String.class));
		/*
		if(response.getStatus()!=201){
			throw new RuntimeException("HTTP Error: "+ response.getStatus());
		}
		
		*/

		
		/*
		String link = "https://api.etilbudsavis.dk/v2/sessions";
    	Client client = ClientBuilder.newClient();
        Response res = client.target(link).queryParam("api_key", "0j278fc1wyfnymdnvb0sm8rnz4qpc4h").request(MediaType.APPLICATION_JSON).get();
        System.out.println("res: "+res.toString());
        */
	}
	
}
