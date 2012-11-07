package com.rarest.processor;

import com.rarest.service.Service;

public interface Preprocessor {
	/**
	 * Pre process the service before being executed
	 * @param service the service to postprocess
	 * @return true if everything is ok and should continue. False if the service should be stopped;
	 */
	public boolean preprocess(Service service);
}
