This is a Maven spring boot CommandLineRunner application.

The service expects that a file to parse is uploaded in a directory listed in configuration file.
This application configuration file is:
src/main/resources/boot.properties

Spring boot application configuration conventional file name:
application.properties

!!!
We expect the next four environment variables to be passed to the application:
db_driver, db_url, db_user=SBR, db_credential
!!!

Maven "package" can copy all dependencies libraries to "target/lib" directory.
Uncomment in pom.xml if this is desirable.

Log file as of now:
log/cscsi-xml-dwnld.log

"CsCsiXmlMicroservice" class is the main application class.

One can run Spring Boot application from Eclipse using a "CsCsiXmlMicroservice" class, using right mouse button menu "Run as" -> "Spring Boot App".
This run menu is shown if using STS IDE, or if Eclipse "STS plugin" is installed.

To run boot from a command line:
1. Go to project pom.xml directory

2. Maven build
On 11g:
mvn -Ddb_driver=... -Ddb_url=<drivertype>:@<url>:<port>:<sid> -Ddb_user=... -Ddb_credential=... clean package
On 12c:
mvn -Ddb_driver=... -Ddb_url=<drivertype>:@<url>:<port>/<service> -Ddb_user=... -Ddb_credential=... clean package 

If environment is set up just run mvn command
>mvn clean package

3.

To run the application using jar file from the main project directory passing environment dynamically:
On 11g
>java -Ddb_driver=... -Ddb_url=<drivertype>:@<url>:<port>:<sid> -Ddb_user=... -Ddb_credential=...  -jar target/cscsi-xml-dwnld-1.0.0.jar

On 12c
>java -Ddb_driver=... -Ddb_url=<drivertype>:@<url>:<port>/<service> -Ddb_user=... -Ddb_credential=...  -jar target/cscsi-xml-dwnld-1.0.0.jar

If environment is set up just run java command
>java -jar target/cscsi-xml-dwnld-1.0.0.jar
