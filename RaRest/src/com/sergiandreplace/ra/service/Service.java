package com.sergiandreplace.ra.service;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;

public class Service {
	
	public static enum Verb {get,post,put,delete,patch,head};
	
	@Attribute(required=false)
	private String name;
	
	@Attribute(required=false)
	private String url;
	
	@Attribute(required=false)
	private Verb verb;
	
	@Attribute(required=false)
	private String parent;
	
	@Attribute(required=false, name="default")
	private boolean defaultParent=false;
	
	@ElementList(inline=true, required=false)
	private List<Header> headers=new ArrayList<Header>();
	
	@ElementList(inline=true, required=false)
	private List<Param> params=new ArrayList<Param>();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Verb getVerb() {
		return verb;
	}
	public void setVerb(Verb verb) {
		this.verb = verb;
	}
	public List<Param> getParams() {
		return params;
	}
	public void setParams(List<Param> params) {
		this.params = params;
	}
	public List<Header> getHeaders() {
		return headers;
	}
	public void setHeaders(List<Header> headers) {
		this.headers = headers;
	}
	
	public String toString() {
		StringBuilder output=new StringBuilder();
		output.append(String.format("    Service.start name=%1$s url=%2$s verb=%3$s parent=%4$s defaultParent=%5$s\n", name, url, verb, parent, defaultParent));
		
		for (Header header:headers) {
			output.append("        ");
			output.append(header.toString());
			output.append("\n");
		}
		for (Param param:params) {
			output.append("        ");
			output.append(param.toString());
			output.append("\n");
		}
		output.append("    Service.end");
		return output.toString();
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public boolean isDefaultParent() {
		return defaultParent;
	}
	public void setDefaultParent(boolean defaultParent) {
		this.defaultParent = defaultParent;
	}
	
}
