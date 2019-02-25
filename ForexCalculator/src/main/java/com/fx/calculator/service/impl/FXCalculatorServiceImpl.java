package com.fx.calculator.service.impl;

import java.math.BigDecimal;
import java.util.Formatter;
import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fx.calculator.exception.NoConversionMatchFoundException;
import com.fx.calculator.service.FXCalculatorService;
import com.fx.calculator.utility.CurrencyExchangeRatesMapping;
import com.fx.calculator.utility.CurrencyPrecisionConfig;
import com.fx.calculator.utility.FXUtility;

/**
 * Implementation caters to calculate currency fx conversions
 * Uses org.jgrapht.* library to search graph of currency pairs and their fx rate
 * @author Ravi Rakesh
 *
 */
//@Service
public class FXCalculatorServiceImpl implements FXCalculatorService{
	private static final Logger log = LoggerFactory.getLogger(FXCalculatorServiceImpl.class);
	private SimpleDirectedGraph currFxRatesGraph;
	private CurrencyExchangeRatesMapping exchngRateMapping;
	private CurrencyPrecisionConfig currencyPrecisionConfig;
	private Formatter fmt;
	public FXCalculatorServiceImpl(CurrencyExchangeRatesMapping exchngRateMapping, CurrencyPrecisionConfig currencyPrecisionConfig) {
		this.exchngRateMapping = exchngRateMapping;
		this.currencyPrecisionConfig = currencyPrecisionConfig;
		fmt = new Formatter();
	}

	/**
	 * args0 : fromCurrency - base currency
	 * args1 : toCurrency -  terms/target currency
	 * args2 : quantity - Quantity user wants to convert
	 */
	@Override
	public double calculate(String fromCurrency, String toCurrency,
			Long quantity) {
		currFxRatesGraph = exchngRateMapping.getCurrencyFxRatesGraph();
		double calcValue=0.00;
		GraphPath<String, Double> graphPath=null;
		try {
			graphPath = DijkstraShortestPath.findPathBetween(currFxRatesGraph, fromCurrency, toCurrency);
			if ( graphPath == null) {
				throw new NoConversionMatchFoundException("No conversion pairing found for pair "+fromCurrency+" and "+toCurrency);
			}else {
				List<Double> listOfpath = graphPath.getEdgeList();
				
				BigDecimal rate = new BigDecimal(1.00);
				for (Double edge : listOfpath) {
					rate = rate.multiply(BigDecimal.valueOf(edge.doubleValue()));
				}
				
				calcValue = rate.multiply(BigDecimal.valueOf(quantity)).doubleValue();
				
				String precision = currencyPrecisionConfig.getProp().getProperty(toCurrency);
				
				if(!isInvalidPrecision(precision)) {
					String frmtCalcValue = fmt.format("%."+precision+"f", calcValue).toString();
					
					calcValue = Double.parseDouble(frmtCalcValue);
				}else {
					log.error("Precision value for currency "+toCurrency+" is invalid,please check ");
				}
			}
			
		}catch(NoConversionMatchFoundException e) {
			e.printStackTrace();
		}catch(IllegalArgumentException e) {
			log.error("Invalid Currency passed for calculation, Please look for option 1 & 2 to add currencies and pair in system");
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return calcValue;
	}
	/**
	 * Method validates precision value from system for bogus/invalid entry. 
	 * @param precision
	 * @return boolean - if precision is valid
	 */
	private boolean isInvalidPrecision(String precision) {
		boolean invalid = false;
		if(FXUtility.isInValidEntry(precision)) {
			invalid = true;
		}else {
			try {
				int precsn = Integer.parseInt(precision);
				if(precsn<0) invalid = true; // To check if its negative value, as negative conversion rate doesn't exist
			}catch(NumberFormatException e) { // Exception handle to deal with non int invalid values
				invalid = true;
			}
		}
		return invalid;
	}
	
}
