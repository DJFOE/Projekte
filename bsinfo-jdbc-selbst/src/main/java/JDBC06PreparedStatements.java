import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class JDBC06PreparedStatements {
	
	public static void main(final String[] args) {
		System.out.println("JDBC Prepared Statements");
		System.out.println("========================");
		
		Connection connection = null;
		PreparedStatement pst = null;
		
		try {
			// 1. Holen einer Connection
			connection = JDBC05Util.getConnection("demoDB");
			// 2. Prepared Statement erzeugen
			pst = connection.prepareStatement("SELECT * FROM employee WHERE lastName=? AND firstName=?");
			// 3. Werte zuweisen
			pst.setString(1, "Doe");
			pst.setString(2, "John");
			ResultSet resultSet = pst.executeQuery();
			// 4. Metadaten ermitteln
			ResultSetMetaData rsMetaData = resultSet.getMetaData();
					
			// 5. Resultset ausgeben
			
			int columnCount = rsMetaData.getColumnCount();
			while (resultSet.next()) {
				for (int i = 1; i <= columnCount; ++i) {
				Object o = resultSet.getObject(i);
				System.out.print(o == null ? "null" : o);
				System.out.print("\t");
				}
				System.out.println();
				}
		} catch (SQLException | ClassNotFoundException | IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			JDBC05Util.close(pst);
			JDBC05Util.close(connection);
		}
	}
}
