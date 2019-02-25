package com.fx.calculator.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import org.jgrapht.graph.SimpleDirectedGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Property Config class
 * Caters to construct graph (using org.jgrapht.*) library of currency pairs and their fx rates.
 * At the time of system start, reads from fxrates.properties and gets updated with user entry.
 * 
 * Singleton as more than one instance can lead to integrity issues.
 * @author Ravi Rakesh
 *
 */
public class CurrencyExchangeRatesMapping {
	private static final Logger log = LoggerFactory.getLogger(CurrencyExchangeRatesMapping.class);
	private SimpleDirectedGraph<String,Double> currencyFxRatesGraph;
	
	private static CurrencyExchangeRatesMapping instance = null;
	
	private CurrencyExchangeRatesMapping(String propertyfilePath){
		currencyFxRatesGraph = new SimpleDirectedGraph<String, Double>(Double.class);
		init(propertyfilePath);
	}
	
	private void init(String propertyfilePath){
		try {
		Properties prop = new Properties();
		if(propertyfilePath!=null && !"".equals(propertyfilePath)) {
			prop.load(new FileInputStream(new File(propertyfilePath)));
		}else {
			prop.load(getClass().getResourceAsStream("/fxrates.properties"));
		}
		
		Set<Object> propsKeys = prop.keySet();
		
		Iterator<Object> itr = propsKeys.iterator();
		while(itr.hasNext()) {
			String property = (String)itr.next();
			Object value = prop.get(property);
			if(value!=null) {
				double	fxRate = Double.parseDouble(String.valueOf(value));
				String[] crrncyPair = property.split("/");
				String fromCurrency = crrncyPair[0];
				String toCurrency = crrncyPair[1];
				boolean success = setCurrencyExchangeRate(fromCurrency, toCurrency, fxRate);
				if(!success) log.error("Error in adding currency pairs to graph"+fromCurrency+" "+toCurrency);
			}else {
				log.error("No value found for Currency pair "+property+" in properties file,hence skipping it");
			}
			
		}
		} catch (FileNotFoundException e) {
			log.error("Seems wrong path entered for property file");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param fromCurrency
	 * @param toCurrency
	 * @param fxRate
	 * @return boolean - sends success/fail message in return.
	 */
	public boolean setCurrencyExchangeRate(String fromCurrency, String toCurrency, double fxRate)
	{
		currencyFxRatesGraph.addVertex(fromCurrency);
		currencyFxRatesGraph.addVertex(toCurrency);
		
		/**
		 * We are removing edges and adding again,because we are assuming that new exchange rate has been provided for current currency pair.
		 */
		if (currencyFxRatesGraph.containsEdge(fromCurrency, toCurrency)) {
			currencyFxRatesGraph.removeEdge(fromCurrency, toCurrency);
			currencyFxRatesGraph.removeEdge(toCurrency, fromCurrency);
		}
		
		boolean	addDirectCurrency = currencyFxRatesGraph.addEdge(fromCurrency, toCurrency, fxRate);
		boolean	addReverseCurrency = currencyFxRatesGraph.addEdge(toCurrency, fromCurrency, 1.0 / fxRate);
		
		return addDirectCurrency && addReverseCurrency;
	}

	public SimpleDirectedGraph<String, Double> getCurrencyFxRatesGraph() {
		return currencyFxRatesGraph;
	}
	
	public static CurrencyExchangeRatesMapping getInstance(String propertyfilePath){
		if(instance == null){
	        synchronized (CurrencyExchangeRatesMapping.class) {
				if(instance==null){
					instance = new CurrencyExchangeRatesMapping(propertyfilePath);
				}
	        }
		}
		return instance;
	}
}
