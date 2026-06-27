package com.mainapp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.entity.Account;
import com.entity.Car;
import com.entity.Company;
import com.entity.Employee;

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

	private static void insert(Session session) {

		Transaction transaction = session.getTransaction();
		transaction.begin();

		Account account1 = new Account("00120001004", "Raju Kumar", "Kempegowda Puram, Bengaluru", "SBI", "SBIN12004");
		Car car1 = new Car("KA04JK1200", "Tata Punch", LocalDateTime.now(), BigDecimal.valueOf(825708.70));
		Company company1 = new Company("CMP-11001", "Bulls Eye Education Pvt Ltd",
				"Local home correspondence for commerce competitive exams", account1);
		Employee employee1 = new Employee(1, "Raju Kumar", company1, car1);

		session.save(employee1);

		transaction.commit();

	}

}
