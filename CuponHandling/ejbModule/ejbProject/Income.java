package ejbProject;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table (name ="INCOME_RECORDS")
@NamedQueries ({
	@NamedQuery(name="viewAllIncome", query="SELECT i FROM Income AS i ORDER BY i.timestamp DESC"),
	@NamedQuery(name="viewAllIncomeByCompany", query="SELECT i FROM Income AS i WHERE i.invokerId = :companyId AND i.incomeType <> :customerPurchase ORDER BY i.timestamp DESC"),
	@NamedQuery(name="viewAllIncomeByCustomer", query="SELECT i FROM Income AS i WHERE i.invokerId = :customerId AND i.incomeType = :customerPurchase ORDER BY i.timestamp DESC")
})

@XmlRootElement
public class Income implements Serializable {

	private static final long serialVersionUID = -486994799968376917L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Long invokerId;
	private Double amount;
	
	@Enumerated(EnumType.STRING)
	private IncomeType incomeType;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date timestamp;
	
	public Income(long invokerId, double amount, IncomeType incomeType) {
		super();
		this.invokerId = invokerId;
		this.amount = amount;
		this.incomeType = incomeType;
		this.timestamp = new Date();
	}

	public Income() {
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getInvokerId() {
		return invokerId;
	}

	public void setInvokerId(Long invokerId) {
		this.invokerId = invokerId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public IncomeType getIncomeType() {
		return incomeType;
	}

	public void setIncomeType(IncomeType incomeType) {
		this.incomeType = incomeType;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}


	
	


	@Override
	public String toString() {
		return "Income [id=" + id + ", invokerId=" + invokerId + ", amount=" + amount + ", incomeType=" + incomeType
				+ ", timestamp=" + timestamp + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Income other = (Income) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	
	
	
	
}
