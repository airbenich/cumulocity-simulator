package com.c8y.models;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.c8y.App;
import com.c8y.helper.Common;

public class Light extends Measurement {

	static Logger logger = LoggerFactory.getLogger(App.class);
	static Properties prop = Common.getPropertyFile();
	
	public Light() {
		try {
			super.startValue = Integer.parseInt(prop.getProperty("light_startValue"));
			super.minValue = Integer.parseInt(prop.getProperty("light_minValue"));
			super.maxValue = Integer.parseInt(prop.getProperty("light_maxValue"));
			super.variance = Integer.parseInt(prop.getProperty("light_variance"));
		} catch(Exception e) {
			logger.debug("Can't read light measurement settings.");
		}
	}
}