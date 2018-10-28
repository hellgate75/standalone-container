package com.services.container.standalone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.services.container.standalone.StandAloneContainer;
import com.services.container.standalone.exceptions.HandlerInvocationException;
import com.services.container.standalone.exceptions.InvalidBeanInstanceException;
import com.services.container.standalone.exceptions.StandAloneContainerAlreadyRunningException;
import com.services.container.standalone.model.BusinessClass;
import com.services.container.standalone.model.SampleBean;
import com.services.container.standalone.model.SampleConfiguration;
import com.services.container.standalone.model.SampleService;
import com.services.container.standalone.utils.AnnotationUtils;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestCases {
	
	SampleConfiguration config = new SampleConfiguration();

	@Test(expected=StandAloneContainerAlreadyRunningException.class)
	public void test01RunUniqueContainerInstance() throws StandAloneContainerAlreadyRunningException {
		StandAloneContainer.reset();
		StandAloneContainer.run();
		StandAloneContainer.run();
	}
	
	@Test(expected=NullPointerException.class)
	public void test02ExpectedNullableInstanceIfNotrunning() {
		StandAloneContainer.reset();
		StandAloneContainer.get();
	}
	
	@Test(timeout=995500)
	public void test03ExpectedScanningAnnotationsFromClasspath() throws StandAloneContainerAlreadyRunningException, InterruptedException {
		StandAloneContainer.reset();
		StandAloneContainer.run();
		//Stay waiting for all entityies and services are loaded in the service
		StandAloneContainer.waitForReadyState();
		int foundAnnotations = StandAloneContainer.get().getEntityRegistry().getDescriptors().size();
		assertEquals("Registry must contain 8 entities", 8, foundAnnotations);
	}

	@Test(timeout=5500)
	public void test04ExpectedComponentAnnotationFromClass() throws StandAloneContainerAlreadyRunningException, InterruptedException, InvalidBeanInstanceException {
		StandAloneContainer.reset();
		StandAloneContainer.run();
		//Stay waiting for all entityies and services are loaded in the service
		StandAloneContainer.waitForReadyState();
		SampleBean foundBean = StandAloneContainer.get().getEntityRegistry().getEntity("sampleTestBean", null);
		assertNotNull("Component Class Entity must exists", foundBean);
	}
	
	@Test(timeout=5500)
	public void test05ExpectedServiceAnnotationFromClass() throws StandAloneContainerAlreadyRunningException, InterruptedException, InvalidBeanInstanceException {
		StandAloneContainer.reset();
		StandAloneContainer.run();
		//Stay waiting for all entityies and services are loaded in the service
		StandAloneContainer.waitForReadyState();
		SampleService foundBean = StandAloneContainer.get().getEntityRegistry().getEntity("sampleTestService", null);
		assertNotNull("Service Class Entity must exists", foundBean);
	}

	@Test(timeout=5500)
	public void test06ExpectedComponentAnnotationFromMethod() throws StandAloneContainerAlreadyRunningException, InterruptedException, InvalidBeanInstanceException {
		StandAloneContainer.reset();
		StandAloneContainer.run();
		//Stay waiting for all entityies and services are loaded in the service
		StandAloneContainer.waitForReadyState();
		SampleBean foundBean = StandAloneContainer.get().getEntityRegistry().getEntity("sampleBean", config);
		assertNotNull("Component Method Entity must exists", foundBean);
	}

	@Test(timeout=5500)
	public void test07ExpectedServiceAnnotationFromMethod() throws StandAloneContainerAlreadyRunningException, InterruptedException, InvalidBeanInstanceException {
		StandAloneContainer.reset();
		StandAloneContainer.run();
		//Stay waiting for all entityies and services are loaded in the service
		StandAloneContainer.waitForReadyState();
		SampleService foundBean = StandAloneContainer.get().getEntityRegistry().getEntity("sampleService", config);
		assertNotNull("Service Method Entity must exists", foundBean);
	}
	
	@Test(timeout=7500)
	public void test08BeansProxyFilter() throws StandAloneContainerAlreadyRunningException, InterruptedException, InvalidBeanInstanceException, HandlerInvocationException {
		StandAloneContainer.reset();
		StandAloneContainer.run();
		//Stay waiting for all entityies and services are loaded in the service
		StandAloneContainer.waitForReadyState();
		int foundAnnotations = StandAloneContainer.get().getEntityRegistry().getDescriptors().size();
		assertEquals("Registry must contain 8 entities", 8, foundAnnotations);
		BusinessClass business = new BusinessClass();
		BusinessClass obj = AnnotationUtils.proxyEntityInstanceReferences(business);
		assertNotNull("Sample Business class must exists", obj);
		assertNotNull("Sample Business class must containes value for Autowired field", obj.getSampleBean());
		assertNotNull("Sample Business class must containes value for Wired field", obj.getSampleService());
	}

	@Test(timeout=7500)
	public void test09BeansProxyFilterFromContainerByClass() throws StandAloneContainerAlreadyRunningException, InterruptedException, InvalidBeanInstanceException, HandlerInvocationException {
		StandAloneContainer.reset();
		StandAloneContainer.run();
		//Stay waiting for all entityies and services are loaded in the service
		StandAloneContainer.waitForReadyState();
		int foundAnnotations = StandAloneContainer.get().getEntityRegistry().getDescriptors().size();
		assertEquals("Registry must contain 8 entities", 8, foundAnnotations);
		BusinessClass obj = StandAloneContainer.get().proxyClassEntities(BusinessClass.class, new Object[0]);
		assertNotNull("Sample Business class must exists", obj);
		assertNotNull("Sample Business class must containes value for Autowired field", obj.getSampleBean());
		assertNotNull("Sample Business class must containes value for Wired field", obj.getSampleService());
	}

	@Test(timeout=7500)
	public void test10BeansProxyFilterFromContainerByInstance() throws StandAloneContainerAlreadyRunningException, InterruptedException, InvalidBeanInstanceException, HandlerInvocationException {
		StandAloneContainer.reset();
		StandAloneContainer.run();
		//Stay waiting for all entityies and services are loaded in the service
		StandAloneContainer.waitForReadyState();
		int foundAnnotations = StandAloneContainer.get().getEntityRegistry().getDescriptors().size();
		assertEquals("Registry must contain 8 entities", 8, foundAnnotations);
		BusinessClass business = new BusinessClass();
		BusinessClass obj = StandAloneContainer.get().proxyInstanceEntities(business);
		assertNotNull("Sample Business class must exists", obj);
		assertNotNull("Sample Business class must containes value for Autowired field", obj.getSampleBean());
		assertNotNull("Sample Business class must containes value for Wired field", obj.getSampleService());
	}
	
	
}
