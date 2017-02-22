#Setting up Nginx for development

#####Installing and configuring
As an easy and lightweight alternative to use the apache 2.4 server for the development environment, 
it is possible to use the nginx server instead. The configuration can be found in 

> ./dk-emrex/ncp/etc/nginx

At the time of writing the project uses Nginx 1.10.2, which is the newest. Documentation 
on installing the windows version can be found [here](http://nginx.org/en/docs/windows.html). 

When nginx has been installed copy the configuration file nginx.conf and the accompanying ssl folder to the <install-dir>/conf
directory. 

#####Using nginx
**Common commands**
- **nginx -s stop** 	fast shutdown
- **nginx -s quit** 	graceful shutdown
- **nginx -s reload** 	changing configuration, starting new worker processes with a new configuration, graceful shutdown of old worker processes
- **nginx -s reopen** 	re-opening log files


**Starting nginx on windows**

Browse the install directory where the nginx.exe file is and type the following command 

> start nginx.exe

To ensure that nginx have started you can view the instances using the following command

> tasklist /FI "imagename eq nginx.exe"

  
