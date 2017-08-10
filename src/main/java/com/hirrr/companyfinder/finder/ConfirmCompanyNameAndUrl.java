package com.hirrr.companyfinder.finder;

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

public class ConfirmCompanyNameAndUrl {
	
	/**
	 * Process for finding exact domain url of a company.
	 * If it is found it will return NULL else the exact domain url. 
	 * @return
	 */
	
	public String getConfirmedDomainUrlForCompanyName(String companyName, List<String> fiftySearchResultUrlsFromGoogle) {
		
		String[] companyNameSplitArray ;
		Integer wordOccurenceCountInUrl = 0;
		Map<String, Integer> prioritizedUrlList = new TreeMap<>();
		companyNameSplitArray = companyName.split(" ");
		for(String eachSearchResultUrlFromGoogle : fiftySearchResultUrlsFromGoogle) {
				eachSearchResultUrlFromGoogle = new DomainUrlFinder().domainFinder(eachSearchResultUrlFromGoogle);
				for(String companyNameWord : companyNameSplitArray) {
					if(eachSearchResultUrlFromGoogle.toUpperCase(Locale.ENGLISH)
							.contains(companyNameWord.toUpperCase(Locale.ENGLISH))) {
						wordOccurenceCountInUrl+=1;
					}
				}
				
				prioritizedUrlList.put(eachSearchResultUrlFromGoogle, wordOccurenceCountInUrl);
				wordOccurenceCountInUrl = 0;
				for (Map.Entry<String, Integer> entry : prioritizedUrlList.entrySet())
				{
				    System.out.println(entry.getKey() + "/" + entry.getValue());
				}
		}
		
		   // get entrySet from HashMap object
        Set<Map.Entry<String, Integer>> prioritizedUrlListSet = prioritizedUrlList.entrySet();
 
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
 
//        System.out.println("Sorting HashMap by its Values in descending order\n");
 
        // iterate LinkedHashMap to retrieved stored values
        for(Map.Entry<String, Integer> lhmap : companyFounderLHMap.entrySet()){
            System.out.println("Key : "  + lhmap.getKey() + "\t\t"
                    + "Value : "  + lhmap.getValue());
        }
		return null;
	}
	

	
	public static void main(String[] args) {
		List<String> fiftySearchResultUrlsFromGoogle = new ArrayList<>();
		fiftySearchResultUrlsFromGoogle.add("http://www.consortdigital.com");
		fiftySearchResultUrlsFromGoogle.add("http://2020imagingindia.com");
		fiftySearchResultUrlsFromGoogle.add("http://21stcenturyinformatics.com");
		fiftySearchResultUrlsFromGoogle.add("http://24x7learning.com");
		fiftySearchResultUrlsFromGoogle.add("http://360consort.com");
		fiftySearchResultUrlsFromGoogle.add("http://365tours.com");
		fiftySearchResultUrlsFromGoogle.add("http://3dsoc.com");
		fiftySearchResultUrlsFromGoogle.add("http://3dplmsoftware.com");
		ConfirmCompanyNameAndUrl com = new ConfirmCompanyNameAndUrl();
		com.getConfirmedDomainUrlForCompanyName("consort digital", fiftySearchResultUrlsFromGoogle);
	}

}
