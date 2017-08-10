package org.hirrr.companynamefinder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.hirrr.companyfinder.utitlities.MagicNumbers;


/**
 * Check for Finding ATS Url & correcting the url to our desired format
 * @author mithunmanohar
 *
 */

public class AtsUrlChecker {
	
	
	public AtsUrlChecker(){
		// Do Nothing
	}
	
	private static final Logger LOGGER = Logger.getLogger(AtsUrlChecker.class);
	private static final String APPLY2JOBSATS = "apply2jobs";
	private static final String PEOPLECLICKATS = "peopleclick";
	private static final String NAUKRICAREERCHECKSTRING  = "careersitemanager.com";
	private static final String CHECKTITLETAG = "title";
	private static final String SITEERRORSTRING  = "site error";
	private static final String POSITION = "Positions";
	private static final String CAREERCHECKSTRING1 = "careers";
	private static final String CAREERCHECKSTRING = "careers/";
	private String atsFinalUrl;
	
	
	/**
	 * Check for finding out if it is a ATS or not
	 * Also converting the url to our desired format if it is possible
	 * @param urlToCheckForAts
	 * @param getCorrectAngelAtsUrl 
	 * @return
	 */
	
	public String getConfirmedAtsUrl(String urlToCheckForAts){
			final String icimsAts = "icims";
			final String hirebridgeAts = "hirebridge";
			final String silkroadAts = "silkroad";
			String urlToCheckForAtsCopy;
			urlToCheckForAtsCopy = urlToCheckForAts;
		
			
						atsFinalUrl = urlToCheckForAtsCopy;
						
						atsFinalUrl = getCorrectUrlForCommonCodePattern(urlToCheckForAtsCopy);
			     		
						if(atsFinalUrl.isEmpty()){
							if(urlToCheckForAtsCopy.contains(icimsAts) ){
								atsFinalUrl = AtsUrlMaker.getCorrectIcimsAtsUrl(urlToCheckForAtsCopy);
							}else if(urlToCheckForAtsCopy.contains(APPLY2JOBSATS) ){
								atsFinalUrl = AtsUrlMaker.getCorrectApply2jobsAtsUrl(urlToCheckForAtsCopy);
							}else if(urlToCheckForAtsCopy.contains(hirebridgeAts) ){
								atsFinalUrl = AtsUrlMaker.getCorrectHirebridgeAtsUrl(urlToCheckForAtsCopy);
							}else if(urlToCheckForAtsCopy.contains("jobsearch.ftl") ){
								atsFinalUrl = AtsUrlMaker.getCorrectTaleoAtsUrl(urlToCheckForAtsCopy);
							}else if(urlToCheckForAtsCopy.contains(PEOPLECLICKATS) ){
								atsFinalUrl = AtsUrlMaker.getCorrectPeopleclickAtsUrl(urlToCheckForAtsCopy);
							}else if(urlToCheckForAtsCopy.contains(silkroadAts)) {
								atsFinalUrl = AtsUrlMaker.getCorrectSilkRoadAtsUrl(urlToCheckForAtsCopy);
							}else if(urlToCheckForAtsCopy.contains("companies.naukri.com")) {
								atsFinalUrl = AtsUrlMaker.getCorrectNaukriAtsUrl(urlToCheckForAtsCopy);
							}else if(urlToCheckForAtsCopy.contains(NAUKRICAREERCHECKSTRING)) {
								atsFinalUrl = AtsUrlMaker.getCorrectNaukriFirstFormatAtsUrl(urlToCheckForAtsCopy);
							}
						}
						
				return atsFinalUrl;
	}
	
	/**
	 * Get formatted url for commonde pattern sites 
	 * @param urlToCheckForAtsCopy
	 * @return
	 */
	
	private String getCorrectUrlForCommonCodePattern(String urlToCheckForAtsCopy){
		final String angelAts = "angel.co";
		final String fastcollabAts = "fastcollab";
		final String applicantTrackingSystemAts = "applicanttrackingsystem";
		final String jobscoreAts = "jobscore";
		final String leverAts = "jobs.lever";
		final String smartRecruitersAts = "smartrecruiters";
		final String recruiterboxAts = "recruiterbox";
		final String resumatorAts = "theresumator";
		final String workableAts = "workable";
		
		if(urlToCheckForAtsCopy.contains(angelAts) ){
			atsFinalUrl = AtsUrlMaker.getCorrectAngelAtsUrl(urlToCheckForAtsCopy);
		}else if(urlToCheckForAtsCopy.contains(fastcollabAts) ){
			atsFinalUrl = AtsUrlMaker.getCorrectFastcollabAtsUrl(urlToCheckForAtsCopy);
		}else if(urlToCheckForAtsCopy.contains(applicantTrackingSystemAts) ){
			atsFinalUrl = AtsUrlMaker.getCorrectFastcollabAtsUrl(urlToCheckForAtsCopy);
		}else if(urlToCheckForAtsCopy.contains(jobscoreAts) ){
			atsFinalUrl = AtsUrlMaker.getCorrectJobscoreAtsUrl(urlToCheckForAtsCopy);
		}else if(urlToCheckForAtsCopy.contains(leverAts) ){
			atsFinalUrl = AtsUrlMaker.getCorrectLeverAtsUrl(urlToCheckForAtsCopy);
		}else if(urlToCheckForAtsCopy.contains(smartRecruitersAts) ){
			atsFinalUrl = AtsUrlMaker.getCorrectSmartRecruitersAtsUrl(urlToCheckForAtsCopy);
		}else if(urlToCheckForAtsCopy.contains(recruiterboxAts) ){
			atsFinalUrl = AtsUrlMaker.getCorrectRecruiterboxAtsUrl(urlToCheckForAtsCopy);
		}else if(urlToCheckForAtsCopy.contains(resumatorAts) ){
			atsFinalUrl = AtsUrlMaker.getCorrectResumatorAtsUrl(urlToCheckForAtsCopy);
		}else if(urlToCheckForAtsCopy.contains(workableAts) ){
			atsFinalUrl = AtsUrlMaker.getCorrectWorkableAtsUrl(urlToCheckForAtsCopy);
		}else{
			atsFinalUrl = "";
		}
		
		return atsFinalUrl;
	}
	
	/**
	 * Identification of company names for FastCollab ATS
	 * @param webpageDocument
	 * @param careerUrl
	 * @return
	 **/
	
	public static String getFastcollabAtsCompanyName(Document webpageDocument, String careerUrl){
		
		String jobElement ;
		
		try{
			jobElement = webpageDocument.select("#lifeatdiv").select("#divLifeatContainer").select("h3").first().text();
			jobElement = jobElement.replace("Life at", "").trim();
		}catch(Exception e){
			LOGGER.warn(e);
			webpageDocument.select("#contacts").select("#lblAddress").remove();
			jobElement = webpageDocument.select("#contacts").text().trim();
			if(jobElement.contains("©")){
				jobElement = jobElement.substring(jobElement.indexOf('©')+MagicNumbers.ONE,jobElement.indexOf('.')).trim();
			}
		}
		
		
		if(jobElement==null||(jobElement.isEmpty())){
			try{
				jobElement = careerUrl.substring(careerUrl.indexOf("Careers/")+MagicNumbers.EIGHT,careerUrl.length()).trim();
				if(jobElement.contains("/")){
					jobElement = jobElement.substring(0,jobElement.indexOf("/")).trim();
				}
				
			}catch(Exception e){
				LOGGER.warn(e);
			}
		}
		
		return jobElement;
	}
	
	/**
	 * Identification of company names for Naukri ATS
	 * @param webpageDocument
	 * @param careerUrl
	 * @return
	 **/
	
	public static String getNaukriAtsCompanyName(Document webpageDocument){
		
		String jobElement ;
		try{
			jobElement = webpageDocument.select(CHECKTITLETAG).first().text();
		if(jobElement.contains("|")){
			jobElement = jobElement.substring(jobElement.lastIndexOf("|")
					+MagicNumbers.ONE,jobElement.length()).trim();
		}else if(jobElement.contains("Openings in")){
			jobElement = jobElement.substring(jobElement.lastIndexOf("Openings in")
					+MagicNumbers.ELEVEN,jobElement.length()).trim();
		}else if(jobElement.contains("openings in")){
			jobElement = jobElement.substring(jobElement.lastIndexOf("openings in")
					+MagicNumbers.ELEVEN,jobElement.length()).trim();
		}else if(jobElement.contains("Vacancies in")){
			jobElement = jobElement.substring(jobElement.lastIndexOf("Vacancies in")
					+MagicNumbers.TWELVE,jobElement.length()).trim();
		}else if(jobElement.contains("Problem loading page")||
				(jobElement.contains(MagicNumbers.ERROR_STATUS+""))){
			jobElement = SITEERRORSTRING;
		}else{
			jobElement = "";
		}
		}catch(Exception e){
			jobElement = null ;
			LOGGER.warn(e);
		}
		
		return jobElement;
	}
	
	/**
	 * Identification of company names for SmartRecruiters ATS
	 * @param webpageDocument
	 * @param careerUrl
	 * @return
	 **/
	
	public static String getSmartRecruitersAtsCompanyName(Document webpageDocument, String careerUrl){
		
		String jobElement = null ;
		try{
			if(webpageDocument.select("head").select(CHECKTITLETAG).size()!=0){
				jobElement = webpageDocument.select("head").select(CHECKTITLETAG).text();
				if(jobElement.contains("at")){
					jobElement = jobElement.substring(jobElement.lastIndexOf("at")+MagicNumbers.TWO,jobElement.length()).trim();
				}else if(jobElement.contains("SmartRecruiters")){
					jobElement = webpageDocument.select(".jobs-header").
							select(".jobs-qty").get(1).select(".ng-binding").first().text().trim();
				}
			}
			}catch(Exception e){
				jobElement = null;
				LOGGER.warn(e);
			}
			if(jobElement==null && careerUrl.contains("company=")){
					String[] ar = careerUrl.split("=");
					jobElement = ar[1];
			}
			
		return jobElement;
	}
	
	/**
	 * Identification of company names for Angel ATS
	 * @param webpageDocument
	 * @param careerUrl
	 * @return
	 **/
	
	public static String getAngelAtsCompanyName(Document webpageDocument, String careerUrl){
		
		String jobElement ;
		try{
			jobElement = webpageDocument.select(".company-summary").select("h1").first().text().trim();
		if(jobElement.contains("Work at")){
			jobElement = jobElement.replace("Work at", "").trim();
		}else if(jobElement.contains("Jobs - AngelList")){
			jobElement = jobElement.replace("Jobs - AngelList", "").trim();
		}
		}catch(Exception e){
			jobElement = null ;
			LOGGER.warn(e);
		}
	
		if(jobElement==null||jobElement.isEmpty()){
			try{
			jobElement = careerUrl.substring(careerUrl.indexOf(".co/")+MagicNumbers.FOUR,careerUrl.indexOf("/jobs")).trim();
			}catch(Exception e){
			LOGGER.warn(e);
			}
			}
		
		return jobElement;
	}
	
	/**
	 * Identification of company names for Recruiterbox ATS
	 * @param webpageDocument
	 * @param careerUrl
	 * @return
	 **/
	
	public static String getRecruiterboxAtsCompanyName(Document webpageDocument, String careerUrl){
		
		String jobElement ;
		try{
			
			webpageDocument.select(".container").select(".page_head").select("h1").
			select(".lightweight").remove();
			
			jobElement = webpageDocument.select(".container").select(".page_head").
					select("h1").first().text().trim();
		}catch(Exception e){
			jobElement = null ;
			LOGGER.warn(e);
		}
	
		if(jobElement==null||jobElement.isEmpty()){
			try{
				jobElement = careerUrl.substring(careerUrl.indexOf("://")
						+MagicNumbers.THREE,careerUrl.indexOf(".recruiterbox")).trim();
			}catch(Exception e){
				LOGGER.warn(e);
			}
		}
		return jobElement;
	}
	
	/**
	 * Identification of company names for Workable ATS
	 * @param webpageDocument
	 * @param careerUrl
	 * @return
	 **/
	
	public static String getWorkableAtsCompanyName(Document webpageDocument){
		
		String jobElement ;
		try{
			jobElement = webpageDocument.select("#company").select(".section__header").first().text().trim();
			if(jobElement.contains("About")){
				jobElement = jobElement.replace("About", "").trim();
			}
			}catch(Exception e){
				jobElement = null ;
				LOGGER.warn(e);
			}
			try{
			if(jobElement==null){
				jobElement = webpageDocument.select("#footer").select("a").first().text().replace("website", "");
			}
			}catch(Exception e){
				LOGGER.warn(e);
			}
		return jobElement;
	}
	
	/**
	 * Identification of company names for Hirebridge ATS
	 * @param webpageDocument
	 * @param careerUrl
	 * @return
	 **/
	
	public static String getHirebridgeAtsCompanyName(Document webpageDocument){
		
		String jobElement ;
		try{
			jobElement = webpageDocument.select(CHECKTITLETAG).first().text();
			if(jobElement.contains(POSITION)){
				jobElement = jobElement.replace(POSITION, "").trim();
			}
		}catch(Exception e){
				jobElement = null ;
				LOGGER.warn(e);
		}
			
		return jobElement;
	}

	/**
	 * Identification of company names for Lever ATS
	 * @param webpageDocument
	 * @param careerUrl
	 * @return
	 **/
	
	public static String getLeverAtsCompanyName(Document webpageDocument){
		
		String jobElement ;
		try{
			jobElement = webpageDocument.select(CHECKTITLETAG).first().text();
			if(jobElement.contains(POSITION)){
				jobElement = jobElement.replace(POSITION, "").trim();
			}else if(jobElement.contains(MagicNumbers.ERROR_STATUS+"")){
				jobElement = SITEERRORSTRING;
			}
		}catch(Exception e){
				jobElement = null ;
				LOGGER.warn(e);
		}
			
		return jobElement;
	}
	
	/**
	 * Identification of company names for Jobscore ATS
	 * @param webpageDocument
	 * @param careerUrl
	 * @return
	 **/
	
	public static String getJobscoreAtsCompanyName(Document webpageDocument){
		
		String jobElement ;
		try{
			jobElement = webpageDocument.select(CHECKTITLETAG).first().text();
			if(jobElement.contains("Jobs")){
				jobElement = jobElement.substring(0,jobElement.indexOf("Jobs")).trim();
			}else if(jobElement.contains("Not Found (Error)")){
				jobElement = SITEERRORSTRING;
			}
		}catch(Exception e){
				jobElement = null ;
				LOGGER.warn(e);
		}
			
		return jobElement;
	}
	
	/**
	 * Identification of company names for Greenhouse ATS
	 * @param webpageDocument
	 * @param careerUrl
	 * @return
	 **/
	
	public static String getGreenhouseAtsCompanyName(Document webpageDocument){
		
		String jobElement ;
		try{
			jobElement = webpageDocument.select("#board_title").text();
			if(jobElement.contains("Openings at")){
				jobElement = jobElement.substring(jobElement.lastIndexOf("Openings at")+MagicNumbers.ELEVEN).trim();
			}
			}catch(Exception e){
				jobElement = null ;
			LOGGER.warn(e);
			}
			
		return jobElement;
	}
	
	/**
	 * Identification of company names for Resumator ATS
	 * @param webpageDocument
	 * @param careerUrl
	 * @return
	 **/
	
	public static String getResumatorAtsCompanyName(Document webpageDocument){
		
		String jobElement ;
		try{
			jobElement = webpageDocument.select(CHECKTITLETAG).first().text();
			if(jobElement.contains("Job Board")){
				jobElement = jobElement.replace("Job Board", "").replace("-", "").trim();
			}
			}catch(Exception e){
				jobElement = null ;
			LOGGER.warn(e);
		}
		
		return jobElement;
	}
	
	/**
	 * Identification of company names for Silkroad ATS
	 * @param webpageDocument
	 * @param careerUrl
	 * @return
	 **/
	
	public static String getSilkroadAtsCompanyName(Document webpageDocument, String careerUrl){
		
		String jobElement ;
		try{
			jobElement = webpageDocument.select(CHECKTITLETAG).first().text();
			if(jobElement.contains("|")){
				jobElement = jobElement.substring(0,jobElement.indexOf("|")).trim();
			}else if(jobElement.contains(CAREERCHECKSTRING1)){
				jobElement = jobElement.substring(0,jobElement.indexOf("Careers")).trim();
			}else if(jobElement.contains("Career Opportunities")){
				jobElement = careerUrl.substring(careerUrl.indexOf("://")+MagicNumbers.THREE,careerUrl.indexOf(".silkroad")).trim();
			}
		}catch(Exception e){
			jobElement = null ;
		LOGGER.warn(e);
		}
		if(jobElement==null){
			try{
			jobElement = careerUrl.substring(careerUrl.indexOf("://")+MagicNumbers.THREE,careerUrl.indexOf(".silkroad")).trim();
			if(jobElement.contains("-")){
				String ar[] = jobElement.split("-");
				jobElement = ar[0];
			}
			}catch(Exception e){
			LOGGER.warn(e);
			}
		}
		
		return jobElement;
	}
	
	/**
	 * Identification of company names for Apply2jobs ATS
	 * @param webpageDocument
	 * @param careerUrl
	 * @return
	 **/
	
	public static String getApply2jobsAtsCompanyName(String careerUrl){
		
		String jobElement = null ;
		try{
			if(careerUrl.contains("www.")&&(careerUrl.contains(APPLY2JOBSATS))){
				jobElement = careerUrl.substring(careerUrl.indexOf("www.")+
						MagicNumbers.FOUR,careerUrl.indexOf(".apply2jobs")).trim();
			}else if(careerUrl.contains("www1")&&(careerUrl.contains(APPLY2JOBSATS))){
				jobElement = careerUrl.substring(careerUrl.indexOf(".com/")+
						MagicNumbers.FIVE,careerUrl.indexOf("/ProfExt")).trim();
			}else if(careerUrl.contains("www5")&&(careerUrl.contains(APPLY2JOBSATS))){
				jobElement = careerUrl.substring(careerUrl.indexOf(".com/")+
						MagicNumbers.FIVE,careerUrl.indexOf("/ProfExt")).trim();
			}
			}catch(Exception e){
			LOGGER.warn(e);
			}
			
		
		return jobElement;
	}
	
	/**
	 * Identification of company names for ICIMS ATS
	 * @param webpageDocument
	 * @param careerUrl
	 * @return
	 **/
	
	public static String getIcimsAtsCompanyName(String careerUrl){
		
		String jobElement = null ;
		String mRslt = "";
		Pattern p = Pattern
				.compile("https:\\/\\/[^-]*-([^\\.]+)\\.icims[^\\n]+");
		Matcher m = null;
		m = p.matcher(careerUrl);
		while (m.find()) {
			mRslt  = m.group();
		}
		mRslt = mRslt.replaceAll("https:\\/\\/[^-]*-([^\\.]+)\\.icims[^\\n]+", "$1");
		jobElement = mRslt ;
			
		
		return jobElement;
	}
	
	/**
	 * Identification of company names for Peopleclick ATS
	 * @param webpageDocument
	 * @param careerUrl
	 * @return
	 **/
	
	public static String getPeopleclickAtsCompanyName(String careerUrl){
		
		String jobElement = null ;
		try{
			String tempURL = "";
			if(careerUrl.contains("client")){
				tempURL = careerUrl.substring(careerUrl.indexOf("client_")+MagicNumbers.SEVEN).trim();
			}else if(careerUrl.contains("Client")){
				tempURL = careerUrl.substring(careerUrl.indexOf("Client_")+MagicNumbers.SEVEN).trim();
			}
			
			if(tempURL.contains("/")){
				jobElement = tempURL.substring(0,tempURL.indexOf("/")).trim();
			}
			
		}catch(Exception e){
			LOGGER.warn(e);
		}
			
		return jobElement;
	}
	/**
	 * Returns jobElement of jobvite site
	 * @param webpageDocument
	 * @return
	 */
	
	private static String getJobViteUrlFromLogoTag(Document webpageDocument){
		String jobElement = null ;
		Elements img = webpageDocument.select("img");
		for(Element im : img){
			if(im.attr("src").contains("logo")){
				jobElement = im.select("img").attr("alt");
			}
		}
		return jobElement;
	}
	/**
	 * Identification of company names for Jobvite ATS
	 * @param webpageDocument
	 * @param careerUrl
	 * @return
	 **/
	
	public static String getJobViteAtsCompanyName(Document webpageDocument, String careerUrl){
		
		String jobElement ;
		
			if(careerUrl.contains(CAREERCHECKSTRING1)&&(careerUrl.contains("jobs?__jvst"))){
				jobElement = null ;
				try{
					String temp = careerUrl.substring(careerUrl.indexOf(CAREERCHECKSTRING)+MagicNumbers.EIGHT).trim();
					if(temp.contains("/")){
						jobElement = temp.substring(0,temp.indexOf("/")).trim();
					}
				}catch(Exception e){
					jobElement = null;
					LOGGER.warn(e);
				}
			}else{
				jobElement = null ;
				try{
					jobElement = getJobViteUrlFromLogoTag(webpageDocument);
				}catch(Exception e){
					jobElement = null ;
					LOGGER.warn(e);
				}
				
			}
	
			
		return jobElement;
	}
	
	/**
	 * Identification of company names for Taleo First Modal ATS
	 * @param webpageDocument
	 * @param careerUrl
	 * @return
	 **/
	
	public static String getTaleoFirstModelAtsCompanyName(String careerUrl){
		
		String jobElement ;
		try{
			jobElement = careerUrl.substring(careerUrl.indexOf("org=")+MagicNumbers.FOUR,careerUrl.length()).trim();
		}catch(Exception e){
			jobElement = null ;
		LOGGER.warn(e);
		}
			
		return jobElement;
	}
	
	/**
	 * Identification of company names for Taleo Second Modal ATS
	 * @param webpageDocument
	 * @param careerUrl
	 * @return
	 **/
	
	public static String getTaleoSecondModelAtsCompanyName(String careerUrl){
		
		String jobElement = null ;
		
			try{
				jobElement = careerUrl.substring(careerUrl.indexOf("://")+MagicNumbers.THREE,careerUrl.indexOf(".taleo.")).trim();
			}catch(Exception e){
				jobElement = null ;
			LOGGER.warn(e);
			}
			if(jobElement==null){
				jobElement = careerUrl.substring(careerUrl.indexOf("taleo.")+MagicNumbers.SIX,careerUrl.indexOf(".com")).trim();
			}
			
			
		return jobElement;
	}
	
	/**
	 * Identification of company names for Taleo Third Modal ATS
	 * @param webpageDocument
	 * @param careerUrl
	 * @return
	 **/
	
	public static String getTaleoThirdModelAtsCompanyName(String careerUrl){
		
		String jobElement = null ;
			try{
				jobElement = careerUrl.substring(careerUrl.indexOf("://")+MagicNumbers.THREE,careerUrl.indexOf(".taleo.")).trim();
			}catch(Exception e){
				jobElement = null ;
			LOGGER.warn(e);
			}
			if(jobElement==null){
				jobElement = careerUrl.substring(careerUrl.indexOf("taleo.")+MagicNumbers.SIX,careerUrl.indexOf(".com")).trim();
			}
			
			
		return jobElement;
	}
	
	/**
	 * Identification of company names for Brassring ATS
	 * @param webpageDocument
	 * @param careerUrl
	 * @return
	 **/
	
	public static String getBrassringAtsCompanyName(Document webpageDocument){
		
		String jobElement ;
		try{
			jobElement = webpageDocument.select("meta[content=search openings page]").first().attr("name");
		}catch(Exception e){
			jobElement = null ;
		LOGGER.warn(e);
		}
		try{
		if(jobElement == null){
			jobElement = webpageDocument.select(CHECKTITLETAG).first().text().trim();
		}
		}catch(Exception e){
		LOGGER.warn(e);
		}
		
		if(jobElement!=null && jobElement.contains("Cookies disabled")){
			jobElement = MagicNumbers.ERROR_STATUS+"";
		}
			
		return jobElement;
	}
	
	/**
	 * Identification of company names for Zoho ATS
	 * @param webpageDocument
	 * @param careerUrl
	 * @return
	 **/
	
	public static String getZohoAtsCompanyName(Document webpageDocument){
		
		String jobElement = null;
		try {
			jobElement = webpageDocument.select("meta[property=og:site_name]")
					.first().attr("content");
		} catch (Exception e) {
			LOGGER.warn(e);
		}

		return jobElement;
	}
	
	
}
