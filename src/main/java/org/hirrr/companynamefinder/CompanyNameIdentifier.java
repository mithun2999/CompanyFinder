/*
 * Jobsnatcher, career url finding utility tool.
 * Copyright 2016 (C) <UMN Networks Private Limited>
 * Jobsnatcher company name identification module
 * Created based on some assumpitons like company name will be present in Copyright tag
 * Seems to have 60-70%  accuracy
 */

package org.hirrr.companynamefinder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;

import com.hirrr.companyfinder.finder.DomainUrlFinder;
import com.hirrr.companyfinder.utitlities.GetDocumentUtil;
import com.hirrr.companyfinder.utitlities.MagicNumbers;

public class CompanyNameIdentifier {
	
	private static final Logger LOGGER = Logger.getLogger(CompanyNameIdentifier.class);
	private Document webpageDocument = null ;
	private static final String GROUP = "GROUP";
	private WebDriver firefoxDriver = null;
	private String companyName = null;
	private FirefoxBinary firefoxBinary = null;
	private Integer webpageHttpResponseCode = 0;
	/**
	 * Company Name Finder for ATS
	 */
	
	
	public String atsCompanyNameIdentifier(String careerUrl, Boolean headlessFlag){
		
		
		firefoxDriver = GetDocumentUtil.getDriver(headlessFlag);
		String tempOP;
		
		LOGGER.warn("Started Pattern Company Name Identification...");
	
		try{
				
			webpageDocument = GetDocumentUtil.loadWebPageSelenium(firefoxDriver, careerUrl);
			/**
			 * Checking the HttpResponse of the site
			 */
						
			webpageHttpResponseCode = getHttpStatusResponseCode(careerUrl);

			if(webpageHttpResponseCode != MagicNumbers.ERROR_STATUS){
				
				companyName = commonCodePatternCompanyNameIdentifier(careerUrl);
			
				if(companyName.equals("not found")){
					companyName = otherPatternSitesCompanyNameIdentification(careerUrl);
				}
				
				companyName = cleanCompanyName(companyName);
				
				tempOP = findSpecialCharacterBeforeFirstWord(companyName);
			    
			    companyName = companyName.replace(tempOP, "").trim();
				
			}else{
				companyName = MagicNumbers.ERROR_STATUS+"";
			}
			
			companyName = replaceLastWordFromCompanyName(companyName);
			
			
		}catch(Exception e){
			LOGGER.warn(e);
		}
		
		finally{
			GetDocumentUtil.closeSeleniumResources(firefoxDriver, firefoxBinary);
		}
		
		return companyName;
	}
	
	/**
	 * Get http status reponse code for career url
	 * @param careerUrl
	 * @return
	 */
	
	private Integer getHttpStatusResponseCode(String careerUrl) {
		try{
			URL url = new URL (careerUrl); 
			HttpURLConnection hucl =  ( HttpURLConnection )  url.openConnection (); 
			hucl.setRequestMethod ("GET");  
			hucl.connect () ; 
			webpageHttpResponseCode = hucl.getResponseCode() ;
		}catch(Exception e){
			LOGGER.warn(e);
		}
		
		return webpageHttpResponseCode;
	}
	
	/**
	 * Company name identification process for common code pattern sites
	 * @param careerUrl
	 * @return
	 */
	
	private String commonCodePatternCompanyNameIdentifier(String careerUrl){
		
		if(careerUrl.contains("fastcollab")){
			
			companyName = AtsUrlChecker.getFastcollabAtsCompanyName(webpageDocument, careerUrl);
					
		}else if(careerUrl.contains("ql=India&pg=")||
				(careerUrl.contains("careersitemanager")||
						(careerUrl.contains("?ql=india&pg")||
								(careerUrl.contains("companies.naukri"))))){
			
			companyName = AtsUrlChecker.getNaukriAtsCompanyName(webpageDocument);
				
		}else if(careerUrl.contains("smartrecruiters")){
			
			companyName = AtsUrlChecker.getSmartRecruitersAtsCompanyName(webpageDocument, careerUrl);
			
		}else if(careerUrl.contains("angel.co")){
			
			companyName = AtsUrlChecker.getAngelAtsCompanyName(webpageDocument, careerUrl);
		
		}else if(careerUrl.contains("recruiterbox")){
			
			companyName = AtsUrlChecker.getRecruiterboxAtsCompanyName(webpageDocument, careerUrl);
		
		}else if(careerUrl.contains("workable")){
			
			companyName = AtsUrlChecker.getWorkableAtsCompanyName(webpageDocument);
			
		}else{
			companyName = getCommonCodePatternNameSubList(careerUrl);
		}
		
		return companyName;
	}
	
	/**
	 * Common code pattern company name identification sublist 
	 * @param careerUrl
	 * @return
	 */
	
	private String getCommonCodePatternNameSubList(String careerUrl){
		
		
		if(careerUrl.contains("hirebridge")){
			
			companyName = AtsUrlChecker.getHirebridgeAtsCompanyName(webpageDocument);
			
		}else if(careerUrl.contains("lever")){
			
			companyName = AtsUrlChecker.getLeverAtsCompanyName(webpageDocument);
			
		}else if(careerUrl.contains("jobscore")){
			
			companyName = AtsUrlChecker.getJobscoreAtsCompanyName(webpageDocument);
			
		}else if(careerUrl.contains("greenhouse")){
			
			companyName = AtsUrlChecker.getGreenhouseAtsCompanyName(webpageDocument);
			
		}else if(careerUrl.contains("resumator")){
			
			companyName = AtsUrlChecker.getResumatorAtsCompanyName(webpageDocument);
		}else{
			companyName = "not found";
		}
		
		return companyName;
	}
	
	/**
	 * Other pattern company name identification sublist 
	 * @param careerUrl
	 * @return
	 */
	
	private String getOtherPatternCompanyNameSubList(String careerUrl){

		if(careerUrl.contains("tbe.taleo.net")&&(careerUrl.contains("cws="))){
			
			companyName = AtsUrlChecker.getTaleoFirstModelAtsCompanyName(careerUrl);
			
		}else if((careerUrl.contains("taleo")&&(careerUrl.contains("lang=en")))){
			
			companyName = AtsUrlChecker.getTaleoSecondModelAtsCompanyName(careerUrl);
			
		}else if((careerUrl.contains("taleo")&&(careerUrl.contains("careersection")))){
			
			companyName = AtsUrlChecker.getTaleoThirdModelAtsCompanyName(careerUrl);
			
		}else if((careerUrl.contains("brassring"))){
			
			companyName = AtsUrlChecker.getBrassringAtsCompanyName(webpageDocument);
			
		}else if(careerUrl.contains("zoho")){
			
			companyName = AtsUrlChecker.getZohoAtsCompanyName(webpageDocument);
			
		}
		
		return companyName;
		
	}
	
	/**
	 * Company name identification process for pattern sites other than common code.
	 * @param careerUrl
	 * @return
	 */
	
	private String otherPatternSitesCompanyNameIdentification(String careerUrl){
		
		if(careerUrl.contains("silkroad")){
			
			companyName = AtsUrlChecker.getSilkroadAtsCompanyName(webpageDocument, careerUrl);
			
		}else if(careerUrl.contains("apply2jobs")){

			companyName = AtsUrlChecker.getApply2jobsAtsCompanyName(careerUrl);
			
		}else if(careerUrl.contains("icims")){
			
			companyName = AtsUrlChecker.getIcimsAtsCompanyName(careerUrl);
			
		}else if(careerUrl.contains("peopleclick")){
			
			companyName = AtsUrlChecker.getPeopleclickAtsCompanyName(careerUrl);
							
		}else if(careerUrl.contains("jobvite")){

			companyName = AtsUrlChecker.getJobViteAtsCompanyName(webpageDocument, careerUrl);
			
		}
			companyName = getOtherPatternCompanyNameSubList(careerUrl);
		
		
		return companyName;
	}
	
	/**
	 * Regex to find the last word after space of a String
	 */
	
	private String replaceLastWordFromCompanyName(String companyName){
		
		Pattern p = Pattern.compile("\\s(\\w+)$");
		Matcher m = p.matcher(companyName);
		while(m.find()){
			String lastWord = m.group().toUpperCase(Locale.ENGLISH);
			if(lastWord.equalsIgnoreCase("JOBS")){
				companyName = companyName.replace(lastWord, "");
			}
		}
		
		if(companyName.contains("[]")){
			companyName = companyName.replace("[]", "").trim();
		}
		
		return companyName;
	}
	
	/**
     * Finding the occurrence of any special character before first word
     */
	
	private String findSpecialCharacterBeforeFirstWord(String companyName){
		
	    String tempOP = "";
	    Pattern p = Pattern.compile("^[^\\w]+");
	    Matcher m = p.matcher(companyName);
	    while(m.find()){
	    	tempOP = m.group();
	    }
	    
	    return tempOP;
	}
	
	/**
	 * Removing unwanted words from Company Name
	 * @param ele
	 * @return
	 * @throws IOException 
	 */
		
	public String cleanCompanyName(String companyName) throws IOException{
	
		BufferedReader br;
		String path = "/TxtModule/rmvWordsFrmName.txt" ;
		br = new BufferedReader(new FileReader(path));
		String titleFrmText;
		String compName;
		compName = companyName ;
		
		try{
			while ((titleFrmText = br.readLine()) != null) {
				if(compName.contains(titleFrmText)){
					compName = compName.replaceAll(titleFrmText, "").trim();
				}
			}
		}catch(Exception e){
			LOGGER.warn(e);
		}
		finally{
			br.close();
			LOGGER.warn("After cleanUp :"+compName);
		}
		
		return compName;
		
	}
	
	/**
	 * Finding out the company name of Normal website
	 */
	
	public  String  normalCompanyNameIdentifier(String homepageUrl){
		
		LOGGER.warn("Started Normal Company Name Identification...");
		
		String textDataFromCopyRightTag;
		String temporaryCompanyName = "";
		
		try{
			    firefoxDriver = GetDocumentUtil.getDriver(true);
				webpageDocument = GetDocumentUtil.loadWebPageSelenium(firefoxDriver, homepageUrl);
				
				textDataFromCopyRightTag = getCompanyNameFromCopyRightTag(webpageDocument);

				temporaryCompanyName = textDataFromCopyRightTag;
				
				temporaryCompanyName = removeUnwantedSiteContentsFromCompanyName(temporaryCompanyName);
			 
				temporaryCompanyName = removeUnwantedContentsFromCompanyName(temporaryCompanyName);
			    		
			    temporaryCompanyName = temporaryCompanyName.trim();
			    
			    if(temporaryCompanyName.isEmpty()||temporaryCompanyName.length() 
			    		< MagicNumbers.THREE||temporaryCompanyName.length() > MagicNumbers.SIXTY){
			    	temporaryCompanyName = getCompanyNameFromHomePageUrl(homepageUrl);
			    }
			    
				LOGGER.warn("Final company Name after removing all unwanted Chars : :" +temporaryCompanyName.trim());
				
		}catch(Exception e){
			LOGGER.warn(e);
		}
		
		
		finally{
				GetDocumentUtil.closeSeleniumResources(firefoxDriver, firefoxBinary);
			}
				
		   return temporaryCompanyName;
			
	}
	
	/**
	 * Get company name from domain Url
	 * @param homePageUrl
	 * @return
	 */
	
	private String getCompanyNameFromHomePageUrl(String homePageUrl) {
		String domainUrl = null;
		String companyName = null;
		domainUrl = new DomainUrlFinder().domainFinder(homePageUrl);
		
		if(domainUrl.contains(".")) {
			companyName = domainUrl.substring(0,domainUrl.indexOf(".")).trim();
			companyName = companyName.toUpperCase(Locale.ENGLISH);
		}else {
			companyName = "";
		}
		
		return companyName;
		
	}
	
	
	/**
	 * Get company name from copyright tag of company website
	 * @return
	 */
	
	private String getCompanyNameFromCopyRightTag(Document webpageDocument) {
		
		String textDataFromCopyRightTag ;
		try{
			textDataFromCopyRightTag = webpageDocument.select("*:containsOwn(Copyright)").text();
		}catch(Exception e)
		{
			LOGGER.warn(e);
			textDataFromCopyRightTag = "";
		}
		
		if(textDataFromCopyRightTag.isEmpty()){
			try{
				textDataFromCopyRightTag = webpageDocument.select("*:containsOwn(rights reserved)").text();
			}catch(Exception e){
				LOGGER.warn(e);
				textDataFromCopyRightTag = "";
			}
		}

		if(textDataFromCopyRightTag.isEmpty()){
			try{
				textDataFromCopyRightTag = webpageDocument.select("*:containsOwn(©)").text();
			}catch(Exception e){
				LOGGER.warn(e);
				textDataFromCopyRightTag = "";
			}
		}
		
		return textDataFromCopyRightTag;
	}
	
	
	/**
	 * Removing unwanted site contents like copyright, sitemap etc from company name 
	 * @param temporaryCompanyName
	 * @return
	 */
	
	private String removeUnwantedSiteContentsFromCompanyName(String temporaryCompanyName){
		
		
	    temporaryCompanyName = temporaryCompanyName.replaceAll(":", "");				//Replace : 
	    temporaryCompanyName = temporaryCompanyName.replaceAll("\u00a9", "");			//Replace copyright symbol ©
	    temporaryCompanyName = temporaryCompanyName.replaceAll("@", "");				//Replace @
	    temporaryCompanyName = temporaryCompanyName.replaceAll("\\d{4}", "");			//Replace 4 digits in a single stretch
	    temporaryCompanyName = temporaryCompanyName.
	    		replaceAll("(?i)Copy right reserved by", ""); //Replace copyright reserved by
	    temporaryCompanyName = temporaryCompanyName.replaceAll("\u00AE", "");			//Replace ®
	    temporaryCompanyName = temporaryCompanyName.replaceAll("\u2122", "");			//Replace ™
	    temporaryCompanyName = temporaryCompanyName.replaceAll("(?i)Copyrights", "");
	    temporaryCompanyName = temporaryCompanyName.replaceAll("(?i)Copyright", "");
	    temporaryCompanyName = temporaryCompanyName.replaceAll("(?i)Trademarks", "");
	    temporaryCompanyName = temporaryCompanyName.replaceAll("(?i)Trademark", "");
	    temporaryCompanyName = temporaryCompanyName.replaceAll("(?i)by", "");
	    temporaryCompanyName = temporaryCompanyName.replaceAll("(?i)Disclaimer", "");
	    temporaryCompanyName = temporaryCompanyName.replaceAll("(?i)Policy", "");
	    temporaryCompanyName = temporaryCompanyName.replaceAll(",","");
	    temporaryCompanyName = temporaryCompanyName.replaceAll("(?i)An ISO  Certified Company", "");
	    temporaryCompanyName = temporaryCompanyName.replaceAll("(?i)All Rights Reservered", "");
	    temporaryCompanyName = temporaryCompanyName.replaceAll("(?i)website", "");
	    temporaryCompanyName = temporaryCompanyName.replaceAll("(?i)sitemap", "");
	    temporaryCompanyName = temporaryCompanyName.replaceAll("\\|", "");
	    temporaryCompanyName = temporaryCompanyName.replaceAll("(?i)Site Map", ""); 
	    temporaryCompanyName = temporaryCompanyName.replaceAll("(?i)Terms of Service", "");
	    temporaryCompanyName = temporaryCompanyName.replaceAll("\\(.*\\)", "");
	    temporaryCompanyName = temporaryCompanyName.replaceAll("(?i)Terms of use", "");
	    temporaryCompanyName = temporaryCompanyName.replaceAll("(?i)Follow Us", "");
	    temporaryCompanyName = temporaryCompanyName.replaceAll("(?i)Terms", "");
	    temporaryCompanyName = temporaryCompanyName.replaceAll("(?i)user", "");
		temporaryCompanyName = temporaryCompanyName.replaceAll("(?i)designed", "");
		temporaryCompanyName = temporaryCompanyName.replaceAll("(?i)All rights reserved", "");
		temporaryCompanyName = temporaryCompanyName.replaceAll("(?i)All right reserved", "");
	    temporaryCompanyName = temporaryCompanyName.replaceAll("(?i)Privacy", "");
	    temporaryCompanyName = temporaryCompanyName.replaceAll("(?i)Google Map data", "");
	    temporaryCompanyName = temporaryCompanyName.replaceAll("(?i)Google", "");    
	    temporaryCompanyName = temporaryCompanyName.replaceAll("(?i)ALL RIGHTS ARE RESERVED.", "");
	    temporaryCompanyName = temporaryCompanyName.replaceAll("(?i)PROBLEM LOADING PAGE", "");
	    temporaryCompanyName = temporaryCompanyName.replaceAll("(?i)Powered", "");
	    temporaryCompanyName = temporaryCompanyName.replaceAll("(?i)PATENT AND", "");
	    temporaryCompanyName = temporaryCompanyName.replaceAll("(?i)WEB DESIGN", "");
	    temporaryCompanyName = temporaryCompanyName.replaceAll("(?i)homepage", "");
	    temporaryCompanyName = temporaryCompanyName.replaceAll("(?i)home page", "");
	    temporaryCompanyName = temporaryCompanyName.replaceAll("(?i)statement", "");
	    temporaryCompanyName = temporaryCompanyName.replaceAll("(?i)your local experts!", "");
	    temporaryCompanyName = temporaryCompanyName.replaceAll("(?i)SITE DESIGN", "");
	    temporaryCompanyName = temporaryCompanyName.replaceAll("(?i)/ LOGO", "");
	    temporaryCompanyName = temporaryCompanyName.replaceAll("(?i)Free Encyclopedia of Data  Open Data", "");
		
		return temporaryCompanyName;
	}
	
	
	/**
	 * Reducing the length of company name by removing unwanted strings 
	 */
	
	private String removeUnwantedContentsFromCompanyName(String temporaryCompanyName){
		 
		 Integer stringLengthToBeRemoved;
		 String wordToFind = "";
		 try {
			wordToFind = getIndexOfPrivateLimitedStrings(temporaryCompanyName);
		 } catch (IOException e) {
			LOGGER.warn(e);
			wordToFind = "";
		 }
		 
		 try {
		 if(!wordToFind.isEmpty()){
			 Pattern word = Pattern.compile(wordToFind);
			 Matcher match = word.matcher(temporaryCompanyName.toUpperCase(Locale.ENGLISH));
			 while (match.find()) {
		    	stringLengthToBeRemoved = match.end();
		    	temporaryCompanyName = removePrivateLimitedStringFromCompanyName(temporaryCompanyName, stringLengthToBeRemoved);
			 }
		    
		    LOGGER.warn("After reducing the length :: " + temporaryCompanyName);
		 }
		 }catch (Exception e) {
			 temporaryCompanyName = "";
		}
		 return temporaryCompanyName;
	}
	
	
	/**
	 * Find the index of PVT Strings
	 * Remove the strings after the PVT strings are found 
	 * Adds accuracy for finding the company name
	 * @param ele
	 * @return
	 * @throws IOException 
	 */
	
	public String getIndexOfPrivateLimitedStrings(String ele) throws IOException{
		String wordToFind = null;
		BufferedReader br = null;
		String path = "/TxtModule/PvtWords.txt" ;
		br = new BufferedReader(new FileReader(path));
		String titleFrmText = "";
		String compName = "";
		compName = ele.toUpperCase(Locale.ENGLISH) ;
		
		try{
			while ((titleFrmText = br.readLine()) != null) {
				if(compName.contains(titleFrmText)){
					wordToFind = titleFrmText;
					break;
				}
			}
		}catch(Exception e){
			LOGGER.warn(e);
		}
		
		finally{
			br.close();
		}
		
		LOGGER.warn("Getting the index of PVT LTD word from given STRING : :"+compName);
		return wordToFind;
		
	}
	
	/**
	 * Remove private limited & Group strings from company name
	 * @return
	 */
	
	public static String removePrivateLimitedStringFromCompanyName
	(String temporaryCompanyName, Integer stringLengthToBeRemoved){
	
   	 temporaryCompanyName = temporaryCompanyName.toUpperCase(Locale.ENGLISH);
   	
   	 if(temporaryCompanyName.contains(GROUP)){
   		 temporaryCompanyName  = temporaryCompanyName.substring
   				 (0, temporaryCompanyName.indexOf(GROUP)+MagicNumbers.FIVE).trim();
   	 }else if((temporaryCompanyName.contains("LTD"))){
   		 temporaryCompanyName  = temporaryCompanyName.substring
   				 (0, temporaryCompanyName.indexOf("LTD")+MagicNumbers.THREE).trim();
   	 }else{
		
     try{
		 temporaryCompanyName  = temporaryCompanyName.substring(0, stringLengthToBeRemoved).trim();
     }catch(Exception e){
    	 LOGGER.warn(e);
		 temporaryCompanyName = "" ;
	 }
   	 }
		
   	 return temporaryCompanyName;
		
	}
	
	
	public static void main(String[] args) {
		CompanyNameIdentifier company = new CompanyNameIdentifier();
		company.normalCompanyNameIdentifier("http://data.danetsoft.com");
	}
	
}
