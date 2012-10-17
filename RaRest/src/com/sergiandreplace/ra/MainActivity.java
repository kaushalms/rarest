package com.sergiandreplace.ra;

import java.io.File;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import com.sergiandreplace.ra.service.Api;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Serializer serializer=new Persister();
        Api api;
		try {
			api = serializer.read(Api.class, getAssets().open("github_api.xml"));
			Log.d("RA",api.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }

   
}
