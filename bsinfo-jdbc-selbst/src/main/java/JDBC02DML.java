import java.sql.*;

public class JDBC02DML {

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

            //1. Erzeugen eines Datensatzes in der Tabelle employees
            Statement statement = connection.createStatement();
            String sql;
            sql = "INSERT INTO employees(lastName, firstName, email) VALUES ('Meier', 'Sahra', 'email@email.de')";
            int i = statement.executeUpdate(sql); //DML
            System.out.println("TODO" + " rows inserted: " + i);

            //2. Ändern Sie den Wert in der Spalte 'lastName'
            sql = "UPDATE employees SET lastName='Mayer' WHERE lastName='Meier'";
            int u = statement.executeUpdate(sql); //DML
            System.out.println("TODO" + " rows updated: " + u);

            //3. Löschen Sie den Mitarbeiter aus der Tabelle
            sql = "DELETE FROM employees WHERE employeeID=5";
            int d = statement.executeUpdate(sql); //DML
            System.out.println("TODO" + " row deleted: " + d);

            //Schließen der Connection
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connection closed");
            }
            //Schließen des Statements
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
