package com.plebbit.rest;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.ws.rs.POST;
import javax.ws.rs.WebApplicationException;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import com.plebbit.rest.offer.Offer;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;


public class ETilbudsAvisREST {

	public static final String secret = "00j278fc1wdew9pbvoqud1buaeh7vx3n";
	public static final String normal = "00j278fc1wyfnymdnvb0sm8rnz4qpc4h";
	
	public static double[] getPriceFromListOfItems(String[] items){
		double[] toBeReturned = new double[items.length];
		Client client = Client.create();
        WebResource webResource = client.resource("https://api.etilbudsavis.dk/v2/sessions");

        ClientResponse response = webResource
                .queryParam("api_key", normal)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .post(ClientResponse.class);

        if (response.getStatus() != 200 && response.getStatus() != 201) {
        	return toBeReturned;
        }

        BasicResponse basicResponse = response.getEntity(BasicResponse.class);
		String xSignature = generateSHA256(secret.concat(basicResponse.token));
		String xToken = basicResponse.token;

		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("_token", xToken);
		queryParams.add("_signature", xSignature);

		ClientResponse sessionInformation = webResource
		        .queryParams(queryParams)
		        .header("Content-Type", "application/json")
		        .header("Accept", "application/json")
		        .get(ClientResponse.class);
		
		for(int i = 0; i < items.length; i++){
			WebResource dealerResource = client.resource("https://api.etilbudsavis.dk/v2/offers/search");
		    MultivaluedMap<String, String> dealerParams = new MultivaluedMapImpl();
		    dealerParams.add("_token", xToken);
		    dealerParams.add("_signature", xSignature);
		    dealerParams.add("query", items[i]);
		    dealerParams.add("offset", "0");
		    dealerParams.add("limit", "50");
		    ClientResponse dealerResponse = dealerResource
		            .queryParams(dealerParams)
		            .header("Content-Type", "application/json")
		            .header("Accept", "application/json")
		            .get(ClientResponse.class);
		    Offer[] offers = dealerResponse.getEntity(Offer[].class);
		    for(int x = 0; x < offers.length; x++){
		    	
		    	if(offers[x].branding.name.toLowerCase().equals("netto") && offers[x].pricing.currency.toLowerCase().equals("dkk")){
		    		//System.out.println("#"+x+" : HEADING "+offers[x].heading+" : DESCRIPTION "+offers[x].description+" : BRANDING "+offers[x].branding.name+" : CURRENCY "+offers[x].pricing.currency);
			    	toBeReturned[i] = offers[x].pricing.price;
			    	break;
			    }
		    }    
		}
		for(int i = 0; i < toBeReturned.length; i++){
			System.out.println("#fghfgh"+i+" : "+items[i]+" : "+toBeReturned[i]);
		}
		return toBeReturned;
	}
	
	public static Offer[] getOffersFromItem(String item){
		Client client = Client.create();
        WebResource webResource = client.resource("https://api.etilbudsavis.dk/v2/sessions");

        ClientResponse response = webResource
                .queryParam("api_key", normal)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .post(ClientResponse.class);

        if (response.getStatus() != 200 && response.getStatus() != 201) {
        	return null;
        }

        BasicResponse basicResponse = response.getEntity(BasicResponse.class);
		String xSignature = generateSHA256(secret.concat(basicResponse.token));
		String xToken = basicResponse.token;

		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("_token", xToken);
		queryParams.add("_signature", xSignature);

		ClientResponse sessionInformation = webResource
		        .queryParams(queryParams)
		        .header("Content-Type", "application/json")
		        .header("Accept", "application/json")
		        .get(ClientResponse.class);
		
		WebResource dealerResource = client.resource("https://api.etilbudsavis.dk/v2/offers/search");
		MultivaluedMap<String, String> dealerParams = new MultivaluedMapImpl();
		dealerParams.add("_token", xToken);
		dealerParams.add("_signature", xSignature);
		dealerParams.add("query", item);
		dealerParams.add("offset", "0");
		dealerParams.add("limit", "50");
		ClientResponse dealerResponse = dealerResource
		        .queryParams(dealerParams)
		        .header("Content-Type", "application/json")
		        .header("Accept", "application/json")
		        .get(ClientResponse.class);
		Offer[] offers = dealerResponse.getEntity(Offer[].class);
		return offers;
	}


	
	/**
	 * Taken from docs.api.etilbudsavis.dk
	 * @param string
	 * @return
	 */
	private static String generateSHA256(String string) {
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
