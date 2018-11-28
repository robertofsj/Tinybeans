package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.data.validation.Required;
import play.db.jpa.Model;


@Entity
@Table(name="TB_PAYMENT")
public class PaymentInfo extends Model{

    @Required
    @ManyToOne
    private User user;
    @Required 
    private String orderId;
    @Required 
    private String chargeId; 
    @Required 
    private String status;
	@Required 
	private Long amount;
	@Required 
	private Date created;
	
	
	public PaymentInfo(User user, String orderId, String chargeId, String status, Long amount, Date created) {
		this.user = user;
		this.orderId = orderId;
		this.chargeId = chargeId;
		this.status = status;
		this.amount = amount;
		this.created = created;
	}
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getChargeId() {
		return chargeId;
	}
	public void setChargeId(String chargeId) {
		this.chargeId = chargeId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Long getAmount() {
		return amount;
	}
	public void setAmount(Long amount) {
		this.amount = amount;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
}
