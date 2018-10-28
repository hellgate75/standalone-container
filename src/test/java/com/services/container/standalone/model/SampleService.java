package com.services.container.standalone.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.services.container.standalone.annotations.Service;
import com.services.container.standalone.utils.EngineUtilities;
import com.services.container.standalone.utils.LogLevel;

@Service(name="sampleTestService")
public class SampleService {
	private static final Logger LOG = LoggerFactory.getLogger(SampleService.class);

	public SampleService() {
		super();
	}
	
	public void doStuff() {
		EngineUtilities.log(LOG, LogLevel.WARN, "Service is running ...");
	}

}
