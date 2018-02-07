package coupon.DAO;

import java.util.Set;

import coupon.JavaBeans.Coupon;
import coupon.JavaBeans.CouponType;
import coupon.exception.CouponSystemException;

public interface CouponDAO {

	public void createCoupon(Coupon newCoupon) throws CouponSystemException;

	public void removeCoupon(Coupon newCoupon)throws CouponSystemException;

	public void updateCoupon(Coupon newCoupon)throws CouponSystemException;

	public Coupon getCoupon(long id)throws CouponSystemException;

	public Set<Coupon> getAllCoupon()throws CouponSystemException;

	public Set<Coupon> getCouponByType(CouponType type)throws CouponSystemException;

}
