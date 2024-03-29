package com.rarest.xml;

import java.io.InputStream;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import com.rarest.exception.ConfigFileException;
import com.rarest.service.Api;

public class XmlLoader {

	public static Api load(InputStream isXml)  {
		Serializer serializer=new Persister();
		try {
			return serializer.read(Api.class, isXml);
		} catch (Exception e) {
			throw new ConfigFileException(e);
		}
	}
}
