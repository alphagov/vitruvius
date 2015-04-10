%%%MetaData
status: Example
version: 0.2
author: Amin Mohammed-Coleman <amin.mohammed-coleman@digital.cabinet-office.gov.uk>
description: Example DAO component that may be used by a service or another component  
department: example
tags: data access
type: component
%%%

# Data Access Component 

# Context

Generic data access component

%%%Uml
@startuml
interface "Data Access" as DA

DA - [Dao Component] 
[Dao Component] ..> HTTP : use

note left of HTTP : Web Service only

note right of [Dao Component]
  this is an example Dao Component diagram
  generated from text
end note
@enduml
%%%
  
  
  
# Further Reading

Read this for more information.

