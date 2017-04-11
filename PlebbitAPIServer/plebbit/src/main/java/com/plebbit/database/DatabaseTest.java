package com.plebbit.database;

import java.sql.SQLException;

public class DatabaseTest {

	public static void main(String[] args) {
		PlebbitDatabase db = new PlebbitDatabase();
		try {
			db.checkTables();
			db.createUser("myboynymann");
			System.out.println(db.userExists("myboynymann"));
			db.createList("winnerwinner", "myboynymann");
			db.createUser("mossi");
			db.addUserToList("mossi", 1);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
