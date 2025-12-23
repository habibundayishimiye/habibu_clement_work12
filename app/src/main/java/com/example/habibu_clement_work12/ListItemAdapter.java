package com.example.habibu_clement_work12;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.ListItemViewHolder> {
    private List<Product> products;
    private OnItemActionListener listener;

    public interface OnItemActionListener {
        void onItemClick(int position);
        void onEditClick(Product product);
        void onDeleteClick(Product product);
    }

    public ListItemAdapter(List<Product> products) {
        this.products = products;
    }

    public void setOnItemActionListener(OnItemActionListener listener) {
        this.listener = listener;
    }

    public void updateProducts(List<Product> newProducts) {
        this.products = newProducts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_item, parent, false);
        return new ListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListItemViewHolder holder, int position) {
        Product product = products.get(position);
        
        // Set icon based on product name or use default
        String icon = getIconForProduct(product.getName());
        holder.txtItemIcon.setText(icon);
        holder.txtItemName.setText(product.getName());
        holder.txtItemPrice.setText(product.getPrice());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(position);
            }
        });

        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(product);
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(product);
            }
        });
    }

    private String getIconForProduct(String productName) {
        String name = productName.toLowerCase();
        if (name.contains("sofa")) return "🛋️";
        if (name.contains("bicycle") || name.contains("bike")) return "🚲";
        if (name.contains("coffee")) return "☕";
        if (name.contains("table")) return "🪑";
        if (name.contains("chair")) return "🪑";
        if (name.contains("lamp")) return "💡";
        if (name.contains("desk")) return "🖥️";
        if (name.contains("bookshelf") || name.contains("book")) return "📚";
        if (name.contains("mirror")) return "🪞";
        if (name.contains("carpet")) return "🏠";
        return "📦"; // Default icon
    }

    @Override
    public int getItemCount() {
        return products != null ? products.size() : 0;
    }

    static class ListItemViewHolder extends RecyclerView.ViewHolder {
        TextView txtItemIcon;
        TextView txtItemName;
        TextView txtItemPrice;
        Button btnEdit;
        Button btnDelete;

        ListItemViewHolder(@NonNull View itemView) {
            super(itemView);
            txtItemIcon = itemView.findViewById(R.id.txtItemIcon);
            txtItemName = itemView.findViewById(R.id.txtItemName);
            txtItemPrice = itemView.findViewById(R.id.txtItemPrice);
            btnEdit = itemView.findViewById(R.id.btnEditItem);
            btnDelete = itemView.findViewById(R.id.btnDeleteItem);
        }
    }
}

