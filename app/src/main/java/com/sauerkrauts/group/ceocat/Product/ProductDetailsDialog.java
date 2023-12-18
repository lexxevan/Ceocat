package com.sauerkrauts.group.ceocat.Product;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.sauerkrauts.group.ceocat.R;
import com.sauerkrauts.group.ceocat.referenceclasses.Product;

public class ProductDetailsDialog extends DialogFragment {

    private static final String ARG_PRODUCT = "product";
    private Product product;

    public ProductDetailsDialog() {
        // Required empty public constructor
    }

    public static ProductDetailsDialog newInstance(Product product) {
        ProductDetailsDialog dialog = new ProductDetailsDialog();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PRODUCT, product);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Set the width and height of the dialog
        if (getDialog() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            getDialog().getWindow().setGravity(Gravity.CENTER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_product_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView productIdTextView = view.findViewById(R.id.productIdTextView);
        TextView productNameTextView = view.findViewById(R.id.productNameTextView);
        TextView quantityTextView = view.findViewById(R.id.quantityTextView);
        TextView providerTextView = view.findViewById(R.id.providerTextView);
        TextView dateOfDeliveryTextView = view.findViewById(R.id.dateOfDeliveryTextView);
        ImageView productImageView = view.findViewById(R.id.productImageView); // New ImageView

        if (getArguments() != null) {
            Product product = getArguments().getParcelable(ARG_PRODUCT);
            if (product != null) {
                productIdTextView.setText("Product ID: " + product.getProductId());
                productNameTextView.setText("Name of Product: " + product.getProductName());
                quantityTextView.setText("Quantity: " + product.getQuantity());
                providerTextView.setText("Delivery Provider: " + product.getDeliveryProvider());
                dateOfDeliveryTextView.setText("Date of Delivery: " + product.getDateOfDelivery());

                // Load the image into ImageView using Glide
                String imageUrl = product.getImageUrl(); // Assuming you have a method like getImageUrl() in your Product class
                Glide.with(requireContext())
                        .load(imageUrl)
                        .placeholder(R.drawable.upload2) // Replace with your placeholder
                        .error(R.drawable.error_image) // Replace with your error image
                        .into(productImageView);

            }
        }
    }
}
