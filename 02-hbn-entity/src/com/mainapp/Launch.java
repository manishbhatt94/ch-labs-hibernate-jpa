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

		System.out.println("Inserting an Employee entity into the database...");
		EntityTransaction transaction = em.getTransaction();
		transaction.begin();

		Employee employee = new Employee(1, "John Doe", "123 Main St", 50000);
		em.persist(employee);

		transaction.commit();
		System.out.println("Insert operation completed successfully!");

		// Close the EntityManager and EntityManagerFactory to release resources:
		em.close();
		emf.close();

	}

}
