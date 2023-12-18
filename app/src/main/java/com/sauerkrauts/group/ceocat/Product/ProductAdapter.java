package com.sauerkrauts.group.ceocat.Product;// ProductAdapter.java
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sauerkrauts.group.ceocat.R;
import com.sauerkrauts.group.ceocat.referenceclasses.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private OnItemClickListener onItemClickListener;

    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    // Add this method to set the list of products
    public void setProductList(List<Product> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public Product getProductAtPosition(int position) {
        return productList.get(position);
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {

        private TextView productNameTextView;

        public ProductViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            productNameTextView = itemView.findViewById(R.id.productNameTextView);

            // Set click listener for the item view
            itemView.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(getAdapterPosition());
                }
            });
        }

        public void bind(Product product) {
            productNameTextView.setText(product.getProductName());
        }
    }

    // Interface for handling item clicks
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
