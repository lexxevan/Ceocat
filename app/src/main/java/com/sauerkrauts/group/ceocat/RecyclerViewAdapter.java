package com.sauerkrauts.group.ceocat;// RecyclerViewAdapter.java
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.sauerkrauts.group.ceocat.Admin.AdminHome;
import com.sauerkrauts.group.ceocat.referenceclasses.ProductInfo;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<ProductInfo> data;
    private AdminHome.OnItemClickListener listener;

    public RecyclerViewAdapter(List<ProductInfo> data, AdminHome.OnItemClickListener listener) {
        this.data = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductInfo productInfo = data.get(position);
        holder.bind(productInfo, listener);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
        }

        void bind(final ProductInfo productInfo, final AdminHome.OnItemClickListener listener) {
            textView.setText(productInfo.getProductName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(productInfo);
                }
            });
        }
    }
}
