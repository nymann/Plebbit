package com.plebbit.database;

import java.sql.SQLException;

import com.plebbit.dto.ListProperties;

public interface IPlebbitDatabase {

	public void checkTables() throws SQLException;
	
	public boolean userExists(String username);
	
	public boolean createUser(String username);
	
	public boolean createList(String listname, String username);
	
	public boolean addUserToList(String username, int listNr);
	
	public ListProperties getList(int listNr);
	
	public boolean updateToken(String username, String newToken);
	
	public boolean updateTime(String username);
	
	public boolean updateTime(int userId);
	
	public int[] getListsForUser(String username);
	
	public String getUsernameFromToken(String token);
	
	public int getIdFromUsername(String username);
	
	public boolean isValidToken(String token);
	
	public boolean deleteList(String token, int listId);
	
	public boolean addItem(String itemName, int listId, int userId);
	
	public boolean removeItem(String itemName, int listId, int userId);
	
	public boolean updateTimeOnToken(String token);
	
	public String getTimeOnToken(String token);
	
	public String getTimeOnUsername(String username);
}
