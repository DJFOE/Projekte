package org.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBC01Connect {

    public static void main(String[] args) {
        System.out.println("JDBC Connection Test");
        System.out.println("====================");

        try {
            // 1. Laden des JDBC Treibers für mariaDB
            Class.forName("com.mysql.cj.jdbc.Driver");
            // jar-Archiv muss im CLASSPATH eingetragen sein.
            /*
             * TODO
             */
            System.out.println("Driver sucessfully loaded!");
            Connection connection = DriverManager.getConnection
                    ("jdbc:mysql://localhost:3306/demoDB", "guest", "Pa$$w0rd");
            System.out.println("Connection established");
            if (connection != null) {
                System.out.println("Connection established!");
            } else {
                System.out.println("Could not establish connection!");
                return;
            }
            // 4. Schließen der Connection
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connection closed");
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
