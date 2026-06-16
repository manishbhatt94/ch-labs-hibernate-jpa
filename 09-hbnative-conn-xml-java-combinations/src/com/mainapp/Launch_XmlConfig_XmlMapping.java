package com.mainapp;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.entity.Employee;

public class Launch_XmlConfig_XmlMapping {

	public static void main(String[] args) {

		System.out.println("############### DEMO: [XML Config + XML Mapping] ##################\n");

		Configuration configuration = new Configuration();

		/**
		 * Using the below overload of Configuration#configure method public
		 * {@code Configuration configure(String resource) throws HibernateException}
		 *
		 * When placing Hibernate Configuration XML in custom file name, other than the
		 * standard "hibernate.cfg.xml", in which case we will call the zero arguments
		 * Configuration#configure method.
		 */
		configuration.configure("my-custom-configs/my-hibernate-config-1.xml");

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
