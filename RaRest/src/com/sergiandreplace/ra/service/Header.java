package com.sergiandreplace.ra.service;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Text;

public class Header {
	
	@Attribute(required=true)
	private String name;
	
	@Attribute(required=false)
	private String alias;
	
	
	
	@Text(required=false)
	private String value;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	

	public String getAlias() {
		if (alias==null) {
			return name;
		}else{
			return alias;
		}	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	public String toString() {
		return String.format("Header name=%1$s alias%2$s value=%3$s",name,getAlias(),value);
	}
}
