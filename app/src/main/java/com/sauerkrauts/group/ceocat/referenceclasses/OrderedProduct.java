package com.sauerkrauts.group.ceocat.referenceclasses;

public class OrderedProduct {
    private String productName;
    private String manufacturer;
    private String quantity;

    public OrderedProduct(String productName, String manufacturer, String quantity) {
        this.productName = productName;
        this.manufacturer = manufacturer;
        this.quantity = quantity;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getProductName() {
        return productName;
    }

    public String getQuantity() {
        return quantity;
    }
}

