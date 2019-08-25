package com.c8y.models;

import org.apache.log4j.Logger;

import com.c8y.Helper;

public class Light extends Measurement {

  private static final Logger logger = Logger.getLogger(Light.class);

  public Light() {
    try {
      super.startValue = Helper.toInt(Helper.light_startValue);
      super.minValue = Helper.toInt(Helper.light_minValue);
      super.maxValue = Helper.toInt(Helper.light_maxValue);
      super.variance = Helper.toInt(Helper.light_variance);
    } catch (Exception e) {
      logger.debug("Can't read light measurement settings. ", e);
    }
  }
}