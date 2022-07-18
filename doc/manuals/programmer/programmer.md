
# Introduction  

The GEi is meant to be used as a web service, and therefore users and applications can interact with the GEi via two sets of RESTful interfaces.  

# API overview  

The subsections below gives an overview of the RESTful API for the NGSI-9 Server and the Sense2Web Platform.  

## NGSI-9 API  

Please refer to the FIWARE NGSI-9 Open RESTful API [specification][ngsi9-spec] on the details on the API, and also the [NGSI Associations concept][ngsi-associations-concept] for details on how to register and discover associations.  

The Standard Operations currently supported are:  

| Verb  | URI          | Payload |  
| ------|:-------------| :-------|  
| POST  | //{hostname}/ngsi9/registerContext | registerContextRequest |  
| POST  | //{hostname}/ngsi9/discoverContextAvailability |   discoverContextAvailabilityRequest |  
| POST  | //{hostname}/ngsi9/subscribeContextAvailability |    subscribeContextAvailabilityRequest |  
| POST  | //{hostname}/ngsi9/updateContextAvailabilitySubscription | updateContextAvailabilitySubscriptionRequest|  
| POST  | //{hostname}/ngsi9/unsubscribeContextAvailability      |   unsubscribeContextAvailabilityRequest|  
| POST  | //{callbackUri} | notifyContextAvailabilityRequest |  

 
The Convenience Operations  currently supported are:  

| Verb  | URI           | Payload  |  
| ------|:-------------| :-------|  
| POST  | //{hostname}/ngsi9/contextEntities/{EntityID} | registerContextRequest |  
| GET  | //{hostname}/ngsi9/contextEntities/{EntityID} |   N/A |  
| GET  | //{hostname}/ngsi9/contextEntities/{EntityID}/attributes |    N/A|  
| GET  | //{hostname}/ngsi9/contextEntities/{EntityID}/attributes/{attributeName} | N/A|  
| GET  | //{hostname}/ngsi9/contextEntityTypes/{typeName}      |   N/A|  

### Standard Operations  
#### Registration of Context Entities  

The first step is to register some Context Entities. So, using a RESTful client, we can send an NGSI-9 ***registerContextRequest*** message like the one shown below using the following URL:  

``` POST http://{hostname}/ngsi9/registerContext ```  

The payload as an example can be the following:  

```   
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

Result obtained should be a ***registerContextResponse*** similar to the following, whereby the registration number will be randomly generated.  

```json  
{
  "duration" : "P1M",
  "registrationId" : "UniS_0AGGEEdSNK"
}  
```  
#### Registration of Associations

```  
{
  "contextRegistrations": [
    {
      "metadatas": [
        {
          "name": "right_neighbour",
          "type": "Association",
          "value": {
            "source": {
              "id": "Room1",
              "type": "Room",
              "isPattern": "false"
            },
            "target": {
              "id": "Room2",
              "type": "Room",
              "isPattern": "false"
            },
            "attributeAssociations": [
              {
                "source": "temperature",
                "target": "temperature"
              }
            ]
          }
        }
      ],
      "providingApplication": "http://www.fi-ware.eu/NGSI/association"
    }
  ],
  "duration": "P1M",
  "registrationId": ""
}
```  

#### Discovery of Context Entities
The next step is to discover the availability of a Context Entity. So, using a REST client, we will send a ***discoverContextAvailabilityRequest *** message like the one shown below:  
``` POST http://{hostname}/ngsi9/discoverContextAvailability ```  

```   
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
Result obtained should be a ***discoverContextAvailabilityResponse*** similar to the following:  

``` 
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

#### Discovery of Associations  

```  
{
  "entities": [
    {
      "type": "Room",
      "isPattern": "false",
      "id": "Room2"
    }
  ],
  "attributes": [
    "temperature"
  ],
  "restriction": {
    "scopes": [
      {
        "type" : "IncludeAssociations",
        "value" : "SOURCES"
      }
    ]
  }
}
```  
The response:

```  
{
  "contextRegistrationResponses": [
    {
      "contextRegistration": {
        "attributes": [
          {
            "isDomain": "false",
            "name": "temperature",
            "type": "float"
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
    },
    {
      "contextRegistration": {
        "metadatas": [
          {
            "name": "right_neighbour",
            "type": "Association",
            "value": {
              "attributeAssociations": [
                {
                  "source": "measurement",
                  "target": "temperature"
                }
              ],
              "entities": {
                "source": {
                  "id": "Room1",
                  "isPattern": "false",
                  "type": "Room"
                },
                "target": {
                  "id": "Room2",
                  "isPattern": "false",
                  "type": "Room"
                }
                }
            }
          }
        ],
        "providingApplication": "http://www.fi-ware.eu/NGSI/association"
      }
    }
  ]
}
```  

#### Subscription  

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
Result obtained should be a ***subscribeContextAvailabilityResponse*** similar to the following:  
  
``` json  
{
    "duration": "P1M",
    "subscriptionId": "UniS_0AGGEEdSNK"
}  
```  
#### Update  

```  
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
Result obtained should be a ***updateContextAvailabilitySubscriptionResponse*** similar to the following:  

```  
{
    "duration": "P1M",
    "subscriptionId": "UniS_0AGGEEdSNK"
}
```  

#### Notify  

```  
{
    "subscriptionId": "UniS_0AGGEEdSNK",
    "contextRegistrationResponses": [
        {
            "contextRegistration": {
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
                        "isDomain": "false",
                        "metadatas": [
                            {
                                "name": "accuracy",
                                "type": "float",
                                "value": "0.8"
                            }
                        ]
                    }
                ],
                "providingApplication": "http://mysensors.com/Rooms"
            }
        }
    ]
}  
```  
Result obtained should be a **notifyContextAvailabilityResponse** similar to the following:  

```  
{    
    "statusCode": {
        "code": "200",
        "reasonPhrase": "OK"
    }
}
```  
### Convenience Operations  
#### Registration   
To register Context Entities, this can be done using the URL structure:
```  
POST http://{hostname}/ngsi9/contextEntities/{EntityID} 
```  
The payload in the body must be a ***registerContextRequest***, as in the register procedure using the standard operation for registration.

#### Discovery  
Discovery of context entities can be done using different criteria. 

##### ID of Entity  
This can be done using the URL structure below, where {EntityID} is replaced by the ID of the Context Entity in question.
```  
GET http://{hostname}/ngsi9/contextEntities/{EntityID} 
```  
The response, as in the standard operation for discovery , will be a ***discoverContextAvailabilityResponse***.

##### Attribute of Entity  
This can be done using the URL structure below, where {attributeName} is replaced by the attribute of the Context Entity in question.
```  
GET http://{hostname}/ngsi9/contextEntities/{EntityID}/attributes/{attributeName}
```  
##### Type of Entity  
This can be done using the URL structure below, where {typeName} is replaced by the type of the Context Entity in question.
```  
GET http://{hostname}/ngsi9/contextEntityTypes/{typeName}
``` 

###Note on Registering Entities and their Attributes:

 - The "**isPattern**" field is not used for registrations but rather for discovery requests for Context Entities. 
 - **Attributes** can be specified as **Attribute Domains** by setting the "**isDomain**" field as "**true**".

#### Definitions
An **attribute** is defined as a set of information, namely a *name*, a *type*, a *value* and a set of associated metadata (e.g. timestamp, expires, source). The attribute *value* is expressed as any content, including strings or opaque objects represented using standard formats.  

## Sense2Web API  
The diagram below illustrates the structure of RESTful API:  


----------


![S2W API overview](http://forge.fiware.org/plugins/mediawiki/wiki/fiware/images/0/09/S2w-api-methods.png)


----------


| Verb  | URI           | Description|  
| ------|:-------------| :-------|  
| GET | //{hostname}/s2w/repository/getVersion | ***getVersion***: returns the current version of the GEi|  
| POST  | //{hostname}/s2w/repository/register/iot-a/{descriptionType} |   ***registerMultipleDescriptions***: registers a set of descriptions with the Sense2Web platform. {descriptionType} can be "resource","entity" or "service". The descriptions that are submitted is must conform with the IoT-A ontologies. |
| POST  | //{hostname}/s2w/repository/register/iot-a/{descriptionType}/{objectID} | ***registerDescription***: registers a description with the Sense2Web paltform. {descriptionType} can be "resource","entity" or "service". The description that is submitted is must conform with the IoT-A ontologies.
| GET | //{hostname}/s2w/repository/lookup/iot-a/{descriptionType}/{objectID}?resultFormat={resultFormat} | ***lookupDescription***: looks up a description with a specific ID and retrieves it in a specific format; "RDF/XML", "RDF/JSON", "N3", "N-TRIPLE", "TURTLE".|
| PUT  | //{hostname}/s2w/repository/update/iot-a/{descriptionType}/{objectID} |   ***updateDescription***: updates a description with a specific ID that is already registered with the platform's repository |
| DELETE | //{hostname}/s2w/repository/delete/iot-a/{descriptionType}/{objectID} | ***deleteDescription***: deletes a description with a specific ID that is already registered with the platform's repository
| GET | //{serverRoot}/s2w/repository/query/sparql/iot-a/{descriptionType}?sparql={sparqlQuery}?resultFormat={resultFormat} | ***queryDescription***: query the repository using SPARQL. The SPARQL query must be inserted in the "sparql" query parameter, with the query itself being URL-encoded for it to be sent as a GET request. "resultFormat" can be "XML", "JSON", "CSV", or "TSV"|
| POST | //{hostname}/s2w/repository/query/sparql/iot-a/{descriptionType}?resultFormat={resultFormat} |   ***queryDescription***: query the repository using SPARQL. The SPARQL query is inserted in the POST payload. "resultFormat" can be "XML", "JSON", "CSV", "TSV"|
| POST | //{hostname}/s2w/repository/discover/iot-a/{descriptionType} | ***discoverDescription***: discover a description by submitting a template of a particular description type, which contain values as keywords for the search.

### Registration  

In this example, we will register an IoT-A description at the Sense2Web repository using the HTTP method and URL below:  

``` 
POST  -  http://{hostname}/s2w/repository/register/iot-a/resource/Resource_16_BA_02_temperature_sensor
```  
We will then retrieve the same description using the HTTP method and URL below:
```  
GET -  http://{hostname}/s2w/repository/lookup/iot-a/resource/Resource_16_BA_02_temperature_sensor
```  
The POST method will contain in its payload a description that conforms to the IoT-A ontology, such as the one below:  
```  
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

```  
{
	"id": "Resource_16_BA_02_temperature_sensor",
	"type": "resource",
	"stored": true,
	"indexed": true,
	"association": ""
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
