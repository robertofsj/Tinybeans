package models;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import play.data.validation.Email;
import play.data.validation.Required;
import play.db.jpa.Model;
import util.Card;


@Entity
@Table(name="TB_USER")
public class User extends Model{

    @Email
    @Required
    public String email;
    @Required 
    public String firstName; 
    @Required 
    public String lastName; 
    @Required 
    public String address; 
    @Required 
    public String city; 
    @Required 
    public String state;
	@Required 
	public String zip; 
	@Required
	@Transient
	public Card card;
	
	public String getFullName() {
		return firstName +" "+ lastName;
	}
}
