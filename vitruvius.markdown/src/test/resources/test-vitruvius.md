%%%MetaData
status: Live
version: 0.2
author: Amin Mohammed-Coleman <amin.mohammed-coleman@digital.cabinet-office.gov.uk>
description: Used to calculate tax but also can be used to pay taxes.
lastUpdated: 22/08/2014
department: HRMC
tags: income, tax
type: service
%%%

# Income Tax Service 

Service for calculating income tax.

# Primary User Needs

Used to calculate and pay income tax and uses other business services to perform the operation. See example

# Components

The following components where used to build this example service
%%%Component
name: component1
description: Used as a component
link: component1
%%%        
  

# Collaborating Services

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

# Data Handled

%%%Data
name: Banking Details
link: ../data/Banking-Details
%%%
%%%Data
name: Person
link: ../data/Person
%%%

# APIs

# Estate

# Data

# Technology

# Hosting

# Role

# Transactions

In order to fulfil the requirements of the business service, our service has to perform interaction with multiple other services. The following shows how my example service interacts with the other services defined in this architecture document.
%%%sequence
MyService->PeopleLookUpService: Find Person
Note right of PeopleLookUpService: PeopleLookUpService performs a look up
PeopleLookUpService-->MyService: Returns an info
MyService->>PeopleLookUpService: Confirmation Done
%%%


# References

# Further Reading

Test	

