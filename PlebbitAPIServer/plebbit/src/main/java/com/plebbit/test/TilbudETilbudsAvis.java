package com.plebbit.test;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Project name: plebbit
 * Developed by: Nymann
 * GitHub: github.com/Nymann/plebbit
 */
public class TilbudETilbudsAvis {
    private static final String apiKey = "00j278fc1wyfnymdnvb0sm8rnz4qpc4h";
    private static String shaApiKey = "";

    private TilbudETilbudsAvis() {
        shaApiKey = generateSHA256(apiKey);
        com.sun.jersey.api.client.Client client = com.sun.jersey.api.client.Client.create();
        WebResource webResource = client.resource("https://api.etilbudsavis.dk/v2/sessions");


        ClientResponse response = webResource.queryParam("api_key", apiKey).type("application/json")
                .post(ClientResponse.class);

        System.out.println(response.toString());

    }

    public static void main(String[] args) {
        new TilbudETilbudsAvis();
    }

    private static String generateSHA256(String string) {
        MessageDigest digest;
        String hash = "";
        try {
            digest = MessageDigest.getInstance("SHA-256");
            digest.update(string.getBytes());
            byte[] bytes = digest.digest();

            StringBuffer sb = new StringBuffer();
            for (byte aByte : bytes) {
                String hex = Integer.toHexString(0xFF & aByte);
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
