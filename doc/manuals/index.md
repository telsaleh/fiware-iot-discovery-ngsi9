IoT Discovery is the reference implementation of the IoT Discovery GE in Java.  

Its role is to act as a meeting point for IoT Context Producers to register the availability of their Things and Sensor devices, and IoT Context Consumers to discover them, using either the OMA NGSI-9 messaging protocol – a simple but powerful API for contextual information exchange, or the Sense2Web API that supports Linked Open Data.  

The API exposes two main modules:  
* NGSI-9 Server  
* Sense2Web Linked-data platform  


NGSI-9 Server  
The server provides a repository for the storage of NGSI entities and allows NGSI-9 clients to:  
* Register context information about Sensors and Things.  
* Discover context information using ID, attribute, attribute domain, and entity type.  

NGSI-9 clients include other FIWARE GEs, such as the Data Handling GE and the Device Management GE for registration, and the IoT Broker for discovery.  

Sense2Web  
A platform which provides a semantic repository for IoT providers to register and manage semantic descriptions (in RDF/OWL) about their "Things", whether they be Sensor/Actuator Devices, virtual computational elements (e.g. data aggregators) or virtual representations of any Physical Entity.  
In turn, it provides IoT Users to discover these registered IoT elements using:  
* retrieve descriptions in RDF
* A probabilistic search mechanism that provides recommended and ranked search results for queries that don’t provide exact matching property values.
* Semantic querying via SPARQL
* An association mechanism that associates Things and sensors based on their shared attribute (e.g. temperature) and spatial proximity, which can then be queried via SPARQL.
