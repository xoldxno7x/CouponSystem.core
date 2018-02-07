package ejbProject;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class IncomeDAOBean implements IncomeDAO {

	@PersistenceContext(unitName = "Coupons")
	private EntityManager em;

	@Override
	public Income storeIncome(Income income) {
		em.persist(income);
		return income;
	}

	@Override
	public List<Income> viewAllIncome() {
		return (em.createNamedQuery("viewAllIncome", Income.class)).getResultList();
	}

	@Override
	public List<Income> viewAllIncomeByCompany(long companyId) {
		return em.createNamedQuery("viewAllIncomeByCompany", Income.class)
				.setParameter("customerPurchase", IncomeType.Customer_Purchase).setParameter("companyId", companyId)
				.getResultList();
	}

	@Override
	public List<Income> viewAllIncomeByCustomer(long customerId) {
		return em.createNamedQuery("viewAllIncomeByCustomer", Income.class)
				.setParameter("customerPurchase", IncomeType.Customer_Purchase).setParameter("customerId", customerId)
				.getResultList();
	}

}
