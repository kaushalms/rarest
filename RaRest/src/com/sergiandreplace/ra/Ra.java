package com.sergiandreplace.ra;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.sergiandreplace.ra.engine.Harvester;
import com.sergiandreplace.ra.exception.ConfigFileException;
import com.sergiandreplace.ra.exception.HeaderAliasNotFoundException;
import com.sergiandreplace.ra.exception.ParamAliasNotFoundException;
import com.sergiandreplace.ra.exception.ServiceNotFoundException;
import com.sergiandreplace.ra.exception.ServiceNotLoadedException;
import com.sergiandreplace.ra.service.Api;
import com.sergiandreplace.ra.service.Service;
import com.sergiandreplace.ra.xml.XmlLoader;

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
	/**
	 * The context
	 */
	private Context context;
	private Api api;
	private String serviceName;
	private Service service;

	public Ra(Context context, String apiName) {
		this.context = context;
		if (apis == null) {
			apis = new HashMap<String, Api>();
		}
		if (!apis.containsKey(apiName)) {
			loadApi(apiName);
		}
		api = apis.get(apiName);
		
	}

	public Ra loadApi(String apiName) {
		String filename = apiName;
		if (!filename.endsWith(".xml")) {
			filename += ".xml";
		}
		InputStream isConfig = null;
		try {
			isConfig = context.getAssets().open(filename);
		} catch (Exception e) {
			throw new ConfigFileException(e);
		}

		Api api = XmlLoader.load(isConfig);
		apis.put(apiName, api);
		return this;
	}

	public Ra service(String serviceName) {
		if (api.hasService(serviceName)) {
			this.serviceName=serviceName;
			this.service = api.loadService(serviceName);
		} else {
			throw new ServiceNotFoundException(serviceName);
		}
		return this;
	}

	public Ra set(String param, String value) {
		if (service == null) {
			throw new ServiceNotLoadedException();
		}
		if (!service.hasParamByAlias(param)) {
			throw new ParamAliasNotFoundException(param);
		}
		service.getParamByAlias(param).setValue(value);
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
	
	public Ra execute() {
		if (service == null) {
			throw new ServiceNotLoadedException();
		}
		Harvester harvester=new Harvester(service);
		harvester.execute();
		return this;
	}

}
