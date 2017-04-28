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
	
	public static int differenceInSeconds(long oldTime){
		long diff = System.nanoTime() - oldTime;
		long inSeconds = diff / 1000000000L;
		int seconds = (int) inSeconds;
		return seconds;
	}
	
}
