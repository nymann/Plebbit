package com.plebbit.database;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Statement;

public class PlebbitDatabase implements IPlebbitDatabase{

	@Override
	public void checkTables() throws SQLException{
		DatabaseMetaData databaseMeta = DatabaseConnector.getTotalConnector().getConnection().getMetaData();
		ResultSet usersTable = databaseMeta.getTables(null, null, "users", null);
		if (!usersTable.next()) {
			String sqlQuery = "CREATE TABLE users (userid INTEGER NOT NULL AUTO_INCREMENT, username VARCHAR(30) NOT NULL, token VARCHAR(50) NOT NULL, PRIMARY KEY (userid));";
			DatabaseConnector.updateInDatabase(sqlQuery);
		}
		
		ResultSet listsTable = databaseMeta.getTables(null, null, "lists", null);
		if (!listsTable.next()) {
			String sqlQuery = "CREATE TABLE lists (listid INTEGER NOT NULL AUTO_INCREMENT, listname VARCHAR(30) NOT NULL, PRIMARY KEY (listid));";
			DatabaseConnector.updateInDatabase(sqlQuery);
		}
		
		ResultSet listmembersTable = databaseMeta.getTables(null, null, "listmembers", null);
		if (!listmembersTable.next()) {
			String sqlQuery = "CREATE TABLE listmembers (listid INTEGER NOT NULL, userid INTEGER NOT NULL, FOREIGN KEY (listid) REFERENCES lists(listid), FOREIGN KEY (userid) REFERENCES users(userid));";
			DatabaseConnector.updateInDatabase(sqlQuery);
		}
		
		ResultSet itemsTable = databaseMeta.getTables(null, null, "items", null);
		if (!itemsTable.next()) {
			String sqlQuery = "CREATE TABLE items (listid INTEGER NOT NULL, itemname varchar(30) NOT NULL, userid INTEGER NOT NULL, FOREIGN KEY (listid) REFERENCES lists(listid), FOREIGN KEY (userid) REFERENCES users(userid));";
			DatabaseConnector.updateInDatabase(sqlQuery);
		}
	}
	
	@Override
	public boolean userExists(String username) {
		String sqlQuery = "select * from users where username = '"+username+"';";
		try {
			ResultSet set = DatabaseConnector.queryInDatabase(sqlQuery);
			if(set.next()){
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean createUser(String username) {
		String sqlQuery = "INSERT INTO users (username, token) VALUES('"+username+"', '');";
		try {
			DatabaseConnector.updateInDatabase(sqlQuery);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean createList(String listname, String username) {
		String sqlQuery = "INSERT INTO lists (listname) VALUES('"+listname+"');";
		try {
			int num = DatabaseConnector.updateInDatabase(sqlQuery, Statement.RETURN_GENERATED_KEYS);
			String sqlQueryTwo = "select * from users where username = '"+username+"';";
			ResultSet set = DatabaseConnector.queryInDatabase(sqlQueryTwo);
			int id = 0;
			if(set.next()){
				id = set.getInt(1);
			}
			String sqlQueryThree = "INSERT INTO listmembers (listid, userid) VALUES("+num+","+id+");";
			DatabaseConnector.updateInDatabase(sqlQueryThree);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean addUserToList(String username, int listNr) {
		String sqlQueryTwo = "select * from users where username = '"+username+"';";
		ResultSet set;
		try {
			set = DatabaseConnector.queryInDatabase(sqlQueryTwo);
			int id = 0;
			if(set.next()){
				id = set.getInt(1);
			}
			String sqlQueryThree = "INSERT INTO listmembers (listid, userid) VALUES("+listNr+","+id+");";
			DatabaseConnector.updateInDatabase(sqlQueryThree);
			return true;
		} catch (SQLException e) {
			return false;
		}
	}

	@Override
	public ListProperties getList(int listNr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateToken(String username, String newToken) {
		String sqlUpdate = "UPDATE users set token = '"+newToken+"' where username = '"+username+"';";
		try {
			DatabaseConnector.updateInDatabase(sqlUpdate);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	

}
