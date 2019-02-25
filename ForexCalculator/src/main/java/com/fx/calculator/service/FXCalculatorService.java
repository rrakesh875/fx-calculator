package com.fx.calculator.service;

import java.math.BigDecimal;

public interface FXCalculatorService {
	public double calculate(String fromCurrency, String toCurrency, Long quantity);
}
