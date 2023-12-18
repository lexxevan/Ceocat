package com.sauerkrauts.group.ceocat.Product;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sauerkrauts.group.ceocat.referenceclasses.OrderedProduct;

import java.util.ArrayList;
import java.util.List;

// OrderedProductViewModel.java
// OrderedProductViewModel.java
public class OrderedProductViewModel extends AndroidViewModel {
    private MutableLiveData<List<OrderedProduct>> orderedProductsLiveData = new MutableLiveData<>();

    public OrderedProductViewModel(Application application) {
        super(application);
    }

    public LiveData<List<OrderedProduct>> getOrderedProductsLiveData() {
        if (orderedProductsLiveData.getValue() == null) {
            orderedProductsLiveData.setValue(new ArrayList<>());
        }
        return orderedProductsLiveData;
    }

    public void addOrderedProduct(OrderedProduct orderedProduct) {
        List<OrderedProduct> currentList = orderedProductsLiveData.getValue();
        if (currentList == null) {
            currentList = new ArrayList<>();
        }
        currentList.add(orderedProduct);
        orderedProductsLiveData.setValue(currentList);
    }
}

