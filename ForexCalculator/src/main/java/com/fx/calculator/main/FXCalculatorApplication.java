package com.fx.calculator.main;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fx.calculator.utility.UserInputProcessor;
/**
 * Main class
 * @author Ravi Rakesh
 */
public class FXCalculatorApplication {
	
	private static final Logger log = LoggerFactory.getLogger(FXCalculatorApplication.class);
	private static boolean run=true;
	public static void main(String[] args) {
		/*ApplicationContext context = 
		    	  new ClassPathXmlApplicationContext(new String[] {"spring.xml"});*/
		log.info("Welcome to Forex ExchangeApp");
		
		String fxRatePropertyfilePath = System.getProperty("FxRate_PropertyfilePath");
		log.info("FX Rate Property File Path"+fxRatePropertyfilePath);
		String currPrecisionPropertyfilePath = System.getProperty("CurrPrecision_PropertyfilePath");
		log.info("Currency Precision File Path"+currPrecisionPropertyfilePath);
		
		UserInputProcessor userInputProcessor = new UserInputProcessor(fxRatePropertyfilePath, currPrecisionPropertyfilePath);
		
		/**
		 * While loop,which keep main thread live till user sends entry to stop app.
		 */
		while(run) {
			run = userInputProcessor.getAndProcessUserInput();
		}
	}
	
	

}
