package com.mainapp;

import java.math.BigDecimal;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.entity.Product;

public class Launch_XmlConfig_JavaMapping {

	public static void main(String[] args) {

		System.out.println("############### DEMO: [XML Config + Java Mapping] ##################");

		Configuration configuration = new Configuration();

		/**
		 * Using the below overload of Configuration#configure method public
		 * {@code Configuration configure(String resource) throws HibernateException}
		 *
		 * When placing Hibernate Configuration XML in custom file name, other than the
		 * standard "hibernate.cfg.xml", in which case we will call the zero arguments
		 * Configuration#configure method.
		 */
		configuration.configure("my-custom-configs/my-hibernate-config-2.xml");

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
