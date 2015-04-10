%%%MetaData
status: Example
version: 0.2
author: Amin Mohammed-Coleman <amin.mohammed-coleman@digital.cabinet-office.gov.uk>
description: Used to calculate tax but also can be used to pay taxes.
department: example
tags: income, tax
type: service
%%%

# Income Tax Service 

Service for calculating income tax.

# Primary User Needs

Used to calculate and pay income tax and uses other business services to perform the operation. 

# Collaborating Services

The following section defines the other services used in order to perform the required business function.

%%%Service
name: PeopleLookUpService
description: Used for looking people up
link: ../repository/peopleLookUpService
%%%

# Transactions

In order to fulfil the requirements of the business service, our service has to perform interaction with multiple other services. The following shows how the Income Tax Service interacts with the other services defined in this architecture document.
%%%sequence
Income Tax Service->People Look Up Service: Find Person
Note right of People Look Up Service: People Look Up Service performs a look up
People Look Up Service-->Income Tax Service: Returns a person or error
Income Tax Service->>People Look Up Service: Confirmation Done
%%%


