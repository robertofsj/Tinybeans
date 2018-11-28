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
	public User user;
    @Required 
    public String orderId;
    @Required 
    public String chargeId; 
    @Required 
    public String status;
	@Required 
	public Long amount;
	@Required 
	public Date created;
	
	
	public PaymentInfo(User user, String orderId, String chargeId, String status, Long amount, Date created) {
		this.user = user;
		this.orderId = orderId;
		this.chargeId = chargeId;
		this.status = status;
		this.amount = amount;
		this.created = created;
	}
}
