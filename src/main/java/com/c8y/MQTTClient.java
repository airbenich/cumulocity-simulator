package com.c8y;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MQTTClient {
	
	static Logger logger = LoggerFactory.getLogger(MQTTClient.class);
	
	private MQTTClient() {}
	
	public static void setMQTTmeasurement(MqttClient client, String value, String templateId) {
		try {
		  MqttMessage message = new MqttMessage(value.getBytes());
			message.setQos(Integer.valueOf(Helper.qos));
			if(client!=null) {
				if(templateId == null) {
					client.publish(Helper.staticTopic, message);
				} else {
					client.publish(Helper.customTopic+templateId, message);
				}
			} else {
				logger.error("Client does not exist.");
			}
			
			// mqttClient.disconnect();
			// System.out.println("Disconnected");
			// System.exit(0);
		} catch (MqttException e) {
			logger.error("There is a problem with the client. ", e);
		}
	}
	
	public static MqttClient createNewMqttClient(String externalId) {
		MqttClient client = null;
		try {
			MemoryPersistence persistence = new MemoryPersistence();
			client = new MqttClient(Helper.mqttServer, externalId, persistence);
			MqttConnectOptions connOpts = getClientConnectOptions();
			client.connect(connOpts);
			return client;
		} catch (MqttException e) {
			logger.error("Can't create an MQTT client resp. a connection. Check your mqtt server address and the external id of your device. ", e);
			return null;
		}
	}
	
	public static MqttConnectOptions getClientConnectOptions() {
		try {
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(Boolean.valueOf(Helper.cleanSession));
			connOpts.setUserName(getMQTTusername());
			connOpts.setPassword(Helper.password.toCharArray());
			return connOpts;
		} catch(Exception e) {
			logger.error("Can't create an MQTT connection. Check your mqtt username. ", e);
			return null;
		}
	}
	
	public static String getMQTTusername() {

	  StringBuilder stringBuilder = new StringBuilder();
	  stringBuilder.append(Helper.tenantId);
	  stringBuilder.append("/");
	  stringBuilder.append(Helper.username);
	  
	  return stringBuilder.toString();
	}
    
}
