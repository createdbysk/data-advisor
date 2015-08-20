# Purpose
Provide a unified view of all data you own, locally and on the web

# Design principles
* Use a requirejs plugin based approach to organize files and provide dependency injection.
* Use the amdefine approach to allow requirejs or node to load the top level files.
* Use notification and queue mechanism to create a staged event driven architecture.
* If there are any top level node specific symbols, pass them as parameters to the app. 

## Design
### Application entry point
Accept a top level plugin name on the commandline to bootstrap the application.

# Implementation
## Libraries
Name | Purpose
-----|--------
commander|cli
