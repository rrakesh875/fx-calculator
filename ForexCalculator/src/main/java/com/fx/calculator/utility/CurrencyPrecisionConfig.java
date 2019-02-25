package com.fx.calculator.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Property Config class
 * Caters to construct list of currencies supported by system and their precision used during conversion.
 * At the time of system start, reads from precision.properties and gets updated with user entry.
 * 
 * Singleton as more than one instance can lead to integrity issues.
 * @author Ravi Rakesh
 *
 */
public class CurrencyPrecisionConfig {
	private static final Logger log = LoggerFactory.getLogger(CurrencyPrecisionConfig.class);
	private static CurrencyPrecisionConfig instance=null;
	private Properties prop;

	private CurrencyPrecisionConfig(String filePath) {init(filePath);};
	
	private void init(String filePath){
		try {
		prop = new Properties();
		if(filePath!=null && !"".equals(filePath)) {
			prop.load(new FileInputStream(new File(filePath)));
		}else {
			prop.load(getClass().getResourceAsStream("/precision.properties"));
		}
		} catch (FileNotFoundException e) {
			log.error("Seems wrong path entered for property file");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static CurrencyPrecisionConfig getInstance(String filePath){
		if(instance == null){
	        synchronized (CurrencyPrecisionConfig.class) {
				if(instance==null){
					instance = new CurrencyPrecisionConfig(filePath);
				}
	        }
		}
		return instance;
	}

	public Properties getProp() {
		return prop;
	}

}
