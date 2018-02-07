package coupon.service;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import coupon.JavaBeans.Company;
import coupon.JavaBeans.Customer;
import coupon.notification.Notification;
import coupon.notification.NotificationException;
import facade.AdminFacade;
import facade.ClientType;
import facade.CouponSystem;

@Path("/admin")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AdminService {

	@GET
	@Path("/login/{username}/{password}")
	public Notification login(@PathParam("username") String username, @PathParam("password") String password,
			@Context HttpServletRequest request) throws NotificationException {
		AdminFacade af = new AdminFacade();
		if (!(username.equals("admin") && password.equals("1234"))) {
			throw new NotificationException("Invalid login inforamtion");
		} else {
			af = ((AdminFacade) CouponSystem.getInstance().login(username, password, ClientType.Admin));
			request.getSession(true).setAttribute("facade", af);
			Notification ms = new Notification("succesful login");
			return ms;
		}
	}

	@POST
	@Path("/createCompany")
	public Long createCompany(Company company, @Context HttpServletRequest request) throws NotificationException {
		try {
			HttpSession session = request.getSession(false);
			Company comp = new Company();
			comp = company;
			AdminFacade af = ((AdminFacade) session.getAttribute("facade"));
			af.createCompany(comp);
			Long id = comp.getId();
			return id;
		} catch (Exception e) {
			throw new NotificationException("Failed creating new company. Invlaid information or company name taken.");
		}
	}

	@DELETE
	@Path("/deleteCompany/{compId}")
	public String deleteCompany(@PathParam("compId") Long compId, @Context HttpServletRequest request)
			throws NotificationException {
		try {
			HttpSession session = request.getSession(false);
			Company comp = new Company(compId);
			AdminFacade af = ((AdminFacade) session.getAttribute("facade"));
			af.removeCompany(comp);
			return "Company " + compId + " deleted.";
		} catch (Exception e) {
			throw new NotificationException("Unable to delete company " + compId);
		}
	}

	@PUT
	@Path("/updateCompany")
	public String updateCompany(Company company, @Context HttpServletRequest request) throws NotificationException {
		try {
			HttpSession session = request.getSession(false);
			AdminFacade af = ((AdminFacade) session.getAttribute("facade"));
			af.updateCompany(company);
			return "Company " + company.getId() + " updated.";
		} catch (Exception e) {
			throw new NotificationException("Unable to update company " + company.getId());
		}

	}

	@GET
	@Path("/getAllCompanies")
	public Set<Company> getAllCompanies(@Context HttpServletRequest request) throws NotificationException {
		try {
			HttpSession session = request.getSession(false);
			AdminFacade af = ((AdminFacade) session.getAttribute("facade"));
			return af.getAllCompanies();
		} catch (Exception e) {
			throw new NotificationException(
					"Oops.. something went wrong, please try again later or contact costumer service.");
		}
	}

	@GET
	@Path("/getCompany/{compId}")
	public Company getCompany(@PathParam("compId") Long compId, @Context HttpServletRequest request)
			throws NotificationException {
		try {
			HttpSession session = request.getSession(false);
			AdminFacade af = ((AdminFacade) session.getAttribute("facade"));
			return af.getCompany(compId);
		} catch (Exception e) {
			throw new NotificationException(
					"Unable to get company " + compId + ", make sure you are using the correct ID");
		}
	}

	@POST
	@Path("/createCustomer")
	public Long createCustomer(Customer customer, @Context HttpServletRequest request) throws NotificationException {
		try {
			HttpSession session = request.getSession(false);
			Customer cust = new Customer();
			cust = customer;
			AdminFacade af = ((AdminFacade) session.getAttribute("facade"));
			af.createCustomer(cust);
			Long id = cust.getId();
			return id;
		} catch (Exception e) {
			throw new NotificationException("Failed creting new customer. Customer ID might be taken.");
		}
	}

	@DELETE
	@Path("/deleteCustomer/{custId}")
	public String deleteCustomer(@PathParam("custId") Long custId, @Context HttpServletRequest request)
			throws NotificationException {
		try {
			HttpSession session = request.getSession(false);
			Customer cust = new Customer(custId);
			AdminFacade af = ((AdminFacade) session.getAttribute("facade"));
			af.removeCustomer(cust);
			return "Customer " + custId + " deleted.";
		} catch (Exception e) {
			throw new NotificationException("Unable to delete customer " + custId);
		}
	}

	@PUT
	@Path("/updateCustomer")
	public String updateCustomer(Customer customer, @Context HttpServletRequest request) throws NotificationException {
		try {
			HttpSession session = request.getSession(false);
			AdminFacade af = ((AdminFacade) session.getAttribute("facade"));
			af.updateCustomer(customer);
			return "Customer " + customer.getId() + " updated.";
		} catch (Exception e) {
			throw new NotificationException("Unable to update customer " + customer.getId());
		}

	}

	@GET
	@Path("/getCustomer/{custId}")
	public Customer getCustomer(@PathParam("custId") Long custId, @Context HttpServletRequest request)
			throws NotificationException {
		try {
			HttpSession session = request.getSession(false);
			AdminFacade af = ((AdminFacade) session.getAttribute("facade"));
			return af.getCustomer(custId);
		} catch (Exception e) {
			throw new NotificationException("Unable to find customer " + custId);
		}
	}

	@GET
	@Path("/getAllCustomers")
	public Set<Customer> getAllCustomers(@Context HttpServletRequest request) throws NotificationException {
		try {
			HttpSession session = request.getSession(false);
			AdminFacade af = ((AdminFacade) session.getAttribute("facade"));
			return af.getAllCustomers();
		} catch (Exception e) {
			throw new NotificationException("Unable to get customer list");
		}
	}

	@GET
	@Path("/logout")
	public String logout(@Context HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		session.invalidate();
		return "Adios muchacho!";
	}
}
