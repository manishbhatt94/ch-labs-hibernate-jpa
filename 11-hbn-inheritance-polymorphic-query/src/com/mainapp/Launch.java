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

		insert(session);
		readSQL_Union(session);
		readHQL_Polymorphic(session);
		readHQL_Cricketer(session);
		readHQL_Footballer(session);

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

	private static void readSQL_Union(Session session) {

		String sql = "SELECT * FROM cricketer UNION ALL SELECT * FROM footballer";

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
	private static void readHQL_Polymorphic(Session session) {

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

	private static void readHQL_Cricketer(Session session) {

		String hql = "from Cricketer";

		Query<Cricketer> query = session.createQuery(hql, Cricketer.class);
		List<Cricketer> list = query.list();

		System.out.println("\n-> Cricketers:");
		list.forEach(p -> System.out.println(p.playerString()));
		System.out.println();

	}

	private static void readHQL_Footballer(Session session) {

		String hql = "from Footballer";

		Query<Footballer> query = session.createQuery(hql, Footballer.class);
		List<Footballer> list = query.list();

		System.out.println("\n-> Footballers:");
		list.forEach(p -> System.out.println(p.playerString()));
		System.out.println();

	}

}
