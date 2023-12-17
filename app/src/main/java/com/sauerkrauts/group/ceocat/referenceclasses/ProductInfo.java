package com.sauerkrauts.group.ceocat.referenceclasses;

// ProductInfo.java
public class ProductInfo {
    private String productId;
    private String productName;
    private String quantity;
    private String deliveryProvider;
    private String dateOfDelivery;

    private String imageUrl;

    public ProductInfo(String productId, String productName, String quantity, String deliveryProvider, String dateOfDelivery, String imageUrl) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.deliveryProvider = deliveryProvider;
        this.dateOfDelivery = dateOfDelivery;
        this.imageUrl = imageUrl;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getDeliveryProvider() {
        return deliveryProvider;
    }

    public String getDateOfDelivery() {
        return dateOfDelivery;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
