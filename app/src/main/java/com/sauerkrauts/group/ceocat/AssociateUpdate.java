package com.sauerkrauts.group.ceocat;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.fragment.app.Fragment;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.sauerkrauts.group.ceocat.referenceclasses.Product;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class AssociateUpdate extends Fragment {

    private String scannedQrCode;

    public AssociateUpdate() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_associate_update, container, false);

        // Button to manually trigger QR code scanning
        Button scanButton = view.findViewById(R.id.scanQrCodeButton);
        scanButton.setOnClickListener(v -> initiateScan());

        return view;
    }

    private void initiateScan() {
        IntentIntegrator.forSupportFragment(this)
                .setOrientationLocked(false)
                .setPrompt("Scan a QR Code")
                .initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                // Handle cancel
                Toast.makeText(getActivity(), "Scan cancelled", Toast.LENGTH_SHORT).show();
            } else {
                // Handle successful scan
                scannedQrCode = result.getContents();
                handleScannedQrCode(scannedQrCode);
            }
        }
    }

    private void handleScannedQrCode(String scannedQrCode) {
        // Split the scanned QR code into lines
        String[] lines = scannedQrCode.split("\n");

        // Check if the QR code has the expected number of lines
        if (lines.length == 6) { // Changed to 6 to include the image URL
            // Extract information from each line
            String imageUrl = lines[0].substring(lines[0].indexOf(":") + 2);
            String productId = lines[1].substring(lines[1].indexOf(":") + 2);
            String productName = lines[2].substring(lines[2].indexOf(":") + 2);
            String quantityStr = lines[3].substring(lines[3].indexOf(":") + 2);
            String deliveryProvider = lines[4].substring(lines[4].indexOf(":") + 2);
            String dateOfDelivery = lines[5].substring(lines[5].indexOf(":") + 2);

            // Create a Product object with the new constructor
            Product scannedProduct = new Product(productId, productName, quantityStr, deliveryProvider, dateOfDelivery, imageUrl);

            // Display information or perform any other actions based on the scanned product
            Toast.makeText(getActivity(), "Product Scanned:\n" + scannedProduct.toString(), Toast.LENGTH_LONG).show();

            // Set the scanned product in the ViewModel
            ProductViewModel productViewModel = new ViewModelProvider(requireActivity()).get(ProductViewModel.class);
            productViewModel.addScannedProduct(scannedProduct);

            // Display information in the EditText
            TextView scannedQrCodeTextView = requireView().findViewById(R.id.scannedQrCodeTextView);
            scannedQrCodeTextView.setVisibility(View.VISIBLE);
            scannedQrCodeTextView.setText(scannedQrCode);

            // Display information in a floating window
            showFloatingWindow(scannedProduct);
        } else {
            // If the QR code does not have the expected format, show an error message
            Toast.makeText(getActivity(), "Invalid QR Code Format", Toast.LENGTH_SHORT).show();
        }
    }

    private void showFloatingWindow(Product scannedProduct) {
        // Create a dialog to display the scanned product information
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.floating_window_layout);

        // Set the scanned product information in the dialog
        TextView productInfoTextView = dialog.findViewById(R.id.productInfoTextView);
        productInfoTextView.setText(scannedProduct.toString());

        // Load the image using InputStream and Bitmap
        ImageView productImageView = dialog.findViewById(R.id.productImageView);
        loadAndDisplayImage(scannedProduct.getImageUrl(), productImageView);

        // Close the dialog when the close button is clicked
        Button closeButton = dialog.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(v -> dialog.dismiss());

        // Show the dialog
        dialog.show();
    }

    private static class ImageLoaderTask extends AsyncTask<String, Void, Bitmap> {
        private WeakReference<ImageView> imageViewReference;

        ImageLoaderTask(ImageView imageView) {
            imageViewReference = new WeakReference<>(imageView);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                return BitmapFactory.decodeStream(input);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            ImageView imageView = imageViewReference.get();
            if (imageView != null && result != null) {
                imageView.setImageBitmap(result);
            } else {
                // Handle the case when the image cannot be loaded
                if (imageView != null) {
                    imageView.setImageResource(R.drawable.upload2);
                }
            }
        }
    }

    private void loadAndDisplayImage(String imageUrl, ImageView imageView) {
        ImageLoaderTask task = new ImageLoaderTask(imageView);
        task.execute(imageUrl);
    }
}
