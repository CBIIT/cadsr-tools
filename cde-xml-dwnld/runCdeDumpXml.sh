#!/bin/sh
echo $0
cwd=`pwd`
echo cwd: $cwd

#export db_credential=
#export db_user=
#export db_url=
#export db_driver=oracle.jdbc.OracleDriver

echo db_url=$db_url

java -Xmx4096m -jar ./target/cde-xml-dwnld-1.0.0.jar xml
