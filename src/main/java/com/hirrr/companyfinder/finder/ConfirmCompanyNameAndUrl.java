package com.hirrr.companyfinder.finder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.hirrr.companyfinder.utitlities.GetDocumentUtil;

public class ConfirmCompanyNameAndUrl {
	
	private static final Logger LOGGER = Logger.getLogger(ConfirmCompanyNameAndUrl.class.getName());
	private boolean hasCompanyNameFirstWordMatchInDomainUrl = false;
	private String[] companyNameSplitArray ;
	
	/**
	 * Process for finding exact domain url of a company.
	 * If it is found it will return NULL else the exact domain url. 
	 * @return
	 */
	
	public String getConfirmedDomainUrlForCompanyName(String companyName, List<String> fiftySearchResultUrlsFromGoogle) {
		
		String domainUrlFromGoogleSearchResult = null;
		boolean hasUrlMatchFlag = false;
		Integer wordOccurenceCountInUrl = 0;
		Map<String, Integer> prioritizedUrlList = new TreeMap<>();
		Map<String, Integer> firstWordMatchUrlList = new LinkedHashMap<>();
		companyNameSplitArray = companyName.split(" ");
		LOGGER.info(fiftySearchResultUrlsFromGoogle);
		for(String eachSearchResultUrlFromGoogle : fiftySearchResultUrlsFromGoogle) {
				
				eachSearchResultUrlFromGoogle = new DomainUrlFinder().domainFinder(eachSearchResultUrlFromGoogle);
				
				for(String companyNameWord : companyNameSplitArray) {
					
					if(eachSearchResultUrlFromGoogle.toUpperCase(Locale.ENGLISH)
							.contains(companyNameWord.toUpperCase(Locale.ENGLISH))) {
						wordOccurenceCountInUrl+=1;
					}
				}
			
				hasCompanyNameFirstWordMatchInDomainUrl = checkForFirstCompanyNameWordMatchInDomainUrl(companyNameSplitArray[0], eachSearchResultUrlFromGoogle);
				if((wordOccurenceCountInUrl>=1) && (hasCompanyNameFirstWordMatchInDomainUrl)) {
					firstWordMatchUrlList.put(eachSearchResultUrlFromGoogle, wordOccurenceCountInUrl);
				}else {
					prioritizedUrlList.put(eachSearchResultUrlFromGoogle, wordOccurenceCountInUrl);
				}
				
				wordOccurenceCountInUrl = 0;
		}
		
		LOGGER.info(firstWordMatchUrlList);
		Set<Map.Entry<String, Integer>> prioritizedUrlListSet = null;
		if(!firstWordMatchUrlList.isEmpty()) {
			  // get entrySet from HashMap object
			 prioritizedUrlListSet = firstWordMatchUrlList.entrySet();
	 
		}else {
			 prioritizedUrlListSet = prioritizedUrlList.entrySet();
		}
		 
        // convert HashMap to List of Map entries
        List<Map.Entry<String, Integer>> companyFounderListEntry = 
                new ArrayList<Map.Entry<String, Integer>>(prioritizedUrlListSet);
 
        // sort list of entries using Collections class utility method sort(ls, cmptr)
        Collections.sort(companyFounderListEntry, 
                new Comparator<Map.Entry<String, Integer>>() {
 
            @Override
            public int compare(Entry<String, Integer> es1, 
                    Entry<String, Integer> es2) {
                return es2.getValue().compareTo(es1.getValue());
            }
        });
 
        // store into LinkedHashMap for maintaining insertion order
        Map<String, Integer> companyFounderLHMap = 
                new LinkedHashMap<String, Integer>();
 
        // iterating list and storing in LinkedHahsMap
        for(Map.Entry<String, Integer> map : companyFounderListEntry){
            companyFounderLHMap.put(map.getKey(), map.getValue());
        }
 
        // iterate LinkedHashMap to retrieved stored values
        for(Map.Entry<String, Integer> lhmap : companyFounderLHMap.entrySet()){
        	LOGGER.info("Key : "  + lhmap.getKey() + "\t\t"
                    + "Value : "  + lhmap.getValue());
        	if(lhmap.getValue() > 0) {
        		domainUrlFromGoogleSearchResult = lhmap.getKey();
        		hasUrlMatchFlag = true;
        		break;
        	}
        }
       
        
        if(hasUrlMatchFlag) {
        	 LOGGER.info("Domain Url Found From Google :"+domainUrlFromGoogleSearchResult);
        }else {
        	domainUrlFromGoogleSearchResult = "";
        	LOGGER.info("Domain Url does not exist");
        }
        
		return domainUrlFromGoogleSearchResult;
	}
	
	/**
	 * Check if company name first Word have found a match in domain url
	 * @param companyName
	 * @param eachSearchResultUrlFromGoogle
	 * @return
	 */
	private boolean checkForFirstCompanyNameWordMatchInDomainUrl(String companyName, String eachSearchResultUrlFromGoogle) {
			hasCompanyNameFirstWordMatchInDomainUrl = false;
			Integer loopCount = 0;
	
			if(loopCount==0) {
				if(eachSearchResultUrlFromGoogle.toUpperCase(Locale.ENGLISH)
						.contains(companyName.toUpperCase(Locale.ENGLISH))) {
					hasCompanyNameFirstWordMatchInDomainUrl = true;
				}
				loopCount++;
			}
		return hasCompanyNameFirstWordMatchInDomainUrl;
	}
	
	
	private static String getPreferredSearchEngineUrl(String companyName){
		
		String searchUrlMadeByAddingCareerStringToCompanyName ;
		final String googleSearchEngine = "https://www.google.co.in/search?num=50&q=";
		final String careerString = " careers";
		final String character = "&";
		final String UtfEight = "%26";
	
		searchUrlMadeByAddingCareerStringToCompanyName = googleSearchEngine + companyName.replace(character, UtfEight) + careerString ;
		
		return searchUrlMadeByAddingCareerStringToCompanyName;
	}

	private static List<String> urlsFromGoogleSearchResultList(String searchUrl, Document document){
		
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
	
	
	public static void main(String[] args) throws InterruptedException, IOException {
			String companyName = null;
			String path = "/home/mithunmanohar/Music/companies.txt";
			BufferedReader br = null;
			br = new BufferedReader(new FileReader(path));
			while((companyName = br.readLine())!=null) {
			LOGGER.info("Company name :"+companyName);
			Document googleSearchResultDocument = null;
			String urlToSearchInGoogle = null;
			List<String> fiftySearchResultUrlsFromGoogle = new ArrayList<>();
			urlToSearchInGoogle = getPreferredSearchEngineUrl(companyName).replaceAll(" ", "+").trim();	
			googleSearchResultDocument = GetDocumentUtil.getJsoupDocumentResponse(urlToSearchInGoogle);//Replacing space by + symbol to make the search query
			fiftySearchResultUrlsFromGoogle = urlsFromGoogleSearchResultList(urlToSearchInGoogle, googleSearchResultDocument);
			ConfirmCompanyNameAndUrl com = new ConfirmCompanyNameAndUrl();
			com.getConfirmedDomainUrlForCompanyName(companyName, fiftySearchResultUrlsFromGoogle);
			LOGGER.info("Completed");
		}
			br.close();
	}

}
