/**
 * 
 */
package com.services.container.standalone;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.services.container.standalone.domain.EntityDescriptor;
import com.services.container.standalone.domain.EntityType;
import com.services.container.standalone.exceptions.InvalidBeanInstanceException;
import com.services.container.standalone.factories.FactoryHelper;
import com.services.container.standalone.utils.EngineUtilities;
import com.services.container.standalone.utils.LogLevel;

/**
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 *
 */
/**
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 *
 */
public class DIBeanRegistry {

	private static final Logger LOG = LoggerFactory.getLogger(DIBeanRegistry.class);

	private static DIBeanRegistry instance=null;
	
	private AtomicInteger taskCounter = new AtomicInteger(0);
	
	/**
	 * Private constructor
	 */
	private DIBeanRegistry() {
		super();
		/*Scans all included classes from the Annotations*/
		EngineUtilities.log(LOG, LogLevel.INFO, "Scan packages...");
		FactoryHelper.createAllFactoryAnnotations();
	}
	
	/**
	 * Retrieve a bean from the registry
	 * @param <T> Template class dynamically acquired from declaration
	 * @param beanName bean name
	 * @param reference bean method object reference
	 * @return a bean or null if bean doesn't exists
	 * @throws InvalidBeanInstanceException occurs when error happen in recovering the bean or its descriptor
	 */
	public <T> T getEntity(String beanName, Object reference) throws InvalidBeanInstanceException {
		return DIClassRegistry.get().getEntity(beanName, reference);
	}
	
	/**
	 * Retrieve all Annotations of a specific type
	 * @param <T> Annotation return type, dynamically recovered by runtime
	 * @param annotationType type of annotation to recover
	 * @return List of retrieved annotations
	 */
	@SuppressWarnings("unchecked")
	public <T extends Annotation> List<T> getAnnotationsByType(EntityType annotationType) {
		return DIClassRegistry.get().getDescriptors().stream()
		.filter( (ed) -> {
			return ed.getType()==annotationType;
		} )
		.map( e -> (T) e.getAnnotation() )
		.collect(Collectors.toList());
		
	}

	/**
	 * Retrieve all bean of a specific annotation type
	 * @param annotationType type of annotation to recover entities
	 * @param reference Reference object to be applied to all annotations to give instance to method annotations
	 * @return List of beans
	 */
	public List<Object> getEntitiesByType(EntityType annotationType, Object reference) {
		return DIClassRegistry.get().getDescriptors().stream()
		.filter( (ed) -> {
			return ed.getType()==annotationType;
		} )
		.map( e ->  {
			try {
				return getEntity(e.getEntityName(), reference);
			} catch (InvalidBeanInstanceException ex) {
			}
			return null;
		} )
		.collect(Collectors.toList());
		
	}
	
	/**
	 * Get list of Entity descriptors stored in the registry
	 * @return list of entity descriptors in the system
	 */
	public Collection<EntityDescriptor> getDescriptors() {
		return DIClassRegistry.get().getDescriptors();
	}
	
	/**
	 * Singleton method for the {@link DIBeanRegistry} 
	 * @return singleton instance for {@link DIBeanRegistry}
	 */
	public static final synchronized DIBeanRegistry get() {
		if (instance == null)
			instance = new DIBeanRegistry();
		return instance;
	}
	
	protected boolean ready() {
		return taskCounter.get() == 0;
	}
	
	public static void reset() {
		instance=null;
		DIClassRegistry.get().reset();
	}
	
}
