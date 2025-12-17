package com.example.habibu_clement_work12;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private List<Post> posts;

    public PostAdapter(List<Post> posts) {
        this.posts = posts;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class PostViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgAvatar;
        private TextView txtUserName;
        private TextView txtPostTime;
        private TextView txtPostText;
        private ImageView imgPostImage;
        private TextView txtLikeCount;
        private TextView txtCommentShare;
        private Button btnLike;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
            txtUserName = itemView.findViewById(R.id.txtUserName);
            txtPostTime = itemView.findViewById(R.id.txtPostTime);
            txtPostText = itemView.findViewById(R.id.txtPostText);
            imgPostImage = itemView.findViewById(R.id.imgPostImage);
            txtLikeCount = itemView.findViewById(R.id.txtLikeCount);
            txtCommentShare = itemView.findViewById(R.id.txtCommentShare);
            btnLike = itemView.findViewById(R.id.btnLike);
        }

        public void bind(Post post) {
            // Load user avatar
            Glide.with(itemView.getContext())
                    .load(post.getUserAvatar())
                    .circleCrop()
                    .placeholder(android.R.drawable.sym_def_app_icon)
                    .error(android.R.drawable.sym_def_app_icon)
                    .into(imgAvatar);

            txtUserName.setText(post.getUserName());
            txtPostTime.setText(post.getPostTime());
            txtPostText.setText(post.getPostText());

            // Load post image if available
            if (post.getPostImageUrl() != null && !post.getPostImageUrl().isEmpty()) {
                imgPostImage.setVisibility(View.VISIBLE);
                Glide.with(itemView.getContext())
                        .load(post.getPostImageUrl())
                        .centerCrop()
                        .placeholder(android.R.drawable.ic_menu_gallery)
                        .error(android.R.drawable.ic_menu_report_image)
                        .into(imgPostImage);
            } else {
                imgPostImage.setVisibility(View.GONE);
            }

            // Update like count
            String likeText = post.getLikeCount() + (post.getLikeCount() == 1 ? " like" : " likes");
            txtLikeCount.setText(likeText);

            // Update comment and share count
            String commentShareText = post.getCommentCount() + " comments · " + post.getShareCount() + " shares";
            txtCommentShare.setText(commentShareText);

            // Update like button state
            if (post.isLiked()) {
                btnLike.setText("Liked");
                btnLike.setTextColor(itemView.getContext().getResources().getColor(android.R.color.holo_blue_dark, null));
                btnLike.setCompoundDrawablesWithIntrinsicBounds(
                        android.R.drawable.btn_star_big_on, 0, 0, 0);
            } else {
                btnLike.setText("Like");
                btnLike.setTextColor(itemView.getContext().getResources().getColor(android.R.color.darker_gray, null));
                btnLike.setCompoundDrawablesWithIntrinsicBounds(
                        android.R.drawable.btn_star_big_off, 0, 0, 0);
            }

            // Like button click handler
            btnLike.setOnClickListener(v -> {
                boolean newLikeState = !post.isLiked();
                post.setLiked(newLikeState);
                int newLikeCount = post.getLikeCount() + (newLikeState ? 1 : -1);
                post.setLikeCount(Math.max(0, newLikeCount));
                
                // Update UI
                if (post.isLiked()) {
                    btnLike.setText("Liked");
                    btnLike.setTextColor(itemView.getContext().getResources().getColor(android.R.color.holo_blue_dark, null));
                    btnLike.setCompoundDrawablesWithIntrinsicBounds(
                            android.R.drawable.btn_star_big_on, 0, 0, 0);
                } else {
                    btnLike.setText("Like");
                    btnLike.setTextColor(itemView.getContext().getResources().getColor(android.R.color.darker_gray, null));
                    btnLike.setCompoundDrawablesWithIntrinsicBounds(
                            android.R.drawable.btn_star_big_off, 0, 0, 0);
                }
                String likeTextUpdated = post.getLikeCount() + (post.getLikeCount() == 1 ? " like" : " likes");
                txtLikeCount.setText(likeTextUpdated);
            });

            // Comment button
            Button btnComment = itemView.findViewById(R.id.btnComment);
            btnComment.setOnClickListener(v -> {
                android.widget.Toast.makeText(itemView.getContext(),
                        "Comment feature coming soon!", android.widget.Toast.LENGTH_SHORT).show();
            });

            // Share button
            Button btnShare = itemView.findViewById(R.id.btnShare);
            btnShare.setOnClickListener(v -> {
                android.widget.Toast.makeText(itemView.getContext(),
                        "Share feature coming soon!", android.widget.Toast.LENGTH_SHORT).show();
            });
        }
    }
}

