package com.c8y;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.c8y.models.Battery;
import com.c8y.models.Custom;
import com.c8y.models.Humidity;
import com.c8y.models.Light;
import com.c8y.models.Measurement;
import com.c8y.models.RandomValue;
import com.c8y.models.SignalStrengthBer;
import com.c8y.models.SignalStrengthRssi;
import com.c8y.models.Temperature;
import com.cumulocity.model.ID;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.identity.ExternalIDRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.sdk.client.identity.IdentityApi;

import c8y.Position;

/**
 * @author aaron.kreis@softwareag.com
 */
public class App {

	private static final Logger logger = LoggerFactory.getLogger(App.class);
	protected static Measurement[] measurements;
	protected static String internalDeviceId;
	
  public static void main(String[] args) {
    if (Cumulocity.initialize()) {
      if (Helper.valid(Helper.externalDeviceId)!= null) {
        internalDeviceId = getInternalId(Helper.externalDeviceId);
        if(internalDeviceId!=null) {
          if (amITheOwnerOfTheDevice(internalDeviceId)) {
            try {
              createMeasurementList();
              startMqttOrRestSimulator();
              startPositionSimulator();
            } catch (Exception e) {
              logger.error("Internal error. ", e);
            }
          } else {
            logger.error("Check if the device exists. Maybe you are not the owner of the device with the id ("+ internalDeviceId + ").");
          }
        }
      }
    } else {
      logger.error("Initialisation error.");
    }
  }

	public static void createMeasurementList() {
	  
		ArrayList<String> measurementListFromSettingsFile = new ArrayList<String>(Arrays.asList(Helper.measurements.toLowerCase().split(",")));
    HashSet<String> hashSet = new HashSet<String>(measurementListFromSettingsFile);
    measurementListFromSettingsFile.clear();
    measurementListFromSettingsFile.addAll(hashSet);
		
		measurements = new Measurement[measurementListFromSettingsFile.size()];

		for(int i=0; i<measurementListFromSettingsFile.size(); i++) {
		    switch (measurementListFromSettingsFile.get(i)) {    
		        case "temperature":
		            measurements[i] = new Temperature();
		            break;
		        case "humidity":
		        	measurements[i] = new Humidity();
		            break;
		        case "battery":
		        	measurements[i] = new Battery();
		            break;
		        case "signalstrengthber":
		        	measurements[i] = new SignalStrengthBer();
		            break;
		        case "signalstrengthrssi":
		        	measurements[i] = new SignalStrengthRssi();
		            break;
		        case "light":
		        	measurements[i] = new Light();
		            break;
		        case "custom":
		        	measurements[i] = new Custom();
		            break;
		        default:
		            logger.error("Unknown measurement. Prease cheese between the supportet measurements.");
		    }
		}
	}
	
	public static void startMqttOrRestSimulator() {
	  if(Helper.numberMeasurements>=1) {
	    if(Helper.delayInSecondsBetweenEachMeasurement>=0) {
	      if(Helper.protocol.equals("mqtt")) {
	        createNewMQTTSimulator(Helper.externalDeviceId, Helper.numberMeasurements, Helper.delayInSecondsBetweenEachMeasurement, measurements);
	      } else if(Helper.protocol.equals("rest")) {
	        createNewRESTSimulator(internalDeviceId, Helper.numberMeasurements, Helper.delayInSecondsBetweenEachMeasurement, measurements);
	      } else {
	        logger.error("Unknown protocol. Choose between mqtt or rest.");
	      }
	    } else {
	      logger.error("Check delay between measurements.");
	    }
	  } else {
	    logger.error("Check number of measurements.");
	  }
	}
	
	public static void startPositionSimulator() {
		if(Helper.showDevicePostion.equals("true")) {
			try {
			  // TODO
				positionSimulator(internalDeviceId, Helper.numberMeasurements, Helper.delayInSecondsBetweenEachMeasurement);
			} catch(Exception e) {
				logger.error("Can't display device position.");
			}		
		}
	}
	
	public static void positionSimulator(String id, int numberMeasurements, int sleepTimerInSeconds) {

		String[] route = Helper.position.split(";");
		
		ManagedObjectRepresentation tempObject = null;
		ManagedObjectRepresentation managedObjectRepresentation = null;
		Position position = null;
		
		int counter = 0;
		for (int i=0; i<numberMeasurements; i++) {	

			if(counter>=route.length) {
				counter = 0;
			}
			
			position = new Position();
			position.setLng(BigDecimal.valueOf(Double.parseDouble(route[counter].split(",")[0])));
			position.setLat(BigDecimal.valueOf(Double.parseDouble(route[counter].split(",")[1])));
			
			// my existing managed object
			managedObjectRepresentation = Cumulocity.inventoryApi.get(GId.asGId(id));
			
			// create a clone of the managed object
			tempObject = new ManagedObjectRepresentation();
			tempObject.setId(managedObjectRepresentation.getId());
			tempObject.set(position);
			Cumulocity.inventoryApi.update(tempObject);
			counter++;
			Helper.sleep(sleepTimerInSeconds);
		}
	}
	
	public static String getInternalId(String externalId) {
	  try {
	    IdentityApi identityApi = Cumulocity.platform.getIdentityApi();       
	    ExternalIDRepresentation externalIDRepresentation = identityApi.getExternalId(new ID("c8y_Serial", externalId));
	    return externalIDRepresentation.getManagedObject().getId().getValue();
	  } catch (Exception e) {
	    logger.error("Check your external Id. ", e);
	    return null;
	  }
	}
	
	public static boolean amITheOwnerOfTheDevice(String id) {
		try {
			ManagedObjectRepresentation managedObjectRepresentation = Cumulocity.inventoryApi.get(GId.asGId(id));
			if(managedObjectRepresentation.getOwner().equals(Helper.username)) {
				return true;
			} else {
				return false;
			}			
		} catch(Exception e) {
			return false;
		}
	}
	
	public static void createNewMQTTSimulator(String id, int numberMeasurements, int sleepTimerInSeconds, Measurement[] measurements) {
		
	  try {
	    MqttClient client = MQTTClient.createNewMqttClient(id);
	    
	    for(int i=0; i<measurements.length; i++) {
	      Measurement measurement = measurements[i];
	      RandomValue randomValue = new RandomValue(measurement.getStartValue(), measurement.getMinValue(), measurement.getMaxValue(), measurement.getVariance());
	      Thread thread = new Thread(new Runnable() {
	        public void run() {
	          if(measurement instanceof Temperature) {
	            MeasurementBuilder.createTemperatureMQTTmeasurement(client, numberMeasurements, sleepTimerInSeconds, randomValue);
	          } else if(measurement instanceof Humidity) {
	            MeasurementBuilder.createHumidityMQTTmeasurement(client, numberMeasurements, sleepTimerInSeconds, randomValue);
	          } else if(measurement instanceof Battery) {
	            MeasurementBuilder.createBatteryMQTTmeasurement(client, numberMeasurements, sleepTimerInSeconds, randomValue);
	          }  else if(measurement instanceof SignalStrengthBer) {
	            MeasurementBuilder.createSignalStrengthBerMQTTmeasurement(client, numberMeasurements, sleepTimerInSeconds, randomValue);
	          } else if(measurement instanceof SignalStrengthRssi) {
	            MeasurementBuilder.createSignalStrengthSsiMQTTmeasurement(client, numberMeasurements, sleepTimerInSeconds, randomValue);
	          } else if(measurement instanceof Light) {
	            MeasurementBuilder.createLightMQTTmeasurement(client, numberMeasurements, sleepTimerInSeconds, randomValue);
	          } else if(measurement instanceof Custom) {
	            MeasurementBuilder.createCustomMQTTmeasurement(client, numberMeasurements, sleepTimerInSeconds, randomValue);
	          }         
	          else {
	            logger.error("Unknown measurement.");
	          }
	        }
	      });
	      
	      thread.start();
	      logger.info("Simulator is up and running. Sending measurements via MQTT :)");
	    }
	  } catch (Exception e) {
	    logger.error("Can't create a MQTT client. ",e);
	  }
	}

	public static void createNewRESTSimulator(String id, int numberMeasurements, int sleepTimerInSeconds, Measurement[] measurements) {
		for(int i=0; i<measurements.length; i++) {
			Measurement measurement = measurements[i];
			RandomValue randomValue = new RandomValue(measurement.getStartValue(), measurement.getMinValue(), measurement.getMaxValue(), measurement.getVariance());
			Thread thread = new Thread(new Runnable() {
				public void run() {
					if(measurement instanceof Temperature) {
						MeasurementBuilder.createTemperatureRESTmeasurement(id, numberMeasurements, sleepTimerInSeconds, randomValue);
					} else if(measurement instanceof Humidity) {
						MeasurementBuilder.createHumidityRESTmeasurement(id, numberMeasurements, sleepTimerInSeconds, randomValue);
					} else if(measurement instanceof Battery) {
						MeasurementBuilder.createBatteryRESTmeasurement(id, numberMeasurements, sleepTimerInSeconds, randomValue);
					}  else if(measurement instanceof SignalStrengthBer) {
						MeasurementBuilder.createSignalStrengthBerRESTmeasurement(id, numberMeasurements, sleepTimerInSeconds, randomValue);
					} else if(measurement instanceof SignalStrengthRssi) {
						MeasurementBuilder.createSignalStrengthSsiRESTmeasurement(id, numberMeasurements, sleepTimerInSeconds, randomValue);
					} else if(measurement instanceof Light) {
						MeasurementBuilder.createLightRESTmeasurement(id, numberMeasurements, sleepTimerInSeconds, randomValue);
					} else if(measurement instanceof Custom) {
						MeasurementBuilder.createCustomRESTmeasurement(id, numberMeasurements, sleepTimerInSeconds, randomValue);
					}
					else {
						logger.error("Unknown measurement.");
					}
				}
			});
			
			thread.start();
			logger.info("Simulator is up and running. Sending measurements via REST :)");
    }
  }

}
