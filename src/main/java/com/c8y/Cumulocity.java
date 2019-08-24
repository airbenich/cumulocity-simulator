package com.c8y;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cumulocity.model.authentication.CumulocityBasicCredentials;
import com.cumulocity.model.authentication.CumulocityCredentials;
import com.cumulocity.sdk.client.Platform;
import com.cumulocity.sdk.client.PlatformBuilder;
import com.cumulocity.sdk.client.inventory.InventoryApi;
import com.cumulocity.sdk.client.measurement.MeasurementApi;

public class Cumulocity {

  private final static Logger logger = LoggerFactory.getLogger(Cumulocity.class);
  
  public static CumulocityCredentials credentials = null;
  public static Platform platform = null;
  public static MeasurementApi measurementApi = null;
  public static InventoryApi inventoryApi = null;
  
  protected static String filePath = System.getProperty("user.home")+File.separator+"simulator.properties";

  private Cumulocity() {}
  
  public static boolean initialize() {
    
    Helper.preparePropertyFile(filePath);
    
    if(Helper.prop!=null) {
      
      try {
        credentials = new CumulocityBasicCredentials(Helper.username, Helper.tenantId, Helper.password, null, null);
      } catch(Exception e) {
        logger.error("Check your credentials and your tenantId. ", e);
        return false;
      }
      
      try {
        platform = PlatformBuilder.platform().withBaseUrl(Helper.server).withCredentials(credentials).build();
      } catch(Exception e) {
        logger.error("Check your server address and your credentials. ", e);
        return false;
      }
      
      try {
        measurementApi = platform.getMeasurementApi();
      } catch(Exception e) {
        logger.error("There is a problem with the measurement API. Check your credentials and the server address. ", e);
        return false;
      }
      
      try {
        inventoryApi = platform.getInventoryApi();
      } catch(Exception e) {
        logger.error("There is a problem with the inventory API. ", e);
        return false;
      }
      return true;
    } else {
      return false;
    } 
  }
}
