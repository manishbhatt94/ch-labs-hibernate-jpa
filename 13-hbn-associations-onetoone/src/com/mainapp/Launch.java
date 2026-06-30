package com.mainapp;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.entity.Address;
import com.entity.Employee;

public class Launch {

	public static void main(String[] args) {

		Configuration configuration = new Configuration();
		configuration.configure();

		SessionFactory sessionFactory = configuration.buildSessionFactory();
		Session session = sessionFactory.openSession();

		System.out.println("Hibernate Session object created: " + session);
		System.out.println("Connection to the database established successfully!");

//		insert(session);
//		testForeignKeyUnique(session);
//		readFromEmployee(session);
		readFromAddress(session);

		session.close();
		sessionFactory.close();
		System.out.println("\n\nSession and SessionFactory closed successfully. Resources released.");

	}

	private static void readFromAddress(Session session) {

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

	}

	private static void readFromEmployee(Session session) {

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

	}

	private static void insert(Session session) {

		Transaction transaction = session.getTransaction();
		transaction.begin();

		Address address1 = new Address(201, "D-59/7 First Floor", "1st Main 22nd Cross", "Kuvempu Nagara 4th Phase",
				"Bengaluru", "Karnataka");
		Employee employee1 = new Employee(1, "Raju Kumar", "Senior IT Admin");
		employee1.addEmployeeAddress(address1);

		session.save(employee1);
		session.flush();
		session.clear();

		Address address2 = new Address(202, "119/2, Opp. Raghavendra Temple", "3rd Main 60 Feet Road",
				"Dwarka Nagar, Hosakerehalli", "Bengaluru", "Karnataka");
		Employee employee2 = new Employee(2, "Raghav Chaddha", "Jr. Boot Licker");
		employee2.addEmployeeAddress(address2);
		session.save(employee2);

		transaction.commit();

	}

	private static void testForeignKeyUnique(Session session) {

		Transaction transaction = session.getTransaction();
		transaction.begin();

		Address address2 = session.get(Address.class, 202);
		Employee employee1 = session.get(Employee.class, 1);

		address2.setEmployee(employee1);

		transaction.commit();

	}

}
