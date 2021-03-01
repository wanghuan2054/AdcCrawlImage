package com.boe.adc.image.file;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileTime;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.boe.adc.image.ADCBackup;
import com.boe.adc.image.ConstantDefinition;
import com.sun.org.apache.xpath.internal.operations.And;

public class FileVisitor extends SimpleFileVisitor<Path> {
	private static final Logger logger = Logger.getLogger(FileVisitor.class);
	private String basePath;
	private String destPath;
	private DateFormat df = new SimpleDateFormat("yyyyMMdd");

	public FileVisitor(String basePath, String destPath) {
		this.basePath = basePath;
		this.destPath = destPath;
	}

	@Override
	// 访问一个目录之前操作
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
			throws IOException {
		// logger.info(dir.getFileName() + " before visit dirs ");
		return super.preVisitDirectory(dir, attrs);
	}

	// 访问一个目录后操作
	public FileVisitResult postVisitDirectory(Path file, IOException exc) {
		return FileVisitResult.CONTINUE;
	}

	// 访问一个文件失败时操作
	public FileVisitResult visitFileFailed(Path file, IOException exc)
			throws IOException {
		return FileVisitResult.CONTINUE;
	}

	// 正在访问一个文件时操作
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
			throws IOException {
		if (!fileNameFilterChain(file)) {
			return FileVisitResult.CONTINUE;
		}
		FileTime lastModifiedTime = attrs.lastModifiedTime();
		if ((lastModifiedTime.toMillis() >= ADCBackup.backUpBeginTime)
				&& (lastModifiedTime.toMillis() <= ADCBackup.backUpEndTime)) {

			try {
				// move(file);
//				copy(file, df.format(lastModifiedTime.toMillis()));
				copy(file, df.format(new Date()));
				
				logger.info(file.toAbsolutePath() + " copy success ! ");
			} catch (FileAlreadyExistsException e) {
				logger.error("FileAlreadyExistsException " + file.getFileName()
						+ " already existed");
				return FileVisitResult.CONTINUE;
			} catch (IOException e) {
				logger.error("File Copy IO Exception " + file.getFileName()
						+ " " + e.getMessage());
				return FileVisitResult.CONTINUE;
			}
		}
		return FileVisitResult.CONTINUE;
	}

	/*
	 * 图片文件名过滤规则链 filterRule1 : 6lka994001c8_dft_map_20190913_222746.jpg
	 */
	private boolean fileNameFilterChain(Path file) {

		String[] stopWords = ConstantDefinition.FILTERWORDS;
		for (String s : stopWords) {
			if (file.toString().contains(s)) {
				logger.info(file.toString() + " Cotains filter words " + s);
				return false;
			}
		}
		return true;
	}

	/**
	 * move: 从源端move文件到目的端，删除源端文件
	 */
	private void move(Path sourceFile) throws IOException {
		String filePath = sourceFile.toString();
		if ((filePath != null)
				&& (filePath.contains(ConstantDefinition.RIGHT_SLASH))) {
			filePath = filePath.replace(ConstantDefinition.RIGHT_SLASH,
					ConstantDefinition.LEFT_SLASH);
		}
		if ((this.basePath != null)
				&& (this.basePath.contains(ConstantDefinition.RIGHT_SLASH))) {
			this.basePath = this.basePath.replace(
					ConstantDefinition.RIGHT_SLASH,
					ConstantDefinition.LEFT_SLASH);
		}
		if ((this.destPath != null)
				&& (this.destPath.contains(ConstantDefinition.RIGHT_SLASH))) {
			this.destPath = this.destPath.replace(
					ConstantDefinition.RIGHT_SLASH,
					ConstantDefinition.LEFT_SLASH);
		}
		Path destFile;
		filePath = filePath.replace(this.basePath, this.destPath);
		destFile = Paths.get(filePath, new String[0]);
		Files.createDirectories(destFile.getParent(), new FileAttribute[0]);
		Files.move(sourceFile, destFile,
				new CopyOption[] { StandardCopyOption.REPLACE_EXISTING });
	}

	/**
	 * copy: 从源端copy文件到目的端，保留源端文件 ， 如果目的端存在该文件， 则替换
	 */
	private void copy(Path sourceFile, String dateForamt) throws IOException {
		String filePath = sourceFile.toString();
		if ((filePath != null)
				&& (filePath.contains(ConstantDefinition.RIGHT_SLASH))) {
			filePath = filePath.replace(ConstantDefinition.RIGHT_SLASH,
					ConstantDefinition.LEFT_SLASH);
		}
		if ((this.basePath != null)
				&& (this.basePath.contains(ConstantDefinition.RIGHT_SLASH))) {
			this.basePath = this.basePath.replace(
					ConstantDefinition.RIGHT_SLASH,
					ConstantDefinition.LEFT_SLASH);
		}
		if ((this.destPath != null)
				&& (this.destPath.contains(ConstantDefinition.RIGHT_SLASH))) {
			this.destPath = this.destPath.replace(
					ConstantDefinition.RIGHT_SLASH,
					ConstantDefinition.LEFT_SLASH);
		}
		Path destFile;
		String imageName = sourceFile.getFileName().toString();
		String step = sourceFile.getParent().toString()
				.contains(ConstantDefinition.GATESTEP) ? ConstantDefinition.GATESTEP
				: ConstantDefinition.SDSTEP;
		if (!getMainSubCode(imageName).equals(ConstantDefinition.EMPTY_STRING)) {

			destFile = Paths.get(this.destPath, new String[] { dateForamt,
					step, getMainSubCode(imageName), imageName });
			// System.out.println("sourceFile :"+sourceFile.toAbsolutePath());
			// System.out.println("destFile :"+destFile.toAbsolutePath());
			Files.createDirectories(destFile, new FileAttribute[0]);
			Files.copy(sourceFile, destFile,
					new CopyOption[] { StandardCopyOption.REPLACE_EXISTING });
		}

	}

	/*
	 * getMainSubCode(): 从图片文件名上获取MainCode 和 SubCode function : 对所有MainCode
	 * 为应对一二期命名规则不一致，采用名字反转数数 SubCode 进行大写转换 return : SubCode/MainCode ,拼成路径返回
	 */
	private String getMainSubCode(String imageName) {
		// 6lka994001axddc_act_g515_d2837_x524.890_y-558.984_p_p0_sdsr5_10305497_20190913_223354.jpg
		// 6lf999d008c8ddl_pad_x709.028_y-621.260_p_p0_sdsp1_10261628_20190919_163327.jpg
		String[] imageNames = imageName.split(ConstantDefinition.UNDERLINE);
		String subCode = imageNames[imageNames.length
				- ConstantDefinition.SUBCODE_POS];
		String mainCode = imageNames[imageNames.length
				- ConstantDefinition.MAINCODE_POS];
		// 如果两个code 在配置文件同时存在 , 则取对应code 图片

		if ((ADCBackup.backUpConfig.getMainCode().toLowerCase()
				.equals(ConstantDefinition.ASTERISK))
				|| (ADCBackup.backUpConfig.getMainCode().toLowerCase()
						.contains(mainCode))) {
			if ((ADCBackup.backUpConfig.getSubCode().toLowerCase()
					.equals(ConstantDefinition.ASTERISK))
					|| (ADCBackup.backUpConfig.getSubCode().toLowerCase()
							.contains(subCode))){
				return  subCode + ConstantDefinition.LEFT_SLASH + mainCode;
			}
				
		}
		return ConstantDefinition.EMPTY_STRING;
	}

	/*
	 * 基于 FileChannel transferTo copy image 方法 待完善， 再启用
	 */
	private void copy(String imageName) {
		/*
		 * long startTime = System.currentTimeMillis(); FileChannel inChannel =
		 * null; FileChannel outChannel = null;
		 * 
		 * try { Path sourcePath = Paths.get(
		 * "F://data//Root//on//image//bp//l691j//6lka//99//40//01//6lka994001a7_dft_map_20190913_222808.jpg"
		 * ); Path destinationPath = Paths.get("F:/data/20190913/");
		 * Files.copy(sourcePath, destinationPath,
		 * StandardCopyOption.REPLACE_EXISTING); inChannel = FileChannel.open(),
		 * StandardOpenOption.READ); outChannel =
		 * FileChannel.open(Paths.get("F:/data/20190913/training.jpg"),
		 * StandardOpenOption.READ, StandardOpenOption.WRITE,
		 * StandardOpenOption.CREATE); inChannel.transferTo(0, inChannel.size(),
		 * outChannel); } catch (Exception e) { e.printStackTrace(); }finally{
		 * if(inChannel != null){ try { inChannel.close(); } catch (IOException
		 * e) { e.printStackTrace(); } } if(outChannel != null){ try {
		 * outChannel.close(); } catch (IOException e) { e.printStackTrace(); }
		 * } }
		 * 
		 * long endTime = System.currentTimeMillis();
		 * System.out.println("通道之间的数据传输（直接缓存区）耗时为" + (endTime - startTime) +
		 * "秒");
		 */
	}

}