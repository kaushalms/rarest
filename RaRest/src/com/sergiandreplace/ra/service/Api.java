package com.sergiandreplace.ra.service;

import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import com.sergiandreplace.ra.exception.ServiceNotFoundException;
import com.sergiandreplace.ra.logger.Logger;

@Root
public class Api {
	@Attribute(required=false,name="baseurl")
	private String baseUrl;

	@Element(required=false)
	private Logger logger;
	
	@ElementList(inline=true,required=false)
	private List<Service> services;
	
	public String getBaseUrl() {
		return baseUrl;
	}
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	public Logger getDebug() {
		return logger;
	}
	public void setDebug(Logger debug) {
		this.logger = debug;
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
		return String.format("===================\nApi.start baseUrl=%s \n%s\n%s\nApi.end\n=====================",baseUrl,logger.toString(),servicesBuilder);
	}
	public boolean hasService(String serviceName) {
		boolean found=false;
		for (Service service:services) {
			if (serviceName.equals(service.getName())) {
				found=true;
				break;
			}
		}
		return found;
	}
	
	public Service getService(String serviceName) {
		Service found=null;
		for (Service service:services) {
			if (serviceName.equals(service.getName())) {
				found=service;
				break;
			}
		}
		if (found==null) {
			throw new ServiceNotFoundException(serviceName);
		}
		return found;
	}
	
	public String getDefaultServiceName() {
		String found=null;
		for (Service service:services) {
			if (service.isDefaultParent()) {
				found=service.getName();
				break;
			}
		}
		return found;
	}
	public Service loadService(String serviceName) {
		Service service;
		Service fullService;
		Service parentService;
		
		service=getService(serviceName);
		
		String parentName=service.getParent();
		if (parentName==null) {
			parentName=getDefaultServiceName();
		}
		if (hasService(parentName)) {
			parentService=getService(parentName);
			fullService=Service.merge(parentService, service);
		}else{
			fullService=service.clone();
		}
		logger.s(fullService.toString());
		return fullService;
		
	}
	
}
