package ejbProject;

import java.util.List;

import javax.ejb.Local;

@Local
public interface IncomeDAO {
	
public Income storeIncome (Income income);
	
	public List<Income> viewAllIncome ();
	
	public List<Income> viewAllIncomeByCompany (long companyId);
	
	public List<Income> viewAllIncomeByCustomer (long customerId);

}
