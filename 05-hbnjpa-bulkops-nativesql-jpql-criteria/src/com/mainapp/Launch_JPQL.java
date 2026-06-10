package com.mainapp;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.entity.Employee;

public class Launch_JPQL {

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
//		demoRead(em);
		demoDelete(em);
//		demoRead(em);

		// Close the EntityManager and EntityManagerFactory to release resources:
		em.close();
		emf.close();
		System.out.println("\n\nEntityManager and EntityManagerFactory closed successfully. Resources released.");

	}

	/* JPQL Does NOT support INSERT Operation */
	private static void demoInsert(EntityManager em) {

		throw new UnsupportedOperationException("JPQL does NOT support INSERT operation. "
				+ "Use native SQL queries instead for bulk insert operations; "
				+ "or EntityManager#persist() for single-row insert.");

	}

	private static void demoRead(EntityManager em) {

		System.out.println("\n\nReading (multiple) Employee records from the database...\n");

		// @formatter:off
//		String sql = "SELECT employee_id, employee_name, employee_address, employee_salary \n" +
//				"    FROM hbn_employee;";
		// or, using SELECT *:
		String sql = "SELECT * FROM hbn_employee;";
		// @formatter:on

		Query nativeQuery1 = em.createNativeQuery(sql);
		System.out.println("Using [Query createNativeQuery(String sqlString)] method \n"
				+ "to create a native SQL query without specifying the result mapping. \n"
				+ "The query results will be returned as an untyped List of Object arrays (List<Object[]>).");

		// Execute a SELECT query and return the query results as an untyped List
		List<Object[]> results1 = nativeQuery1.getResultList();
		System.out.println("Number of Employee records retrieved: " + results1.size() + "\n");
		System.out.println("Raw query results (untyped List): " + results1 + "\n");
		System.out.println("Employee records retrieved from the database:");

		for (Object[] objectArray : results1) {
			for (Object obj : objectArray) {
				System.out.print(obj + " | ");
			}
			System.out.println();
		}

		Query nativeQuery2 = em.createNativeQuery(sql, Employee.class);
		System.out.println("\nUsing [Query createNativeQuery(String sqlString, Class resultClass)] \n"
				+ "method to create a native SQL query with result mapping to the \n"
				+ "Employee entity class. The query results will be returned as an \n"
				+ "untyped List whose entries can be cast to Employee entity objects.");

		// Execute a SELECT query and return the query results as an untyped List
		List<Employee> result2 = nativeQuery2.getResultList();
		System.out.println("Number of Employee records retrieved: " + result2.size() + "\n");
		System.out.println("Raw query results (untyped List): " + result2 + "\n");
		System.out.println("Employee records retrieved from the database:");

		for (Object obj : result2) {
			Employee emp = (Employee) obj;
			System.out.println(emp);
		}

		System.out.println("\nRead operation completed successfully!");

	}

	private static void demoUpdate(EntityManager em) {

		System.out.println("\n\nUpdating Employee records in the database...\n");
		// @formatter:off
		String sql = "UPDATE hbn_employee \n"
				+ "    SET employee_salary = employee_salary + 345 \n"
				+ "    WHERE employee_salary < 11000;";
		// @formatter:on

		EntityTransaction transaction = em.getTransaction();
		transaction.begin();

		Query nativeQuery = em.createNativeQuery(sql);
		int rowsAffected = nativeQuery.executeUpdate();

		transaction.commit();
		System.out.println("Number of Employee records updated: " + rowsAffected);

		System.out.println("\nUpdate operation completed successfully!");

	}

	private static void demoDelete(EntityManager em) {

		System.out.println("\n\nDeleting an Employee entity from the database...\n");
		// @formatter:off
		String sql = "DELETE FROM hbn_employee \n"
				+ "    WHERE employee_id > 10;";
		// @formatter:on

		EntityTransaction transaction = em.getTransaction();
		transaction.begin();
		Query nativeQuery = em.createNativeQuery(sql);
		int rowsAffected = nativeQuery.executeUpdate();

		transaction.commit();
		System.out.println("Number of Employee records deleted: " + rowsAffected);

		System.out.println("\nDelete operation completed successfully!");

	}

	private static void demoNonJpqlInsert(EntityManager em) {

		System.out.println(
				"\n\nInserting multiple Employee entities using EntityManager#persist() method in a loop...\n");

		EntityTransaction transaction = em.getTransaction();
		transaction.begin();

		int baseId = 500000;
		int numRecordsToInsert = 15;
		int batchSize = 6; // Adjust batch size as needed
		int inBatchProcessedCount = 0;

		for (int i = 1; i <= numRecordsToInsert; i++) {
			Employee emp = new Employee(baseId + i, "Employee " + i, "Address " + i, 40000 + i);
			em.persist(emp);
			inBatchProcessedCount++;

			// Flush and clear the persistence context after processing a batch of records
			if (inBatchProcessedCount % batchSize == 0) {
				em.flush();
				em.clear();
				System.out.println("Inserted " + inBatchProcessedCount + " Employee records so far...");
			}
		}

		if (inBatchProcessedCount % batchSize != 0) {
			em.flush();
			em.clear();
			System.out.println("Inserted total of " + inBatchProcessedCount + " Employee records.");
		}

		System.out.println("Committing the transaction to persist the Employee entities to the database...");
		transaction.commit();
		System.out.println("Employee entities inserted successfully using EntityManager#persist() method in loop.");

		System.out.println("\nInsert operation completed successfully!");

	}

}
