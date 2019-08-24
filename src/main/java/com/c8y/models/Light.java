package com.c8y.models;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.c8y.Helper;

public class Light extends Measurement {

	static Logger logger = LoggerFactory.getLogger(Light.class);
	
	public Light() {
		try {
			super.startValue = Helper.toInt(Helper.light_startValue);
			super.minValue = Helper.toInt(Helper.light_minValue);
			super.maxValue = Helper.toInt(Helper.light_maxValue);
			super.variance = Helper.toInt(Helper.light_variance);
		} catch(Exception e) {
			logger.debug("Can't read light measurement settings. ", e);
		}
	}
}