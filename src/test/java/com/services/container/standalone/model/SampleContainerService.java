/**
 * 
 */
package com.services.container.standalone.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.services.container.standalone.annotations.Service;
import com.services.container.standalone.exceptions.ServiceInvocationException;
import com.services.container.standalone.model.IContainerService;
import com.services.container.standalone.utils.EngineUtilities;
import com.services.container.standalone.utils.LogLevel;

/**
 * @author TORELFA
 *
 */
@Service
public class SampleContainerService implements IContainerService {
	private static final Logger LOG = LoggerFactory.getLogger(SampleContainerService.class);

	/**
	 * Default Constructor
	 */
	public SampleContainerService() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.services.container.standalone.model.IContainerService#run()
	 */
	@Override
	public void run() throws ServiceInvocationException {
		//Execute simple service logging
		EngineUtilities.log(LOG, LogLevel.WARN, "Auto Executing service {}", SampleContainerService.class.getName()); 
	}

}
