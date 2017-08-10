package org.hirrr.companynamefinder;

import org.apache.log4j.Logger;

import com.hirrr.companyfinder.utitlities.MagicNumbers;

/**
 * Reconstruct the url into a better format for efficiency
 * @author mithunmanohar
 *
 */
public class AtsUrlMaker {
	
	private AtsUrlMaker() {
	}
	
	private static final Logger LOGGER = Logger.getLogger(AtsUrlMaker.class);
	private static final String JOBCHECKSTRING = "/jobs";
	private static final String JOBCHECKSTRING1 = "/jobs/";
	private static final String CAREERCHECKSTRING = "careers/";
	private static final String CAREERCHECKSTRING1 = "careers";
	private static final String NAUKRICAREERCHECKSTRING  = "careersitemanager.com";
	private static volatile String temporaryUrl;
	private static final String PEOPLECLICKATS = "peopleclick";

	
	/**
	 * Tweak in ats angel url 
	 * @param urlToCheckForAtsCopy
	 * @return
	 */
	
	public static String getCorrectAngelAtsUrl(String urlToCheckForAtsCopy){
		

		try{
				if(urlToCheckForAtsCopy.contains(JOBCHECKSTRING)){
					if(urlToCheckForAtsCopy.endsWith(JOBCHECKSTRING) || urlToCheckForAtsCopy.endsWith(JOBCHECKSTRING1)){
						urlToCheckForAtsCopy = urlToCheckForAtsCopy.replace(JOBCHECKSTRING1, JOBCHECKSTRING);
					}else{
						urlToCheckForAtsCopy = urlToCheckForAtsCopy.
								substring(0, urlToCheckForAtsCopy.indexOf(JOBCHECKSTRING) + MagicNumbers.FIVE);
					}
				}else{
					if(urlToCheckForAtsCopy.endsWith("/")){
						urlToCheckForAtsCopy  = urlToCheckForAtsCopy + "jobs";
					}else{
						urlToCheckForAtsCopy  = urlToCheckForAtsCopy + JOBCHECKSTRING;
					}
				}
			
		}catch(Exception e){
			LOGGER.warn(e);
		}
		
		return urlToCheckForAtsCopy;
	}
	
	/**
	 * Tweak in ats fastcollab url 
	 * @param urlToCheckForAtsCopy
	 * @return
	 */
	
	public static String getCorrectFastcollabAtsUrl(String urlToCheckForAtsCopy){
		
		try{
			if(urlToCheckForAtsCopy.contains(CAREERCHECKSTRING)){
				temporaryUrl = urlToCheckForAtsCopy.substring(
						(urlToCheckForAtsCopy.indexOf(CAREERCHECKSTRING)+MagicNumbers.EIGHT),urlToCheckForAtsCopy.length());
				if(temporaryUrl.contains("/")){
					String appendURL = temporaryUrl.substring(0,temporaryUrl.indexOf("/")).trim();
					urlToCheckForAtsCopy = "http://www.fastcollab.com/careers/" + appendURL;
				}
			}
		}catch(Exception e){
			LOGGER.warn(e);
		}
		
		return urlToCheckForAtsCopy;
	}
	
	/**
	 * Tweak in ats applicantTrackingSystem url 
	 * @param urlToCheckForAtsCopy
	 * @return
	 */
	
	public static String getCorrectApplicantTrackingSystemAtsUrl(String urlToCheckForAtsCopy){
		
		try{
			if(urlToCheckForAtsCopy.contains(CAREERCHECKSTRING)){
				temporaryUrl = urlToCheckForAtsCopy.substring((urlToCheckForAtsCopy.
						indexOf(CAREERCHECKSTRING)+MagicNumbers.EIGHT),urlToCheckForAtsCopy.length());
				if(temporaryUrl.contains("/")){
					String appendURL = temporaryUrl.substring(0,temporaryUrl.indexOf("/")).trim();
					urlToCheckForAtsCopy = "http://www.applicanttrackingsystem.co/careers/" + appendURL;
				}
					
			}
		}catch(Exception e){
			LOGGER.warn(e);
		}
		
		return urlToCheckForAtsCopy;
	}
	
	/**
	 * Tweak in ats Jobscore url 
	 * @param urlToCheckForAtsCopy
	 * @return
	 */
	
	public static String getCorrectJobscoreAtsUrl(String urlToCheckForAtsCopy){
		
		try{
			if(urlToCheckForAtsCopy.contains(CAREERCHECKSTRING)){
				temporaryUrl = urlToCheckForAtsCopy.substring((urlToCheckForAtsCopy.
						indexOf(CAREERCHECKSTRING)+MagicNumbers.EIGHT),urlToCheckForAtsCopy.length());
				if(temporaryUrl.contains("/")){
					String appendURL = temporaryUrl.substring(0,temporaryUrl.indexOf("/")).trim();
					urlToCheckForAtsCopy = "https://careers.jobscore.com/careers/" + appendURL;
				}
					
				}
		}catch(Exception e){
		LOGGER.warn(e);
		}
		
		return urlToCheckForAtsCopy;
	}
	
	/**
	 * Tweak in ats Lever url 
	 * @param urlToCheckForAtsCopy
	 * @return
	 */
	
	public static String getCorrectLeverAtsUrl(String urlToCheckForAtsCopy){
		
		try{
			temporaryUrl = urlToCheckForAtsCopy.substring(urlToCheckForAtsCopy.indexOf("lever.co/")+MagicNumbers.NINE).trim();
			if(temporaryUrl.contains("/")){
			temporaryUrl = temporaryUrl.substring(0,temporaryUrl.indexOf("/")+1);
		}
			urlToCheckForAtsCopy = "https://jobs.lever.co/"+temporaryUrl;
			
			
		}catch(Exception e){
			LOGGER.warn(e);
		}
		
		if(urlToCheckForAtsCopy.contains("blog/")){
			urlToCheckForAtsCopy = "";							
		}
		
		return urlToCheckForAtsCopy;
	}
	
	/**
	 * Tweak in ats SmartRecruiters url 
	 * @param urlToCheckForAtsCopy
	 * @return
	 */
	
	public static String getCorrectSmartRecruitersAtsUrl(String urlToCheckForAtsCopy){
		
		try{
			temporaryUrl = urlToCheckForAtsCopy.substring
					(urlToCheckForAtsCopy.indexOf("smartrecruiters.com/")+MagicNumbers.TWENTY).trim();
			if(temporaryUrl.contains("/")){
				temporaryUrl = temporaryUrl.substring(0,temporaryUrl.indexOf("/")+1);
			}
			urlToCheckForAtsCopy = "https://jobs.smartrecruiters.com/"+"?company="+temporaryUrl;
			
			
		}catch(Exception e){
				LOGGER.warn(e);
		}
		
		return urlToCheckForAtsCopy;
	}
	
	
	/**
	 * Tweak in ats Recruiterbox url 
	 * @param urlToCheckForAtsCopy
	 * @return
	 */
	
	public static String getCorrectRecruiterboxAtsUrl(String urlToCheckForAtsCopy){
		
		try{
			temporaryUrl = urlToCheckForAtsCopy.substring
					(0,urlToCheckForAtsCopy.indexOf("recruiterbox.com/")+MagicNumbers.SIXTEEN).trim();
			urlToCheckForAtsCopy = temporaryUrl+"/";
			
		
		}catch(Exception e){
			LOGGER.warn(e);
		}
		if(urlToCheckForAtsCopy.contains("app.recruiterbox")){
			urlToCheckForAtsCopy = "";
		}
		
		return urlToCheckForAtsCopy;
	}
	
	/**
	 * Tweak in ats Resumator url 
	 * @param urlToCheckForAtsCopy
	 * @return
	 */
	
	public static String getCorrectResumatorAtsUrl(String urlToCheckForAtsCopy){
		
		try{
			temporaryUrl = urlToCheckForAtsCopy.substring
					(0,urlToCheckForAtsCopy.indexOf("theresumator.com")+MagicNumbers.SIXTEEN).trim();
			urlToCheckForAtsCopy = temporaryUrl+"/";
			
			
		}catch(Exception e){
				LOGGER.warn(e);
		}
		
		return urlToCheckForAtsCopy;
	}
	
	/**
	 * Tweak in ats Workable url 
	 * @param urlToCheckForAtsCopy
	 * @return
	 */
	
	public static String getCorrectWorkableAtsUrl(String urlToCheckForAtsCopy){
		
		if(urlToCheckForAtsCopy.startsWith("https://www.workable.com")){
			urlToCheckForAtsCopy = "";
		}else{
			try{
				temporaryUrl = urlToCheckForAtsCopy.substring
						(0,urlToCheckForAtsCopy.indexOf("workable.com")+MagicNumbers.TWELVE).trim();
				urlToCheckForAtsCopy = temporaryUrl+"/";
				
			
			}catch(Exception e){
					LOGGER.warn(e);
			}
		}
		
		return urlToCheckForAtsCopy;
	}
	
	/**
	 * Tweak in ats ICIMS url 
	 * @param urlToCheckForAtsCopy
	 * @return
	 */
	
	public static String getCorrectIcimsAtsUrl(String urlToCheckForAtsCopy){
		
		try{
			urlToCheckForAtsCopy = urlToCheckForAtsCopy.substring
					(0,urlToCheckForAtsCopy.indexOf(".com")+MagicNumbers.FOUR).trim()+JOBCHECKSTRING1;
			urlToCheckForAtsCopy = urlToCheckForAtsCopy + "search?ss=1&searchKeyword=";
			
		}catch(Exception e){
			LOGGER.warn(e);
		}
		
		return urlToCheckForAtsCopy;
	}
	
	/**
	 * Tweak in ats apply2jobs url 
	 * @param urlToCheckForAtsCopy
	 * @return
	 */
	
	public static String getCorrectApply2jobsAtsUrl(String urlToCheckForAtsCopy){
		
		try{
			if(urlToCheckForAtsCopy.contains("apply2jobs.com/CRS/ProfExt/")){
				urlToCheckForAtsCopy = urlToCheckForAtsCopy.substring
						(0,urlToCheckForAtsCopy.indexOf("ProfExt/")+MagicNumbers.EIGHT).trim();
				urlToCheckForAtsCopy = urlToCheckForAtsCopy +
						"/index.cfm?fuseaction=mExternal.showSearchInterface";
			}else{
				urlToCheckForAtsCopy = urlToCheckForAtsCopy.substring
						(0,urlToCheckForAtsCopy.indexOf("apply2jobs.com")+MagicNumbers.FOURTEEN).trim();
				urlToCheckForAtsCopy = urlToCheckForAtsCopy +
						"/index.cfm?fuseaction=mExternal.showSearchInterface";
			}
			
		}catch(Exception e){
			LOGGER.warn(e);
		}
		
		return urlToCheckForAtsCopy;
	}
	
	/**
	 * Tweak in ats hirebridge url 
	 * @param urlToCheckForAtsCopy
	 * @return
	 */
	
	public static String getCorrectHirebridgeAtsUrl(String urlToCheckForAtsCopy){
		
		if (urlToCheckForAtsCopy.contains("index.asp")) {
			try {
				urlToCheckForAtsCopy = urlToCheckForAtsCopy.replace(
						"index.asp", "Searchjobresults.asp").trim();

			} catch (Exception e) {

				LOGGER.warn(e);
			}
		}
		if (urlToCheckForAtsCopy.contains("/jobseeker2/Searchjobresults.asp")) {
			urlToCheckForAtsCopy = urlToCheckForAtsCopy.replace(
					"/jobseeker2/Searchjobresults.asp", "/v3/jobs/list.aspx")
					.trim();
		} else if (urlToCheckForAtsCopy.contains("/jobseeker2/joblist2.asp")) {
			urlToCheckForAtsCopy = urlToCheckForAtsCopy.replace(
					"/jobseeker2/joblist2.asp", "/v3/jobs/list.aspx").trim();
		}
		
		return urlToCheckForAtsCopy;
	}
	
	/**
	 * Tweak in ats taleo url 
	 * @param urlToCheckForAtsCopy
	 * @return
	 */
	
	public static String getCorrectTaleoAtsUrl(String urlToCheckForAtsCopy){
		
		try{
			urlToCheckForAtsCopy = urlToCheckForAtsCopy.substring
					(0,urlToCheckForAtsCopy.indexOf(".ftl")+MagicNumbers.FOUR).trim();
			urlToCheckForAtsCopy = urlToCheckForAtsCopy + "?lang=en";
			
		}catch(Exception e){
				LOGGER.warn(e);
		}
		
		return urlToCheckForAtsCopy;
	}
	
	/**
	 * Tweak in ats peopleclick url 
	 * @param urlToCheckForAtsCopy
	 * @return
	 */
	
	public static String getCorrectPeopleclickAtsUrl(String urlToCheckForAtsCopy){
		
		if(urlToCheckForAtsCopy.contains(PEOPLECLICKATS)&&(urlToCheckForAtsCopy.contains("search.do")) ){
		try{
			urlToCheckForAtsCopy = urlToCheckForAtsCopy.substring
					(0,urlToCheckForAtsCopy.indexOf("search.do")+MagicNumbers.NINE).trim();
			}catch(Exception e){
				LOGGER.warn(e);
			}
		}else if(urlToCheckForAtsCopy.contains(PEOPLECLICKATS)&&
				(urlToCheckForAtsCopy.contains("external_acis")) ){
			try{
			urlToCheckForAtsCopy = urlToCheckForAtsCopy.substring
					(0,urlToCheckForAtsCopy.indexOf("external_acis")+MagicNumbers.THIRTEEN).trim();
			urlToCheckForAtsCopy =  urlToCheckForAtsCopy + "/search.do";
			}catch(Exception e){
				LOGGER.warn(e);
			}
		}
		return urlToCheckForAtsCopy;
	}
	
	/**
	 * Tweak in ats silkroad url 
	 * @param urlToCheckForAtsCopy
	 * @return
	 */
	
	public static String getCorrectSilkRoadAtsUrl(String urlToCheckForAtsCopy){
		
		try{
			if(urlToCheckForAtsCopy.contains("silkroad.com")){
				urlToCheckForAtsCopy = urlToCheckForAtsCopy.substring
						(0,urlToCheckForAtsCopy.indexOf(".com")+MagicNumbers.FOUR).trim();
				urlToCheckForAtsCopy = urlToCheckForAtsCopy 
						+ "/epostings/index.cfm?fuseaction=app.jobsearch";
			}
			if(urlToCheckForAtsCopy.contains(CAREERCHECKSTRING1)){
				urlToCheckForAtsCopy = urlToCheckForAtsCopy.replace("careers", "-openhire").trim();
			}
			
		}catch(Exception e){
			LOGGER.warn(e);
		}
		
		return urlToCheckForAtsCopy;
	}
	
	/**
	 * Tweak in ats naukri url 
	 * @param urlToCheckForAtsCopy
	 * @return
	 */
	
	public static String getCorrectNaukriAtsUrl(String urlToCheckForAtsCopy){
		
		try{
			   String samp ;
			   if(urlToCheckForAtsCopy.contains("-jobs/")){
				   samp = urlToCheckForAtsCopy.substring(0,urlToCheckForAtsCopy.indexOf("-jobs/")+MagicNumbers.SIX).trim();
				   urlToCheckForAtsCopy = samp + "jobs/";
				   
			   }
		   }catch(Exception e){
			   LOGGER.warn(e);
		   }
		
		return urlToCheckForAtsCopy;
	}
	
	/**
	 * Tweak in ats naukri first format url 
	 * @param urlToCheckForAtsCopy
	 * @return
	 */
	
	public static String getCorrectNaukriFirstFormatAtsUrl(String urlToCheckForAtsCopy){
		
		try{
			   String samp ;
			   if(urlToCheckForAtsCopy.contains(NAUKRICAREERCHECKSTRING)){
				   samp = urlToCheckForAtsCopy.substring(0,
						   urlToCheckForAtsCopy.indexOf(NAUKRICAREERCHECKSTRING)+MagicNumbers.TWENTYONE).trim();
				   urlToCheckForAtsCopy = samp + "/jobs/";
			   }
		   }catch(Exception e){
			   LOGGER.warn(e);
		   }
		
		return urlToCheckForAtsCopy;
	}
}
