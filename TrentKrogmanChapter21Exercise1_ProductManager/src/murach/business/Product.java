package murach.business;

/*
 * Name: Trent Krogman
 * Date: 11/17/20
 * This is the Product class for Chapter 21 Exercise 1
 */

import java.text.NumberFormat;

public class Product {
    private long id;
    private String code;
    private String description;
    private double price;

    public Product() {
		this.code = "";
		this.description = "";
		this.price = 0.0;
	}
    
    public Product(String code, String description, double listPrice) {
		this.code = code;
		this.description = description;
		this.price = listPrice;
	}
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String name) {
        this.description = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    
    public String getPriceFormatted() {
        NumberFormat currencyFormatter =
                NumberFormat.getCurrencyInstance();
        return currencyFormatter.format(getPrice());
    }    
}