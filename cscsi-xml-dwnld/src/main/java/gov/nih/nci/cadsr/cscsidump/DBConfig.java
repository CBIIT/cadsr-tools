/*
 * Copyright (C) 2019 Frederick National Laboratory for Cancer Research - - All rights reserved.
 */
package gov.nih.nci.cadsr.cscsidump;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.jdbc.core.JdbcTemplate;
/**
 * We use embedded Tomcat DB data source.
 * Spring framework can use HikariCP.
 * app.datasource is a prefix for Hikari.
 * 
 * @author asafievan
 *
 */
@Configuration
@ConfigurationProperties(prefix = "spring.datasource")
public class DBConfig {
	private static final Logger logger = LoggerFactory.getLogger(DBConfig.class);

	@Autowired
	DataSource dataSource;

	@Primary
	@Bean(name = "jdbcTemplate")
	public JdbcTemplate getJdbcTemplate() {
		logger.debug("jdbcTemplate loading, dataSource defined? " + (dataSource != null));
		JdbcTemplate templ = new JdbcTemplate(dataSource);
		return templ;
	}
}
