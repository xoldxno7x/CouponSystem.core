package coupon.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import connectionPool.ConnectionPool;
import coupon.JavaBeans.Coupon;
import coupon.JavaBeans.CouponType;
import coupon.exception.CouponSystemException;

public class CouponDBDAO implements CouponDAO {

	private static ConnectionPool pool = ConnectionPool.getInstance();

	public void createCoupon(Coupon newCoupon) throws CouponSystemException {

		String sql = "INSERT INTO Coupon VALUES(?,?,?,?,?,?,?,?,?)";
		Connection con = pool.getConnection();

		try {

			PreparedStatement pstmt = con.prepareStatement(sql);

			pstmt.setLong(1, newCoupon.getId());
			pstmt.setString(2, newCoupon.getTitle());
			pstmt.setDate(3, new java.sql.Date(newCoupon.getStartDate().getTime()));
			pstmt.setDate(4, new java.sql.Date(newCoupon.getEndDate().getTime()));
			pstmt.setInt(5, newCoupon.getAmount());
			pstmt.setString(6, newCoupon.getType().name());
			pstmt.setString(7, newCoupon.getMessage());
			pstmt.setDouble(8, newCoupon.getPrice());
			pstmt.setString(9, newCoupon.getImage());

			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new CouponSystemException(
					"Unable to create new coupon, make sure you are typing in the correct information.");
		} finally {
			pool.returnConnection(con);

		}

	}

	@Override
	public void removeCoupon(Coupon newCoupon) throws CouponSystemException {
		String sql = "DELETE FROM Coupon WHERE Id =" + newCoupon.getId();
		String sql2 = "DELETE FROM Customer_Coupon WHERE COUPON_Id =" + newCoupon.getId();
		String sql3 = "DELETE FROM Company_Coupon WHERE COUPON_Id =" + newCoupon.getId();

		try (Connection con = pool.getConnection();) {

			Statement stmt = con.createStatement();
			stmt.executeUpdate(sql);
			stmt.executeUpdate(sql2);
			stmt.executeUpdate(sql3);
			pool.returnConnection(con);

		} catch (SQLException e) {
			throw new CouponSystemException("Unable to remove coupon with Id:" + newCoupon.getId() + ",Make sure "
					+ "you are typing in the correct information.");
		}
	}

	@Override
	public void updateCoupon(Coupon newCoupon) throws CouponSystemException {
		String sql = "update Coupon set TITLE = ?, START_DATE = ?, END_DATE = ?, AMOUNT = ?, TYPE = ?, "
				+ " MESSAGE = ?, PRICE = ?, IMAGE = ?   WHERE Id = ?";
		Connection con = pool.getConnection();

		try {

			PreparedStatement pstmt = con.prepareStatement(sql);

			pstmt.setString(1, newCoupon.getTitle());
			pstmt.setDate(2, new java.sql.Date(newCoupon.getStartDate().getTime()));
			pstmt.setDate(3, new java.sql.Date(newCoupon.getEndDate().getTime()));
			pstmt.setInt(4, newCoupon.getAmount());
			pstmt.setString(5, newCoupon.getType().name());
			pstmt.setString(6, newCoupon.getMessage());
			pstmt.setDouble(7, newCoupon.getPrice());
			pstmt.setString(8, newCoupon.getImage());
			pstmt.setLong(9, newCoupon.getId());

			pstmt.executeUpdate();
		} catch (SQLException e) {

			throw new CouponSystemException("Unable to update coupon with Id:" + newCoupon.getId()
					+ ", make sure you are typing in the correct information.");
		} finally {
			pool.returnConnection(con);

		}

	}

	public void updateCouponDandP(Coupon newCoupon) throws CouponSystemException {
		String sql = "update Coupon set END_DATE = ?, PRICE = ? WHERE Id = ?";
		Connection con = pool.getConnection();

		try {

			PreparedStatement pstmt = con.prepareStatement(sql);

			pstmt.setDate(1, new java.sql.Date(newCoupon.getEndDate().getTime()));
			pstmt.setDouble(2, newCoupon.getPrice());
			pstmt.setLong(3, newCoupon.getId());

			pstmt.executeUpdate();
		} catch (SQLException e) {

			throw new CouponSystemException("Unable to update coupon with Id:" + newCoupon.getId()
					+ ", make sure you are typing in the correct information.");
		} finally {
			pool.returnConnection(con);

		}

	}

	@Override
	public Coupon getCoupon(long id) throws CouponSystemException {
		Connection con = pool.getConnection();
		String sql = "select * from Coupon where Id =" + id;
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
			}

		} catch (SQLException e) {
			throw new CouponSystemException(
					"Unable to find coupon with Id:" + id + ", make sure your are using the correct Id.");
		} finally {
			pool.returnConnection(con);
		}
		return coupon;

	}

	@Override
	public Set<Coupon> getAllCoupon() throws CouponSystemException {
		Connection con = pool.getConnection();
		String sql = "select * from Coupon";
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

	@Override
	public Set<Coupon> getCouponByType(CouponType type) throws CouponSystemException {
		Connection con = pool.getConnection();
		String sql = "select * from Coupon where TYPE = '" + type.name() + "'";
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
			throw new CouponSystemException("No coupons of type " + type.name() + " found, make sure"
					+ "you are using the correct information");
		} finally {
			pool.returnConnection(con);
		}
		return coup;
	}

	public Set<Coupon> removeAllCoupons() throws CouponSystemException {
		Set<Coupon> backUp = null;
		try {
			backUp = getAllCoupon();
		} catch (CouponSystemException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String sql = "DELETE FROM Coupon WHERE Id > 1 ";
		String sql2 = "DELETE FROM Customer_Coupon WHERE COUPON_Id > 1";
		String sql3 = "DELETE FROM Company_Coupon WHERE COUPON_Id > 1";

		try (Connection con = pool.getConnection();) {

			Statement stmt = con.createStatement();
			stmt.executeUpdate(sql);
			stmt.executeUpdate(sql2);
			stmt.executeUpdate(sql3);
			pool.returnConnection(con);

		} catch (SQLException e) {

			throw new CouponSystemException(
					"Unable to remove and backup all coupons, please conntect support for more information.");
		}
		return backUp;

	}

	public boolean checkDuplicateName(Coupon coup) {
		try {
			Set<Coupon> coupons = new HashSet<>(getAllCoupon());
			for (int i = 0; i < coupons.size(); i++) {
				if (coupons.iterator().next().getTitle().toLowerCase().equals(coup.getTitle().toLowerCase())) {
					return true;
				}
			}
		} catch (CouponSystemException e) {
			e.printStackTrace();
		}
		return false;
	}

	private Set<Coupon> selectExpiredCoupons() throws CouponSystemException {
		Connection con = pool.getConnection();
		String sql = "select * from Coupon where END_DATE < CURRENT_DATE";
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
			throw new CouponSystemException("No expired Coupons avilable");
		} finally {
			pool.returnConnection(con);
		}
		return coup;
	}

	public void removeExpiredCoupons() throws CouponSystemException {
		Set<Coupon> expiredCoupons = null;

		try {
			expiredCoupons = selectExpiredCoupons();
			for (int i = 0; i < expiredCoupons.size(); i++) {
				removeCoupon(expiredCoupons.iterator().next());
			}
		} catch (CouponSystemException e) {
			throw new CouponSystemException("Unable to remove all expired coupons.");
		}
	}

}
