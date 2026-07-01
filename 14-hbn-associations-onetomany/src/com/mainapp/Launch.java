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
//		readFromCustomer(session);
//		readFromOrder(session);

		session.close();
		sessionFactory.close();
		System.out.println("\n\nSession and SessionFactory closed successfully. Resources released.");

	}

	private static void readFromOrder(Session session) {

		final int orderId = 1001;
		System.out.println("\n\nReading order, with orderId: " + orderId);

		Order order = session.get(Order.class, orderId);
		if (order != null) {
			System.out.println("\nFound order: " + order);
			System.out.println("\nPrinting just the customer_id of the customer associated with the order:");
			System.out.println("This order belongs to customer ID: " + order.getCustomer().getCustomerId());
			System.out.println("\nCalling non-id getters of associated customer (via toString) - "
					+ "which will trigger SELECT query in case of FetchType.LAZY:");
			System.out.println("Customer who made this order: " + order.getCustomer());
		} else {
			System.out.println("\nNot found order, with orderId: " + orderId);
		}

		System.out.println("\nReading task finished for order, with orderId: " + orderId);

	}

	private static void readFromCustomer(Session session) {

		final int customerId = 1;
		System.out.println("\n\nReading customer, with customerId: " + customerId);

		Customer customer = session.get(Customer.class, customerId);
		if (customer != null) {
			System.out.println("\nFound customer: " + customer);
			System.out.println("\nCalling customer.getOrders():");
			System.out.println("Customer's orders:");
			customer.getOrders().forEach(System.out::println);
		} else {
			System.out.println("\nNot found customer, with customerId: " + customerId);
		}

		System.out.println("\nReading task finished for customer, with customerId: " + customerId);

	}

	private static void insert(Session session) {

		Transaction transaction = session.getTransaction();
		transaction.begin();

		Order order1 = new Order(1001, LocalDateTime.parse("2024-06-26T16:15:30"), BigDecimal.valueOf(640.58));
		Order order2 = new Order(1002, LocalDateTime.parse("2024-08-19T08:30:00"), BigDecimal.valueOf(165.0));
		Order order3 = new Order(1003, LocalDateTime.parse("2024-09-15T00:49:53"), BigDecimal.valueOf(349.99));
		List<Order> orders = Arrays.asList(order1, order2, order3);

		Customer customer = new Customer(1, "Manish B", orders);

		// Set the customer for each order (bidirectional association):
		for (Order order : orders) {
			order.setCustomer(customer);
		}

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
