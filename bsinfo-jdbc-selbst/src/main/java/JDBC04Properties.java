import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JDBC04Properties {

	public static void main(String[] args) {
		System.out.println("Connection with Properties");
		System.out.println("==========================");
		try {
			Properties properties = new Properties();
			properties.load(new FileInputStream(".idea/credentials.properties"));
			String dbDriver = properties.getProperty("DBDRIVER");
			String dbUrl = properties.getProperty("DBURL");
			String dbUser = properties.getProperty("DBUSER");
			String dbPassword = properties.getProperty("DBPASSWORD");

			Class.forName(dbDriver);
			Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
			
			if (connection != null) {
				System.out.println("Connection established!");
			} else {
				System.out.println("Could not establish connection!");
				return;
			}
						
			if (connection != null && !connection.isClosed()) {
				connection.close();
				System.out.println("Connection closed");
			}

		} catch (ClassNotFoundException e) {
			System.out.println("MySQL JDBC-Treiber not found!");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("You have an SQL Syntax error!");
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			System.out.println("No such file!");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Could not read file!");
			e.printStackTrace();
		}
	}
}
