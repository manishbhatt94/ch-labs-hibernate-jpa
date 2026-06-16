package com.mainapp;

import java.util.Properties;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

public class Launch {

	public static void main(String[] args) {

		Properties properties = new Properties();

		// Put all connection related (& other Hibernate Configuration) details
		// in the Properties object, using key-values strings from Hibernate
		// class org.hibernate.cfg.Environment as follows:

		// (Note: We could (should) have, instead, kept these hardcoded details in
		// a .properties file, and read that file as a resource from Java into
		// an InputStream, using below commented code; and use that InputStream to load
		// up a Properties object)

		// Let's say the .properties file is at location:
		// `src/my-hibernate-cfg.properties`
//		InputStream is = Launch.class.getClassLoader().getResourceAsStream("my-hibernate-cfg.properties");
//		properties.load(is);

		// Environment.DRIVER stores the key "hibernate.connection.driver_class":
		properties.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");

		// Environment.URL stores the key "hibernate.connection.url":
		properties.put(Environment.URL, "jdbc:mysql://localhost:3306/ch_labs_hibernate_01");

		// Environment.USER stores the key "hibernate.connection.username":
		properties.put(Environment.USER, "root");

		// Environment.PASS stores the key "hibernate.connection.password":
		properties.put(Environment.PASS, "manish");

		// .. other configuration keys also exist in Environment class such as
		// Environment.SHOW_SQL, Environment.FORMAT_SQL, Environment.HBM2DDL_AUTO
		// but only the above four we used, are necessary for a connection.

		// Then, we create a org.hibernate.cfg.Configuration object, same as we
		// did in XML approach, but instead of using Configuration#configure()
		// method which loads info from `hibernate.cfg.xml` file, we set the
		// Properties object we just populated, on the Configuration object:
		Configuration configuration = new Configuration();
		configuration.setProperties(properties);

		// Now, the further steps are exactly the same as with XML approach,
		// i.e., get a org.hibernate.SessionFactory object first (from the
		// org.hibernate.cfg.Configuration object we just created), and using
		// that, we get a org.hibernate.Session object. This Session object
		// we thereafter be used for any interacts with the RDBMS.

		SessionFactory sessionFactory = configuration.buildSessionFactory();

		Session session = sessionFactory.openSession();

		System.out.println("Hibernate Session object created: " + session);
		// ↑ Prints similar to:
		// Hibernate Session object created: SessionImpl(1279740095<open>)

		System.out.println("Connection to the database established successfully!");

		// Close the Session and SessionFactory to release resources:
		session.close();
		sessionFactory.close();

		System.out.println("\n\nSession and SessionFactory closed successfully. Resources released.");

	}

}
