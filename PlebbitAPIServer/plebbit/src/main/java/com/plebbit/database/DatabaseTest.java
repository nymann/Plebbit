package com.plebbit.database;

import java.sql.SQLException;

import com.plebbit.dto.ListProperties;

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
			ListProperties probs = db.getList(1);
			System.out.println(probs.nameOfList);
			System.out.println(probs.users.size());
			System.out.println(probs.users.get(0).name);
			System.out.println(probs.users.get(1).name);
			System.out.println(probs.items.size());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
