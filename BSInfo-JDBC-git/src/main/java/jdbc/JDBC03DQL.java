package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;



public class JDBC03DQL {

	public static void main(String[] args) {
		System.out.println("Execute DML");
		System.out.println("====================");
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/demoDB", "guest", "Pa$$w0rd");
			
			if (connection != null) {
				System.out.println("Connection established!");
			} else {
				System.out.println("Could not establish connection!");
				return;
			}
			
			Statement statement = connection.createStatement();
			//1. Lassen Sie sich die Struktur der Tabelle emloyees anzeigen.

			String sql ="DESC employee";
			//a) ResultSet erzeugen
			ResultSet resultSet = statement.executeQuery(sql);
			
			//b) Metadaten ermitteln
			ResultSetMetaData rsMetaData = resultSet.getMetaData();
	
			//c) ResultSet ausgeben
			int columnCount = rsMetaData.getColumnCount();
			while (resultSet.next()) {
				for (int i = 1; i <= columnCount; ++i) {
				Object o = resultSet.getObject(i);
				System.out.print(o == null ? "null" : o);
				System.out.print("\t");
				}
				System.out.println();
				}

			
			if (connection != null && !connection.isClosed()) {
				connection.close();
				System.out.println("Connection closed");
			}
			if (statement != null && !statement.isClosed()) {
				statement.close();
				System.out.println("Connection closed");
			}
			if (resultSet != null && !resultSet.isClosed()) {
				resultSet.close();
				System.out.println("ResultSet closed");
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

