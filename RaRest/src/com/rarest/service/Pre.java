package com.rarest.service;

import org.simpleframework.xml.Attribute;

public class Pre {
	@Attribute(required=true)
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Pre clone() {
		Pre other=new Pre();
		other.setName(name);
		return other;
	}
}
