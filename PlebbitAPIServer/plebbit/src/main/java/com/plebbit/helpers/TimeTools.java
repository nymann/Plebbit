package com.plebbit.helpers;

public class TimeTools {

	public static long EXPIRED_TIME_NANO = 1800000000000L; 
	
	public static boolean isExpired(String time){
		if(time.isEmpty() || time.equals("")){
			System.out.println("WTFFAMTHISISFUCKINGDASGAME");
			return true;
		}
		long curTime = System.nanoTime();
		long oldTime = Long.parseLong(time);
		if((oldTime + EXPIRED_TIME_NANO) < curTime){
			System.out.println("ITS FUCKING EXPIRED FAM");
			return true;
		}
		
		return false;
		
	}
	
}
