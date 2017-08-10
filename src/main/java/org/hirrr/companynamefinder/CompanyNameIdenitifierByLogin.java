package org.hirrr.companynamefinder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.hirrr.companyfinder.utitlities.GetDbConnection;
/**
 * Identifying company name and it's industry from linkedin
 * @author mithunmanohar
 *
 */

public class CompanyNameIdenitifierByLogin {
	private WebDriver firefoxDriver;
	private FirefoxBinary firefoxBinary2 = null ;
	private static final String LINKEDIN_HOME_PAGE_URL = "https://www.linkedin.com/";
	
	/**
	 * Browsing the identified linkedin share url in linkedin & finding company name & it's industry
	 * @throws IOException
	 */
	
	private void connectToLinkedIn(String LINKEDIN_HOME_PAGE_URL) throws IOException{
			String companyName ;
			String industry;
		    Document document = null;
			try{
				
					firefoxDriver = getDriver(false);
					loadWebPageSelenium(firefoxDriver, LINKEDIN_HOME_PAGE_URL);
					document = Jsoup.parse(firefoxDriver.getPageSource());
					companyName = document.select(".main").select(".org-top-card-module__details").select("h1").first().text();
					industry =  document.select(".main").select(".org-top-card-module__details").select(".company-industries").text();
					System.out.println("Company Name Found :"+companyName);
					System.out.println("Industry Found :"+industry);
					insertIntoCompanyNameLoginTable(companyName, industry);
				
			}catch(Exception e){
				e.printStackTrace();
			}
			
	}
	
	/**
	 * Inserting company url and it's name to database
	 * @param companyUrlFromTextFile
	 * @param companyNameFromUrl
	 * @throws SQLException
	 */
	
	public void insertIntoCompanyNameLoginTable(String companyUrlFromTextFile, String companyNameFromUrl) throws SQLException{
		Connection connection = null;
		connection = GetDbConnection.getDbConnection();
		String sql = "insert into jobboard_scrap.CompanyNameLogin (company_name, industry) values (?,?)";
		PreparedStatement statement = null;
		statement = connection.prepareStatement(sql);
		statement.setString(1, companyUrlFromTextFile);
		statement.setString(2, companyNameFromUrl);
		int a = statement.executeUpdate();
		if(a>0){
			System.out.println("success");
		}else{
			System.out.println("Failed");
		}
	}
	
	
	
	/**
	 * Loading linkedin home page using Selenium webdriver
	 * @param firefoxDriver
	 * @param webpageUrl
	 * @throws InterruptedException
	 */
	
	public void loadWebPageSelenium(WebDriver firefoxDriver, String webpageUrl) throws InterruptedException
	{
		try 
		{
			firefoxDriver.get(webpageUrl);
			Thread.sleep(2000);
		} 
		catch (Exception e) 
		{
			firefoxDriver.get(webpageUrl);
			Thread.sleep(2000);
		}
		
		firefoxDriver.findElement(By.id("login-email")).sendKeys("arjun200@gmail.com");
		firefoxDriver.findElement(By.id("login-password")).sendKeys("1234umn");
		firefoxDriver.findElement(By.id("login-submit")).click();
	}
	
	/**
	 * Getting selenium webdriver instance
	 * @param headlessFlag
	 * @return
	 */
	
	public WebDriver getDriver(Boolean headlessFlag)
	{
		final String SELENIUM_HEADLESS_PROPERTY_1 = "lmportal.xvfb.id";		
		final String SELENIUM_HEADLESS_PORT_NO = ":1";
		final String SELENIUM_FIREFOX_BROWSER_SET_PREFERENCE = "browser.private.browsing.autostart";
	 
		if(headlessFlag)
		{
			    String Xport = System.getProperty(SELENIUM_HEADLESS_PROPERTY_1, SELENIUM_HEADLESS_PORT_NO);
			    firefoxBinary2 = new FirefoxBinary();
			    firefoxBinary2.setEnvironmentProperty("DISPLAY", Xport);
			    firefoxDriver = new FirefoxDriver(firefoxBinary2, null);
		}
		else
		{
			 	FirefoxProfile profile = new FirefoxProfile();
			 	firefoxDriver = new FirefoxDriver(profile);
			 	profile.setPreference(SELENIUM_FIREFOX_BROWSER_SET_PREFERENCE,true);						
		}
		
		return firefoxDriver;
	}
	
	
	public static void main(String[] args) throws IOException {
		CompanyNameIdenitifierByLogin clogin = new CompanyNameIdenitifierByLogin();
		clogin.connectToLinkedIn("");
	}
}
