package com.boe.adc.image;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="backup")
public class BackUpConfig
{
  public static final String TIMEUNIT_MONTH = "M";
  public static final String TIMEUNIT_DATE = "D";
  public static final String TIMEUNIT_HOUR = "H";
  private String dataDir;
  private String backUpDir;
  private String mainCode;
  private String subCode;
  private String backUpFolderFormat; 
  /*  2019.07.10 增加 Incrementalbackup|OneBackUp 备份策略 */
  private IncrementalBackupConfig incrementalBackupConfig;
  private OneBackupConfig oneBackupConfig;
  private List<String> threadDirs;
  
  @XmlElement(name="datadir")
  public String getDataDir()
  {
    return this.dataDir;
  }
  
  public void setDataDir(String dataDir)
  {
    this.dataDir = dataDir;
  }
  
  @XmlElement(name="backupdir")
  public String getBackUpDir()
  {
    return this.backUpDir;
  }
  
  public void setBackUpDir(String backUpDir)
  {
    this.backUpDir = backUpDir;
  }
  
  @XmlElement(name="maincode")
  public String getMainCode()
  {
    return this.mainCode;
  }
  
  public void setMainCode(String mainCode)
  {
    this.mainCode = mainCode;
  }
  
  @XmlElement(name="subcode")
  public String getSubCode()
  {
    return this.subCode;
  }
  
  public void setSubCode(String subCode)
  {
    this.subCode = subCode;
  }
  
  @XmlElement(name="backupfolderformat")
  public String getBackUpFolderFormat()
  {
    return this.backUpFolderFormat;
  }
  
  public void setBackUpFolderFormat(String backUpFolderFormat)
  {
    this.backUpFolderFormat = backUpFolderFormat;
  }
  
  @XmlElement(name = "Incrementalbackup")
	public IncrementalBackupConfig getIncrementalBackupConfig() {
		return this.incrementalBackupConfig;
	}

	public void setIncrementalBackupConfig(IncrementalBackupConfig incrementalBackupConfig) {
		this.incrementalBackupConfig = incrementalBackupConfig;
	}
	
	@XmlElement(name = "OneBackup")
	public OneBackupConfig getOneBackupConfig() {
		return this.oneBackupConfig;
	}

	public void setOneBackupConfig(OneBackupConfig oneBackupConfig) {
		this.oneBackupConfig = oneBackupConfig;
	}
  
  @XmlElement(name="threadir")
  public List<String> getThreadDirs()
  {
    return this.threadDirs;
  }
  
  public void setThreadDirs(List<String> threadDirs)
  {
    this.threadDirs = threadDirs;
  }
}
