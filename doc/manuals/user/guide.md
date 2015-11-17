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