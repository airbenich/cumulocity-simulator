package com.c8y.models;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.c8y.Helper;

public class Temperature extends Measurement {
	
	static Logger logger = LoggerFactory.getLogger(Temperature.class);
	
	public Temperature() {
		try {
			super.startValue = Helper.toInt(Helper.temperature_startValue);
			super.minValue = Helper.toInt(Helper.temperature_minValue);
			super.maxValue = Helper.toInt(Helper.temperature_maxValue);
			super.variance = Helper.toInt(Helper.temperature_variance);
		} catch(Exception e) {
			logger.debug("Can't read temperature measurement settings. ", e);
		}
	}
	
}