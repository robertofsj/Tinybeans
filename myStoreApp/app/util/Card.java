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
	public Long cardNumber; 
	@Required 
	public String expDate; 
	@Required 
	public String cvc;
	
}
