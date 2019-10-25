This is caDSR runner microservice to download CS/CSI component into XML format.
JIRA: CADSRMETA-757.
This is a Maven and Springboot application. The services connect to caDSR DB.

The service expects that a file to parse is uploaded in a directory listed in configuration file.
This application configuration file is:
src/main/resources/boot.properties

Spring boot application configuration conventional file name:
application.properties

!!!
We expect the next four environment variables to be passed to the application:
db_driver, db_url, db_user=SBR, db_credential
!!!

Log file as of now:
log/cscsi-xml-dwnld.log

To run boot from a command line:
1. Go to project pom.xml file directory with environment set up.

2. Build:
$ mvn clean package

3. To run the application using jar file when the environment is set up run java command:
$ java -jar target/cscsi-xml-dwnld-1.0.0.jar

4. Results.
XML directory is "xmldir" configured in boot.properties.
Files created there are named as:
xml_cde_2019102544648.xml
xml_cde_2019102544648.zip

