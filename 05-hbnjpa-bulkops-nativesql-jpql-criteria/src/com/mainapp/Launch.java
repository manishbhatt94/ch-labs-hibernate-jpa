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


		EntityTransaction transaction = em.getTransaction();
		transaction.begin();



		transaction.commit();
		System.out.println("Insert operation completed successfully!");

	}

	private static void demoRead(EntityManager em) {

		System.out.println("\n\nReading an Employee entity from the database...");

		System.out.println("Employee found: " + null);

	}

	private static void demoUpdate(EntityManager em) {

		System.out.println("\n\nUpdating an Employee entity in the database...");

		EntityTransaction transaction = em.getTransaction();
		transaction.begin();

		transaction.commit();

		System.out.println("Update operation completed successfully!");

	}

	private static void demoDelete(EntityManager em) {

		System.out.println("\n\nDeleting an Employee entity from the database...");

		EntityTransaction transaction = em.getTransaction();
		transaction.begin();

		transaction.commit();

		System.out.println("Delete operation completed successfully!");

	}

}
