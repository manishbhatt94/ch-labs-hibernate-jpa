package com.mainapp;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.entity.Address;
import com.entity.Employee;

public class Launch {

	public static void main(String[] args) {

		Configuration configuration = new Configuration();
		configuration.configure();

		SessionFactory sessionFactory = configuration.buildSessionFactory();
		Session session = sessionFactory.openSession();

		System.out.println("Hibernate Session object created: " + session);
		System.out.println("Connection to the database established successfully!");

//		insert(session);
		readFromEmployee(session);
		readFromAccount(session);

		session.close();
		sessionFactory.close();
		System.out.println("\n\nSession and SessionFactory closed successfully. Resources released.");

	}

	private static void readFromAccount(Session session) {

		System.out.println("\n\nReading address: addressId: 201");
		Address address = session.get(Address.class, 201);
		System.out.println("\nFound address: " + address);

	}

	private static void readFromEmployee(Session session) {

		System.out.println("\n\nReading employee: employeeId: 1");
		Employee employee = session.get(Employee.class, 1);
		System.out.println("\nFound employee: " + employee);

	}

	private static void insert(Session session) {

		Transaction transaction = session.getTransaction();
		transaction.begin();

		Address address1 = new Address(201, "D-59/7 First Floor", "1st Main 22nd Cross", "Kuvempu Nagara 4th Phase",
				"Bengaluru", "Karnataka");
		Employee employee1 = new Employee(1, "Raju Kumar", "Senior IT Admin", address1);

		session.save(employee1);

		transaction.commit();

	}

}
