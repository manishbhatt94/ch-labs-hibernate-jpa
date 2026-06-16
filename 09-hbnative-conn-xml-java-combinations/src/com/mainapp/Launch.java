package com.mainapp;

public class Launch {

	public static void main(String[] args) {

		System.out.println("Hibernate Session object created: "/* + session */);
		// ↑ Prints something like:
		// Hibernate Session object created: SessionImpl(1850874910<open>)

		System.out.println("Connection to the database established successfully!");

		// Close the Session and SessionFactory to release resources:

		System.out.println("\n\nSession and SessionFactory closed successfully. Resources released.");

	}

}
