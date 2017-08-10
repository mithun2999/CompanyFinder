package com.hirrr.companyfinder.utitlities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

public class GetDbConnection {
	
	private static final Logger LOGGER = Logger.getLogger(GetDbConnection.class.getName());
	
	/**
	 * Getting db connection 
	 * @return
	 */
	
	public static Connection getDbConnection(){
		Connection connection = null;
		final String dbUrl = "jdbc:mysql://localhost/results_provider_db";
		final String userName = "root";
		final String password = "careersnow@123";
		try {
			connection = DriverManager.getConnection(dbUrl, userName, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}
	
	
	/**
	 * Closing dbresources as soon as it's use is completed
	 * @param connection
	 * @param statement
	 * @param resultSet
	 */
	
	public static void closeDbResources(Connection connection, Statement statement, ResultSet resultSet){
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (Exception e) {
			LOGGER.warn(e);
		}
		try {
			if (statement != null) {
				statement.close();
			}
		} catch (Exception e) {
			LOGGER.warn(e);
		}
		try {
			if (resultSet != null) {
				resultSet.close();
			}
		} catch (Exception e) {
			LOGGER.warn(e);
		}
	}
	
	/**
	 * Get db updation status printed on the console
	 */
	
	public static void getDbUpdationStatus(Integer queryUpdationResultCount) {
		final String dbUpdationSuccessMessage = "Db insertion was successful";
		final String dbUpdationFailureMessage = "Db insertion failed";
		
		if(queryUpdationResultCount > 1) {
			LOGGER.warn(dbUpdationSuccessMessage);
		}else {
			LOGGER.warn(dbUpdationFailureMessage);
		}
		
	}
	

}
