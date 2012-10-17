package com.sergiandreplace.ra;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Ra ra=new Ra(this,"github_api");
        ra.service("updateCurrentUser");
        
    }

   
}
