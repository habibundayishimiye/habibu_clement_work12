package com.example.habibu_clement_work12;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListActivity extends AppCompatActivity {

    private RecyclerView recyclerViewItems;
    private ListItemAdapter listItemAdapter;
    private ProductDatabaseHelper productDatabaseHelper;
    private Button btnAddItem;
    private int editingProductId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Initialize database
        productDatabaseHelper = new ProductDatabaseHelper(this);

        // Initialize RecyclerView
        recyclerViewItems = findViewById(R.id.recyclerViewItems);
        recyclerViewItems.setLayoutManager(new LinearLayoutManager(this));

        // Setup Add Button
        btnAddItem = findViewById(R.id.btnAddItem);
        btnAddItem.setOnClickListener(v -> showAddItemDialog());

        // Load products from database
        loadProductsFromDatabase();
    }

    private void loadProductsFromDatabase() {
        List<Product> products = productDatabaseHelper.getAllProducts();
        
        // If database is empty, add sample products
        if (products.isEmpty()) {
            addSampleProducts();
            products = productDatabaseHelper.getAllProducts();
        }

        // Setup adapter
        listItemAdapter = new ListItemAdapter(products);
        listItemAdapter.setOnItemActionListener(new ListItemAdapter.OnItemActionListener() {
            @Override
            public void onItemClick(int position) {
                // Navigate to Activity3 (Database Activity)
                Intent intent = new Intent(ListActivity.this, Activity3.class);
                startActivity(intent);
            }

            @Override
            public void onEditClick(Product product) {
                showEditItemDialog(product);
            }

            @Override
            public void onDeleteClick(Product product) {
                showDeleteConfirmation(product);
            }
        });
        recyclerViewItems.setAdapter(listItemAdapter);
    }

    private void addSampleProducts() {
        String[] names = {"Sofa", "Bicycle", "Coffee Maker", "Table", "Chair", "Lamp", "Desk", "Bookshelf", "Mirror", "Carpet"};
        String[] prices = {"$450", "$280", "$120", "$85", "$320", "$150", "$200", "$180", "$75", "$250"};
        
        for (int i = 0; i < names.length; i++) {
            Product product = new Product(names[i], prices[i], null);
            productDatabaseHelper.insertProduct(product, "", "Furniture", null);
        }
    }

    private void showAddItemDialog() {
        editingProductId = -1;
        showItemDialog(null);
    }

    private void showEditItemDialog(Product product) {
        editingProductId = product.getId();
        showItemDialog(product);
    }

    private void showItemDialog(Product product) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_item, null);
        
        EditText editItemName = dialogView.findViewById(R.id.editItemName);
        EditText editItemPrice = dialogView.findViewById(R.id.editItemPrice);
        Spinner spinnerCategory = dialogView.findViewById(R.id.spinnerItemCategory);
        Button btnSave = dialogView.findViewById(R.id.btnSaveItem);
        Button btnCancel = dialogView.findViewById(R.id.btnCancelItem);

        // Setup category spinner
        String[] categories = {"Furniture", "Electronics", "Clothing", "Vehicles", 
                "Home & Garden", "Sports & Outdoors", "Toys & Games", "Books & Media"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, 
                android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        // If editing, populate fields
        if (product != null) {
            editItemName.setText(product.getName());
            editItemPrice.setText(product.getPrice());
            if (product.getCategory() != null) {
                int position = adapter.getPosition(product.getCategory());
                if (position >= 0) {
                    spinnerCategory.setSelection(position);
                }
            }
            btnSave.setText("Update");
        }

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();

        btnSave.setOnClickListener(v -> {
            String name = editItemName.getText().toString().trim();
            String price = editItemPrice.getText().toString().trim();
            String category = spinnerCategory.getSelectedItem() != null 
                    ? spinnerCategory.getSelectedItem().toString() : "";

            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter item name", Toast.LENGTH_SHORT).show();
                return;
            }

            if (price.isEmpty()) {
                Toast.makeText(this, "Please enter price", Toast.LENGTH_SHORT).show();
                return;
            }

            Product newProduct = new Product(name, price, null);
            newProduct.setCategory(category);

            long result;
            if (editingProductId > 0) {
                newProduct.setId(editingProductId);
                result = productDatabaseHelper.updateProduct(newProduct, "", category, null);
                if (result > 0) {
                    Toast.makeText(this, "Item updated successfully", Toast.LENGTH_SHORT).show();
                }
            } else {
                result = productDatabaseHelper.insertProduct(newProduct, "", category, null);
                if (result > 0) {
                    Toast.makeText(this, "Item added successfully", Toast.LENGTH_SHORT).show();
                }
            }

            if (result > 0) {
                dialog.dismiss();
                loadProductsFromDatabase(); // Refresh list
            } else {
                Toast.makeText(this, "Error saving item", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void showDeleteConfirmation(Product product) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Item")
                .setMessage("Are you sure you want to delete \"" + product.getName() + "\"?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    int result = productDatabaseHelper.deleteProduct(product.getId());
                    if (result > 0) {
                        Toast.makeText(this, "Item deleted successfully", Toast.LENGTH_SHORT).show();
                        loadProductsFromDatabase(); // Refresh list
                    } else {
                        Toast.makeText(this, "Error deleting item", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProductsFromDatabase(); // Refresh when returning to this activity
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}



