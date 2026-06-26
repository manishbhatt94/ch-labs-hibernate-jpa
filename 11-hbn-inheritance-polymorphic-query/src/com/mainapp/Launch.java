package com.mainapp;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import com.entity.Cricketer;
import com.entity.Footballer;
import com.entity.Player;

public class Launch {

	public static void main(String[] args) {

		Configuration configuration = new Configuration();
		configuration.configure();

		SessionFactory sessionFactory = configuration.buildSessionFactory();
		Session session = sessionFactory.openSession();

		System.out.println("Hibernate Session object created: " + session);
		System.out.println("Connection to the database established successfully!");

//		insert(session);
		readSQL(session);
//		readHQL(session);

		session.close();
		sessionFactory.close();
		System.out.println("\n\nSession and SessionFactory closed successfully. Resources released.");

	}

	private static void insert(Session session) {

		Transaction transaction = session.getTransaction();
		transaction.begin();

		Cricketer cricketer = new Cricketer(123, "Raju", "WICKET_KEEPER", 1000);
		Footballer footballer = new Footballer(124, "Kaju", "MIDFIELDER", 70);
		session.save(cricketer);
		session.save(footballer);

		transaction.commit();

	}

	private static void readSQL(Session session) {

		String sql = "SELECT * FROM Cricketer UNION ALL SELECT * FROM Footballer";

		NativeQuery<Object[]> nativeQuery = session.createNativeQuery(sql);

		List<Object[]> list = nativeQuery.list();

		for (Object[] orr : list) {
			for (Object o : orr) {
				System.out.print(o + " ");
			}
			System.out.println();
		}

	}

	// POLYMORPHIC QUERY
	private static void readHQL(Session session) {

		String hql = "from Player";

		Query<Player> query = session.createQuery(hql, Player.class);
		List<Player> list = query.list();

		for (Player p : list) {
			if (p instanceof Cricketer) {
				System.out.println("Cricketer: " + p.playerString());
			} else if (p instanceof Footballer) {
				System.out.println("Footballer: " + p.playerString());
			} else {
				System.out.println("Player: " + p.playerString());
			}
		}
	}

}
