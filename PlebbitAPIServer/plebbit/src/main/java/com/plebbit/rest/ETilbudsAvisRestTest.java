package com.plebbit.rest;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.ws.rs.POST;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;


public class ETilbudsAvisRestTest {

	public static final String secret = "00j278fc1wdew9pbvoqud1buaeh7vx3n";
	public static final String normal = "00j278fc1wyfnymdnvb0sm8rnz4qpc4h";
	
	public static void main(String[] args) {
		com.sun.jersey.api.client.Client client = com.sun.jersey.api.client.Client.create();
		WebResource webResource = client.resource("https://api.etilbudsavis.dk/v2/sessions");
		
		
		ClientResponse response = webResource.queryParam("api_key", normal).type("application/json")
				.post(ClientResponse.class);
		
		System.out.println("RES: "+response.getLength());
		System.out.println("RES: "+response.getLanguage());
		System.out.println("RES: "+response.getStatus());
		System.out.println("RES: "+response.getClient().toString());
		System.out.println("RES: "+response.getHeaders().toString());
		System.out.println("RES: "+response.getClient().getProperties().toString());
		try {
			BasicResponse res = response.getEntity(BasicResponse.class);
			System.out.println(res.token+" "+res.client_id+" "+res.expires);
			String toHash = generateSHA256(secret.concat(res.token));
			System.out.println(toHash);
			WebResource dealer = client.resource("https://api.etilbudsavis.dk/v2/dealers/70d42L");//9ba51");
			//dealer.header("X-Token", res.token);
			//dealer.header("X-Signature", toHash);
			ClientResponse dealerResponse = webResource.queryParam("_token", res.token).queryParam("_signature", toHash).type("application/json").post(ClientResponse.class);
			Dealer deal = dealerResponse.getEntity(Dealer.class);
			System.out.println("RES: "+dealerResponse.getStatus());
			System.out.println("RES: "+dealerResponse.getHeaders().toString());
			System.out.println(deal.name+" "+deal.url_name+" "+deal.website+" "+deal.id+" "+deal.ern+" "+deal.logo);
		} catch(WebApplicationException e){
			e.printStackTrace();
		}
		
		///v2/sessions?_token=<your token here>
		
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
	/**
	 * Taken from docs.api.etilbudsavis.dk
	 * @param string
	 * @return
	 */
	public static String generateSHA256(String string) {
	    MessageDigest digest=null;
	    String hash = "";
	    try {
	        digest = MessageDigest.getInstance("SHA-256");
	        digest.update(string.getBytes());
	        byte[] bytes = digest.digest();
	        
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < bytes.length; i++) {
	            String hex = Integer.toHexString(0xFF & bytes[i]);
	            if (hex.length() == 1) {
	                sb.append('0');
	            }
	            sb.append(hex);
	        }
	        hash = sb.toString();
	    } catch (NoSuchAlgorithmException e1) {
	        e1.printStackTrace();
	    }
	    return hash;
	}
	
}
