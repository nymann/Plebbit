package com.plebbit.helpers;

public class TimeTools {

	public static long EXPIRED_TIME_NANO = 1800000000000L; 
	
	public static boolean isExpired(String time){
		if(time.isEmpty() || time.equals("")){
			return true;
		}
		long curTime = System.nanoTime();
		long oldTime = Long.parseLong(time);
		if((oldTime + EXPIRED_TIME_NANO) < curTime){
			return true;
		}
		return false;
		
	}
	
}
