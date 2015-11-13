

# Introduction

Welcome to the Installation and Administration Guide for the IoT Discovery GE Implementation (GEi) - IoT Discovery. The online documents are being continuously updated and improved, and will therefore be the most appropriate place to get the most up-to-date information on installation and administration. The main components for installation the GEi are 2 Java WAR files that are deployed on Apache Tomcat as server modules, and a backend MySQL database.

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
	 - <pre>$ sudo apt-get install mysql-server</pre>
 - [MySQL Community Workbench 5.2](http://www.mysql.com/downloads/workbench/) (Optional)
	 - MySQL Workbench can be installed on Ubuntu by running the following command: <pre>$ sudo apt-get install mysql-workbench</pre>

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
Alternatively, to do this step directly from the command-line, connect to MySQL using: <pre>$ mysql -u {username} -p</pre>
Enter the password, then run the script included in the GEi package using the command: <pre>$ mysql < \ . {scriptname} </pre>

#### Option 2: MySQL workbench
To do this, run the MySQL workbench by entering the following command: 
<pre>$ mysql-workbench</pre>
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
 - In Ubuntu, the command line below can be used: <pre>jar -xvf  ngsi9.war</pre>
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

<pre>$ sudo service tomcat7 start</pre>

 - Windows: 
<pre> /CATALINA_HOME/bin/startup.bat</pre>

**PLEASE NOTE**: if MySQL is not setup correctly, the Sense2Web Platform will not run. This means making sure the MySQL server is running, the tables are created, and the username and passwords in the web.xml are valid for access to your MySQL server.

# System Administration

## Monitoring

The GEi can be monitored by opening another terminal and entering the command in the directory: 
<pre>$ tail -f /CATALINA_HOME/logs/catalina.out</pre>

## Logging

For accessing logs, they can be found in:
<pre>
/CATALINA_HOME/logs/catalina.{date}.log
/CATALINA_HOME/logs/localhost.{date}.log
</pre>

These can be used to check if deployment was successful, and also any issues with Servlets processing requests. The ''date'' value reflects the date of the log, which is created on a daily basis.

## Sanity Check Procedures
The Sanity Check Procedure is the first step that a System Administrator will do to verify that the GEi is well installed and ready to be tested. This is therefore a preliminary set of tests to ensure that obvious or basic malfunctioning is fixed before proceeding to unit tests, integration tests and user validation.
The first step is to check the databases, and then the end-to-end testing.

## End to End testing

Using a web browser or REST client, contact the GEi via HTTP on: 
<pre> http://{serverRoot}/s2w/repository/getVersion/ </pre>
<pre>http://{serverRoot}/ngsi9/sanityCheck/ </pre>

The response should be:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<sanityCheck>
<name>IoT Discovery</name>
<type>Sanity Check</type>
<version>Version: 4.1.SNAPSHOT</version>
</sanityCheck>
```
This means that the Sanity Check is passed and the GEi is correctly deployed. If you get a **JAVA INTERNAL ERROR** or **405 METHOD NOT SUPPORTED** it means that the GEi is not correctly deployed.

### NGSI-9 Server
#### Test 1
Verify that http://{serverRoot}/ngsi9 can be reached and returns the following web page:
![ngsi9-index](http://forge.fiware.org/plugins/mediawiki/wiki/fiware/images/a/ab/S2W-ngsi9-index.png)

This page contains information about the version of the GEi and which operations are supported on the current release.

#### Test 2
After that it is possible to test one of the supported NGSI-9 resources. For example let us try to send an **HTTP GET** to the ''contextEntity/EntityId'' resource. 
<pre>http://{serverRoot}/ngsi9/contextEntities/Kitchen</pre>

The response should be a **discoverContextAvailabilityResponse** response in XML, with the ERROR CODE:
```xml
   <discoverContextAvailabilityResponse>
     <errorCode>
     <code> 404 </code>
     <reasonPhrase>CONTEXT ELEMENT NOT FOUND</reasonPhrase>
     </errorCode>
   </discoverContextAvailabilityResponse>
```
### Sense2Web

#### Test 1
Verify that `http://{serverRoot}/s2w/` can be reached and returns the following page:
![s2w-homepage](http://forge.fiware.org/plugins/mediawiki/wiki/fiware/images/6/6e/S2W-homepage.png)

#### Test 2
The RESTful interface for the Sense2Web platform can be tested by sending a HTTP GET request using the following URL: <pre>http://{serverRoot}/s2w/repository/lookup/iot-a/resource/ALL</pre>
The result should be an RDF document with all the descriptions in the **Resource** repository.
If you get this error this means that the Entity that you are requesting is not available.


## List of Running Processes

* java
* mysqld

## Network interfaces Up & Open

 - TCP: 8080 (default) 
 - TCP: 3306

## Databases
+ [db4o][db4o] for the NGSI-9 Server.
+ [MySQL][MySQL] for the Sense2Web Platform.

### Check db4o
The database will be created upon the first NGSI-9 registration. Please refer to the [User And Programmers guide][UPG] on how to register a description via NGSI-9.

To check the database, you will need to use the Eclipse IDE. Download the [db4o][db4o-install] package, unzip it, and go to **/ome** folder and follow the instructions in **readme.html** to install the Object Manager Enterprise plugin for Eclipse.
Once this is done, go to the OME perspective in the IDE and you should have this view.

![db4o OME Connect to Database](http://forge.fiware.org/plugins/mediawiki/wiki/fiware/images/3/39/Db4o-ome.png)

- In the menu, go to '''OME->Connect/Disconnect DB'''.
- Enter the location of the db4o file. This should point to the ```/{CATALINA}/webapps/ngsi9/repository``` folder.
	- Please note that if the databse connection fails, this could be due to the NGSI-9 Server locking it. In this case, stop the NGSI-9 webapp in the tomcat server.
- Once connected, you should see a list of objects in the db4o browser, after the first NGSI-9 registration has occurred.
- Look for **RegisterContextRequest**, right-click it, and in the context menu choose ***view all objects***.
![db4o OME Browser](http://forge.fiware.org/plugins/mediawiki/wiki/fiware/images/4/45/Db4o-ome-browse1.png)

* On the right-hand side in the Query Results tab, you can see the available registrations. If you choose one, the object structure of the registration will show below.

### Check MySQL
To test that the script that was run during setup has been successful, enter the following commands in the mysql command line client.

#### Test 1a

<pre>mysql>desc resourcedb.Triples;</pre>
<pre>mysql>desc entitydb.Triples;</pre>
<pre>mysql>desc servicedb.Triples;</pre>
Each result should present a table with the following columns which shows the structure of the table.

<pre>
+-------+---------+------+-----+---------+-------+
| Field | Type    | Null | Key | Default | Extra |
+-------+---------+------+-----+---------+-------+
| s     | int(11) | NO   | PRI | NULL    |       |
| p     | int(11) | NO   | PRI | NULL    |       |
| o     | int(11) | NO   | PRI | NULL    |       |
+-------+---------+------+-----+---------+-------+
3 rows in set (0.01 sec)

</pre>
#### Test 1b
<pre>mysql>desc resourcedb.Nodes;</pre>

<pre>mysql>desc entitydb.Nodes;</pre>

<pre>mysql>desc servicedb.Nodes;</pre>
Each result should present a table with the following columns which shows the structure of the table.
<pre>
+----------+------------------+------+-----+---------+----------------+
| Field    | Type             | Null | Key | Default | Extra          |
+----------+------------------+------+-----+---------+----------------+
| id       | int(10) unsigned | NO   | PRI | NULL    | auto_increment |
| hash     | bigint(20)       | NO   | UNI | 0       |                |
| lex      | longtext         | YES  |     | NULL    |                |
| lang     | varchar(10)      | NO   |     |         |                |
| datatype | varchar(200)     | NO   |     |         |                |
| type     | int(10) unsigned | NO   |     | 0       |                |
+----------+------------------+------+-----+---------+----------------+
6 rows in set (0.00 sec)
</pre>

#### Test 1c
<pre>mysql>desc resourcedb.Prefixes;</pre>
<pre>mysql>desc entitydb.Prefixes;</pre>
<pre>mysql>desc servicedb.Prefixes;</pre>

Each result should present a table with the following columns which shows the structure of the table.
<pre>
+--------+--------------+------+-----+---------+-------+
| Field  | Type         | Null | Key | Default | Extra |
+--------+--------------+------+-----+---------+-------+
| prefix | varchar(50)  | NO   | PRI | NULL    |       |
| uri    | varchar(500) | NO   |     | NULL    |       |
+--------+--------------+------+-----+---------+-------+
2 rows in set (0.00 sec)
</pre>

#### Test 1d
<pre>mysql>desc resourcedb.Quads;</pre>
<pre>mysql>desc entitydb.Quads;</pre>
<pre>mysql>desc servicedb.Quads;</pre>

Each result should present a table with the following columns which shows the structure of the table.
<pre>
+-------+---------+------+-----+---------+-------+
| Field | Type    | Null | Key | Default | Extra |
+-------+---------+------+-----+---------+-------+
| g     | int(11) | NO   | PRI | NULL    |       |
| s     | int(11) | NO   | PRI | NULL    |       |
| p     | int(11) | NO   | PRI | NULL    |       |
| o     | int(11) | NO   | PRI | NULL    |       |
+-------+---------+------+-----+---------+-------+
4 rows in set (0.00 sec)
</pre>

#### Test 2a

If you already have registered a description you can check if it has been stored in the database using the following tests:
<pre>mysql> \G SELECT * FROM resourcedb.Triples LIMIT 10;</pre>

<pre>mysql> \G SELECT * FROM entitydb.Triples LIMIT 10;</pre>

<pre>mysql> \G SELECT * FROM servicedb.Triples LIMIT 10;</pre>

The result should present a table with the following columns. The tables will be empty when newly created, but will be populated as new description are registered. The table defines the mappings between RDF '''triples''' in the semantic repository. 
<pre>
+-----+-----+------+
| s   | p   | o    |
+-----+-----+------+
|  46 |  46 |  315 |
|  46 |  46 | 2793 |
|  46 | 171 |  305 |
|  48 |  46 |  315 |
|  48 |  46 | 2793 |
|  48 | 171 |  193 |
| 157 |  46 |   47 |
| 157 |  48 |   60 |
| 157 |  50 |   60 |
| 157 |  51 |   60 |
+-----+-----+------+
10 rows in set (0.00 sec)
</pre>

#### Test 2b
<pre>mysql> \G SELECT * FROM resourcedb.Nodes LIMIT 10;</pre>
<pre>mysql> \G SELECT * FROM entitydb.Nodes LIMIT 10;</pre>
<pre>mysql> \G SELECT * FROM servicedb.Nodes LIMIT 10;</pre>

The result should present a table with the following columns. The tables will be empty when newly created, but will be populated as new description are registered. The table defines the mappings between RDF '''nodes''' in the semantic repository.
<pre>
+-----+----------------------+----------------------------------------------------------------+------+----------+------+
| id  | hash                 | lex              					      | lang | datatype | type |
+-----+----------------------+----------------------------------------------------------------+------+----------+------+
| 109 |  5596883189078519408 | http://www.surrey.ac.uk/ccsr/IoT-A/EntityModel.owl#            |      |          |    2 |
| 110 | -6430697865200335348 | http://www.w3.org/1999/02/22-rdf-syntax-ns#type                |      |          |    2 |
| 111 | -2401239393240133156 | http://www.surrey.ac.uk/ccsr/IoT-A/EntityModel.owl#entity      |      |          |    2 |
| 112 |  6454844767405606854 | http://www.w3.org/2000/01/rdf-schema#label              	      |      |          |    2 |
| 113 |  7196782991867380714 |              						      |      |          |    3 |
| 114 | -7436251478343769787 | http://www.surrey.ac.uk/ccsr/IoT-A/EntityModel.owl#hasentityID |      |          |    2 |
| 115 | -5872643041377611799 | http://www.surrey.ac.uk/ccsr/IoT-A/EntityModel.owl#hasName     |      |          |    2 |
| 116 |  2122874005417209587 | http://www.surrey.ac.uk/ccsr/IoT-A/EntityModel.owl#hasTag      |      |          |    2 |
| 117 | -8874962510547211933 | http://www.surrey.ac.uk/ccsr/IoT-A/EntityModel.owl#hasURITag   |      |          |    2 |
| 118 | -8028859106695045829 | http://www.surrey.ac.uk/ccsr/IoT-A/EntityModel.owl#hasTimeZone |      |          |    2 |
+-----+----------------------+----------------------------------------------------------------+------+----------+------+
</pre>

#### Test 2c
<pre>mysql> \G SELECT * FROM resourcedb.Prefixes LIMIT 10;</pre>
<pre>mysql> \G SELECT * FROM entitydb.Prefixes LIMIT 10;</pre>
<pre>mysql> \G SELECT * FROM servicedb.Prefixes LIMIT 10;</pre>
The result should present a table with the following columns. The tables will be empty when newly created, but will be populated as new description are registered. The table defines the mappings between RDF '''prefixes''' in the semantic repository.
<pre>
+----------------+--------------------------------------------------------------------+
| prefix         | uri      |
+----------------+--------------------------------------------------------------------+
| :              | http://www.surrey.ac.uk/ccsr/ontologies/VirtualEntityInstance.owl# |
| EntityModel:   | http://www.surrey.ac.uk/ccsr/IoT-A/EntityModel.owl#                |
| ResourceModel: | http://www.surrey.ac.uk/ccsr/IoT-A/ResourceModel.owl#              |
| owl2xml:       | http://www.w3.org/2006/12/owl2-xml#                                |
| owl:           | http://www.w3.org/2002/07/owl#                                     |
| protege:       | http://protege.stanford.edu/plugins/owl/protege#                   |
| rdf:           | http://www.w3.org/1999/02/22-rdf-syntax-ns#                        |
| rdfs:          | http://www.w3.org/2000/01/rdf-schema#                              |
| swrl:          | http://www.w3.org/2003/11/swrl#                                    |
| swrlb:         | http://www.w3.org/2003/11/swrlb#                                   |
+----------------+--------------------------------------------------------------------+
10 rows in set (0.01 sec)
</pre>

#### Test 2d
<pre>mysql> \G SELECT * FROM resourcedb.Quads LIMIT 10;</pre>

<pre>mysql> \G SELECT * FROM entitydb.Quads LIMIT 10;</pre>

<pre>mysql> \G SELECT * FROM servicedb.Quads LIMIT 10;</pre>

The result should present a table with the following columns. The tables will be empty when newly created, but will be populated as new description are registered. The table defines the mappings between RDF '''quads''' in the semantic repository. 

<pre>
+-----+-----+-----+------+
   g  | s   |  p  | o    |
+-----+-----+-----+------+
| 23  |  46 |  46 |  315 |
| 56  |  46 |  46 | 2793 |
| 76  |  46 | 171 |  305 |
| 12  |  48 |  46 |  315 |
| 34  |  48 |  46 | 2793 |
| 67  |  48 | 171 |  193 |
| 31  | 157 |  46 |   47 |
| 22  | 157 |  48 |   60 |
| 78  | 157 |  50 |   60 |
| 18  | 157 |  51 |   60 |
+-----+-----+-----+------+
10 rows in set (0.00 sec)
</pre>

## Diagnosis Procedures
The Diagnosis Procedures are the first steps that a System Administrator will take to locate the source of an error in a GE. Once the nature of the error is identified with these tests, the system admin will very often have to resort to more concrete and specific testing to pinpoint the exact point of error and a possible solution. Such specific testing is out of the scope of this section.

## Resource availability
* RAM
 * recommended: 4GB
* HARD DISK
 * minimum: 5GB
 * recommended: 15GB
* CPU:
 * recommended: 2x2.8GHz

##Remote Service Access

* TCP connection exposed for invocation from IoT GEs to NGSI-9 server on port 8080 (default).
* TCP connection exposed for invocation from Semantic GEs and Applications to Sense2Web platform on port 8080 (default).

## Resource consumption

### **Tomcat Server**

- Process
 - java.exe - org.apache.catalina.startup.Bootstrap start
 - VM config: 
 <pre>-XX:MaxPermSize=512M -Xms2048M -Xmx2048M -Xss64M -XX:+CMSClassUnloadingEnabled -XX:+CMSPermGenSweepingEnabled</pre>
- Memory consumption 
 - Committed: 2GB
- Threads
 - Live threads: 28
 - Peak: 29
 - Daemon threads: 27
- CPU:
 - idle: 0%
 - active: ~5% (max)
### **MySQL**

* Process 
 * mysqld
* Memory consumption
 - Committed: 100MB
 * Typical: 9MB
 * Threads
 * Live threads: 46
* CPU:
 - idle: 0%
 - active: ~2% (max)

## I/O flows
The only expected I/O flow is of type HTTP, on the port set for the Tomcat Server (default is 8080).

[Oracle Java]: http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
[Oracle Java Ubuntu default]: http://www.wikihow.com/Upgrade-Oracle-Java-on-Ubuntu-Linux
[Oracle Java Ubuntu webupd8]: http://www.webupd8.org/2012/09/install-oracle-java-8-in-ubuntu-via-ppa.html
[Apache Tomcat]: http://tomcat.apache.org/download-70.cgi
[db4o]: http://www.db4o.com/
[MySQL]: http://mysql.com/
[UPG]: http://forge.fiware.org/plugins/mediawiki/wiki/fiware/index.php/Configuration_Manager_-_IoT_Discovery_-_User_and_Programmers_Guide#NGSI-9_API
[db4o-install]: http://db4o-object-manager.software.informer.com/download/?ca4611a
> Written with [StackEdit](https://stackedit.io/).
