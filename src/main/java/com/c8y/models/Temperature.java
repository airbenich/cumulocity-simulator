package com.c8y.models;

import org.apache.log4j.Logger;

import com.c8y.Helper;

public class Temperature extends Measurement {

  private static final Logger logger = Logger.getLogger(Temperature.class);

  public Temperature() {
    try {
      super.startValue = Helper.toInt(Helper.temperature_startValue);
      super.minValue = Helper.toInt(Helper.temperature_minValue);
      super.maxValue = Helper.toInt(Helper.temperature_maxValue);
      super.variance = Helper.toInt(Helper.temperature_variance);
    } catch (Exception e) {
      logger.debug("Can't read temperature measurement settings. ", e);
    }
  }

}