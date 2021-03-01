package com.boe.adc.image;

public class ConstantDefinition {

	public static final String[] FILTERWORDS = {"map"} ;
	public static final String MONTH = "M";
	public static final String DAY_OF_YEAR = "D";
	public static final String HOUR_OF_DAY = "H";
	public static final String DEFAULT_BACKUPCONFIG = "/config/backupconfig.xml";
	public static final String DEFAULT_LOGCONFIG = "/config/log4j.xml";
	public static final String LEFT_SLASH = "/";
	public static final String RIGHT_SLASH = "\\";
	public static final String UNDERLINE = "_";
	public static final String ASTERISK = "*";
	public static final String EMPTY_STRING = "EMPTY";
	public static final String LBP_SHOP = "/bp/" ;
	public static final String DMY_SUFFIX = ".dmy" ;
	public static final int MAINCODE_POS = 5 ;
	public static final int SUBCODE_POS  = 4 ;
	public static final String ACTSTEP  = "l290j" ;
	public static final String GATESTEP  = "l490j" ;
	public static final String SDSTEP  = "l691j" ;
	
 	
	public static final String ZERO_CLOCK = "000000";
	public static final String LINE_SEPARATOR = "line.separator";

	public static final String DELEMETER_SPACE = String.format("%c",
			new Object[] { Integer.valueOf(32) });

	public static final int BUFFER_SIZE = 1024;

}
