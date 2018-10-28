/**
 * 
 */
package com.services.container.standalone.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation describes the configuration parameters. It's not needed for scan components,
 * however it describe for instance packages to scan. If no package is defined engine will scann all classes, despite performance
 * of the coded application
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value=ElementType.TYPE)
public @interface Configuration {
	/**
	 * Describe list of packages to be scanned or wildcat to scan all packages, if empty only the declaring class will be scanned
	 * @return list of packages to be scanned
	 */
	String[] packages() default {};
	/**
	 * Describe list of urls in the classpath to read classes or all if empty 
	 * @return list of urls to find the artifacts to be scanned
	 */
	String[] urls() default {};
	/**
	 * Name of the StandAlone Container Application
	 * @return Application name
	 */
	String applicationName() default "";
}
