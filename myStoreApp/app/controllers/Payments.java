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
import play.mvc.Controller;
import util.Card;

//TODO: ADICIONAR COMENTARIOS NO CODIGO background-color: #9f1e22;
public class Payments extends Controller {

    public static void show(Long productId) {
    	Product product = Product.findById(productId);
    	User user = new User();
    	user.setCard(new Card());
        render(product, user);
    }
    
    //TODO: ADICIONAR COMENTARIOS
    public static void doPayment(Long productId, User user) throws StripeException {
    	
    	Product product = Product.findById(productId);
    	
    	if(!isExpDateValid(user.getCard().getExpDate())) {
    		validation.addError("Expiration Date", "Field %s is invalid.");
    	}
    	if(validation.hasErrors()) {
    		render("Payments/show.html", product, user);
        }

    	Stripe.apiKey = "sk_test_m7LkSZWfGqJz4yapIaED2ac9";
    	
    	if(product.getSku() == null) {
    		registerSku(product);
    	}

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
    	
    	User entity = User.find("email", user.getEmail()).first();
    	if(entity == null) {
    		user.save();
    	} else {
    		user = entity;
    	}
    	
    	PaymentInfo payInfo = new PaymentInfo(user, order.getId(), order.getCharge(), order.getStatus(), order.getAmount(), new Date());
    	
    	payInfo.save();
    	
    	flash.success(String.format("The payment of %s for %s was approved", product.getFormatedPrice(), product.getName()));
        render("Payments/confirmation.html", payInfo);
    	

    }

	private static Map<String, Object> createOrderParams(User user, Product product) {
		Map<String, Object> orderParams = new HashMap<String, Object>();
		orderParams.put("currency", "usd");
		orderParams.put("email", user.getEmail());
		orderParams.put("items", createItemParams(product));
		return orderParams;
	}

	private static Map<String, Object> createTokenParams(User user) {
		Map<String, Object> cardParams = new HashMap<String, Object>();
    	String[] expDate = user.getCard().getExpDate().split("/");
    	
    	cardParams.put("name", user.getFullName());
    	cardParams.put("number", user.getCard().getCardNumber());
    	cardParams.put("exp_month", expDate[0]);
    	cardParams.put("exp_year", expDate[1]);
    	cardParams.put("cvc", user.getCard().getCvc());
    	cardParams.put("address_line1", user.getAddress());
    	cardParams.put("address_city", user.getCity());
    	cardParams.put("address_state", user.getState());
    	cardParams.put("address_zip", user.getZip());
    	
    	Map<String, Object> tokenParams = new HashMap<String, Object>();
    	tokenParams.put("card", cardParams);
    	
		return tokenParams;
	}

	private static void registerSku(Product product) throws StripeException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", "sku");
    	params.put("name", product.getName());
    	params.put("description", product.getDescription());
    	params.put("type", "good");
    	params.put("shippable", "false");
		com.stripe.model.Product stripeProduct = com.stripe.model.Product.create(params);
		
		Map<String, Object> skuParams = new HashMap<String, Object>();
		skuParams.put("product", stripeProduct.getId());
		skuParams.put("price", product.getPrice().toString().replaceAll("[^0-9]", ""));
		skuParams.put("currency", "usd");
		Map<String, Object> inventoryParams = new HashMap<String, Object>();
		inventoryParams.put("type", "infinite");
		skuParams.put("inventory", inventoryParams);

		Sku sku = Sku.create(skuParams);
		
		product.setSku(sku.getId());
		product.save();
	}

	private static List<Object> createItemParams(Product product) {
		Map<String, Object> item = new HashMap<String, Object>();
    	item.put("type", "sku");
    	item.put("parent", product.getSku());
    	
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