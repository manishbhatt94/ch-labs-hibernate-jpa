package com.mainapp;

import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.entity.Employee;

public class Launch {

	public static void main(String[] args) {

		Configuration configuration = new Configuration();
		configuration.configure();

		SessionFactory sessionFactory = configuration.buildSessionFactory();
		Session session = sessionFactory.openSession();

		System.out.println("Hibernate Session object created: " + session);
		System.out.println("Connection to the database established successfully!");

//		demoInsert(session);
//		demoRead(session);
//		demoUpdate(session);
//		demoDelete(session);
		demoGetVsLoad(session);

		session.close();
		sessionFactory.close();
		System.out.println("\n\nSession and SessionFactory closed successfully. Resources released.");

	}

	private static void demoInsert(Session session) {

		System.out.println("\n\nInserting an Employee entity into the database...");

		Employee employee = new Employee(1, "Eric Wright", "122nd Ave., Compton, CA", 8920);
//		Employee employee = new Employee(2, "Nasir Jones", "21st Main, Queens Bridge, NY", 11265);
//		Employee employee = new Employee(3, "Method Man", "Park Hill Projects, Staten Island, NY", 9280);
//		Employee employee = new Employee(4, "Ol' Dirty Bastard", "Brooklyn, NY", 10550);
//		Employee employee = new Employee(5, "Slim Shady", "8 Mile Road, Detroit, MI", 9870);

		Transaction transaction = session.getTransaction();
		transaction.begin();

		session.save(employee);

		transaction.commit();
		System.out.println("\nInsert operation completed successfully!");

	}

	private static void demoRead(Session session) {

		System.out.println("\n\nReading an Employee entity from the database...");

		Employee employee = session.get(Employee.class, 1);

		System.out.println("\nEmployee found: " + employee);

	}

	private static void demoUpdate(Session session) {

		System.out.println("\n\nUpdating an Employee entity in the database...");

		Employee employee = session.get(Employee.class, 1);

		if (employee != null) {
			employee.setEmployeeSalary(10580);

			Transaction transaction = session.getTransaction();
			transaction.begin();

			session.update(employee);

			transaction.commit();
			System.out.println("\nUpdate operation completed successfully!");
		} else {
			System.out.println("\nEmployee with ID 1 not found for update.");
		}

	}

	private static void demoDelete(Session session) {

		System.out.println("\n\nDeleting an Employee entity from the database...");

		Employee employee = session.get(Employee.class, 1);

		if (employee != null) {
			Transaction transaction = session.getTransaction();
			transaction.begin();

			session.delete(employee);

			transaction.commit();
			System.out.println("\nDelete operation completed successfully!");
		} else {
			System.out.println("\nEmployee with ID 1 not found for deletion.");
		}

	}

	private static void demoGetVsLoad(Session session) {

		System.out.println("\n\n======== Demo: Session#get() [Eager] vs. Session#load() [Lazy] =========\n");
		final int idThatExists = 1;
		final int idThatDoesntExists = 9999;

		System.out.println("\n~-~-~-~-~-~-~-~ Session#get() [Eager] ~-~-~-~-~~-~-~-~-~-~-~\n\n");

		System.out.println("------>>> When entity with given ID - EXISTS in DB:\n");
		debugCallGet(session, idThatExists);

		System.out.println("------>>> When entity with given ID - DOES NOT EXIST in DB:\n");
		debugCallGet(session, idThatDoesntExists);

		System.out.println("\n~-~-~-~-~-~-~-~ Session#load() [Lazy] ~-~-~-~-~~-~-~-~-~-~-~\n\n");

		System.out.println("------>>> When entity with given ID - EXISTS in DB:\n");
		debugCallLoad(session, idThatExists);

		System.out.println("------>>> When entity with given ID - DOES NOT EXIST in DB:\n");
		debugCallLoad(session, idThatDoesntExists);

	}

	private static void debugCallGet(Session session, final int id) {

		session.clear();

		Employee employee;
		int receivedId;
		String receivedName;

		System.out.println("-@-- Calling Session#get() with ID: " + id + " ...");
		employee = session.get(Employee.class, id);
		System.out.println("-@---- Called Session#get() with ID: " + id);
		if (employee == null) {
			System.out.println("-@-- Received <null> from Session#get() call with ID: " + id);
			System.out.println("-@-- DB table record with ID: " + id + " -- DOES NOT EXIST!");
		} else {
			System.out.println(
					"-@-- Returned employee object class is: [" + employee.getClass() + "] -- which IS NOT A proxy!");
			System.out.println("-@-- Received <non-null> entity object with ID: " + id);
			System.out.println("-@-- DB table record with ID: " + id + " -- EXISTS!");
			System.out.println("-@-- Calling employee.getEmployeeId() ... ");
			receivedId = employee.getEmployeeId();
			System.out.println("-@---- Received employeeId: " + receivedId);
			System.out.println("-@- Accessing non-ID fields of employee ... ");
			System.out.println("-@-- Calling employee.getEmployeeName() ... ");
			receivedName = employee.getEmployeeName();
			System.out.println("-@---- Received employeeName: " + receivedName);
			System.out.println("-@-- Calling employee.toString() ... ");
			System.out.println("-@---- employee.toString(): " + employee.toString());
			System.out.println("-@- Able to access non-ID fields of employee!");
		}
		System.out.println();

	}

	private static void debugCallLoad(Session session, final int id) {

		session.clear();

		Employee employee;
		int receivedId;
		String receivedName;

		System.out.println("-@-- Calling Session#load() with ID: " + id + " ...");
		employee = session.load(Employee.class, id);
		System.out.println("-@---- Called Session#load() with ID: " + id);
		System.out
				.println("-@-- Returned employee object class is: [" + employee.getClass() + "] -- which IS A proxy!");

		System.out.println("-@-- Received <non-null> entity object with ID: " + id);
		System.out.println("-@-- DB table record with ID: " + id + " -- MIGHT or MIGHT NOT EXIST!");
		System.out.println("-@-- Calling employee.getEmployeeId() ... ");
		receivedId = employee.getEmployeeId();
		System.out.println("-@---- Received employeeId: " + receivedId);

		try {
			System.out.println("-@- Accessing non-ID fields of employee ... ");
			System.out.println("-@-- Calling employee.getEmployeeName() ... ");
			receivedName = employee.getEmployeeName();
			System.out.println("-@---- Received employeeName: " + receivedName);
			System.out.println("-@-- Calling employee.toString() ... ");
			System.out.println("-@---- employee.toString(): " + employee.toString());
			System.out.println("-@- Able to access non-ID fields of employee!");
			System.out.println("-@-- Therefore, DB table record with ID: " + id + " -- EXISTS!");
		} catch (ObjectNotFoundException e) { // org.hibernate.ObjectNotFoundException
			System.out.println("-@-- Caught ObjectNotFoundException while accessing non-ID "
					+ "fields. Exception message: " + e.getMessage());
			System.out.println("-@-- Therefore, DB table record with ID: " + id + " -- DOES NOT EXIST!");
		}
		System.out.println();

	}

}
