# RaREST for Android #
## Introduction ##

RaREST is an Androd library intended to simplify the access to RESTful services, or any other service based on HTTP verbs and form content.

RaREST core component is the configuration file, an xml file with an specific format that contains all the information needed to execute the REST services. The configuration files uses a parent-child configuration for services that helps reducing verbosity of recurrent parameters (ex.: user token)

RaREST library can load one or more configuration files on runtime containing services that can be easily invoked from code.

## Documentation ##

Check the wiki sections:

  * [Usage](Usage.md)
  * [Xml Configuration File](ConfigFile.md)
  * [Processors](Processors.md)

## Acknowledgments ##
Thanks to Alberto Aparicio and Juan Belmonte for their valuable feedback and recommendations.