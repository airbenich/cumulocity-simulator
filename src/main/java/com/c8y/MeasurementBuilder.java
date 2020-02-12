package com.c8y;

import java.math.BigDecimal;
import java.util.Random;

import org.apache.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.joda.time.DateTime;

import com.c8y.models.RandomValue;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.model.measurement.MeasurementValue;
import com.cumulocity.rest.representation.inventory.ManagedObjects;
import com.cumulocity.rest.representation.measurement.MeasurementRepresentation;

import c8y.Battery;
import c8y.HumidityMeasurement;
import c8y.LightMeasurement;
import c8y.SignalStrength;
import c8y.TemperatureMeasurement;

public class MeasurementBuilder {
	
  private static final Logger logger = Logger.getLogger(MeasurementBuilder.class);

	private MeasurementBuilder() {}
	
	public static MeasurementRepresentation createMeasurementRepresentation(String deviceId) {
		try {
			MeasurementRepresentation measurementRepresentation = new MeasurementRepresentation();
			measurementRepresentation.setSource(ManagedObjects.asManagedObject(GId.asGId(deviceId)));
			measurementRepresentation.setDateTime(new DateTime());
			return measurementRepresentation;
		} catch (Exception e) {
			logger.debug("Can't create measurement representation object.");
			return null;
		}
	}
	
	// -----------------------------
	// Temperature
	// -----------------------------
	public static void createTemperatureMQTTmeasurement(MqttClient client, int numberMeasurements, int sleepTimerInSeconds, RandomValue randomValue) {
		try {
			for (int i = 0; i < numberMeasurements; i++) {
				StringBuilder value = new StringBuilder();
				value.append(Helper.temperature_templateId);
				value.append(",");
				value.append(MeasurementBuilder.getRandomValue(randomValue));
				MQTTClient.setMQTTmeasurement(client, value.toString(), null);
				Helper.sleep(sleepTimerInSeconds);
			}			
		} catch(Exception e) {
			logger.error("Can't create temperature measurement via MQTT.");
		}
	}

	public static void createTemperatureRESTmeasurement(String deviceId, int numberMeasurements, int sleepTimerInSeconds, RandomValue randomValue) {
		try {
			MeasurementRepresentation measurementRepresentation = createMeasurementRepresentation(deviceId);
			for (int i = 0; i < numberMeasurements; i++) {
				TemperatureMeasurement temperatureMeasurement = new TemperatureMeasurement();
				temperatureMeasurement.setT(new MeasurementValue(BigDecimal.valueOf(getRandomValue(randomValue)), Helper.temperature_unit));
				measurementRepresentation.setType(Helper.temperature_fragment);
				measurementRepresentation.set(temperatureMeasurement);
				measurementRepresentation.setDateTime(new DateTime());
				Cumulocity.measurementApi.create(measurementRepresentation);
				Helper.sleep(sleepTimerInSeconds);
			}
		} catch(Exception e) {
			logger.error("Can't create temperature measurement via REST.");
		}
	}

	// -----------------------------
	// Humidity
	// -----------------------------
	public static void createHumidityMQTTmeasurement(MqttClient client, int numberMeasurements, int sleepTimerInSeconds, RandomValue randomValue) {
		try {
			for (int i = 0; i < numberMeasurements; i++) {
				StringBuilder value = new StringBuilder();
				value.append(Helper.humidity_templateId);
				value.append(",");
				value.append(Helper.humidity_fragment);
				value.append(",");
				value.append(Helper.humidity_serial);
				value.append(",");
				value.append(MeasurementBuilder.getRandomValue(randomValue));
				value.append(",");
				value.append(Helper.humidity_unit);
				MQTTClient.setMQTTmeasurement(client, value.toString(), null);
				Helper.sleep(sleepTimerInSeconds);
			}	
		} catch(Exception e) {
			logger.error("Can't create humidity measurement via MQTT.");
		}
	}

	public static void createHumidityRESTmeasurement(String deviceId, int numberMeasurements, int sleepTimerInSeconds, RandomValue randomValue) {
		try {
			MeasurementRepresentation measurementRepresentation = createMeasurementRepresentation(deviceId);
			for (int i = 0; i < numberMeasurements; i++) {
				HumidityMeasurement humidityMeasurement = new HumidityMeasurement();
				humidityMeasurement.setH(new MeasurementValue(BigDecimal.valueOf(getRandomValue(randomValue)), Helper.humidity_unit));
				measurementRepresentation.setType(Helper.humidity_fragment);
        measurementRepresentation.setDateTime(new DateTime());
				measurementRepresentation.set(humidityMeasurement);
				Cumulocity.measurementApi.create(measurementRepresentation);
				Helper.sleep(sleepTimerInSeconds);
			}
		} catch(Exception e) {
			logger.error("Can't create humidity measurement via REST.");
		}
	}
	
	
	// -----------------------------
	// Battery
	// -----------------------------
	public static void createBatteryMQTTmeasurement(MqttClient client, int numberMeasurements, int sleepTimerInSeconds, RandomValue randomValue) {
		try {
			int counter = randomValue.getStartValue();
			for (int i = 0; i < numberMeasurements; i++) {
				StringBuilder value = new StringBuilder();
				value.append(Helper.battery_templateId);
				value.append(",");
				value.append(Helper.battery_fragment);
				value.append(",");
				value.append(Helper.battery_serial);
				value.append(",");
				if(counter>randomValue.getMaxValue()) {
					counter = randomValue.getStartValue();
				}
				value.append(counter);
				value.append(",");
				value.append(Helper.battery_unit);
				MQTTClient.setMQTTmeasurement(client, value.toString(), null);
				counter += 1;
				Helper.sleep(sleepTimerInSeconds);
			}			
		} catch(Exception e) {
			logger.error("Can't create battery measurement via MQTT.");
		}
	}

	public static void createBatteryRESTmeasurement(String deviceId, int numberMeasurements, int sleepTimerInSeconds, RandomValue randomValue) {
		try {
			MeasurementRepresentation measurementRepresentation = createMeasurementRepresentation(deviceId);
			int counter = randomValue.getStartValue();
			for (int i = 0; i < numberMeasurements; i++) {
				if(counter>randomValue.getMaxValue()) {
					counter = randomValue.getStartValue();
				}
				Battery battery = new Battery();
				battery.setLevel(new MeasurementValue(BigDecimal.valueOf(counter), Helper.battery_unit));
				measurementRepresentation.setType(Helper.battery_fragment);
        measurementRepresentation.setDateTime(new DateTime());
				measurementRepresentation.set(battery);
				Cumulocity.measurementApi.create(measurementRepresentation);
				counter += 1;
				Helper.sleep(sleepTimerInSeconds);
			}
		} catch(Exception e) {
			logger.error("Can't create battery measurement via REST.");
		}
	}
	
	// -----------------------------
	// Signal strength (ber)
	// -----------------------------
	public static void createSignalStrengthBerMQTTmeasurement(MqttClient client, int numberMeasurements, int sleepTimerInSeconds, RandomValue randomValue) {
		try {
			for (int i = 0; i < numberMeasurements; i++) {
				StringBuilder value = new StringBuilder();
				value.append(Helper.signalStrength_ber_templateId);
				value.append(",");
				value.append(Helper.signalStrength_ber_fragment);
				value.append(",");
				value.append(Helper.signalStrength_ber_serial);
				value.append(",");
				value.append(MeasurementBuilder.getRandomValue(randomValue));
				value.append(",");
				value.append(Helper.signalStrength_ber_unit);
				MQTTClient.setMQTTmeasurement(client, value.toString(), null);
				Helper.sleep(sleepTimerInSeconds);
			}	
		} catch(Exception e) {
			logger.error("Can't create signal strength (ber) measurement via MQTT.");
		}
	}

	public static void createSignalStrengthBerRESTmeasurement(String deviceId, int numberMeasurements, int sleepTimerInSeconds, RandomValue randomValue) {
		try {
			MeasurementRepresentation measurementRepresentation = createMeasurementRepresentation(deviceId);
			for (int i = 0; i < numberMeasurements; i++) {
				SignalStrength signalStrength = new SignalStrength();
				signalStrength.setBer(new MeasurementValue(BigDecimal.valueOf(getRandomValue(randomValue)), Helper.signalStrength_ber_unit));
				measurementRepresentation.setType(Helper.signalStrength_ber_fragment);
        measurementRepresentation.setDateTime(new DateTime());
				measurementRepresentation.set(signalStrength);
				Cumulocity.measurementApi.create(measurementRepresentation);
				Helper.sleep(sleepTimerInSeconds);
			}
		} catch(Exception e) {
			logger.error("Can't create signal strength (ber) measurement via REST.");
		}
	}
	
	// -----------------------------
	// Signal strength (ssi)
	// -----------------------------
	public static void createSignalStrengthSsiMQTTmeasurement(MqttClient client, int numberMeasurements, int sleepTimerInSeconds, RandomValue randomValue) {
		try {
			for (int i = 0; i < numberMeasurements; i++) {
				StringBuilder value = new StringBuilder();
				value.append(Helper.signalStrength_ssi_templateId);
				value.append(",");
				value.append(Helper.signalStrength_ssi_fragment);
				value.append(",");
				value.append(Helper.signalStrength_ssi_serial);
				value.append(",");
				value.append(MeasurementBuilder.getRandomValue(randomValue));
				value.append(",");
				value.append(Helper.signalStrength_ssi_unit);
				MQTTClient.setMQTTmeasurement(client, value.toString(), null);
				Helper.sleep(sleepTimerInSeconds);
			}	
		} catch(Exception e) {
			logger.error("Can't create signal strength (rssi) measurement via MQTT.");
		}
	}

	public static void createSignalStrengthSsiRESTmeasurement(String deviceId, int numberMeasurements, int sleepTimerInSeconds, RandomValue randomValue) {
		try {
			MeasurementRepresentation measurementRepresentation = createMeasurementRepresentation(deviceId);
			for (int i = 0; i < numberMeasurements; i++) {
				SignalStrength signalStrength = new SignalStrength();
				signalStrength.setRssi(new MeasurementValue(BigDecimal.valueOf(getRandomValue(randomValue)), Helper.signalStrength_ssi_unit));
				measurementRepresentation.setType(Helper.signalStrength_ssi_fragment);
        measurementRepresentation.setDateTime(new DateTime());
				measurementRepresentation.set(signalStrength);
				Cumulocity.measurementApi.create(measurementRepresentation);
				Helper.sleep(sleepTimerInSeconds);
			}
		} catch(Exception e) {
			logger.error("Can't create signal strength (rssi) measurement via REST.");
		}

	}
	
	// -----------------------------
	// Light
	// -----------------------------
	public static void createLightMQTTmeasurement(MqttClient client, int numberMeasurements, int sleepTimerInSeconds, RandomValue randomValue) {
		try {
			int counter = randomValue.getStartValue();
			for (int i = 0; i < numberMeasurements; i++) {
				StringBuilder value = new StringBuilder();
				value.append(Helper.light_templateId);
				value.append(",");
				value.append(Helper.light_fragment);
				value.append(",");
				value.append(Helper.light_serial);
				value.append(",");
				if(counter>randomValue.getMaxValue()) {
					counter = randomValue.getStartValue();
				}
				value.append(counter);
				value.append(",");
				value.append(Helper.light_unit);
				MQTTClient.setMQTTmeasurement(client, value.toString(), null);
				counter += 1;
				Helper.sleep(sleepTimerInSeconds);
			}
		} catch(Exception e) {
			logger.error("Can't create light measurement via MQTT.");
		}
	}
	
	public static void createLightRESTmeasurement(String deviceId, int numberMeasurements, int sleepTimerInSeconds, RandomValue randomValue) {
		try {
			int counter = randomValue.getStartValue();
			MeasurementRepresentation measurementRepresentation = createMeasurementRepresentation(deviceId);
			for (int i = 0; i < numberMeasurements; i++) {
				if(counter>randomValue.getMaxValue()) {
					counter = randomValue.getStartValue();
				}
				LightMeasurement lightMeasurement = new LightMeasurement();
				lightMeasurement.setE(new MeasurementValue(BigDecimal.valueOf(counter), Helper.light_unit));
				measurementRepresentation.setType(Helper.light_fragment);
        measurementRepresentation.setDateTime(new DateTime());
				measurementRepresentation.set(lightMeasurement);
				Cumulocity.measurementApi.create(measurementRepresentation);
				counter += 1;
				Helper.sleep(sleepTimerInSeconds);
			}
		} catch(Exception e) {
			logger.error("Can't create light measurement via REST.");
		}
	}
	
	// -----------------------------
	// Custom
	// -----------------------------
	public static void createCustomMQTTmeasurement(MqttClient client, int numberMeasurements, int sleepTimerInSeconds, RandomValue randomValue) {
		try {
			for (int i = 0; i < numberMeasurements; i++) {
				StringBuilder value = new StringBuilder();
				value.append(Helper.custom_templateId);
				value.append(",");
				value.append(Helper.custom_fragment);
				value.append(",");
				value.append(Helper.custom_serial);
				value.append(",");
				value.append(MeasurementBuilder.getRandomValue(randomValue));
				value.append(",");
				value.append(Helper.custom_unit);
				MQTTClient.setMQTTmeasurement(client, value.toString(), null);
				Helper.sleep(sleepTimerInSeconds);
			}
		} catch(Exception e) {
			logger.error("Can't create custom measurement via MQTT.");
		}
	}

	public static void createCustomRESTmeasurement(String deviceId, int numberMeasurements, int sleepTimerInSeconds, RandomValue randomValue) {
		/*/
		try {
			MeasurementRepresentation measurementRepresentation = createMeasurementRepresentation(deviceId);
			for (int i = 0; i < numberMeasurements; i++) {
				
				HumidityMeasurement humidityMeasurement = new HumidityMeasurement();
				humidityMeasurement.setH(new MeasurementValue(BigDecimal.valueOf(getRandomValue(randomValue)), Cumulocity.prop.getProperty("custom_unit")));
				measurementRepresentation.setType(Cumulocity.prop.getProperty("custom_fragment"));
				measurementRepresentation.set(humidityMeasurement);
				measurementApi.create(measurementRepresentation);
				
				Common.sleep(sleepTimerInSeconds);
			}
		} catch(Exception e) {
			logger.error("Can't create custom measurement via REST.");
		}
		//*/
	}
		
	// -----------------------------
	// Values
	// -----------------------------	
	static int currentValue = 0;
	public static double getRandomValue(RandomValue randomValue) {
		int random = new Random().nextInt(4);
		if(random % 2 == 0) {
			currentValue = randomValue.getStartValue()+new Random().nextInt(randomValue.getVariance());
		} else {
			currentValue = randomValue.getStartValue()-new Random().nextInt(randomValue.getVariance());
		}
		if(currentValue > randomValue.getMaxValue()) {
			currentValue -= random;
		}
		if(currentValue < randomValue.getMinValue()) {
			currentValue += random;
		}
		randomValue.setStartValue(currentValue);
		return currentValue;
	}
	
	static int currentIncreasingValue = 0;
	public static double getRandomIncreasingAndDecreasingValue(RandomValue randomValue) {
		for(int i=randomValue.getMinValue(); i<=randomValue.getMaxValue(); i++) {
			currentIncreasingValue = i;
			if(i>=randomValue.getMaxValue()) {
				for(int j=randomValue.getMaxValue(); j>=randomValue.getMinValue(); j--) {
					currentIncreasingValue = j;
				}
			}
		}
		return currentIncreasingValue;
	}
	
}
