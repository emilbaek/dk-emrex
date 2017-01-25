# Setting up development server environment
 
## Apache server on dans-emrexws.kmd.dk
Install Apache 2.4 on dans-emrexws.kmd.dk.  
Copy all files and folders from project at dk-emrex/ncp/etc/httpd to /etc/httpd on server.  
Configure Apache as service.  
To start server use 'sudo /sbin/service httpd start'.   
To restart server use 'sudo /sbin/service httpd restart'.  
To stop server use 'sudo /sbin/service httpd stop'. 
	 
## Weblogic server on dans-emrex.kmd.dk
Download WebLogic 12.2.x Quick Installer (not Generic installer) from [here](http://www.oracle.com/technetwork/middleware/weblogic/overview/index.html "WebLogic download").     
Install WebLogic on dans-emrex.kmd.dk.   
For non-production environments put '-Djava.security.egd=file:/dev/./urandom' in JAVA_OPTIONS in startWebLogic.sh.  
To assume weblogic without x window support use 'sudo -iu weblogic'. 
To assume weblogic with x window support use 'xsudo weblogic'
Adminstrationskonsol på http://dans-emrex.kmd.dk:8000/console.   
Install NCP WAR-file in root context as application on port 8000. 
stads-registry fil placeres i /etc/emrex sammen med certifikat og private key filer.  
Change WebLogic SSL hostname verification to 'weblogic.security.utils.SSLWLSWildcardHostnameVerifier'
Use './weblogic start' to start application.  
Use './weblogic restart' to restart application.  
Use './weblogic stop' to stop application.  
