/**
 * 
 */
package com.services.container.standalone.domain;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Entity that collect information for make call and collect response from a 
 * method into the enclosed class
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 *
 */
public class EntityDescriptor {

	private Class<?> containingClass;
	private Method methodReference;
	private EntityType type;
	private String entityName;
	private Annotation annotation;
	private Type parameterType;
	private Parameter[] fieldParameters;
	private Parameter fieldParameter;
	private List<EntityDescriptor> parameters;
	private Map<String, Object> annotationMap; 
	
	/**
	 * Private Constructor
	 * 
	 * @param entityName Name of entity
	 * @param type Type of Entity registration
	 * @param annotation Intercepted annotation
	 * @param containingClass Class that contains the method
	 */
	private EntityDescriptor(String entityName, EntityType type, Annotation annotation, Class<?> containingClass) {
		super();
		this.containingClass=containingClass;
		this.methodReference=null;
		this.type = type;
		this.entityName=entityName;
		this.annotation = annotation;
		this.parameters = new ArrayList<>(0);
	}

	/**
	 * Private Constructor
	 * 
	 * @param entityName Name of entity
	 * @param type Type of Entity registration
	 * @param annotation Intercepted annotation
	 * @param containingClass Class that contains the method
	 * @param parameters Parameters associated to method call/type constructor
	 */
	private EntityDescriptor(String entityName, EntityType type, Annotation annotation, Class<?> containingClass, List<EntityDescriptor> parameters) {
		super();
		this.containingClass=containingClass;
		this.methodReference=null;
		this.type = type;
		this.entityName=entityName;
		this.annotation = annotation;
		this.parameters = parameters;
	}
	
	/**
	 * Private Constructor
	 * 
	 * @param entityName Name of entity
	 * @param type Type of Entity registration
	 * @param annotation Intercepted annotation
	 * @param methodReference Method reflection reference
	 */
	private EntityDescriptor(String entityName, EntityType type, Annotation annotation, Method methodReference) {
		super();
		this.containingClass=methodReference.getDeclaringClass();
		this.methodReference=methodReference;
		this.type = type;
		this.entityName=entityName;
		this.annotation = annotation;
	}
	/**
	 * Private Constructor
	 * 
	 * @param fieldParameters Fields for invocation
	 * @param entityName Name of entity
	 * @param type Type of Entity registration
	 * @param annotation Intercepted annotation
	 * @param methodReference Method reflection reference
	 * @param parameters Parameters associated to method call/type constructor
	 */
	private EntityDescriptor(Parameter[] fieldParameters, String entityName, EntityType type, Annotation annotation, Method methodReference, List<EntityDescriptor> parameters) {
		super();
		this.containingClass=methodReference.getDeclaringClass();
		this.methodReference=methodReference;
		this.type = type;
		this.entityName=entityName;
		this.annotation = annotation;
		this.parameters = parameters;
		this.fieldParameters=fieldParameters;
	}
	
	/**
	 * Private Constructor
	 * 
	 * @param fieldParameters Fields for invocation
	 * @param entityName Name of entity
	 * @param type Type of Entity registration
	 * @param annotationMap Map of intercepted annotation types
	 * @param parameterType Method Parameter Type
	 */
	private EntityDescriptor(Parameter fieldParameter, String entityName, EntityType type, Map<String, Object> annotationMap, Type parameterType) {
		super();
		this.containingClass=parameterType.getClass();
		this.parameterType=parameterType;
		this.type = type;
		this.entityName=entityName;
		this.annotationMap = annotationMap;
		this.fieldParameter=fieldParameter;
	}
	
	
	/**
	 * Retrieve Method Containing Class
	 * @return the Method Containing Class
	 */
	public Class<?> getContainingClass() {
		return containingClass;
	}


	/**
	 * Retrieve Method Reference
	 * @return the Method Reference
	 */
	public Method getMethodReference() {
		return methodReference;
	}


	/**
	 * Retrieve Method/Constructor Call Parameter Type
	 * @return the parameterType for Method/Constructor Call
	 */
	public Type getParameterType() {
		return parameterType;
	}

	/**
	 * Return Fields composing parameters in the right position
	 * @return the Field Parameters
	 */
	public Parameter[] getFieldParameters() {
		return fieldParameters;
	}

	/**
	 * Return Field where annotation is placed
	 * @return the Field Parameter
	 */
	public Parameter getFieldParameter() {
		return fieldParameter;
	}

	/**
	 * Retrieve the parameter descriptors
	 * @return the parameters descriptors
	 */
	public List<EntityDescriptor> getParameterDescriptors() {
		return parameters;
	}

	/**
	 * Constructor method for the Type Descriptor
	 * @param entityName Name of entity
	 * @param type Type of Entity registration
	 * @param containingClass Class that contains the method
	 * @param annotation Intercepted annotation
	 * @return MethodDescriptor new instance
	 */
	public static final EntityDescriptor newInstance(String entityName, EntityType type, Annotation annotation, Class<?> containingClass) {
		return new EntityDescriptor(entityName, type, annotation, containingClass);
	}
	
	/**
	 * Constructor method for the Type Descriptor
	 * @param entityName Name of entity
	 * @param type Type of Entity registration
	 * @param containingClass Class that contains the method
	 * @param annotation Intercepted annotation
	 * @param parameters Parameters associated to type constructor
	 * @return MethodDescriptor new instance
	 */
	public static final EntityDescriptor newInstance(String entityName, EntityType type, Annotation annotation, Class<?> containingClass, List<EntityDescriptor> parameters) {
		return new EntityDescriptor(entityName, type, annotation, containingClass, parameters);
	}

	/**
	 * Constructor method for the Method Descriptor
	 * @param entityName Name of entity
	 * @param type Type of Entity registration
	 * @param annotation Intercepted annotation
	 * @param methodReference Method reflection reference
	 * @return MethodDescriptor new instance
	 */
	public static final EntityDescriptor newInstance(String entityName, EntityType type, Annotation annotation, Method methodReference) {
		return new EntityDescriptor(entityName, type, annotation, methodReference);
	}

	/**
	 * Constructor method for the Method Descriptor
	 * @param entityName Name of entity
	 * @param fieldParameters parameters present in the method call
	 * @param type Type of Entity registration
	 * @param annotation Intercepted annotation
	 * @param methodReference Method reflection reference
	 * @param parameters Parameters associated to method call
	 * @return MethodDescriptor new instance
	 */
	public static final EntityDescriptor newInstance(Parameter[] fieldParameters, String entityName, EntityType type, Annotation annotation, Method methodReference, List<EntityDescriptor> parameters) {
		return new EntityDescriptor(fieldParameters, entityName, type, annotation, methodReference, parameters);
	}

	/**
	 * Constructor method parameter for the Method Parameter Descriptor
	 * 
	 * @param fieldParameter Filed where annotation is stored
	 * @param entityName Name of entity
	 * @param type Type of Entity registration
	 * @param annotationMap Map of intercepted annotation types
	 * @param parameterType Method Parameter Type
	 * @return MethodDescriptor new instance
	 */
	public static final EntityDescriptor newInstance(Parameter fieldParameter, String entityName, EntityType type, Map<String, Object> annotationMap, Type parameterType) {
		return new EntityDescriptor(fieldParameter, entityName, type, annotationMap, parameterType);
	}
	
	

	/**
	 * Retrieve Entity Type
	 * @return the entity type
	 */
	public EntityType getType() {
		return type;
	}

	/**
	 * Retrieve entity name
	 * @return the entityName
	 */
	public String getEntityName() {
		return entityName;
	}

	/**
	 * Retrieve Reference Annotation
	 * @return the annotation
	 */
	public Annotation getAnnotation() {
		return annotation;
	}

	/**
	 * Annotation Map reflecting Annotation values
	 * @return the annotationMap
	 */
	public Map<String, Object> getAnnotationMap() {
		return annotationMap;
	}

}
