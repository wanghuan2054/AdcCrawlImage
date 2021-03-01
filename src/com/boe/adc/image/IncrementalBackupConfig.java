package com.boe.adc.image;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Incrementalbackup")
public class IncrementalBackupConfig {
	// public static final String end_IGNORE = "IGNORE";
	private String begin;
	private String end;
//	private String interval;
//	private String timeUnit;
	private String enabled;

	@XmlElement(name = "begin")
	public String getBegin() {
		return this.begin.trim();
	}

	public void setBegin(String begin) {
		this.begin = begin;
	}

	@XmlElement(name = "end")
	public String getEnd() {
		return this.end.trim();
	}

	public void setEnd(String end) {
		this.end = end;
	}

//	@XmlElement(name = "interval")
//	public String getInterval() {
//		return this.interval;
//	}
//
//	public void setInterval(String interval) {
//		this.interval = interval;
//	}
//
//	@XmlElement(name = "timeunit")
//	public String getTimeUnit() {
//		return this.timeUnit;
//	}
//
//	public void setTimeUnit(String timeUnit) {
//		this.timeUnit = timeUnit;
//	}

	@XmlElement(name = "enabled")
	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}
	
	public boolean isEnabled(){
		if(enabled.toLowerCase().equals("true")){
			return true ;
		}
		return false ;
	}

}
