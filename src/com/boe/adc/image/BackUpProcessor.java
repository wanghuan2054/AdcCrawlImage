package com.boe.adc.image;

import com.boe.adc.image.file.FileWalker;

import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

public class BackUpProcessor
  extends Thread
{
  private static final Logger logger = Logger.getLogger(BackUpProcessor.class);
  private String basePath;
  private String threadPath;
  private String destPath;
  
  public BackUpProcessor(String basePath, String destPath)
  {
    this.basePath = basePath;
    this.destPath = destPath;
  }
  
  public BackUpProcessor(String basePath, String threadPath, String destPath)
  {
    this.basePath = basePath;
    this.threadPath = threadPath;
    this.destPath = destPath;
  }
  
  public void run()
  {
    SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Path currentPath;
    if (this.threadPath == null) {
      currentPath = Paths.get(this.basePath, new String[0]);
    } else {
      currentPath = Paths.get(this.basePath, new String[] { this.threadPath });
    }
    System.out.println(Thread.currentThread().getName() + " "+date.format(new Date()) + " Start run " + currentPath.toString() + ". ");
    logger.info(Thread.currentThread().getName() + " Start run " + currentPath.toString() + ". ");
    FileWalker walker = new FileWalker(this.basePath, this.destPath);
    walker.walk(currentPath);
    System.out.println(Thread.currentThread().getName() + " "+date.format(new Date()) + " End run " + currentPath.toString() + ". ");
    logger.info(Thread.currentThread().getName() + " End run " + currentPath.toString() + ". ");
  }
}
