package com.c8y.models;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.c8y.Helper;

public class SignalStrengthBer extends Measurement {

	static Logger logger = LoggerFactory.getLogger(SignalStrengthBer.class);
	
	public SignalStrengthBer() {
		try {
			super.startValue = Helper.toInt(Helper.signalStrength_ber_startValue);
			super.minValue = Helper.toInt(Helper.signalStrength_ber_minValue);
			super.maxValue = Helper.toInt(Helper.signalStrength_ber_maxValue);
			super.variance = Helper.toInt(Helper.signalStrength_ber_variance);
		} catch(Exception e) {
			logger.debug("Can't read signal strength (ber) measurement settings. ", e);
		}
	}
}