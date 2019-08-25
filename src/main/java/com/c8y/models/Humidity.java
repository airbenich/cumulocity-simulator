package com.c8y.models;

import org.apache.log4j.Logger;

import com.c8y.Helper;

public class Humidity extends Measurement {

  private static final Logger logger = Logger.getLogger(Humidity.class);

  public Humidity() {
    try {
      super.startValue = Helper.toInt(Helper.humidity_startValue);
      super.minValue = Helper.toInt(Helper.humidity_minValue);
      super.maxValue = Helper.toInt(Helper.humidity_maxValue);
      super.variance = Helper.toInt(Helper.humidity_variance);
    } catch (Exception e) {
      logger.debug("Can't read humidity measurement settings. ", e);
    }
  }
}