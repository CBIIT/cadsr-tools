/*
 * Copyright (C) 2019 Frederick National Laboratory for Cancer Research - - All rights reserved.
 */
package gov.nih.nci.cadsr.cscsidump;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Component;
/**
 * 
 * @author asafievan
 *
 */
@EnableAutoConfiguration
@Component
public class CsCsiXmlRunner implements CommandLineRunner {
	private static final Logger logger = LoggerFactory.getLogger(CsCsiXmlRunner.class);
	static final String sessionCookieName = "_cchecker";
    @Autowired
    private DataElementRepository dataElemenRepository;
	private static int BUFFER = 1048576;//1 MB
    
	private void createZipFile(List<String> fileList, String zipFilename) throws Exception {
		BufferedInputStream origin = null;
		FileOutputStream dest = null;
		ZipOutputStream out = null;
		int count;
		try {
			dest = new FileOutputStream(zipFilename);
			out = new ZipOutputStream(new BufferedOutputStream(dest));

			byte[] data = new byte[BUFFER];

			for (int i = 0; i < fileList.size(); ++i) {
				FileInputStream fi = new FileInputStream(fileList.get(i));

				origin = new BufferedInputStream(fi, BUFFER);
				File file = new File(fileList.get(i));
				String fileName = file.getName();
				ZipEntry entry = new ZipEntry(fileName);
				out.putNextEntry(entry);

				while ((count = origin.read(data, 0, BUFFER)) != -1) {
					out.write(data, 0, count);
				}

				origin.close();
			}
		} 
		catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		finally {
			out.close();
		}
	}
	public String getFileNamePrefix() {
		return "xml_cde_";
	}
	@Override
	public void run(String... args) throws Exception {
		String strDataDirectory = CsCsiXmlMicroservice.UPLOADED_FOLDER + File.separator;
		String timeStr = getTimeStr();
		String currFilePath = strDataDirectory + getFileNamePrefix() + timeStr + ".xml";
		dataElemenRepository.retrieveWriteClob(currFilePath);
		logger.info("CsCsiXmlRunner done creating xml file: " + currFilePath);
		
		List<String> zipFiles = new ArrayList<String>(4);
		zipFiles.add(currFilePath);
		String zipFileName = strDataDirectory + getFileNamePrefix() + timeStr + ".zip";
		createZipFile(zipFiles, zipFileName);
		logger.info("CsCsiXmlRunner done creating zip file: " + zipFileName);
		exit(0);
	}
	private void exit(int i) {
		logger.info("Exiting CsCsiXmlRunner with code: " + i);
		System.exit(i);
	}
	//FIXME make better file name format
	protected String getTimeStr() {
		Calendar cal = Calendar.getInstance();
		int date = cal.get(5);
		int month = cal.get(2) + 1;
		int year = cal.get(1);
		int sec = cal.get(13);
		int min = cal.get(12);
		int hour = cal.get(10);

		return "" + year + month + date + hour + min + sec;
	}
}
