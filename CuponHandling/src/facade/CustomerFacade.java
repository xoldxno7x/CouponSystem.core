package facade;

import java.util.Set;

import coupon.DAO.CouponDBDAO;
import coupon.DAO.CustomerDBDAO;
import coupon.JavaBeans.Coupon;
import coupon.JavaBeans.CouponType;
import coupon.JavaBeans.Customer;
import coupon.exception.CouponSystemException;

public class CustomerFacade implements CouponClientFacade {

	Customer cust = new Customer();
	private CustomerDBDAO custDBDAO = new CustomerDBDAO();
    private CouponDBDAO coupDBDAO = new CouponDBDAO();
	
	public CustomerFacade() {
	}

	@Override
	public boolean login(String name, String password, ClientType type) throws CouponSystemException {
		if (type.name().contentEquals("Customer") && custDBDAO.Login(name, password)) {
			this.cust = custDBDAO.LoginCust(name);
			return true;
		} else {
			return false;
		}
	}
	
	public void purchaseNewCoupon(Coupon coup) {
	try {
		custDBDAO.purchaseCoupon(coup, this.cust);
	} catch (CouponSystemException e) {
	}
	}
	
	public Set<Coupon> browsePurchaseHistory(){
		Set<Coupon> history = null;
		try {
			history = custDBDAO.getCoupons(this.cust);
		} catch (CouponSystemException e) {
		}
		return history;
	}
	
	public Set<Coupon> browsePurchaseHistoryByPrice(Double price){
		Set<Coupon> history = null;
		try {
			history = custDBDAO.getCouponsByTopPrice(this.cust, price);
		} catch (CouponSystemException e) {
		}
		return history;
	}
	
	public Set<Coupon> browsePurchaseHistoryByType(CouponType type){
		Set<Coupon> history = null;
		try {
			history = custDBDAO.getCouponsByType(this.cust, type);
		} catch (CouponSystemException e) {
		}
		return history;
	}
	
	public Set<Coupon> viewAllCoupons(){
		Set<Coupon> coup = null;
		try {
			coup = coupDBDAO.getAllCoupon();
		} catch (CouponSystemException e) {
		}
		return coup;
	}
	
	
	
	

}
