package coupon.DAO;

import java.util.Set;

import coupon.JavaBeans.Company;
import coupon.JavaBeans.Coupon;
import coupon.exception.CouponSystemException;

public interface CompanyDAO {

	public void createCompany(Company newCompany) throws CouponSystemException;

	public void removeCompany(Company newCompany) throws CouponSystemException;

	public void updateCompany(Company newCompany) throws CouponSystemException;

	public Company getCompany(long id) throws CouponSystemException;

	public Set<Company> getAllCompanies() throws CouponSystemException;

	public Set<Coupon> getCoupons(Company comp) throws CouponSystemException;

	public boolean Login(String comptName, String password) throws CouponSystemException;

}
