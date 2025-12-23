package com.example.habibu_clement_work12;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class ExploreAdapter extends RecyclerView.Adapter<ExploreAdapter.ExploreViewHolder> {
    private List<ExploreItem> items;

    public ExploreAdapter(List<ExploreItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ExploreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_explore, parent, false);
        return new ExploreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExploreViewHolder holder, int position) {
        ExploreItem item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ExploreViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgExplore;
        private TextView txtCategory;
        private TextView txtTitle;
        private TextView txtDescription;
        private TextView txtEngagement;
        private Button btnExplore;

        public ExploreViewHolder(@NonNull View itemView) {
            super(itemView);
            imgExplore = itemView.findViewById(R.id.imgExplore);
            txtCategory = itemView.findViewById(R.id.txtCategory);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            txtEngagement = itemView.findViewById(R.id.txtEngagement);
            btnExplore = itemView.findViewById(R.id.btnExplore);

            itemView.setOnClickListener(v -> {
                Toast.makeText(itemView.getContext(), "Exploring: " + txtTitle.getText(), 
                    Toast.LENGTH_SHORT).show();
            });

            btnExplore.setOnClickListener(v -> {
                Toast.makeText(itemView.getContext(), "Exploring: " + txtTitle.getText(), 
                    Toast.LENGTH_SHORT).show();
            });
        }

        public void bind(ExploreItem item) {
            // Load image
            Glide.with(itemView.getContext())
                    .load(item.getImageUrl())
                    .centerCrop()
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_menu_report_image)
                    .into(imgExplore);

            txtCategory.setText(item.getCategory());
            txtTitle.setText(item.getTitle());
            txtDescription.setText(item.getDescription());

            // Format engagement count
            String engagementText;
            if (item.getEngagementCount() >= 1000) {
                engagementText = String.format("%.1fK engaged", item.getEngagementCount() / 1000.0);
            } else {
                engagementText = item.getEngagementCount() + " engaged";
            }
            txtEngagement.setText(engagementText);
        }
    }
}









