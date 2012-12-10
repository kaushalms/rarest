package com.rarest;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.rarest.R;
import com.rarest.engine.Harvester;
import com.rarest.engine.Response;
import com.rarest.exception.ConfigFileException;
import com.rarest.exception.HeaderAliasNotFoundException;
import com.rarest.exception.ParamAliasNotFoundException;
import com.rarest.exception.ServiceNotFoundException;
import com.rarest.exception.ServiceNotLoadedException;
import com.rarest.logger.Logger;
import com.rarest.service.Api;
import com.rarest.service.Service;
import com.rarest.xml.XmlLoader;

/**
 * Main class from the RaREST library. This will be responsible of all interactions with the library components.
 * @author Sergi Martínez
 *
 */
public class Ra {
	/**
	 * List of configuration files loaded. This will be maintained across instances
	 */
	private static Map<String, Api> apis;
	Logger logger;
	
	private Api api;
	
	/**
	 * The current loaded api for the current api object with its current values
	 * @return the whole api that is currently loaded
	 */
	public Api getApi() {
		return api;
	}

	
	private String serviceName;
	private Service service;


	/**
	 * Generates a new instance of the RA object with the selected api cloned and ready to be modified
	 * @param apiName the name of the api to use. If it it not currently loaded, a runtime error will be thrown
	 */
	public Ra(String apiName) {
		if (!apis.containsKey(apiName)) {
			throw new ConfigFileException();
		}
		api = apis.get(apiName);
		this.logger=api.getLogger();
		logger.log("Api " + apiName + " selected");
	}
	
	/**
	 * Static method that loads a config file and stores it
	 * @param apiName Key used to retrieve the configuration later on
	 * @param isConfig InputStream to the configuration file
	 */
	public static void loadApi(String apiName, InputStream isConfig) {
		Api api = XmlLoader.load(isConfig);
		apis.put(apiName, api);
		api.getLogger().log("Api " + apiName + " loaded");
	}

	/**
	 * Stablished the current service to invoke. From this point, all the set and header 
	 * calls are applied to this service.
	 * If you need to call another service, execute this method again with the new service. This will remove
	 * all the values established, and the new service will contain the values setup in the configuration file
	 * @param serviceName name of the service to load. It will throw a runtime error if service is not in the current config.
	 * @return the same Ra object. 
	 */
	public Ra service(String serviceName) {
		if (api.hasService(serviceName)) {
			this.serviceName=serviceName;
			this.service = api.loadService(serviceName);
			this.service.setLogger(logger);
		} else {
			throw new ServiceNotFoundException(serviceName);
		}
		return this;
	}

	/**
	 * 
	 * @param param
	 * @param value
	 * @return
	 */
	public Ra set(String param, String value) {
		if (service == null) {
			throw new ServiceNotLoadedException();
		}
		if (!service.hasParamByAlias(param)) {
			throw new ParamAliasNotFoundException(param);
		}
		service.getParamByAlias(param).setValue(value).setLogger(logger);
		return this;
	}

	public Ra header(String param, String value) {
		if (service == null) {
			throw new ServiceNotLoadedException();
		}
		if (!service.hasHeaderByAlias(param)) {
			throw new HeaderAliasNotFoundException(param);
		}
		service.getParamByAlias(param).setValue(value);
		return this;
	}

	public String getServiceName() {
		return serviceName;
	}
	
	public Response execute() {
		if (service == null) {
			throw new ServiceNotLoadedException();
		}
		Harvester harvester=new Harvester(api.getBaseUrl(), service);
		harvester.setLogger(logger);
		return harvester.execute();
	
	}

}
