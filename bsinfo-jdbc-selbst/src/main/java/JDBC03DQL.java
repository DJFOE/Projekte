import java.sql.*;

public class JDBC03DQL {

	public static void main(String[] args) {
		System.out.println("Execute DML");
		System.out.println("====================");
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection connection = DriverManager.getConnection
					("jdbc:mysql://localhost:3306/demoDB", "guest", "Pa$$w0rd");
			
			if (connection != null) {
				System.out.println("Connection established!");
			} else {
				System.out.println("Could not establish connection!");
				return;
			}
			
			Statement statement = connection.createStatement();
			//1. Lassen Sie sich die Struktur der Tabelle emloyees anzeigen.
			String sql ="DESC employees";
			//a) ResultSet erzeugen
			ResultSet rs = statement.executeQuery(sql);
			//b) Metadaten ermitteln
			ResultSetMetaData meta = rs.getMetaData();
			//c) ResultSet ausgeben
			int columnCount = meta.getColumnCount();
			while (rs.next()) {
				for (int i = 1; i <= columnCount; ++i) {
					Object o = rs.getObject(i);
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
			if (rs != null && !rs.isClosed()) {
				rs.close();
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
	/*
	public static void printResultSet(ResultSet rs) {
		try {
			ResultSetMetaData meta = rs.getMetaData();
			int colmax = meta.getColumnCount();
			while (rs.next()) {
				for (int i = 1; i <= colmax; ++i) {
					Object o = rs.getObject(i);
					System.out.print(o == null ? "null" : o);
					System.out.print("\t");
				}
				System.out.println();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	} 
	*/
}

