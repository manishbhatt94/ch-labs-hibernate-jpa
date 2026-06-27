package com.mainapp;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.data.EmployeeData;
import com.entity.Employee;

public class Launch_HQL {

	public static void main(String[] args) {

		Configuration configuration = new Configuration();
		configuration.configure();

		SessionFactory sessionFactory = configuration.buildSessionFactory();
		Session session = sessionFactory.openSession();

		System.out.println("Hibernate Session object created: " + session);
		System.out.println("Connection to the database established successfully!");

		demoInsert(session);
		demoNonHqlInsert_WithSession(session);
		demoNonHqlInsert_WithStatelessSession(sessionFactory.openStatelessSession());
		demoInsertIntoSelect(session);
		demoRead(session);
		demoUpdate(session);
		demoRead(session);
		demoDelete(session);
		demoRead(session);
		demoRead_UsingNamedQuery(session);

		session.close();
		sessionFactory.close();
		System.out.println("\n\nSession and SessionFactory closed successfully. Resources released.");

	}

	/* HQL Does NOT support (INSERT INTO ... VALUES) Operation */
	private static void demoInsert(Session session) {

		throw new UnsupportedOperationException("HQL does NOT support INSERT operation.\n"
				+ "(More specifically, the VALUES clause in INSERT statement is NOT SUPPORTED)\n"
				+ "`INSERT INTO ... SELECT` is supported, though, or\n"
				+ "Use native SQL queries instead for bulk insert operations;\n"
				+ "or Session#save() for single-row insert.");

		/**
		 * Below code doesn't work and fails with exception:
		 *
		 * Exception in thread "main" java.lang.IllegalArgumentException:
		 * org.hibernate.hql.internal.ast.QuerySyntaxException: unexpected token: VALUES
		 * near line 2, column 5 [INSERT INTO Employee (employeeId, employeeName,
		 * employeeAddress, employeeSalary) VALUES (:id, :name, :address, :salary)]
		 *
		 * This error occurs because Hibernate Query Language (HQL) does not support the
		 * VALUES keyword for INSERT statements. In HQL, you cannot insert arbitrary,
		 * raw values directly into a table like you do in standard SQL. HQL only
		 * supports `INSERT INTO ... SELECT ...` statements, where you transfer data
		 * from one entity to another.
		 */

		// @formatter:off
//		String hql = "INSERT INTO Employee (employeeId, employeeName, employeeAddress, employeeSalary) \n"
//				+ "    VALUES (:id, :name, :address, :salary)";
		// @formatter:on

//		Transaction transaction = session.getTransaction();
//		transaction.begin();
//
//		org.hibernate.query.Query<?> query = session.createQuery(hql);
//
//		query.setParameter("id", 55555);
//		query.setParameter("name", "Isaac Newton");
//		query.setParameter("address", "Berlin, Germany");
//		query.setParameter("empSalary", 50000);
//
//		int rowsAffected = query.executeUpdate();
//
//		transaction.commit();
//		System.out.println("Number of Employee records inserted: " + rowsAffected);

	}

	private static void demoRead(Session session) {

		System.out.println("\n\nReading (multiple) Employee records from the database...\n");

		// @formatter:off
		String hql = "FROM Employee"; // Note: Semicolon omitted (Semicolon not allowed in HQL also, like JPQL).
									// Exception: org.hibernate.QueryException: unexpected char: ';' [FROM Employee;]
		// @formatter:on

		org.hibernate.query.Query<?> query1 = session.createQuery(hql);
		System.out.println("HQL query created successfully: " + query1 + "\n"
				+ "Untyped Query object created using Session's method: \n"
				+ "    org.hibernate.query.Query createQuery(String queryString);" + "\n");

		// Execute a SELECT query and return the query results as an untyped List
		List<?> results1 = query1.list();
		System.out.println("Number of Employee records retrieved: " + results1.size() + "\n");
		// System.out.println("Raw query results (untyped List): " + results1 + "\n");
		System.out.println("Employee records retrieved from the database:");

		for (Object obj : results1) {
			Employee emp = (Employee) obj;
			System.out.println(emp);
		}
		System.out.println("\n");

		org.hibernate.query.Query<Employee> query2 = session.createQuery(hql, Employee.class);

		System.out.println("HQL query created successfully: " + query2 + "\n"
				+ "Typed Query object created using Session's method: \n"
				+ "    <T> org.hibernate.query.Query<T> createQuery(String queryString, Class<T> resultType);" + "\n");

		// Execute a SELECT query and return the query results as a typed List.
		List<Employee> result2 = query2.list();
		System.out.println("Number of Employee records retrieved: " + result2.size() + "\n");
		// System.out.println("Query results (typed List): " + result2 + "\n");
		System.out.println("Employee records retrieved from the database:");

		for (Employee emp : result2) {
			System.out.println(emp);
		}

		System.out.println("\nRead operation completed successfully!");

	}

	private static void demoUpdate(Session session) {

		System.out.println("\n\nUpdating Employee records in the database...\n");
		// @formatter:off
		// Note: It seems in HQL, mentioning entity alias (e.g. "Employee e") is not
		// mandatory like in JPQL:
		String hql = "UPDATE Employee \n"
				+ "    SET employeeSalary = employeeSalary + ?2 \n" // ?2 -> salary increment
				+ "    WHERE employeeSalary < ?1"; // ?1 -> max salary for increment
//		String hql = "UPDATE Employee e  \n"
//				+ "    SET e.employeeSalary = e.employeeSalary + ?2 \n" // ?2 -> salary increment
//				+ "    WHERE e.employeeSalary < ?1"; // ?1 -> max salary for increment
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
		 * String hql = "UPDATE Employee e SET e.employeeSalary = e.employeeSalary + :increment";
		 * org.hibernate.query.Query<?> query = session.createQuery(hql);
		 * query.setParameter("increment", 345);
		 * query.executeUpdate();
		 * @formatter:on
		 */

		Transaction transaction = session.getTransaction();
		transaction.begin();

		org.hibernate.query.Query<?> query = session.createQuery(hql);

		query.setParameter(1, 50000); // ?1 -> max salary for increment
		query.setParameter(2, 999); // ?2 -> salary increment

		int rowsAffected = query.executeUpdate();

		transaction.commit();
		System.out.println("Number of Employee records updated: " + rowsAffected);

		System.out.println("\nUpdate operation completed successfully!");

	}

	private static void demoDelete(Session session) {

		System.out.println("\n\nDeleting an Employee entity from the database...\n");
		// @formatter:off
		String hql = "DELETE FROM Employee e \n"
				+ "    WHERE e.employeeSalary < :empSalary";
		// @formatter:on

		Transaction transaction = session.getTransaction();
		transaction.begin();

		org.hibernate.query.Query<?> query = session.createQuery(hql);

		query.setParameter("empSalary", 50000);

		int rowsAffected = query.executeUpdate();

		transaction.commit();
		System.out.println("Number of Employee records deleted: " + rowsAffected);

		System.out.println("\nDelete operation completed successfully!");

	}

	private static void demoNonHqlInsert_WithStatelessSession(StatelessSession statelessSession) {

		System.out.println(
				"\n\nInserting multiple Employee entities using StatelessSession#insert() method in a loop...\n");

		List<Employee> employeesToInsert = EmployeeData.getEmployees();
		int numRecordsToInsert = employeesToInsert.size();

		long startTime = System.nanoTime();

		Transaction transaction = statelessSession.getTransaction();
		transaction.begin();

		for (int i = 1; i <= numRecordsToInsert; i++) {
			Employee emp = employeesToInsert.get(i - 1);
			statelessSession.insert(emp);
		}

		System.out.println("Inserted total of " + numRecordsToInsert + " Employee records.");

		System.out.println("Committing the transaction to persist the Employee entities to the database...");
		transaction.commit();

		long finishTime = System.nanoTime();
		long timeElapsedMs = (finishTime - startTime) / 1_000_000;
		System.out.println("Committed inserts. Time elapsed: " + timeElapsedMs + "ms");
		// 66ms

		System.out.println("Employee entities inserted successfully using StatelessSession#insert() method in loop.");
		System.out.println("\nInsert operation completed successfully!");

		statelessSession.close();

	}

	private static void demoNonHqlInsert_WithSession(Session session) {

		System.out.println("\n\nInserting multiple Employee entities using Session#save() method in a loop...\n");

		List<Employee> employeesToInsert = EmployeeData.getEmployees();
		int numRecordsToInsert = employeesToInsert.size();

		int batchSize = 20; // Adjust batch size as needed
		int inBatchProcessedCount = 0;

		long startTime = System.nanoTime();

		Transaction transaction = session.getTransaction();
		transaction.begin();

		for (int i = 1; i <= numRecordsToInsert; i++) {
			Employee emp = employeesToInsert.get(i - 1);
			session.save(emp);
			inBatchProcessedCount++;

			// Flush and clear the persistence context after processing a batch of records
			if (inBatchProcessedCount % batchSize == 0) {
				session.flush();
				session.clear();
				System.out.println("Inserted " + inBatchProcessedCount + " Employee records so far...");
			}
		}

		if (inBatchProcessedCount % batchSize != 0) {
			session.flush();
			session.clear();
			System.out.println("Inserted total of " + inBatchProcessedCount + " Employee records.");
		}

		System.out.println("Committing the transaction to persist the Employee entities to the database...");
		transaction.commit();

		long finishTime = System.nanoTime();
		long timeElapsedMs = (finishTime - startTime) / 1_000_000;
		System.out.println("Committed inserts. Time elapsed: " + timeElapsedMs + "ms");
		// 144ms

		System.out.println("Employee entities inserted successfully using Session#save() method in loop.");
		System.out.println("\nInsert operation completed successfully!");

	}

	private static void demoRead_UsingNamedQuery(Session session) {

		final String queryName = "Employee.findByName";

		// @formatter:off
		System.out.println("\n\nReading Employee record(s) from the database\n" +
				"  (Using Named [HQL] Query: \"" + queryName + "\")...\n");
		// @formatter:on

		@SuppressWarnings("unchecked")
		org.hibernate.query.Query<Employee> namedQuery = session.getNamedQuery(queryName);
		// --------------------------------------------- ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
		// ↑ Eclipse warning: Type safety: The expression of type Query needs unchecked
		// conversion to conform to Query<Employee>

		namedQuery.setParameter("name", "%Sharma");

		List<Employee> results = namedQuery.list();
		System.out.println("Number of Employee records retrieved: " + results.size() + "\n");
		System.out.println("Employee records retrieved from the database:");

		for (Employee emp : results) {
			System.out.println(emp);
		}

		System.out.println("\nRead operation completed successfully!");

	}

	// For copying data from one table to another using INSERT INTO ... SELECT
	// query:
	private static void demoInsertIntoSelect(Session session) {

		System.out.println("\n\nInserting records into EmployeeArchive entity table from Employee entity table...\n");
		// @formatter:off
//		String hql = "INSERT INTO EmployeeArchive (employeeId, employeeName, employeeAddress, employeeSalary) \n"
//				+ "  SELECT e.employeeId, e.employeeName, e.employeeAddress, e.employeeSalary FROM Employee e";
		// Or without using any entity alias like "FROM Employee e":
		String hql = "INSERT INTO EmployeeArchive (employeeId, employeeName, employeeAddress, employeeSalary) \n"
				+ "  SELECT employeeId, employeeName, employeeAddress, employeeSalary FROM Employee";
		// @formatter:on

		Transaction transaction = session.getTransaction();
		transaction.begin();

		org.hibernate.query.Query<?> query = session.createQuery(hql);

		int rowsAffected = query.executeUpdate();

		transaction.commit();
		System.out.println("Number of Employee records inserted: " + rowsAffected);

		System.out.println("\nInsert (data copy) operation completed successfully!");

	}

}
