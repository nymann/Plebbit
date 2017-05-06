package com.plebbit.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;


public class DatabaseConnector {

	private static DatabaseConnector totalConnector;
	
    //private static Connection mysqlConnection;
    
    private static MysqlDataSource ds;
    
    private DatabaseConnector() throws SQLException {
    	if(ds==null){
    		upDataSource();
    		/*
			try {
				Class.forName("com.mysql.jdbc.Driver");
				String databaseConnectionUrl = "jdbc:mysql://";
				databaseConnectionUrl += DatabaseVariables.DATABASE_HOST_LOCATION + ":" + DatabaseVariables.DATABASE_PORT_NUMBER + "/" + DatabaseVariables.DATABASE_NAME;
				mysqlConnection = DriverManager.getConnection(databaseConnectionUrl, DatabaseVariables.DATABASE_ROOT_USERNAME, DatabaseVariables.DATABASE_ROOT_PASSWORD);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			*/
		}
    }
    
    private static void upDataSource(){
    	ds = new MysqlDataSource();
 		ds.setURL("jdbc:mysql://"+DatabaseVariables.DATABASE_HOST_LOCATION+":"+DatabaseVariables.DATABASE_PORT_NUMBER+"/"+DatabaseVariables.DATABASE_NAME);
 		ds.setUser(DatabaseVariables.DATABASE_ROOT_USERNAME);
 		ds.setPassword(DatabaseVariables.DATABASE_ROOT_PASSWORD);
    }
    
    public static boolean resetConnection(){
    	//try {
    		upDataSource();
    		//mysqlConnection.close();
    		//totalConnector = null;
			//totalConnector = new DatabaseConnector();
			return true;
		/*} catch (SQLException e) {
			e.printStackTrace();
		}
    	return false;*/
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
     * @throws SQLException 
     */
	public Connection getConnection() throws SQLException {
		return ds.getConnection();
	}
	
	public static ResultSet queryInDatabase(String request) throws SQLException{
		Statement queryStatement = ds.getConnection().createStatement();
		return queryStatement.executeQuery(request);
	}
	
	public static void updateInDatabase(String request) throws SQLException{
		Statement updateStatement = ds.getConnection().createStatement();
		updateStatement.executeUpdate(request, Statement.RETURN_GENERATED_KEYS);
	}
	
	public static int updateInDatabase(String request, int returnID) throws SQLException{
		Statement updateStatement = ds.getConnection().createStatement();
		return updateStatement.executeUpdate(request, returnID);
	}
	
}
