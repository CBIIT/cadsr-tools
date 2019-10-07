package gov.nih.nci.cbiit.cadsr.datadump;
/*
 * Copyright (C) 2018 Leidos Biomedical Research, Inc. - All rights reserved.
 */
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
public class SpringUtil {
	static XmlBeanFactory beanFactory = new XmlBeanFactory(new ClassPathResource("beans.xml"));

	public static CDEDump getXMLCDEDumper() {
		return ((CDEDump) beanFactory.getBean("xmlCDEDumper"));
	}

	static {
		PropertyPlaceholderConfigurer config = (PropertyPlaceholderConfigurer) beanFactory
				.getBean("propertyConfigurer");
		config.postProcessBeanFactory(beanFactory);
	}
}
