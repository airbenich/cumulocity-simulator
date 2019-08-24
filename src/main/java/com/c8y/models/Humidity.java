package com.c8y.models;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.c8y.Helper;

public class Humidity extends Measurement {
	
	static Logger logger = LoggerFactory.getLogger(Humidity.class);

	public Humidity() {
		try {
			super.startValue = Helper.toInt(Helper.humidity_startValue);
			super.minValue = Helper.toInt(Helper.humidity_minValue);
			super.maxValue = Helper.toInt(Helper.humidity_maxValue);
			super.variance = Helper.toInt(Helper.humidity_variance);
		} catch(Exception e) {
			logger.debug("Can't read humidity measurement settings. ", e);
		}
	}
}