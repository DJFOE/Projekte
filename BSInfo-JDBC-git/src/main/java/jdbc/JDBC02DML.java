package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBC02DML {

	public static void main(String[] args) {
		System.out.println("Execute DML");
		System.out.println("====================");
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/demoDB", "guest", "Pa$$w0rd");
			
			if (connection != null) {
				System.out.println("Connection established!");
			} else {
				System.out.println("Could not establish connection!");
				return;
			}
			
			Statement statement = null;
			//1. Erzeugen eines Datensatzes in der Tabelle employee
			statement = connection.createStatement();
			String sql = "INSERT INTO employee(lastName, firstName, email) VALUES ('Doe', 'John', 'john.doe@demoDB')" ;
			int i = statement.executeUpdate(sql);
			System.out.println("Query ok, " +i+ " rows affected");
			if (connection != null && !connection.isClosed()) {
				connection.close();
				System.out.println("Connection closed");
			}
			if (statement != null && !statement.isClosed()) {
				statement.close();
				System.out.println("Statement closed");
			}
		} catch (ClassNotFoundException e) {
			System.out.println("MySQL JDBC-Treiber not found!\n");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("You have an SQL Syntax error");
			e.printStackTrace();
		}
	}
}
