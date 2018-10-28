/**
 * 
 */
package com.services.container.standalone;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.services.container.standalone.domain.EntityDescriptor;
import com.services.container.standalone.domain.EntityType;
import com.services.container.standalone.exceptions.InvalidBeanInstanceException;
import com.services.container.standalone.exceptions.InvalidEntityRegistrationException;
import com.services.container.standalone.utils.AnnotationUtils;
import com.services.container.standalone.utils.EngineUtilities;
import com.services.container.standalone.utils.LogLevel;

/**
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 *
 */
public class DIClassRegistry {
	
	private static Logger LOG = LoggerFactory.getLogger(DIClassRegistry.class);
	
	private static DIClassRegistry instance=null;
	
	private final ConcurrentHashMap<String, EntityDescriptor> entityMap = new ConcurrentHashMap<>(0);
	
	/**
	 * Private constructor
	 */
	private DIClassRegistry() {
		super();
	}
	
	/**
	 * Register entity by type and name into the Entity registry
	 * @param type Type of the entity
	 * @param name  Name of the entity
	 * @param invocationClass Class of the entity
	 * @throws InvalidEntityRegistrationException Exception in case of errors during entity registration
	 */
	public void register(EntityType type, String name, Class<?> invocationClass) throws InvalidEntityRegistrationException {
		this.register(type, name, invocationClass, null);
	}

	/**
	 * Register entity by type and name into the Entity registry
	 * @param type Type of the entity
	 * @param name  Name of the entity
	 * @param invocationClass Class of the entity
	 * @param method Method reference for the entity instance
	 * @throws InvalidEntityRegistrationException Exception in case of errors during entity registration
	 */
	public void register(EntityType type, String name, Class<?> invocationClass, Method method) throws InvalidEntityRegistrationException {
		try {
			if (! containsEntityByName(name) ) {
				if (method == null) {
					Annotation annotation = AnnotationUtils.getAnnotationByType(type, invocationClass);
					if (annotation!= null)
						entityMap.put(name, EntityDescriptor.newInstance(name, type, annotation, invocationClass));
					else
						EngineUtilities.log(LOG, LogLevel.WARN, "Unable to recover annotation for class bean : {}", name);
				}
				else {
					Annotation annotation = AnnotationUtils.getAnnotationByType(type, method);
					if (annotation!= null)
						entityMap.put(name, EntityDescriptor.newInstance(name, type, annotation, method));
					else
						EngineUtilities.log(LOG, LogLevel.WARN, "Unable to recover annotation for method bean : {}", name);
				}
			}
			else
				throw new IllegalStateException("Multiple component registrations for name: " + name + " of type : " + type.name());
		} catch (Exception t) {
			EngineUtilities.log(LOG, LogLevel.ERROR, "Error registering entity", t);
			throw new InvalidEntityRegistrationException("Error registering entity", t);
		}
	}

	/**
	 * Register entity by type and name into the Entity registry
	 * @param type Type of the entity
	 * @param name  Name of the entity
	 * @param invocationClass Class of the entity
	 * @param method Method reference for the entity instance
	 * @param descriptors A list of annotated parameter inject entities from the registry
	 * @throws InvalidEntityRegistrationException Exception in case of errors during entity registration
	 */
	public void register(EntityType type, String name, Class<?> invocationClass, Method method, List<EntityDescriptor> descriptors) throws InvalidEntityRegistrationException {
		try {
			if (! containsEntityByName(name) ) {
				if (method == null) {
					Annotation annotation = AnnotationUtils.getAnnotationByType(type, invocationClass);
					if (annotation!= null)
						entityMap.put(name, EntityDescriptor.newInstance(name, type, annotation, invocationClass, descriptors));
					else
						EngineUtilities.log(LOG, LogLevel.WARN, "Unable to recover annotation for class bean : {}", name);
				}
				else {
					Annotation annotation = AnnotationUtils.getAnnotationByType(type, method);
					if (annotation!= null)
						entityMap.put(name, EntityDescriptor.newInstance(method.getParameters(),name, type, annotation, method, descriptors));
					else
						EngineUtilities.log(LOG, LogLevel.WARN, "Unable to recover annotation for method bean : {}", name);
				}
			}
			else
				throw new IllegalStateException("Multiple component registrations for name: " + name + " of type : " + type.name());
		} catch (Exception t) {
			EngineUtilities.log(LOG, LogLevel.ERROR, "Error registering entity", t);
			throw new InvalidEntityRegistrationException("Error registering entity", t);
		}
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
			EngineUtilities.log(LOG, LogLevel.VERBOSE, "Contains Entity name \"{}\" :  {}", beanName, containsEntityByName(beanName));
			if (containsEntityByName(beanName)) {
				EntityDescriptor descr = getEntityByName(beanName);
				EngineUtilities.log(LOG, LogLevel.VERBOSE, "Recovered descriptor: {} with name {}", descr, beanName);
				return AnnotationUtils.getEntityInstance(beanName, descr, reference);
			}
			return null;
	}
	
	/**
	 * Retrieve an Entity Descriptor by Name
	 * @param name name of the entity
	 * @return the {@link EntityDescriptor} or null
	 */
	public EntityDescriptor getEntityDescriptor(String name) {
		if ( containsEntityByName(name) ) {
			return entityMap.get(name);
		}
		return null;
	}

	/**
	 * Return a copy of the EntityDescriptor map
	 * @return copy of the registry map
	 */
	public Map<String, EntityDescriptor> getDescriptorsMapCopy() {
		HashMap<String, EntityDescriptor> clone = new HashMap<>();
		clone.putAll(this.entityMap);
		return clone;
	}
	
	/**
	 * Retrieve the whole descriptors
	 * @return Descriptors collection
	 */
	public Collection<EntityDescriptor> getDescriptors() {
		return this.entityMap.values();
	}
	
	/**
	 * Verify if an entity has been registered with a specific name
	 * @param name Entity name
	 * @return the existing flag
	 */
	public boolean containsEntityByName(String name) {
		EngineUtilities.log(LOG, LogLevel.VERBOSE, "DI Class Registry keys: {}", Arrays.toString(entityMap.keySet().toArray()));
		EngineUtilities.log(LOG, LogLevel.VERBOSE, "DI Class Registry looking for : {}", name);
		return entityMap.containsKey(name);
	}
	/**
	 * Retrieve the entity descriptor that has been registered with a specific name
	 * @param name Entity name
	 * @return the {@link EntityDescriptor}
	 */
	public final EntityDescriptor getEntityByName(String name) {
		return entityMap.get(name);
	}
	
	/**
	 * Singleton method for the {@link DIClassRegistry} 
	 * @return singleton instance for {@link DIClassRegistry}
	 */
	protected static final synchronized DIClassRegistry get() {
		if (instance == null)
			instance = new DIClassRegistry();
		return instance;
	}
	
	/**
	 * Reset the current registry state
	 */
	protected void reset() {
		AnnotationUtils.resetIndexes();
		this.entityMap.clear();
		DIClassRegistry.instance = null;
	}
	
}
