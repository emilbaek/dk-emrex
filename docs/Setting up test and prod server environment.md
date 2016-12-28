# Setting up test or prod server environment

## WebLogic server set up
### Prerequisites

1.	WebLogic installed.

## Setting up WebLogic domain
1. 	Change Java random number generator to '-Djava.security.egd=file:/dev/./urandom'. See how to change random number generator [here](http://docs.oracle.com/cd/E12529_01/wlss31/configwlss/jvmrand.html "title"). 
2. 	Run WL_HOME/oracle_common/common/bin/config.sh.
3.	Select 'Create a new domain', specify 'Domain Location' and press 'Next'.
4.	Press 'Next'.
5. 	Specify admin userid and password and press 'Next'.
6.	Select 'Production' and press 'Next'.
7.	Select 'Administration Server' and press 'Next'.
8.	Specify 8000 as 'Listen Port' and press 'Next'.
9.	Press 'Create'.
10. Press 'Next'.
11. Press 'Finish'.

## Setting up application

1.  Copy NCP WAR-file to desired installation folder.
2.  Start up WebLogic console.
3.  Select 'Deployments'.
4.	Delete existing NCP deployment, if any.
5.	Press 'Install'.
6.	Select NCP WAR-file from install folder and press 'Next'.
8.	Select 'Install this deployment as an application' and press 'Next'.
10.	(Select desired server and press next).
12. Press 'Finish'.

## Configuring application
### Properties files
**Please replace ${env} below with either dev, test or prod for desired environment.** 

1.	Copy stads-registry-${env}.json til /etc/emrex
2.	Copy dk-emrex-${env}.cer til /etc/emrex  
3.	Copy dk-emrex-${env}.key til /etc/emrex

### Setting environment for test and prod
**Please that dev is the default if test and prod is not specified** 
Edit WL_HOME/user_projects/emrex/bin/setDomainEnv.sh to add '-Dspring.profiles.active=test' or -Dspring.profiles.active=prod' to JAVA_OPTIONS at top of file with line as shown below.

**Windows**

> set JAVA_OPTIONS=%JAVA_OPTIONS% -Dspring.profiles.active=test

**Linux**

> JAVA_OPTIONS="${JAVA_OPTIONS} -Dspring.profiles.active=test
> export JAVA_OPTIONS 





