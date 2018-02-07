package facade;

import connectionPool.ConnectionPool;
import coupon.exception.CouponSystemException;

public class CouponSystem {

	private static CouponSystem instance = null;
	private AdminFacade admin = new AdminFacade();
	private CompanyFacade company = new CompanyFacade();
	private CustomerFacade customer = new CustomerFacade();

	private CouponSystem() {
		
	}

	public static final CouponSystem getInstance() {
		if (instance == null) {
			instance = new CouponSystem();
		}
		return instance;
	}

	public CouponClientFacade login(String name, String password, ClientType type) {

		boolean valid = false;

		switch (type.name()) {
		case "Customer":
			try {
				valid = customer.login(name, password, type);
			} catch (CouponSystemException e) {
			}
			if (valid) {
				return customer;
			}

			break;

		case "Company":
			try {
				valid = company.login(name, password, type);
			} catch (CouponSystemException e) {
			}

			if (valid) {
				return company;
			}

		case "Admin":
			try {
				valid = admin.login(name, password, type);
			} catch (CouponSystemException e) {
			}

			if (valid) {
				return admin;
			}

		default:
			break;

		}
		return null;

	}
	

	public void shutdown() {
		ConnectionPool pool = ConnectionPool.getInstance();
		pool.closeConnections();
	}

}
