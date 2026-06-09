package com.mainapp;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import com.entity.Employee;

public class Launch {

	public static void main(String[] args) {

		// Create EntityManagerFactory using the persistence unit name defined in
		// persistence.xml configuration file:
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("my-persistence-unit-1");

		// Create an EntityManager from the factory, which will be used to interact with
		// the database:
		EntityManager em = emf.createEntityManager();

		System.out.println("EntityManager created successfully: " + em);
		// Prints message similar to:
		// EntityManager created successfully: SessionImpl(1950800085<open>)

		System.out.println("Connection to the database established successfully!");

		demoInsert(em);
		demoRead(em);
		demoUpdate(em);
		demoRead(em);
		demoDelete(em);
		demoRead(em);

		// Close the EntityManager and EntityManagerFactory to release resources:
		em.close();
		emf.close();
		System.out.println("\n\nEntityManager and EntityManagerFactory closed successfully. Resources released.");

	}

	private static void demoInsert(EntityManager em) {

		System.out.println("\n\nInserting an Employee entity into the database...");

		// (NOTE: Copied from Claude's explanation)
		// Unlike JDBC (where auto-commit is ON by default), JPA and Hibernate
		// require explicit transaction boundaries. Write operations (persist,
		// merge, remove) will throw an exception if called outside a transaction.
		//
		// Actually, on running the INSERT code (i.e. call to persist()) without
		// explicitly transaction.begin() and transaction.commit(), I noticed
		// that the record gets inserted but only for the current connection/session.
		// Once the connection is closed, the inserted record is not visible in the
		// database. This behavior is because the changes are not committed to the
		// database without an explicit transaction commit. So, while it may seem like
		// the record is inserted, it is not actually persisted in the database until a
		// transaction is committed.
		// But, NO EXCEPTION WAS THROWN!
		//
		// Read operations (SELECT) are technically permitted outside a
		// transaction, but Hibernate's own guidance is to always demarcate
		// explicit transactions — even for reads.
		//
		// In a standalone (non-managed) environment like this project, that
		// means calling begin() and commit() manually in application code.
		// In a managed environment (JavaEE server / Spring), the container
		// or framework handles this for you declaratively.

		EntityTransaction transaction = em.getTransaction();
		transaction.begin();

//		Employee employee = new Employee(1, "Eric Wright", "122nd Ave., Compton, CA", 8920);
//		Employee employee = new Employee(2, "Nasir Jones", "21st Main, Queens Bridge, NY", 11200);
//		Employee employee = new Employee(3, "Method Man", "Park Hill Projects, Staten Island, NY", 9210);
//		Employee employee = new Employee(4, "Ol' Dirty Bastard", "Brooklyn, NY", 10500);
		Employee employee = new Employee(5, "Slim Shady", "8 Mile Road, Detroit, MI", 9800);

		em.persist(employee);

		transaction.commit();
		System.out.println("Insert operation completed successfully!");

	}

	private static void demoRead(EntityManager em) {

		System.out.println("\n\nReading an Employee entity from the database...");

		Employee employee = em.find(Employee.class, 2);

		System.out.println("Employee found: " + employee);

	}

	private static void demoUpdate(EntityManager em) {

		System.out.println("\n\nUpdating an Employee entity in the database...");

		Employee employee = em.find(Employee.class, 2);

		if (employee != null) {
			employee.setEmployeeSalary(15700);

			EntityTransaction transaction = em.getTransaction();
			transaction.begin();

			em.merge(employee);

			transaction.commit();
			System.out.println("Update operation completed successfully!");
		} else {
			System.out.println("Employee with ID 2 not found for update.");
		}

	}

	private static void demoDelete(EntityManager em) {

		System.out.println("\n\nDeleting an Employee entity from the database...");

		Employee employee = em.find(Employee.class, 2);

		if (employee != null) {
			EntityTransaction transaction = em.getTransaction();
			transaction.begin();

			em.remove(employee);

			transaction.commit();
			System.out.println("Delete operation completed successfully!");
		} else {
			System.out.println("Employee with ID 2 not found for deletion.");
		}

	}

}
