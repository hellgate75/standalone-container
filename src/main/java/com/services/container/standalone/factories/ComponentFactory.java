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
import com.services.container.standalone.annotations.Component;
import com.services.container.standalone.domain.EntityDescriptor;
import com.services.container.standalone.domain.EntityType;
import com.services.container.standalone.exceptions.InvalidBeanInstanceException;
import com.services.container.standalone.utils.AnnotationUtils;
import com.services.container.standalone.utils.EngineUtilities;
import com.services.container.standalone.utils.LogLevel;

/**
 * Component Factory
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 *
 */
public class ComponentFactory {
	private static final Logger LOG = LoggerFactory.getLogger(ComponentFactory.class);
	
	private static ComponentFactory instance=null;
	
	private static boolean annotationsBuilt=false;
	protected static boolean annotationsBuiltRunning=false;

	protected void buildAnnotations()  {
		if (annotationsBuilt || annotationsBuiltRunning)
			return;
		buildAnnotations(
				new Reflections(EngineUtilities.getTypeScannerBuilder(ConfigurationFactory.getPackages(), ConfigurationFactory.getUrls())),
				new Reflections(EngineUtilities.getMethodScannerBuilder(ConfigurationFactory.getPackages(), ConfigurationFactory.getUrls()))
				);
	  }
	
	/**
	 * Scans, Discovers and Creates annotation of type {@link Component}
	 * @param reflections Class scan annotations {@link Reflections}
	 * @param methodReflections method scan annotation {@link Reflections}
	 */
	protected void buildAnnotations(Reflections reflections, Reflections methodReflections) {
		if (annotationsBuilt || annotationsBuiltRunning)
			return;
		EngineUtilities.log(LOG, LogLevel.INFO, "Scanning Component(s)..."); 
	    Set<Class<?>> types = reflections.getTypesAnnotatedWith(Component.class);
	    EngineUtilities.log(LOG, LogLevel.VERBOSE, "Class Component Annotations : {}", types.size());
	    AnnotationUtils.scanAndRegisterClassAnnotations(types, EntityType.COMPONENT);
	    Set<Method> mTypes = methodReflections.getMethodsAnnotatedWith(Component.class);
	    EngineUtilities.log(LOG, LogLevel.VERBOSE, "Method Component Annotations : {}", mTypes.size());
	    AnnotationUtils.scanAndRegisterMethodAnnotations(mTypes, EntityType.METHOD_COMPONENT);
	    markComplete();
	}
	
	private static void markComplete() {
		ComponentFactory.annotationsBuiltRunning = false;
		ComponentFactory.annotationsBuilt=true;
	}
	
	/**
	 * Return the required bean or null
	 * @param <T> Template class dynamically acquired from declaration
	 * @param beanName name of the Bean to retrieve
	 * @param reference instance of the object containing the method, if needed or null
	 * @return The Bean instance
	 * @throws InvalidBeanInstanceException Thrown when any issue occurs during bean retrieve
	 */
	public <T> T getBean(String beanName, Object reference) throws InvalidBeanInstanceException {
		if (StandAloneContainer.get().getClassRegistry().containsEntityByName(beanName)) {
			EntityDescriptor descr = StandAloneContainer.get().getClassRegistry().getEntityByName(beanName);
			return AnnotationUtils.getEntityInstance(beanName, descr, reference);
		}
		return null;
	}
	
	/**
	 * Private Constructor
	 */
	private ComponentFactory() {
		super();
		StandAloneContainer.get().getClassRegistry();
		buildAnnotations();
	}
	
	/**
	 * Private Constructor
	 * @param reflections Type {@link Reflections} seeker
	 * @param methodReflections Type method {@link Reflections} seeker
	 */
	private ComponentFactory(Reflections reflections, Reflections methodReflections) {
		super();
		StandAloneContainer.get().getClassRegistry();
		buildAnnotations(reflections, methodReflections);
	}

	/**
	 * Return singleton instance of the {@link ComponentFactory}
	 * @return the {@link ComponentFactory}
	 */
	public static final synchronized ComponentFactory get() {
		if (instance == null)
			instance = new ComponentFactory();
		return instance;
	}

	/**
	 * Return singleton instance of the {@link ComponentFactory}
	 * @param reflections Type {@link Reflections} seeker
	 * @param methodReflections Type method {@link Reflections} seeker
	 * @return the {@link ComponentFactory}
	 */
	public static final synchronized ComponentFactory get(Reflections reflections, Reflections methodReflections) {
		if (instance == null)
			instance = new ComponentFactory(reflections, methodReflections);
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
