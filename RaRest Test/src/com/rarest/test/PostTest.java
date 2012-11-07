package com.rarest.test;

import android.util.Log;

import com.rarest.engine.Response;
import com.rarest.processor.Postprocessor;
import com.rarest.service.Service;

public class PostTest implements Postprocessor{



	@Override
	public int postprocess(Service service, Response response, int count) {
		Log.d("RA.Post","Postrocessing, response http:" + response.getStatus());
		return POST_CONTINUE;
	}

}
