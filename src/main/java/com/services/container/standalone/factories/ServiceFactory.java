/**
 * 
 */
package com.services.container.standalone.factories;

import java.lang.reflect.Method;
import java.util.Set;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.services.container.standalone.StandAloneContainer;
import com.services.container.standalone.annotations.Service;
import com.services.container.standalone.domain.EntityDescriptor;
import com.services.container.standalone.domain.EntityType;
import com.services.container.standalone.exceptions.InvalidBeanInstanceException;
import com.services.container.standalone.utils.AnnotationUtils;
import com.services.container.standalone.utils.EngineUtilities;
import com.services.container.standalone.utils.LogLevel;

/**
 * Service Factory
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 *
 */
public class ServiceFactory {
	
	private static final Logger LOG = LoggerFactory.getLogger(ServiceFactory.class);
	
	private static ServiceFactory instance=null;
	
	private static boolean annotationsBuilt=false;
	protected static boolean annotationsBuiltRunning=false;

	private void buildAnnotations() {
		if (annotationsBuilt || annotationsBuiltRunning)
			return;
		buildAnnotations(
				new Reflections(EngineUtilities.getTypeScannerBuilder(ConfigurationFactory.getPackages(), ConfigurationFactory.getUrls())),
				new Reflections(EngineUtilities.getMethodScannerBuilder(ConfigurationFactory.getPackages(), ConfigurationFactory.getUrls()))
				);
	}

	/**
	 * Scans, Discovers and Creates annotation of type {@link Service}
	 * @param reflections Class scan annotations {@link Reflections}
	 * @param methodReflections method scan annotation {@link Reflections}
	 */
	protected void buildAnnotations(Reflections reflections, Reflections methodReflections) {
		if (annotationsBuilt || annotationsBuiltRunning)
			return;
		EngineUtilities.log(LOG, LogLevel.INFO, "Scanning Service(s)..."); 
	    Set<Class<?>> types = reflections.getTypesAnnotatedWith(Service.class);
	    EngineUtilities.log(LOG, LogLevel.VERBOSE, "Class Service Annotations : {}", types.size());
	    AnnotationUtils.scanAndRegisterClassAnnotations(types, EntityType.SERVICE);
	    Set<Method> mTypes = methodReflections.getMethodsAnnotatedWith(Service.class);
	    EngineUtilities.log(LOG, LogLevel.VERBOSE, "Method Service Annotations : {}", mTypes.size());
	    AnnotationUtils.scanAndRegisterMethodAnnotations(mTypes, EntityType.METHOD_SERVICE);
	    markComplete();
	 }
	
	private static void markComplete() {
	    ServiceFactory.annotationsBuiltRunning = false;
	    ServiceFactory.annotationsBuilt=true;
	}
	
	/**
	 * Return the required bean or null
	 * @param <T> Template class dynamically acquired from declaration
	 * @param beanName name of the Bean to retrieve
	 * @param reference instance of the object containing the method, if needed or null
	 * @return The Bean instance
	 * @throws InvalidBeanInstanceException Thrown when any issue occurs during bean retrieve
	 */
	public <T> T getEntity(String beanName, Object reference) throws InvalidBeanInstanceException {
			if (StandAloneContainer.get().getClassRegistry().containsEntityByName(beanName)) {
				EntityDescriptor descr = StandAloneContainer.get().getClassRegistry().getEntityByName(beanName);
				return AnnotationUtils.getEntityInstance(beanName, descr, reference);
			}
			return null;
	}
	
	/**
	 * Private Constructor
	 * @param reflections Type {@link Reflections} seeker
	 * @param methodReflections Type method {@link Reflections} seeker
	 */
	private ServiceFactory(Reflections reflections, Reflections methodReflections) {
		super();
		StandAloneContainer.get().getClassRegistry();
		buildAnnotations(reflections, methodReflections);
	}
	
	/**
	 * Private Constructor
	 */
	private ServiceFactory() {
		super();
		StandAloneContainer.get().getClassRegistry();
		buildAnnotations();
	}

	/**
	 * Return singleton instance of the {@link ServiceFactory}
	 * @return the {@link ServiceFactory} singleton
	 */
	public static final synchronized ServiceFactory get() {
		if (instance == null)
			instance = new ServiceFactory();
		return instance;
	}

	/**
	 * Return singleton instance of the {@link ServiceFactory}
	 * @param reflections Type {@link Reflections} seeker
	 * @param methodReflections Type method {@link Reflections} seeker
	 * @return the {@link ServiceFactory} singleton
	 */
	public static final synchronized ServiceFactory get(Reflections reflections, Reflections methodReflections) {
		if (instance == null)
			instance = new ServiceFactory(reflections, methodReflections);
		return instance;
	}

	
	/**
	 * Reset factory singleton instance
	 */
	public static void reset() {
		instance=null;
		annotationsBuilt=false;
	}
}
