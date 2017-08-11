package com.hirrr.companyfinder.utitlities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;



public class ConsultancyCheck {
	
	private static final Logger LOGGER = Logger.getLogger(ConsultancyCheck.class.getName());
	
	/**
	 * Check if the company is a Consultancy or not 
	 * @param jobTitle
	 * @return
	 * @throws IOException
	 */
	public static boolean isConsultancy(String companyName) throws IOException {
		
		BufferedReader bufferedReader = null;
		String companyNameToCheck = null;
		Boolean isConsultancyWordMatchFlag = false;
		String consultancyRelatedWordsFromTextFile = "";
		companyNameToCheck = companyName.toUpperCase(Locale.ENGLISH);
		companyNameToCheck = companyName.replaceAll("\u00A0", " ").replaceAll("\u00a0", " ");
		String path = "/home/mithunmanohar/Downloads/consultancy";
	
		try {
			
			bufferedReader = new BufferedReader(new FileReader(path));
			while ((consultancyRelatedWordsFromTextFile = bufferedReader.readLine()) != null) {
				consultancyRelatedWordsFromTextFile = consultancyRelatedWordsFromTextFile.toUpperCase(Locale.ENGLISH);
				Pattern pattern = Pattern.compile("\\b" + consultancyRelatedWordsFromTextFile + "\\b");
				Matcher matcher = pattern.matcher(companyNameToCheck);
				if (matcher.find()) {
					isConsultancyWordMatchFlag = true;
					break;
				}
			}
		}catch (Exception e) {
			LOGGER.warn(e);
		}
		finally {
			bufferedReader.close();
		}

		return isConsultancyWordMatchFlag;
	}
	
	
	/**
	 * Check if the company is already present consultancy company names list
	 * @return
	 */
	
	public static boolean isExactConsultancy(String companyName) {
		boolean isExactConsultancyFlag = false;
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		final String checkForExactConsultancyCompanyQuery = "select * from  jobboard_scrap.consultancy_Name where nameOfConsultancy = ?";
		connection = GetDbConnection.getDbConnection();
		
		try {
			statement = connection.prepareStatement(checkForExactConsultancyCompanyQuery);
			statement.setString(1, companyName);
			resultSet = statement.executeQuery();
			
			if(resultSet.next()) {
				
				// Shows that company name is present in consultancy company table
				
				isExactConsultancyFlag = true;
				
			}
			
		}catch (Exception e) {
			LOGGER.warn(e);
		}
		
		finally {
			GetDbConnection.closeDbResources(connection, statement, resultSet);
		}
		
		LOGGER.warn(companyName + "--->" +isExactConsultancyFlag);
		return isExactConsultancyFlag;
	}
	
}
