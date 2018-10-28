/**
 * 
 */
package com.services.container.standalone.factories;

import org.reflections.Reflections;

import com.services.container.standalone.model.IFactoryManager;
import com.services.container.standalone.utils.EngineUtilities;

/**
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 *
 */
public class DefaultFactoryManager implements IFactoryManager {

	/**
	 * Default Constructor
	 */
	public DefaultFactoryManager() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.services.container.standalone.model.IFactoryManager#startUpFactories()
	 */
	@Override
	public void startUpFactories() {
		ConfigurationFactory.buildAnnotations();
		ServiceFactory.get(
			new Reflections(EngineUtilities.getTypeScannerBuilder(ConfigurationFactory.getPackages(), ConfigurationFactory.getUrls())),
			new Reflections(EngineUtilities.getMethodScannerBuilder(ConfigurationFactory.getPackages(), ConfigurationFactory.getUrls()))
		);
		ComponentFactory.get(
				new Reflections(EngineUtilities.getTypeScannerBuilder(ConfigurationFactory.getPackages(), ConfigurationFactory.getUrls())),
				new Reflections(EngineUtilities.getMethodScannerBuilder(ConfigurationFactory.getPackages(), ConfigurationFactory.getUrls()))
		);
		EntityFactory.get(
				new Reflections(EngineUtilities.getTypeScannerBuilder(ConfigurationFactory.getPackages(), ConfigurationFactory.getUrls())),
				new Reflections(EngineUtilities.getMethodScannerBuilder(ConfigurationFactory.getPackages(), ConfigurationFactory.getUrls()))
		);

	}

	/* (non-Javadoc)
	 * @see com.services.container.standalone.model.IFactoryManager#tearDownFactories()
	 */
	@Override
	public void tearDownFactories() {
		ConfigurationFactory.reset();
		ServiceFactory.reset();
		ComponentFactory.reset();
		EntityFactory.reset();
	}

}
