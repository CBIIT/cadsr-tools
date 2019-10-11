#!/bin/sh
echo $0
cwd=`pwd`
echo cwd: $cwd
#export db_credential=
#export db_user=
#export db_url=
#export db_driver=oracle.jdbc.OracleDriver

echo db_url: $db_url

archive=$WORKSPACE/cde-xml-dwnld/archive
export xmldir=$WORKSPACE/cde-xml-dwnld/xmldir

mkdir -p $archive
mkdir -p $xmldir

#create zip file
java -Xmx4096m -jar $WORKSPACE/cde-xml-dwnld/target/cde-xml-dwnld-1.0.0.jar

#echo archive $archive
ftp_dir="${ftp_dir:-/caDSR_Downloads/CDE}"

j=0
var="`find $WORKSPACE/cde-xml-dwnld/xmldir/ -name 'xml_cde_*.zip' -type f -mtime -1`"
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
#ftp -in >> /tmp/ftp.good 2>> /tmp/ftp.bad <<-EOF
/local/home/cadsrdocker/bin/ftp -in >> /tmp/ftp.good 2>> /tmp/ftp.bad <<-EOF
	open $ftp_host
	user $ftp_user $ftp_cred
	bin
	cd $ftp_dir
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
