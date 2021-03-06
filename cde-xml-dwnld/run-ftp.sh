#!/bin/sh
echo $0
cwd=`pwd`
echo cwd: $cwd
export db_credential=Nci_gue5t
export db_user=guest
#export db_url=
export db_driver=oracle.jdbc.OracleDriver

echo db_url=$db_url

#create zip file
java -Xmx4096m -jar ./target/cde-xml-dwnld-1.0.0.jar
archive=$(pwd)/archive
mkdir -p $archive
#echo archive $archive

j=0
var="`find ./xmldir/ -name 'xml_cde_*.zip' -type f -mtime -1`"
#we have in $var some files with last day change date

#the next shall be set up in the environment
ftp_cred="${ftp_credential}"
ftp_user="${ftp_user}"
ftp_host="${ftp_host}"
echo ftp_host: $ftp_host

for i in $var
do
j=$(( $j + 1 ))
dirname="`dirname $i`"
filename="`basename $i`"
echo dirname: $dirname, filename: $filename
ftp -in >> /tmp/ftp.good 2>> /tmp/ftp.bad <<-EOF
	open $ftp_host
	user $ftp_user $ftp_cred
	bin
	cd /caDSR_Downloads/CDE
	lcd $dirname
	put $filename
	quit
EOF
#end of ftp
mv $dirname/$filename $archive
rm $dirname/*.xml
done
#end of for iteratior
#delete archived files older than 7 days
find $archive -type f -mtime +7 -delete
