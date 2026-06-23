package com.mainapp;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.NativeQuery;

import com.entity.Employee;

public class Launch_NativeSQL {

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
//		demoRead(session);
//		demoDelete(session);
//		demoRead(session);
//		demoRead_UsingNamedNativeQuery(session);

		session.close();
		sessionFactory.close();
		System.out.println("\n\nSession and SessionFactory closed successfully. Resources released.");

	}

	private static void demoInsert(Session session) {

		List<Employee> employees = new ArrayList<>();

		employees.add(new Employee(10234, "Arjun Sharma", "Indiranagar, Bangalore, Karnataka", 55000));
		employees.add(new Employee(234567, "Priya Nair", "Panampilly Nagar, Kochi, Kerala", 48000));
		employees.add(new Employee(8765432, "Ravi Kumar", "Madhapur, Hyderabad, Telangana", 60000));
		employees.add(new Employee(45678, "Sneha Reddy", "T. Nagar, Chennai, Tamil Nadu", 52000));
		employees.add(new Employee(98765, "Vikram Singh", "Malviya Nagar, Jaipur, Rajasthan", 45000));

		System.out.println("\n\nInserting Employee records into the database...\n");

		// SQL INSERT statement with named parameters (:emp_id, :emp_name, etc.)
		// Note: Hibernate's Native SQL queries only support:
		// 1. Plain positional parameters (i.e. Raw JDBC style "?" only)
		// 2. Named parameters (e.g. ":emp_id" etc.),
		// .. and NOT ordinal parameters.
		// @formatter:off
		String sql = "INSERT INTO hbn_employee \n"
				+ "    (employee_id, employee_name, employee_address, employee_salary) \n"
				+ "    VALUES (:emp_id, :emp_name, :emp_address, :emp_salary);";
		// ↑ Using named parameters in the SQL query string, instead of Plain Positional Parameters i.e. just "?".
		// @formatter:on

		Transaction transaction = session.getTransaction();
		transaction.begin();

		NativeQuery<?> nativeQuery = session.createNativeQuery(sql);
//		Query nativeQuery = session.createNativeQuery(sql);

		for (Employee emp : employees) {
			nativeQuery.setParameter("emp_id", emp.getEmployeeId());
			nativeQuery.setParameter("emp_name", emp.getEmployeeName());
			nativeQuery.setParameter("emp_address", emp.getEmployeeAddress());
			nativeQuery.setParameter("emp_salary", emp.getEmployeeSalary());
			nativeQuery.executeUpdate();
		}

		transaction.commit();

		System.out.println("\nInsert operation completed successfully!");

	}

	private static void demoRead(Session session) {

		System.out.println("\n\nReading (multiple) Employee records from the database...\n");

		// @formatter:off
//		String sql = "SELECT employee_id, employee_name, employee_address, employee_salary \n" +
//				"    FROM hbn_employee;";
		// or, using SELECT *:
		String sql = "SELECT * FROM hbn_employee;";
		// @formatter:on

		NativeQuery<?> nativeQuery1 = session.createNativeQuery(sql);
		System.out.println("Using [org.hibernate.query.NativeQuery createNativeQuery​(String sqlString)] method \n"
				+ "to create a native SQL query without specifying the result mapping. \n"
				+ "The query results will be returned as an untyped List of Object arrays (List<Object[]>).\n");

		// Execute a SELECT query and return the query results as an untyped List
//		@SuppressWarnings("unchecked")
//		List<Object[]> results1 = (List<Object[]>) nativeQuery1.list();
		// ---------------------- ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
		// ↑ Eclipse warning: Type safety: Unchecked cast from List<capture#6-of ?> to
		// List<Object[]>
		// (i.e. from List<?> to List<Object[]>)

		List<?> results1 = nativeQuery1.list();

		// In Hibernate Native API, getResultList() method is a default method in
		// interface which just calls the list() method:
//		List<Object[]> results1 = nativeQuery1.getResultList();

		System.out.println("Number of Employee records retrieved: " + results1.size() + "\n");
		System.out.println("Raw query results (untyped List): " + results1 + "\n");
		System.out.println("Employee records retrieved from the database:");

		/*
		 * Use below loop when using:
		 *
		 * List<Object[]> results1 = (List<Object[]>) nativeQuery1.list();
		 */
//		for (Object[] objectArray : results1) {
//			for (Object obj : objectArray) {
//				System.out.print(obj + " | ");
//			}
//			System.out.println();
//		}

		/*
		 * Use below loop when using:
		 *
		 * List<?> results1 = nativeQuery1.list();
		 */
		for (Object rowObj : results1) {
			Object[] row = (Object[]) rowObj;
			for (Object columnValue : row) {
				System.out.print(columnValue + " | ");
			}
			System.out.println();
		}

		NativeQuery<Employee> nativeQuery2 = session.createNativeQuery(sql, Employee.class);
		System.out.println("\nUsing [<R> NativeQuery<R> createNativeQuery(String sqlString, Class<R> resultClass);] \n"
				+ "method to create a native SQL query with result mapping to the \n"
				+ "Employee entity class. The query results will be returned as an \n"
				+ "untyped List whose entries can be cast to Employee entity objects.\n");

		// Execute a SELECT query and return the query results as an untyped List
		List<Employee> results2 = nativeQuery2.list();
		System.out.println("Number of Employee records retrieved: " + results2.size() + "\n");
		System.out.println("Raw query results (untyped List): " + results2 + "\n");
		System.out.println("Employee records retrieved from the database:");

		for (Object obj : results2) {
			Employee emp = (Employee) obj;
			System.out.println(emp);
		}

		System.out.println("\nRead operation completed successfully!");

	}

	private static void demoUpdate(Session session) {

		System.out.println("\n\nUpdating Employee records in the database...\n");
		// @formatter:off
		String sql = "UPDATE hbn_employee \n"
				+ "    SET employee_salary = employee_salary + ? \n"
				+ "    WHERE employee_salary < ?;";
		// @formatter:on

		Transaction transaction = session.getTransaction();
		transaction.begin();

		/*
		  * @formatter:off
	      * NativeQuery<?> means:
	      *   - This query object is generic, but we don’t care about the type parameter
	      *   - The '?' wildcard says: "There is some type here, but I’m not going to use it"
	      *   - This avoids the raw type warning (NativeQuery without <T>)
	      *   - It also avoids misleading code like NativeQuery<Employee> for an UPDATE,
	      *     because UPDATE queries don’t return Employee objects.
	      *
	      * Think of <?> as a "don’t know / don’t care" placeholder for the type.
	      * It keeps the compiler happy without lying about the query’s result type.
	      * @formatter:on
	      */
		NativeQuery<?> nativeQuery = session.createNativeQuery(sql);

		// Bind parameters by position:
		nativeQuery.setParameter(1, 999); // ? -> salary increment.
		nativeQuery.setParameter(2, 50000); // ? -> maximum salary for increment to apply.

		// executeUpdate() returns the number of rows affected (int), not table records
		// like SELECT queries
		int rowsAffected = nativeQuery.executeUpdate();

		transaction.commit();
		System.out.println("Number of Employee records updated: " + rowsAffected);

		System.out.println("\nUpdate operation completed successfully!");

	}

	private static void demoDelete(Session session) {

		System.out.println("\n\nDeleting an Employee entity from the database...\n");
		// @formatter:off
		String sql = "DELETE FROM hbn_employee \n"
				+ "    WHERE employee_id > :max_emp_id ;";
		// ↑ Note: the intentional space before semicolon above "WHERE employee_id > :max_emp_id ;"
		// This is so that the parameter name isn't confused to contain the semicolon, and we
		// getting error:
		// Could not locate named parameter [max_emp_id], expecting one of [max_emp_id;]
		// @formatter:on

		Transaction transaction = session.getTransaction();
		transaction.begin();

		NativeQuery<?> nativeQuery = session.createNativeQuery(sql);

		nativeQuery.setParameter("max_emp_id", 100000);

		int rowsAffected = nativeQuery.executeUpdate();

		transaction.commit();
		System.out.println("Number of Employee records deleted: " + rowsAffected);

		System.out.println("\nDelete operation completed successfully!");

	}

	private static void demoRead_UsingNamedNativeQuery(Session session) {

		final String queryName = "Employee.findBySalaryGreaterThan";

		// @formatter:off
		System.out.println("\n\nReading Employee record(s) from the database\n" +
				"  (Using Named Native [SQL] Query: \"" + queryName + "\")...\n");
		// @formatter:on

		@SuppressWarnings("unchecked")
		NativeQuery<Employee> namedNativeQuery = session.getNamedNativeQuery(queryName);
		// ------------------------------------- ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
		// ↑ Eclipse warning: Type safety: The expression of type NativeQuery needs
		// unchecked conversion to conform to NativeQuery<Employee>

		namedNativeQuery.setParameter("salary", 50000);

		List<Employee> results = namedNativeQuery.list();

		System.out.println("Number of Employee records retrieved: " + results.size() + "\n");
		System.out.println("Employee records retrieved from the database:");

		for (Employee emp : results) {
			System.out.println(emp);
		}

		System.out.println("\nRead operation completed successfully!");

	}

}
