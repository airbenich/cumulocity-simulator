package com.c8y.models;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.c8y.App;
import com.c8y.helper.Common;

public class SignalStrengthBer extends Measurement {

	static Logger logger = LoggerFactory.getLogger(App.class);
	static Properties prop = Common.getPropertyFile();
	
	public SignalStrengthBer() {
		try {
			super.startValue = Integer.parseInt(prop.getProperty("signalStrength_ber_startValue"));
			super.minValue = Integer.parseInt(prop.getProperty("signalStrength_ber_minValue"));
			super.maxValue = Integer.parseInt(prop.getProperty("signalStrength_ber_maxValue"));
			super.variance = Integer.parseInt(prop.getProperty("signalStrength_ber_variance"));
		} catch(Exception e) {
			logger.debug("Can't read signal strength (ber) measurement settings.");
		}
	}
}