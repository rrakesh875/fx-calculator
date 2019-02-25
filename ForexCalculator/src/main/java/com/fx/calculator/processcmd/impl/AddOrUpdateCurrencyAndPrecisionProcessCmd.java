package com.fx.calculator.processcmd.impl;

import java.util.InputMismatchException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fx.calculator.utility.FXUtility;

/**
 * Class is responsible for add Currency and its precision to the system at runtime.
 * We initially load these values from precision.properties file, but to make system configurable,
 * we have added this implementation. Refers to precision.properties(CurrencyPrecisionConfig.java)
 * @author Ravi Rakesh
 *
 */
public class AddOrUpdateCurrencyAndPrecisionProcessCmd extends AbstractProcessCmd{
	private static final Logger log = LoggerFactory.getLogger(AddOrUpdateCurrencyAndPrecisionProcessCmd.class);
	private StringBuilder builder = FXUtility.getStringBuilder();
	
	public AddOrUpdateCurrencyAndPrecisionProcessCmd(String fxRatePropertyfilePath, String currPrecisionPropertyfilePath) {
		super(fxRatePropertyfilePath,currPrecisionPropertyfilePath);
	}
	
	/**
	 * Allows user to add/update currencies and their precision in system.
	 * return boolean - sends back signal to terminate program or not
	 */
	@Override
	public boolean processcmd() {
		log.debug("AddOrUpdateCurrencyAndPrecisionProcessCmd -> processcmd");
		log.info("List of currencies supported by system");
		printCurrentlySupportedCurrency();
		System.out.println("Please enter A for adding new currency and U for updating current");
		try {
			String choice = in.next();
			if(!FXUtility.isInValidEntry(choice)) {
				choice = choice.toUpperCase();
				log.debug("AddOrUpdateCurrencyAndPrecisionProcessCmd -> processcmd choice"+choice);
				switch(choice) {
					case "A": processAdd(); break;
					case "U": processUpdate();break;
					default:{
						log.error("Please enter valid input: A/U");
						return true;
					}
				}
			}else {
				log.error("Please enter valid choice");
			}
		}catch(InputMismatchException e) {
			log.error("Invalid input,Please enter correct input");
		}catch(Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	/**
	 * To handle add flow of new currency and its precision
	 */
	private void processAdd() {
		try {
			System.out.println("Please enter new currency");
			String newCurr = in.next();
			if(!FXUtility.isInValidEntry(newCurr)) {
				newCurr = newCurr.toUpperCase();
				log.debug("AddOrUpdateCurrencyAndPrecisionProcessCmd -> processAdd choice"+newCurr);
				if(isCurrencyPresent(newCurr)) {
					builder.append("Seems we already have the currency ").append(newCurr).append(" in system try updating it if you want");
					log.error(builder.toString());
				}else {
					System.out.println("Please enter precision. Please positive only. Negative or decimal values are not accepted");
					int precision = in.nextInt();
					log.debug("AddOrUpdateCurrencyAndPrecisionProcessCmd -> processAdd precision"+precision);
					currencyPrecisionConfig.getProp().setProperty(newCurr, String.valueOf(precision));
					log.info("List of currencies supported by system now");
					printCurrentlySupportedCurrency();
				}
			}else {
				log.error("Please enter valid currency");
			}
			
		}catch(InputMismatchException e) {
			log.error("Invalid input,Please choose correct input");
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			builder.setLength(0);
		}
	}
	
	/**
	 * To handle update of existing currency  
	 */
	private void processUpdate() {
		try {
			System.out.println("Please enter currency you want to update");
			String curr = in.next();
			if(!FXUtility.isInValidEntry(curr)) {
				curr = curr.toUpperCase();
				log.debug("AddOrUpdateCurrencyAndPrecisionProcessCmd -> processUpdate curr"+curr);
				if(!isCurrencyPresent(curr)) {
					builder.append("Seems currency ").append(curr).append(" is not present in system,please add it first");
					log.error(builder.toString());
				}else {
					System.out.println("Please enter updated precision");
					double precision = in.nextDouble();
					log.debug("AddOrUpdateCurrencyAndPrecisionProcessCmd -> processUpdate precision"+precision);
					currencyPrecisionConfig.getProp().put(curr, Double.valueOf(precision));
				}
			}else {
				log.error("Please enter valid currency");
			}
		}catch(InputMismatchException e) {
			log.error("Invalid input,Please choose correct input");
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			builder.setLength(0);
		}
	}
	
	/**
	 * Haven't added support of removal of any currency, because -
	 * 1. Its quite rare that any currency is being removed from system
	 * 2. And if decided to removed, I believe updating 'precision.properties' and 'fxrates.properties' would be better idea at the time app start.
	 * Don't have much knowledge on domain,so I might be wrong here.
	 */
	

}
