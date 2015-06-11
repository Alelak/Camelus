package test;

import com.devsolutions.camelus.services.DBConnection;

public class DBConnectionTest {

	public void getConnection() {
		DBConnection.getSqlSessionFactory();
	}

	public static void main(String[] atrgs) {
		DBConnectionTest dbConnectionTest = new DBConnectionTest();
		dbConnectionTest.getConnection();
	}
}
