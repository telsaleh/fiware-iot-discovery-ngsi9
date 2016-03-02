### IoT Discovery: Registration and Discovery of IoT entities  

**This project is part of  [FIWARE](http://fiware.org)**.  

Check out the FIWARE catalogue [page](http://catalogue.fiware.org/enablers/iot-discovery) for more info and resources.  

## Index

* [Overview](#overview)

* Installation and Administration
    - [Install](doc/manuals/install/install.md)  
    - [Deploy](doc/manuals/install/install.md#configuration-and-deployment)  
    - [Sanity checking](doc/manuals/admin/admin.md#sanity-check-procedures)  
    - [Diagnosing](doc/manuals/admin/admin.md#diagnosis-procedures)   
       
* User & Programmers Guide    
 * [User](doc/manuals/user/user.md)  
 * [Programmer](doc/manuals/programmer/programmer.md)  

## Overview  
The reference implementation for IoT Discovery GE. Its role is to act as a meeting point for IoT Context Producers to register the availability of their Things and Sensor devices, and IoT Context Consumers to discover them, using either the OMA NGSI-9 messaging protocol – a simple but powerful API for contextual information exchange, or the Sense2Web API that supports Linked Open Data. 

The primary purpose is to allow context producers to register their IoT Objects in linked-data format, and in turn allow context consumers to discover them using a set of search techniques.

This is a reference implementation for the IoT Discovery GE open specification. The implementation provide two modules, the NGSI-9 Server and the Sense2Web Platform, in which both provide support for the registration and discovery of IoT entities. Please refer to the Users and programmers guide for more information.

[IoT Discovery GE Open Specification](http://forge.fiware.org/plugins/mediawiki/wiki/fiware/index.php/FIWARE.OpenSpecification.IoT.Backend.IoTDiscovery)

The API exposes two main modules:   

* NGSI-9 Server  
* Sense2Web Linked-data platform  

### NGSI-9 Server  
The server provides a repository for the storage of NGSI entities and allows NGSI-9 clients to: 
Register context information about Sensors and Things.
Discover context information using ID, attribute, attribute domain, and entity type.
NGSI-9 clients include other FIWARE GEs, such as the Data Handling GE and the Device Management GE for registration, and the IoT Broker for discovery. 

### Sense2Web  
A platform which provides a semantic repository for IoT providers to register and manage semantic descriptions (in RDF/OWL) about their "Things", whether they be Sensor/Actuator Devices, virtual computational elements (e.g. data aggregators) or virtual representations of any Physical Entity. 
In turn, it provides IoT Users to discover these registered IoT elements using: 

- Retrieval of IoT Descriptions in RDF 
- Semantic querying via a SPARQL Endpoint
- An association mechanism that associates Things and Sensor/Actuator devices based on their shared attribute (e.g. temperature) and spatial proximity, which can then be queried via SPARQL.
- A probabilistic search mechanism that provides recommended and ranked search results for queries that
   don’t provide exact matching property values.

The platform currently supports IoT Descriptions based on the IoT-A Project (http://iot-a.eu), but can be extended to support other types. 
A Web User interface is provided for users to create, read, update and delete (CRUD) semantically-annotated IoT Descriptions, and also link them to other Linked Open Data (LOD) resources on the Web. 