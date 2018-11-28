package util;

import javax.persistence.Entity;
import javax.persistence.Table;

import play.data.validation.Email;
import play.data.validation.MinSize;
import play.data.validation.Required;
import play.db.jpa.Model;


public class Card {

	@Required 
	@MinSize(16) 
	private Long cardNumber; 
	@Required 
	private String expDate; 
	@Required 
	private String cvc;
	
	public Card() {};
	
	public Card(Long cardNumber, String expDate, String cvc) {
		this.cardNumber = cardNumber;
		this.expDate = expDate;
		this.cvc = cvc;
	}
	public Long getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(Long cardNumber) {
		this.cardNumber = cardNumber;
	}
	public String getExpDate() {
		return expDate;
	}
	public void setExpDate(String expDate) {
		this.expDate = expDate;
	}
	public String getCvc() {
		return cvc;
	}
	public void setCvc(String cvc) {
		this.cvc = cvc;
	}
	
	
}
