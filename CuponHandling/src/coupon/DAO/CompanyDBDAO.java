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
import coupon.JavaBeans.Company;
import coupon.JavaBeans.Coupon;
import coupon.JavaBeans.CouponType;
import coupon.exception.CouponSystemException;

public class CompanyDBDAO implements CompanyDAO {
	
	

	private static ConnectionPool pool = ConnectionPool.getInstance();

	@Override
	public void createCompany(Company newCompany) throws CouponSystemException {
		String sql = "INSERT INTO Company VALUES(?,?,?,?)";
		Connection con = pool.getConnection();

		try {

			PreparedStatement pstmt = con.prepareStatement(sql);

			pstmt.setLong(1, newCompany.getId());
			pstmt.setString(2, newCompany.getCompanyName());
			pstmt.setString(3, newCompany.getPassword());
			pstmt.setString(4, newCompany.getEmail());

			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CouponSystemException(
					"Unable to create new company, make sure you are typing in the correct information.");
		} finally {
			pool.returnConnection(con);

		}

	}

	@Override
	public void removeCompany(Company newCompany) throws CouponSystemException {
		String sql = "DELETE FROM Company WHERE Id =" + newCompany.getId();
		String sql2 = "DELETE FROM Company_Coupon WHERE COMP_Id =" + newCompany.getId();

		Connection con = pool.getConnection();
		try {

			getAllCompanyCouponsFromCust(newCompany.getId());

			Statement stmt = con.createStatement();
			stmt.executeUpdate(sql);
			stmt.executeUpdate(sql2);

		} catch (SQLException e) {
			throw new CouponSystemException("Unable to remove company with Id:" + newCompany.getId() + ",Make sure "
					+ "you are typing in the correct information.");
		} finally {
			pool.returnConnection(con);

		}

	}

	@Override
	public void updateCompany(Company newCompany) throws CouponSystemException {
		String sql = "update Company set COMP_NAME = ?, PASSWORD = ?, EMAIL =? WHERE Id = ?";
		Connection con = pool.getConnection();

		try {

			PreparedStatement pstmt = con.prepareStatement(sql);

			pstmt.setString(1, newCompany.getCompanyName());
			pstmt.setString(2, newCompany.getPassword());
			pstmt.setString(3, newCompany.getEmail());
			pstmt.setLong(4, newCompany.getId());

			pstmt.executeUpdate();
		} catch (SQLException e) {

			throw new CouponSystemException("Unable to update comapny with Id:" + newCompany.getId()
					+ ", make sure you are typing in the correct information.");
		} finally {
			pool.returnConnection(con);
		}

	}

	public void updateCompanyByName(Company newCompany) throws CouponSystemException {
		String sql = "update Company set Id = ?, PASSWORD = ?, EMAIL =? WHERE COMP_NAME = ?";
		Connection con = pool.getConnection();

		try {

			PreparedStatement pstmt = con.prepareStatement(sql);

			pstmt.setLong(1, newCompany.getId());
			pstmt.setString(2, newCompany.getPassword());
			pstmt.setString(3, newCompany.getEmail());
			pstmt.setString(4, newCompany.getCompanyName());

			pstmt.executeUpdate();
		} catch (SQLException e) {

			throw new CouponSystemException("Unable to update comapny with Id:" + newCompany.getId()
					+ ", make sure you are typing in the correct information.");
		} finally {
			pool.returnConnection(con);
		}

	}

	@Override
	public Company getCompany(long id) throws CouponSystemException {
		Connection con = pool.getConnection();
		String sql = "select * from Company where Id =" + id;
		Company comp = null;
		try {
			ResultSet rs = con.createStatement().executeQuery(sql);
			while (rs.next()) {
				comp = new Company();
				comp.setId(rs.getLong("Id"));
				comp.setCompanyName(rs.getString("COMP_NAME"));
				comp.setPassword(rs.getString("PASSWORD"));
				comp.setEmail(rs.getString("EMAIL"));
			}

		} catch (SQLException e) {
			throw new CouponSystemException(
					"Unable to find company with Id:" + id + ", make sure your are using the correct Id.");
		} finally {
			pool.returnConnection(con);
		}
		return comp;
	}

	@Override
	public Set<Company> getAllCompanies() throws CouponSystemException {
		Connection con = pool.getConnection();
		String sql = "select * from Company";
		Set<Company> com = new HashSet<>();
		Company comp = null;
		try {
			ResultSet rs = con.createStatement().executeQuery(sql);
			while (rs.next()) {
				comp = new Company();
				comp.setId(rs.getLong("Id"));
				comp.setCompanyName(rs.getString("COMP_NAME"));
				comp.setPassword(rs.getString("PASSWORD"));
				comp.setEmail(rs.getString("EMAIL"));
				com.add(comp);
			}

		} catch (SQLException e) {
			throw new CouponSystemException("Unable to get the company list, list might be empty.");
		} finally {
			pool.returnConnection(con);
		}
		return com;

	}

	public Set<Coupon> getAllCompanyCouponsFromCust(long id) throws CouponSystemException {
		Connection con = pool.getConnection();
		String sql = "select COUPON_ID from Company_Coupon where COMP_Id =" + id;
		Set<Coupon> coupons = new HashSet<>();
		Coupon coup = null;
		try {
			ResultSet rs = con.createStatement().executeQuery(sql);
			while (rs.next()) {
				coup = new Coupon();
				coup.setId(rs.getLong("COUPON_ID"));
				coupons.add(coup);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.returnConnection(con);
		}
		return coupons;

	}

	public void deleteComCoupfromCust(long id) throws CouponSystemException {
		Connection con = pool.getConnection();
		Set<Coupon> coupons = null;
		try {
			coupons = getAllCompanyCouponsFromCust(id);
			for (int i = 0; i < coupons.size(); i++) {
				long idC = coupons.iterator().next().getId();
				String sql2 = "DELETE FROM Customer_Coupon WHERE COUPON_Id =" + idC;
				Statement stmt = con.createStatement();
				stmt.executeUpdate(sql2);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.returnConnection(con);
		}

	}

	@Override
	public Set<Coupon> getCoupons(Company comp) throws CouponSystemException {
		Connection con = pool.getConnection();
		String sql = "select * from Coupon inner join Company_Coupon on Coupon.Id = COUPON_Id where COMP_Id ="
				+ comp.getId();
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

	public Set<Coupon> getCouponsByType(Company comp, CouponType type) throws CouponSystemException {
		Connection con = pool.getConnection();
		String sql = "select * from Coupon inner join Company_Coupon on Coupon.Id = COUPON_Id where TYPE = '"
				+ type.name() + "'" + "and COMP_Id =" + comp.getId();

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

	public Set<Coupon> getCouponsByTopPrice(Company comp, Double price) throws CouponSystemException {
		Connection con = pool.getConnection();
		String sql = "select * from Coupon inner join Company_Coupon on Coupon.Id = COUPON_Id where PRICE <= " + price
				+ "and COMP_Id =" + comp.getId();

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

	public Set<Coupon> getCouponsUpToEndDate(Company comp, Date date) throws CouponSystemException {
		Set<Coupon> allCoupons = getCoupons(comp);
		Set<Coupon> upToDate = new HashSet<>();
		for (int i = 0; i < allCoupons.size(); i++) {
			if (allCoupons.iterator().next().getEndDate().before(date)) {
				upToDate.add(allCoupons.iterator().next());
			}
		}
		if (upToDate.isEmpty()) {
			throw new CouponSystemException("No Coupons with selected end date avilalbe.");
		} else {
			return upToDate;

		}
	}

	@Override
	public boolean Login(String comptName, String password) throws CouponSystemException {
		Connection con = pool.getConnection();
		String sql = "select * from Company where COMP_NAME = '" + comptName + "' AND PASSWORD = '" + password + "'";

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

	public Company LoginComp(String compName) throws CouponSystemException {
		Connection con = pool.getConnection();
		String sql = "select * from Company where COMP_NAME = '" + compName +"'";
		Company comp = null;
		try {
			ResultSet rs = con.createStatement().executeQuery(sql);
			while (rs.next()) {
				comp = new Company();
				comp.setId(rs.getLong("Id"));
				comp.setCompanyName(rs.getString("COMP_NAME"));
				comp.setPassword(rs.getString("PASSWORD"));
				comp.setEmail(rs.getString("EMAIL"));
			}

		} catch (SQLException e) {
			throw new CouponSystemException(
					"Unable to find company :" + compName + ", make sure your are using the correct Id.");
		} finally {
			pool.returnConnection(con);
		}
		return comp;
	}

	public boolean checkDuplicateName(Company comp) {
		try {
			Set<Company> companies = new HashSet<>(getAllCompanies());
			for (int i = 0; i < companies.size(); i++) {
				if (companies.iterator().next().getCompanyName().toLowerCase()
						.equals(comp.getCompanyName().toLowerCase())) {
					return true;
				}
			}
		} catch (CouponSystemException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void createCompCoup(long compId, long coupId) throws CouponSystemException {
		Connection con = pool.getConnection();
		String sql2 = "INSERT INTO Company_Coupon VALUES (" + compId + "," + coupId + ")";
		try {
			Statement stmt = con.createStatement();
			stmt.executeUpdate(sql2);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.returnConnection(con);
		}

	}

}
