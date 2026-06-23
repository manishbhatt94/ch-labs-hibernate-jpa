package com.mainapp;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

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
		demoNonJpqlInsert(em);
		demoRead(em);
		demoUpdate(em);
		demoRead(em);
		demoDelete(em);
		demoRead(em);
		demoRead_UsingNamedQuery(em);

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

		/**
		 * Note: In JPQL: 1. We query against the entity class and its properties, not
		 * the database table and columns.
		 *
		 * 2. The SELECT clause specifies the entity alias (e.g., 'e') to refer to the
		 * entity in the query.
		 *
		 * 3. In SELECT queries, using the Alias is mandatory.
		 *
		 * 4. Using semicolon to terminate the JPQL query string is optional and
		 * generally not recommended when using JPA APIs, as it may lead to syntax
		 * errors in some JPA implementations. It's best to omit the semicolon at the
		 * end of JPQL query strings when using them with JPA APIs.
		 */
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
		/**
		 * Note: Plain `?` positional parameters are considered deprecated in JPA & give
		 * below error:
		 *
		 * Caused by: org.hibernate.QueryException: Legacy-style query parameters (`?`)
		 * are no longer supported; use JPA-style ordinal parameters (e.g., `?1`)
		 * instead. ---
		 *
		 * And, apart from using Ordinal Parameters (e.g., `?1`, `?2`), we can also use
		 * Named Parameters (e.g., `:paramName`) in JPQL queries. For example:
		 *
		 * @formatter:off
		 * String jpql = "UPDATE Employee e SET e.employeeSalary = e.employeeSalary + :increment";
		 * Query query = em.createQuery(jpql);
		 * query.setParameter("increment", 345);
		 * query.executeUpdate();
		 * @formatter:on
		 *
		 * Note: Plain JDBC's PreparedStatement only supports Plain Positional Parameters (i.e. "?"),
		 * and doesn't support neither Ordinal Parameters nor Named Parameters.
		 */

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

	private static void demoRead_UsingNamedQuery(EntityManager em) {

		final String queryName = "Employee.findByName";

		// @formatter:off
		System.out.println("\n\nReading Employee record(s) from the database\n" +
				"  (Using Named [JPQL] Query: \"" + queryName + "\")...\n");
		// @formatter:on

		Query query = em.createNamedQuery(queryName);

		query.setParameter("name", "Method Man");

		List results = query.getResultList();
		System.out.println("Number of Employee records retrieved: " + results.size() + "\n");
		System.out.println("Employee records retrieved from the database:");

		for (Object obj : results) {
			Employee emp = (Employee) obj;
			System.out.println(emp);
		}

		System.out.println("\nRead operation completed successfully!");

	}

}
