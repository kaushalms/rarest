package com.rarest.test;

import android.test.AndroidTestCase;
import android.util.Log;

import com.rarest.Ra;
import com.rarest.engine.Response;

public class XmlTest extends AndroidTestCase {
	
	public void testLoadXml() {
		 Ra ra=new Ra(getContext(),"condis_api");
		 Log.d("Ra.Test",ra.getApi().toString());
		 Response r=ra.service("getAds").set("id","1").execute();
		 assertFalse(r.isError());
		 
	}

}
