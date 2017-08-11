package org.hirrr.companynamefinder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;

import com.hirrr.companyfinder.utitlities.GetDocumentUtil;

public class CompanyNameFromSocialMedia {
	
	private static final Logger LOGGER = Logger.getLogger(CompanyNameFromSocialMedia.class.getName());
	private List<String> linkedLinksList;
	private List<String> facebookLinksList;
	private List<String> twitterLinksList;
	private String siteName ;
	private Document document ;
	
	/**
	 * Storing the social media links in corresponding list
	 * @param companyUrl
	 * @throws InterruptedException
	 */
	
	private void storeSocialMediaLinksToList(String companyUrl) throws InterruptedException {
		
		linkedLinksList = new ArrayList<>();
		facebookLinksList = new ArrayList<>();
		twitterLinksList = new ArrayList<>();
		
		
		try {
			
		document = GetDocumentUtil.getJsoupDocumentResponse(companyUrl);
	
		Elements jobElements = document.select("a");
		for(Element singleJobElement : jobElements){
			
			if(singleJobElement.select("a").attr("href").contains("linkedin.com/company")){
				linkedLinksList.add(singleJobElement.select("a").attr("href"));
			}else if(singleJobElement.select("a").attr("href").contains("twitter.com")){
				twitterLinksList.add(singleJobElement.select("a").attr("href"));
			}else if(singleJobElement.select("a").attr("href").contains("facebook.com")){
				facebookLinksList.add(singleJobElement.select("a").attr("href"));
			}
		}
		
		printArrayList();
		}catch (Exception e) {
			LOGGER.warn(e);
		}
	}
	
	private void printArrayList() {
		
		LOGGER.info(linkedLinksList);
		LOGGER.info(twitterLinksList);
		LOGGER.info(facebookLinksList);

	}
	
	/**
	 * Processing linkedin related urls obtained from the site.
	 * @return
	 */
	
	private String companyNameFromLinkedInUrls() {
		String companyNameSubString = null;
		siteName = "LINKEDIN";
		
		for(String eachUrlFromLinkedLinksList : linkedLinksList) {
			companyNameSubString = eachUrlFromLinkedLinksList.substring(eachUrlFromLinkedLinksList.
					indexOf("company/")+8, eachUrlFromLinkedLinksList.length()).trim();
			if(companyNameSubString.contains("?")) {
				seleniumSearchToFindCompanyName(siteName, eachUrlFromLinkedLinksList);
			}
		}
		
		return null;
	}
	
	
	/**
	 * Processing twitter related urls obtained from the site.
	 * @return
	 */
	
	private String companyNameFromTwitterUrls() {
		
		
		
		return null;
	}
	
	/**
	 * Processing facebook related urls obtained from the site.
	 * @return
	 */
	
	private String companyNameFromFacebookUrls() {
		
		
		
		return null;
	}
	
	
	/**
	 * Common selenium function to search company in respective websites
	 * @return
	 */
	
	private String seleniumSearchToFindCompanyName(String siteName, String urlToSearch) {
		
		WebDriver firefoxDriver = GetDocumentUtil.getDriver(true);
		
		try {
			document =  GetDocumentUtil.loadWebPageSelenium(firefoxDriver, urlToSearch);
		}catch (Exception e) {
			LOGGER.warn(e);
		}
		
		if(siteName.equals("LINKEDIN")) {
			getCompanyNameFromLinkedInPage(document);
		}
		
		
		
		return null;
	}
	
	/**
	 * Get company name from linked in page
	 * @return
	 */
	
	private String getCompanyNameFromLinkedInPage(Document document) {
		String companyName = null;
		
		
		
		
		
		return companyName;
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		String companyName = null;
		CompanyNameFromSocialMedia com = new CompanyNameFromSocialMedia();
		String path = "/home/mithunmanohar/Music/urls.txt";
		BufferedReader br = null;
		br = new BufferedReader(new FileReader(path));
		while((companyName = br.readLine())!=null) {
			
			com.storeSocialMediaLinksToList(companyName);
			
		}
		br.close();
	}

}
