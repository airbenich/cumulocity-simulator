package com.c8y;

import java.io.File;
import java.math.BigDecimal;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.c8y.helper.Common;
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
	protected static String externalId;
	protected static String internalId;
	protected static int delayInSecondsBetweenEachMeasurement;
	protected static int totalNumberMeasurements;
	
	public static void main(String[] args) {
		
		try {
			
			if(Common.getPropertyFile()!=null) {
				
				boolean initialiationStatus = MeasurementBuilder.initialize();
				externalId = MeasurementBuilder.prop.getProperty("externalDeviceId");
				internalId = getInternalId(externalId);
				delayInSecondsBetweenEachMeasurement = Integer.parseInt(MeasurementBuilder.prop.getProperty("delayInSecondsBetweenEachMeasurement"));
				totalNumberMeasurements = Integer.parseInt(MeasurementBuilder.prop.getProperty("numberMeasurements"));
				
				if(initialiationStatus) {
					
					if(amITheOwnerOfTheDevice(internalId)) {
						try {
							
							createMeasurementList();
							
							startMqttOrRestSimulator();
							
							startPositionSimulator();
							
						} catch(Exception e) {
							logger.error("Can't create measurments. Check your external device id and the list of your measurments.");
						}
					} else {
						logger.error("Can't start the simulator.");
					}
				}
			}
		} catch(Exception e) {
			logger.error("Can't read the property file or the keys. Please copy your property file into the folowing folder: \n"+System.getProperty("user.home")+File.separator+"simulator.properties");
		}

	}

	public static void createMeasurementList() {
		
		String[] measurementList = MeasurementBuilder.prop.getProperty("measurements").toLowerCase().replaceAll("\\s+","").split(",");
		measurements = new Measurement[measurementList.length];

		for(int i=0; i<measurementList.length; i++) {
		    switch (measurementList[i]) {
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
		if(MeasurementBuilder.prop.getProperty("protocol").equals("mqtt")) {
			createNewMQTTSimulator(externalId, totalNumberMeasurements, delayInSecondsBetweenEachMeasurement, measurements);
			logger.info("Simulator is up and running. Sending measurements via MQTT :)");
		} else if(MeasurementBuilder.prop.getProperty("protocol").equals("rest")) {
			createNewRESTSimulator(internalId, totalNumberMeasurements, delayInSecondsBetweenEachMeasurement, measurements);
			logger.info("Simulator is up and running. Sending measurements via REST :)");
		} else {
			logger.error("Unknown protocol. Please choose between mqtt or rest.");
		}
	}
	
	public static void startPositionSimulator() {
		if(MeasurementBuilder.prop.getProperty("showDevicePostion").equals("true")) {
			try {
				positionSimulator(getInternalId(MeasurementBuilder.prop.getProperty("externalDeviceId")), totalNumberMeasurements, delayInSecondsBetweenEachMeasurement);
			} catch(Exception e) {
				logger.error("Can't display device position.");
			}		
		}
	}
	
	public static void positionSimulator(String id, int numberMeasurements, int sleepTimerInSeconds) {

		String[] route = MeasurementBuilder.prop.getProperty("position").split(";");
		
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
			managedObjectRepresentation = MeasurementBuilder.inventoryApi.get(GId.asGId(id));
			
			// create a clone of the managed object
			tempObject = new ManagedObjectRepresentation();
			tempObject.setId(managedObjectRepresentation.getId());
			tempObject.set(position);
			MeasurementBuilder.inventoryApi.update(tempObject);
			counter++;
			Common.sleep(sleepTimerInSeconds);
		}
	}
	
	public static String getInternalId(String externalId) {
		IdentityApi identityApi = MeasurementBuilder.platform.getIdentityApi();				
		ExternalIDRepresentation externalIDRepresentation = identityApi.getExternalId(new ID("c8y_Serial", externalId));
		return externalIDRepresentation.getManagedObject().getId().getValue();
	}
	
	public static boolean amITheOwnerOfTheDevice(String id) {
		try {
			ManagedObjectRepresentation managedObjectRepresentation = MeasurementBuilder.inventoryApi.get(GId.asGId(id));
			if(managedObjectRepresentation.getOwner().equals(MeasurementBuilder.prop.getProperty("username"))) {
				return true;
			} else {
				logger.error("Check if the device exists. Maybe you are not the owner of the device with the id ("+id+").");
			}			
		} catch(Exception e) {
			return false;
		}
		return false;
	}
	
	public static void createNewMQTTSimulator(String id, int numberMeasurements, int sleepTimerInSeconds, Measurement[] measurements) {
		
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
		}
	}

}
