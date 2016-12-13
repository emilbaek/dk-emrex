# Documentation of smp-mock server
### Introduction
The purpose of the program is to provide an easy way 
to test the NCP (national contact point application). 
This is done by POSTing a returnUrl to the NCP application. 
The returnUrl is used by the NCP application to post back 
the exam results. 

### The application.properties file
The smp-mock application is a spring boot application, therefore
you can setup the application in the application.properties file, 
which is read from the classpath of the application. 

An example of the application.properties file: 

- server.port = 8080
- environment.url = http://dans-emrexws.kmd.dk
- environment.port = 8000
- return.url = http://dans-emrexws.kmd.dk

##### Short description of the fields.
- server.port - The port where the smp-mock application is supposed to start on.
- environment.url  - The URL of the NCP application.
- environment.port - The port of the NCP application. 
- return.url - The URL of the smp-mock application.

### How to use
Start the war file (java -jar &lt;file_name&gt;.war), and go to the /smp 
page and press the submit button. This should bring you to the 
NCP site. On the NCP select the courses, and proceed through the 
flow and POST the results back. This will bring you to the /onReturn 
site on the smp-mock application. Here you should be able to view the
received PDF og XML information.