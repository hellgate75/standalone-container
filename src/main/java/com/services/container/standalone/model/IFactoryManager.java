package com.services.container.standalone.model;

/**
 * Factory Manager allows the system to start and reset factories. <br>
 * It will be run at start-up after registration into the system. Factory managers
 * will be scanned into the system before the start-up of the implementing application
 * stand-alone engine run.
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 *
 */
public interface IFactoryManager {

	/**
	 * Create And start Factories and registry entities
	 */
	void startUpFactories();
	/**
	 * Reset and put down factories and registry entities
	 */
	void tearDownFactories();
	
}
