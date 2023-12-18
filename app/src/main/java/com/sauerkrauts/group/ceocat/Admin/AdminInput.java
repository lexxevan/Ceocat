package com.sauerkrauts.group.ceocat.Admin;// AdminInput.java
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sauerkrauts.group.ceocat.Product.OrderedProductAdapter;
import com.sauerkrauts.group.ceocat.Product.OrderedProductViewModel;
import com.sauerkrauts.group.ceocat.R;
import com.sauerkrauts.group.ceocat.referenceclasses.OrderedProduct;

import java.util.ArrayList;
import java.util.List;

// AdminInput.java
public class AdminInput extends Fragment implements OrderedProductAdapter.OrderedProductClickListener {

    private List<OrderedProduct> orderedProducts = new ArrayList<>();
    private OrderedProductAdapter adapter;
    private OrderedProductViewModel orderedProductViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderedProductViewModel = new ViewModelProvider(this).get(OrderedProductViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_input, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        adapter = new OrderedProductAdapter(orderedProducts, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        Button btnOrderStock = view.findViewById(R.id.btnOrderStock);
        btnOrderStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOrderDialog();
            }
        });

        // Observe the LiveData in the ViewModel
        orderedProductViewModel.getOrderedProductsLiveData().observe(getViewLifecycleOwner(), new Observer<List<OrderedProduct>>() {
            @Override
            public void onChanged(List<OrderedProduct> products) {
                // Update the adapter with the new data
                orderedProducts.clear();
                orderedProducts.addAll(products);
                adapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    private void showOrderDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_order_stock, null);
        builder.setView(dialogView);

        final EditText etProductName = dialogView.findViewById(R.id.etProductName);
        final EditText etManufacturer = dialogView.findViewById(R.id.etManufacturer);
        final EditText etQuantity = dialogView.findViewById(R.id.etQuantity);

        builder.setPositiveButton("Order", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String productName = etProductName.getText().toString();
                String manufacturer = etManufacturer.getText().toString();
                String quantity = etQuantity.getText().toString();

                addOrderedProduct(productName, manufacturer, quantity);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    private void addOrderedProduct(String productName, String manufacturer, String quantity) {
        OrderedProduct orderedProduct = new OrderedProduct(productName, manufacturer, quantity);
        orderedProducts.add(orderedProduct);
        adapter.notifyDataSetChanged();

        orderedProductViewModel.addOrderedProduct(orderedProduct);
    }

    @Override
    public void onItemClick(int position) {
        OrderedProduct selectedProduct = orderedProducts.get(position);
        showDetailedView(selectedProduct);
    }

    private void showDetailedView(OrderedProduct selectedProduct) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_detailed_view, null);
        builder.setView(dialogView);

        TextView tvProductName = dialogView.findViewById(R.id.tvDetailedProductName);
        TextView tvManufacturer = dialogView.findViewById(R.id.tvDetailedManufacturer);
        TextView tvQuantity = dialogView.findViewById(R.id.tvDetailedQuantity);

        tvProductName.setText(selectedProduct.getProductName());
        tvManufacturer.setText(selectedProduct.getManufacturer());
        tvQuantity.setText(selectedProduct.getQuantity());

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }
}

