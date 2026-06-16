package com.mainapp;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Properties;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import com.entity.Product;

public class Launch_JavaConfig_JavaMapping {

	public static void main(String[] args) {

		System.out.println("############### DEMO: [Java Config + XML Mapping] ##################\n");

		final String propertiesFileResourcePath = "my-custom-configs/my-database-info.properties";

		ClassLoader classLoader = Launch_JavaConfig_JavaMapping.class.getClassLoader();
		InputStream is = classLoader.getResourceAsStream(propertiesFileResourcePath);
		// This .properties file contains DRIVER, URL, USER, PASS - these 4
		// (four) connection related config values.

		if (is == null) {
			System.out.println(
					"Received null InputStream from resource [" + propertiesFileResourcePath + "].\n" + "Exiting!");
			System.exit(0);
		}

		Properties properties = new Properties();

		try {
			properties.load(is);
		} catch (IOException e) {
			System.out.println("Failed to load properties from file. Exception trace:");
			e.printStackTrace();
			System.out.println("Exiting!");
			System.exit(0);
		}

		// Add more config properties to this Properties object in addition to
		// four properties already present in it:
		properties.put(Environment.DIALECT, "org.hibernate.dialect.MySQL8Dialect");
		properties.put(Environment.HBM2DDL_AUTO, "update");
		properties.put(Environment.SHOW_SQL, "true");
		properties.put(Environment.FORMAT_SQL, "true");

		Configuration configuration = new Configuration();
		configuration.setProperties(properties);

		// Add mapping (annotated) classes:
		configuration.addAnnotatedClass(Product.class);

		SessionFactory sessionFactory = configuration.buildSessionFactory();

		Session session = sessionFactory.openSession();

		System.out.println("\nHibernate Session object created: " + session);
		// ↑ Prints something like:
		// Hibernate Session object created: SessionImpl(1850874910<open>)

		System.out.println("Connection to the database established successfully!");

		demoInsert(session);
		demoRead(session);

		// Close the Session and SessionFactory to release resources:
		session.close();
		sessionFactory.close();
		System.out.println("\n\nSession and SessionFactory closed successfully. Resources released.");

	}

	private static void demoInsert(Session session) {

		System.out.println("\n\nInserting a Product entity into the database...\n");

		Product product = new Product(5555, "Boat Airdopes Joy Bluetooth Earbuds", BigDecimal.valueOf(899.99),
				"Headphones In-Ear",
				"Boat Airdopes Joy, 35Hrs Battery, Fast Charge, IWP Tech, Low Latency, 2Mic ENx, Type-C Port, v5.3 Bluetooth Earbuds, TWS Ear Buds Wireless Earphones with mic (Jet Black)");

		Transaction transaction = session.getTransaction();
		transaction.begin();

		session.save(product);

		transaction.commit();
		System.out.println("\nInsert operation completed successfully!");

	}

	private static void demoRead(Session session) {

		System.out.println("\n\nReading a Product entity from the database...\n");

		Product product = session.get(Product.class, 5555);

		System.out.println("\nProduct found: " + product + "\n");

	}

}
