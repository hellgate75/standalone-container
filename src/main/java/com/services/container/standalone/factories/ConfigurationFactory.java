/**
 * 
 */
package com.services.container.standalone.factories;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.services.container.standalone.StandAloneContainer;
import com.services.container.standalone.annotations.Configuration;
import com.services.container.standalone.domain.EntityDescriptor;
import com.services.container.standalone.domain.EntityType;
import com.services.container.standalone.exceptions.InvalidBeanInstanceException;
import com.services.container.standalone.utils.AnnotationUtils;
import com.services.container.standalone.utils.EngineUtilities;
import com.services.container.standalone.utils.LogLevel;

/**
 * Configuration Factory
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 *
 */
public class ConfigurationFactory {
	private static final Logger LOG = LoggerFactory.getLogger(ConfigurationFactory.class);
	
	private static ConfigurationFactory instance=null;
	
	private static List<URL> urlsList = new ArrayList<>(0);
	private static List<String> packagesList = new ArrayList<>(0);
	private static List<Class<?>> classList = new ArrayList<>(0);
	
	private static boolean annotationsBuilt=false;
	protected static boolean annotationsBuiltRunning=false;

	/**
	 * Scans, Discovers and Creates annotation of type {@link Configuration}
	 */
	protected static void buildAnnotations()  {
		if (annotationsBuilt || annotationsBuiltRunning)
			return;
		annotationsBuiltRunning = true;
		EngineUtilities.log(LOG, LogLevel.INFO, "Scanning Configuration(s)..."); 
		List<String> packages = new ArrayList<>(0);
		packages.add("*");
	    Reflections reflections = new Reflections(EngineUtilities.getFullTypeScannerReflections());
	    Set<Class<?>> types = reflections.getTypesAnnotatedWith(Configuration.class);
	    EngineUtilities.log(LOG, LogLevel.VERBOSE, "Class Configuration Annotations : {}", types.size());
	    List<Configuration> configAnnotationList = AnnotationUtils.scanAndRegisterClassAnnotations(types, EntityType.CONFIGURATION);
	    configAnnotationList.forEach( c -> {
	    	List<String> packageElements = Arrays.asList(c.packages());
	    	if (c.packages()!=null && c.packages().length>0 && !packageElements.contains("*")) {
	    		Arrays.asList(c.packages()).forEach( cs -> {
	    			if (! packagesList.contains(cs)) {
	    				packagesList.add(cs);
	    			}
	    		} );
	    	}
	    	else if (c.packages()!=null && c.packages().length>0 && packageElements.contains("*")){
	    		packagesList.clear();
	    		packagesList.add("");
	    	}
    		classList.add(c.annotationType().getDeclaringClass());
	    	if (c.urls()!=null && c.urls().length>0) {
		    	Arrays.asList(c.urls()).stream().forEach( s -> {
		    		try {
						urlsList.add(new URL(s));
					} catch (Exception e) {
						EngineUtilities.log(LOG, LogLevel.ERROR, "Error creating URL from string {}", s, e);
					}
		    	});
	    	}
	    	EngineUtilities.log(LOG, LogLevel.VERBOSE, "Original packages : {}", packageElements);
	    });
    	if (packagesList.contains("") && packagesList.size()>1) {
    		packagesList.clear();
    		packagesList.add("");
    	}
	    AnnotationUtils.scanAndRegisterClassAnnotations(types, EntityType.METHOD_COMPONENT);
	    AnnotationUtils.scanAndRegisterClassAnnotations(types, EntityType.METHOD_SERVICE);
    	EngineUtilities.log(LOG, LogLevel.VERBOSE, "Found packages : {}", packagesList);
		annotationsBuiltRunning = false;
		annotationsBuilt=true;
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
	private ConfigurationFactory() {
		super();
		StandAloneContainer.get().getClassRegistry();
		buildAnnotations();
	}

	/**
	 * Return singleton instance of the {@link ConfigurationFactory}
	 * @return the {@link ConfigurationFactory}
	 */
	public static final synchronized ConfigurationFactory get() {
		if (instance == null)
			instance = new ConfigurationFactory();
		return instance;
	}
	
	public static final List<String> getPackages() {
		if (packagesList==null || packagesList.isEmpty()) {
			packagesList = new ArrayList<>();
		}
		return packagesList; 
	}
	
	public static final List<URL> getUrls() {
		return urlsList; 
	}
	
	/**
	 * Reset factory singleton instance
	 */
	public static void reset() {
		instance=null;
		packagesList.clear();
		urlsList.clear();
		annotationsBuilt=false;
	}

}
