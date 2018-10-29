/**
 * 
 */
package com.services.container.standalone.runners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.services.container.standalone.StandAloneContainer;
import com.services.container.standalone.exceptions.RunnerExecutionException;
import com.services.container.standalone.factories.FactoryHelper;
import com.services.container.standalone.utils.EngineUtilities;
import com.services.container.standalone.utils.LogLevel;
import com.services.container.standalone.utils.Runner;

/**
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 *
 */
public class ProxyInstanceRunner extends Runner {
	
	private static final Logger LOG = LoggerFactory.getLogger(ProxyInstanceRunner.class);

	private Object instance = null;
	
	/**
	 * Default Constructor
	 * @param instance Object to be proxied
	 */
	public ProxyInstanceRunner(Object instance) {
		super(true);
		this.instance = instance;
	}

	/* (non-Javadoc)
	 * @see com.services.container.standalone.utils.Runner#execute()
	 */
	@Override
	public void execute() throws RunnerExecutionException {
		StandAloneContainer container = null;
		while (container == null && ! StandAloneContainer.isReady() && FactoryHelper.isCreationForAllFactoryAnnotationsRunning()) {
			try {
				if (container == null)
					container = StandAloneContainer.get();
				else {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e1) {
					}
				}
			} catch (Exception e) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e1) {
				}
			}
			EngineUtilities.log(LOG, LogLevel.VERBOSE, "Waiting for engine and/or scanning to be completed ...");
		}
		try {
			EngineUtilities.log(LOG, LogLevel.VERBOSE, "Running proxy of instance ...");
			StandAloneContainer.get().proxyInstanceEntities(this.instance);
			EngineUtilities.log(LOG, LogLevel.VERBOSE, "Proxy of instance completed");
		} catch (Exception e) {
		}
		EngineUtilities.log(LOG, LogLevel.VERBOSE, "Reporting proxy completed state completed");
		StandAloneContainer.created();

	}

}
