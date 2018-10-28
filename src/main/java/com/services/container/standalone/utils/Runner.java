/**
 * 
 */
package com.services.container.standalone.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.services.container.standalone.exceptions.RunnerExecutionException;

/**
 * Abstract class that implements Thread and allow run of a single task
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 *
 * @see Thread
 */
public abstract class Runner extends Thread {
	private static final Logger LOG = LoggerFactory.getLogger(Runner.class);
	private boolean taskExecution = false;

	/**
	 * Default Constructor
	 * @param daemon Define if a Runner will run as a daemon thread, outside the JVM system process
	 * @see Thread#isDaemon()
	 */
	public Runner(boolean daemon) {
		super();
		setDaemon(daemon);
	}

	/**
	 * Default Constructor
	 * @param daemon Define if a Runner will run as a daemon thread, outside the JVM system process
	 * @param priority Qualify priority of the runner thread between {@link Thread#MIN_PRIORITY} and {@link Thread#MAX_PRIORITY}
	 * @see Thread#MIN_PRIORITY
	 * @see Thread#MAX_PRIORITY
	 * @see Thread#getPriority()
	 * @see Thread#isDaemon()
	 */
	public Runner(boolean daemon, int priority) {
		super();
		setDaemon(daemon);
		setPriority(priority);
	}

	/**
	 * Abstract Method that describes Thread task
	 * @throws RunnerExecutionException Exception that should be thrown at any error occurring during runner execution
	 */
	public abstract void execute() throws RunnerExecutionException;
	
	/**
	 * Execute check in of task start
	 */
	protected void checkIn() {
		taskExecution = true;
	}
	
	/**
	 * Execute check out when task finish
	 */
	protected void checkOut() {
		taskExecution = true;
	}

	/**
	 * Retrieve the running state of the task. Relevant functions that infear with this state are checkIn and checkOut
	 * @return task execution state
	 */
	public boolean isTaskRunning() {
		return taskExecution;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		super.run();
		try {
			this.execute();
		} catch (RunnerExecutionException e) {
			EngineUtilities.log(LOG, LogLevel.ERROR, "Error Executing thread task", e);
		}
	}

	
}
