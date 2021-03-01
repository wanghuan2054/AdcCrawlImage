package com.boe.adc.image;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="OneBackup")
public class OneBackupConfig
{
  private long actionTime;
  private String time;
  private String timeUnit;
  
  @XmlElement(name="time")
  public String getTime()
  {
    return this.time;
  }
  
  public void setTime(String time)
  {
    this.time = time;
  }
  
  @XmlElement(name="timeunit")
  public String getTimeUnit()
  {
    return this.timeUnit;
  }
  
  public void setTimeUnit(String timeUnit)
  {
    this.timeUnit = timeUnit;
  }
  
  public long getActionTime()
  {
    return this.actionTime;
  }
  
  public void setActionTime(long actionTime)
  {
    this.actionTime = actionTime;
  }
}
