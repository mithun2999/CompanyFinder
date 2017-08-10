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
import org.apache.tools.ant.MagicNames;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.core.type.StandardAnnotationMetadata;

import com.hirrr.companyfinder.commonfields.CommonFields;
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
	private String domainUrl = null;
	
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
		
		String urlToSearchInGoogle = null;
		Document googleSearchResultDocument = null;
		List<String> fiftySearchResultUrlsFromGoogle ;
		
		long randomTimeGeneratedFromRandomTimeGeneration = RandomValueGeneratorUtil.randomTimeGeneration();
		Thread.sleep(randomTimeGeneratedFromRandomTimeGeneration);
	
		urlToSearchInGoogle = getPreferredSearchEngineUrl(companyName).replaceAll(" ", "+").trim();		//Replacing space by + symbol to make the search query
	
		LOGGER.warn("Search URL : :"+urlToSearchInGoogle);
	
		googleSearchResultDocument = GetDocumentUtil.getJsoupDocumentResponse(urlToSearchInGoogle);
	
		fiftySearchResultUrlsFromGoogle = urlsFromGoogleSearchResultList(urlToSearchInGoogle, googleSearchResultDocument);
		
		LOGGER.warn("First 50 search result urls >> After Search : "+fiftySearchResultUrlsFromGoogle);
		
		processingOfFiftyUrls(fiftySearchResultUrlsFromGoogle);
		
		return fiftySearchResultUrlsFromGoogle;
	}
	
	
	/**
	 * Iterating through the fifty search result urls, Finding the company domain Url
	 * Inserting only the unique values into company finder table 
	 *
	 */
	
	private void processingOfFiftyUrls(List<String> fiftySearchResultUrlsFromGoogle) {
		CommonFields commonFields = new CommonFields();
		commonFields.setCompanyFinderStatus("ready");
		commonFields.setDateTime(new Date()+"");
		
		for(String singleUrlFromSearchResult : fiftySearchResultUrlsFromGoogle) {
			
			try {
				
			commonFields.setCompanyUrl(singleUrlFromSearchResult);
			commonFields.setDomainUrl(new DomainUrlFinder().domainFinder(singleUrlFromSearchResult));
			
			insertIntoCompanyFinderTable(commonFields);		//Inserting into co-found table
			
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
	
	public void insertIntoCompanyFinderTable(CommonFields commonFields) throws SQLException{
		
		final String duplicateDomainUrlMessage = "Duplicate entry";
		final String insertIntoCompanyFinderQuery = "insert into results_provider_db.co_found"
				+ "(company_name,company_url,domain_url,company_status,duplicate_count,status,date_time) values (?,?,?,?,?,?,?)";
		
		commonFields.setCompanyName("");
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
			System.out.println(statement);
			int queryUpdationResultCount = statement.executeUpdate();
			GetDbConnection.getDbUpdationStatus(queryUpdationResultCount);
		}catch (Exception e) {
			if(e.getMessage().contains(duplicateDomainUrlMessage)){
				domainUrl = e.getMessage();
				domainUrl = findDomainUrlUsingRegex(domainUrl);
				updateDuplicateDomainUrlCount(domainUrl); 
			}
			LOGGER.warn(e);
		}finally {
			GetDbConnection.closeDbResources(dbConnection, statement, resultSet );
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
		final String domainUrlDuplicationCheckQuery = "select count(*) from results_provider_db.co_found where domain_url = ?";
		final String updateUrlRepetitionCountQuery = "update results_provider_db.co_found set duplicate_count = ?, set date_time where domain_url = ?";
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
		final String careerString = " careers";
		final String character = "&";
		final String UtfEight = "%26";
	
		searchUrlMadeByAddingCareerStringToCompanyName = googleSearchEngine + companyName.replace(character, UtfEight) + careerString ;
		
		return searchUrlMadeByAddingCareerStringToCompanyName;
	}
	
	
	public static void main(String[] args) throws IOException {
		CompanyFinderFromGoogle companyFromGoogle = new CompanyFinderFromGoogle();
		companyFromGoogle.readCompanyNamesFromCoData();
	}

}
