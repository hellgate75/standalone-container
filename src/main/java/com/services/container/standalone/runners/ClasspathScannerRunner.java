/**
 * 
 */
package com.services.container.standalone.runners;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.services.container.standalone.DIBeanRegistry;
import com.services.container.standalone.StandAloneContainer;
import com.services.container.standalone.domain.EntityDescriptor;
import com.services.container.standalone.exceptions.RunnerExecutionException;
import com.services.container.standalone.utils.EngineUtilities;
import com.services.container.standalone.utils.LogLevel;
import com.services.container.standalone.utils.Runner;

/**
 * Describe Scanner for execution of Reflection Annotation scanning
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 *
 */
public class ClasspathScannerRunner extends Runner {

	private static final Logger LOG = LoggerFactory.getLogger(ClasspathScannerRunner.class);
	
	/**
	 * Default Constructor
	 */
	public ClasspathScannerRunner() {
		super(true);
	}

	/* (non-Javadoc)
	 * @see com.services.container.standalone.utils.Runner#execute()
	 */
	@Override
	public void execute() throws RunnerExecutionException {
		this.checkIn();
		Collection<EntityDescriptor> descriptors = DIBeanRegistry.get().getDescriptors();
		List<String> beans = descriptors.stream().map( d -> d.getEntityName() ).collect(Collectors.toList());
		EngineUtilities.log(LOG, LogLevel.INFO, "Lookup completed - Found Beans : {}", beans.toString());
		StandAloneContainer.ready();
		this.checkOut();
	}

}
