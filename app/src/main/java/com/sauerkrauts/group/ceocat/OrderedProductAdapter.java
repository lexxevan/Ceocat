package com.sauerkrauts.group.ceocat;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sauerkrauts.group.ceocat.referenceclasses.OrderedProduct;

import java.util.List;

// OrderedProductAdapter.java
// OrderedProductAdapter.java
public class OrderedProductAdapter extends RecyclerView.Adapter<OrderedProductAdapter.ViewHolder> {

    private List<OrderedProduct> orderedProducts;
    private OrderedProductClickListener clickListener;

    public OrderedProductAdapter(List<OrderedProduct> orderedProducts, OrderedProductClickListener clickListener) {
        this.orderedProducts = orderedProducts;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ordered_product, parent, false);
        return new ViewHolder(view, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderedProduct orderedProduct = orderedProducts.get(position);
        holder.bind(orderedProduct);
    }

    @Override
    public int getItemCount() {
        return orderedProducts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvProductName;

        public ViewHolder(@NonNull View itemView, OrderedProductClickListener clickListener) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            clickListener.onItemClick(position);
                        }
                    }
                }
            });
        }

        public void bind(OrderedProduct orderedProduct) {
            tvProductName.setText(orderedProduct.getProductName());
            // Add more bindings for other properties if needed
        }
    }

    public interface OrderedProductClickListener {
        void onItemClick(int position);
    }
}

