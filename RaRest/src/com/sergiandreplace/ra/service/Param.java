package com.sergiandreplace.ra.service;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Text;

public class Param {
	public static enum ParamType {rest,query,body};
	
	@Attribute(required=true)
	private ParamType type;
	
	@Attribute(required=true)
	private String name;
	
	@Attribute(required=false)
	private String alias;
	
	@Attribute(required=false)
	private boolean mandatory=true;
	
	@Text(required=false)
	private String value;
	
	
	public ParamType getType() {
		return type;
	}
	public void setType(ParamType type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAlias() {
		if (alias==null) {
			return name;
		}else{
			return alias;
		}
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public boolean isMandatory() {
		return mandatory;
	}
	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public String toString() {
		return String.format("Param name=%1$s alias=%2$s type=%3$s mandatory=%4$s value=%5$s",name,getAlias(),type,mandatory,value);
	}
}
