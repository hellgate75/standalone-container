/**
 * 
 */
package com.services.container.standalone.handler;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.services.container.standalone.StandAloneContainer;
import com.services.container.standalone.annotations.Autowired;
import com.services.container.standalone.annotations.Execute;
import com.services.container.standalone.annotations.Service;
import com.services.container.standalone.annotations.Wired;
import com.services.container.standalone.exceptions.HandlerInvocationException;
import com.services.container.standalone.exceptions.InvalidBeanInstanceException;
import com.services.container.standalone.exceptions.ServiceExecutionException;
import com.services.container.standalone.exceptions.ServiceInvocationException;
import com.services.container.standalone.model.IContainerService;
import com.services.container.standalone.utils.AnnotationUtils;
import com.services.container.standalone.utils.EngineUtilities;
import com.services.container.standalone.utils.LogLevel;

/**
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 *
 */
public class AnnotationScannerHandler<T> implements IHandler<T> {
	private static final Logger LOG = LoggerFactory.getLogger(AnnotationScannerHandler.class);
	
	/* (non-Javadoc)
	 * @see com.services.container.standalone.handler.IHandler#handle(java.lang.Object)
	 */
	@Override
	public T handle(T target) throws HandlerInvocationException {
		if (target == null) {
			return target;
		}
		EngineUtilities.log(LOG, LogLevel.VERBOSE, "Target Class Name: {}", target.getClass().getName());
	    try {
			Reflections reflections = new Reflections(EngineUtilities.getFieldScannerBuilderByClassPackage(target));
			List<Field> autowiredFields = reflections.getFieldsAnnotatedWith(Autowired.class).stream().filter( a -> a.getDeclaringClass().isAssignableFrom(target.getClass()) ).collect(Collectors.toList());
			EngineUtilities.log(LOG, LogLevel.VERBOSE, "Autowired Annotations: {}", autowiredFields.size());
			for (Field f: autowiredFields) {
				String entityName = f.getName();
				EngineUtilities.log(LOG, LogLevel.VERBOSE, "Autowired Name: {}", entityName);
				f.setAccessible(true);
				T bean = StandAloneContainer.get().getEntityRegistry().getEntity(entityName, target);
				if (bean != null) {
					EngineUtilities.log(LOG, LogLevel.VERBOSE, "Autowired - Entity Scanned for sub annotations: {}", bean.getClass().getName());
					bean = AnnotationUtils.proxyEntityInstanceReferences(bean);
					f.set(target, bean);
				} else {
					EngineUtilities.log(LOG, LogLevel.ERROR, "Error Giving instance to Entity : {}", entityName);
				}
			}
			List<Field> wiredFields = reflections.getFieldsAnnotatedWith(Wired.class).stream().filter( a -> a.getDeclaringClass().isAssignableFrom(target.getClass()) ).collect(Collectors.toList());
			EngineUtilities.log(LOG, LogLevel.VERBOSE, "Wired Annotations: {}", wiredFields.size());
			for (Field f: wiredFields) {
				String entityName = f.getName();
				String name = f.getAnnotation(Wired.class).entityName();
				if (name!=null && !name.isEmpty()) {
					entityName=name;
				}
				EngineUtilities.log(LOG, LogLevel.VERBOSE, "Wired Name: {}", entityName);
				f.setAccessible(true);
				T bean = StandAloneContainer.get().getEntityRegistry().getEntity(entityName, target);
				if (bean != null) {
					EngineUtilities.log(LOG, LogLevel.VERBOSE, "Wired - Entity Scanned for sub annotations: {}", bean.getClass().getName());
					bean = AnnotationUtils.proxyEntityInstanceReferences(bean);
					f.set(target, bean);
				} else {
					EngineUtilities.log(LOG, LogLevel.ERROR, "Error Giving instance to Entity : {}", entityName);
				}
			}
			Reflections executeReflections = new Reflections(EngineUtilities.getFieldScannerBuilderByClassPackage(target));
			List<Field> executeFields = executeReflections.getFieldsAnnotatedWith(Execute.class).stream().filter( a -> a.getDeclaringClass().isAssignableFrom(target.getClass()) ).collect(Collectors.toList());
			EngineUtilities.log(LOG, LogLevel.VERBOSE, "Execute Annotations: {}", executeFields.size());
			for (Field f: executeFields) {
				String entityName = f.getName();
				f.setAccessible(true);
				if (f.getAnnotation(Service.class)!=null && f.getAnnotation(Service.class).name()!=null &&
							!f.getAnnotation(Service.class).name().isEmpty()) {
						entityName = f.getAnnotation(Service.class).name();
				}
				Execute executionAnnotation = f.getAnnotation(Execute.class);
				EngineUtilities.log(LOG, LogLevel.VERBOSE, "Execute Name: {}", entityName);
				T bean = StandAloneContainer.get().getEntityRegistry().getEntity(entityName, target);
				if (bean != null) {
					if (IContainerService.class.isAssignableFrom(bean.getClass())) {
						EngineUtilities.log(LOG, LogLevel.VERBOSE, "Execution - Entity Scanned for sub annotations: {}", bean.getClass().getName());
						bean = AnnotationUtils.proxyEntityInstanceReferences(bean);
						f.set(target, bean);
						IContainerService service = (IContainerService)bean;
						try {
							service.run();
						} catch (ServiceInvocationException e) {
							EngineUtilities.log(LOG, LogLevel.ERROR, "Execution of type {} - has raisen error : {}", bean.getClass().getName(), e.getMessage());
							EngineUtilities.log(LOG, LogLevel.ERROR, "Exception stack :", e);
							if (executionAnnotation.blockOnThows()) {
								throw new ServiceExecutionException("Service " + bean.getClass().getName() + " has thrown an exception, e");
							}
						}
					} else {
						EngineUtilities.log(LOG, LogLevel.WARN, "Execution of type {} - not possible due to unimplemented IContainer Service", bean.getClass().getName());
					}
				} else {
					EngineUtilities.log(LOG, LogLevel.ERROR, "Error Giving instance to Service : {}", entityName);
				}
			}
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException | InvalidBeanInstanceException | ServiceExecutionException  e) {
			EngineUtilities.log(LOG, LogLevel.ERROR, "Error Scanning Annotations", e);
			throw new HandlerInvocationException("Error injecting beans", e);
		}
		return target;
	}


}
