package com.c8y;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

public class Helper {

  private static final Logger logger = Logger.getLogger(Helper.class);

  public static Properties prop = new Properties();

  protected static String username;
  protected static String tenantId;
  protected static String password;
  protected static String server;

  protected static String protocol;
  protected static String externalDeviceId;
  protected static int delayInSecondsBetweenEachMeasurement;
  protected static int numberMeasurements;
  protected static String measurements;
  protected static String showDevicePostion;
  protected static String position;

  protected static String mqttServer;
  protected static String qos;
  protected static String cleanSession;
  protected static String staticTopic;
  protected static String customTopic;

  protected static String temperature_templateId;
  protected static String temperature_unit;
  protected static String temperature_fragment;
  public static String temperature_startValue;
  public static String temperature_minValue;
  public static String temperature_maxValue;
  public static String temperature_variance;

  protected static String humidity_templateId;
  protected static String humidity_fragment;
  protected static String humidity_serial;
  protected static String humidity_unit;
  public static String humidity_startValue;
  public static String humidity_minValue;
  public static String humidity_maxValue;
  public static String humidity_variance;

  protected static String battery_templateId;
  protected static String battery_fragment;
  protected static String battery_serial;
  protected static String battery_unit;
  public static String battery_startValue;
  public static String battery_minValue;
  public static String battery_maxValue;
  public static String battery_variance;

  protected static String signalStrength_ber_templateId;
  protected static String signalStrength_ber_fragment;
  protected static String signalStrength_ber_serial;
  protected static String signalStrength_ber_unit;
  public static String signalStrength_ber_startValue;
  public static String signalStrength_ber_minValue;
  public static String signalStrength_ber_maxValue;
  public static String signalStrength_ber_variance;

  protected static String signalStrength_ssi_templateId;
  protected static String signalStrength_ssi_fragment;
  protected static String signalStrength_ssi_serial;
  protected static String signalStrength_ssi_unit;
  public static String signalStrength_ssi_startValue;
  public static String signalStrength_ssi_minValue;
  public static String signalStrength_ssi_maxValue;
  public static String signalStrength_ssi_variance;

  protected static String light_templateId;
  protected static String light_fragment;
  protected static String light_serial;
  protected static String light_unit;
  public static String light_startValue;
  public static String light_minValue;
  public static String light_maxValue;
  public static String light_variance;

  protected static String custom_templateId;
  protected static String custom_fragment;
  protected static String custom_serial;
  protected static String custom_unit;
  public static String custom_startValue;
  public static String custom_minValue;
  public static String custom_maxValue;
  public static String custom_variance;

  private Helper() {
  }

  protected static Properties preparePropertyFile(String filePath) {
    try {
      prop.load(new FileInputStream(filePath));
      setSettingsFilerValues();
      return prop;
    } catch (IOException e) {
      logger.error(
          "Can't read the property file. Copy your property file (simulator.properties) into the following folder: "
              + filePath);
      prop = null;
      return null;
    }
  }

  protected static void setSettingsFilerValues() {

    username = valid(prop.getProperty("username"));
    tenantId = valid(prop.getProperty("tenantId"));
    password = valid(prop.getProperty("password"));
    server = valid(prop.getProperty("server"));

    protocol = valid(prop.getProperty("protocol"));
    externalDeviceId = valid(prop.getProperty("externalDeviceId"));
    delayInSecondsBetweenEachMeasurement = toInt(
        valid(prop.getProperty("delayInSecondsBetweenEachMeasurement")));
    numberMeasurements = toInt(valid(prop.getProperty("numberMeasurements")));
    measurements = valid(prop.getProperty("measurements"));
    showDevicePostion = valid(prop.getProperty("showDevicePostion"));
    position = valid(prop.getProperty("position"));

    mqttServer = valid(prop.getProperty("mqttServer"));
    qos = valid(prop.getProperty("qos"));
    cleanSession = valid(prop.getProperty("cleanSession"));
    staticTopic = valid(prop.getProperty("staticTopic"));
    customTopic = valid(prop.getProperty("customTopic"));

    temperature_templateId = valid(prop.getProperty("temperature_templateId"));
    temperature_unit = valid(prop.getProperty("temperature_unit"));
    temperature_fragment = valid(prop.getProperty("temperature_fragment"));
    temperature_startValue = valid(prop.getProperty("temperature_startValue"));
    temperature_minValue = valid(prop.getProperty("temperature_minValue"));
    temperature_maxValue = valid(prop.getProperty("temperature_maxValue"));
    temperature_variance = valid(prop.getProperty("temperature_variance"));

    humidity_templateId = valid(prop.getProperty("humidity_templateId"));
    humidity_fragment = valid(prop.getProperty("humidity_fragment"));
    humidity_serial = valid(prop.getProperty("humidity_serial"));
    humidity_unit = valid(prop.getProperty("humidity_unit"));
    humidity_startValue = valid(prop.getProperty("humidity_startValue"));
    humidity_minValue = valid(prop.getProperty("humidity_minValue"));
    humidity_maxValue = valid(prop.getProperty("humidity_maxValue"));
    humidity_variance = valid(prop.getProperty("humidity_variance"));

    battery_templateId = valid(prop.getProperty("battery_templateId"));
    battery_fragment = valid(prop.getProperty("battery_fragment"));
    battery_serial = valid(prop.getProperty("battery_serial"));
    battery_unit = valid(prop.getProperty("battery_unit"));
    battery_startValue = valid(prop.getProperty("battery_startValue"));
    battery_minValue = valid(prop.getProperty("battery_minValue"));
    battery_maxValue = valid(prop.getProperty("battery_maxValue"));
    battery_variance = valid(prop.getProperty("battery_variance"));

    signalStrength_ber_templateId = valid(prop.getProperty("signalStrength_ber_templateId"));
    signalStrength_ber_fragment = valid(prop.getProperty("signalStrength_ber_fragment"));
    signalStrength_ber_serial = valid(prop.getProperty("signalStrength_ber_serial"));
    signalStrength_ber_unit = valid(prop.getProperty("signalStrength_ber_unit"));
    signalStrength_ber_startValue = valid(prop.getProperty("signalStrength_ber_startValue"));
    signalStrength_ber_minValue = valid(prop.getProperty("signalStrength_ber_minValue"));
    signalStrength_ber_maxValue = valid(prop.getProperty("signalStrength_ber_maxValue"));
    signalStrength_ber_variance = valid(prop.getProperty("signalStrength_ber_variance"));

    signalStrength_ssi_templateId = valid(prop.getProperty("signalStrength_ssi_templateId"));
    signalStrength_ssi_fragment = valid(prop.getProperty("signalStrength_ssi_fragment"));
    signalStrength_ssi_serial = valid(prop.getProperty("signalStrength_ssi_serial"));
    signalStrength_ssi_unit = valid(prop.getProperty("signalStrength_ssi_unit"));
    signalStrength_ssi_startValue = valid(prop.getProperty("signalStrength_ssi_startValue"));
    signalStrength_ssi_minValue = valid(prop.getProperty("signalStrength_ssi_minValue"));
    signalStrength_ssi_maxValue = valid(prop.getProperty("signalStrength_ssi_maxValue"));
    signalStrength_ssi_variance = valid(prop.getProperty("signalStrength_ssi_variance"));

    light_templateId = valid(prop.getProperty("light_templateId"));
    light_fragment = valid(prop.getProperty("light_fragment"));
    light_serial = valid(prop.getProperty("light_serial"));
    light_unit = valid(prop.getProperty("light_unit"));
    light_startValue = valid(prop.getProperty("light_startValue"));
    light_minValue = valid(prop.getProperty("light_minValue"));
    light_maxValue = valid(prop.getProperty("light_maxValue"));
    light_variance = valid(prop.getProperty("light_variance"));

    custom_templateId = valid(prop.getProperty("custom_templateId"));
    custom_fragment = valid(prop.getProperty("custom_fragment"));
    custom_serial = valid(prop.getProperty("custom_serial"));
    custom_unit = valid(prop.getProperty("custom_unit"));
    custom_startValue = valid(prop.getProperty("custom_startValue"));
    custom_minValue = valid(prop.getProperty("custom_minValue"));
    custom_maxValue = valid(prop.getProperty("custom_maxValue"));
    custom_variance = valid(prop.getProperty("custom_variance"));
  }

  public static void sleep(int sleepTimerInSeconds) {
    try {
      TimeUnit.SECONDS.sleep(sleepTimerInSeconds);
    } catch (Exception e) {
      logger.debug("Sleep timer does not work.");
    }
  }

  public static String valid(String str) {
    if (str != null && str.length() >= 1) {
      return str.replaceAll("\\s+", "");
    }
    return null;
  }

  public static int toInt(String str) {
    if (valid(str) != null) {
      try {
        return Integer.parseInt(str);
      } catch (Exception e) {
        return 0;
      }
    } else {
      return 0;
    }
  }

  public static String getRamdomHash() {
    char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    StringBuilder sb = new StringBuilder(20);
    Random random = new Random();
    for (int i = 0; i < 20; i++) {
      char c = chars[random.nextInt(chars.length)];
      sb.append(c);
    }
    return sb.toString();
  }

}
