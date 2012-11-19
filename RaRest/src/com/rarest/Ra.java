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
	/**
	 * The context
	 */
	private Context context;
	private Api api;
	public Api getApi() {
		return api;
	}

	public void setApi(Api api) {
		this.api = api;
	}

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

	
	public Ra(String apiName) {
		if (!apis.containsKey(apiName)) {
			throw new ConfigFileException();
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
	
	public Response execute() {
		if (service == null) {
			throw new ServiceNotLoadedException();
		}
		Harvester harvester=new Harvester(api.getBaseUrl(), service);
		return harvester.execute();
	
	}

}
