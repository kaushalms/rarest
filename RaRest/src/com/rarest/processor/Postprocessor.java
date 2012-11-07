package com.rarest.processor;

import com.rarest.engine.Response;
import com.rarest.service.Service;

public interface Postprocessor {
	public static final int POST_CONTINUE=0;
	public static final int POST_RETRY=1;
	public static final int POST_STOP=0;
	

	public int postprocess(Service service, Response response, int count);
}
