%%% MetaData
status: Example
version: 0.1
author: Amin Mohammed-Coleman <amin.mohammed-coleman@digital.cabinet-office.gov.uk>
description: Car tax service
department: example
tags: car tax, public API, java
type: service
%%% 

# Car Tax Service 


# User Need

As a UK road user 
I need to be able to pay my car tax 
So that I do not face prosecution




# Components

The following components where used to build this example service
%%%Component
name: Dao Component
description: Used for Database interaction
link: ../repository/daoComponent
%%%  
%%%Component
name: Payment Component
description: Used for payements
link: ../repository/paymentComponent
%%%


%%%Uml
interface "Data Access" as DA

DA - [First Component]
[First Component] ..> HTTP : use

note left of HTTP : Web Service only

note right of [First Component]
  A note can also
  be on several lines
end note
%%%

# Services

The following section defines the other services used in order to perform the required business function.

%%%Service
name: PeopleLookUpService
description: Used for looking people up
link: ../repository/peopleLookUpService
%%%


# Transactions

In order to fulfil the requirements of the business service, our service has to perform interaction with multiple other services. The following shows how my example service interacts with the other services defined in this architecture document.

%%%sequence style=napkin
Car Tax Service->People Look Up Service: Find Person and Test
Note right of PeopleLookUpService: PeopleLookUpService performs a look up
People Look Up Service-->Car Tax Service: Returns an individual
Car Tax Service->>People Look Up Service: Confirmation
%%%
