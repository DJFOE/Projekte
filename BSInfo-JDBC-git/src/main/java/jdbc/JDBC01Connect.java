package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBC01Connect {
	public static void main(String[] args) {
		System.out.println("JDBC Connection Test");
		System.out.println("====================");
		try {
			// 1. Laden des JDBC Treibers f�r mariaDB
			// jar-Archiv muss im CLASSPATH eingetragen sein.
			Class.forName("com.mysql.cj.jdbc.Driver");

			System.out.println("Driver sucessfully loaded!");
			// 2. Erstellen der Connection zur Datenbank
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/demoDB", "guest", "Pa$$w0rd");
			if (connection != null) {
				System.out.println("Connection established!");
			} else {
				System.out.println("Could not establish connection!");
				return;
			}
			// 4. Schlie�en der Connection
			if (!connection.isClosed()) {
				connection.close();
				System.out.println("Connection closed");
			}
		} catch (ClassNotFoundException e) {
			System.out.println("JDBC-Driver not found");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("Connection could not be established");
			e.printStackTrace();
		}
	}
}