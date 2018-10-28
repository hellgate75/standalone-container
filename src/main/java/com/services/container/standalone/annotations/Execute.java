/**
 * 
 */
package com.services.container.standalone.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.services.container.standalone.StandAloneContainer;
import com.services.container.standalone.exceptions.ServiceInvocationException;
import com.services.container.standalone.model.IContainerService;

/**
 * Annotation that execute at runtime an {@link IContainerService} implementation instances. This annotation is applied to {@link Service} annotation types objects. Execution happen when a configuration object instance get proxied via engine with {@link StandAloneContainer} proxy methods. 
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 * @see IContainerService
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value={ElementType.FIELD})
public @interface Execute {
	/**
	 * Block code execution in case the service execution raise a {@link ServiceInvocationException}, default false (disabled)
	 * @return blocking flag
	 */
	boolean blockOnThows() default false;
}
