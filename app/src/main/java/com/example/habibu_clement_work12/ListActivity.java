package com.example.habibu_clement_work12;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private ListView listView;

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

        // Initialize ListView
        listView = findViewById(R.id.listView);

        // Create sample data
        List<String> items = createSampleItems();

        // Setup ArrayAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                items
        );

        listView.setAdapter(adapter);
    }

    private List<String> createSampleItems() {
        List<String> items = new ArrayList<>();
        items.add("Sofa - $450");
        items.add("Bicycle - $280");
        items.add("Coffee Maker - $120");
        items.add("Table - $85");
        items.add("Chair - $320");
        items.add("Lamp - $150");
        items.add("Desk - $200");
        items.add("Bookshelf - $180");
        items.add("Mirror - $75");
        items.add("Carpet - $250");
        return items;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}



