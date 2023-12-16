package com.sauerkrauts.group.ceocat.referenceclasses;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {
    private String productId;
    private String productName;
    private String quantity; // Change to String
    private String deliveryProvider;
    private String dateOfDelivery;
    private String imageUrl;

    // Constructors
    public Product() {
        // Default constructor required for Firebase
    }

    public Product(String productId, String productName, String quantity, String deliveryProvider, String dateOfDelivery, String imageUrl) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.deliveryProvider = deliveryProvider;
        this.dateOfDelivery = dateOfDelivery;
        this.imageUrl = imageUrl;
    }

    // Getters and Setters
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDeliveryProvider() {
        return deliveryProvider;
    }

    public void setDeliveryProvider(String deliveryProvider) {
        this.deliveryProvider = deliveryProvider;
    }

    public String getDateOfDelivery() {
        return dateOfDelivery;
    }

    public void setDateOfDelivery(String dateOfDelivery) {
        this.dateOfDelivery = dateOfDelivery;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "Product ID: " + productId +
                "\nName of Product: " + productName +
                "\nQuantity: " + quantity +
                "\nDelivery Provider: " + deliveryProvider +
                "\nDate of Delivery: " + dateOfDelivery;
    }

    // Parcelable implementation
    protected Product(Parcel in) {
        productId = in.readString();
        productName = in.readString();
        quantity = in.readString();
        deliveryProvider = in.readString();
        dateOfDelivery = in.readString();
        imageUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productId);
        dest.writeString(productName);
        dest.writeString(quantity);
        dest.writeString(deliveryProvider);
        dest.writeString(dateOfDelivery);
        dest.writeString(imageUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
