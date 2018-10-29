# StanAlone Container

## Objectives


Definition of realtime entity and ejb-like container for any kind of application. When you need minimal but powerful use of EJB container, similar to Spring Annotation Engine, this project and this libraryu is what you need. It works even for standalone and console Java applications.



## Features


StandAlone container manage and provide features for :

* Components (Annotation Component)
* Services (Annotation Service) auto-executable if class implements interface IContainerService
* Entities (Annotation Entities) representing the data bean
* Configurations (Annotation Configuration) that declare any service, component entity in the application)
* Auto-wire service (Annotation Autowired) that wire beans based on declaring field name;
* Wire service (Annotation Wired) that wire beans based on provied or declaring field name
* Service Execution Service (Annotation Execute) that wire and execute services implementing interface IContainerService
* Injection engine (Annotation Inject) that inject entities, components, services into methods, constructors or any parameter used in the structure definition


The engine is easy to use and there are two proxy options, that wire and execute services for the main class....

Proxies services are present in class StandAloneContainer, as public methods.


## License

This product is subject to LGPL v. 3.0 license described in the following link.


[LICENSE FILE](/LICENSE "LICENSE FILE")