package com.c8y.models;

import org.apache.log4j.Logger;

import com.c8y.Helper;

public class Battery extends Measurement {

  private static final Logger logger = Logger.getLogger(Battery.class);

  public Battery() {
    try {
      super.startValue = Helper.toInt(Helper.battery_startValue);
      super.minValue = Helper.toInt(Helper.battery_minValue);
      super.maxValue = Helper.toInt(Helper.battery_maxValue);
      super.variance = Helper.toInt(Helper.battery_variance);
    } catch (Exception e) {
      logger.debug("Can't read battery measurement settings. ", e);
    }
  }
}