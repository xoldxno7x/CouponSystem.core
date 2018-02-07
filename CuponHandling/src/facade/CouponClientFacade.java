package facade;

import coupon.exception.CouponSystemException;

public interface CouponClientFacade {

	public boolean login (String name, String password, ClientType type) throws CouponSystemException;
}
