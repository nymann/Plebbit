package com.plebbit.database;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.plebbit.helpers.WriteSomething;

public class KeepAlive implements Runnable{

	private Thread t;
	
	public KeepAlive() {
		t = new Thread(this);
	}
	
	public void startThread(){
		t.start();
	}
	
	
	@Override
	public void run() {
		while(true){
			if(PlebbitDatabase.db.checkConnection()){
				WriteSomething.writeInFile(WriteSomething.location, "Over an hour since last query/update, resetting connection.");
				try {
					ResultSet set = DatabaseConnector.queryInDatabase("select * from users;");
					if(set.next()){
						System.out.println("Nice.");
						WriteSomething.writeInFile(WriteSomething.location, "Got response, should be fine!");
					} else{
						WriteSomething.writeInFile(WriteSomething.location, "Didnt get response, shouldnt be fine!");
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				PlebbitDatabase.db.lastDataBaseChange = System.nanoTime();
			} else{
				try {
					Thread.sleep(60000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
