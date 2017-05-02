package com.plebbit.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DatabaseConnector {

	private static DatabaseConnector totalConnector;
	
    private static Connection mysqlConnection;
    
    private DatabaseConnector() throws SQLException {
    	if(mysqlConnection==null){
			try {
				Class.forName("com.mysql.jdbc.Driver");
				String databaseConnectionUrl = "jdbc:mysql://";
				databaseConnectionUrl += DatabaseVariables.DATABASE_HOST_LOCATION + ":" + DatabaseVariables.DATABASE_PORT_NUMBER + "/" + DatabaseVariables.DATABASE_NAME;
				mysqlConnection = DriverManager.getConnection(databaseConnectionUrl, DatabaseVariables.DATABASE_ROOT_USERNAME, DatabaseVariables.DATABASE_ROOT_PASSWORD);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
    }
    
    public static boolean resetConnection(){
    	try {
    		mysqlConnection.close();
    		totalConnector = null;
			totalConnector = new DatabaseConnector();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return false;
    }
    
    public static DatabaseConnector getTotalConnector() throws SQLException{  
    	if (totalConnector == null){
    		totalConnector = new DatabaseConnector();
    	}
    	return totalConnector;
    }

    /**
     * Dont use this unless important, use query/update functions
     * @return
     */
	public Connection getConnection() {
		return mysqlConnection;
	}
	
	public static ResultSet queryInDatabase(String request) throws SQLException{
		Statement queryStatement = mysqlConnection.createStatement();
		return queryStatement.executeQuery(request);
	}
	
	public static void updateInDatabase(String request) throws SQLException{
		Statement updateStatement = mysqlConnection.createStatement();
		updateStatement.executeUpdate(request, Statement.RETURN_GENERATED_KEYS);
	}
	
	public static int updateInDatabase(String request, int returnID) throws SQLException{
		Statement updateStatement = mysqlConnection.createStatement();
		return updateStatement.executeUpdate(request, returnID);
	}
	
}
