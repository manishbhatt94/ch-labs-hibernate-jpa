package com.mainapp;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.entity.Employee;

public class Launch {

	public static void main(String[] args) {

		Configuration configuration = new Configuration();
		configuration.configure();

		SessionFactory sessionFactory = configuration.buildSessionFactory();
		Session session = sessionFactory.openSession();

		System.out.println("Hibernate Session object created: " + session);
		System.out.println("Connection to the database established successfully!");

//		demoInsert(session);
		demoRead(session);
//		demoUpdate(session);
		demoDelete(session);

		session.close();
		sessionFactory.close();
		System.out.println("\n\nSession and SessionFactory closed successfully. Resources released.");

	}

	private static void demoInsert(Session session) {

		System.out.println("\n\nInserting an Employee entity into the database...");

		Employee employee = new Employee(1, "Eric Wright", "122nd Ave., Compton, CA", 8920);
//		Employee employee = new Employee(2, "Nasir Jones", "21st Main, Queens Bridge, NY", 11265);
//		Employee employee = new Employee(3, "Method Man", "Park Hill Projects, Staten Island, NY", 9280);
//		Employee employee = new Employee(4, "Ol' Dirty Bastard", "Brooklyn, NY", 10550);
//		Employee employee = new Employee(5, "Slim Shady", "8 Mile Road, Detroit, MI", 9870);

		Transaction transaction = session.getTransaction();
		transaction.begin();

		session.save(employee);

		transaction.commit();
		System.out.println("\nInsert operation completed successfully!");

	}

	private static void demoRead(Session session) {

		System.out.println("\n\nReading an Employee entity from the database...");

		Employee employee = session.get(Employee.class, 1);

		System.out.println("\nEmployee found: " + employee);

	}

	private static void demoUpdate(Session session) {

		System.out.println("\n\nUpdating an Employee entity in the database...");

		Employee employee = session.get(Employee.class, 1);

		if (employee != null) {
			employee.setEmployeeSalary(10580);

			Transaction transaction = session.getTransaction();
			transaction.begin();

			session.update(employee);

			transaction.commit();
			System.out.println("\nUpdate operation completed successfully!");
		} else {
			System.out.println("\nEmployee with ID 1 not found for update.");
		}

	}

	private static void demoDelete(Session session) {

		System.out.println("\n\nDeleting an Employee entity from the database...");

		Employee employee = session.get(Employee.class, 1);

		if (employee != null) {
			Transaction transaction = session.getTransaction();
			transaction.begin();

			session.delete(employee);

			transaction.commit();
			System.out.println("\nDelete operation completed successfully!");
		} else {
			System.out.println("\nEmployee with ID 1 not found for deletion.");
		}

	}

}
