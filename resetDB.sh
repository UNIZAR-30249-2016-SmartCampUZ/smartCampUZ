if [ `ls | grep dump.sql` ]; then
    psql -a -f dump.sql template1 
else
    echo "Missing dump.sql file. Please contact developers"  
fi
