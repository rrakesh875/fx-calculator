package com.fx.calculator.processcmd.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fx.calculator.processcmd.ProcessCmd;

/**
 * Class just sends signal back to terminate app
 * @author Ravi Rakesh
 *
 */
public class AbortProgramProcessCmd implements ProcessCmd{
	private static final Logger log = LoggerFactory.getLogger(AbortProgramProcessCmd.class);
	@Override
	public boolean processcmd() {
		log.info("Aborting App");
		return false;
	}

}
