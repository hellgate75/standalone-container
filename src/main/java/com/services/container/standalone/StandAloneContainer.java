package com.services.container.standalone;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.services.container.standalone.annotations.Autowired;
import com.services.container.standalone.annotations.Wired;
import com.services.container.standalone.exceptions.HandlerInvocationException;
import com.services.container.standalone.exceptions.InvalidBeanInstanceException;
import com.services.container.standalone.exceptions.StandAloneContainerAlreadyRunningException;
import com.services.container.standalone.factories.FactoryHelper;
import com.services.container.standalone.runners.ClasspathScannerRunner;
import com.services.container.standalone.utils.AnnotationUtils;
import com.services.container.standalone.utils.EngineUtilities;
import com.services.container.standalone.utils.LogLevel;

/**
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 *
 */
public class StandAloneContainer {
	private static final Logger LOG = LoggerFactory.getLogger(StandAloneContainer.class);

	private static StandAloneContainer instance=null;
	protected static boolean loadedPackages=false;
	private ClasspathScannerRunner runner = new ClasspathScannerRunner();
	
	/**
	 * Protected Default constructor
	 */
	protected StandAloneContainer() {
		super();
		reset();
		/* Load asynchronously beans from the class-path */
/*		new Thread(new Runnable() {
			
			@Override
			public void run() {
				DIBeanRegistry.get();
				ready();
			}
		}).start();
*/
		runner.start();
	}
	
	/**
	 * Execute Default Container operations
	 * @param arguments arguments used to bootstrap the container
	 * @throws StandAloneContainerAlreadyRunningException Exception that occurs during container start up
	 */
	public static void run(String[] arguments) throws StandAloneContainerAlreadyRunningException {
		if (instance!=null) {
			throw new StandAloneContainerAlreadyRunningException("You cannot run the container multiple times");
		}
		EngineUtilities.log(LOG, LogLevel.WARN, "Arguments: {}", Arrays.toString(arguments));
		instance = new StandAloneContainer();
	}
	
	/**
	 * Execute Default Container operations
	 * @throws StandAloneContainerAlreadyRunningException Exception that occurs during container start up
	 */
	public static void run() throws StandAloneContainerAlreadyRunningException {
		run(new String[0]);
	}
	
	/**
	 * Get singleton instance of {@link StandAloneContainer} or null, if container is not started
	 * @return Instance of {@link StandAloneContainer} or null
	 */
	public static StandAloneContainer get() {
		if (instance == null)
			throw new NullPointerException("Stand Alone Container not running");
		return instance;
	}
	
	/**
	 * Recover current {@link DIBeanRegistry} instance
	 * @return Current {@link DIBeanRegistry} instance
	 */
	public DIBeanRegistry getEntityRegistry() {
		return DIBeanRegistry.get();
	}
	/**
	 * Recover current {@link DIClassRegistry} instance
	 * @return Current {@link DIClassRegistry} instance
	 */
	public DIClassRegistry getClassRegistry() {
		return DIClassRegistry.get();
	}
	
	/**
	 * Reset current Container and clean any state
	 */
	public static void reset() {
		instance=null;
		loadedPackages=false;
		FactoryHelper.resetAllFactoriesAndCleanAnnotations();
	}
	
	/**
	 * Inject {@link Wired} and {@link Autowired} annotation entities into the class instance and all sub references in the Java hierarchy
	 * @param <T> Template type associated on runtime
	 * @param clz Class to get instance
	 * @param args Arguments for the constructors
	 * @return Proxy Instance of given class
	 * @throws InvalidBeanInstanceException Exception that occurs when any error is raisen during the proxy operations
	 */
	@SuppressWarnings("unchecked")
	public <T> T proxyClassEntities(Class<? extends T> clz, Object[] args) throws InvalidBeanInstanceException {
		try {
			for(Constructor<?> constr: clz.getConstructors()) {
				if (constr.getParameterTypes().length==args.length) {
					return (T) AnnotationUtils.proxyEntityInstanceReferences(constr.newInstance(args));
				}
			}
			return null;
		} catch (SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			EngineUtilities.log(LOG, LogLevel.ERROR, "Error in class instance", e);
			throw new InvalidBeanInstanceException("Error in class instance", e);
		} catch (HandlerInvocationException e) {
			EngineUtilities.log(LOG, LogLevel.ERROR, "Error in class bean proxy", e);
			throw new InvalidBeanInstanceException("Error in class bean proxy", e);
		}
	}

	/**
	 * Inject {@link Wired} and {@link Autowired} annotation entities into the instance and all sub references in the Java hierarchy
	 * @param <T> Template type associated on runtime
	 * @param target target instance to get Wired, Autowired and other entity proxy refedrence to beans
	 * @return Template Type instance
	 * @throws InvalidBeanInstanceException Exception that occurs when any error is risen during proxy operation
	 */
	public <T> T proxyInstanceEntities(T target) throws InvalidBeanInstanceException {
		try {
		return (T) AnnotationUtils.proxyEntityInstanceReferences(target);
		} catch (HandlerInvocationException e) {
			EngineUtilities.log(LOG, LogLevel.ERROR, "Error in instance bean proxy", e);
			throw new InvalidBeanInstanceException("Error in class bean proxy", e);
		}
	}
	
	/**
	 * Enable ready state of the engine
	 */
	public static void ready() {
		loadedPackages=true;
	}
	
	/**
	 * Method that stay in wait for the engine to load all entities and starts services
	 */
	public static void waitForReadyState() {
		while ( ! loadedPackages || FactoryHelper.isCreationForAllFactoryAnnotationsRunning() ) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				EngineUtilities.log(LOG, LogLevel.WARN, "Interrupted Excetion in wait for Engine scan", e);
			}
		}
	}

}
