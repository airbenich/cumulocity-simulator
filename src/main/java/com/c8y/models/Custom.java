package com.c8y.models;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.c8y.Helper;

public class Custom extends Measurement {
	
	static Logger logger = LoggerFactory.getLogger(Custom.class);

	public Custom() {
		try {
			super.startValue = Helper.toInt(Helper.custom_startValue);
			super.minValue = Helper.toInt(Helper.custom_minValue);
			super.maxValue = Helper.toInt(Helper.custom_maxValue);
			super.variance = Helper.toInt(Helper.custom_variance);
		} catch(Exception e) {
			logger.debug("Can't read custom measurement settings. ", e);
		}
	}
}