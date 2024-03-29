package com.rarest.service;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;

import com.rarest.exception.HeaderNotFoundException;
import com.rarest.exception.ParamAliasNotFoundException;
import com.rarest.exception.ParamNotFoundException;
import com.rarest.logger.Logger;

public class Service {
	
	public static enum Verb {get,post,put,delete,patch,head};

	private Logger logger;
	
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
	
	@ElementList(inline=true, required=false)
	private List<Pre> pres=new ArrayList<Pre>();
	
	@ElementList(inline=true, required=false)
	private List<Post> posts=new ArrayList<Post>();

	public Service() {}
	
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
		for (Pre pre:pres) {
			output.append("        ");
			output.append("Pre: "+pre.getName());
			output.append("\n");
		}
		for (Post post:posts) {
			output.append("        ");
			output.append("Post: "+post.getName());
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
	
	public Service clone() {
		Service cloned=new Service();
		cloned.setName(name);
		cloned.setUrl(url);
		cloned.setVerb(verb);
		cloned.setDefaultParent(defaultParent);
		cloned.setParent(parent);
		for(Param param:params) {
			cloned.getParams().add(param.clone());
		}
		for(Header header:headers) {
			cloned.getHeaders().add(header.clone());
		}
		for (Pre pre:pres) {
			cloned.getPres().add(pre.clone());
		}
		for (Post post:posts) {
			cloned.getPosts().add(post.clone());
		}
		return cloned;
	}
	
	public static Service merge(Service template, Service service) {
		Service result;
		
		result=template.clone();
		result.setName(service.getName());
		result.setParent(service.getParent());
		result.setDefaultParent(service.isDefaultParent());
		if (service.getUrl()!=null) {
			result.setUrl(service.getUrl());
		}
		if (service.getVerb()!=null) {
			result.setVerb(service.getVerb());
		}
		
		for (Param param:service.getParams()) {
			if (result.hasParam(param.getName())) {
				result.getParam(param.getName()).merge(param);
			}else{
				result.getParams().add(param);
			}
		}
		for (Header header:service.getHeaders()) {
			if (result.hasHeader(header.getName())) {
				result.getHeader(header.getName()).merge(header);
			}else{
				result.getHeaders().add(header);
			}
		}
		for (Pre pre:service.getPres()) {
			if(!result.hasPre(pre.getName())) {
				result.getPres().add(pre);
			}
		}
		for (Post post:service.getPosts()) {
			if(!result.hasPost(post.getName())) {
				result.getPosts().add(post);
			}
		}
		return result;
		
	}
	
	public boolean hasParam(String paramName) {
		boolean found=false;
		for (Param param:params) {
			if (paramName.equals(param.getName())) {
				found=true;
				break;
			}
		}
		return found;
	}
	public boolean hasParamByAlias(String paramAlias) {
		boolean found=false;
		for (Param param:params) {
			if (paramAlias.equals(param.getAlias())) {
				found=true;
				break;
			}
		}
		return found;
	}
	public boolean hasHeader(String headerName) {
		boolean found=false;
		for (Header header:headers) {
			if (headerName.equals(header.getName())) {
				found=true;
				break;
			}
		}
		return found;
	}
	public boolean hasPre(String preName) {
		boolean found=false;
		for (Pre pre:pres) {
			if (preName.equals(pre.getName())) {
				found=true;
				break;
			}
		}
		return found;
	}
	public boolean hasPost(String postName) {
		boolean found=false;
		for (Post post:posts) {
			if (postName.equals(post.getName())) {
				found=true;
				break;
			}
		}
		return found;
	}
	public boolean hasHeaderByAlias(String headerAlias) {
		boolean found=false;
		for (Header header:headers) {
			if (headerAlias.equals(header.getAlias())) {
				found=true;
				break;
			}
		}
		return found;
	}
	public Param getParam(String paramName) {
		Param found=null;
		for (Param param:params) {
			if (paramName.equals(param.getName())) {
				found=param;
				break;
			}
		}
		if (found==null) {
			throw new ParamNotFoundException(paramName);
		}
		return found;
	}
	public Param getParamByAlias(String paramAlias) {
		Param found=null;
		for (Param param:params) {
			if (paramAlias.equals(param.getAlias())) {
				found=param;
				break;
			}
		}
		if (found==null) {
			throw new ParamAliasNotFoundException(paramAlias);
		}
		return found;
	}
	public Header getHeader(String headerName) {
		Header found=null;
		for (Header header:headers) {
			if (headerName.equals(header.getName())) {
				found=header;
				break;
			}
		}
		if (found==null) {
			throw new HeaderNotFoundException(headerName);
		}
		return found;
	}

	public List<Pre> getPres() {
		return pres;
	}

	public void setPres(List<Pre> pres) {
		this.pres = pres;
	}

	public List<Post> getPosts() {
		return posts;
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}
}
