package com.sauerkrauts.group.ceocat.Product;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sauerkrauts.group.ceocat.referenceclasses.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductViewModel extends ViewModel {

    private MutableLiveData<List<Product>> scannedProducts = new MutableLiveData<>();
    private Product selectedProduct;  // New field to store the selected product

    // Private constructor to prevent direct instantiation
    private ProductViewModel() {
    }

    // Static method to provide a singleton instance
    public static ProductViewModel getInstance() {
        return ProductViewModelHolder.INSTANCE;
    }

    // Nested class to hold the singleton instance
    private static class ProductViewModelHolder {
        private static final ProductViewModel INSTANCE = new ProductViewModel();
    }

    public LiveData<List<Product>> getScannedProducts() {
        if (scannedProducts.getValue() == null) {
            scannedProducts.setValue(new ArrayList<>());
        }
        return scannedProducts;
    }

    public void addScannedProduct(Product product) {
        List<Product> currentList = scannedProducts.getValue();
        if (currentList == null) {
            currentList = new ArrayList<>();
        }
        currentList.add(product);
        scannedProducts.setValue(currentList);
    }

    // Getter and setter for the selectedProduct
    public Product getSelectedProduct() {
        return selectedProduct;
    }

    public void setSelectedProduct(Product selectedProduct) {
        this.selectedProduct = selectedProduct;
    }

    // Clear the scanned products list
    public void clearScannedProducts() {
        scannedProducts.setValue(new ArrayList<>());
    }
}
