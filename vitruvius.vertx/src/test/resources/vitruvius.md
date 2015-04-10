%%% MetaData
status: Live
version: 0.1
author: Amin Mohammed-Coleman <amin.mohammed-coleman@digital.cabinet-office.gov.uk>
description: Car tax service
lastUpdated: 22/08/2014
type:service
department: test
%%%

# Car Tax Service 


# Context

Used to pay tax on your car.


# Components

The following components where used to build this example service
    - name: component one
      description:
        Here I describe the component one in some detail. It should
        perhaps be snapped out as a separate element?        
	- name: component two
	  description:
	   	Here I describe the component one in some detail. It should
	    perhaps be snapped out as a separate element?        
	- name: component three
	  description:
		Here I describe the component one in some detail. It should
		perhaps be snapped out as a separate element?        
  

# Services

The following section defines the other services used in order to perform the required business function.

%%%Service
name: PeopleLookUpService
description: Used for looking people up
link: ../repository/PeopleLookUpService
%%%
%%%Service
name: PaymentService
link: ../repository/PaymentService
%%%

# Transactions

In order to fulfil the requirements of the business service, our service has to perform interaction with multiple other services. The following shows how my example service interacts with the other services defined in this architecture document.

%%%sequence
MyService->PeopleLookUpService: Find Person
Note right of PeopleLookUpService: PeopleLookUpService performs a look up
PeopleLookUpService-->MyService: Returns an individual
MyService->>PeopleLookUpService: Confirmation
%%%



# Further Reading

Test	

