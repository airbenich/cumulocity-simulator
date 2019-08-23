package com.c8y.models;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.c8y.App;
import com.c8y.helper.Common;

public class Battery extends Measurement {
	
	static Logger logger = LoggerFactory.getLogger(App.class);
	static Properties prop = Common.getPropertyFile();

	public Battery() {
		try {
			super.startValue = Integer.parseInt(prop.getProperty("battery_startValue"));
			super.minValue = Integer.parseInt(prop.getProperty("battery_minValue"));
			super.maxValue = Integer.parseInt(prop.getProperty("battery_maxValue"));
			super.variance = Integer.parseInt(prop.getProperty("battery_variance"));
		} catch(Exception e) {
			logger.debug("Can't read battery measurement settings.");
		}
	}
}