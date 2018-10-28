package com.services.container.standalone.model;

import com.services.container.standalone.annotations.Component;

@Component(name="sampleTestBean")
public class SampleBean {
	
	private String sampleField=null;

	public SampleBean() {
		super();
	}

	/**
	 * @return the sampleField
	 */
	public String getSampleField() {
		return sampleField;
	}

	/**
	 * @param sampleField the sampleField to set
	 */
	public void setSampleField(String sampleField) {
		this.sampleField = sampleField;
	}

}
