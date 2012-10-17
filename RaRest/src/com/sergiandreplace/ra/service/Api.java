package com.sergiandreplace.ra.service;

import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root
public class Api {
	@Attribute(required=false,name="baseurl")
	private String baseUrl;

	@Element(required=false)
	private Debug debug;
	
	@ElementList(inline=true,required=false)
	private List<Service> services;
	
	public String getBaseUrl() {
		return baseUrl;
	}
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	public Debug getDebug() {
		return debug;
	}
	public void setDebug(Debug debug) {
		this.debug = debug;
	}
	public List<Service> getServices() {
		return services;
	}
	public void setServices(List<Service> services) {
		this.services = services;
	}
	public String toString() {
		StringBuilder servicesBuilder=new StringBuilder();
		for (Service service:services) {
			servicesBuilder.append(service.toString());
			servicesBuilder.append("\n");
		}
		return String.format("===================\nApi.start baseUrl=%s \n%s\n%s\nApi.end\n=====================",baseUrl,debug.toString(),servicesBuilder);
	}
	
}
