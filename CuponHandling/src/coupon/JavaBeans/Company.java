package coupon.JavaBeans;

import java.util.ArrayList;
import java.util.List;

public class Company {

	private long id;
	private String companyName;
	private String password;
	private String email;
	private List<Coupon> coupons;
	
	
	
	public Company(long id) {
		super();
		this.id = id;
	}

	public Company() {
	}

	public Company(long id, String companyName, String password, String email) {
		super();
		this.id = id;
		this.companyName = companyName;
		this.password = password;
		this.email = email;
		this.coupons = new ArrayList<Coupon>();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Coupon> getCoupons() {
		return coupons;
	}

	public void setCoupons(List<Coupon> coupons) {
		this.coupons = coupons;
	}

	@Override
	public String toString() {
		return "Company [id=" + id + ", companyName=" + companyName + ", password=" + password + ", email=" + email
				+ ", coupons=" + coupons + "]";
	}
	
	
	
	
}
