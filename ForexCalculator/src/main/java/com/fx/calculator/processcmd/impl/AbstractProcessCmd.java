package com.fx.calculator.processcmd.impl;

import java.io.File;
import java.util.Scanner;
import java.util.Set;

import com.fx.calculator.processcmd.ProcessCmd;
import com.fx.calculator.utility.CurrencyExchangeRatesMapping;
import com.fx.calculator.utility.CurrencyPrecisionConfig;
import com.fx.calculator.utility.FXUtility;
import com.fx.calculator.validation.Validator;

public abstract class AbstractProcessCmd implements ProcessCmd,Validator{
	Scanner in;
	CurrencyExchangeRatesMapping exchngRateMapping;
	CurrencyPrecisionConfig currencyPrecisionConfig;
	
	private String fxRatesPropertyFilePath,currencyPrecisionPropertyFilePath;
	
	public AbstractProcessCmd(String fxRatesPropertyFilePath, String currencyPrecisionPropertyFilePath) {
		in = new Scanner(System.in);
		if(!FXUtility.isInValidEntry(fxRatesPropertyFilePath)) this.fxRatesPropertyFilePath = fxRatesPropertyFilePath;
		if(!FXUtility.isInValidEntry(currencyPrecisionPropertyFilePath)) this.currencyPrecisionPropertyFilePath = currencyPrecisionPropertyFilePath;
		exchngRateMapping = CurrencyExchangeRatesMapping.getInstance(fxRatesPropertyFilePath);
		currencyPrecisionConfig = CurrencyPrecisionConfig.getInstance(currencyPrecisionPropertyFilePath);
	}

	@Override
	public boolean isCurrencyPresent(String currency) {
		Set<Object> currSet = currencyPrecisionConfig.getProp().keySet();
		for(Object obj : currSet) {
			String currntCurr = (String)obj;
			if(currency.equalsIgnoreCase(currntCurr)) return true;
		}
		return false;
	}
	
	void printCurrentlySupportedCurrency() {
		Set<Object> currSet = currencyPrecisionConfig.getProp().keySet();
		for(Object obj : currSet) {
			System.out.println(String.valueOf(obj));
		}
	}

}
