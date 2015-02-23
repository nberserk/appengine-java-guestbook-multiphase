package com.nberserk.gae.lessontable;

import java.util.logging.Logger;

public class Common {
	private static Logger sLogger = Logger.getLogger(Common.class.getName());
	
	public static void info(String s){
		sLogger.info(s);
	}
}
