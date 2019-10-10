This is Maven springboot command line utility to generate XML CDE Released dump with CDE XML Download format.

To package:
>mvn clean package
It creates springboot cdereleased-1.0.1.jar which includes all dependencies.

Environment required:
db_credential
db_user
db_url
db_driver=oracle.jdbc.OracleDriver

Format of db_url:
jdbc:oracle:thin:@<url>:<port>:<OID>

To run using Maven from project directory:
>mvn spring-boot:run -Drun.arguments="xml"

To run using jar provide a path to jar file. 

Example:
> java -Xmx4096m -jar target/cde-xml-dwnld-1.0.0.jar

To run upload to FTP site, use run-ftp.sh
Environment required:
ftp_host
ftp_user
ftp_credential
ftp_directory

The utility runs java command to create a zip file with XML files in "xmldir" directory, 
sends zip file to FTP site, 
moves zip file to "archive" directory, 
deletes all created XML files from "xmldir",
deletes all zip files older than 7 days from the archive.
