package com.mainapp;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.data.EmployeeData;
import com.entity.Employee;

public class Launch_CriteriaAPI {

	public static void main(String[] args) {

		EntityManagerFactory emf = Persistence.createEntityManagerFactory("my-persistence-unit-1");

		EntityManager em = emf.createEntityManager();

		System.out.println("EntityManager created successfully: " + em);

		System.out.println("Connection to the database established successfully!");

//		demoInsert(em);
		demoNonCriteriaInsert(em);
//		demoRead(em);
//		demoUpdate(em);
//		demoRead(em);
//		demoDelete(em);
//		demoRead(em);

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

		// Return an instance of CriteriaBuilder for the creation of CriteriaQuery
		// objects:
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		// Create a CriteriaQuery object (or more loosely, a "Read Criteria") with the
		// specified result type:
		// (Note: The result type is the type of the objects that will be returned when
		// the query is executed.)
		// (CriteriaQuery interface is used for defining SELECT queries in a type-safe
		// manner using the Criteria API.)
		// (For UPDATE and DELETE operations, there are separate interfaces:
		// CriteriaUpdate and CriteriaDelete, respectively.)
		CriteriaQuery<Employee> cq = criteriaBuilder.createQuery(Employee.class);
		// ↑⇑⇈ This is how you create a CriteriaQuery object using the
		// CriteriaBuilder.

		// Let's say we want to select all Employee records from the database, i.e.,
		// the equivalent of "SELECT e FROM Employee e" in JPQL.
		// (or "SELECT * FROM hbn_employee" in SQL.)

		// This defines the FROM clause of the query, specifying the entity to query:
		Root<Employee> from = cq.from(Employee.class); // ← `from Employee e`

		// This defines the SELECT clause of the query, specifying what to select:
		cq.select(from); // ← `SELECT e`

		// Create a TypedQuery object from the CriteriaQuery object:
		TypedQuery<Employee> query = em.createQuery(cq);

		// Execute a SELECT query and return the query results as a typed List:
		List<Employee> results = query.getResultList();

		System.out.println("Number of Employee records retrieved: " + results.size() + "\n");
		// System.out.println("Raw query results (untyped List): " + results1 + "\n");
		System.out.println("Employee records retrieved from the database:");

		for (Employee emp : results) {
			System.out.println(emp);
		}
		System.out.println("\n");

		System.out.println("\nRead operation completed successfully!");

	}

		// This defines the FROM clause of the query, specifying the entity to query:
		Root<Employee> from = cq.from(Employee.class); // ← `from Employee e`

		// This defines the SELECT clause of the query, specifying what to select:
		cq.select(from); // ← `SELECT e`

		// Create a TypedQuery object from the CriteriaQuery object:
		TypedQuery<Employee> query = em.createQuery(cq);

		// Execute a SELECT query and return the query results as a typed List:
		List<Employee> results = query.getResultList();

		System.out.println("Number of Employee records retrieved: " + results.size() + "\n");
		// System.out.println("Raw query results (untyped List): " + results1 + "\n");
		System.out.println("Employee records retrieved from the database:");

		for (Employee emp : results) {
			System.out.println(emp);
		}
		System.out.println("\n");

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

		List<Employee> employeesToInsert = EmployeeData.getEmployees();

		System.out.println(
				"\n\nInserting multiple Employee entities using EntityManager#persist() method in a loop...\n");

		int numRecordsToInsert = employeesToInsert.size();
		int batchSize = 20; // Adjust batch size as needed
		int inBatchProcessedCount = 0;

		EntityTransaction transaction = em.getTransaction();
		transaction.begin();

		for (int i = 1; i <= numRecordsToInsert; i++) {
			Employee emp = employeesToInsert.get(i - 1);
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
