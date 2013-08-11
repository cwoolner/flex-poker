
if [ -z "$TOMCAT8_HOME" ]; then
    echo -e "\n\nPlease set TOMCAT8_HOME\n\n"
    exit 1
fi

rm -rf $TOMCAT8_HOME/webapps/flexpoker/resources/
rm -rf $TOMCAT8_HOME/webapps/flexpoker/WEB-INF/views/

cp -r src/main/webapp/resources $TOMCAT8_HOME/webapps/flexpoker/
cp -r src/main/webapp/WEB-INF/views $TOMCAT8_HOME/webapps/flexpoker/WEB-INF/
