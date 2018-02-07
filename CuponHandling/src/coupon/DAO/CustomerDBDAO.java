package coupon.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import connectionPool.ConnectionPool;
import coupon.JavaBeans.Coupon;
import coupon.JavaBeans.CouponType;
import coupon.JavaBeans.Customer;
import coupon.exception.CouponSystemException;

public class CustomerDBDAO implements CustomerDAO {

	private static ConnectionPool pool = ConnectionPool.getInstance();

	@Override
	public void createCustomer(Customer newCustomer) throws CouponSystemException {

		String sql = "INSERT INTO Customer VALUES(?,?,?)";
		Connection con = pool.getConnection();

		try {

			PreparedStatement pstmt = con.prepareStatement(sql);

			pstmt.setLong(1, newCustomer.getId());
			pstmt.setString(2, newCustomer.getCustName());
			pstmt.setString(3, newCustomer.getPassword());

			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new CouponSystemException(
					"Unable to create new customer, make sure you are typing in the correct information.");
		} finally {
			pool.returnConnection(con);

		}

	}

	@Override
	public void removeCustomer(Customer newCustomer) throws CouponSystemException {

		String sql = "DELETE FROM Customer WHERE Id =" + newCustomer.getId();
		String sql2 = "DELETE FROM Customer_Coupon WHERE CUST_Id =" + newCustomer.getId();

		Connection con = pool.getConnection();
		try {

			Statement stmt = con.createStatement();
			stmt.executeUpdate(sql);
			stmt.executeUpdate(sql2);

		} catch (SQLException e) {
			throw new CouponSystemException("Unable to remove customer with Id:" + newCustomer.getId() + ",Make sure "
					+ "you are typing in the correct information.");
		} finally {
			pool.returnConnection(con);

		}

	}

	@Override
	public void updateCustomer(Customer newCustomer) throws CouponSystemException {

		String sql = "update Customer set CUST_NAME = ?, PASSWORD = ? WHERE Id = ?";
		Connection con = pool.getConnection();

		try {

			PreparedStatement pstmt = con.prepareStatement(sql);

			pstmt.setString(1, newCustomer.getCustName());
			pstmt.setString(2, newCustomer.getPassword());
			pstmt.setLong(3, newCustomer.getId());

			pstmt.executeUpdate();
		} catch (SQLException e) {

			throw new CouponSystemException("Unable to update customer with Id:" + newCustomer.getId()
					+ ", make sure you are typing in the correct information.");
		} finally {
			pool.returnConnection(con);
		}
	}

	public void updateCustomerByName(Customer newCustomer) throws CouponSystemException {
		String sql = "update Customer set Id = ?, PASSWORD = ? WHERE CUST_NAME = ?";
		Connection con = pool.getConnection();

		try {

			PreparedStatement pstmt = con.prepareStatement(sql);

			pstmt.setLong(1, newCustomer.getId());
			pstmt.setString(2, newCustomer.getPassword());
			pstmt.setString(3, newCustomer.getCustName());

			pstmt.executeUpdate();
		} catch (SQLException e) {

			throw new CouponSystemException("Unable to update customer with Id:" + newCustomer.getId()
					+ ", make sure you are typing in the correct information.");
		} finally {
			pool.returnConnection(con);
		}

	}

	@Override
	public Customer getCustomer(long id) throws CouponSystemException {
		Connection con = pool.getConnection();
		String sql = "select * from Customer where Id =" + id;
		Customer cus = null;
		try {
			ResultSet rs = con.createStatement().executeQuery(sql);
			while (rs.next()) {
				cus = new Customer();
				cus.setId(rs.getLong("Id"));
				cus.setCustName(rs.getString("CUST_NAME"));
				cus.setPassword(rs.getString("PASSWORD"));
			}

		} catch (SQLException e) {
			throw new CouponSystemException(
					"Unable to find customer with Id:" + id + ", make sure your are using the correct Id.");
		} finally {
			pool.returnConnection(con);
		}
		return cus;

	}

	@Override
	public Set<Customer> getAllCustomer() throws CouponSystemException {

		Connection con = pool.getConnection();
		String sql = "select * from Customer";
		Set<Customer> cus = new HashSet<>();
		Customer customer = null;
		try {
			ResultSet rs = con.createStatement().executeQuery(sql);
			while (rs.next()) {
				customer = new Customer();
				customer.setId(rs.getLong("Id"));
				customer.setCustName(rs.getString("CUST_NAME"));
				customer.setPassword(rs.getString("PASSWORD"));
				cus.add(customer);

			}

		} catch (SQLException e) {
			throw new CouponSystemException("Unable to get the customer list, list might be empty.");
		} finally {
			pool.returnConnection(con);
		}
		return cus;
	}

	@Override
	public boolean Login(String custName, String password) throws CouponSystemException {
		Connection con = pool.getConnection();
		String sql = "select * from Customer where CUST_NAME = '" + custName + "' AND PASSWORD = '" + password + "'";

		try {
			ResultSet rs = con.createStatement().executeQuery(sql);
			if (rs.next()) {
				return true;
			} else {
				return false;
			}

		} catch (SQLException e) {
			throw new CouponSystemException("Failed to login, make sure you are using the correct login information.");
		} finally {
			pool.returnConnection(con);
		}

	}

	public Customer LoginCust(String custName) throws CouponSystemException {
	
			Connection con = pool.getConnection();
			String sql = "select * from Customer where CUST_NAME = '" + custName +"'";
			Customer cus = null;
			try {
				ResultSet rs = con.createStatement().executeQuery(sql);
				while (rs.next()) {
					cus = new Customer();
					cus.setId(rs.getLong("Id"));
					cus.setCustName(rs.getString("CUST_NAME"));
					cus.setPassword(rs.getString("PASSWORD"));
				}

			} catch (SQLException e) {
				throw new CouponSystemException(
						"Unable to find customer :" + custName+ ", make sure your are using the correct Id.");
			} finally {
				pool.returnConnection(con);
			}
			return cus;
		}

	@Override
	public Set<Coupon> getCoupons(Customer cust) throws CouponSystemException {
		Connection con = pool.getConnection();
		String sql = "select * from Coupon inner join Customer_Coupon on Coupon.Id = COUPON_Id where CUST_Id ="
				+ cust.getId();
		Set<Coupon> coup = new HashSet<>();
		Coupon coupon = null;
		try {
			ResultSet rs = con.createStatement().executeQuery(sql);
			while (rs.next()) {
				coupon = new Coupon();
				coupon.setId(rs.getLong("Id"));
				coupon.setTitle(rs.getString("TITLE"));
				coupon.setStartDate(rs.getDate("START_DATE"));
				coupon.setEndDate(rs.getDate("END_DATE"));
				coupon.setAmount(rs.getInt("AMOUNT"));
				coupon.setType(CouponType.valueOf(rs.getString("TYPE")));
				coupon.setMessage(rs.getString("MESSAGE"));
				coupon.setPrice(rs.getDouble("PRICE"));
				coupon.setImage(rs.getString("IMAGE"));
				coup.add(coupon);
			}

		} catch (SQLException e) {
			throw new CouponSystemException("Unable to get the coupon list, list might be empty.");
		} finally {
			pool.returnConnection(con);
		}
		return coup;

	}

	public boolean checkDuplicateCustomer(Customer cust) {
		try {
			Set<Customer> customers = new HashSet<>(getAllCustomer());
			for (int i = 0; i < customers.size(); i++) {
				if (customers.iterator().next().getCustName().toLowerCase().equals(cust.getCustName().toLowerCase())) {
					return true;
				}
			}
		} catch (CouponSystemException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean checkPreviouslyPurchasedCuopon(Customer cust, Coupon coupon) {
		try {
			Set<Coupon> coup = new HashSet<>(getCoupons(cust));
			for (int i = 0; i < coup.size(); i++) {
				if (coup.iterator().next().getTitle().toLowerCase().equals(coupon.getTitle().toLowerCase())) {
					return true;
				}
			}
		} catch (CouponSystemException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean checkIfCouponAvilable(Coupon coup) throws CouponSystemException {
		CouponDBDAO coupDBDAO = new CouponDBDAO();
		try {
			Set<Coupon> coupons = new HashSet<>(coupDBDAO.getAllCoupon());
			for (int i = 0; i < coupons.size(); i++) {
				if (coupons.iterator().next().getAmount() == 0
						&& coupons.iterator().next().getTitle().toLowerCase().equals(coup.getTitle().toLowerCase())) {
					return false;
				}
			}
		} catch (CouponSystemException e) {
			throw new CouponSystemException(
					"Unable to check amount of given coupon, make sure you are using the correct information");
		}

		return true;
	}

	public boolean checkCouponExpiry(Coupon coup) throws CouponSystemException {
		CouponDBDAO coupons = new CouponDBDAO();
		if (coupons.getCoupon(coup.getId()).getEndDate().before(new Date())) {
			return true;
		} else {
			return false;
		}

	}

	public void createNewCoupon(long custId, long coupId) throws CouponSystemException, SQLException {
		Connection con = pool.getConnection();
		String sql = "INSERT INTO Customer_Coupon VALUES (" + custId + "," + coupId + ")";
		try {
			Statement stmt = con.createStatement();
			stmt.executeUpdate(sql);

		} catch (SQLException e) {
			throw new CouponSystemException(
					"Unable to create new customer coupon, make sure you are using the correct info.");
		} finally {
			pool.returnConnection(con);
		}
	}

	public void purchaseCoupon(Coupon coup, Customer cust) throws CouponSystemException {
		Connection con = pool.getConnection();
		String sql = "update Coupon set AMOUNT = AMOUNT -1 where Id =" + coup.getId();

		boolean verification = true;
		if (checkPreviouslyPurchasedCuopon(cust, coup)) {
			verification = false;
			throw new CouponSystemException("Could not complete purchase, coupon has already been purchased.");
		}
		if (checkIfCouponAvilable(coup)) {
			verification = true;
		} else {
			verification = false;
			throw new CouponSystemException("Could not complete purchase, coupon is out of stock.");
		}
		if (checkCouponExpiry(coup)) {
			verification = false;
			throw new CouponSystemException("Could not complete purchase, coupon is expired.");
		} else {
			verification = true;
		}

		if (verification == true) {
			try {
				Statement stmt = con.createStatement();
				stmt.executeUpdate(sql);
				createNewCoupon(cust.getId(), coup.getId());
			} catch (SQLException e) {
			}
		}
	}

	public Set<Coupon> getCouponsByTopPrice(Customer cust, Double price) throws CouponSystemException {
		Connection con = pool.getConnection();
		String sql = "select * from Coupon inner join Customer_Coupon on Coupon.Id = COUPON_Id where PRICE <= " + price
				+ "and CUST_Id =" + cust.getId();

		Set<Coupon> coupons = new HashSet<>();
		Coupon coupon = null;
		try {
			ResultSet rs = con.createStatement().executeQuery(sql);
			while (rs.next()) {
				coupon = new Coupon();
				coupon.setId(rs.getLong("Id"));
				coupon.setTitle(rs.getString("TITLE"));
				coupon.setStartDate(rs.getDate("START_DATE"));
				coupon.setEndDate(rs.getDate("END_DATE"));
				coupon.setAmount(rs.getInt("AMOUNT"));
				coupon.setType(CouponType.valueOf(rs.getString("TYPE")));
				coupon.setMessage(rs.getString("MESSAGE"));
				coupon.setPrice(rs.getDouble("PRICE"));
				coupon.setImage(rs.getString("IMAGE"));
				coupons.add(coupon);
			}

		} catch (SQLException e) {
			throw new CouponSystemException("Unable to get the coupon list, list might be empty.");
		} finally {
			pool.returnConnection(con);
		}
		return coupons;

	}

	public Set<Coupon> getCouponsByType(Customer cust, CouponType type) throws CouponSystemException {
		Connection con = pool.getConnection();
		String sql = "select * from Coupon inner join Customer_Coupon on Coupon.Id = COUPON_Id where TYPE = '"
				+ type.name() + "'" + "and CUST_Id =" + cust.getId();

		Set<Coupon> coup = new HashSet<>();
		Coupon coupon = null;
		try {
			ResultSet rs = con.createStatement().executeQuery(sql);
			while (rs.next()) {
				coupon = new Coupon();
				coupon.setId(rs.getLong("Id"));
				coupon.setTitle(rs.getString("TITLE"));
				coupon.setStartDate(rs.getDate("START_DATE"));
				coupon.setEndDate(rs.getDate("END_DATE"));
				coupon.setAmount(rs.getInt("AMOUNT"));
				coupon.setType(CouponType.valueOf(rs.getString("TYPE")));
				coupon.setMessage(rs.getString("MESSAGE"));
				coupon.setPrice(rs.getDouble("PRICE"));
				coupon.setImage(rs.getString("IMAGE"));
				coup.add(coupon);
			}

		} catch (SQLException e) {
			throw new CouponSystemException("Unable to get the coupon list, list might be empty.");
		} finally {
			pool.returnConnection(con);
		}
		return coup;

	}

}
