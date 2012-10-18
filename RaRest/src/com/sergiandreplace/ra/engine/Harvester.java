package com.sergiandreplace.ra.engine;

import org.apache.http.Header;
import org.apache.http.HttpMessage;
import org.apache.http.impl.client.DefaultHttpClient;

import com.sergiandreplace.ra.exception.NullHeaderException;
import com.sergiandreplace.ra.service.Service;

public class Harvester {
	private Service service;
	private DefaultHttpClient httpClient;
	
	public Harvester(Service service) {
		this.service=service;
	}
	public Response execute() {
		Response response=null;
		
		
		
		
		
		return response;
		
	}
	
	/**
	 * return the headers variables array.
	 * 
	 * @param httpRequest
	 *            the get/post object to get initial headers
	 * @return an array of Header objects
	 */
	private Header[] generateHeaders(HttpMessage httpRequest) {
		for (com.sergiandreplace.ra.service.Header header:service.getHeaders()) {
		
			String key = header.getName();
			if (header.getValue()==null) {
				throw new NullHeaderException(header.getName());
			}
			String value = header.getValue();// URLEncoder.encode(e.getValue());
			httpRequest.addHeader(key, value);
		}
		return httpRequest.getAllHeaders();

	}

}
