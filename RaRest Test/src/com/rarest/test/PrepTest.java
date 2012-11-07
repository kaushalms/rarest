package com.rarest.test;

import android.util.Log;

import com.rarest.processor.Preprocessor;
import com.rarest.service.Service;

public class PrepTest implements Preprocessor{

	@Override
	public boolean preprocess(Service service) {
		Log.d("RA.Prep","Preprocessing, hoorray!!!");
		return true;
	}

}
