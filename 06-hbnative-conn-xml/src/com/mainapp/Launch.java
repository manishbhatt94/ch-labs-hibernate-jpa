package com.mainapp;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Launch {

	public static void main(String[] args) {

		// Hibernate Native API provides class org.hibernate.cfg.Configuration
		// that deals with Bootstrapping Hibernate and provides the
		// Configuration#configure() method to load configuration
		Configuration configuration = new Configuration();

		// Configuration#configure() method loads the Hibernate configuration
		// present in hibernate.cfg.xml file:
		// (In JPA, we used to write the below line for reading configuration:
		// @formatter:off
		// EntityManagerFactory emf = Persistence.createEntityManagerFactory("my-persistence-unit-1");
		// @formatter:on
		configuration.configure(); // Use the mappings and properties specified in an application resource named
									// hibernate.cfg.xml.

		// org.hibernate.SessionFactory interface type object is the heavy-weight
		// object responsible for doing all setup work (e.g. creating connection
		// pool, maintaining mappings, etc.) - which is similar to JPA equivalent
		// javax.persistence.EntityManagerFactory interface object.
		SessionFactory sessionFactory = configuration.buildSessionFactory();

		// After all setup work is finished, we use the org.hibernate.Session
		// interface type object, created from SessionFactory, and this
		// Session interface is used to perform all interactions with the DB.
		// This is similar to how we get a javax.persistence.EntityManager
		// interface object from EntityManagerFactory, in JPA, to perform the
		// DB interactions.
		Session session = sessionFactory.openSession();

		// If the above call to SessionFactory#openSession() method executes
		// without failure and we receive a Session object, then that implies
		// Hibernates has successfully established connection to the DB.

		System.out.println("Hibernate Session object created: " + session);
		// ↑ Prints something like:
		// Hibernate Session object created: SessionImpl(1850874910<open>)

		System.out.println("Connection to the database established successfully!");

		// Close the Session and SessionFactory to release resources:
		session.close();
		sessionFactory.close();

		System.out.println("\n\nSession and SessionFactory closed successfully. Resources released.");

	}

}
