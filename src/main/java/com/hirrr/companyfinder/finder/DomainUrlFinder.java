package com.hirrr.companyfinder.finder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

/**
 * Finding domain url for a given company url
 * @author mithunmanohar
 *
 */


public class DomainUrlFinder {
	
	private static final Logger LOGGER = Logger.getLogger(DomainUrlFinder.class.getName());
	
    public String domainFinder(String inputUrlToDomainFinder) {
	     
    	String domainUrl = null;

    	try {
		     	URI uri = new URI(inputUrlToDomainFinder);
		     	String hostname = uri.getHost();
		         if((!hostname.startsWith("www"))&&(!isAtsUrl(hostname))) {
		        	 
		        	 String[] dotarray = hostname.split("\\.");
		        	 String temp = dotarray[1];
		             boolean result = matcher("."+temp+".");
		             if(!result) {
		            	 hostname = hostname.replaceFirst(dotarray[0]+".", "");
		            	 domainUrl = hostname;
		             }else {
		            	 domainUrl = hostname;
		             }
		        	 
		         }else {
		        	 hostname = hostname.replaceAll("www.", "");
		        	 domainUrl = hostname;
		         }
	         
			}catch (Exception e) {
                LOGGER.warn(e);
			}
    	
    	return domainUrl;
    }
    
  	    
  	    /**
  	     * domain Matcher
  	     * @param str
  	     * @return
  	     */
  	    private boolean matcher(String str) {
  	    	final String regexForIdentifyingDomain = "[.](ac|ad|ae|af|ag|ai|al|am|an|ao|aq|ar|as|at|au|aw|ax|az|ba|bb|bd|be|bf|bg|bh|"
  	    			+ "bi|bj|bl|bm|bn|bo|bq|br|bs|bt|bv|bw|by|bz|ca|cc|cd|cf|cg|ch|ci|ck|cl|cm|cn|co|cr|cs|cu|cv|cw|cx|cy|cz|de|dj|dk|"
  	    			+ "dm|do|dz|ec|ee|eg|eh|er|es|et|eu|fi|fj|fk|fm|fo|fr|ga|gb|uk|gd|ge|gf|gg|gh|gi|gl|gm|gn|gp|gq|gr|gs|gt|gu|gw|gy|"
  	    			+ "hk|hm|hn|hr|ht|hu|id|ie|il|im|in|io|iq|ir|is|it|je|jm|jo|jp|ke|kg|kh|ki|km|kn|kp|kr|kw|ky|kz|la|lb|lc|li|lk|lr|ls|"
  	    			+ "lt|lu|lv|ly|ma|mc|md|me|mf|mg|mh|mk|ml|mm|mn|mo|mp|mq|mr|ms|mt|mu|mv|mw|mx|my|mz|na|nc|ne|nf|ng|ni|nl|no|np|nr|nu|"
  	    			+ "nz|om|pa|pe|pf|pg|ph|pk|pl|pm|pn|pr|ps|pt|pw|py|qa|re|ro|rs|ru|rw|sa|sb|sc|sd|se|sg|sh|si|sj|sk|sl|sm|sn|so|sr|ss|"
  	    			+ "st|su|sv|sx|sy|sz|tc|td|tf|tg|th|tj|tk|tl|tm|tn|to|tp|tr|tt|tv|tw|tz|ua|ug|uk|um|us|uy|uz|va|vc|ve|vg|vi|vn|vu|wf|ws"
  	    			+ "|ye|yt|yu|za|zm|zr|zw|yu|academy|accountant|accountants|active|actor|ads|adult|aero|agency|airforce|apartments|app|"
  	    			+ "archi|army|art|associates|attorney|auction|audible|audio|author|auto|autos|aws|baby|band|bar|barefoot|bargains|"
  	    			+ "baseball|beauty|beer|best|bestbuy|bet|bid|bike|bingo|bio|biz|black|blackfriday|blockbuster|blog|blue|boo|book|"
  	    			+ "bot|boutique|box|broker|build|builders|business|buy|buzz|cab|cafe|call|cam|camera|camp|cancerresearch|capital|"
  	    			+ "cards|care|career|careers|cars|case|cash|casino|catering|catholic|center|ceo|cfd|channel|chat|cheap|christmas|"
  	    			+ "church|circle|city|claims|cleaning|click|clinic|clothing|cloud|club|coach|codes|coffee|college|community|company|"
  	    			+ "computer|condos|construction|consulting|contact|contractors|cooking|cool|coop|country|coupon|coupons|courses|credit|"
  	    			+ "creditcard|cricket|cruises|dad|dance|date|dating|day|deal|deals|degree|delivery|democrat|dental|dentist|design|dev|diamonds|"
  	    			+ "diet|digital|direct|directory|discount|diy|doctor|dog|domains|download|duck|earth|eat|eco|education|email|energy|engineer|"
  	    			+ "engineering|equipment|esq|estate|events|exchange|expert|exposed|express|fail|faith|family|fan|fans|farm|fashion|fast|feedback|"
  	    			+ "film|final|finance|financial|fire|fish|fishing|fit|fitness|flights|florist|flowers|fly|foo|food|foodnetwork|football|forsale|"
  	    			+ "forum|foundation|free|frontdoor|fun|fund|furniture|fyi|gallery|game|games|garden|gift|gifts|gives|glass|global|gold|golf|gop|"
  	    			+ "graphics|green|gripe|group|guide|guitars|guru|hair|hangout|health|healthcare|help|here|hiphop|hiv|hockey|holdings|holiday|homegoods"
  	    			+ "|homes|homesense|horse|host|hosting|hot|house|how|ice|info|ing|ink|institute|insurance|insure|international|investments|jewelry|"
  	    			+ "jobs|joy|kim|kitchen|land|latino|law|lawyer|lease|legal|lgbt|life|lifeinsurance|lighting|like|limited|limo|link|live|living|loan|"
  	    			+ "loans|locker|lol|lotto|love|luxe|luxury|makeup|management|market|marketing|markets|mba|media|meet|meme|memorial|men|menu|mint|mobi|"
  	    			+ "mobily|moe|money|mortgage|motorcycles|mov|movie|museum|name|navy|network|new|news|ngo|ninja|now|off|one|ong|onl|online|ooo|open"
  	    			+ "|organic|origins|page|partners|parts|party|pay|pet|pharmacy|photo|photography|photos|physio|pics|pictures|pid|pin|pink|pizza|place|"
  	    			+ "plumbing|plus|poker|porn|post|press|prime|pro|productions|prof|promo|properties|property|protection|qpon|racing|radio|read|realestate|"
  	    			+ "realty|recipes|red|rehab|ren|rent|rentals|repair|report|republican|rest|review|reviews|rich|rip|rocks|rodeo|room|rsvp|run|safe|sale|"
  	    			+ "save|scholarships|school|science|secure|security|select|services|sex|sexy|shoes|shop|shopping|show|showtime|silk|singles|site|ski|"
  	    			+ "skin|sky|smile|soccer|social|software|solar|solutions|song|space|spot|spreadbetting|store|stream|studio|study|style|sucks|supplies|"
  	    			+ "supply|support|surf|surgery|systems|talk|tattoo|tax|taxi|team|tech|technology|tel|tennis|theater|theatre|tickets|tips|tires|today|"
  	    			+ "tools|top|tours|town|toys|trade|trading|training|travel|travelersinsurance|trust|tube|tunes|university|vacations|vet|video|villas|"
  	    			+ "vip|vision|vodka|vote|voting|voyage|wang|watch|watches|weather|webcam|website|wed|wedding|"
  	    			+ "whoswho|wiki|win|wine|winners|work|works|world|wow|wtf|xxx|xyz|yoga|you|zero|zone|com|org|net|int|edu|gov|mil|govt|nic|asia)[.]";
  	    	boolean flag = false;
  	    	Pattern r = Pattern.compile(regexForIdentifyingDomain);
  			Matcher m = r.matcher(str);
  			while (m.find()) {
  				flag = true;
  			}
  	    	
  	    	return flag;
  	    }
  	    
  	    /**
  		 * ATS MATCHER from document 
  		 */
  	    
  		public boolean isAtsUrl(String url)
  				throws IOException {
  			final String atsWordsTxt = "/home/mithunmanohar/Downloads/atsAndSpecialDomain.txt";
  			String pattern = "";
  			boolean flag = false;
  			String atsWords = "";
  			File file = new File(atsWordsTxt);
  			InputStreamReader inputStream = new InputStreamReader(new FileInputStream(file));
  			BufferedReader reader = new BufferedReader(inputStream);
  			
  			try{
  			while ((atsWords = reader.readLine()) != null) {
  				pattern = atsWords;
  				Pattern r = Pattern.compile(pattern);
  				Matcher m = r.matcher(url);
  				while (m.find()) {
  					flag = true;
  					break;
  				}
  				if(flag){
  					break;
  				}
  			}
  			}catch(Exception e){
  				LOGGER.warn(e);
  			}finally {
  				try {reader.close();}catch (Exception e) {LOGGER.warn(e);}
  				try {inputStream.close();}catch (Exception e) {LOGGER.warn(e);}
  			}
  			return flag;
  		}
  	
  		
}
