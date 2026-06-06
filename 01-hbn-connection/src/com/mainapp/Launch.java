package com.mainapp;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Launch {

	public static void main(String[] args) {

		EntityManagerFactory emf = Persistence.createEntityManagerFactory("my-persistence-unit-1");

	}

}
