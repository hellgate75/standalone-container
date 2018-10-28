package com.services.container.standalone.model;

import com.services.container.standalone.StandAloneContainer;
import com.services.container.standalone.annotations.Component;
import com.services.container.standalone.annotations.Execute;
import com.services.container.standalone.annotations.Inject;
import com.services.container.standalone.annotations.Service;
import com.services.container.standalone.exceptions.InvalidBeanInstanceException;

public class SampleConfiguration {

	public SampleConfiguration() {
		super();
	}

	@Component
	public SampleBean sampleBean() {
		return new SampleBean();
	}
	
	@Service
	public SampleService sampleService() {
		return new SampleService();
	}
	
	@Execute
	public SampleContainerService sampleContainerService;
	
	@Component
	public Boolean sampleUseInjectedTestBean(@Inject(entityName="sampleInjectedBean") SampleInjectedBean bean) throws InvalidBeanInstanceException {
		bean = StandAloneContainer.get().proxyInstanceEntities(bean);
		return !bean.getSampleField().isEmpty();
	}
}
