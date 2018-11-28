package models;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import javax.persistence.Entity;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(name="TB_PRODUCT")
public class Product extends Model {

	private String sku;
	private String name;
	private String description;
	private BigDecimal price;
	private String imageName;
	
	public Product(String name, String description, BigDecimal price, String imageName) {
		this.name = name;
		this.description = description;
		this.price = price;
		this.imageName = imageName;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public String getFormatedPrice() {
		return NumberFormat.getCurrencyInstance(new Locale("en", "US")).format(price);
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public String getImagePath() {
		return "/public/images/"+imageName;
	}
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
}
