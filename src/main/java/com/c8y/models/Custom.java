package com.c8y.models;

import org.apache.log4j.Logger;

import com.c8y.Helper;

public class Custom extends Measurement {

  private static final Logger logger = Logger.getLogger(Custom.class);

  public Custom() {
    try {
      super.startValue = Helper.toInt(Helper.custom_startValue);
      super.minValue = Helper.toInt(Helper.custom_minValue);
      super.maxValue = Helper.toInt(Helper.custom_maxValue);
      super.variance = Helper.toInt(Helper.custom_variance);
    } catch (Exception e) {
      logger.debug("Can't read custom measurement settings. ", e);
    }
  }
}