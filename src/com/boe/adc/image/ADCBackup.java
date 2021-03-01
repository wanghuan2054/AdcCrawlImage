package com.boe.adc.image;

import java.io.File;
import java.io.PrintStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

public class ADCBackup {
	private static final Logger logger = Logger.getLogger(ADCBackup.class);

	public static BackUpConfig backUpConfig;
	public static String backUpFloder;
	public static long backUpBeginTime;   // 备份区间开始时间
	public static long backUpEndTime;     // 备份区间结束时间
	public static long deleteFolderTime;

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		ADCBackup backup = new ADCBackup();
		try {
			backup.initLog();
			backup.initConfig();
		} catch (Exception e) {
			logger.info(e.getMessage());
			System.exit(-1);
		}
		List<String> threadDirs = backUpConfig.getThreadDirs();
		
		// /ADC/data/Data/Root/on/image/bp/ , Array Image Root Path 
		// backUpConfig.getDataDir()

		if ((threadDirs == null) || (threadDirs.size() == 0)) {
			Thread t = new BackUpProcessor(backUpConfig.getDataDir(),
					backUpFloder);
			t.run();
		} else { 
			ExecutorService service = java.util.concurrent.Executors
					.newFixedThreadPool(2);
			for (String threadDir : threadDirs) {
				Object t = new BackUpProcessor(backUpConfig.getDataDir(),
						threadDir, backUpFloder);
				service.execute((Runnable) t);
			}
			service.shutdown();
			try {
				service.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
			} catch (Exception e) {
				logger.error("ADC-BAK-000: [EXEC ERROR] termination thread error ."
						+ e.getMessage());
			}
		}
		long endTime = System.currentTimeMillis();
		System.out.println("Adc Image Crawling End , 耗时为  "+(endTime - startTime) + " ms");
		logger.info("All BackUp End.");
	}

	private void initConfig() throws Exception {
		BackUpConfig config ;
		try {
			URL url = getClass().getResource(ConstantDefinition.DEFAULT_BACKUPCONFIG);

			JAXBContext configContext = JAXBContext
					.newInstance(new Class[] { BackUpConfig.class });
			config = (BackUpConfig) configContext.createUnmarshaller()
					.unmarshal(url);
		} catch (Exception e) {
			logger.error("ADC-BAK-001: [INIT CONFIG ERROR] Read config file error . "
					+ e.getMessage());
			throw e;
		}
		
		// 获取是否启用自定义备份策略标志
		Boolean enable = config.getIncrementalBackupConfig().isEnabled() ;
		int timeInt = 0; 
		Calendar now = new GregorianCalendar();
	    Calendar cal = (Calendar)now.clone();
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		if(enable){   //  启动自定义区间备份
			 String beginDate = config.getIncrementalBackupConfig().getBegin() ;
			 String endDate = config.getIncrementalBackupConfig().getEnd() ;
			 try{
				 cal.setTime(sdf.parse(beginDate)) ;
				 backUpBeginTime =  cal.getTimeInMillis();
				 cal.setTime(sdf.parse(endDate));
				 backUpEndTime =  cal.getTimeInMillis();
			 }catch(ParseException e) {
				 logger.error("ADC-BAK-008: [INIT CONFIG ERROR] Config begin|end date format error . " + 
					        e.getMessage());
					      throw e;
			 }
		}else{
		    //  OneBackupConfig Start 
		    try
		    {
		      timeInt = Integer.parseInt(config.getOneBackupConfig().getTime());
		      if (timeInt <= 0) {
		        throw new Exception(" Time must larger than 0. ");
		      }
		    }
		    catch (Exception e)
		    {
		      logger.error("ADC-BAK-001: [INIT CONFIG ERROR] Config time error . " + 
		        e.getMessage());
		      throw e;
		    }
		    
		    if (ConstantDefinition.MONTH.equals(config.getOneBackupConfig().getTimeUnit()))
		    {
		      cal.add(Calendar.MONTH, -timeInt);
		    }
		    else if (ConstantDefinition.DAY_OF_YEAR.equals(config.getOneBackupConfig().getTimeUnit()))
		    {
		      cal.add(Calendar.DAY_OF_YEAR, -timeInt);
		    }
		    else if (ConstantDefinition.HOUR_OF_DAY.equals(config.getOneBackupConfig().getTimeUnit()))
		    {
		      cal.add(Calendar.HOUR_OF_DAY, -timeInt);
		    }
		    else
		    {
		      logger.error("ADC-BAK-002: [INIT CONFIG ERROR] No support the time unit. ");
		      throw new Exception(" No support the time unit. ");
		    }
		    backUpBeginTime = 0 ;
		    backUpEndTime = cal.getTimeInMillis();
		    //  OneBackupConfig End 
		}
		Path dataPath = Paths.get(config.getDataDir(), new String[0]);
		if (!Files.exists(dataPath, new LinkOption[0])) {
			logger.error("ADC-BAK-003: [INIT CONFIG ERROR] Data path is not exist. ");
			throw new Exception(" Data path is not exist. ");
		}

		Path backUpPath = Paths.get(config.getBackUpDir(), new String[0]);
		if (!Files.exists(backUpPath, new LinkOption[0])) {
			logger.info("ADC-BAK-004: [INIT CONFIG INFO] BackUp path is not exist. ");
			//throw new Exception(" BackUp path is not exist. ");
		}
		
		String folder;
		try {
			/*sdf = new SimpleDateFormat(
					config.getBackUpFolderFormat());
			folder = sdf.format(now.getTime());*/
			Path backUp = Paths.get(config.getBackUpDir(), new String[0]);
			Files.createDirectories(backUp,
					new java.nio.file.attribute.FileAttribute[0]);
			backUpFloder = backUp.toString();
		} catch (Exception e) {
			logger.error("ADC-BAK-005: [INIT CONFIG ERROR] Create backup folder failed . "
					+ e.getMessage());
			throw e;
		}
		backUpConfig = config;
	}

	protected void initLog() {
		try {
			URL url = getClass().getResource(ConstantDefinition.DEFAULT_LOGCONFIG);
			DOMConfigurator.configure(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
