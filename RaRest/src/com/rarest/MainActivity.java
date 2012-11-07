package com.rarest;

import android.app.Activity;
import android.os.Bundle;
import com.rarest.R;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Ra ra=new Ra(this,"github_api");
        ra.service("getUserRepositories").set("userId","2").set("sort","true").execute();
        
        
    }

   
}
