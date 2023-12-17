package com.sauerkrauts.group.ceocat;// AdminHome.java
import android.app.AlertDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.sauerkrauts.group.ceocat.referenceclasses.ProductInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdminHome extends Fragment {

    private RecyclerView recyclerView;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public AdminHome() {
        // Required empty public constructor
    }

    public static AdminHome newInstance(String param1, String param2) {
        AdminHome fragment = new AdminHome();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_home, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        setupRecyclerView();
        return view;
    }

    private void setupRecyclerView() {
        List<ProductInfo> dummyData = generateDummyData();
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(dummyData, new OnItemClickListener() {
            @Override
            public void onItemClick(ProductInfo productInfo) {
                // Provide ProductInfo when showing the dialog
                showDialog(productInfo);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private List<ProductInfo> generateDummyData() {
        List<ProductInfo> data = new ArrayList<>();

        // Add your desired product details here
        data.add(new ProductInfo("4758493721", "Coka Cola", "320", "Bob Ross", "12 31 2023", "https://images.pexels.com/photos/1904262/pexels-photo-1904262.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"));
        data.add(new ProductInfo("8796054623", "Rexona Deodorant", "210", "Dize Nats", "01 23 2023", "https://images.pexels.com/photos/3646109/pexels-photo-3646109.jpeg?auto=compress&cs=tinysrgb&w=600"));
        data.add(new ProductInfo("1234567890", "Pepsi", "200", "Dize Nats", "06 15 2023", "https://images.pexels.com/photos/12040256/pexels-photo-12040256.jpeg?auto=compress&cs=tinysrgb&w=600"));
        data.add(new ProductInfo("9876543210", "Sprite", "150", "Josie Rizal", "09 30 2023","https://images.pexels.com/photos/12040256/pexels-photo-12040256.jpeg?auto=compress&cs=tinysrgb&w=600"));
        data.add(new ProductInfo("1111222233", "Fanta", "100", "Earl Daniel", "03 15 2023", "https://images.pexels.com/photos/13950097/pexels-photo-13950097.jpeg?auto=compress&cs=tinysrgb&w=600"));
        data.add(new ProductInfo("4444555566", "Mountain Dew", "180", "Axo Yu", "07 01 2023", "https://images.pexels.com/photos/18714939/pexels-photo-18714939/free-photo-of-green-cans-of-ginger-ale-from-president-choice.jpeg?auto=compress&cs=tinysrgb&w=600"));
        data.add(new ProductInfo("7777888899", "Dr. Pepper", "250", "Bob Ross", "05 20 2023","https://images.pexels.com/photos/13599785/pexels-photo-13599785.jpeg?auto=compress&cs=tinysrgb&w=600"));
        data.add(new ProductInfo("9999000011", "Coca Cola Zero", "180", "Dize Nats", "08 10 2023","https://images.pexels.com/photos/3819969/pexels-photo-3819969.jpeg?auto=compress&cs=tinysrgb&w=600"));
        data.add(new ProductInfo("1212121212", "Pepsi Max", "120", "Josie Rizal", "04 05 2023","https://images.pexels.com/photos/11942006/pexels-photo-11942006.jpeg?auto=compress&cs=tinysrgb&w=600"));
        data.add(new ProductInfo("3434343434", "7UP", "90", "Earl Daniel", "11 11 2023","https://images.pexels.com/photos/14247881/pexels-photo-14247881.jpeg?auto=compress&cs=tinysrgb&w=600"));
        data.add(new ProductInfo("5656565656", "Root Beer", "200", "Axo Yu", "01 25 2023","https://images.pexels.com/photos/4068312/pexels-photo-4068312.jpeg?auto=compress&cs=tinysrgb&w=600"));



        return data;
    }

    private void showDialog(ProductInfo productInfo) {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_product_info, null);

        ImageView productImageView = dialogView.findViewById(R.id.productImageView);
        TextView productIdTextView = dialogView.findViewById(R.id.productIdTextView);
        TextView productInfoTextView = dialogView.findViewById(R.id.productInfoTextView);

        // Load image using Picasso
        Picasso.get().load(productInfo.getImageUrl()).into(productImageView);

        productIdTextView.setText("Product ID: " + productInfo.getProductId());
        productInfoTextView.setText(getProductInfoMessage(productInfo));

        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Product Overview")
                .setView(dialogView)
                .setPositiveButton("OK", null)
                .show();
    }





    private String getProductInfoMessage(ProductInfo productInfo) {
        // Customize the message based on the product details
        return "Product ID: " + productInfo.getProductId() +
                "\nName of Product: " + productInfo.getProductName() +
                "\nQuantity: " + productInfo.getQuantity() +
                "\nDelivery Provider: " + productInfo.getDeliveryProvider() +
                "\nDate of Delivery: " + productInfo.getDateOfDelivery();
    }

    // Interface to handle item clicks
    public interface OnItemClickListener {
        void onItemClick(ProductInfo productInfo);
    }
}
