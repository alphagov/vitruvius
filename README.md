VITRUVIUS
=========

Vitruvius is an app for presenting and potentially interfacing with architecture content. The general pitch is that 
architecture data comes from a number of repositories - closest to the 'source' for each service or component, 
but that a coherent view can be mashed together to show the whole service landscape. Vitruvius is an app to 
experiment with that notion.
 
Vitruvius is an app for presenting a cut down summary of platforms, services, components and registers used by government departments.  
 

## Installation

It is currently a vertx/node app, which by configuration you point to one or more github or local repositories.

 * Install [git] (http://git-scm.com)
 * Install [node] (http://nodejs.org/download)
 * Install [node package manager](https://www.npmjs.org)
 * Install [Vertx](http://vertx.io/install.html)
 * Install [Java](https://java.com/en/download/)
 * Install [Maven](http://maven.apache.org/download.cgi)
 * Clone this repository
 * Navigate to the Vitruvius home directory and run <code>mvn clean package</code> from the root directory
 * Navigate to vitruvius.web and run <code>npm start</code>

By default Vitruvius will start an embedded elastic search with data held in the <code>/var/tmp/</code> directory, if you do not have this directory/do not have access to this directory or are on windows you will .  These values can be changed in the <code>vitruvius.web/app.js</code> file .

Please note that there are a number of system variables which can override the default behaviors which are currently set to openshift defaults as that was the POC platform of choice.

A running example is here http://vitruviusweb-vitruvius.rhcloud.com 

The application shuts down when not in use so if it does not appear right away please refresh every minute or so until it starts.

## Licence
 
Released under the MIT Licence, a copy of which can be found in the file [Licence](Licence.md)