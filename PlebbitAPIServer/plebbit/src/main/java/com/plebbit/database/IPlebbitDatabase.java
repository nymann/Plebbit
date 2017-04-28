package com.plebbit.database;

import java.sql.SQLException;

import com.plebbit.dto.Item;
import com.plebbit.dto.ListProperties;
import com.plebbit.dto.User;

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
	
	public String getListLastChanged(int listid);
	
	public boolean updateListLastChanged(int listid);
	
	public boolean renameListName(int listid, String newname);
	
	public boolean setBoughtItem(int listId, String itemName, boolean toSet);
	
	public Item getItem(int listId, String itemName);
	
	public User getUserFromToken(String token);
	
	public boolean setItemName(int listId, String itemName, String newName);
	
	public boolean setListName(int listId, String listName);
}
