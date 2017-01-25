
# Setting up WebLogic on Windows
1.  Put JDK (not JRE) on path.
2.  Position in folder where you would like installation to take place.
3.  Run 'java -jar fmw_12.2.x.x.x_wls.jar.jar'.
4.  Change to folder wls12xxx.
5.	Run wlserver\server\bin\setWLSEnv.cmd.
6.  Run 'mkdir emrex'.
7.	Run 'cd emrex'
8.  Run '%JAVA_HOME%\bin\java.exe  weblogic.Server'.
9.  Specify user as 'weblogic'
10. Specify password as 'Welcome1'
11. Restart weblogic
12. Point your browser to [http://localhost:7001/console/](http://localhost:7001/console/ "WebLogic console").
13. Change listen port to 8000.
14. Deploy NCP war-file as application.
15. Change WebLogic SSL hostname verification to 'weblogic.security.utils.SSLWLSWildcardHostnameVerifier'.
