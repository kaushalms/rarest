package com.rarest;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        InputStream isConfig;
		try {
			isConfig = getAssets().open("github_api.xml");
			Ra.loadApi("github_api", isConfig);
	        Ra ra=new Ra("github_api");
	        ra.service("getUserRepositories").set("userId","2").set("sort","true").execute();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        
        
        
    }

   
}
