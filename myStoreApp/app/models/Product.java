package models;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

import javax.persistence.Entity;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(name="TB_PRODUCT")
public class Product extends Model {

	public String sku;
	public String name;
	public String description;
	public BigDecimal price;
	public String imageName;
	
	public String getFormatedPrice() {
		return NumberFormat.getCurrencyInstance(new Locale("en", "US")).format(price);
	}
	public String getImagePath() {
		return "/public/images/"+imageName;
	}
}
