package coupons.web;

import java.util.List;

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
import coupons.core.facade.AdminFacade;
import coupons.core.facade.ClientType;
import coupons.web.beans.Message;
import ejbProject.Income;
import ejbProject.IncomeDAO;

@Path("/coupons/admin")
public class AdminService {

	
	private IncomeDAO DAOStub;
	
	public AdminService() {
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
		AdminFacade admin = (AdminFacade) CouponsSystem.getInstance().login(username, password, ClientType.ADMIN);
		HttpSession session = request.getSession(true);
		session.setAttribute("admin", admin);
		
		return new Message(session.getId());
	}
	
	@GET
	@Path("/logout")
	@Produces(MediaType.APPLICATION_JSON)
	public Message logout (@Context HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null && session.getAttribute("admin") != null) {
			session.invalidate();
			return new Message("Successful logout");
		}else {
			throw new RuntimeException("Could not logout");
		}
	}
	
	@GET
	@Path("/viewAllIncome")
	@Produces(MediaType.APPLICATION_JSON)
	public Income [] viewAllIncome(@Context HttpServletRequest request) {
		return this.DAOStub.viewAllIncome().toArray(new Income[0]);
		
		
	}
	
	@GET
	@Path("/viewAllIncomeByCompany/{companyId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Income [] viewAllIncomeByCompany(@Context HttpServletRequest request,@PathParam("companyId")long companyId) {
		List<Income> list = this.DAOStub.viewAllIncomeByCompany(companyId);
	    Income[] rs = new Income [list.size()];
		return list.toArray(rs);

	}
	
	@GET
	@Path("/viewAllIncomeByCustomer/{customerId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Income [] viewAllIncomeByCustomer(@Context HttpServletRequest request, @PathParam("customerId")long customerId) {
		List<Income> list = this.DAOStub.viewAllIncomeByCustomer(customerId);
		Income[] rs = new Income [list.size()];
		return list.toArray(rs);
	}
	
}
