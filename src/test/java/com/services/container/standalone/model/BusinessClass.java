package com.services.container.standalone.model;

import com.services.container.standalone.annotations.Autowired;
import com.services.container.standalone.annotations.Wired;

public class BusinessClass {

	@Autowired
	private SampleBean sampleBean;
	
	@Wired(entityName="sampleTestService")
	private SampleService sampleService;
	
	public BusinessClass() {
		super();
	}

	/**
	 * @return the sampleBean
	 */
	public SampleBean getSampleBean() {
		return sampleBean;
	}

	/**
	 * @return the sampleService
	 */
	public SampleService getSampleService() {
		return sampleService;
	}
	
	

}
