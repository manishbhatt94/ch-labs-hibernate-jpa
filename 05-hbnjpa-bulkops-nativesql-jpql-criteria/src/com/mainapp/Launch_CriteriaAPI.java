package com.mainapp;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CompoundSelection;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.data.EmployeeData;
import com.entity.Employee;
import com.entity.EmployeeShort;

public class Launch_CriteriaAPI {

	public static void main(String[] args) {

		EntityManagerFactory emf = Persistence.createEntityManagerFactory("my-persistence-unit-1");

		EntityManager em = emf.createEntityManager();

		System.out.println("EntityManager created successfully: " + em);

		System.out.println("Connection to the database established successfully!");

		demoInsert(em);
		demoNonCriteriaInsert(em);
		demoRead(em);
		demoReadComplex_Projection(em);
		demoReadComplex_SelectAll(em);
		demoReadComplex_MultiSelect(em);
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

		System.out.println("\n\nReading (multiple) Employee records from the database (Simple CriteriaQuery)...\n");

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
		CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);
		// ↑⇑⇈ This is how you create a CriteriaQuery object using the
		// CriteriaBuilder.

		// Let's say we want to select all Employee records from the database, i.e.,
		// the equivalent of "SELECT e FROM Employee e" in JPQL.
		// (or "SELECT * FROM hbn_employee" in SQL.)

		// This defines the FROM clause of the query, specifying the entity to query:
		Root<Employee> from = criteriaQuery.from(Employee.class); // ← `from Employee e`

		// This defines the SELECT clause of the query, specifying what to select:
		criteriaQuery.select(from); // ← `SELECT e`

		// Create a TypedQuery object from the CriteriaQuery object:
		TypedQuery<Employee> query = em.createQuery(criteriaQuery);

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

	private static void demoReadComplex_Projection(EntityManager em) {

		// Let's say we want to execute a more complex SELECT query than a simple
		// "SELECT * FROM hbn_employee".

		// For instance, we want to select the the `employee_id`, `employee_name` &
		// `employee_salary` columns from `hbn_employee` table for those employees whose
		// `employee_salary` is greater than 50000, and whose `employee_name` starts
		// with 'R', and order the results by `employee_salary` in descending order.

		// The equivalent JPQL query would be:
		// @formatter:off
		// SELECT e.employeeId, e.employeeName FROM Employee e
		// 		WHERE e.employeeSalary > 50000 AND e.employeeName LIKE 'R%'
		// 		ORDER BY e.employeeSalary DESC
		// @formatter:on

		// And the equivalent SQL query would be:
		// @formatter:off
		// SELECT employee_id, employee_name FROM hbn_employee
		// 		WHERE employee_salary > 50000 AND employee_name LIKE 'R%'
		// 		ORDER BY employee_salary DESC;
		// @formatter:on

		System.out.println("\n\nReading (multiple) Employee records from the database (Complex CriteriaQuery)...\n");

		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		CriteriaQuery<EmployeeShort> criteriaQuery = criteriaBuilder.createQuery(EmployeeShort.class);

		// This defines the FROM clause of the query, specifying the entity to query:
		Root<Employee> root = criteriaQuery.from(Employee.class); // ← `from Employee e`

		// These two lines specify the two filter conditions which we will provide in
		// the WHERE clause of the query:
		// Condition 1: employeeSalary > 50000 (JPQL) or employee_salary > 50000 (SQL):
		Predicate salaryPredicate = criteriaBuilder.greaterThan(root.get("employeeSalary"), 50000);
		// Condition 2: employeeName LIKE 'R%' (JPQL) or employee_name LIKE 'R%' (SQL):
		Predicate namePredicate = criteriaBuilder.like(root.get("employeeName"), "R%");

		// Combine the two conditions using AND operator:
		Predicate combinedPredicate = criteriaBuilder.and(salaryPredicate, namePredicate);

		// This defines the SELECT clause of the query, specifying what to select:
		// @see: https://thorben-janssen.com/optimal-query-and-projection-jpa-hibernate/

		// (1. For `SELECT *`, we simply select the root entity:)
		// criteriaQuery.select(root); // ← `SELECT e`

		// (2. For selecting only some columns, we can define a CompoundSelection
		CompoundSelection<EmployeeShort> selection = criteriaBuilder.construct(EmployeeShort.class,
				root.get("employeeId"), root.get("employeeName"), root.get("employeeSalary"));
		// (2. and then, use that in `criteriaQuery.select( ..<selection>.. )`) method:
		criteriaQuery.select(selection); // ← `SELECT e.employeeId, e.employeeName, e.employeeSalary`

		// (3. Or, we can directly specify the columns to select in the
		// `criteriaQuery.multiselect( .. )` method:)
//		criteriaQuery.multiselect(root.get("employeeId"), root.get("employeeName"), root.get("employeeSalary"));
		// (But, this returns Employee objects only, with the non selected fields as
		// nulls.)

		// This defines the WHERE clause of the query, specifying the filter conditions:
		criteriaQuery.where(combinedPredicate); // ← `WHERE e.employeeSalary > 50000 AND e.employeeName LIKE 'R%'`

		// This defines an Order to be provided to the ORDER BY clause:
		// ← `ORDER BY e.employeeSalary DESC`
		Order orderDescBySalary = criteriaBuilder.desc(root.get("employeeSalary"));

		// Add the ORDER BY clause to the CriteriaQuery object:
		criteriaQuery.orderBy(orderDescBySalary);

		// Create a TypedQuery object from the CriteriaQuery object:
		// (i.e. generate the actual SQL query from the CriteriaQuery object, and create
		// a Query object to execute the query against the database.)
		TypedQuery<EmployeeShort> query = em.createQuery(criteriaQuery);

		// Execute the SELECT query and return the query results as a typed List:
		List<EmployeeShort> results = query.getResultList();

		System.out.println("Number of Employee records retrieved: " + results.size() + "\n");
		// System.out.println("Raw query results (untyped List): " + results1 + "\n");
		System.out.println("Employee records retrieved from the database:");

		for (EmployeeShort emp : results) {
			System.out.println(emp);
		}
		System.out.println("\n");

		System.out.println("\nRead operation completed successfully!");

	}

	private static void demoReadComplex_SelectAll(EntityManager em) {

		System.out.println("\n\nReading (multiple) Employee records from the database (Complex CriteriaQuery)...\n");

		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);

		Root<Employee> root = criteriaQuery.from(Employee.class);

		Predicate salaryPredicate = criteriaBuilder.greaterThan(root.get("employeeSalary"), 50000);
		Predicate namePredicate = criteriaBuilder.like(root.get("employeeName"), "R%");

		Predicate combinedPredicate = criteriaBuilder.and(salaryPredicate, namePredicate);

		criteriaQuery.select(root);
		criteriaQuery.where(combinedPredicate);

		Order orderDescBySalary = criteriaBuilder.desc(root.get("employeeSalary"));
		criteriaQuery.orderBy(orderDescBySalary);

		TypedQuery<Employee> query = em.createQuery(criteriaQuery);

		List<Employee> results = query.getResultList();

		System.out.println("Number of Employee records retrieved: " + results.size() + "\n");
		System.out.println("Employee records retrieved from the database:");

		for (Employee emp : results) {
			System.out.println(emp);
		}
		System.out.println("\n");

		System.out.println("\nRead operation completed successfully!");

	}

	private static void demoReadComplex_MultiSelect(EntityManager em) {

		System.out.println("\n\nReading (multiple) Employee records from the database (Complex CriteriaQuery)...\n");

		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);

		Root<Employee> root = criteriaQuery.from(Employee.class);

		Predicate salaryPredicate = criteriaBuilder.greaterThan(root.get("employeeSalary"), 50000);
		Predicate namePredicate = criteriaBuilder.like(root.get("employeeName"), "R%");

		Predicate combinedPredicate = criteriaBuilder.and(salaryPredicate, namePredicate);

		// Note: For using below multiselect method, we had to defined the corresponding
		// parameterized constructor in Employee entity class, which takes only
		// employeeId, employeeName and employeeSalary as parameters, and initializes
		// the corresponding fields. This is because, when we use multiselect method to
		// select only some columns, the non selected fields will be nulls in the
		// returned Employee objects. So, we need a constructor that can create Employee
		// objects with only the selected fields initialized.
		// i.e. the below constructor:
		// public Employee(int employeeId, String employeeName, int employeeSalary)
		criteriaQuery.multiselect(root.get("employeeId"), root.get("employeeName"), root.get("employeeSalary"));
		criteriaQuery.where(combinedPredicate);

		Order orderDescBySalary = criteriaBuilder.desc(root.get("employeeSalary"));
		criteriaQuery.orderBy(orderDescBySalary);

		TypedQuery<Employee> query = em.createQuery(criteriaQuery);

		List<Employee> results = query.getResultList();

		System.out.println("Number of Employee records retrieved: " + results.size() + "\n");
		System.out.println("Employee records retrieved from the database:");

		for (Employee emp : results) {
			System.out.println(emp);
		}
		System.out.println("\n");

		System.out.println("\nRead operation completed successfully!");

	}

	private static void demoUpdate(EntityManager em) {

		System.out.println("\n\nUpdating Employee records in the database...\n");

		// Let's say we to execute below Bulk UPDATE query:
		// UPDATE Employee e SET e.employeeSalary = 12000 WHERE e.employeeId <= 10;

		EntityTransaction transaction = em.getTransaction();
		transaction.begin();

		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		// Create a CriteriaUpdate interface type object (or an "Update Criteria"):
		CriteriaUpdate<Employee> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(Employee.class);

		Root<Employee> root = criteriaUpdate.from(Employee.class);

		criteriaUpdate.set("employeeSalary", 12000); // ← SET e.employeeSalary = 12000

		criteriaUpdate.where(criteriaBuilder.lessThanOrEqualTo(root.get("employeeId"), 10));
		// ↑ WHERE e.employeeId <= 10

		// Create a Query from the CriteriaUpdate object:
		Query query = em.createQuery(criteriaUpdate);

		// Execute the above Query (which is an UPDATE) using Query#executeUpdate()
		int rowsAffected = query.executeUpdate();

		transaction.commit();
		System.out.println("Number of Employee records updated: " + rowsAffected);

		System.out.println("\nUpdate operation completed successfully!");

	}

	private static void demoDelete(EntityManager em) {

		System.out.println("\n\nDeleting Employee entities from the database...\n");

		// Let's say we to execute below Bulk UPDATE query:
		// DELETE FROM Employee e WHERE e.employeeId > 10;

		EntityTransaction transaction = em.getTransaction();
		transaction.begin();

		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		// Create a CriteriaUpdate interface type object (or an "Update Criteria"):
		CriteriaDelete<Employee> criteriaDelete = criteriaBuilder.createCriteriaDelete(Employee.class);

		Root<Employee> root = criteriaDelete.from(Employee.class);

		criteriaDelete.where(criteriaBuilder.greaterThan(root.get("employeeId"), 10));
		// ↑ WHERE e.employeeId > 10

		// Create a Query from the CriteriaDelete object:
		Query query = em.createQuery(criteriaDelete);

		// Execute the above Query (which is a DELETE) using Query#executeUpdate()
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
