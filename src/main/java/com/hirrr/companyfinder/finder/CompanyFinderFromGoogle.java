package com.hirrr.companyfinder.finder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.hirrr.companynamefinder.CompanyNameIdentifier;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.hirrr.companyfinder.commonfields.CommonFields;
import com.hirrr.companyfinder.utitlities.ConsultancyCheck;
import com.hirrr.companyfinder.utitlities.GetDbConnection;
import com.hirrr.companyfinder.utitlities.GetDocumentUtil;
import com.hirrr.companyfinder.utitlities.MagicNumbers;
import com.hirrr.companyfinder.utitlities.RandomValueGeneratorUtil;

/**
 * Process for finding mutilple domain urls from google search result for a particular company search
 * @author mithunmanohar
 *
 */

public class CompanyFinderFromGoogle {
	
	private static final Logger LOGGER = Logger.getLogger(CompanyFinderFromGoogle.class.getName());
	private Connection dbConnection = null;
	private PreparedStatement statement = null;
	private ResultSet resultSet = null;
	private CommonFields commonFields = new CommonFields();
	private String urlFoundStatus = "URL FOUND" ;
	
	
	/**
	 * Read jobsnatcher input company names from text file
	 * @throws IOException 
	 */
	
	public void readCompanyNamesFromCoData() {
		
		String companyNameFromCoData = null;
		final String getCompanyNamesFromCoDataQuery = "select company_name from results_provider_db.co_data";
		try {
		
			dbConnection = GetDbConnection.getDbConnection();
			statement = dbConnection.prepareStatement(getCompanyNamesFromCoDataQuery);
			resultSet = statement.executeQuery();
			if(resultSet != null) {
				while (resultSet.next()) {
					try{
						companyNameFromCoData = resultSet.getString("company_name");
						commonFields.setCompanyName(companyNameFromCoData);
						getFiftySearchResultUrlsFromGoogle(companyNameFromCoData);
					}catch(Exception e){
						LOGGER.warn("Exception in reading company name");
					}
				}
			}
		} catch (Exception e) {
			LOGGER.warn("Exception in reading company name");
		}
		finally{
			GetDbConnection.closeDbResources(dbConnection, statement, resultSet );
		}
	}
	
	
	/**
	 * Search a company name in google and return the 50 urls.
	 * Excludes blacklisted urls.
	 * @return
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	
	private List<String> getFiftySearchResultUrlsFromGoogle(String companyName) throws InterruptedException {
		
		String domainUrlForSearchedCompanyName = null;
		String hasUrlStatus = null;
		final String urlNotFoundStatus = "URL NOT FOUND" ;
		String urlToSearchInGoogle = null;
		Document googleSearchResultDocument = null;
		List<String> fiftySearchResultUrlsFromGoogle ;
		
		long randomTimeGeneratedFromRandomTimeGeneration = RandomValueGeneratorUtil.randomTimeGeneration();
		Thread.sleep(randomTimeGeneratedFromRandomTimeGeneration);
	
		urlToSearchInGoogle = getPreferredSearchEngineUrl(companyName).replaceAll(" ", "+").trim();		//Replacing space by + symbol to make the search query
	
		LOGGER.warn("Search URL : :"+urlToSearchInGoogle);
	
		googleSearchResultDocument = GetDocumentUtil.getJsoupDocumentResponse(urlToSearchInGoogle);
		
		fiftySearchResultUrlsFromGoogle = urlsFromGoogleSearchResultList(urlToSearchInGoogle, googleSearchResultDocument);
		
		//---------------------------------
		
		domainUrlForSearchedCompanyName = new ConfirmCompanyNameAndUrl().
				getConfirmedDomainUrlForCompanyName(companyName, fiftySearchResultUrlsFromGoogle);
	
		if(!domainUrlForSearchedCompanyName.isEmpty()) {
			
			processingOfSearchCompany(domainUrlForSearchedCompanyName, companyName);
			hasUrlStatus = urlFoundStatus;
			statusUpdationOfDomainUrlInCoDataTable(urlFoundStatus, companyName);
		}else {
			hasUrlStatus = urlNotFoundStatus;
			statusUpdationOfDomainUrlInCoDataTable(urlNotFoundStatus, companyName);
		}
		
		//----------------------------------
		
		LOGGER.warn("First 50 search result urls >> After Search : "+fiftySearchResultUrlsFromGoogle);
		
		processingOfFiftyUrls(fiftySearchResultUrlsFromGoogle, domainUrlForSearchedCompanyName, hasUrlStatus);
		
		//----------------------------------
		
		/**
		 * Processing company name finder
		 */
		
		
		processingOfCompanyNameFinder();
		
		
		
		//----------------------------------
		
		return fiftySearchResultUrlsFromGoogle;
	}
	
	/**
	 * Process of finding the company names for the obtained urls
	 * updating it in the database
	 */
	
	private void processingOfCompanyNameFinder() {
		
		String companyConsultancyString = null;
		boolean isConsultancyFlag = false;
		String companyNameObtainedFromCompanyNameFinder = null;
		String companyUrlFromCoFoundTable = null;
		String domainUrlFromCoFoundTable = null;
		final String takeCompanyWithStatusReadyFromCoFoundQuery = "SELECT company_url,domain_url"
				+ " FROM results_provider_db.co_found where company_status = 'ready'";
		try {
			statement = dbConnection.prepareStatement(takeCompanyWithStatusReadyFromCoFoundQuery);
			resultSet = statement.executeQuery();
			while(resultSet.next()) {
				
				companyUrlFromCoFoundTable = resultSet.getString("company_url");
				domainUrlFromCoFoundTable = resultSet.getString("domain_url");
				companyNameObtainedFromCompanyNameFinder = new CompanyNameIdentifier().
						normalCompanyNameIdentifier(companyUrlFromCoFoundTable);
				if(!companyNameObtainedFromCompanyNameFinder.isEmpty()) {
					isConsultancyFlag = getConsultancyMatchFlag(companyNameObtainedFromCompanyNameFinder);
					
					if(isConsultancyFlag) {
						companyConsultancyString = "CONSULTANCY";
						companyOrConsultancyStatusUpdationInCoFound(companyConsultancyString,
								companyNameObtainedFromCompanyNameFinder, domainUrlFromCoFoundTable);
					}else {
						companyConsultancyString = "COMPANY";
						companyOrConsultancyStatusUpdationInCoFound(companyConsultancyString,
								companyNameObtainedFromCompanyNameFinder, domainUrlFromCoFoundTable);
					}
				}
			}
		
		}catch (Exception e) {
			LOGGER.warn(e);
		}
		
	}
	
	/**
	 * Company name updation 
	 * Company/Consultancy status updation
	 */
	private void companyOrConsultancyStatusUpdationInCoFound(String companyConsultancyString, 
			String companyNameObtainedFromCompanyNameFinder, String domainUrlFromCoFoundTable) {
		
		final String companyOrConsultancyStatusUpdationQuery = "update results_provider_db.co_found set"
				+ " company_status = ?, company_name = ? where domain_url = ?";
		
		try {
			statement = dbConnection.prepareStatement(companyOrConsultancyStatusUpdationQuery);
			statement.setString(1, companyConsultancyString);
			statement.setString(2, companyNameObtainedFromCompanyNameFinder);
			statement.setString(3, domainUrlFromCoFoundTable);
			statement.executeUpdate();

		} catch (SQLException e) {
			LOGGER.warn(e);
		}
	}
	
	/**
	 * Check for consultancy word & exact match
	 * @param companyNameObtainedFromCompanyNameFinder
	 * @return
	 */
	
	private boolean getConsultancyMatchFlag(String companyNameObtainedFromCompanyNameFinder) {
		boolean isConsultancyFlag = false;
		
			try {
				isConsultancyFlag = ConsultancyCheck.isConsultancy(companyNameObtainedFromCompanyNameFinder);
				if(!isConsultancyFlag) {
					isConsultancyFlag = ConsultancyCheck.isExactConsultancy(companyNameObtainedFromCompanyNameFinder);
				}
				
			}catch (Exception e) {
				LOGGER.warn(e);
			}
		
		return isConsultancyFlag;
	}
	
	/**
	 * Updating the status field of co-data table.
	 * If the domain url is obtained, it will be URL FOUND else URL NOT FOUND
	 */
	
	private void statusUpdationOfDomainUrlInCoDataTable(String urlFoundStatus, String companyName) {
		
		final String urlFoundStatusUpdationQuery = "update results_provider_db.co_data set status = ? where company_name = ?";
		try {
			statement = dbConnection.prepareStatement(urlFoundStatusUpdationQuery);
			statement.setString(1, urlFoundStatus);
			statement.setString(2, companyName);
			statement.executeUpdate();

		} catch (SQLException e) {
			LOGGER.warn(e);
		}
	}
	
	
	/**
	 * Inserting the exact domain url for the searched company
	 * @param domainUrlForSearchedCompanyName
	 */
	
	private void processingOfSearchCompany(String domainUrlForSearchedCompanyName, String companyName) {
		final String addProtocol = "http://www.";
		commonFields.setDomainUrl(domainUrlForSearchedCompanyName);
		commonFields.setCompanyFinderStatus("ready");
		commonFields.setCompanyName(companyName);
		commonFields.setDateTime(new Date()+"");
		commonFields.setCompanyUrl(addProtocol+domainUrlForSearchedCompanyName);
		
		try {
			insertIntoCompanyFinderTable(commonFields,"input");
		}catch (Exception e) {
			LOGGER.warn("Exception in insertion to database");
		}
	}
	
	
	/**
	 * Iterating through the fifty search result urls, Finding the company domain Url
	 * Inserting only the unique values into company finder table 
	 *
	 */
	
	private void processingOfFiftyUrls(List<String> fiftySearchResultUrlsFromGoogle, String domainUrlForSearchedCompanyName, String hasUrlStatus) {
		
		commonFields.setCompanyFinderStatus("ready");
		commonFields.setDateTime(new Date()+"");
		
		for(String singleUrlFromSearchResult : fiftySearchResultUrlsFromGoogle) {
			
			try {
				
			commonFields.setCompanyUrl(singleUrlFromSearchResult);
			commonFields.setDomainUrl(new DomainUrlFinder().domainFinder(singleUrlFromSearchResult));
			
			/**
			 * Removing already processed domain url from fifty search result
			 */
			
			if((commonFields.getDomainUrl().contains(domainUrlForSearchedCompanyName))
					&&(hasUrlStatus.contains(urlFoundStatus))) {
				continue;
			}
			
			insertIntoCompanyFinderTable(commonFields,"fifty");		//Inserting into co-found table
			
			}catch (Exception e) {
				LOGGER.warn("Exception in insertion to database");
			}
		}
	}
	
	/**
	 * Inserting unique domain url to co-found table
	 * @param companyUrlFromTextFile
	 * @param companyNameFromUrl
	 * @throws SQLException
	 */
	
	public void insertIntoCompanyFinderTable(CommonFields commonFields, String source) throws SQLException{
		String domainUrl = null;
		final String duplicateDomainUrlMessage = "Duplicate entry";
		final String insertIntoCompanyFinderQuery = "insert into results_provider_db.co_found"
				+ "(company_name,company_url,domain_url,company_status,duplicate_count,status,date_time) values (?,?,?,?,?,?,?)";
		
		if(!source.contains("input")) {
			commonFields.setCompanyName("");
		}
		
		commonFields.setStatus("");
		commonFields.setDuplicateCount(0);
		try {
			commonFields.setCompanyUrl(getHomePageUrl(commonFields.getCompanyUrl()));
			statement = dbConnection.prepareStatement(insertIntoCompanyFinderQuery);
			statement.setString(MagicNumbers.ONE, commonFields.getCompanyName());
			statement.setString(MagicNumbers.TWO, commonFields.getCompanyUrl());
			statement.setString(MagicNumbers.THREE, commonFields.getDomainUrl());
			statement.setString(MagicNumbers.FOUR, commonFields.getCompanyFinderStatus());
			statement.setInt(MagicNumbers.FIVE, commonFields.getDuplicateCount());
			statement.setString(MagicNumbers.SIX, commonFields.getStatus());
			statement.setString(MagicNumbers.SEVEN, commonFields.getDateTime());
			
			int queryUpdationResultCount = statement.executeUpdate();
			GetDbConnection.getDbUpdationStatus(queryUpdationResultCount);
		}catch (Exception e) {
			if(e.getMessage().contains(duplicateDomainUrlMessage)){
				domainUrl = e.getMessage();
				domainUrl = findDomainUrlUsingRegex(domainUrl);
				updateDuplicateDomainUrlCount(domainUrl); 
			}
			LOGGER.warn(e);
		}
	}
	
	
	/**
	 * Extract domain url using regex
	 * @return
	 */
	
	private String findDomainUrlUsingRegex(String domainUrl) {
		Matcher matcher = null;
		final String regexToGetFirstSingleQuoteValue = "([\"'])(?:(?=(\\\\?))\\2.)*?\\1";
		Pattern pattern = Pattern.compile(regexToGetFirstSingleQuoteValue);
		matcher = pattern.matcher(domainUrl);
		if(matcher.find()) {
			domainUrl = matcher.group().replaceAll("\'", "");
		}
		
		return domainUrl;
		
	}
	
	/**
	 * If domain url already exist in co-found table
	 * Then update how the repetition number corresponding to domain url
	 * Instead of re-entering it.
	 */
	
	private void updateDuplicateDomainUrlCount(String domainUrl) {
		final String domainUrlDuplicationCheckQuery = "select duplicate_count from results_provider_db.co_found where domain_url = ?";
		final String updateUrlRepetitionCountQuery = "update results_provider_db.co_found set duplicate_count = ?, date_time = ? where domain_url = ?";
		Integer urlDuplicateCount = 0 ;
		
		try {
			statement = dbConnection.prepareStatement(domainUrlDuplicationCheckQuery);
			statement.setString(1, domainUrl);
			resultSet = statement.executeQuery();
			if(resultSet.next()) {
				urlDuplicateCount = resultSet.getInt(1);
				urlDuplicateCount+=1;
				statement = dbConnection.prepareStatement(updateUrlRepetitionCountQuery);
				statement.setInt(1, urlDuplicateCount);
				statement.setString(2, new Date()+"");
				statement.setString(3, domainUrl);
				statement.executeUpdate();
			}
		
		}catch (Exception e) {
			LOGGER.warn(e);
		}
	}
	
	/**
	 * Finding the home page URL
	 * @throws MalformedURLException 
	 */
	
	public String getHomePageUrl(String temporaryResultUrl)
	{
		
		final String appendString = "://";
		URL urlConnectionURL = null;
		String homePageUrl = null;
		
		try 
		{
			urlConnectionURL = new URL(temporaryResultUrl);
		} 
		catch (MalformedURLException e) 
		{
			LOGGER.warn(e);
		}
		
		if(urlConnectionURL != null) {
			homePageUrl = urlConnectionURL.getProtocol() + appendString + urlConnectionURL.getHost();
		}
		
		return homePageUrl;
	
	}
	
	
	/**
	 * Select the preferrable search engine and get all href links from the search list
	 * Returns the sorted out urls from search result
	 * @param searchUrl
	 * @param document
	 * @return
	 */
	
	private List<String> urlsFromGoogleSearchResultList(String searchUrl, Document document){
		
		final String duckduckGoSearchEngine = "duckduckgo";
		final String bingSearchEngine = "bing";
		final String firstParentTagOfDuckDuckGo = "#links";
		final String secondTagOfDuckDuckGo = ".result__title";
		final String thirdTagOfDuckDuckGo = ".result__a";
		final String aTag = "a";
		final String hrefTag = "href";
		final String firstParentTagOfBing = "#b_content";
		final String secondTagOfBing = "#b_results";
		final String thirdTagOfBing = "h2";
		final String firstParentTagOfGoogle = ".rc";
		final String secondTagOfGoogle = "h3";
		Elements urlLinksFromSearchResult = null;
				
				
		List<String> urlsFromGoogleSearchResultList = new ArrayList<>();
		
		if(searchUrl.contains(duckduckGoSearchEngine)){
			urlLinksFromSearchResult = document.select(firstParentTagOfDuckDuckGo).
					select(secondTagOfDuckDuckGo).select(thirdTagOfDuckDuckGo).select(aTag);
		}else if(searchUrl.contains(bingSearchEngine)){
			urlLinksFromSearchResult = document.select(firstParentTagOfBing).
					select(secondTagOfBing).select(thirdTagOfBing).select(aTag);
		}else {
			urlLinksFromSearchResult = document.select(firstParentTagOfGoogle).
					select(secondTagOfGoogle).select(aTag);
		}
		
		for(Element urlFromSearchResult : urlLinksFromSearchResult){
			urlsFromGoogleSearchResultList.add(urlFromSearchResult.select(aTag).first().attr(hrefTag));
		}
		
		return urlsFromGoogleSearchResultList;
	}
	
	
	/**
	 * Takes the input company name from the text file
	 * Makes google search url
	 */
	
	private String getPreferredSearchEngineUrl(String companyName){
		
		String searchUrlMadeByAddingCareerStringToCompanyName ;
		final String googleSearchEngine = "https://www.google.co.in/search?num=50&q=";
		final String character = "&";
		final String UtfEight = "%26";
	
		searchUrlMadeByAddingCareerStringToCompanyName = googleSearchEngine + companyName.replace(character, UtfEight) ;
		
		return searchUrlMadeByAddingCareerStringToCompanyName;
	}
	
	
	public static void main(String[] args) throws IOException {
		CompanyFinderFromGoogle companyFromGoogle = new CompanyFinderFromGoogle();
		companyFromGoogle.readCompanyNamesFromCoData();
	}

}
