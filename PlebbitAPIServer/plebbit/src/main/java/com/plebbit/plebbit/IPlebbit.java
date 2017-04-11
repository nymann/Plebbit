package com.plebbit.plebbit;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.plebbit.dto.ListProperties;

@WebService
public interface IPlebbit {
	/**
	 * 
	 * @param username
	 * @param password
	 * @return token which will be used to confirm name
	 */
	@WebMethod public String login(String username, String password);

	@WebMethod public void forgotPassword(String username);
	
	@WebMethod public void changePassword(String username, String oldPassword, String newPassword);
	
	@WebMethod public int[] getListOfUser(String token);
	
	@WebMethod public ListProperties getListFromId(int id, String token);
	
	@WebMethod public boolean addUserToList(int listId, String token, String inviteUserName);
	
	@WebMethod public void createNewList(String token, String listname);
	
	@WebMethod public boolean deleteList(String token, int listId);
	
	@WebMethod public void addItemToList(String token, String item, int listId);
	
	@WebMethod public boolean removeItemFromList(String token, String item, int listId);
	
	@WebMethod public void logout(String token);
	
}
