package com.sauerkrauts.group.ceocat.Associate;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sauerkrauts.group.ceocat.Product.ProductAdapter;
import com.sauerkrauts.group.ceocat.Product.ProductDetailsDialog;
import com.sauerkrauts.group.ceocat.Product.ProductViewModel;
import com.sauerkrauts.group.ceocat.R;
import com.sauerkrauts.group.ceocat.User.UserViewModel;
import com.sauerkrauts.group.ceocat.referenceclasses.Product;

import java.util.ArrayList;
import java.util.List;

// Inside AssociateConfirm class
public class AssociateConfirm extends Fragment {

    private Product foundProduct;
    private ProductViewModel productViewModel;
    private ProductAdapter productAdapter;
    private UserViewModel userViewModel;
    private DatabaseReference databaseReference;

    public AssociateConfirm() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the ProductViewModel
        productViewModel = new ViewModelProvider(requireActivity()).get(ProductViewModel.class);

        // Initialize the UserViewModel
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        // Initialize the ProductAdapter
        productAdapter = new ProductAdapter(new ArrayList<>());

        // Initialize the Realtime Database reference
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://ceocat-910da-default-rtdb.asia-southeast1.firebasedatabase.app/");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_associate_confirm, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(productAdapter);

        // Observe changes in the list of scanned products
        productViewModel.getScannedProducts().observe(getViewLifecycleOwner(), products -> {
            productAdapter.setProductList(products);
        });

        // Set click listener for each item in the RecyclerView
        productAdapter.setOnItemClickListener(position -> {
            Product selectedProduct = productAdapter.getProductAtPosition(position);
            showProductDetailsDialog(selectedProduct);
        });

        // Button to trigger the confirmation dialog
        Button updateStockButton = view.findViewById(R.id.updateStockButton);
        updateStockButton.setOnClickListener(v -> showUpdateStockDialog());

        // Button to trigger the confirmation dialog
        Button uploadButton = view.findViewById(R.id.uploadBtn);
        uploadButton.setOnClickListener(v -> showConfirmationDialog());

        return view;
    }

    private void showProductDetailsDialog(Product product) {
        ProductDetailsDialog productDetailsDialog = ProductDetailsDialog.newInstance(product);
        productDetailsDialog.show(getChildFragmentManager(), "ProductDetailsDialog");
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Confirm Upload");

        // Get the list of scanned products
        List<Product> scannedProducts = productViewModel.getScannedProducts().getValue();

        // Create a simple adapter to display product names in the dialog
        List<String> productNames = new ArrayList<>();
        if (scannedProducts != null) {
            for (Product product : scannedProducts) {
                productNames.add(product.getProductName());
            }
        }

        CharSequence[] items = productNames.toArray(new CharSequence[0]);

        builder.setItems(items, (dialog, which) -> {
            // Handle item click if needed
        });

        builder.setPositiveButton("Confirm", (dialog, which) -> {
            // Upload scanned products to the database
            uploadScannedProducts(scannedProducts);

            // Clear the scanned products list
            productViewModel.clearScannedProducts();

            Toast.makeText(requireContext(), "Products uploaded successfully", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> {
            // Do nothing or add any other cancellation logic
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showUpdateStockDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.update_stock_dialog, null);
        builder.setView(dialogView);

        // Get the views from the dialog layout
        SearchView searchView = dialogView.findViewById(R.id.searchView);
        TextView quantityLabel = dialogView.findViewById(R.id.quantityLabel);
        EditText quantityEditText = dialogView.findViewById(R.id.quantityEditText);
        Button confirmUpdateButton = dialogView.findViewById(R.id.confirmUpdateButton);

        // Set up the search view
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Perform the search and show/hide relevant views
                searchProductAndUpdateViews(query, quantityLabel, quantityEditText, confirmUpdateButton);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle text change if needed
                return false;
            }
        });

        // Set up the confirm update button
        confirmUpdateButton.setOnClickListener(v -> {
            // Perform the update with the entered quantity
            confirmUpdateAndUpdateDatabase(quantityEditText.getText().toString());
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void searchProductAndUpdateViews(String productId, TextView quantityLabel,
                                             EditText quantityEditText, Button confirmUpdateButton) {
        DatabaseReference productReference = databaseReference.child("users").child("Associate")
                .child(userViewModel.getCurrentUserId()).child("Products").child(productId);

        productReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Product found, update views
                    foundProduct = snapshot.getValue(Product.class);
                    if (foundProduct != null) {
                        // Show relevant views
                        quantityLabel.setVisibility(View.VISIBLE);
                        quantityEditText.setVisibility(View.VISIBLE);
                        confirmUpdateButton.setVisibility(View.VISIBLE);

                        // Set current quantity
                        quantityLabel.setText("Current Quantity: " + foundProduct.getQuantity());
                    }
                } else {
                    // Product not found, hide views
                    quantityLabel.setVisibility(View.GONE);
                    quantityEditText.setVisibility(View.GONE);
                    confirmUpdateButton.setVisibility(View.GONE);

                    // Show a toast or any message indicating that the product was not found
                    Toast.makeText(requireContext(), "Product not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors if needed
                Toast.makeText(requireContext(), "Error in database operation: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }





    private void confirmUpdateAndUpdateDatabase(String newQuantity) {
        if (foundProduct != null) {
            // Perform the update in the database
            DatabaseReference productReference = databaseReference.child("products").child(foundProduct.getProductId());

            // Convert the newQuantity to an integer
            int updatedQuantity = Integer.parseInt(newQuantity);

            // Update the quantity field
            productReference.child("quantity").setValue(updatedQuantity);

            // Show a toast or any message indicating the successful update
            Toast.makeText(requireContext(), "Quantity updated successfully", Toast.LENGTH_SHORT).show();
        }
    }


    private void uploadScannedProducts(List<Product> scannedProducts) {
        // Get the current user's ID
        String userId = userViewModel.getCurrentUserId();

        if (userId != null) {
            // Upload each scanned product under the user's ID
            DatabaseReference userProductsRef = databaseReference.child("users").child("Associate").child(userId).child("Products");

            for (Product product : scannedProducts) {
                // Set the quantity field for each scanned product
                product.setQuantity(String.valueOf(0)); // You can set it to any default value

                // Push the product to the database
                userProductsRef.push().setValue(product);
            }
        }
    }

}
