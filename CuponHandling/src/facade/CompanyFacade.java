package facade;

import java.util.Date;
import java.util.Set;

import coupon.DAO.CompanyDBDAO;
import coupon.DAO.CouponDBDAO;
import coupon.JavaBeans.Company;
import coupon.JavaBeans.Coupon;
import coupon.JavaBeans.CouponType;
import coupon.exception.CouponSystemException;

public class CompanyFacade implements CouponClientFacade {

	private CouponDBDAO coupDBDAO = new CouponDBDAO();
	private CompanyDBDAO compDBDAO = new CompanyDBDAO();
	Company company = new Company();

	public CompanyFacade() {
	}

	@Override
	public boolean login(String name, String password, ClientType type) throws CouponSystemException {
		if (type.name().contentEquals("Company") && compDBDAO.Login(name, password)) {
			this.company=compDBDAO.LoginComp(name);
			return true;
		} else {
			return false;
		}
	}

	

	public void createCoupon(Coupon coupon) throws CouponSystemException {
		if (coupDBDAO.checkDuplicateName(coupon)) {
			throw new CouponSystemException("Coupon with same title exists, please choose a different title.");
		} else {
			try {
				coupDBDAO.createCoupon(coupon);
				compDBDAO.createCompCoup(this.company.getId(), coupon.getId());
			} catch (CouponSystemException e) {
			}
		}
	}

	public void removeCoupon(Coupon newCoupon) {
		try {
			coupDBDAO.removeCoupon(newCoupon);
		} catch (CouponSystemException e) {
		}
	}

	public void updateCoupon(Coupon newCoupon) {
		try {
			coupDBDAO.updateCouponDandP(newCoupon);
		} catch (CouponSystemException e) {
		}
	}

	public Coupon getCoupon(long id) {

		Coupon coup = null;
		try {
			coup = coupDBDAO.getCoupon(id);
		} catch (CouponSystemException e) {
		}
		return coup;
	}
	
	public Set<Coupon> getCompanyCoupon(){
		Set<Coupon> coup = null;
		try {
			coup = compDBDAO.getCoupons(this.company);
		} catch (CouponSystemException e) {
		}
		return coup;
	}
	
	

	public Set<Coupon> getCouponByType(CouponType type) {
		Set<Coupon> coupons = null;
		try {
			coupons = compDBDAO.getCouponsByType(this.company, type);
		} catch (CouponSystemException e) {
		}
		return coupons;
	}

	public Set<Coupon> getCouponByPrice(Double price) {
		Set<Coupon> coupons = null;
		try {
			coupons = compDBDAO.getCouponsByTopPrice(this.company, price);
		} catch (CouponSystemException e) {
		}
		return coupons;
	}

	public Set<Coupon> getCouponsUpToDate(Date date) {
		Set<Coupon> upToDate = null;
		try {
			upToDate = compDBDAO.getCouponsUpToEndDate(this.company, date);
		} catch (CouponSystemException e) {
		}
		return upToDate;
	}

}
