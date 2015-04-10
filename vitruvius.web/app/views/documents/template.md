%%%MetaData
status: Live
version: 0.2
author: Graham Cross <graham.cross@digital.cabinet-office.gov.uk>
department: GDS
tags: example, markdown
type: component
%%%

# Template markdown 

This is the template markdown for vitruvius.
The first section you need is the metadata using the custom %%%MetaData tag (closing with %%%).  Mandatory fields are:

+ author - this will be used as the main contact for anyone discovering the service/component
+ status - e.g. draft, alpha, beta, live, deprecated
+ description is derived from the Git hub repository description
+ department
+ type - must be either service or component.  In this context a service is a thing that runs on a server somewhere while a component is a distinct codebase from a running service.

Other acceptable fields are:

+ version
+ tags - comma separated list of tags you think could be useful in identifying this project. They could include language or technology type(e.g. java, scala, mongo, heroku, etc.), type of data (e.g. people, cars, etc.), area of interest (e.g. security, web tier, etc.) or anything else you think would be useful.  It will eventually fall to the vitruvius product owner to weed out duplicates and unnecessary tags.

Next should be a header with the name of the service/component and a brief paragraph about what it's purpose is.  You should also include a link to the git repository that contains the codebase [code lives here](https://github.com/alphagov/vitruvius)
Following is a description about other expected headers (though none are mandatory) under the headers themselves.
Standard markdown should be used for all of your information except where custom headers explained in this document are used.
Note that Primary User Needs, Components and Services are expected but not mandatory and the rest of the document is free form to deliver the most useful summary of the service you can.


# Primary User Needs

Here you should detail the user need that drove the creation of the codebase.  For some components this may be more difficult to do, but the need might become a system need at this point.

# Components
Here you should detail the components that this service or component use
%%%Component
name: Dao Component
description: Used for Database interaction
link: daoComponent
%%%
Some text which is the reason this is used
%%%Component
name: Domain Component
description: Library that describes the projects domain
link: domainComponent
%%%

using the custom %%%Component tag you can define a component that will be displayed as part of the stub information and in the markdown.  The available fields are:
+ name
+ description
+ link - which should be a link to the component on vitruvius.  This should be the same as the name of the component

# Services

The following section defines the other services available in Vitruvius used in order to perform the required business function.

%%%Service
name: PeopleLookUpService
description: Used for looking people up
link: ../repository/PeopleLookUpService
%%%
Information about how the services are connected could be useful here.  Links to the api's etc.
%%%Service
name: PaymentService
link../repository/s/PaymentService
%%%
using the custom %%%Service tag you can define a component that will be displayed as part of the stub information and in the markdown.  The available fields are:
+ name
+ description
+ link - which should be a link to the component on vitruvius.  This should be the same as the name of the service

# APIs

If the service/component has an API then it would be useful to put information about them. 
The best use may be if swagger or a.n.other tool to generate this content and put a link to where it is hosted e.g.
This service has a [public API](http://localhost:8081/swagger/ "People Look Up Service API")
Other information you may wish to include:
API owner (if different from author at the top)
Technical Owner​​ (if different from author at the top)
API endpoint (if not sensitive)
API signup link (if registration and key required)
API documentation link
API security classification

# Transactions

If the service/component you are describing has a complex or interesting relationship with another service you may wish to use an illustrative diagram and some text to explain it.

%%%sequence
MyService->PeopleLookUpService: Find Person
Note right of PeopleLookUpService: PeopleLookUpService performs a look up
PeopleLookUpService-->MyService: Returns an info
MyService->>PeopleLookUpService: Confirmation Done
%%%

This uses the custom markdown tag %%%sequence to invoke the Markdown4j WebSequencePlugin.
Read how to build your own diagrams through text [here](https://code.google.com/p/markdown4j/#Websequence_support)

# Some other optional headers:

+ Estate
+ Data
+ Technology
+ Hosting
+ Role
+ References
+ Further Reading


	

