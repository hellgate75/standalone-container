package com.services.container.standalone.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.services.container.standalone.DIBeanRegistry;
import com.services.container.standalone.StandAloneContainer;
import com.services.container.standalone.annotations.Autowired;
import com.services.container.standalone.annotations.Component;
import com.services.container.standalone.annotations.Configuration;
import com.services.container.standalone.annotations.Entity;
import com.services.container.standalone.annotations.Inject;
import com.services.container.standalone.annotations.Service;
import com.services.container.standalone.annotations.Wired;
import com.services.container.standalone.domain.EntityDescriptor;
import com.services.container.standalone.domain.EntityType;
import com.services.container.standalone.exceptions.HandlerInvocationException;
import com.services.container.standalone.exceptions.InvalidBeanInstanceException;
import com.services.container.standalone.exceptions.InvalidEntityRegistrationException;
import com.services.container.standalone.handler.AnnotationScannerHandler;
import com.services.container.standalone.handler.IHandler;

public class AnnotationUtils {
	private static final Logger LOG = LoggerFactory.getLogger(AnnotationUtils.class);

	private static long CONFIG_INDEX = 0l;
	private static long DEFAULT_INDEX = 0l;

	protected AnnotationUtils() {
		super();
		throw new IllegalStateException("Utility class canot be instanziated");
	}
	
	/**
	 * Reset indexes due to a new empty scan
	 */
	public static void resetIndexes() {
		CONFIG_INDEX = 0l;
		DEFAULT_INDEX = 0l;
	}
	
	private static <T> T newEntityInstance(EntityDescriptor descriptor, Class<? extends T> clazz, List<EntityDescriptor> parameters) throws InvalidBeanInstanceException {
		try {
			if (parameters!= null && ! parameters.isEmpty()) {
				List<Object> params = new ArrayList<>(0);
				parameters.forEach( ep -> {
					if (ep.getAnnotation().annotationType() == Inject.class) {
						String name = getEntitySystemName((Inject)ep.getAnnotation());
						try {
							params.add(DIBeanRegistry.get().getEntity(name, null));
						} catch (Exception e) {
							EngineUtilities.log(LOG, LogLevel.ERROR, "Error generating bean parameter : {}", name);
						}
					} else {
						EngineUtilities.log(LOG, LogLevel.WARN, "Wrong annotation to inject component: {}", ep.getAnnotation().getClass());
					}
				});
				List<Class<?>> classes = params.stream().map( Object::getClass ).collect(Collectors.toList());
				return clazz.getConstructor(classes.toArray(new Class[0])).newInstance(params);
				
			} else {
				return clazz.newInstance();
			}
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			EngineUtilities.log(LOG, LogLevel.ERROR, "Error making new instance of entity", e);
			throw new InvalidBeanInstanceException("Instance of bean failed!!", e);
		}
	}

	@SuppressWarnings("unchecked")
	private static <T> T newEntityInstance(EntityDescriptor descr, Method method , List<EntityDescriptor> parameters) throws InvalidBeanInstanceException {
		try {
			EngineUtilities.log(LOG, LogLevel.VERBOSE, "Entity name: {}", descr.getEntityName());
			EngineUtilities.log(LOG, LogLevel.VERBOSE, "Number of parameters: {}", parameters.size());
			if (parameters!= null && ! parameters.isEmpty()) {
				List<Object> params = new ArrayList<>(0);
				parameters.forEach( ep -> {
					String enityName = (String)ep.getAnnotationMap().get("entityName");
					if (enityName!=null && !enityName.isEmpty()) {
						EngineUtilities.log(LOG, LogLevel.VERBOSE, "Entity Name in Inject Annoration: {}", enityName);
						try {
							params.add(DIBeanRegistry.get().getEntity(enityName, null));
						} catch (Exception e) {
							EngineUtilities.log(LOG, LogLevel.ERROR, "Error generating bean parameter : {}", enityName);
						}
					} else {
						EngineUtilities.log(LOG, LogLevel.WARN, "Wrong annotation to inject component: {}", ep.getAnnotation().getClass());
					}
				});
				EngineUtilities.log(LOG, LogLevel.VERBOSE, "Number of built parameters: {}", params.size());
				return (T) method.invoke(method.getDeclaringClass().newInstance(), params.toArray());
				
			} else {
				return (T) method.invoke(method.getDeclaringClass().newInstance(), new Object[0]);
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
			EngineUtilities.log(LOG, LogLevel.ERROR, "Error making new instance of entity from method", e);
			throw new InvalidBeanInstanceException("Instance of bean from method failed!!", e);
		}
	}

	/**
	 * Retrieve a fresh instance of an entity from the registry
	 * @param <T> Template Annotation type bound dynamically on runtime
	 * @param beanName name of descriptor to give instance
	 * @param descr descriptor data
	 * @param reference included reference element (for method call instance entities)
	 * @return The entity instance
	 * @throws InvalidBeanInstanceException Thrown when any exception occurs during entity instance operations
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getEntityInstance(String beanName, EntityDescriptor descr, Object reference) throws InvalidBeanInstanceException {
		try {
			if (descr!=null) {
				if (descr.getType()==EntityType.SERVICE ||
					descr.getType()==EntityType.COMPONENT ||
					descr.getType()==EntityType.ENTITY ||
					descr.getType()==EntityType.CONFIGURATION) {
					return (T) newEntityInstance(descr, descr.getContainingClass(), descr.getParameterDescriptors()) ;
				} else if (descr.getType()==EntityType.METHOD_SERVICE ||
					descr.getType()==EntityType.METHOD_ENTITY ||
					descr.getType()==EntityType.METHOD_COMPONENT) {
					Method m = descr.getMethodReference();
					return (T) newEntityInstance(descr, m, descr.getParameterDescriptors());
				}
			}
			return null;
		} catch (ClassCastException | IllegalArgumentException e) {
			EngineUtilities.log(LOG, LogLevel.ERROR, "Error applying Bean descriptor to build the Bean", e);
			throw new InvalidBeanInstanceException("Unable to create a bean for name: " + beanName, e);
		}
	}
	
	private static String getAnnotationNameParamInClass(Class<?> clz, EntityType eType) {
		if (eType == EntityType.SERVICE)
			return clz.getAnnotationsByType(Service.class)[0].name();
		else if (eType == EntityType.COMPONENT)
			return clz.getAnnotationsByType(Component.class)[0].name();
		else if (eType == EntityType.ENTITY)
			return clz.getAnnotationsByType(Entity.class)[0].name();
		return null;
	}
	
	private static final String getEntitySystemName(Class<?> iface, EntityType eType) {
		 String name = getAnnotationNameParamInClass(iface, eType);
		 if ((name==null || name.isEmpty()) && eType != EntityType.CONFIGURATION) {
			 name = iface.getSimpleName();
			 name = name.substring(0, 1).toLowerCase() + name.substring(1);
		 } else if (eType == EntityType.CONFIGURATION) {
			 name = "config" + (++CONFIG_INDEX);
		 }
		 if (name==null || name.isEmpty()) {
			 name = "noname" + (++DEFAULT_INDEX);
		 }
		 return name;
	}
	
	private static final String getEntitySystemName(Method m, EntityType eType) {
	   	 String name = getAnnotationNameParamInMethod(m, eType);
	   	 EngineUtilities.log(LOG, LogLevel.VERBOSE, "Entity Annotation name: {} from method: {} as type: {}", name, m.getName(), eType);
	   	 if (name==null || name.isEmpty()) {
	   		 name = m.getName();
	   		 name = name.substring(0, 1).toLowerCase() + name.substring(1);
	   	 }
	   	 return name;
	}
	
	private static final String getEntitySystemName(Inject a) {
		String name = a.entityName();
		if (name==null || a.entityName().isEmpty()) {
			throw new RuntimeException("Inject must declare the beanName");
		}
		return name;
	}
	
	private static void registerClassEntity(Class<?> iface, EntityType eType) {
		 String name = getEntitySystemName(iface, eType);
		 EngineUtilities.log(LOG, LogLevel.VERBOSE, "Class Entity name: {}", name);
       try {
			StandAloneContainer.get().getClassRegistry().register(eType, name, iface, null, getAnnotatedTypeDescriptors(iface.getConstructors()[0], null, iface.getConstructors()[0].getParameters(), iface.getConstructors()[0].getAnnotatedParameterTypes()));
		} catch (InvalidEntityRegistrationException e) {
			EngineUtilities.log(LOG, LogLevel.ERROR, "Error registering Entity Descriptor", e);
		}
	}

	private static boolean isAnnotationPresentInClass(Class<?> clz, EntityType eType) {
		if (eType == EntityType.SERVICE)
			return clz.isAnnotationPresent(Service.class);
		else if (eType == EntityType.COMPONENT)
			return clz.isAnnotationPresent(Component.class);
		else if (eType == EntityType.ENTITY)
			return clz.isAnnotationPresent(Entity.class);
		else if (eType == EntityType.CONFIGURATION)
			return clz.isAnnotationPresent(Configuration.class);
		return false;
	}
	
	/**
	 * Scan and register annotations present at Type level
	 * @param <T> Template Annotation type bound dynamically on runtime
	 * @param types Set of classes where annotation could be
	 * @param eType required eType to be found into the result
	 * @return list of discovered annotations
	 */
	public static <T extends Annotation> List<T> scanAndRegisterClassAnnotations(Set<Class<?>> types, EntityType eType) {
		List<T> annotations = new ArrayList<>(0);
		for (Class<?> implementationClass : types) {
	    	 if (isAnnotationPresentInClass(implementationClass, eType)) {
		    	  registerClassEntity(implementationClass, eType);
		    	  annotations.add(getAnnotationByType(eType, implementationClass));
	    	 }
		      for (Class<?> iface : implementationClass.getInterfaces()) {
		    	 if (isAnnotationPresentInClass(iface, eType)) {
			    	  registerClassEntity(iface, eType);
			    	  annotations.add(getAnnotationByType(eType, iface));
		    	 }
		      }
		    }
		return annotations;
	}
	
	private static boolean isAnnotationPresentInMethod(Method m, EntityType eType) {
		if (eType == EntityType.METHOD_SERVICE)
			return m.isAnnotationPresent(Service.class);
		else if (eType == EntityType.METHOD_COMPONENT)
			return m.isAnnotationPresent(Component.class);
		else if (eType == EntityType.METHOD_ENTITY)
			return m.isAnnotationPresent(Entity.class);
		return false;
	}

	private static String getAnnotationNameParamInMethod(Method m, EntityType eType) {
		if (eType == EntityType.METHOD_SERVICE)
			return m.getAnnotationsByType(Service.class)[0].name();
		else if (eType == EntityType.METHOD_COMPONENT)
			return m.getAnnotationsByType(Component.class)[0].name();
		else if (eType == EntityType.METHOD_ENTITY)
			return m.getAnnotationsByType(Entity.class)[0].name();
		return null;
	}
	
	/**
	 * Retrieve the annotation by Type and {@link EntityType}
	 * @param <T> Template Annotation type bound dynamically on runtime
	 * @param eType Type of annotation
	 * @param enclosingClass class containing annotations
	 * @return Retrieved annotation or null
	 */
	@SuppressWarnings("unchecked")
	public static final <T extends Annotation> T getAnnotationByType(EntityType eType, Class<?> enclosingClass) {
		switch (eType) {
			case COMPONENT:
		    	 EngineUtilities.log(LOG, LogLevel.VERBOSE, "Component in class is  present: {}", enclosingClass.isAnnotationPresent(Component.class));
				if (enclosingClass.isAnnotationPresent(Component.class))
					return (T) Arrays.asList(enclosingClass.getDeclaredAnnotations()).stream().filter( (a) -> a.annotationType() == Component.class).collect(Collectors.toList()).get(0);
				break;
			case SERVICE:
		    	 EngineUtilities.log(LOG, LogLevel.VERBOSE, "Service in class is present: {}", enclosingClass.isAnnotationPresent(Service.class));
				if (enclosingClass.isAnnotationPresent(Service.class))
					return (T) Arrays.asList(enclosingClass.getDeclaredAnnotations()).stream().filter( (a) -> a.annotationType() == Service.class).collect(Collectors.toList()).get(0);
				break;
			case ENTITY:
		    	 EngineUtilities.log(LOG, LogLevel.VERBOSE, "Entity in class is present: {}", enclosingClass.isAnnotationPresent(Service.class));
				if (enclosingClass.isAnnotationPresent(Entity.class))
					return (T) Arrays.asList(enclosingClass.getDeclaredAnnotations()).stream().filter( (a) -> a.annotationType() == Entity.class).collect(Collectors.toList()).get(0);
				break;
			case CONFIGURATION:
		    	 EngineUtilities.log(LOG, LogLevel.VERBOSE, "Configuration in class is  present: {}", enclosingClass.isAnnotationPresent(Configuration.class));
				if (enclosingClass.isAnnotationPresent(Configuration.class))
					return (T) Arrays.asList(enclosingClass.getDeclaredAnnotations()).stream().filter( (a) -> a.annotationType() == Configuration.class).collect(Collectors.toList()).get(0);
				break;
			default:
				return null;
		}
		return null;
	}

	/**
	 * Retrieve the annotation by Method and {@link EntityType}
	 * @param <T> Template Annotation type bound dynamically on runtime
	 * @param eType Type of annotation
	 * @param enclosingMethod method containing annotations
	 * @return Retrieved annotation or null
	 */
	@SuppressWarnings("unchecked")
	public static final <T extends Annotation> T getAnnotationByType(EntityType eType, Method enclosingMethod) {
		switch (eType) {
			case METHOD_COMPONENT:
		    	 EngineUtilities.log(LOG, LogLevel.VERBOSE, "Component in method is present: {}", enclosingMethod.isAnnotationPresent(Component.class));
				if (enclosingMethod.isAnnotationPresent(Component.class))
					return (T) Arrays.asList(enclosingMethod.getDeclaredAnnotations()).stream().filter( (a) -> a.annotationType() == Component.class).collect(Collectors.toList()).get(0);
				break;
			case METHOD_SERVICE:
		    	 EngineUtilities.log(LOG, LogLevel.VERBOSE, "Service in method is present: {}", enclosingMethod.isAnnotationPresent(Service.class));
				if (enclosingMethod.isAnnotationPresent(Service.class))
					return (T) Arrays.asList(enclosingMethod.getDeclaredAnnotations()).stream().filter( (a) -> a.annotationType() == Service.class).collect(Collectors.toList()).get(0);
				break;
			case METHOD_ENTITY:
		    	 EngineUtilities.log(LOG, LogLevel.VERBOSE, "Entity in method is present: {}", enclosingMethod.isAnnotationPresent(Service.class));
				if (enclosingMethod.isAnnotationPresent(Entity.class))
					return (T) Arrays.asList(enclosingMethod.getDeclaredAnnotations()).stream().filter( (a) -> a.annotationType() == Entity.class).collect(Collectors.toList()).get(0);
				break;
			default:
				return null;
		}
		return null;
	}
	
	/**
	 * Scan and register annotations present at Method level
	 * @param <T> Template Annotation type bound dynamically on runtime
	 * @param mTypes Set of methods where annotation could be
	 * @param eType required eType to be found into the result
	 * @return list of discovered annotations
	 */
	public static <T extends Annotation> List<T> scanAndRegisterMethodAnnotations(Set<Method> mTypes, EntityType eType) {
		List<T> annotations = new ArrayList<>(0);
	    for(Method m: mTypes) {
	    	 if (isAnnotationPresentInMethod(m, eType)) {
		    	 String name = getEntitySystemName(m, eType);
		    	 EngineUtilities.log(LOG, LogLevel.VERBOSE, "Method Entity name: {}", name);
		        try {
		        	StandAloneContainer.get().getClassRegistry().register(eType, name, null, m, getAnnotatedTypeDescriptors(null, m, m.getParameters(), m.getAnnotatedParameterTypes()));
			    	annotations.add(getAnnotationByType(eType, m));
				} catch (InvalidEntityRegistrationException e) {
					EngineUtilities.log(LOG, LogLevel.ERROR, "Error registering Method Entity", e);
				}
	    	 }
	    }
	    return annotations;
	}
	
	private static List<EntityDescriptor> getAnnotatedTypeDescriptors(Constructor<?> constructor, Method method, Parameter[] parameters, AnnotatedType[] maTypes) {
		List<EntityDescriptor> types = new ArrayList<>(0);
		Arrays.asList(maTypes).forEach( ma -> {
			
			List<Annotation> annotations = new ArrayList<>(0);
			if (constructor!=null) {
				annotations = Arrays.asList(constructor.getParameters()).stream().filter( p -> p.getAnnotation(Inject.class)!=null).map(p -> p.getAnnotation(Inject.class)).collect(Collectors.toList());
			} else {
				annotations = Arrays.asList(method.getParameters()).stream().filter( p -> p.getAnnotation(Inject.class)!=null).map(p -> p.getAnnotation(Inject.class)).collect(Collectors.toList());
			}
			
			Inject annotation = (Inject)(annotations.isEmpty() ? null : annotations.get(0));
			EngineUtilities.log(LOG, LogLevel.VERBOSE, "Annotated Types: {}", maTypes.length);
			EngineUtilities.log(LOG, LogLevel.VERBOSE, "Annotations found: {}", annotation);
			if (annotation != null ) {
				EngineUtilities.log(LOG, LogLevel.VERBOSE, "Found element: {}", annotation);
				Type type = ma.getType();
				List<Parameter> params = Arrays.asList(parameters).stream().filter( p -> Arrays.asList(p.getDeclaredAnnotations()).stream().filter( ann -> ann.equals(annotation) ).count()>0 ).collect(Collectors.toList());
				EngineUtilities.log(LOG, LogLevel.VERBOSE, "Found element type: {}", type);
				String name = getEntitySystemName(annotation);
				if (! params.isEmpty()){
					EngineUtilities.log(LOG, LogLevel.VERBOSE, "Found element annotated name: {}", name);
					Map<String, Object> annotationMap = new HashMap<>(0);
					annotationMap.put("entityName", name);
					types.add(EntityDescriptor.newInstance(params.get(0), name, EntityType.METHOD_INJECTION, annotationMap, type));
				}
				else 
					EngineUtilities.log(LOG, LogLevel.WARN, "Discarding descriptor for annorated name {}", name);
			}
		} );
		EngineUtilities.log(LOG, LogLevel.VERBOSE, "Found annotated elements: {}", types.size());
		return types;
	}
	
	/**
	 * Create an Object filled with {@link Wired} and {@link Autowired} annotaions reference filled in
	 * @param <T> Template class dynamically acquired from declaration
	 * @param target Source Bean/Entity object
	 * @throws HandlerInvocationException Exception arisen for issues during proxy creation
	 * @return Handled and modified proxy object
	 */
	public static <T> T proxyEntityInstanceReferences(T target) throws HandlerInvocationException {
		IHandler<T> seekerHandler = new AnnotationScannerHandler<>();
		return seekerHandler.handle(target);
	}
}
