package facade;

import java.util.Set;

import coupon.DAO.CompanyDBDAO;
import coupon.DAO.CustomerDBDAO;
import coupon.JavaBeans.Company;
import coupon.JavaBeans.Customer;
import coupon.exception.CouponSystemException;

public class AdminFacade implements CouponClientFacade {
	 CompanyDBDAO compDBDAO = new CompanyDBDAO();
	 CustomerDBDAO custDBDAO = new CustomerDBDAO();

	public AdminFacade() {
	}
	
	@Override
	public boolean login(String name, String password, ClientType type) throws CouponSystemException {
		if (type.name().contentEquals("Admin") && name.equals("admin") && password.equals("1234")) {
			return true;
		}else {
			return false;
		}
	}

	public void createCompany(Company company) throws CouponSystemException {
		if (compDBDAO.checkDuplicateName(company)) {
			throw new CouponSystemException(
					"Company name is taken, please chose a different name.");
		} else {
			try {
				compDBDAO.createCompany(company);
			} catch (CouponSystemException e) {
			}
		}
	}

	public void removeCompany(Company company) {
		try {
			compDBDAO.removeCompany(company);
		} catch (CouponSystemException e) {
		}
	}

	public void updateCompany(Company company) {
		try {
			compDBDAO.updateCompanyByName(company);
		} catch (CouponSystemException e) {
		}
	}

	public Company getCompany(long id){
		try {
			return compDBDAO.getCompany(id);
		} catch (CouponSystemException e) {
		}
		return null;
	}

	public Set<Company> getAllCompanies(){
		Set<Company> companies = null;
		try {
			companies = compDBDAO.getAllCompanies();
		} catch (CouponSystemException e) {
		}
		return companies;
	}

	public void createCustomer(Customer costumer) throws CouponSystemException {
		if (custDBDAO.checkDuplicateCustomer(costumer)) {
			throw new CouponSystemException(
					"Customer name is taken, please chose a different name.");
		} else {
			try {
				custDBDAO.createCustomer(costumer);
			} catch (CouponSystemException e) {
				
			}
		}
	}

	public void removeCustomer(Customer costumer) {
		try {
			custDBDAO.removeCustomer(costumer);
		} catch (CouponSystemException e) {
		}
	}

	public void updateCustomer(Customer costumer){
		try {
			custDBDAO.updateCustomerByName(costumer);
		} catch (CouponSystemException e) {
		}
	}

	public Customer getCustomer(long id){
		try {
			return custDBDAO.getCustomer(id);
		} catch (CouponSystemException e) {
		}
		return null;
	}

	public Set<Customer> getAllCustomers() {
	Set<Customer> customers = null;
	try {
		customers = custDBDAO.getAllCustomer();
	} catch (CouponSystemException e) {
	}
		return customers;
	}


}
