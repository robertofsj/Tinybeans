package controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Order;
import com.stripe.model.Sku;
import com.stripe.model.Token;

import models.PaymentInfo;
import models.Product;
import models.User;
import play.Play;
import play.mvc.Controller;
import util.Card;

public class Payments extends Controller {

    public static void show(Long productId) {
    	Product product = Product.findById(productId);
    	
    	User user = new User();
    	user.card = new Card();
        render(product, user);
    }
    
    public static void doPayment(Long productId, User user) throws StripeException {
    	
    	Product product = Product.findById(productId);
    	
    	//verify if expiration date is valid
    	if(!isExpDateValid(user.card.expDate)) {
    		validation.addError("Expiration Date", "Field %s is invalid.");
    	}
    	if(validation.hasErrors()) {
    		render("Payments/show.html", product, user);
        }

    	//retrieve stripe apiKey from application.conf file
    	Stripe.apiKey = Play.configuration.getProperty("stripe.api.key");
    	
    	//if a product doesn't have sku, create it on Stripe.
    	if(product.sku == null) {
    		registerSku(product);
    	}

    	//create order and execute payment
    	Order order = null;
    	try {
    		order = Order.create(createOrderParams(user, product));
    		
    		Map<String, Object> orderPayParams = new HashMap<String, Object>();
        	Token token = Token.create(createTokenParams(user));
        	orderPayParams.put("source", token.getId());
        	order = order.pay(orderPayParams);
        	
		} catch (StripeException e) {
			validation.addError("", e.getLocalizedMessage());
			render("Payments/show.html",product, user);
		}
    	
    	//verify if it's a new user.
    	User entity = User.find("email", user.email).first();
    	if(entity == null) {
    		user.save();
    	} else {
    		user = entity;
    	}
    	
    	//create a payment info in database
    	PaymentInfo payInfo = new PaymentInfo(user, order.getId(), order.getCharge(), order.getStatus(), order.getAmount(), new Date());
    	payInfo.save();
    	
    	flash.success(String.format("The payment of %s for %s was approved", product.getFormatedPrice(), product.name));
        render("Payments/confirmation.html", payInfo);
    	

    }

	private static Map<String, Object> createOrderParams(User user, Product product) {
		Map<String, Object> orderParams = new HashMap<String, Object>();
		orderParams.put("currency", "usd");
		orderParams.put("email", user.email);
		orderParams.put("items", createItemParams(product));
		return orderParams;
	}

	private static Map<String, Object> createTokenParams(User user) {
		Map<String, Object> cardParams = new HashMap<String, Object>();
    	String[] expDate = user.card.expDate.split("/");
    	
    	cardParams.put("name", user.getFullName());
    	cardParams.put("number", user.card.cardNumber);
    	cardParams.put("exp_month", expDate[0]);
    	cardParams.put("exp_year", expDate[1]);
    	cardParams.put("cvc", user.card.cvc);
    	cardParams.put("address_line1", user.address);
    	cardParams.put("address_city", user.city);
    	cardParams.put("address_state", user.state);
    	cardParams.put("address_zip", user.zip);
    	
    	Map<String, Object> tokenParams = new HashMap<String, Object>();
    	tokenParams.put("card", cardParams);
    	
		return tokenParams;
	}

	private static void registerSku(Product product) throws StripeException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", "sku");
    	params.put("name", product.name);
    	params.put("description", product.description);
    	params.put("type", "good");
    	params.put("shippable", "false");
		com.stripe.model.Product stripeProduct = com.stripe.model.Product.create(params);
		
		Map<String, Object> skuParams = new HashMap<String, Object>();
		skuParams.put("product", stripeProduct.getId());
		skuParams.put("price", product.price.toString().replaceAll("[^0-9]", ""));
		skuParams.put("currency", "usd");
		Map<String, Object> inventoryParams = new HashMap<String, Object>();
		inventoryParams.put("type", "infinite");
		skuParams.put("inventory", inventoryParams);

		Sku sku = Sku.create(skuParams);
		
		product.sku = sku.getId();
		product.save();
	}

	private static List<Object> createItemParams(Product product) {
		Map<String, Object> item = new HashMap<String, Object>();
    	item.put("type", "sku");
    	item.put("parent", product.sku);
    	
    	List<Object> itemsParams = new LinkedList<Object>();
    	itemsParams.add(item);
		return itemsParams;
	}

	private static boolean isExpDateValid(String expDate) {
	    SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
        sdf.setLenient(false);
        
		try {
            sdf.parse(expDate);
            return true;
    	}
    	catch (Exception e) {
    	  return false;
    	}
	}
}