package com.rarest.engine;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpMessage;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.text.TextUtils;

import com.rarest.exception.NullHeaderException;
import com.rarest.exception.UrlFieldNotFoundInParams;
import com.rarest.exception.UrlFieldTypeShouldBeRest;
import com.rarest.exception.VerbNotAllowedException;
import com.rarest.processor.Postprocessor;
import com.rarest.processor.Preprocessor;
import com.rarest.service.Param;
import com.rarest.service.Param.ParamType;
import com.rarest.service.Post;
import com.rarest.service.Pre;
import com.rarest.service.Service;
import com.rarest.service.Service.Verb;

public class Harvester {
	private static final Pattern urlFieldPattern=Pattern.compile("\\{.*?\\}");
	
	private Service service;
	private DefaultHttpClient httpClient=new DefaultHttpClient();
	

	private String baseUrl;
	
	
	public Harvester(String baseUrl,Service service) {
		this.service=service;
		this.baseUrl=baseUrl;
	}
	public Response execute() {
		int count=0;
		int postResult;
		executePres();
		Response response=null;
		HttpResponse httpResponse;
		HttpRequestBase request=generateRequest();
		generateHeaders(request);
		try {
			request.setURI(new URI(generateUrl()));
			if (service.getVerb()==Verb.post || service.getVerb()==Verb.put || service.getVerb()==Verb.patch) {
				((HttpEntityEnclosingRequestBase)request).setEntity(generateEntity());
			}
			do {
				count++;
				httpResponse=httpClient.execute(request);
				response=new Response(httpResponse);
				postResult=executePosts(service,response,count);
			} while (postResult==Postprocessor.POST_RETRY);
		} catch (Exception e) {
			response=new Response(e);
		} 
		
		return response;
		
	}
	
	private int executePosts(Service service, Response response, int count) {
		for (Post post:service.getPosts()) {
			Postprocessor p;
			try {
				p=(Postprocessor) Class.forName(post.getName()).newInstance();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			int result=p.postprocess(service, response, count);
			if (result!=Postprocessor.POST_CONTINUE) {
				return result;
			}
		}
		return Postprocessor.POST_CONTINUE;
	}
	
	
	private boolean executePres() {
		for(Pre pre:service.getPres()) {
			Preprocessor p;
			try {
				p=(Preprocessor) Class.forName(pre.getName()).newInstance();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			if (!p.preprocess(service)) return false;
		}
		return true;
		
	}
	/***
	 * Generates a request based on the verb of the service
	 * @return An extension of HttpRequestBase (HttpDelete, HttpGet, etc)
	 */
	private HttpRequestBase generateRequest() {
		HttpRequestBase request;
		switch (service.getVerb()) {
		case delete:
			request=new HttpDelete();
			break;
		case get:
			request=new HttpGet();
			break;
		case head:
			request=new HttpHead();
			break;
		case patch:
			request=new HttpPatch();
			break;
		case post:
			request=new HttpPost();
			break;
		case put:
			request=new HttpPut();
			break;
		default:
			throw new VerbNotAllowedException();
		}
		return request;
	}
	
	/**
	 * Sets the header in an http request from service headers.
	 * 
	 * @param httpRequest
	 *            the get/post object to get initial headers
	 * @return an array of Header objects
	 */
	private Header[] generateHeaders(HttpMessage httpRequest) {
		for (com.rarest.service.Header header:service.getHeaders()) {
		
			String key = header.getName();
			if (header.getValue()==null) {
				throw new NullHeaderException(header.getName());
			}
			String value = header.getValue();// URLEncoder.encode(e.getValue());
			httpRequest.addHeader(key, value);
		}
		return httpRequest.getAllHeaders();

	}

	
	private String generateUrl() {
		String url=baseUrl;
		if (!url.endsWith("/")) {
			url=url+"/";
		}
		String path=service.getUrl();
		if (path.startsWith("/")) {
			path=path.substring(1);
		}
		url=url+path;
		url=setUrlFields(url);
		url=setRestParams(url);
		return url;
		
	}
	
	/**
	 * Matches all url fields with corresponding Rest parameters. 
	 *
	 * @param url the String url to perform the replaces
	 * @return Url with replaced fields
	 */
	private String setUrlFields(String url) {
		String newUrl=url;
		Matcher urlFieldMatcher=urlFieldPattern.matcher(url);
		while (urlFieldMatcher.find()) {
			String field=urlFieldMatcher.group();
			field=field.substring(1, field.length()-1);
			if (!service.hasParam(field)) {
				throw new UrlFieldNotFoundInParams(field);
			}else{
				Param param=service.getParam(field);
				if (param.getType()!=ParamType.rest) {
					throw new UrlFieldTypeShouldBeRest(field); 
				}else{
					String value=service.getParam(field).getValue();
					if (value!=null && !TextUtils.isEmpty(value)) {
						value=URLEncoder.encode(value);
						newUrl=newUrl.replaceAll("\\{"+field+"\\}", service.getParam(field).getValue());
					}
				}
			}
		}
		return newUrl;
	}
	
	private String setRestParams(String url) {
		StringBuilder newUrl=new StringBuilder(url);
		boolean first=true;
		if (url.contains("?")) {
			newUrl.append("&");
		}else{
			newUrl.append("?");
		}
		for (Param param:service.getParams()) {
			String value=param.getValue();
			if (param.getType()==ParamType.query && value!=null && !TextUtils.isEmpty(value)) {
				if (first) {
					first=false;
				}else{
					newUrl.append("&");
				}
				newUrl.append(param.getName());
				newUrl.append("=");
				newUrl.append(URLEncoder.encode(value));
			}
		}
		
		return newUrl.toString();
	}
	
	/**
	 * return the entities array.

	 * @return an Entitiy object with all the parameters
	 */
	public HttpEntity generateEntity() {
		List<NameValuePair> bodyParams = new ArrayList<NameValuePair>();
		for (Param param:service.getParams()) {
			if (param.getType()==ParamType.body && param.getValue()!=null && !TextUtils.isEmpty(param.getValue())) {
				bodyParams.add(new BasicNameValuePair(param.getAlias(),param.getValue()));
			}
		}
		
		UrlEncodedFormEntity ent = null;
		try {
			ent = new UrlEncodedFormEntity(bodyParams);
		} catch (UnsupportedEncodingException e) {
			ent = null;
		}
		return ent;
	}
}
