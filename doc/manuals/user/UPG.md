## User and Programmers Guide
# Introduction
This guide will explain how to interact with the IoT Discovery GEi and also the APIs that are exposed by it. The GEi itself contains two modules; the NGSI-9 Server module, and the Sense2Web Platform. Both modules serve as a service discovery mechanism (SDM) for IoT Descriptions. An SDM is analogous to a registry or directory, and can be seen as a "yellow pages" for IoT entities, whereby you can discover information about the IoT entity, such as what attributes you can query about, and metadata about those attributes which provide more detailed information about it. It also provide information on how to reach it. It allows users to discover or check what is available and know where actual context sources are, and avoid unnecessary network overload of IoT context providers, especially if the context provider have constrained resources, such as gateways, or any wireless device.

![Sense2Web Platform](http://forge.fiware.org/plugins/mediawiki/wiki/fiware/images/f/fa/Gei-overview-latest.png)
<center>Figure 1: NGSI-9 Server and Sense2Web Platform</center>

The NGSI-9 server handles IoT Description defined using the NGSI Context Entity model and is compliant with the FIWARE IoT architecture. This means it can be used in conjunction with other GEs that support the NGSI-9 interface, e.g. IoT Broker, Data Handling, and Backend Device Management GE.

![FIWARE IoT Arch](http://forge.fiware.org/plugins/mediawiki/wiki/fiware/images/0/08/FIWARE_IoT_R4_arch_v3.png)
<center>Figure 2: FIWARE IoT Architecture</center>

The Sense2Web Platform however, is currently a **standalone** module that allows users to register their semantic IoT descriptions. The reason that this module is standalone is due to the fact there are no other GEs in the FIWARE IoT Architecture that currently handle or deal with semantic descriptions.

## NGSI-9 Server
### Overview
The NGSI-9 server allows NGSI-9 clients to register and discover descriptions about the **availability** their IoT entities, which are based on the NGSI context entity model.
![NGSI Context Entity Model](http://forge.fiware.org/plugins/mediawiki/wiki/fiware/images/8/8f/Context-entity-model.png)
<center>Figure 3: NGSI Context Entity Model</center>

### Interfaces
The NGSI-9 server exposes interfaces for registration, discovery, subscription and notification. These functions are realized by two sets of HTTP-based operations; standard and convenience. Both provide the functions mentioned, but are done differently mainly when it comes to discovery. All standard operations use the POST method for requests, with the body containing an NGSI-9 message that relates to a particular function. For example, a register request has a ***RegisterContextRequest*** message in its body. For convenience operations, discovery requests are simplified by using the GET method, whereby the query is in the URL itself.
### Components
The main components of the NGSI-9 server is the NGSI-9 handler and the NGSI-9 store. The NGSI-9 handler acts as the configuration manager. The NGSI-9 handler is responsible for handling requests based the corresponding function, and also handling the representation of the request/response based on what the client sends and expects (currently XML or JSON). The NGSI-9 store is responsible for the storage of registrations and subscriptions, and querying the store based on a client's discovery request.
### Persistence
The persistence currently used is a object store. Db4o was selected for this.
### Interactions
The NGSI-9 server can interact with any FIWARE NGSI-9 compliant client. This includes IoT Agents (e.g. gateways) who have exposed a service endpoint for data/actuation provision via NGSI-10 (“data interface”), and want to announce the availability of their IoT entities. GEs in the FIWARE IoT architecture also can interact with the NGSI-9 Server. These include: 

* Data Handling GE: Register resources; sensing sources, actuators, processing elements (composite of sensing sources)
* Backend Device Management GE: Register sensing sources, aggregated sensing sources
* IoT Broker GE: Discover entities on behalf of the consumers. Retrieves, assembles, and processes information from the providers, and offers consumers a simple interface and masking the complexity and heterogeneity of the IoT.

## Sense2Web Platform
### Overview

This component addresses the discovery of Internet of Things (IoT) Objects, by providing a repository for IoT Context Producers to register their IoT Things, Resources, and Devices, using semantically-annotated Descriptions based on the [IoT-A][IoT-A] (Internet of Things Architecture) ontology [models][IoT-A models]. In turn, it provides a set of search mechanisms for their look-up and discovery. One of the main goals of this component is to make use of semantic annotation in order to apply formal naming and relational conventions to the description of an IoT Object, which is explicitly absent in NGSI-9/10.
The component makes use of the [Sense2Web][s2w-wiki-info] IoT Linked Data Platform baseline asset, which provides a repository for the CRUD (Create, Read, Update and Delete) management of semantic IoT descriptions, that complies with the IoT-A ontology models. Sense2Web can also associate different IoT object ontologies to domain data and other resources on the Web using Linked Open Data.

### Interfaces
This component provides a set of interfaces a user can interact with. The first is a Web User Interface (UI) whereby a user can perform CRUD operations on the IoT Descriptions, and also query the IoT Descriptions as well. When registering or updating, a user can either upload an IoT Description or complete a form which is then sent to the server to be converted to RDF, and storing it in the RDF database.
The second interface is a RESTful CRUD and SPARQL interface. This interface mainly supports M2M interactions. An application can also perform CRUD operations on the IoT descriptions in the repository, and query for a particular piece of information from the descriptions using SPARQL.
The third interface allows users to query about an IoT description using keywords or templates that share the same structure as the IoT description. This type of query input is handled by the IoT Search Engine, which will search for the relevant query.
### Components
The main components for this component are: 

* ***Search Engine***: a probabilistic search mechanism that is based on text analysis for indexing and searching.
* ***Associations Engine***: a semantic search mechanism that is used to establish and maintain associations between IoT Objects. 

## Background and Detail
This User and Programmers Guide relates to the IoT Discovery (Configuration Management) GE which is part of the [Internet of Things (IoT) Services Enablement chapter][iot-chap-wiki].
Please find more information about this Generic Enabler in the following [Open Specification][iotd-arch-wiki].

# User Guide

## NGSI-9 Server
The NGSI-9 Server is a server with a repository that allows the registration and discovery of NGSI-9 Context Entities. Interaction with the server can be done via a REST web service client, or a web browser using the a REST Client Plugin, e.g. Google Chrome with [Advanced REST Client][Advanced-REST-Client].
![REST client for NGSI-9](http://forge.fiware.org/plugins/mediawiki/wiki/fiware/images/e/ef/Ngsi9-register.png)

## Sense2Web Platform
Sense2Web is a platform for supporting semantic linked-open-data (LOD) descriptions that model IoT Resources, Entities and Services, which are based on the [IoT-A][IoT-A] ontologies. It provides a set of triple stores for the storage of the of these descriptions, and exposes a Web User Interface (UI) and RESTful interfaces for CRUD (Create/Read/Update/Delete) management. In turn, the platform provides a set of search mechanisms for their lookup and discovery, namely the Probabilistic Search Engine and the Associations Engine. Please refer to the IoT-A ontologies can be found in via this [link][iota-ontologies].

### Web User Interface
#### Home page
The Web UI can be accessed via `http://{serverRoot}/s2w`. The main page displays page links to the current features that the platform currently supports. Click on the associated icon for a particular feature.

![Sense2Web Homepage](http://forge.fiware.org/plugins/mediawiki/wiki/fiware/images/6/6e/S2W-homepage.png)

#### Register
This page provides the user to register an IoT description. The current ontologies that are supported are the IoT-A ontologies that define a '''Resource''', '''Virtual Entity''' and '''Service'''. Please refer to this [paper][iota-paper] for more details on the IoT-A ontologies. Registering can be done either by uploading a description file or by completing a form. The form is provided for entering values for the description properties. The form will not be accepted unless an ID, Name and Latitude/Longitude co-ordinates are at least entered. The tag field is used to provide a keyword for the description. The linked-data tag is used to link the description with another description, which can be used to provide more detailed information about a particular element in the description. Currently linked-data tags can only be retrieved from “dbpedia.com”. To retrieve a linked-data tag the user must enter a keyword. Then double-clicking the text box will trigger the retrieval of a set of linked-data tag to choose from. The search results can be refined by specifying a topic and also by limiting the number of results. The local and global location fields also provide linked-data descriptions that can be associated with a particular location. Location can also be entered in terms of co-ordinates and altitude. A Google mini-map can be used to assist in entering the co-ordinates. To select a point in the map, just simply click on the point required, or alternatively the marker can be dragged to the point required. Once the form is submitted the page will return links for viewing the RDF result of the submission in various formats i.e. **RDF/XML**, **RDF/XML-ABBREV**, **RDF/JSON**, **N3**, **N-TRIPLE** and **TURTLE**. If the ID entered for this description is the same as one already available in the repository, then an error message is returned.

![Sense2Web Register](http://forge.fiware.org/plugins/mediawiki/wiki/fiware/images/2/20/S2W-register.png)

#### Lookup
To lookup a description, the relevant repository needs to be selected i.e. '''Resource''', '''Entity''', '''Service'''. The ID of the description in question must then be entered.
![Sense2Web Lookup](http://forge.fiware.org/plugins/mediawiki/wiki/fiware/images/5/56/S2W-lookup.png)

#### Update
The Update page is similar to the Register page with the exception that the current values for a description can be retrieved and populated into the fields by entering the ID of the description in question, and clicking on the "'''retrieve'''" button.
![Sense2Web Update](http://forge.fiware.org/plugins/mediawiki/wiki/fiware/images/8/83/S2W-update.png)

#### Delete
To delete a description, the relevant repository needs to be selected i.e. '''Resource''', '''Entity''', '''Service'''. The ID of the description in question must then be entered.

![Sense2Web Delete](http://forge.fiware.org/plugins/mediawiki/wiki/fiware/images/5/51/S2W-delete.png)

#### Query
The platform supports SPARQL for querying IoT descriptions. When choosing a particular type of description, the respective SPARQL template is provided for a user to use and edit. The template includes the properties of a description type. In the case of finding associations between Entities and Services, the OPTION field in the SPARQL template for the Entity can be used to retrieve IoT Service Decription URI, which can then be used by a user/application to retrieve information on how to to reach a Service Endpoint that provides information that is currently relevant to the attributes of an Entity e.g. a Service of a temperature sensor (i.e. the Resource) attached to a mobile target that is currently in the vicinity of a room (i.e. the Entity in question).

![Sense2Web Query](http://forge.fiware.org/plugins/mediawiki/wiki/fiware/images/1/1e/S2W-discover.png)

#### Discover
In the case where a user does not know the description or the exact naming for its attributes that the user is looking for, the probabilistic search engine can be used to provide recommended and ranked suggestion for a description relevant to the search input. Here the user should enter a keyword for as many fields as required.
![Sense2Web Discover](http://forge.fiware.org/plugins/mediawiki/wiki/fiware/images/1/1e/S2W-discover.png)

#### Locate

A simple map application is provided to show the location of a '''Resource''' or '''Entity'''. Clicking on a particular Object will display its main properties and a link to its description.

![Sense2Web Locate](http://forge.fiware.org/plugins/mediawiki/wiki/fiware/images/2/2c/S2W-locate.png)

# Programmers Guide

The GEi is meant to be used as a web service, and therefore users and applications can interact with the GEi via two sets of RESTful interfaces.

## API overview

The subsections below gives an overview of the RESTful API for the NGSI-9 Server and the Sense2Web Platform.

## NGSI-9 API

Please refer to the FI-WARE NGSI-9 Open RESTful API [specification][ngsi9-spec] on the details on the API, and also the [NGSI Associations concept][ngsi-associations-concept] for details on how to register and discover associations.

The Standard Operations currently supported are:

| Verb  | URI           | Payload  |
| ------|:-------------| :-------|
| POST  | //{hostname}/ngsi9/registerContext | registerContextRequest |
| POST  | //{hostname}/ngsi9/discoverContextAvailability |   discoverContextAvailabilityRequest |
| POST  | //{serverRoot}/ngsi9/subscribeContextAvailability |    subscribeContextAvailabilityRequest |
| POST  | //{hostname}/ngsi9/updateContextAvailabilitySubscription | updateContextAvailabilitySubscriptionRequest|
| POST  | //{hostname}/ngsi9/unsubscribeContextAvailability      |   unsubscribeContextAvailabilityRequest|

 
The Convenience Operations  currently supported are:

| Verb  | URI           | Payload  |
| ------|:-------------| :-------|
| POST  | //{hostname}/ngsi9/contextEntities/{EntityID} | registerContextRequest |
| GET  | //{hostname}/ngsi9/contextEntities/{EntityID} |   N/A |
| GET  | //{hostname}/ngsi9/contextEntities/{EntityID}/attributes |    N/A|
| GET  | //{hostname}/ngsi9/contextEntities/{EntityID}/attributes/{attributeName} | N/A|
| GET  | //{hostname}/ngsi9/contextEntities/{EntityID}/attributeDomains/{attributeDomainName}      |   N/A|
| GET  | //{hostname}/ngsi9/contextEntityTypes/{typeName}      |   N/A|


#### Registration

The first step is to Register some Context Entities. So, using a REST client, we will send an NGSI-9 registerContextRequest message like the one shown below using the following URL:

<pre>POST http://{hostname}/ngsi9/registerContext</pre>

The payload as an example can be the following:
```xml

    <?xml version="1.0"?>
    <registerContextRequest>
      <contextRegistrationList>
        <contextRegistration>
          <entityIdList>
            <entityId type="Room" isPattern="false">
              <id>Room1</id>
            </entityId>
            <entityId type="Room" isPattern="false">
              <id>Room2</id>
            </entityId>
          </entityIdList>
          <contextRegistrationAttributeList>
            <contextRegistrationAttribute>
              <name>temperature</name>
              <type>float</type>
              <isDomain>false</isDomain>
            </contextRegistrationAttribute>
            <contextRegistrationAttribute>
              <name>pressure</name>
              <type>integer</type>
              <isDomain>false</isDomain>
            </contextRegistrationAttribute>
          </contextRegistrationAttributeList>
          <providingApplication>http://mysensors.com/Rooms</providingApplication>
      </contextRegistration>
      </contextRegistrationList>
      <duration>P1M</duration>
    </registerContextRequest>
```
``` json
{
    "contextRegistrations": [
        {
            "entities": [
                {
                    "type": "Room",
                    "isPattern": "false",
                    "id": "Room1"
                },
                {
                    "type": "Room",
                    "isPattern": "false",
                    "id": "Room2"
                }
            ],
            "attributes": [
                {
                    "name": "temperature",
                    "type": "float",
                    "isDomain": "false"
                },
                {
                    "name": "pressure",
                    "type": "integer",
                    "isDomain": "false"
                }
            ],
            "providingApplication": "http://mysensors.com/Rooms"
        }
    ],
    "duration": "P1M"
}
```

Results obtained: should be a registerContextResponse similar to the following, whereby the registration number will be randomly generated.

``` xml
<?xml version="1.0"?>
<registerContextResponse>
  <duration>P1M</duration>
  <registrationId>UniS_0AGGEEdSNK</registrationId>
</registerContextResponse>
</pre>
| <pre style="color:darkgreen; white-space: pre-wrap; white-space: -moz-pre-wrap; white-space: -pre-wrap; white-space: -o-pre-wrap; word-wrap: break-word;">
{
  "duration" : "P1M",
  "registrationId" : "UniS_0AGGEEdSNK"
}
```
#### Discovery
The next step is to discover the availability of a Context Entity. So, using a REST client, we will send a discoverContextAvailabilityRequest message like the one shown below:
<pre>POST http://{hostname}/ngsi9/discoverContextAvailability</pre>
``` xml
<?xml version="1.0"?>
<discoverContextAvailabilityRequest>
  <entityIdList>
    <entityId type="Room" isPattern="false">
      <id>Room1</id>
    </entityId>
  </entityIdList>
  <attributeList/>
</discoverContextAvailabilityRequest>
```
``` json
{
    "entities": [
        {
            "type": "Room",
            "isPattern": "false",
            "id": "Room1"
        }
    ]
}
```
``` xml

<?xml version="1.0"?>
<discoverContextAvailabilityResponse>
  <contextRegistrationResponseList>
    <contextRegistrationResponse>
      <contextRegistration>
        <entityIdList>
          <entityId type="Room" isPattern="false">
            <id>Room1</id>
          </entityId>
        </entityIdList>
        <contextRegistrationAttributeList>
          <contextRegistrationAttribute>
            <name>temperature</name>
            <type>float</type>
            <isDomain>false</isDomain>
          </contextRegistrationAttribute>
          <contextRegistrationAttribute>
            <name>pressure</name>
            <type>integer</type>
            <isDomain>false</isDomain>
          </contextRegistrationAttribute>
        </contextRegistrationAttributeList>
        <providingApplication>http://mysensors.com/Rooms</providingApplication>
      </contextRegistration>
    </contextRegistrationResponse>
  </contextRegistrationResponseList>
</discoverContextAvailabilityResponse>
```
``` json
{
    "contextRegistrationResponses": [
        {
            "contextRegistration": {
                "attributes": [
                    {
                        "isDomain": "false",
                        "name": "temperature",
                        "type": "float"
                    },
                    {
                        "isDomain": "false",
                        "name": "pressure",
                        "type": "integer"
                    }
                ],
                "entities": [
                    {
                        "id": "Room1",
                        "isPattern": "false",
                        "type": "Room"
                    }
                ],
                "providingApplication": "http://mysensors.com/Rooms"
            }
        }
    ]
}
```
#### Subscription

``` xml 

<?xml version="1.0"?>
<subscribeContextAvailabilityRequest>
  <entityIdList>
    <entityId type="Room" isPattern="true">
      <id>.*</id>
    </entityId>
  </entityIdList>
  <attributeList>
    <attribute>temperature</attribute>
  </attributeList>
  <reference>http://localhost:1028/accumulate</reference>
  <duration>P1M</duration>
</subscribeContextAvailabilityRequest>
```
``` json
{
    "entities": [
    {
        "type": "Room",
        "isPattern": "true",
        "id": ".*"
    }
    ],
    "attributes": [
    "temperature"
    ],
    "reference": "http://localhost:1028/accumulate",
    "duration": "P1M"
}
```
```xml 
<?xml version="1.0"?>
<subscribeContextAvailabilityResponse>
  <subscriptionId>UniS_0AGGEEdSNK</subscriptionId>
  <duration>P1M</duration>
</subscribeContextAvailabilityResponse>
```
```json
{
    "duration": "P1M",
    "subscriptionId": "UniS_0AGGEEdSNK"
}
```
#### Update

``` xml
<?xml version="1.0"?>
<updateContextAvailabilitySubscriptionRequest>
  <entityIdList>
    <entityId type="Car" isPattern="true">
      <id>.*</id>
    </entityId>
  </entityIdList>
  <attributeList/>
  <duration>P1M</duration>
  <subscriptionId>UniS_0AGGEEdSNK</subscriptionId>
</updateContextAvailabilitySubscriptionRequest>
```
```json

{
    "entities": [
    {
        "type": "Car",
        "isPattern": "true",
        "id": ".*"
    }
    ],
    "duration": "P1M",
    "subscriptionId": "UniS_0AGGEEdSNK"
}
```
```xml
<?xml version="1.0"?>
<updateContextAvailabilitySubscriptionResponse>
  <subscriptionId>UniS_0AGGEEdSNK</subscriptionId>
  <duration>P1M</duration>
</updateContextAvailabilitySubscriptionResponse>
```
```json
{
    "duration": "P1M",
    "subscriptionId": "UniS_0AGGEEdSNK"
}
```
##Sense2Web API
The diagram below illustrates the structure of RESTful API:


----------


![S2W API overview](http://forge.fiware.org/plugins/mediawiki/wiki/fiware/images/0/09/S2w-api-methods.png)


----------


| Verb  | URI           | Description|
| ------|:-------------| :-------|
| GET | //{hostname}/s2w/repository/getVersion | ***getVersion***: returns the current version of the GEi|
| POST  | //{hostname}/s2w/repository/register/iot-a/{descriptionType} |   ***registerMultipleDescriptions***: registers a set of descriptions with the Sense2Web platform. {descriptionType} can be "resource","entity" or "service". The descriptions that are submitted is must conform with the IoT-A ontologies. NOTE for Release 2: This will trigger the training process to be executed, but associations will not be created. |
| POST  | //{hostname}/s2w/repository/register/iot-a/{descriptionType}/{objectID} | ***registerDescription***: registers a description with the Sense2Web paltform. {descriptionType} can be "resource","entity" or "service". The description that is submitted is must conform with the IoT-A ontologies.
| GET | //{hostname}/s2w/repository/lookup/iot-a/{descriptionType}/{objectID}?resultFormat={resultFormat} | ***lookupDescription***: looks up a description with a specific ID and retrieves it in a specific format; "RDF/XML", "RDF/JSON", "N3", "N-TRIPLE", "TURTLE".|
| PUT  | //{hostname}/s2w/repository/update/iot-a/{descriptionType}/{objectID} |   ***updateDescription***: updates a description with a specific ID that is already registered with the platform's repository |
| DELETE | //{hostname}/s2w/repository/delete/iot-a/{descriptionType}/{objectID} | ***deleteDescription***: deletes a description with a specific ID that is already registered with the platform's repository
| GET | //{serverRoot}/s2w/repository/query/sparql/iot-a/{descriptionType}?sparql={sparqlQuery}?resultFormat={resultFormat} | ***queryDescription***: query the repository using SPARQL. The SPARQL query must be inserted in the "sparql" query parameter, with the query itself being URL-encoded for it to be sent as a GET request. "resultFormat" can be "XML", "JSON", "CSV", or "TSV"|
| POST | //{hostname}/s2w/repository/query/sparql/iot-a/{descriptionType}?resultFormat={resultFormat} |   ***queryDescription***: query the repository using SPARQL. The SPARQL query is inserted in the POST payload. "resultFormat" can be "XML", "JSON", "CSV", "TSV"|
| POST | //{hostname}/s2w/repository/discover/iot-a/{descriptionType} | ***discoverDescription***: discover a description by submitting a template of a particular description type, which contain values as keywords for the search.

### Registration

In this example, we will register an IoT-A description at the Sense2Web repository using the HTTP method and URL below:
<pre>
POST  -  http://{hostname}/s2w/repository/register/iot-a/resource/Resource_16_BA_02_temperature_sensor
</pre>
We will then retrieve the same description using the HTTP method and URL below:
<pre>
GET -  http://{hostname}/s2w/repository/lookup/iot-a/resource/Resource_16_BA_02_temperature_sensor
</pre>
The POST method will contain in its payload a description that conforms to the IoT-A ontology, such as the one below:
```xml

<rdf:RDF
    xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
    xmlns="http://www.surrey.ac.uk/ccsr/ontologies/ResourceModel.owl#"
    xmlns:protege="http://protege.stanford.edu/plugins/owl/protege#"
    xmlns:xsp="http://www.owl-ontologies.com/2005/08/07/xsp.owl#"
    xmlns:owl="http://www.w3.org/2002/07/owl#"
    xmlns:dm="http://www.surrey.ac.uk/ccsr/ontologies/DeviceModel.owl#"
    xmlns:xsd="http://www.w3.org/2001/XMLSchema#"
    xmlns:swrl="http://www.w3.org/2003/11/swrl#"
    xmlns:swrlb="http://www.w3.org/2003/11/swrlb#"
    xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#"
    xmlns:j.0="http://purl.oclc.org/NET/ssnx/ssn#"
    xml:base="http://www.surrey.ac.uk/ccsr/ontologies/ResourceModel.owl">
  <OnDeviceResource rdf:ID="Resource_16_BA_02_temperature_sensor">
    <hasResourceLocation>
      <Location rdf:ID="Location_16_BA_02_temperature_sensor">
        <hasLongitude rdf:datatype="http://www.w3.org/2001/XMLSchema#float">-0.57</hasLongitude>
        <hasLocalLocation rdf:datatype="http://www.w3.org/2001/XMLSchema#anyURI">http://www.surrey.ac.uk/ccsr/ontologies/LocationModel.owl#E16</hasLocalLocation>
        <hasGlobalLocation rdf:datatype="http://www.w3.org/2001/XMLSchema#anyURI">http://www.geonames.org/2647793/</hasGlobalLocation>
        <hasAltitude rdf:datatype="http://www.w3.org/2001/XMLSchema#int">45</hasAltitude>
        <hasLatitude rdf:datatype="http://www.w3.org/2001/XMLSchema#float">51.23</hasLatitude>
      </Location>
    </hasResourceLocation>
    <isHostedOn><j.0:SensingDevice rdf:ID="PloggBoard_16_BA_02_temperature"/></isHostedOn>
    <hasResourceID rdf:datatype="http://www.w3.org/2001/XMLSchema#anyURI">16_BA_02_temperature_sensor</hasResourceID>
    <hasType rdf:resource="#Sensor"/>
    <isExposedThroughService rdf:datatype="http://www.w3.org/2001/XMLSchema#anyURI"
    >http://www.surrey.ac.uk/ccsr/ontologies/ServiceModel.owl#16_BA_02_temperature_sensingService</isExposedThroughService>
    <hasTag rdf:datatype="http://www.w3.org/2001/XMLSchema#string">temperature sensor 16,2nd,BA,office</hasTag>
    <hasName rdf:datatype="http://www.w3.org/2001/XMLSchema#string">temperaturesensor16_BA_02</hasName>
    <hasTimeOffset rdf:datatype="http://www.w3.org/2001/XMLSchema#int">0</hasTimeOffset>
  </OnDeviceResource>
</rdf:RDF>
```

The response, which reports the result of the registration, should be a JSON response with the following format:

```json
{
id: "Resource_16_BA_02_temperature_sensor"
type: "resource"
stored: true
indexed: true
association: ""
}
```

* the GET request should return the same description as the one registered with the POST example.

Please refer to the Unit Test Plan wiki page for this GEi to get examples on how to interact with the web server using IoT-A models and SPARQL queries.

[IoT-A]: http://www.iot-a.eu/public
[IoT-A models]: http://www.iot-a.eu/public/public-documents/documents-1/1/1/copy2_of_d4.2/at_download/file
[s2w-wiki-info]: https://forge.fi-ware.eu/plugins/mediawiki/wiki/fiware/index.php/Linked_Data_Platform_and_Gateway
[iot-chap-wiki]: http://forge.fi-ware.org/plugins/mediawiki/wiki/fiware/index.php/Internet_of_Things_(IoT)_Services_Enablement_Architecture
[iotd-arch-wiki]: http://forge.fi-ware.org/plugins/mediawiki/wiki/fiware/index.php/FIWARE.ArchitectureDescription.IoT.Backend.IoTDiscovery
[Advanced-REST-Client]: https://chrome.google.com/webstore/detail/advanced-rest-client/hgmloofddffdnphfgcellkdfbfbjeloo
[iota-ontologies]: http://iot.ee.surrey.ac.uk/s2w/share/ontologies/iot-a/original/
[iota-paper]: http://epubs.surrey.ac.uk/531903/
[ngsi9-spec]: https://forge.fi-ware.eu/plugins/mediawiki/wiki/fiware/index.php/FI-WARE_NGSI-9_Open_RESTful_API_Specification
[ngsi-associations-concept]: http://forge.fi-ware.eu/plugins/mediawiki/wiki/fiware/index.php/NGSI_association

> Written with [StackEdit](https://stackedit.io/).
