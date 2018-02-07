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
import coupons.core.beans.Coupon;
import coupons.core.facade.ClientType;
import coupons.core.facade.CompanyFacade;
import coupons.web.beans.Message;
import ejbProject.Income;
import ejbProject.IncomeDAO;
import ejbProject.IncomeType;
import mdb.IncomeService;


@Path("/coupons/company")
public class CompanyService extends BaseService{

	private IncomeService serviceStub;
	
	
	private IncomeDAO DAOStub;
	
	public CompanyService() {
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
		CompanyFacade company = (CompanyFacade) CouponsSystem.getInstance().login(username, password, ClientType.COMPANY);
		HttpSession session = request.getSession(true);
		session.setAttribute("company", company);
		
		return new Message(session.getId());
	}
	
	@GET
	@Path("/logout")
	@Produces(MediaType.APPLICATION_JSON)
	public Message logout (@Context HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null && session.getAttribute("company") != null) {
			session.invalidate();
			return new Message("Successful logout");
		}else {
			throw new RuntimeException("Could not logout");
		}
	}
	
	@GET
	@Path("/coupon/create/{price}")
	@Produces(MediaType.APPLICATION_JSON)
	public Message createCoupon (
			@PathParam("price") double price, 
			@Context HttpServletRequest request) {
		CompanyFacade company = (CompanyFacade) getFacadeFromSession("company", request);
		Long compId = company.getId();
		Coupon newCoupon = company.createCoupon(new Coupon(price));
        this.serviceStub.StoreService(new Income(compId, 100, IncomeType.Company_Create));	
		return new Message("Company #" + compId + " successfully created coupon #" + newCoupon.getId());
	}
	
	@GET
	@Path("/coupon/update/{id}/{newPrice}")
	@Produces(MediaType.APPLICATION_JSON)
	public Message updateCoupon (
			@PathParam("id") Long id,
			@PathParam("newPrice") double newPrice, 
			@Context HttpServletRequest request) {
		CompanyFacade company = (CompanyFacade) getFacadeFromSession("company", request);
		Long compId = company.getId();
		company.updateCoupon(new Coupon(id, newPrice));
		this.serviceStub.StoreService(new Income(compId, 10, IncomeType.Company_Update));	
		return new Message("Company #" + compId + " successfully updated coupon #" + id);
	}
	
	@GET
	@Path("/viewAllIncomeByCompany/")
	@Produces(MediaType.APPLICATION_JSON)
	public Income [] viewAllIncomeByCompany(@Context HttpServletRequest request) {
		CompanyFacade company = (CompanyFacade) getFacadeFromSession("company", request);
		Long companyId = company.getId();
		List<Income> list = this.DAOStub.viewAllIncomeByCompany(companyId);
	    Income[] rs = new Income [list.size()];
		return list.toArray(rs);

	}


}
