package com.mainapp;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.entity.Employee;

public class Launch_CriteriaAPI {

	public static void main(String[] args) {

		EntityManagerFactory emf = Persistence.createEntityManagerFactory("my-persistence-unit-1");

		EntityManager em = emf.createEntityManager();

		System.out.println("EntityManager created successfully: " + em);

		System.out.println("Connection to the database established successfully!");

		demoInsert(em);
		demoNonCriteriaInsert(em);
		demoRead(em);
		demoUpdate(em);
		demoRead(em);
		demoDelete(em);
		demoRead(em);

		em.close();
		emf.close();
		System.out.println("\n\nEntityManager and EntityManagerFactory closed successfully. Resources released.");

	}

	/* Criteria API Does NOT support INSERT Operation */
	private static void demoInsert(EntityManager em) {

		throw new UnsupportedOperationException("Criteria API does NOT support INSERT operation. "
				+ "Use native SQL queries instead for bulk insert operations; "
				+ "or EntityManager#persist() for single-row insert.");

	}

	private static void demoRead(EntityManager em) {

		System.out.println("\n\nReading (multiple) Employee records from the database...\n");

		// @formatter:off
		String jpql = "SELECT e FROM Employee e"; // Notice: No ending semicolon, Alias 'e' is used.
		// @formatter:on

		Query query1 = em.createQuery(jpql);
		System.out.println("JPQL query created successfully: " + query1 + "\n"
				+ "Untyped Query object created using EntityManager's method: \n"
				+ "    public Query createQuery(String qlString);" + "\n");

		// Execute a SELECT query and return the query results as an untyped List
		List results1 = query1.getResultList();
		System.out.println("Number of Employee records retrieved: " + results1.size() + "\n");
		// System.out.println("Raw query results (untyped List): " + results1 + "\n");
		System.out.println("Employee records retrieved from the database:");

		for (Object obj : results1) {
			Employee emp = (Employee) obj;
			System.out.println(emp);
		}
		System.out.println("\n");

		TypedQuery<Employee> query2 = em.createQuery(jpql, Employee.class);

		System.out.println("JPQL query created successfully: " + query2 + "\n"
				+ "Typed Query object created using EntityManager's method: \n"
				+ "    public <T> TypedQuery<T> createQuery(String qlString, Class<T> resultClass);" + "\n");

		// Execute a SELECT query and return the query results as a typed List.
		List<Employee> result2 = query2.getResultList();
		System.out.println("Number of Employee records retrieved: " + result2.size() + "\n");
		// System.out.println("Query results (typed List): " + result2 + "\n");
		System.out.println("Employee records retrieved from the database:");

		for (Employee emp : result2) {
			System.out.println(emp);
		}

		System.out.println("\nRead operation completed successfully!");

	}

	private static void demoUpdate(EntityManager em) {

		System.out.println("\n\nUpdating Employee records in the database...\n");
		// @formatter:off
		String jpql = "UPDATE Employee e \n"
				+ "    SET e.employeeSalary = e.employeeSalary + ?1 \n"
				+ "    WHERE e.employeeSalary < ?2";
		// @formatter:on

		EntityTransaction transaction = em.getTransaction();
		transaction.begin();

		Query query = em.createQuery(jpql);

		query.setParameter(1, 99);
		query.setParameter(2, 11000);

		int rowsAffected = query.executeUpdate();

		transaction.commit();
		System.out.println("Number of Employee records updated: " + rowsAffected);

		System.out.println("\nUpdate operation completed successfully!");

	}

	private static void demoDelete(EntityManager em) {

		System.out.println("\n\nDeleting an Employee entity from the database...\n");
		// @formatter:off
		String jpql = "DELETE FROM Employee e \n"
				+ "    WHERE e.employeeId > :empId";
		// @formatter:on

		EntityTransaction transaction = em.getTransaction();
		transaction.begin();

		Query query = em.createQuery(jpql);

		query.setParameter("empId", 10);

		int rowsAffected = query.executeUpdate();

		transaction.commit();
		System.out.println("Number of Employee records deleted: " + rowsAffected);

		System.out.println("\nDelete operation completed successfully!");

	}

	private static void demoNonCriteriaInsert(EntityManager em) {

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
