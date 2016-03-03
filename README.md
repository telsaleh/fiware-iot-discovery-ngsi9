# IoT Discovery 
### Registration and Discovery of IoT entities

**This project is part of  [FIWARE](http://fiware.org)**.  

Check out IoT Discovery from the FIWARE [catalogue](http://catalogue.fiware.org/enablers/iot-discovery) for more info and resources.  

[![License badge](https://img.shields.io/badge/license-AGPL-blue.svg)](https://opensource.org/licenses/AGPL-3.0)

* [Introduction](#introduction)
* Source  
 * [Dependencies](#dependencies)  
 * [Build](#build)  
* [Docker](#docker)
* [License](#licence)
* Setup   
 * [Installation](doc/manuals/install/install.md)  
 * [Deploy](doc/manuals/install/install.md#configuration-and-deployment)  
 * [End-to-end Testing (Sanity check)](doc/manuals/admin/admin.md#sanity-check-procedures)  
 * [Diagnostics](doc/manuals/admin/admin.md#diagnosis-procedures)  
* Usage  
 * [User](doc/manuals/user/user.md)  
 * [Programmer](doc/manuals/programmer/programmer.md)  
 * [ API Reference Documentation](http://docs.ngsi9.apiary.io/#) 
 * [IoT Discovery GE Open Specification](http://forge.fiware.org/plugins/mediawiki/wiki/fiware/index.php/FIWARE.OpenSpecification.IoT.Backend.IoTDiscovery)

##Introduction

This is the reference implementation for IoT Discovery GE. Its role is to act as a meeting point for IoT Context Producers to register the availability of their Things and Sensor devices, and IoT Context Consumers to discover them, using either the OMA NGSI-9 messaging protocol – a simple but powerful API for contextual information exchange, or the Sense2Web API that supports Linked Open Data. 

The primary purpose is to allow context producers to register their IoT Objects in linked-data format, and in turn allow context consumers to discover them using a set of search techniques.

This is a reference implementation for the IoT Discovery GE open specification. The implementation provide two modules, the NGSI-9 Server and the Sense2Web Platform. Both modules serve as a service discovery mechanism (SDM) for IoT Descriptions. An SDM is analogous to a registry or directory, and can be seen as a "yellow pages" for IoT entities, whereby you can discover information about the IoT entity, such as what attributes you can query about, and metadata about those attributes which provide more detailed information about it. It also provide information on how to reach it. It allows users to discover or check what is available and know where actual context sources are, and avoid unnecessary network overload of IoT context providers, especially if the context provider have constrained resources, such as gateways, or any wireless device.

The API exposes two main modules:   

* NGSI-9 server  
* Sense2Web linked-data platform  

![iot-discovery-gei-arch]( http://forge.fiware.org/plugins/mediawiki/wiki/fiware/images/f/fa/Gei-overview-latest.png)

The NGSI-9 server provides a repository for the storage of NGSI entities and allows NGSI-9 clients to:   

* Register context information about Sensors and Things.  
* Discover context information using ID, attribute, attribute domain, and entity type.

NGSI-9 clients include other FIWARE GEs, such as the Data Handling GE and the Device Management GE for registration, and the IoT Broker for discovery. The NGSI-9 server handles IoT Description defined using the NGSI Context Entity model and is compliant with the FIWARE IoT architecture. 

![FIWARE IoT Architecture](http://forge.fiware.org/plugins/mediawiki/wiki/fiware/images/0/08/FIWARE_IoT_R4_arch_v3.png)

The Sense2Web platform is a **standalone** module provides a semantic repository for IoT providers to register and manage semantic descriptions (in RDF/OWL) about their "Things", whether they be Sensor/Actuator Devices, virtual computational elements (e.g. data aggregators) or virtual representations of any Physical Entity. 
In turn, it provides IoT Users to discover these registered IoT elements by: 

- Retrieval of IoT Descriptions in RDF 
- Semantic querying via a SPARQL Endpoint
- An association mechanism that associates Things and Sensor/Actuator devices based on their shared attribute (e.g. temperature) and spatial proximity, which can then be queried via SPARQL.
- A probabilistic search mechanism that provides recommended and ranked search results for queries that
   don’t provide exact matching property values.

The platform currently supports IoT Descriptions based on the IoT-A Project (http://iot-a.eu), but can be extended to support other types. 
A Web User interface is provided for users to create, read, update and delete (CRUD) semantically-annotated IoT Descriptions, and also link them to other Linked Open Data (LOD) resources on the Web. 

## Dependencies  
* Oracle Java 8 JRE/JDK  
* Maven 2  
* Apache Tomcat 7/8  
* MySQL server  

## Build  

Project is built via Maven  
```
git clone https://github.com/UniSurreyIoT/fiware-iot-discovery-ngsi9.git  
mvn clean install  
```

## Docker  

If you have a Docker container installed, then you can deploy the GEi using the command:  
```
docker pull telsaleh/fiware-iot-discovery-ngsi9  

```

## License  

FIWARE IoT Discovery is licensed under the GNU Affero General Public License Version 3 ([AGPLv3](http://www.gnu.org/licenses/agpl-3.0.en.html)).