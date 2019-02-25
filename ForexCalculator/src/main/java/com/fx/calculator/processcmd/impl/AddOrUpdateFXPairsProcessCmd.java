package com.fx.calculator.processcmd.impl;

import java.util.InputMismatchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fx.calculator.exception.CurrencyNotSupportedException;
import com.fx.calculator.utility.FXUtility;

/**
 * Class caters for Adding/Updating FX Rates of currency pairs
 * @author Ravi Rakesh
 *
 */
public class AddOrUpdateFXPairsProcessCmd extends AbstractProcessCmd{
	private static final Logger log = LoggerFactory.getLogger(AddOrUpdateFXPairsProcessCmd.class);
	private StringBuilder builder = FXUtility.getStringBuilder();
	
	public AddOrUpdateFXPairsProcessCmd(String fxRatePropertyfilePath, String currPrecisionPropertyfilePath) {
		super(fxRatePropertyfilePath,currPrecisionPropertyfilePath);
	}
	
	/**
	 * Method handling user input.
	 * User enters origin/base,target/terms currencies and rate.
	 * Method accepts all of it in different steps and sends it to  currecnyFXGraph which includes this pair in its data structure.
	 */
	@Override
	public boolean processcmd() {
		try {
			System.out.println("Please enter from/origin currency");
			String fromCurr = in.next();
			log.debug("AddOrUpdateFXPairsProcessCmd -> processcmd fromCurr "+fromCurr);
			System.out.println("Please enter to/target currency");
			String toCurr= in.next();
			log.debug("AddOrUpdateFXPairsProcessCmd -> processcmd toCurr "+toCurr);
			if(FXUtility.isInValidEntry(fromCurr) || FXUtility.isInValidEntry(toCurr)) {
				log.error("Invalid entries by user,please enter valid choice");
			}else {
				fromCurr = fromCurr.toUpperCase();
				toCurr = toCurr.toUpperCase();
				
				if(!isCurrencyPresent(fromCurr)) {
					builder.append("Currency").append(fromCurr).append(" is not supported,Please add it to the system");
					throw new CurrencyNotSupportedException(builder.toString());
				}else if(!isCurrencyPresent(toCurr)) {
					builder.append("Currency").append(toCurr).append(" is not supported,Please add it to the system");
					throw new CurrencyNotSupportedException(builder.toString());
				}else {
					System.out.println("Please enter "+fromCurr+" to "+toCurr+"fx rate");
					double fxrate = in.nextDouble();
					log.debug("AddOrUpdateFXPairsProcessCmd -> processcmd fxrate "+fxrate);
					exchngRateMapping.setCurrencyExchangeRate(fromCurr, toCurr, fxrate);
				}
			}
		}catch(InputMismatchException e) {
			log.error("Invalid input,Please enter correct input");
		}catch(CurrencyNotSupportedException e) {
			log.error(e.getMessage());
			log.info("List of currencies supported by system");
			printCurrentlySupportedCurrency();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			builder.setLength(0);
		}
		return true;
	}


}
