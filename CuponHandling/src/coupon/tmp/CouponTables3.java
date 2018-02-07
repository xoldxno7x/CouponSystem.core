package coupon.tmp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CouponTables3 {

	public static void main(String[] args) {
		
		String sql = "Create Table Coupon (Id BIGINT primary key, TITLE varchar (20), START_DATE date, END_DATE date,AMOUNT integer, TYPE varchar(20), MESSAGE varchar(20), PRICE float, IMAGE varchar(100))";
		String url = "jdbc:derby://localhost:1527/cdb1;";
		
		try (Connection con = DriverManager.getConnection(url);){
			Statement stmn = con.createStatement();
			stmn.executeUpdate(sql);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		System.out.println(sql +" - DONE ");

	}

}
