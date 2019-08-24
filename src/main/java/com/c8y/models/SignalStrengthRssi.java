package com.c8y.models;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.c8y.Helper;

public class SignalStrengthRssi extends Measurement {

	static Logger logger = LoggerFactory.getLogger(SignalStrengthRssi.class);
	
	public SignalStrengthRssi() {
		try {
			super.startValue = Helper.toInt(Helper.signalStrength_ssi_startValue);
			super.minValue = Helper.toInt(Helper.signalStrength_ssi_minValue);
			super.maxValue = Helper.toInt(Helper.signalStrength_ssi_maxValue);
			super.variance = Helper.toInt(Helper.signalStrength_ssi_variance);
		} catch(Exception e) {
			logger.debug("Can't read signal strength (rssi) measurement settings. ", e);
		}
	}
}