/**
 * 
 */
package com.services.container.standalone.factories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;
import org.slf4j.LoggerFactory;

import com.services.container.standalone.DIBeanRegistry;
import com.services.container.standalone.StandAloneContainer;
import com.services.container.standalone.model.IFactoryManager;
import com.services.container.standalone.utils.EngineUtilities;
import com.services.container.standalone.utils.LogLevel;

/**
 * Helper class that helps in use of sub factories
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 * @see ComponentFactory
 * @see EntityFactory
 * @see ConfigurationFactory
 * @see ServiceFactory
 */
public class FactoryHelper {

	private static List<Class<? extends IFactoryManager>> managers = new ArrayList<>(0);
	
	/**
	 * Private Constructor
	 */
	private FactoryHelper() {
		super();
		throw new IllegalStateException("Unable to create instance for helper class");
	}
	
	/**
	 * Checks if any of the factory annotation build is in progress
	 * @return annotation creation progress
	 */
	public static final boolean isCreationForAllFactoryAnnotationsRunning() {
		return ConfigurationFactory.annotationsBuiltRunning || ServiceFactory.annotationsBuiltRunning ||
				ComponentFactory.annotationsBuiltRunning || EntityFactory.annotationsBuiltRunning;
	}
	
	private static final void loadFactories() {
		EngineUtilities.log(LoggerFactory.getLogger(FactoryHelper.class), LogLevel.INFO, "Scanning for FactoryManager implementations...");
		Reflections refl = new Reflections(EngineUtilities.getFullTypeScannerReflectionsNoClassLoader());
		Set<Class<? extends IFactoryManager>> classes =  refl.getSubTypesOf(IFactoryManager.class);
		managers.clear();
		managers.addAll(classes);
		EngineUtilities.log(LoggerFactory.getLogger(FactoryHelper.class), LogLevel.INFO, "FactoryManager implementations found: {}", managers.size());
		classes.forEach( FactoryHelper::printClass );
	}
	
	protected final static void printClass(String cls) {
		EngineUtilities.log(LoggerFactory.getLogger(FactoryHelper.class), LogLevel.INFO, "Found Type class name : {}", cls);
	}
	protected final static void printClass(Class<?> cls) {
		EngineUtilities.log(LoggerFactory.getLogger(FactoryHelper.class), LogLevel.INFO, "Loaded FactoryMananager subtype class name : {}", cls);
	}
	
	protected final static void printClassInstanceError(Class<?> cls, Exception exception) {
		EngineUtilities.log(LoggerFactory.getLogger(FactoryHelper.class), LogLevel.ERROR, "Error giving instance to FactoryMananager subtype class name : {}", cls);
		EngineUtilities.log(LoggerFactory.getLogger(FactoryHelper.class), LogLevel.ERROR, "", exception);
	}

	/**
	 * Scans, Discover and Creates Annotations for All Factories in the right sequence
	 */
	public static final void createAllFactoryAnnotations() {
		StandAloneContainer.get().getClassRegistry();
		EngineUtilities.log(LoggerFactory.getLogger(FactoryHelper.class), LogLevel.INFO, "FactoryManager implementations registry is empty: {}", managers.isEmpty());
		if (managers.isEmpty())
			loadFactories();
		managers.forEach( mc -> {
			try {
				mc.newInstance().startUpFactories();
			} catch (Exception e) {
				printClassInstanceError(mc, e);
			}
		});
	}
	
	/**
	 * Destroy Annotations for All Factories in the Registry
	 */
	public static final void resetAllFactoriesAndCleanAnnotations() {
		if (managers.isEmpty())
			loadFactories();
		managers.forEach( mc -> {
			try {
				mc.newInstance().tearDownFactories();
			} catch (Exception e) {
				printClassInstanceError(mc, e);
			}
		});
		DIBeanRegistry.reset();
	}
	
}
