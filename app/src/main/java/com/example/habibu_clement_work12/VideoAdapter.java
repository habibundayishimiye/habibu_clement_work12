package com.example.habibu_clement_work12;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {
    private List<Video> videos;

    public VideoAdapter(List<Video> videos) {
        this.videos = videos;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_video, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        Video video = videos.get(position);
        holder.bind(video);
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgThumbnail;
        private ImageView imgCreatorAvatar;
        private TextView txtTitle;
        private TextView txtCreator;
        private TextView txtViews;
        private TextView txtDuration;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            imgThumbnail = itemView.findViewById(R.id.imgThumbnail);
            imgCreatorAvatar = itemView.findViewById(R.id.imgCreatorAvatar);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtCreator = itemView.findViewById(R.id.txtCreator);
            txtViews = itemView.findViewById(R.id.txtViews);
            txtDuration = itemView.findViewById(R.id.txtDuration);

            itemView.setOnClickListener(v -> {
                Toast.makeText(itemView.getContext(), "Playing video: " + txtTitle.getText(), 
                    Toast.LENGTH_SHORT).show();
            });
        }

        public void bind(Video video) {
            // Load video thumbnail
            Glide.with(itemView.getContext())
                    .load(video.getThumbnailUrl())
                    .centerCrop()
                    .placeholder(android.R.drawable.ic_media_play)
                    .error(android.R.drawable.ic_media_play)
                    .into(imgThumbnail);

            // Load creator avatar
            Glide.with(itemView.getContext())
                    .load(video.getCreatorAvatar())
                    .circleCrop()
                    .placeholder(android.R.drawable.sym_def_app_icon)
                    .error(android.R.drawable.sym_def_app_icon)
                    .into(imgCreatorAvatar);

            txtTitle.setText(video.getTitle());
            txtCreator.setText(video.getCreator());
            txtDuration.setText(video.getDuration());

            // Format view count
            String viewText;
            if (video.getViewCount() >= 1000) {
                viewText = String.format("%.1fK views", video.getViewCount() / 1000.0);
            } else {
                viewText = video.getViewCount() + " views";
            }
            txtViews.setText(viewText + " · " + video.getTimeAgo());
        }
    }
}




