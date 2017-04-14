package com.plebbit.helpers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class WriteSomething {

	public static String location = "log.txt";
	
	public static void writeInFile(String path, String str){
		File file = new File(path);
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}
		try {
			String before = "[";
			String date = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
			before += date + "] ";
		   Files.write(Paths.get(path), (before+str+"\n").getBytes(), StandardOpenOption.APPEND);
		}catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}
	
}
