package coupon.tmp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CouponTables {

	public static void main(String[] args) {
		String sql = "Create Table Company (Id BIGINT primary key, COMP_NAME varchar (20), PASSWORD varchar (20), EMAIL varchar (20))";
		String url = "jdbc:derby://localhost:1527/cdb1;create=true";

		try (Connection con = DriverManager.getConnection(url);) {
			Statement stmn = con.createStatement();
			stmn.executeUpdate(sql);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		System.out.println(sql + " - DONE ");

	}

}
