package models;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import play.data.validation.Email;
import play.data.validation.MinSize;
import play.data.validation.Required;
import play.db.jpa.Model;
import util.Card;


@Entity
@Table(name="TB_USER")
public class User extends Model{

    @Email
    @Required
    private String email;
    @Required 
    private String firstName; 
    @Required 
    private String lastName; 
    @Required 
    private String address; 
    @Required 
    private String city; 
    @Required 
    private String state;
	@Required 
	private String zip; 
	@Required
	@Transient
	private Card card;
	
	public User() {};
	
	public User(String email, String firstName, String lastName, String address, String city, String state, String zip,
			Card card) {
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.card = card;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public Card getCard() {
		return card;
	}
	public void setCard(Card card) {
		this.card = card;
	}
	public String getFullName() {
		return firstName +" "+ lastName;
	}
}
