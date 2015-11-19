# RaREST configuration file #

## Introduction ##

The configuration file is where all the magic of RaREST is done. This file is a flexible xml file that will help you to implement Rest clients faster, it will also give you centralized configuration for all them, so you can abstract your code from api implementation.

The current format is still an early release with some sections not fully functional. Once the first RTM version will be released, an XSD scheme will be generated in order to allow validation in edit time.

## Ra tag ##

This is the root tag of the configuration file. It has a single mandatory parameter baseUrl, which contains the url where the api is located.

Api tag can contain two tags: logger and service

## Logger tag ##

This section will allow us to define the level of logging shown in the Android log. That will help us easily to switch on or off the logging of services use and their parameters.

This section is still under construction and is not fully prepared.

## Service tags ##

This is a multi entry tag, you can have as many as needed. Each one of them is the representation of a specific Rest service. It will include all the needed info to invoke them. Service tag can be used also to specify a template to be used in other services as a placeholder.

### Attributes ###

| name | The name that will be used in the service method to load it |
|:-----|:------------------------------------------------------------|
| url  | the url of the service. It will be concatenated to the api base url. It could contain url fields that will be later replaced by rest parameters. Ex: "/user/{userId}/info" |
| verb | The http verbe that should be executed when invoking the service: get, post, put, delete, patch, head. |
| default | If set to "yes", the service will be used as a template for all other services if not parent is provided. You can set more than one service as default, but RaRest is expecting only one, so behaviour could be unexpected |

Example:
```
<api baseUrl="http://acme.com/api">
  <service name="getDevice" url="/device/{deviceId}" verb="get" />
</api>
```

That specific example will fail on invoke, as we declared the field deviceId in the path, but no rest parameters is matching it.

The service tag can contain several tags: header, param, pre and post.

### Header tag ###

The header tag represents a header value to set on the Rest petition. If this tag contains any value, it will be used as default value when the service is loaded. It could be changed via set method.

#### Attributes ####

| name | Name of the header to be used on the rest petition |
|:-----|:---------------------------------------------------|
| alias | Optional. This allows us to stablish another name for this header that will be used on code when invoking the set method |
| mandatory | Defines if this header must have a value before doing the rest petition |

### Param tag ###

Each one of the parameters used on the rest petition. The value of the tag will be used as default value for the parameter. Could be changed on runtime with the set method.

#### Attributes ####

| name | Name of the header to be used on the rest petition |
|:-----|:---------------------------------------------------|
| alias | Optional. This allows us to establish another name for this parameter that will be used on code when invoking the set method |
| mandatory | Defines if this header must have a value before doing the rest petition |
| type | The type of parameter to use. See below            |

There are three different types of parameters:
  * query: this is a query param. To be added in the url in the shape of key=value, separated by & and url-encoded
  * rest: this parameter value will replace a field in the url. It should be named in the same way and it's case-sensitive.
  * body: this will be included in the body as a line in a form body using key=value format. At the moment, RaREST does only support x-www-form-urlencoded as body. To be improved :) 

### Pre tag ###

This tag indicates one or more processors that will be invoked when the execute method is called but just before the HttpPetition is really created, so the Service object can still be handled.

The only attribute is name, that should contain the canonical name of a class that implements the com.rarest.processor.Preprocessor interface.

### Post tag ###

This tag indicates one or more processors that will be invoked right after the execute method is called. Post processors can ask Ra to execute the rest service again.

The only attribute is name, that should contain the canonical name of a class that implements the com.rarest.processor.Postprocessor interface.

## Example ##

```
<?xml version="1.0" encoding="UTF-8"?>
<api baseurl="http://api.github.com">
	<logger show="true">
		<include type="service" show="true"/>
		<include type="param" />
		<include type="header"/> 
	</logger>
	<service name="jsonresponse" default="true">
		<header name="accept-response">application/json</header>
		<param alias="oAuthToken" mandatory="true" name="access_token" type="query"/>
		<pre name="com.myapp.security.TokenPreprocessor"/>
		<post name="com.myapp.security.SecurityIssuesPostProcessor"/>
	</service>
	<!-- get current user service -->
	<service name="getUser" url="/users/{userId}" verb="get" parent="xmlresponse">
		<param mandatory="true" name="userId" type="rest"/>
	</service>
	<service name="getCurrentUser" url="/user" verb="get"/>
	<!--Update the authenticated user-->
	<service name="updateCurrentUser" url="/user" verb="patch">
		<param name="name" type="body"/>
		<param name="name" type="body"/>
		<param name="email" type="body"/>
		<param name="blog" type="body"/>
		<param name="company" type="body"/>
		<param name="location" type="body"/>
		<param name="hireable" type="body"/>
		<param name="bio" type="body"/>
	</service>
	<!--Get your repositiories-->
	<service name="getCurrentUserRepositories" url="/user/repos" verb="get">
		<param name="type" type="query"/>
		<!-- all, owner, public, private, member. Default: all-->
		<param name="sort" type="query"/>
		<!-- created, updated, pushed, full_name, default: full_name -->
		<param name="direction" type="query"/>
		<!-- asc or desc, default: when using full_name: asc, otherwise desc.-->
	</service>
</api>
```