package com.mainapp;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

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
//		demoRead(em);
//		demoUpdate(em);
//		demoRead(em);
//		demoDelete(em);
//		demoRead(em);

		// Close the EntityManager and EntityManagerFactory to release resources:
		em.close();
		emf.close();
		System.out.println("\n\nEntityManager and EntityManagerFactory closed successfully. Resources released.");

	}

	private static void demoInsert(EntityManager em) {

		List<Employee> employees = new ArrayList<>();

		employees.add(new Employee(10234, "Arjun Sharma", "Indiranagar, Bangalore, Karnataka", 55000));
		employees.add(new Employee(234567, "Priya Nair", "Panampilly Nagar, Kochi, Kerala", 48000));
		employees.add(new Employee(8765432, "Ravi Kumar", "Madhapur, Hyderabad, Telangana", 60000));
		employees.add(new Employee(45678, "Sneha Reddy", "T. Nagar, Chennai, Tamil Nadu", 52000));
		employees.add(new Employee(98765, "Vikram Singh", "Malviya Nagar, Jaipur, Rajasthan", 45000));

		System.out.println("\n\nInserting Employee records into the database...\n");
		// @formatter:off
		String sql = "INSERT INTO hbn_employee \n"
				+ "    (employee_id, employee_name, employee_address, employee_salary) \n"
				+ "    VALUES (?, ?, ?, ?);";
		// @formatter:on

		EntityTransaction transaction = em.getTransaction();
		transaction.begin();

		Query nativeQuery = em.createNativeQuery(sql);

		for (Employee emp : employees) {
			nativeQuery.setParameter(1, emp.getEmployeedId());
			nativeQuery.setParameter(2, emp.getEmployeeName());
			nativeQuery.setParameter(3, emp.getEmployeeAddress());
			nativeQuery.setParameter(4, emp.getEmployeeSalary());
			nativeQuery.executeUpdate();
		}

		transaction.commit();

		System.out.println("\nInsert operation completed successfully!");

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
