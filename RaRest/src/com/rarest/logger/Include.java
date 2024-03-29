package com.rarest.logger;

import org.simpleframework.xml.Attribute;


public class Include {
	
	@Attribute(required=true)
	private String type;
	@Attribute(required=false)
	private Boolean show=true;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	
	public String toString() {
		return String.format("Include type=%s status=%s", type, getShow().toString());
	}
	public Boolean getShow() {
		return show;
	}
	public void setShow(Boolean show) {
		this.show = show;
	}
	
}
