
# Installation of Standalone NCP

This document describes how to install NCP war file as stand-alone application run with embedded Tomcat.

### Prerequisites :

Red Hat Linux downloaded and installed. Downloads from https://access.redhat.com/downloads.

WebLogic 12.2.x installed on a Red Hat

### Installation
**Please replace ${env} below with either dev, test or prod for desired environment.**

Copy stads-registry-${env}.json til /etc/emrex

Copy dk-emrex-${env}.cer til /etc/emrex  
Copy dk-emrex-${env}.key til /etc/emrex

The prod .cer and .key files for production are private to the customer and as such not available to KMD.  

Copy NCP ncp-1.0-SNAPSHOT.war file to desired location.

Start application on command line by executing :
  java -jar -Dspring.profiles.active=${env} ncp-1.0-SNAPSHOT.war

Logging is done to STDOUT.   

##### Installation as init.d service
To use application as a init.d service please see this [init.d guide](http://docs.spring.io/spring-boot/docs/current/reference/html/deployment-install.html "information about init.d").
