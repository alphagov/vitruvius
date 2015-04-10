%%%MetaData
status: Draft
version: 0.1
author: Graham Cross <graham.cross@digital.cabinet-office.gov.uk>
description: Pan Government Service/Module/Component catalogue
lastUpdated: 23/09/2014
department: Cabinet Office
tags: catalogue, java, vertex
type: platform
%%%

# Vitruvius

Pan Government Service/Module/Component catalogue.

# Primary User Needs

  * Reduce duplication of effort between projects
  * Understand the service landscape of departments
  * Speed up development by reusing existing services and components

# Structure

The following components were used to build this example service though none were broken out as separate entities as yet:

  * vitruvius web
    * display and deploy module - uses vertex modules to access to java functions
  * vitruvius.markdown
    * a java library that contains custom markdown4j plugins that allow us to pick out the Vitruvius specific markdown and make sense of it.
  * vitruvius.vertex
    * a vertex module that contains the java backend of vitruvius.  Includes an embedded elastic search implementation that can be used instead of an externally deployed one.

# References

[Architecture content](https://github.com/alphagov/architecture-content) is a good place to read up about how to format your markdown files for vitruvius

# Further Reading
	

