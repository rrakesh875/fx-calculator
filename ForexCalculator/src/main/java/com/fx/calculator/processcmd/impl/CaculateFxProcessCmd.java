package com.fx.calculator.processcmd.impl;

import java.util.InputMismatchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fx.calculator.exception.CurrencyNotSupportedException;
import com.fx.calculator.service.FXCalculatorService;
import com.fx.calculator.service.impl.FXCalculatorServiceImpl;
import com.fx.calculator.utility.FXUtility;

/**
 * Class caters to calculate currency conversion
 * Refers to fxrates.properties(CurrencyExchangeRatesMapping.java)
 * and precision.properties(CurrencyPrecisionConfig.java)
 * @author Ravi Rakesh
 *
 */
public class CaculateFxProcessCmd extends AbstractProcessCmd{
	private static final Logger log = LoggerFactory.getLogger(CaculateFxProcessCmd.class);
	private StringBuilder builder = FXUtility.getStringBuilder();
	/*@Autowired
	@Qualifier("fxImpl")*/
	private FXCalculatorService service;
	
	public CaculateFxProcessCmd(String fxRatePropertyfilePath, String currPrecisionPropertyfilePath) {
		super(fxRatePropertyfilePath,currPrecisionPropertyfilePath);
		this.service = new FXCalculatorServiceImpl(exchngRateMapping, currencyPrecisionConfig);
	}
	
	/**
	 * Method handles user inputs at different steps to calcuate conversion
	 * @return boolean - cascades signal back to terminate app
	 */
	@Override
	public boolean processcmd() {
		try {
			System.out.println("Please enter from/origin currency");
			String fromCurr = in.next();
			log.debug("CaculateFxProcessCmd -> processcmd fromCurr "+fromCurr);
			System.out.println("Please enter to/target currency");
			String toCurr =in.next();
			log.debug("CaculateFxProcessCmd -> processcmd toCurr "+toCurr);
			System.out.println("Please enter amount");
			Long quantity =in.nextLong();
			log.debug("CaculateFxProcessCmd -> processcmd quantity "+quantity);
			if(FXUtility.isInValidEntry(fromCurr) || FXUtility.isInValidEntry(toCurr)) {
				log.error("Invalid entries by user,please enter valid choice");
			}else {
				fromCurr = fromCurr.toUpperCase();
				toCurr = toCurr.toUpperCase();
				if(!isCurrencyPresent(fromCurr)) {
					builder.append("Currency ").append(fromCurr).append(" is not supported,Please add it to the system");
					throw new CurrencyNotSupportedException(builder.toString());
				}else if(!isCurrencyPresent(toCurr)) {
					builder.append("Currency ").append(toCurr).append(" is not supported,Please add it to the system");
					throw new CurrencyNotSupportedException(builder.toString());
				}else {
					double calcValue= service.calculate(fromCurr, toCurr, quantity);
					log.debug("CaculateFxProcessCmd -> processcmd calcValue "+calcValue);
					if(calcValue!=0.00) {
						builder.append("Calculated amount for given pair ").append(fromCurr).append(" to ").append(toCurr).append(" for quantity ").append(quantity)
						.append(" is ").append(calcValue);
						log.info(builder.toString());
					}
				}
			}
		}catch(InputMismatchException e) {
			log.error("Invalid input,Please enter correct input");
		}catch(CurrencyNotSupportedException e) {
			log.error(e.getMessage());
			log.info("List of currencies supported by system");
			printCurrentlySupportedCurrency();
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			builder.setLength(0);
		}
		return true;
	}


}
