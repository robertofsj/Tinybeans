package controllers;

import java.util.List;

import models.Product;
import play.mvc.Controller;

public class Products extends Controller {

    public static void show() {
        List<Product> products = Product.findAll();
        render(products);
    }
}