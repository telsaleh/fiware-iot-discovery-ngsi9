# Introduction

The main components for installation the GEi are 2 Java WAR files that are deployed on Apache Tomcat as server modules, and a backend MySQL database.

# System Requirements

In order to deploy IoT Discovery the following software must be previously installed:
<br>

 - [Oracle Java J2SE v8 JRE/JDK][Oracle Java]
	 - The GEi requires that Oracle Java 8 is installed. 
	 - If Ubuntu Linux is used, please follow the guide available [here][Oracle Java Ubuntu default] or [here][Oracle Java Ubuntu webupd8].
 - [Apache Tomcat 7.x][Apache Tomcat]
	 - Tomcat can simply be installed by downloading the compressed file and unpackaging it in the required folder.
	 - Alternatively, it can be installed for autorun at startup.
		 - For Windows 7, download and run the *.msi file.
		 - For Ubuntu, refer to the guide available [here](https://www.digitalocean.com/community/tutorials/how-to-install-apache-tomcat-7-on-ubuntu-14-04-via-apt-get).
 - [MySQL Community Server 5.5](http://dev.mysql.com/downloads/mysql/)
	 - MySQL can be installed on Ubuntu by running the following command: 
	 ``` $ sudo apt-get install mysql-server ```
 - [MySQL Community Workbench 5.2](http://www.mysql.com/downloads/workbench/) (Optional)
	 - MySQL Workbench can be installed on Ubuntu by running the following command:
	 ``` $ sudo apt-get install mysql-workbench ```

# System Installation

The following steps below need to be performed to get the IoT Discovery GEi up & running.

## Unpackage the GEi

The GEi is provided in a zip file. The content of the zip file has the following structure:

- ***sql***
	 - *triple-store.sql*: contains the SQL script for creating the tables for the Sense2Web platform.
- ***tomcat***
	 - *ngsi9.war*: The Java WAR file for the NGSI-9 Server.
	 - *s2w.war*: The Java WAR file for the Sense2Web Platform.
	 - *tomcat-users.xml*: Tomcat configuration for admin GUI access.
- ***unit_test_suites***
  - *Ngsi9ServerTest.zip*: contains the Unit Test Suite for the NGSI-9 Server.
  - *Sense2WebTest.zip*: contains the Unit Test Suite for the Sense2Web Platform.

### Setup MySQL Databases

The first task to perform is to create the databases and their respective tables for the *Sense2Web* platform module. This is done by importing the SQL script *triple-store.sql* provided in the GEi package, which should produce the following databases:

 - resourcedb 
 - entitydb 
 - servicedb

#### Option 1: Command-line Client
Alternatively, to do this step directly from the command-line, connect to MySQL using: ``` $ mysql -u {username} -p ```
Enter the password, then run the script included in the GEi package using the command: ``` $ mysql < \ . {scriptname} ```

#### Option 2: MySQL workbench
To do this, run the MySQL workbench by entering the following command: 
``` $ mysql-workbench ```
![MySQL workbench - admin](http://forge.fiware.org/plugins/mediawiki/wiki/fiware/images/9/9a/Mysql-wb-1.png)

Access the running MySQL server instance under **Server Administration**. Check that the server is running. Under **Data Import/Restore**, import and run the SQL script **triple-store.sql** file that is included in the GEi package.

![MySQL workbench - admin](http://forge.fiware.org/plugins/mediawiki/wiki/fiware/images/d/d8/Mysql-wb-2.png)


### Setup WARs for Tomcat

Copy the Web Application Archive files **s2w.war** and **ngsi9.war** to `/$CATALINA_HOME/webapps/`  whereby **CATALINA_HOME** is the root folder for Tomcat.

#### **ngsi9.war**
The default path for the embedded databases for the NGSI-9 server is `/$CATALINA_HOME/webapps/ngsi9/WEB-INF/repository`
 
Please note that for every undeployment the databases will be removed. Therefore if you still require any registered data, you must specify another path outside the webapp folder (e.g. your home directory).

 To access and edit this file: 

If the tomcat service is running then the WAR file should be uncompressed automatically. In the case where this does not happen, you can try restarting the tomcat server. If this fails, extract the contents of ***.war** file to the "/**webapps**" folder. 

 - This can be done using a file archiver tool such as [7-Zip](http://www.7-zip.org/). 
 - In Ubuntu, the command line below can be used: ``` jar -xvf  ngsi9.war ```
 - In the extracted folder i.e. **/s2w**, go to **/WEB-INF** and open the **web.xml** using your preferred text editor.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app version="2.5" xmlns="http://java.sun.com/xml/ns/javaee" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd">
    <description>NGSI-9 server for IoT Agents that want to register, update and discover IoT NGSI Context Entities</description>
    <display-name>NGSI-9 Server / IoT Discovery</display-name>
    <context-param>
        <description>path to where the ngsi repository is to be located</description>
        <param-name>repository_path</param-name>
        <param-value>none</param-value>
    </context-param>
...
</web-app>
```

#### **s2w.war**

If you have already deployed the web app, then it is likely that it will not not run. This is because you need to make sure that you have set the web app to know what the username and password of the MySQL server is. This can be done bu editing the context parameters ('''context-param''') in the '''web.xml'''. 

Part of the '''web.xml''' containing the context parameters is shown below:
```xml
<?xml version="1.0" encoding="UTF-8"?>
    <web-app version="2.5" xmlns="http://java.sun.com/xml/ns/javaee" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
    xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd">    
        <description>Linked Data Platform for the Internet of Things
            CCSR, University of Surrey 2014</description>
        <display-name>Sense2Web / IoTDiscovery</display-name>
        <context-param>
            <description>username for MySQL access</description>
            <param-name>username_mysql</param-name>
            <param-value>root</param-value>
        </context-param>
        <context-param>
            <description>password for MySQL access</description>
            <param-name>password_mysql</param-name>
            <param-value>root</param-value>
        </context-param>
        <context-param>
            <description>This value reflects the percentage between the number of trained concepts and concepts that have been folded in.
                The lower the value, the more frequent the engine will likely be trained</description>
            <param-name>retrain_threshold</param-name>
            <param-value>0.1</param-value>
        </context-param>
        <context-param>
            <description>minimum number of descriptions in database for training</description>
            <param-name>training_set_min</param-name>
            <param-value>10</param-value>
        </context-param>   
        ...
    </web-app>
```
There are several parameters in the web.xml file that can be configured. These are:

 - *username_mysql* - this is the '''username''' for accessing the MySQL database.
 - *password_mysql* - this is the '''password''' for accessing the MySQL database.
 - *training_set_min* - this parameter sets the '''minimum number''' of descriptions stored in the repository that are required for the training process to be executed.
 - *retrain_threshold* - this threshold value (between 0 and 1) is the '''ratio''' of new concepts (i.e. samples received after the training process) to the the number of old concepts (i.e. samples used in the training process). The lower the value, the more likely the engine will be re-trained more frequently, which becomes more resource-intensive as the repository grows.

For terminologies relating to the parameters ''*training_set_min*'' and ''*retrain_threshold*'' such as "concept", please refer to the subsection on IoT Search Engine in the [Open Specification](http://forge.fi-ware.org/plugins/mediawiki/wiki/fiware/index.php/FIWARE.OpenSpecification.IoT.Backend.IoTDiscovery#IoT_Search_Engine_.28Probabilistic.29) of the IoT Discovery GE.

### Run Tomcat

The last step is to run the Tomcat server. In a console terminal enter the following command:

 - Ubuntu: 

``` $ sudo service tomcat7 start ```

 - Windows: 
``` /CATALINA_HOME/bin/startup.bat ```

**PLEASE NOTE**: if MySQL is not setup correctly, the Sense2Web Platform will not run. This means making sure the MySQL server is running, the tables are created, and the username and passwords in the web.xml are valid for access to your MySQL server.


[Oracle Java]: http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
[Oracle Java Ubuntu default]: http://www.wikihow.com/Upgrade-Oracle-Java-on-Ubuntu-Linux
[Oracle Java Ubuntu webupd8]: http://www.webupd8.org/2012/09/install-oracle-java-8-in-ubuntu-via-ppa.html
[Apache Tomcat]: http://tomcat.apache.org/download-70.cgi
[db4o]: http://www.db4o.com/
[MySQL]: http://mysql.com/
[UPG]: http://forge.fiware.org/plugins/mediawiki/wiki/fiware/index.php/Configuration_Manager_-_IoT_Discovery_-_User_and_Programmers_Guide#NGSI-9_API
[db4o-install]: http://db4o-object-manager.software.informer.com/download/?ca4611a
> Written with [StackEdit](https://stackedit.io/).
