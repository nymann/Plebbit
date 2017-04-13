package com.plebbit.plebbit;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.xml.ws.Service;

import org.omg.Messaging.SyncScopeHelper;

import com.plebbit.dto.ListProperties;

import javax.xml.namespace.QName;

public class PlebbitClient {

	public static void main(String[] args) throws MalformedURLException {
		URL plebbitUrl = new URL("http://gibbo.dk:9427/plebbit?wsdl");
        QName plebbitQName = new QName("http://plebbit.plebbit.com/", "PlebbitLogicService");
        Service plebbitService = Service.create(plebbitUrl, plebbitQName);
        IPlebbit iPlebbit = plebbitService.getPort(IPlebbit.class);
        Scanner scanner = new Scanner(System.in);
        boolean isControlling = true;
        boolean isLoggedIn = false;
        String token = "";
        while(isControlling){
        	if(!isLoggedIn){
        		PlebbitClient.printOptionsNotLoggedIn();
        		int choice;
        		try{
        			choice = Integer.parseInt(scanner.nextLine());
        			switch(choice){
        			case 1: 
        				System.out.print("Type username: ");
        				String user = scanner.nextLine();
        				if(user.length() == 0 || user.isEmpty()){
        					System.out.println("Invalid username");
        					break;
        				}
        				System.out.print("Type password: ");
        				String pass = scanner.nextLine();
        				if(user.length() == 0 || user.isEmpty()){
        					System.out.println("Invalid old password");
        					break;
        				}
        				String resTok = iPlebbit.login(user, pass);
        				if(resTok.length() > 0){
        					System.out.println("You Logged in!");
        					isLoggedIn = true;
        					token = resTok;
        				} else{
        					System.out.println("Wrong password/username!");
        				}
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
        		int choice;
        		try{
        			choice = Integer.parseInt(scanner.nextLine());
        			switch(choice){
        			case 1: 
        				int[] listIds = iPlebbit.getListOfUser(token);
        				if(listIds.length == 0){
        					break;
        				}
        				ListProperties[] lists = new ListProperties[listIds.length];
        				for(int i = 0; i < listIds.length; i++){
        					lists[i] = iPlebbit.getListFromId(listIds[i], token);
        				}
        				
        				for(int i = 0; i < lists.length; i++){
        					System.out.println("Listname: "+lists[i].nameOfList+" | List id: "+lists[i].listId);
        					System.out.println("Users:");
        					for(int x = 0; x < lists[i].users.size(); x++){
        						System.out.println("\t"+lists[i].users.get(x).name);
        					}
        					System.out.println("Items:");
        					if(lists[i].items != null){
        						for(int x = 0; x < lists[i].items.size(); x++){
            						System.out.println("\t"+lists[i].items.get(x).name+" - "+lists[i].items.get(x).user.name);
            					}
        					}
        				}
        				break;
        			case 2:
        				System.out.print("Type new listname: ");
        				String listname = scanner.nextLine();
        				if(listname.length() == 0 || listname.isEmpty()){
        					System.out.println("Invalid listname");
        					break;
        				}
        				if(iPlebbit.createNewList(token, listname)){
        					System.out.println("List "+listname+" created!");
        				} else{
        					System.out.println("Failed to create list.");
        				}
        				break;
        			case 3: 
        				System.out.print("Type user to invite: ");
        				String inviteUser = scanner.nextLine();
        				if(inviteUser.length() == 0 || inviteUser.isEmpty()){
        					System.out.println("Invalid username");
        					break;
        				}
        				System.out.print("Type id of list: ");
        				int idOfList = Integer.parseInt(scanner.nextLine());
        				if(idOfList != 0){
        					if(iPlebbit.addUserToList(idOfList, token, inviteUser)){
        						System.out.println("User invited!");
        					} else{
        						System.out.println("Could not invite user.");
        					}
        				} else{
        					System.out.println("Invalid id.");
        				}
        				break;
        			case 4: 
        				System.out.print("Type item to add: ");
        				String item = scanner.nextLine();
        				if(item.length() == 0 || item.isEmpty()){
        					System.out.println("Invalid itemname");
        					break;
        				}
        				System.out.print("Type id of list: ");
        				int itemId = Integer.parseInt(scanner.nextLine());
        				if(itemId != 0){
        					if(iPlebbit.addItemToList(token, item, itemId)){
        						System.out.println("Added item to list!");
        					} else{
        						System.out.println("Failed adding item to list.");
        					}
        				} else{
        					System.out.println("Invalid list id.");
        				}
        				break;
					case 5: 
						System.out.print("Type id of list: ");
        				int idOfListToDelete = Integer.parseInt(scanner.nextLine());
        				if(idOfListToDelete != 0){
        					if(iPlebbit.deleteList(token, idOfListToDelete)){
        						System.out.println("List deleted.");
        					} else{
        						System.out.println("Failed to delete list.");
        					}
        				} else{
        					System.out.println("Invalid list id.");
        				}
        				break;
					case 6: 
						token = "";
						isLoggedIn = false;
						break;
        			case 7:
        				isControlling = false; // <-- Soft exit
        				break;
        			}
        		} catch(NumberFormatException e){
        			e.printStackTrace();
        		}
        	}
        }
	}
	
	public static void printOptionsLoggedIn(){
		System.out.println("Choose options");
		System.out.println("1. Show lists");
		System.out.println("2. Create new list");
		System.out.println("3. Invite users to list");
		System.out.println("4. Add item to list");
		System.out.println("5. Delete list");
		System.out.println("6. Logout");
		System.out.println("7. Exit");
	}
	
	public static void printOptionsNotLoggedIn(){
		System.out.println("Choose options");
		System.out.println("1. Login");
		System.out.println("2. Forgot password");
		System.out.println("3. Change password");
		System.out.println("4. Exit");
	}
	
}
