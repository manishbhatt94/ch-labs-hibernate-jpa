package com.mainapp;

import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.entity.Employee;

public class Launch_UsingStatelessSession {

	public static void main(String[] args) {

		Configuration configuration = new Configuration();
		configuration.configure();

		SessionFactory sessionFactory = configuration.buildSessionFactory();
		StatelessSession statelessSession = sessionFactory.openStatelessSession();

		System.out.println("Hibernate StatelessSession object created: " + statelessSession);
		System.out.println("Connection to the database established successfully!");

		demoInsert(statelessSession);
		demoRead(statelessSession);
		demoUpdate(statelessSession);
		demoDelete(statelessSession);

		// Note: StatelessSession interface doesn't have the load() method like
		// Session does.

		// Also, StatelessSession doesn't have methods like clear(), evict(),
		// flush(), that are used to manage the First-Level Cache (FLC), since
		// StatelessSession doesn't have an FLC at all.

		// From Javadocs of org.hibernate.StatelessSession:
		/*
		 * A command-oriented API for performing bulk operations against a database.
		 *
		 * A stateless session does not implement a first-level cache nor interact with
		 * any second-level cache, nor does it implement transactional write-behind or
		 * automatic dirty checking, nor do operations cascade to associated instances.
		 * Collections are ignored by a stateless session. Operations performed via a
		 * stateless session bypass Hibernate's event model and interceptors. Stateless
		 * sessions are vulnerable to data aliasing effects, due to the lack of a
		 * first-level cache.
		 *
		 * For certain kinds of transactions, a stateless session may perform slightly
		 * faster than a stateful session.
		 */

		statelessSession.close();
		sessionFactory.close();
		System.out.println("\n\nSession and SessionFactory closed successfully. Resources released.");

	}

	private static void demoInsert(StatelessSession statelessSession) {

		System.out.println("\n\nInserting Employee entities into the database...\n");
		Employee employee = null;
		Integer insertedRecordId = null;

		Transaction transaction = statelessSession.getTransaction();
		transaction.begin();

		employee = new Employee(1, "Eric Wright", "122nd Ave., Compton, CA", 8920);
		insertedRecordId = (Integer) statelessSession.insert(employee);
		System.out.println("Inserted a record. Identifier (ID) of inserted entity: " + insertedRecordId + ".\n");
		// Note: StatelessSession doesn't have a `save(..)` method at all.
		// Instead, there's a StatelessSession#insert(Object entity) method
		// with signature:
		// Serializable insert(Object entity);
		// where the return value (similar to what Session#save method returns)
		// is: the identifier of the inserted entity.

		employee = new Employee(2, "Nasir Jones", "21st Main, Queens Bridge, NY", 11265);
		insertedRecordId = (Integer) statelessSession.insert(employee);
		System.out.println("Inserted a record. Identifier (ID) of inserted entity: " + insertedRecordId + ".\n");

		employee = new Employee(3, "Method Man", "Park Hill Projects, Staten Island, NY", 9280);
		insertedRecordId = (Integer) statelessSession.insert(employee);
		System.out.println("Inserted a record. Identifier (ID) of inserted entity: " + insertedRecordId + ".\n");

		employee = new Employee(4, "Ol' Dirty Bastard", "Brooklyn, NY", 10550);
		insertedRecordId = (Integer) statelessSession.insert(employee);
		System.out.println("Inserted a record. Identifier (ID) of inserted entity: " + insertedRecordId + ".\n");

		employee = new Employee(5, "Slim Shady", "8 Mile Road, Detroit, MI", 9870);
		insertedRecordId = (Integer) statelessSession.insert(employee);
		System.out.println("Inserted a record. Identifier (ID) of inserted entity: " + insertedRecordId + ".\n");

		transaction.commit();
		System.out.println("\nCommitted all INSERTs!");
		System.out.println("Insert operation completed successfully!");

	}

	private static void demoRead(StatelessSession statelessSession) {

		System.out.println("\n\nReading Employee entities from the database...\n"
				+ "(Also validate that Session cache is not being used)\n");

		System.out.println("Reading Employee #1 & #2:\n");

		// Signature of StatelessSession#get method is:
		// Object get(Class entityClass, Serializable id);
		// ↑ Any method in Stateless session doesn't accept type parameters &
		// so, they return Object type instead of the entity class type.

		Employee employee1 = (Employee) statelessSession.get(Employee.class, 1);
		System.out.println("Employee #1 found: " + employee1 + "\n");

		Employee employee2 = (Employee) statelessSession.get(Employee.class, 2);
		System.out.println("Employee #2 found: " + employee2 + "\n");

		System.out.println("\nReading Employee #1 & #2 (again) - Note: Queries fired again:\n");

		Employee employee11 = (Employee) statelessSession.get(Employee.class, 1);
		System.out.println("Employee #1 found (again): " + employee11 + "\n");

		Employee employee22 = (Employee) statelessSession.get(Employee.class, 2);
		System.out.println("Employee #2 found (again): " + employee22 + "\n");

		System.out.println("\nRead operation completed successfully!");

	}

	private static void demoUpdate(StatelessSession statelessSession) {

		System.out.println("\n\nUpdating an Employee entity in the database...\n"
				+ "(Also validate that automatic dirty checking IS NOT happening)\n");

		Transaction transaction = statelessSession.getTransaction();
		transaction.begin();

		System.out.println("Fetching Employee #1...");
		Employee employee1 = (Employee) statelessSession.get(Employee.class, 1);
		if (employee1 != null) {
			System.out.println("Fetched. Employee #1 found.");
			System.out.println("Setting salary of Employee #1 entity to 19191919 (using setter).");
			employee1.setEmployeeSalary(19191919);
			System.out.println("Calling statelessSession#update method with Employee #1 entity..");
			statelessSession.update(employee1);
			System.out.println("Update to Employee #1 WILL BE persisted - since update() was called explicitly."
					+ "\n    (No dirty checking because of StatelessSession)");
		} else {
			System.out.println("Fetched. Employee #1 NOT found.");
		}

		System.out.println();

		System.out.println("Fetching Employee #2...");
		Employee employee2 = (Employee) statelessSession.get(Employee.class, 2);
		if (employee2 != null) {
			System.out.println("Fetched. Employee #2 found.");
			System.out.println("Setting salary of Employee #2 entity to 55555555 (using setter).");
			employee2.setEmployeeSalary(55555555);
			System.out.println("NOT calling statelessSession#update method with Employee #2 entity!");
			// statelessSession.update(employee1); // NOT calling update(entityObj) method!
			System.out.println(
					"Update to Employee #2 WILL NOT BE persisted" + " - since update() was NOT called explicitly."
							+ "\n    (No dirty checking because of StatelessSession)");
		} else {
			System.out.println("Fetched. Employee #2 NOT found.");
		}

		transaction.commit();
		System.out.println("\nCommitted transaction!");
		System.out.println("Update operation completed successfully!");

	}

	private static void demoDelete(StatelessSession statelessSession) {

		System.out.println("\n\nDeleting an Employee entity from the database...\n");

		Transaction transaction = statelessSession.getTransaction();
		transaction.begin();

		System.out.println("Fetching Employee #5...");
		Employee employee = (Employee) statelessSession.get(Employee.class, 5);

		if (employee != null) {
			System.out.println("Fetched. Employee #5 found.");

			System.out.println("Call statelessSession#delete method with Employee #5 entity...");
			statelessSession.delete(employee);
			// Note: Similar to the #demoUpdate method, here too, due to absence
			// of Session cache (or First-Level Cache) as we used StatelessSession,
			// so here as well, we need to explicitly call StatelessSession#delete
			// method; or else the change (i.e. deletion) will not happen in the
			// database.

		} else {
			System.out.println("Fetched. Employee #5 NOT found.");
			System.out.println("Employee with ID 5 not found for deletion.");
		}

		System.out.println("\nCommitted transaction!");
		transaction.commit();
		System.out.println("Delete operation completed successfully!");

	}

}
