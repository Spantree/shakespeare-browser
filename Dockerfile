FROM atomi/wheezy-oraclejdk:8
MAINTAINER Cedric Hurst <cedric@spantree.net>

ENV TOMCAT_MAJOR_VERSION 7
ENV TOMCAT_MINOR_VERSION 7.0.57
ENV CATALINA_HOME /tomcat

# Install Tomcat
RUN wget -q https://archive.apache.org/dist/tomcat/tomcat-$TOMCAT_MAJOR_VERSION/v$TOMCAT_MINOR_VERSION/bin/apache-tomcat-$TOMCAT_MINOR_VERSION.tar.gz && \
    wget -qO- https://archive.apache.org/dist/tomcat/tomcat-$TOMCAT_MAJOR_VERSION/v$TOMCAT_MINOR_VERSION/bin/apache-tomcat-$TOMCAT_MINOR_VERSION.tar.gz.md5 | md5sum -c - && \
    tar zxf apache-tomcat-*.tar.gz && \
    rm apache-tomcat-*.tar.gz && \
    mv apache-tomcat* $CATALINA_HOME

# Remove management, config and documentations contexts but keep ROOT
RUN find $CATALINA_HOME/webapps/* ! -name 'ROOT' -type d -exec rm -rf {} +

# Use /data for easier volume handling
RUN ln -s $CATALINA_HOME/webapps/ROOT /data

# Clean out ROOT and add our workspace
WORKDIR /data
ONBUILD RUN rm -rf *
ONBUILD ADD target/shakespeare-browser-0.1.war /data/ROOT.war

EXPOSE 8080

CMD $CATALINA_HOME/bin/catalina.sh run
