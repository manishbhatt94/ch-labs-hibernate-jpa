package com.mainapp;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConfigLoader {

	private String configFileLocation;

	private Properties configAsProperties;

	private Map<String, String> configAsMap;

	public ConfigLoader(String configFileLocation) {
		this.configFileLocation = configFileLocation;
	}

	public Map<String, String> loadConfigAsMap() throws IOException {

		if (configAsMap != null) {
			return configAsMap;
		}

		configAsProperties = loadConfigAsProperties();
		printProperties(configAsProperties);
		configAsMap = convertPropertiesToMap(configAsProperties);

		return configAsMap;
	}

	private void printProperties(Properties props) {

		System.out.println("\nLoaded properties:");
		for (String key : props.stringPropertyNames()) {
			String value = props.getProperty(key);
			System.out.println(key + " = " + value);
		}
		System.out.println();

	}

	private Map<String, String> convertPropertiesToMap(Properties props) {

		Map<String, String> map = new HashMap<>();
		for (String key : props.stringPropertyNames()) {
			map.put(key, props.getProperty(key));
		}
		return map;

	}

	private Properties loadConfigAsProperties() throws IOException {

		// Step A: Get the ClassLoader of the current class
		ClassLoader classLoader = Launch.class.getClassLoader();

		InputStream inputStream = null;
		// Step B: Ask it to find the file on the classpath and return a stream
		inputStream = classLoader.getResourceAsStream(configFileLocation);

		// Step C: Check it was actually found
		if (inputStream == null) {
			throw new RuntimeException("File not found on classpath!");
		}

		// Step D: Load into a Properties object
		Properties props = new Properties();
		props.load(inputStream);

		try {
			inputStream.close();
		} catch (IOException e) {
			System.out.println("Error closing config file's input stream: " + e.getMessage());
			e.printStackTrace();
		}

		return props;
	}

}
