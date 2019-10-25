/*
 * Copyright (C) 2019 Frederick National Laboratory for Cancer Research - - All rights reserved.
 */
package gov.nih.nci.cadsr.cscsidump;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
/**
 * 
 * @author asafievan
 *
 */
@SpringBootApplication(scanBasePackages = {"gov.nih.nci.cadsr.cscsidump"})
@EnableAutoConfiguration
public class CsCsiXmlMicroservice {
    private static final Logger logger = LoggerFactory.getLogger(CsCsiXmlMicroservice.class);
    //Save the file to this folder
    static String UPLOADED_FOLDER;// xmldir
    
	@Autowired
	DataSource dataSource;
	
	public static void main(String[] args) throws Exception {
		Properties properties = new Properties();
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		try (InputStream input = classLoader.getResourceAsStream("boot.properties")) {
			properties.load(input);
		}
		logger.info(properties.toString());
		String propVal;
		if ((propVal = properties.getProperty("xmldir")) != null)
			UPLOADED_FOLDER = propVal;
		else UPLOADED_FOLDER = "xmldir";

		File uploadeDir = new File(UPLOADED_FOLDER);
		if (! uploadeDir.exists())	{
			uploadeDir.mkdirs();
			logger.debug("Directory created: "+uploadeDir.getAbsolutePath());
		}
		
		final ApplicationContext ctx = SpringApplication.run(CsCsiXmlMicroservice.class, args);
		DataSource ds = ctx.getBean(DataSource.class);
		if (ds != null)
			logger.debug("DataSource bean in use: "+ ds.getClass().getName());
		else {
			logger.error("!!! DataSource bean not defined !!!");
		}
	}
}