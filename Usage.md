# Usage #

## Introductions ##

RaREST library is based in the use of a configuration file (or several files) that contains all the needed info to call a set of REST services.

## Dependencies ##

In order to make RaREST work, you need to include the following libraries:

  * simple-xml-2.6.7.jar: an incredibly powerful xml persitence library. Download from http://simple.sourceforge.net/download.php
  * jackson-core-asl-1.9.7.jar and jackson-mapper-asl-1.9.7.jar: another wonderful serialization library, used for json in this case. Get it from http://wiki.fasterxml.com/JacksonDownload#Latest_stable_1.x_version (keep in mind that RaREST has not been tested with 2.x versions)


## Configuration file ##

The configuration file is an xml file that should be place in the assets folder.
More information on config file format on the wiki page about [Configuration file](ConfigFile.md)

## Using the Ra object ##

After adding the Jar file to your project, you need to create an instance of the Ra object:
```
Ra ra=new Ra(getContext(),"acme_api");
```

In the constructor the Ra object loads the config file. This file should be placed in assets folder and it should be named with xml extension (acme\_api.xml in the example).

Now this Ra object is able to load any of the services defined in the config file.

Once we execute the method service, we load a single service in the Ra object:

```
ra.service("getGadgets");
```

After this, the Ra object loads all parameters and default values from the configuration. All the changes done at this moment are applied to the instance of the getGadgets.

If we want to reset the service to its default definition or load another service, we just execute the service method again.

Now we can set different values for the parameters of the Rest service with the set method. And finally execute it. All methods of Ra return the same Ra object, so they can be chained:

```
Response response=ra.service("getGadgets").set("pageSize","10").set("pageNumber","20").execute();
```

Once executed, the response object contains all info needed to know how it worked. It can return the answer parsed as json or xml depending on the mime type answer of the service.