package coupon.DAO;

import java.util.Set;

import coupon.JavaBeans.Coupon;
import coupon.JavaBeans.Customer;
import coupon.exception.CouponSystemException;

public interface CustomerDAO {

	public void createCustomer(Customer newCustomer) throws CouponSystemException ;

	public void removeCustomer(Customer newCustomer) throws CouponSystemException ;

	public void updateCustomer(Customer newCustomer) throws CouponSystemException ;

	public Customer getCustomer(long id) throws CouponSystemException ;
//might return Companies - need to check.
	public Set<Customer> getAllCustomer() throws CouponSystemException ;
	
	public Set<Coupon> getCoupons (Customer newCustomer) throws CouponSystemException ;

	public boolean Login(String custName, String password) throws CouponSystemException ;

}
