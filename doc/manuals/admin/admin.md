# Introduction

# Monitoring

The GEi can be monitored by opening another terminal and entering the command in the directory:  
``` $ tail -f /CATALINA_HOME/logs/catalina.out ```

# Logging

For accessing logs, they can be found in:  
```
/CATALINA_HOME/logs/catalina.{date}.log
/CATALINA_HOME/logs/localhost.{date}.log
```

These can be used to check if deployment was successful, and also any issues with Servlets processing requests. The ''date'' value reflects the date of the log, which is created on a daily basis.

# Sanity Check Procedures
The Sanity Check Procedure is the first step that a System Administrator will do to verify that the GEi is well installed and ready to be tested. This is therefore a preliminary set of tests to ensure that obvious or basic malfunctioning is fixed before proceeding to unit tests, integration tests and user validation.
The first step is to check the databases, and then the end-to-end testing.

# End to End testing

Using a web browser or REST client, contact the GEi via HTTP on: 
``` http://{serverRoot}/s2w/repository/getVersion/ ```
``` http://{serverRoot}/ngsi9/sanityCheck/ ```

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

## NGSI-9 Server
### Test 1
Verify that http://{serverRoot}/ngsi9 can be reached and returns the following web page:
![ngsi9-index](http://forge.fiware.org/plugins/mediawiki/wiki/fiware/images/a/ab/S2W-ngsi9-index.png)

This page contains information about the version of the GEi and which operations are supported on the current release.

### Test 2
After that it is possible to test one of the supported NGSI-9 resources. For example let us try to send an **HTTP GET** to the ''contextEntity/EntityId'' resource. 
``` http://{serverRoot}/ngsi9/contextEntities/Kitchen ```

The response should be a **discoverContextAvailabilityResponse** response in XML, with the ERROR CODE:
```xml
   <discoverContextAvailabilityResponse>
     <errorCode>
     <code> 404 </code>
     <reasonPhrase>CONTEXT ELEMENT NOT FOUND</reasonPhrase>
     </errorCode>
   </discoverContextAvailabilityResponse>
```
## Sense2Web

### Test 1
Verify that `http://{serverRoot}/s2w/` can be reached and returns the following page:
![s2w-homepage](http://forge.fiware.org/plugins/mediawiki/wiki/fiware/images/6/6e/S2W-homepage.png)

### Test 2
The RESTful interface for the Sense2Web platform can be tested by sending a HTTP GET request using the following URL:  
``` http://{serverRoot}/s2w/repository/lookup/iot-a/resource/ALL ```
The result should be an RDF document with all the descriptions in the **Resource** repository.
If you get this error this means that the Entity that you are requesting is not available.


# List of Running Processes

* java
* mysqld

# Network interfaces Up & Open

 - TCP: 8080 (default) 
 - TCP: 3306

# Databases
+ [db4o][db4o] for the NGSI-9 Server.
+ [MySQL][MySQL] for the Sense2Web Platform.

## Check db4o
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

## Check MySQL
To test that the script that was run during setup has been successful, enter the following commands in the mysql command line client.

### Test 1a

``` mysql>desc resourcedb.Triples; ```  
``` mysql>desc entitydb.Triples; ```  
``` mysql>desc servicedb.Triples; ```  
Each result should present a table with the following columns which shows the structure of the table.

```
+-------+---------+------+-----+---------+-------+
| Field | Type    | Null | Key | Default | Extra |
+-------+---------+------+-----+---------+-------+
| s     | int(11) | NO   | PRI | NULL    |       |
| p     | int(11) | NO   | PRI | NULL    |       |
| o     | int(11) | NO   | PRI | NULL    |       |
+-------+---------+------+-----+---------+-------+
3 rows in set (0.01 sec)

```  
### Test 1b
``` mysql>desc resourcedb.Nodes; ```  

``` mysql>desc entitydb.Nodes; ```  

``` mysql>desc servicedb.Nodes; ```  
Each result should present a table with the following columns which shows the structure of the table.
``` 
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
```  

### Test 1c
``` mysql>desc resourcedb.Prefixes; ```
``` mysql>desc entitydb.Prefixes; ```
``` mysql>desc servicedb.Prefixes; ```

Each result should present a table with the following columns which shows the structure of the table.
``` 
+--------+--------------+------+-----+---------+-------+
| Field  | Type         | Null | Key | Default | Extra |
+--------+--------------+------+-----+---------+-------+
| prefix | varchar(50)  | NO   | PRI | NULL    |       |
| uri    | varchar(500) | NO   |     | NULL    |       |
+--------+--------------+------+-----+---------+-------+
2 rows in set (0.00 sec)
``` 

### Test 1d
``` mysql>desc resourcedb.Quads; ```  
``` mysql>desc entitydb.Quads; ```  
``` mysql>desc servicedb.Quads; ```  

Each result should present a table with the following columns which shows the structure of the table.
```  
+-------+---------+------+-----+---------+-------+
| Field | Type    | Null | Key | Default | Extra |
+-------+---------+------+-----+---------+-------+
| g     | int(11) | NO   | PRI | NULL    |       |
| s     | int(11) | NO   | PRI | NULL    |       |
| p     | int(11) | NO   | PRI | NULL    |       |
| o     | int(11) | NO   | PRI | NULL    |       |
+-------+---------+------+-----+---------+-------+
4 rows in set (0.00 sec)
```  

### Test 2a

If you already have registered a description you can check if it has been stored in the database using the following tests:
``` mysql> \G SELECT * FROM resourcedb.Triples LIMIT 10; ```  

``` mysql> \G SELECT * FROM entitydb.Triples LIMIT 10; ```  

``` mysql> \G SELECT * FROM servicedb.Triples LIMIT 10; ```  

The result should present a table with the following columns. The tables will be empty when newly created, but will be populated as new description are registered. The table defines the mappings between RDF '''triples''' in the semantic repository. 
```  
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
```  

### Test 2b
``` mysql> \G SELECT * FROM resourcedb.Nodes LIMIT 10; ```  
``` mysql> \G SELECT * FROM entitydb.Nodes LIMIT 10; ```  
``` mysql> \G SELECT * FROM servicedb.Nodes LIMIT 10; ```   

The result should present a table with the following columns. The tables will be empty when newly created, but will be populated as new description are registered. The table defines the mappings between RDF '''nodes''' in the semantic repository.
```  
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
```  

### Test 2c
``` mysql> \G SELECT * FROM resourcedb.Prefixes LIMIT 10; ```  
``` mysql> \G SELECT * FROM entitydb.Prefixes LIMIT 10; ```  
``` mysql> \G SELECT * FROM servicedb.Prefixes LIMIT 10; ```
The result should present a table with the following columns. The tables will be empty when newly created, but will be populated as new description are registered. The table defines the mappings between RDF '''prefixes''' in the semantic repository.
```  
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
```  

### Test 2d
``` mysql> \G SELECT * FROM resourcedb.Quads LIMIT 10; ```  

``` mysql> \G SELECT * FROM entitydb.Quads LIMIT 10; ```  

``` mysql> \G SELECT * FROM servicedb.Quads LIMIT 10; ```  

The result should present a table with the following columns. The tables will be empty when newly created, but will be populated as new description are registered. The table defines the mappings between RDF '''quads''' in the semantic repository. 

```  
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
```  

# Diagnosis Procedures
The Diagnosis Procedures are the first steps that a System Administrator will take to locate the source of an error in a GE. Once the nature of the error is identified with these tests, the system admin will very often have to resort to more concrete and specific testing to pinpoint the exact point of error and a possible solution. Such specific testing is out of the scope of this section.

# Resource availability
* RAM
 * recommended: 4GB
* HARD DISK
 * minimum: 5GB
 * recommended: 15GB
* CPU:
 * recommended: 2x2.8GHz

#Remote Service Access

* TCP connection exposed for invocation from IoT GEs to NGSI-9 server on port 8080 (default).
* TCP connection exposed for invocation from Semantic GEs and Applications to Sense2Web platform on port 8080 (default).

# Resource consumption

## **Tomcat Server**

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
## **MySQL**

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

# I/O flows
The only expected I/O flow is of type HTTP, on the port set for the Tomcat Server (default is 8080).

[Oracle Java]: http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
[Oracle Java Ubuntu default]: http://www.wikihow.com/Upgrade-Oracle-Java-on-Ubuntu-Linux
[Oracle Java Ubuntu webupd8]: http://www.webupd8.org/2012/09/install-oracle-java-8-in-ubuntu-via-ppa.html
[Apache Tomcat]: http://tomcat.apache.org/download-70.cgi
[db4o]: http://www.db4o.com/
[MySQL]: http://mysql.com/
[UPG]: http://forge.fiware.org/plugins/mediawiki/wiki/fiware/index.php/Configuration_Manager_-_IoT_Discovery_-_User_and_Programmers_Guide#NGSI-9_API
[db4o-install]: http://db4o-object-manager.software.informer.com/download/?ca4611a
