package com.mainapp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import com.entity.Employee;

public class Launch_JavaConfig_XmlMapping {

	public static void main(String[] args) {

		System.out.println("############### DEMO: [Java Config + XML Mapping] ##################\n");

		final String propertiesFileResourcePath = "my-custom-configs/my-database-info.properties";

		ClassLoader classLoader = Launch_JavaConfig_XmlMapping.class.getClassLoader();
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

		// Add mapping XML file(s):
		configuration.addResource("my-custom-configs/Employee.hbm.xml");

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

		System.out.println("\n\nInserting an Employee entity into the database...\n");

		Employee employee = new Employee(5555, "Ra's Al-Ghul", "Nanda Parbat, The Himalayas", 78630);

		Transaction transaction = session.getTransaction();
		transaction.begin();

		session.save(employee);

		transaction.commit();
		System.out.println("\nInsert operation completed successfully!");

	}

	private static void demoRead(Session session) {

		System.out.println("\n\nReading an Employee entity from the database...\n");

		Employee employee = session.get(Employee.class, 5555);

		System.out.println("\nEmployee found: " + employee + "\n");

	}

}
