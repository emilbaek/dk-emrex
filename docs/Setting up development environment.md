# Set up development environment

### Java installation
Install latest Windows 64 bit JDK 8 (not JRE).

### Eclipse installation
Install Eclipse Spring Tool Suite 64 bit for Windows [Spring Tool Suite 64 bit for Windows](https://spring.io/tools/sts/all "Spring Tool Suite").  
Install [Lombok](https://projectlombok.org/ "Project Lombok") in Eclipse.  
Set eclipse to use JDK (not JRE) in sts.ini file by adding the following lines to the beginning of sts.ini file.
  
> -vm
> C:\<path to jdk>\bin\javaw.exe

Set eclipse to use JDK (not JRE) as default jre in eclipse\preference\Installed JREs.  
If not already installed then install into Eclipse the m2e connector for build-helper-maven-plugin.  
If not already installed then install into Eclipse the m2e-wro4j m2e connector from Eclipse Marketplace.  
Install Markdown Text Editor from Eclipse Marketplace. 

### Import source code
Import project with all sub projects as Eclipse projects from GITHUB at 
[https://github.com/emilbaek/dk-emrex.git](https://github.com/emilbaek/dk-emrex.git "dk-emrex GITHUB").  
Run maven clean install with the pom from the root of the dk-emrex project. A launch profile called 'maven clean install.launch' can be found in dk-emrex/launch.     

### Set up for testing
Install latest stable version of nginx.   
Configure nginx with files from dk-emrex/ncp/etc/nginx.  
Set hostnames dans-emrexws.kmd.dk, emrex-test-stads.dk and emrex.stads.dk to point to 127.0.0.1 in etc/hosts file.   

### Start application
Run the launch profile 'ncp-dev.launch' (or 'ncp-test.launch' or 'ncp-prod.launch' as appropriate) from 'dk-emrex/ncp/launch'.   
Point your browser to [local EMREX NCP](https://localhost "EMREX NCP").  
Select 'IT Department of the Ministry of Higher Education'.  
Log in using userid/password as either embkmd/kmdem1l or qnkkmd/kmd8qnk. 
   
