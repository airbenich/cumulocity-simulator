package com.c8y.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Common {
	
	static Logger logger = LoggerFactory.getLogger(Common.class);

	public static Properties getPropertyFile() {
		Properties prop = new Properties();
		String filePath = null;
		try {
			filePath = System.getProperty("user.home")+File.separator+"simulator.properties";
			prop.load(new FileInputStream(filePath));
			return prop;
		} 
		catch (IOException ex) {
			logger.error("Can't read the property file. Please copy your property file into the folowing folder: \n"+filePath);
			return null;
		}
	}
	
	public static void sleep(int sleepTimerInSeconds) {
		try {
			TimeUnit.SECONDS.sleep(sleepTimerInSeconds);
		} catch (InterruptedException e) {
			logger.debug("Sleep timer does not work.");
		}
	}
	
	/*/
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
	//*/
	
	/*/
	// base64 encoding
	public static String getBAse64String(ByteArrayOutputStream bos) {				
		String encodedfile = null;
		try {
			encodedfile = new String(Base64.encodeBase64(bos.toByteArray()), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}	
		return encodedfile;
	}
	//*/
	
	/*/
	public static boolean doesTemplateExists(Platform platform, String externalId) {
		
		try {
			IdentityApi identityApi = platform.getIdentityApi();
			
			ID id = new ID();
			id.setType("c8y_SmartRest2DeviceIdentifier");
			id.setValue(externalId);
			
			ExternalIDRepresentation externalIDRepresentation = identityApi.getExternalId(id);
			
			return true;
			
		} catch(Exception e) {
			return false;
		}

	}
	//*/
}
