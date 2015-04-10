%%%MetaData
status: Example
version: 0.1
author: Amin Mohammed-Coleman <amin.mohammed-coleman@digital.cabinet-office.gov.uk>
description: Reusable payment service for taking payments. Public API Example
department: example
tags: people, lookup
type: component
%%%

# Payment Component 

Used to make payments. Use carefully.

# Context

This service is used to take payments.


# Components

The following components where used to build this example service
%%%Component
name: Dao Component
description: Used for Database interaction
link: ../repository/daoComponent
%%%  

See below for component interaction

%%%Uml
package "Payment" {
  PaymentAPI - [Payment Component]
  PaymentHTTP - [Payment Component]
}
package "Persistence" {
  PersistenceAPI - [Dao Component]
  [Payment Component] --> PersistenceAPI
}
%%%

