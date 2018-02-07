package coupon.tmp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CouponTables4 {

	public static void main(String[] args) {
		
		String sql = "Create Table Customer_Coupon (CUST_Id BIGINT, COUPON_Id BIGINT, PRIMARY KEY (CUST_Id,COUPON_Id) )";
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
