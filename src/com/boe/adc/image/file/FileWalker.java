package com.boe.adc.image.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileWalker
{
  private String basePath;
  private String destPath;
  
  public FileWalker(String basePath, String destPath)
  {
    this.basePath = basePath;
    this.destPath = destPath;
  }

  public FileVisitResult walk(Path file)
  {
    try
    {
      Files.walkFileTree(file, new FileVisitor(this.basePath, this.destPath));
    } catch (IOException e) {
      e.printStackTrace();
    }
    return FileVisitResult.CONTINUE;
  }
}
