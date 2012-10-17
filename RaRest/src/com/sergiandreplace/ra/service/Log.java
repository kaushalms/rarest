package com.sergiandreplace.ra.service;

import org.simpleframework.xml.Attribute;

public class Log {
	
	@Attribute(required=true)
	private String type;
	@Attribute(required=false)
	private Debug.Switcher status=Debug.Switcher.on;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Debug.Switcher getStatus() {
		return status;
	}
	public void setStatus(Debug.Switcher status) {
		this.status = status;
	}
	
	public String toString() {
		return String.format("Log type=%s status=%s", type, status.toString());
	}
	
}
