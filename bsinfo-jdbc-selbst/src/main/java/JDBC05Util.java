import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JDBC05Util {

	private static Connection connection = null;

	public static Connection getConnection(final String db) throws SQLException, ClassNotFoundException, IOException {
		
		//1. Implementieren Sie eine Methode, die ein Connectionobjekt zur�ckgibt.
		//Die Verbindungsdaten werden aus der Propertiesdatei gelesen.
		//Die Methode soll im Singletonpattern implementiert werden.
		if (connection == null) {
			final Properties prop = new Properties();
			prop.load(new FileReader(db + ".properties"));
			final String driver = prop.getProperty("DBDRIVER");
			final String url = prop.getProperty("DBURL");
			final String user = prop.getProperty("DBUSER");
			final String password = prop.getProperty("DBPASSWORD");
			Class.forName(driver);
			return connection = DriverManager.getConnection(url, user, password);
		}

		return connection;

	}

	public static void close(final AutoCloseable obj) {
		
		//1. Implementieren Sie eine Methode, die jedes Objekt, dass das AutoCloseable Interface implementiert
		//schlie�en kann.
		if (obj != null) {
			try {
				obj.close();
			} catch (final Exception e) {
				// ignore
			}
		}
	}
}
