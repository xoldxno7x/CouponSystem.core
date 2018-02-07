package facade;

import coupon.DAO.CouponDBDAO;
import coupon.exception.CouponSystemException;

public class DailyCouponExpirationTask implements Runnable {

	public DailyCouponExpirationTask() {

	}

	private CouponDBDAO coup = new CouponDBDAO();
	private boolean quit = false;
	private final static long TIME = 1000 * 60 * 60 * 24;

	@Override
	public void run() {
		try {
			running();
		} catch (CouponSystemException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void running() throws CouponSystemException, InterruptedException {
		while (quit == false) {
			coup.removeExpiredCoupons();
			Thread.sleep(TIME);
		}
	}

	public void stopTask() {
		quit = true;
	}

}
