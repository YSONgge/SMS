/**
 * 
 */
package db;

/**
 * @author yeye
 *
 */

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionMysql {
	private static final String DRIVR = "com.mysql.jdbc.Driver";
	//private static final String IP = "192.168.1.101";
	private static final String IP = "localhost";

	private static final String DBNAME = "MySQL";
	private static final String PORT = "3306";
	private static final String URL = "jdbc:mysql://" + IP + ":" + PORT
			+ "/" + DBNAME;
	private static final String USER = "root";
	private static final String PASSWORD = "jiushimysql";
	
	public Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName(DRIVR);
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			System.out.println("Get Connection");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	/**
	 * Test Mod
	 * @param args
	 */
	public static void main(String[] args) {
		new ConnectionMysql().getConnection();
	}
}
