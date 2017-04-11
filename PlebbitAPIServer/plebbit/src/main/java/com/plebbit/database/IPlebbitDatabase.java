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
}
