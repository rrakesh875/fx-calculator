package com.fx.calculator.utility;

import java.util.InputMismatchException;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fx.calculator.processcmd.ProcessCmd;
import com.fx.calculator.processcmd.impl.AbortProgramProcessCmd;
import com.fx.calculator.processcmd.impl.AddOrUpdateCurrencyAndPrecisionProcessCmd;
import com.fx.calculator.processcmd.impl.AddOrUpdateFXPairsProcessCmd;
import com.fx.calculator.processcmd.impl.CaculateFxProcessCmd;

/**
 * Class objective is to take user input,process it and calls specific workflow class
 * @author Ravi Rakesh
 */
public class UserInputProcessor {
	private static final Logger log = LoggerFactory.getLogger(UserInputProcessor.class);
	private String fxRatePropertyfilePath,currPrecisionPropertyfilePath;
	
	public UserInputProcessor(String fxRatePropertyfilePath, String currPrecisionPropertyfilePath) {
		this.fxRatePropertyfilePath = fxRatePropertyfilePath;
		this.currPrecisionPropertyfilePath = currPrecisionPropertyfilePath;
	}
	
	/**
	 * Method is responsible for getting user's entry from console and pass it to processCmdChoice
	 * @return boolean
	 */
	public boolean  getAndProcessUserInput() {
		Scanner in=new Scanner(System.in);;
		int cmdChoice=0;
		try {
			log.debug("Processing -> processUserInput");
			System.out.println();;//Just to add some space when this will appear again
			System.out.println("Please enter 0 to abort app, 1 to add new currency and its precision to system,2 to add fx currency pair, 3 to calculate");
			cmdChoice = in.nextInt();
		}catch(InputMismatchException e) {
			log.error("Invalid input,Please enter correct input");
			return true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return processCmdChoice(cmdChoice);
	}
	
	/**
	 * Method calls specific selected implementation based on selection of user
	 * Have tried to use Strategy Pattern here. 
	 * @param cmdChoice - selection type of user
	 * @return boolean - this will sends the signal back to main thread to continue/stop app
	 */
	private boolean processCmdChoice(int cmdChoice) {
		boolean run=true;
		switch(cmdChoice) {
			case 0: run = callProcessCmd(new AbortProgramProcessCmd()); break;
			case 1: run = callProcessCmd(new AddOrUpdateCurrencyAndPrecisionProcessCmd(fxRatePropertyfilePath,currPrecisionPropertyfilePath));break;
			case 2: run = callProcessCmd(new AddOrUpdateFXPairsProcessCmd(fxRatePropertyfilePath,currPrecisionPropertyfilePath)); break;
			case 3: run = callProcessCmd(new CaculateFxProcessCmd(fxRatePropertyfilePath,currPrecisionPropertyfilePath)); break;
			default: {
				log.error("Please enter valid input: 0,1,2,3");
				return true;
			}
		}
		
		return run;
	}
	
	
	private static boolean callProcessCmd(ProcessCmd processCmd) {
		return processCmd.processcmd();
	}

}
