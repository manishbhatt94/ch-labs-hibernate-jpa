package com.mainapp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.entity.Customer;
import com.entity.Order;

public class Launch {

	public static void main(String[] args) {

		Configuration configuration = new Configuration();
		configuration.configure();

		SessionFactory sessionFactory = configuration.buildSessionFactory();
		Session session = sessionFactory.openSession();

		System.out.println("Hibernate Session object created: " + session);
		System.out.println("Connection to the database established successfully!");

		insert(session);

		session.close();
		sessionFactory.close();
		System.out.println("\n\nSession and SessionFactory closed successfully. Resources released.");

	}

	private static void readFromAddress(Session session) {

		/* @formatter:off
		final int addressId = 201;
		System.out.println("\n\nReading address, with addressId: " + addressId);

		Address address = session.get(Address.class, addressId);
		if (address != null) {
			System.out.println("\nFound address: " + address);
			System.out.println("Printing just the employee_id of the employee associated with the address:");
			System.out.println("Address belongs to employee ID: " + address.getEmployee().getEmployeeId());
			System.out.println("Calling non-id getters of associated employee (via toString) - "
					+ "which will trigger SELECT query in case of FetchType.LAZY:");
			System.out.println("Address that belongs to employee: " + address.getEmployee());
		} else {
			System.out.println("\nNot found employee, with employeeId: " + addressId);
		}

		System.out.println("\nReading task finished for address, with addressId: " + addressId);
		@formatter:on
		*/

	}

	private static void readFromEmployee(Session session) {

		/* @formatter:off
		final int employeeId = 1;
		System.out.println("\n\nReading employee, with employeeId: " + employeeId);

		Employee employee = session.get(Employee.class, employeeId);
		if (employee != null) {
			System.out.println("\nFound employee: " + employee);
			System.out.println("Printing just the address_id of the address associated with this employee:");
			System.out.println("Employee's address ID: " + employee.getEmployeeAddress().getAddressId());
			System.out.println("Calling non-id getters of associated address (via toString):");
			System.out.println("Employee's address: " + employee.getEmployeeAddress());
		} else {
			System.out.println("\nNot found employee, with employeeId: " + employeeId);
		}

		System.out.println("\nReading task finished for employee, with employeeId: " + employeeId);
		@formatter:on
		*/

	}

	private static void insert(Session session) {

		Transaction transaction = session.getTransaction();
		transaction.begin();

		Order order1 = new Order(1001, LocalDateTime.parse("2024-06-26T16:15:30"), BigDecimal.valueOf(640.58));
		Order order2 = new Order(1002, LocalDateTime.parse("2024-08-19T08:30:00"), BigDecimal.valueOf(165.0));
		Order order3 = new Order(1003, LocalDateTime.parse("2024-09-15T00:49:53"), BigDecimal.valueOf(349.99));
		List<Order> orders = Arrays.asList(order1, order2, order3);

		Customer customer = new Customer(1, "Manish B", orders);

		session.save(customer);

		transaction.commit();

	}

	private static void testForeignKeyUnique(Session session) {

		/* @formatter:off
		Transaction transaction = session.getTransaction();
		transaction.begin();

		Address address2 = session.get(Address.class, 202);
		Employee employee1 = session.get(Employee.class, 1);

		address2.setEmployee(employee1);

		transaction.commit();
		@formatter:on
		*/

	}

}
