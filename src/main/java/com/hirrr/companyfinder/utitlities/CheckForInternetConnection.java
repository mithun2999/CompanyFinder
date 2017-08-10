

package com.hirrr.companyfinder.utitlities;

import java.io.IOException;
import java.util.logging.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;



/**
 * Check if internet connection is available or not 
 * @author mithunmanohar
 *
 */

public class CheckForInternetConnection {
	
	private CheckForInternetConnection() {
	}
	
	private static final Logger LOGGER = Logger.getLogger(CheckForInternetConnection.class.getName());
	private static final  String GOOGLE_URL = "http://google.com";
	private static final  Integer TIMEOUT = 25000;
	
	/**
	 * Check if internet connection is available
	 * @return
	 */
	
	public static boolean isInternetConnected () {
		boolean isConnected = false ;
		Document webpageDocument;
		
		try {
			webpageDocument = Jsoup.connect(GOOGLE_URL).timeout(TIMEOUT).get();
			
			if(webpageDocument!=null){
				isConnected = true;
			}else{
				isConnected = false;
			}

		} catch (IOException e) {
			LOGGER.severe("Internet is not connected " + e);
		}
		
		return isConnected;
	}
}
