package coupons.web;

import java.util.List;

import javax.ejb.EJB;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import coupons.core.CouponException;
import coupons.core.CouponsSystem;
import coupons.core.beans.Coupon;
import coupons.core.facade.ClientType;
import coupons.core.facade.CustomerFacade;
import coupons.web.beans.Message;
import ejbProject.Income;
import ejbProject.IncomeDAO;
import ejbProject.IncomeType;
import mdb.IncomeService;

@Path("/coupons/customer")
public class CustomerService extends BaseService{
	
	private IncomeService serviceStub;
	
	@EJB
	private IncomeDAO DAOStub;
	
	public CustomerService() {
		try {
			this.serviceStub = (IncomeService) new InitialContext().lookup("java:global/CouponEJB.EAR/CouponEJB/IncomeServiceBean!mdb.IncomeService");
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}
		try {
			this.DAOStub = (IncomeDAO) new InitialContext().lookup("java:global/CouponEJB.EAR/CouponEJB/IncomeDAOBean");
		} catch (NamingException e) {
		throw new RuntimeException(e);
		}
	}
	@GET
	@Path("/login/{username}/{password}")
	@Produces(MediaType.APPLICATION_JSON)
	public Message login (
			@PathParam("username") String username, 
			@PathParam("password") String password, 
			@Context HttpServletRequest request) throws CouponException {
		CustomerFacade customer = (CustomerFacade) CouponsSystem.getInstance().login(username, password, ClientType.CUSTOMER);
		HttpSession session = request.getSession(true);
		session.setAttribute("customer", customer);
		
		return new Message(session.getId());
	}
	
	@GET
	@Path("/logout")
	@Produces(MediaType.APPLICATION_JSON)
	public Message logout (@Context HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null && session.getAttribute("customer") != null) {
			session.invalidate();
			return new Message("Successful logout");
		}else {
			throw new RuntimeException("Could not logout");
		}
	}
	
	@GET
	@Path("/coupon/purchase/{id}/{price}")
	@Produces(MediaType.APPLICATION_JSON)
	public Message purchaseCoupon (
			@PathParam("id") Long id,
			@PathParam("price") double price, 
			@Context HttpServletRequest request) {
		CustomerFacade customer = (CustomerFacade) getFacadeFromSession("customer", request);
		Long custId = customer.getId();
		customer.purchaseCoupon(new Coupon(id, price));
		this.serviceStub.StoreService(new Income(custId, price, IncomeType.Customer_Purchase));
		return new Message("Customer #" + custId + " successfully purchased coupon #" + id + " for $" + price);
	}
	
	@GET
	@Path("/viewAllIncomeByCustomer/")
	@Produces(MediaType.APPLICATION_JSON)
	public Income [] viewAllIncomeByCustomer(@Context HttpServletRequest request) {
		CustomerFacade customer = (CustomerFacade) getFacadeFromSession("customer", request);
		Long customerId = customer.getId();
		List<Income> list = this.DAOStub.viewAllIncomeByCustomer(customerId);
		Income[] rs = new Income [list.size()];
		return list.toArray(rs);
	}


}
