package com.plebbit.database;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.Statement;
import com.plebbit.dto.Item;
import com.plebbit.dto.ListProperties;
import com.plebbit.dto.User;

public class PlebbitDatabase implements IPlebbitDatabase{

	public static PlebbitDatabase db;
	
	@Override
	public void checkTables() throws SQLException{
		DatabaseMetaData databaseMeta = DatabaseConnector.getTotalConnector().getConnection().getMetaData();
		ResultSet usersTable = databaseMeta.getTables(null, null, "users", null);
		if (!usersTable.next()) {
			String sqlQuery = "CREATE TABLE users (userid INTEGER NOT NULL AUTO_INCREMENT, username VARCHAR(30) NOT NULL, token VARCHAR(50) NOT NULL, time VARCHAR(50), PRIMARY KEY (userid));";
			DatabaseConnector.updateInDatabase(sqlQuery);
		}
		
		ResultSet listsTable = databaseMeta.getTables(null, null, "lists", null);
		if (!listsTable.next()) {
			String sqlQuery = "CREATE TABLE lists (listid INTEGER NOT NULL AUTO_INCREMENT, listname VARCHAR(30) NOT NULL, lastchanged VARCHAR(50) NOT NULL, PRIMARY KEY (listid));";
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
		if(this.userExists(username)){
			return false;
		}
		String sqlQuery = "INSERT INTO users (username, token, time) VALUES('"+username+"', '', '');";
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
		String sqlQuery = "INSERT INTO lists (listname, lastChanged) VALUES('"+listname+"','"+System.nanoTime()+"');";
		try {
			DatabaseConnector.updateInDatabase(sqlQuery);
			String sqlQueryGetId = "select * from lists where listname = '"+listname+"';";
			ResultSet idSet = DatabaseConnector.queryInDatabase(sqlQueryGetId);
			int num = 0;
			while(idSet.next()){
				num = idSet.getInt(1);
			}
			String sqlQueryTwo = "select * from users where username = '"+username+"';";
			ResultSet set = DatabaseConnector.queryInDatabase(sqlQueryTwo);
			int id = 0;
			
			if(set.next()){
				id = set.getInt(1);
			} else{
				return false;
			}
			String sqlQueryThree = "INSERT INTO listmembers (listid, userid) VALUES("+num+","+id+");";
			DatabaseConnector.updateInDatabase(sqlQueryThree);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean addUserToList(String username, int listNr) {
		String sqlQuery = "select * from users where username = '"+username+"';";
		ResultSet set;
		try {
			set = DatabaseConnector.queryInDatabase(sqlQuery);
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
		ListProperties prop = new ListProperties();
		prop.users = new ArrayList<User>();
		prop.items = new ArrayList<Item>();
		prop.listId = listNr;
		String sqlQuery = "select * from lists where listid = "+listNr+";";
		try {
			ResultSet set = DatabaseConnector.queryInDatabase(sqlQuery);
			if(set.next()){
				prop.nameOfList = set.getString(2);
			}
			
			String sqlQueryTwo = "select * from listmembers where listid = "+listNr+";";
			ResultSet setTwo = DatabaseConnector.queryInDatabase(sqlQueryTwo);
			while(setTwo.next()){
				String sqlQueryThree = "select * from users where userid = "+setTwo.getInt(2)+";";
				ResultSet setThree = DatabaseConnector.queryInDatabase(sqlQueryThree);
				if(setThree.next()){
					User user = new User();
					user.userId = setTwo.getInt(2);
					user.name = setThree.getString(2);
					user.token = setThree.getString(3);
					user.time = setThree.getString(4);
					prop.users.add(user);
				}
			}
			
			String sqlQueryFour = "select * from items where listid = "+listNr+";";
			ResultSet setFour = DatabaseConnector.queryInDatabase(sqlQueryFour);
			while(setFour.next()){
				Item item = new Item();
				item.name = setFour.getString(2);
				int userId = setFour.getInt(3);
				for(int i = 0 ; i < prop.users.size(); i++){
					if(prop.users.get(i).userId == userId){
						item.user = prop.users.get(i);
						break;
					}
				}
				prop.items.add(item);
			}
			return prop;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
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

	@Override
	public boolean updateTime(String username) {
		String time = System.nanoTime()+"";
		String sqlUpdate = "UPDATE users set time = '"+time+"' where username = '"+username+"';";
		try {
			DatabaseConnector.updateInDatabase(sqlUpdate);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean updateTime(int userId) {
		String time = System.nanoTime()+"";
		String sqlUpdate = "UPDATE users set time = '"+time+"' where userid = "+userId+";";
		try {
			DatabaseConnector.updateInDatabase(sqlUpdate);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public int[] getListsForUser(String username) {
		ArrayList<Integer> listid = new ArrayList<Integer>();
		int id = getIdFromUsername(username);
		if(id != -1){
			String sqlQuery = "select * from listmembers where userid = "+id+";";
			ResultSet set;
			try {
				set = DatabaseConnector.queryInDatabase(sqlQuery);
				while(set.next()){
					listid.add(set.getInt(1));
				}
				int[] listids = new int[listid.size()];
				for(int i = 0; i < listids.length; i++){
					listids[i] = listid.get(i);
				}
				return listids;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public String getUsernameFromToken(String token) {
		String sqlQuery = "select * from users where token = '"+token+"';";
		try {
			ResultSet set = DatabaseConnector.queryInDatabase(sqlQuery);
			if(set.next()){
				String usrname = set.getString(2);
				return usrname;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public int getIdFromUsername(String username) {
		String sqlQuery = "select * from users where username = '"+username+"';";
		ResultSet set;
		try {
			set = DatabaseConnector.queryInDatabase(sqlQuery);
			if(set.next()){
				return set.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
		
	}

	@Override
	public boolean isValidToken(String token) {
		String sqlQuery = "select * from users where token = '"+token+"';";
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
	public boolean deleteList(String token, int listId) {
		ListProperties lp = PlebbitDatabase.db.getList(listId);
		if(lp.users.size() > 0){
			if(lp.users.get(0).token.equals(token)){
				//Is owner
				try {
					String sqlUpdateItems = "delete from items where listid ="+listId+";";
					DatabaseConnector.updateInDatabase(sqlUpdateItems);
					String sqlUpdate = "delete from listmembers where listid = "+listId+";";
					DatabaseConnector.updateInDatabase(sqlUpdate);
					String sqlUpdate2 = "delete from lists where listid = "+listId+";";
					DatabaseConnector.updateInDatabase(sqlUpdate2);
					return true;
				} catch (SQLException e) {
					e.printStackTrace();
					return false;
				}
			}
		}
		return false;
	}

	@Override
	public boolean addItem(String itemName, int listId, int userId) {
		ListProperties lp = PlebbitDatabase.db.getList(listId);
		boolean exists = false;
		for(int i = 0; i < lp.users.size(); i++){
			if(lp.users.get(i).userId == userId){
				exists = true;
			}
		}
		if(!exists){
			return false;
		}
		String sqlUpdate = "insert into items (listid, itemname, userid) values ("+listId+",'"+itemName+"',"+userId+");";
		try {
			DatabaseConnector.updateInDatabase(sqlUpdate);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean removeItem(String itemName, int listId, int userId) {
		ListProperties lp = PlebbitDatabase.db.getList(listId);
		boolean exists = false;
		for(int i = 0; i < lp.users.size(); i++){
			if(lp.users.get(i).userId == userId){
				exists = true;
			}
		}
		if(!exists){
			return false;
		}
		String sqlUpdate = "delete from items where itemname = '"+itemName+"';";
		try {
			DatabaseConnector.updateInDatabase(sqlUpdate);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean updateTimeOnToken(String token) {
		return this.updateTime(getIdFromUsername(getUsernameFromToken(token)));
	}
	@Override
	public String getTimeOnToken(String token) {
		String time = "";
		String sqlQuery = "select * from users where token = '"+token+"';";
		ResultSet set;
		try {
			set = DatabaseConnector.queryInDatabase(sqlQuery);
			if(set.next()){
				time = set.getString(4);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return time;
	}

	@Override
	public String getTimeOnUsername(String username) {
		String time = "";
		String sqlQuery = "select * from users where username = '"+username+"';";
		ResultSet set;
		try {
			set = DatabaseConnector.queryInDatabase(sqlQuery);
			if(set.next()){
				time = set.getString(4);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return time;
	}

	@Override
	public String getListLastChanged(int listid) {
		String sqlQuery = "select * from lists where listid = "+listid+";";
		ResultSet set;
		try {
			set = DatabaseConnector.queryInDatabase(sqlQuery);
			if(set.next()){
				return set.getString(3);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public boolean updateListLastChanged(int listid) {
		String sqlUpdate = "update lists set lastchanged = '"+System.nanoTime()+"' where listid = "+listid+";";
		try {
			DatabaseConnector.updateInDatabase(sqlUpdate);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean renameListName(int listid, String newname) {
		String sqlUpdate = "update lists set listname = '"+newname+"' where listid = "+listid+";";
		try {
			DatabaseConnector.updateInDatabase(sqlUpdate);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	

}
