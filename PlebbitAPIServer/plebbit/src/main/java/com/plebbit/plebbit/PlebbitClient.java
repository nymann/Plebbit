package com.plebbit.plebbit;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.xml.ws.Service;

import org.omg.Messaging.SyncScopeHelper;

import javax.xml.namespace.QName;

public class PlebbitClient {

	public static void main(String[] args) throws MalformedURLException {
		URL plebbitUrl = new URL("http://gibbo.dk:9427/plebbit?wsdl");
        QName plebbitQName = new QName("http://plebbit.plebbit.com/", "PlebbitLogicService");
        Service plebbitService = Service.create(plebbitUrl, plebbitQName);
        IPlebbit iPlebbit = plebbitService.getPort(IPlebbit.class);
        String tok = iPlebbit.login("s144827", "kek");
        System.out.println(iPlebbit.tokenStillValid(tok));
        /*Scanner scanner = new Scanner(System.in);
        boolean isControlling = true;
        boolean isLoggedIn = false;
        String token = "";
        while(isControlling){
        	if(!isLoggedIn){
        		PlebbitClient.printOptionsNotLoggedIn();
        		int choice;
        		try{
        			choice = scanner.nextInt();
        			switch(choice){
        			case 1: 
        				
        				break;
        			case 2:
        				System.out.print("Type username: ");
        				String usrnam = scanner.nextLine();
        				if(usrnam.length() == 0 || usrnam.isEmpty()){
        					System.out.println("Invalid username");
        					break;
        				}
        				iPlebbit.forgotPassword(usrnam);
        				System.out.println("Sent email to recover password!");
        				break;
        			case 3: 
        				System.out.print("Type username: ");
        				String username = scanner.nextLine();
        				if(username.length() == 0 || username.isEmpty()){
        					System.out.println("Invalid username");
        					break;
        				}
        				System.out.print("Type old password: ");
        				String oldPassword = scanner.nextLine();
        				if(oldPassword.length() == 0 || oldPassword.isEmpty()){
        					System.out.println("Invalid old password");
        					break;
        				}
        				System.out.print("Type new password: ");
        				String newPassword = scanner.nextLine();
        				if(newPassword.length() == 0 || newPassword.isEmpty()){
        					System.out.println("Invalid new password");
        					break;
        				}
        				iPlebbit.changePassword(username, oldPassword, newPassword);
        				System.out.println("Changed password!");
        				break;
        			case 4:
        				isControlling = false; // <-- Soft exit
        				break;
        			}
        		} catch(NumberFormatException e){
        			e.printStackTrace();
        		}
        	} else{
        		PlebbitClient.printOptionsLoggedIn();
        	}
        }//*/
	}
	
	public static void printOptionsLoggedIn(){
		System.out.println("Choose options");
		System.out.println("1. Show lists");
		System.out.println("2. Create new list");
		System.out.println("3. Invite users to list");
		System.out.println("4. Delete list");
		System.out.println("5. Logout");
		System.out.println("6. Exit");
		System.out.println();
		System.out.println();
	}
	
	public static void printOptionsNotLoggedIn(){
		System.out.println("Choose options");
		System.out.println("1. Login");
		System.out.println("2. Forgot password");
		System.out.println("3. Change password");
		System.out.println("4. Exit");
		System.out.println();
		System.out.println();
	}
	
}
