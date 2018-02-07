package coupon.JavaBeans;

import java.util.HashSet;
import java.util.Set;

public class Customer {

	private long id;
	private String custName;
	private String password;
	private Set<Coupon> coupons;
	
	
	
	

	public Customer(long id) {
		super();
		this.id = id;
	}

	public Customer() {
	}

	public Customer(long id, String custName, String password) {
		super();
		this.id = id;
		this.custName = custName;
		this.password = password;
		this.coupons = new HashSet<>();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Coupon> getCoupons() {
		return coupons;
	}

	public void setCoupons(Set<Coupon> coupons) {
		this.coupons = coupons;
	}

	@Override
	public String toString() {
		return "Costumer [id=" + id + ", custName=" + custName + ", password=" + password + "]";
	}

}
