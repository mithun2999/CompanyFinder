package com.hirrr.companyfinder.utitlities;

import java.security.SecureRandom;

/**
 * Random userAgent & random time generation
 * @author mithunmanohar
 *
 */

public class RandomValueGeneratorUtil{
	
	 private RandomValueGeneratorUtil() {
	    throw new IllegalStateException("Random Utility class");
	 }
	
	/**
	 * Random Search Engine Fetch
	 * @return
	 */
	
	public static String randomsearchEngineFetch(){
		String searchAgent;
		String[] searchAgentArray = 
			{
				"duckduckgo",
				"bing",
				"google"
				
			};
		
		int end = searchAgentArray.length;
		int start = 0;
		SecureRandom r = new SecureRandom();
		int val = r.nextInt(end-start) + start;
		searchAgent = searchAgentArray[val];
		return searchAgent;
		
	}
	
	/**
	 * Random Time Generator
	 * @return
	 */
	
	public static long randomTimeGeneration(){
		long time ;
		final Integer randomValue1 = 	2507;
		final Integer randomValue2 =	3529;
		final Integer randomValue3 =	4532;
		final Integer randomValue4 =	5555;
		final Integer randomValue5 =	6563;
		final Integer randomValue6 =	7579;
		final Integer randomValue7 =	2547;
		final Integer randomValue8 =	4539;
		final Integer randomValue9 =	6550;
		final Integer randomValue10 =	4542;
		final Integer randomValue11 =	3503;
		final Integer randomValue12 =	2583;
		final Integer randomValue13 =	1587;
		final Integer randomValue14 =	5590;
		final Integer randomValue15 =	3593;
		final Integer randomValue16 =	7608;
		final Integer randomValue17 =	3611;
		final Integer randomValue18 =	5642;
		final Integer randomValue19 =	2624;
		final Integer randomValue20 =	3635;
		final Integer randomValue21 =	4650;
		final Integer randomValue22 =	2667;
		final Integer randomValue23 =	3672;
		final Integer randomValue24 =	3679;
		final Integer randomValue25 =	2685;
		final Integer randomValue26 =	6696;
		final Integer randomValue27 =	3700;
		final Integer randomValue28 =	4718;
		final Integer randomValue29 =	5725;
		final Integer randomValue30 =	3738;
		final Integer randomValue31 =	2744;
		final Integer randomValue32 =	4757;
		final Integer randomValue33 =	5769;
		final Integer randomValue34 =	2790;
		final Integer randomValue35 =	5802;
		final Integer randomValue36 =	6823;
		final Integer randomValue37 =	4849;
		final Integer randomValue38 =	2837;
		final Integer randomValue39 =	3857;
		final Integer randomValue40 =	3897;
		final Integer randomValue41 =	5918;
		final Integer randomValue42 =	3990;
		final Integer randomValue43 =	4987;
		final Integer randomValue44 =	2910;
		final Integer randomValue45 =	2120;
		final Integer randomValue46 =	4235;
		final Integer randomValue47 =	2345;
		final Integer randomValue48 =	3009;
		final Integer randomValue49 =	2278;
		final Integer randomValue50 =	4450;
		final Integer randomValue51 =	4395;
		final Integer randomValue52 =	2333;
		final Integer randomValue53 =	2579;
		final Integer randomValue54 =	3190;
		final Integer randomValue55 =	2230;
		final Integer randomValue56 =	2145;
		final Integer randomValue57 =	4180;
		final Integer randomValue58 =	2279;
		final Integer randomValue59 =	4239;
		final Integer randomValue60 =	3537;
		final Integer randomValue61 =	2778;
		final Integer randomValue62 =	4998;
		final Integer randomValue63 =	3887;
		final Integer randomValue64 =	5460;
		final Integer randomValue65 =	2678;
		final Integer randomValue66 =	1994;
		final Integer randomValue67 =	2200;
		final Integer randomValue68 =	2130;
		final Integer randomValue69 =	2245;
		final Integer randomValue70 =	2350;
		final Integer randomValue71 =	2410;
		final Integer randomValue72 =	2499;

		
		long[] timeArray = 
			{	
				randomValue1,
				randomValue2,
				randomValue3,
				randomValue4,
				randomValue5,
				randomValue6,
				randomValue7,
				randomValue8,
				randomValue9,
				randomValue10,
				randomValue11,
				randomValue12,
				randomValue13,
				randomValue14,
				randomValue15,
				randomValue16,
				randomValue17,
				randomValue18,
				randomValue19,
				randomValue20,
				randomValue21,
				randomValue22,
				randomValue23,
				randomValue24,
				randomValue25,
				randomValue26,
				randomValue27,
				randomValue28,
				randomValue29,
				randomValue30,
				randomValue31,
				randomValue32,
				randomValue33,
				randomValue34,
				randomValue35,
				randomValue36,
				randomValue37,
				randomValue38,
				randomValue39,
				randomValue40,
				randomValue41,
				randomValue42,
				randomValue43,
				randomValue44,
				randomValue45,
				randomValue46,
				randomValue47,
				randomValue48,
				randomValue49,
				randomValue50,
				randomValue51,
				randomValue52,
				randomValue53,
				randomValue54,
				randomValue55,
				randomValue56,
				randomValue57,
				randomValue58,
				randomValue59,
				randomValue60,
				randomValue61,
				randomValue62,
				randomValue63,
				randomValue64,
				randomValue65,
				randomValue66,
				randomValue67,
				randomValue68,
				randomValue69,
				randomValue70,
				randomValue71,
				randomValue72
				
				};
		
		int end = timeArray.length;
		int start = 0;
		SecureRandom r = new SecureRandom();
		int val = r.nextInt(end-start) + start;
		time = timeArray[val];
		return time;
		
	}
}
