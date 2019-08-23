package com.c8y.models;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.c8y.App;
import com.c8y.helper.Common;

public class Custom extends Measurement {
	
	static Logger logger = LoggerFactory.getLogger(App.class);
	static Properties prop = Common.getPropertyFile();

	public Custom() {
		try {
			super.startValue = Integer.parseInt(prop.getProperty("custom_startValue"));
			super.minValue = Integer.parseInt(prop.getProperty("custom_minValue"));
			super.maxValue = Integer.parseInt(prop.getProperty("custom_maxValue"));
			super.variance = Integer.parseInt(prop.getProperty("custom_variance"));
		} catch(Exception e) {
			logger.debug("Can't read custom measurement settings.");
		}
	}
}