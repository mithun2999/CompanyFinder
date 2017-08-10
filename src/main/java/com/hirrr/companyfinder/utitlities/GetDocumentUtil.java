package com.hirrr.companyfinder.utitlities;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;


public class GetDocumentUtil {
	
	private static final Logger LOGGER = Logger.getLogger(GetDocumentUtil.class.getName());
	
	/**
	 * JSOUP Get Document Response 
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	
	public static Document getJsoupDocumentResponse(String careerURL) throws InterruptedException  {
		long randomTimeGeneratedFromRandomTimeGeneration = RandomValueGeneratorUtil.randomTimeGeneration();
		final String userAgent = "Mozilla/5.0 (X11; Linux x86_64; rv:43.0) Gecko/20100101 Firefox/43.0";
		Document frontPageDocument = null;
		Thread.sleep(randomTimeGeneratedFromRandomTimeGeneration);
		try {
			frontPageDocument = Jsoup.connect(careerURL).userAgent(userAgent).timeout(MagicNumbers.TEN_THOUSAND).get();
		} catch (Exception e) {
			LOGGER.warn(e);
		while (!CheckForInternetConnection.isInternetConnected()) {
				try {
					Thread.sleep(3000);
				} catch (Exception e1) {
					LOGGER.warn(e1);
				}
			}
		Thread.sleep(randomTimeGeneratedFromRandomTimeGeneration);
		try{
			frontPageDocument = Jsoup.connect(careerURL).userAgent(userAgent).timeout(MagicNumbers.TEN_THOUSAND).get();
		}catch(Exception e1){
			LOGGER.warn("Could not get document response from the site :"+careerURL);
		}
		}
		
		return frontPageDocument ;
	}
	
	/**
	 * Get selenium webdriver using headlessFlag value
	 * If it is true, it will be headless otherwise browser will be opened
	 */
	
	public static WebDriver getDriver(Boolean headlessFlag)
	{
		WebDriver firefoxDriver ;
		FirefoxBinary firefoxBinary2 = null ;
		final String seleniumHeadlessProperty1 = "lmportal.xvfb.id";		
		final String seleniumHeadlessPortNumber = ":1";
		final String displayString = "DISPLAY";
		final String seleniumBrowserPreference = "browser.private.browsing.autostart";
	 
		if(headlessFlag)
		{
			
			    String xport = System.getProperty(seleniumHeadlessProperty1, seleniumHeadlessPortNumber);
			    firefoxBinary2 = new FirefoxBinary();
			    firefoxBinary2.setEnvironmentProperty(displayString, xport);
			    firefoxDriver = new FirefoxDriver(firefoxBinary2, null);
		}
		else
		{
			 	FirefoxProfile profile = new FirefoxProfile();
			 	firefoxDriver = new FirefoxDriver(profile);
			 	profile.setPreference(seleniumBrowserPreference,true);						
		}
		
		return firefoxDriver;
	}
	
	
	/**
	 * Load webpage using Selenium
	 * @param firefoxDriver
	 * @param webpageUrl
	 * @throws InterruptedException 
	 */
	
	public static Document loadWebPageSelenium(WebDriver firefoxDriver, String webpageUrl) throws InterruptedException
	{
		Document webpageDocument = null;
		try 
		{
			firefoxDriver.get(webpageUrl);
			Thread.sleep(MagicNumbers.THOUSAND);
		} 
		catch (Exception e) 
		{
			LOGGER.warn(e);
			while (!CheckForInternetConnection.isInternetConnected()) 
			{
				LOGGER.warn(("Internet is disconnected while scrapping..... trying to reconnect...Please Check Cable.."));
				Thread.sleep(MagicNumbers.THOUSAND);
			}
			
			firefoxDriver.get(webpageUrl);
			Thread.sleep(MagicNumbers.THOUSAND);
		}
		
		webpageDocument = Jsoup.parse(firefoxDriver.getPageSource());
		return webpageDocument ;
	}

	
	/**
	 * Close selenium resources after use
	 */
	
	public static void closeSeleniumResources(WebDriver firefoxDriver, FirefoxBinary firefoxBinary){
		
		if(firefoxBinary != null){
			try{
				firefoxBinary.quit();
			}catch(Exception e){
				LOGGER.info(e);
			}
		}
		
		if(firefoxDriver != null){
			try{
				firefoxDriver.quit();
			}catch(Exception e){
				LOGGER.info(e);
			}
		}
	}
}
