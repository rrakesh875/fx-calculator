package com.fx.calculator.service;

import java.io.File;

import org.junit.BeforeClass;
import org.junit.Test;

import com.fx.calculator.exception.NoConversionMatchFoundException;
import com.fx.calculator.service.FXCalculatorService;
import com.fx.calculator.service.impl.FXCalculatorServiceImpl;
import com.fx.calculator.utility.CurrencyExchangeRatesMapping;
import com.fx.calculator.utility.CurrencyPrecisionConfig;

import junit.framework.TestCase;

/**
 * Unit test for simple App.
 */
public class FxServiceTest 
    extends TestCase
{
	private String fxRatesPropertyFilePath="src"+File.separator+"test"+File.separator+"resources"+File.separator+"fxrates.properties";
	private String currencyPrecisionPropertyFilePath="src"+File.separator+"test"+File.separator+"resources"+File.separator+"precision.properties";
	private CurrencyExchangeRatesMapping exchngRateMapping;
	private CurrencyPrecisionConfig currencyPrecisionConfig;
	private FXCalculatorService service;
	
	
	@BeforeClass
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		exchngRateMapping = CurrencyExchangeRatesMapping.getInstance(fxRatesPropertyFilePath);
		currencyPrecisionConfig = CurrencyPrecisionConfig.getInstance(currencyPrecisionPropertyFilePath);
		service = new FXCalculatorServiceImpl(exchngRateMapping, currencyPrecisionConfig);
	}
	
	@Test
	public void test_calculateConversionForDirect() {
		double calculatedVal = service.calculate("AUD", "USD", 10L);
		assertEquals(calculatedVal, 8.37);
	}
	
	@Test
	public void test_CalcuateConversionInverse() {
		double calculatedVal = service.calculate("USD", "AUD", 10L);
		assertEquals(calculatedVal, 11.95);
	}
	
	@Test
	public void test_CalcuateConversionCrossCurrency() {
		double calculatedVal = service.calculate("AUD", "NOK", 10L);
		assertEquals(calculatedVal, 58.90);
	}
	
	@Test
	public void test_CalcuateConversionWithZeroPrecision() {
		double calculatedVal = service.calculate("AUD", "JPY", 10L);
		assertEquals(calculatedVal, 1004.00);
	}
	
	@Test(expected = NoConversionMatchFoundException.class)
	public void test_CalcuateConversionNoConversion() {
		service.calculate("AUD", "NZD", 10L);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_CalcuateConversionInValidCurrency() {
		service.calculate("ZAR", "NZD", 10L);
	}
	
	
}
