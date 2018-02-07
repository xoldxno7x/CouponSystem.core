package connectionPool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ConnectionPool {

	private static final int MAX_CON = 10;
	private static ConnectionPool instance = null;
	private List<Connection> pool = new ArrayList<>();
	private List<Connection> conInUse = new ArrayList<>();
	private String url = "jdbc:derby://localhost:1527/cdb1;";

	// Get instance for singleton
	public static ConnectionPool getInstance() {
		if (instance == null) {
			instance = new ConnectionPool();
		}
		return instance;
	}

	// Private CTOR + Initialization
	private ConnectionPool() {
		initialize();
	}

	// Initialization - Loading driver, creating connection, filling con pool set.
	private void initialize() {
		
		try {
			Class.forName("org.apache.derby.jdbc.ClientDriver");
			Connection con = DriverManager.getConnection(url);
			while (pool.size() < MAX_CON) {
				pool.add(con);
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
//if the pool is not empty provide and open connection from con set, else wait for avilable cons
	public synchronized Connection getConnection() {
		while (pool.size() == 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
		Connection con = pool.iterator().next();
		pool.remove(con);
		conInUse.add(con);
		notifyAll();
		return con;

	}

	//return cons 
	public synchronized void returnConnection(Connection con) {
		while (pool.size() == MAX_CON) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			pool.add(con);
			conInUse.remove(con);
			notifyAll();

		}
	}

	//close all connections that are not in use
	public void closeConnections() {
		for(int i = 0; i<pool.size();i++) {
			Connection con = pool.iterator().next();
			try {
				con.close();
				emergencyCloseConnections();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	//close all connections that are in use
	public void emergencyCloseConnections() {
		for(int i = 0; i<conInUse.size();i++) {
			Connection con = conInUse.iterator().next();
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	//count open connections
	public int countOpenCon() {
		int count = conInUse.size();
		return count;
	}

}