package com.example.habibu_clement_work12;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> products;
    private List<Product> productsFiltered;

    public ProductAdapter(List<Product> products) {
        this.products = new ArrayList<>(products);
        this.productsFiltered = new ArrayList<>(products);
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productsFiltered.get(position);
        holder.txtName.setText(product.getName());
        holder.txtPrice.setText(product.getPrice());
        
        // Load image from URL if available, otherwise use drawable resource
        if (product.hasImageUrl()) {
            Glide.with(holder.itemView.getContext())
                    .load(product.getImageUrl())
                    .centerCrop()
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_menu_report_image)
                    .into(holder.imgProduct);
        } else if (product.getImageResourceId() != 0) {
            holder.imgProduct.setImageResource(product.getImageResourceId());
        }
    }

    @Override
    public int getItemCount() {
        return productsFiltered.size();
    }

    public void filter(String query) {
        productsFiltered.clear();
        if (query == null || query.isEmpty()) {
            productsFiltered.addAll(products);
        } else {
            String lowerQuery = query.toLowerCase();
            for (Product product : products) {
                if (product.getName().toLowerCase().contains(lowerQuery) ||
                    product.getPrice().toLowerCase().contains(lowerQuery)) {
                    productsFiltered.add(product);
                }
            }
        }
        notifyDataSetChanged();
    }
    
    public void addProduct(Product product) {
        products.add(product);
        productsFiltered.add(product);
        notifyItemInserted(productsFiltered.size() - 1);
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView txtName;
        TextView txtPrice;

        ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtName = itemView.findViewById(R.id.txtProductName);
            txtPrice = itemView.findViewById(R.id.txtProductPrice);
        }
    }
}


