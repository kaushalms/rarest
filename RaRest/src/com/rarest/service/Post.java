package com.rarest.service;

import org.simpleframework.xml.Attribute;

public class Post {
	@Attribute(required=true)
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Post clone() {
		Post other=new Post();
		other.setName(name);
		return other;
	}
}
