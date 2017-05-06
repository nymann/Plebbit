package com.plebbit.rest;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class SpellCheckRest {

	public static String keyOne = "bce54384932c43ada3e54e69c0a0cc04";
	public static String keyTwo = "097dd0fc7de14802bb8c06ce50458414";
	
	/**
	 * returns null if error, return empty array if correct, otherwise return suggestions with highest score first
	 * @param word
	 * @return
	 */
	public static String[] getSuggestionsBasedOnWord(String word){
		com.sun.jersey.api.client.Client client = com.sun.jersey.api.client.Client.create();
		WebResource webResource = client.resource("https://api.cognitive.microsoft.com/bing/v5.0/spellcheck/");
			
		
		ClientResponse response = webResource.header("Ocp-Apim-Subscription-Key", keyOne).
				type("application/x-www-form-urlencoded").
				
				post(ClientResponse.class, "text="+word);
		Suggestion res = response.getEntity(Suggestion.class);
		System.out.println("RES: "+response.getLength());
		System.out.println("RES: "+response.getLanguage());
		System.out.println("RES: "+response.getStatus());
		System.out.println("RES: "+response.getClient().toString());
		System.out.println("RES: "+response.getHeaders().toString());
		System.out.println("RES: "+response.getClient().getProperties().toString());
		System.out.println("type: "+res._type);
		try{
			System.out.println("LENGTH: "+res.flaggedTokens.length);
			if(res.flaggedTokens.length > 0){
				System.out.println("For token: "+res.flaggedTokens[0].token);
				System.out.println("Have suggestions: ");
				Suggestions[] sugg = res.flaggedTokens[0].suggestions;
				String[] array = new String[sugg.length];
				for(int i = 0; i < sugg.length; i++){
					System.out.println("#"+i+" : "+sugg[i].suggestion+" with score "+sugg[i].score);
					array[i] = sugg[i].suggestion;
				}
				return array;
			}
		} catch(NullPointerException e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		String[] suggs = getSuggestionsBasedOnWord("Cabr+dfg+df+heyu+eg+df");
		if(suggs != null){
			for(int i = 0; i < suggs.length; i++){
				System.out.println("#"+i+" : "+suggs[i]);
			}
		}
	}
	
}
