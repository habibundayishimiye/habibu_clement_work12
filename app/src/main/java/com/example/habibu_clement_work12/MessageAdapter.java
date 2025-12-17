package com.example.habibu_clement_work12;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Message> messages;

    public MessageAdapter(List<Message> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.txtSenderName.setText(message.getSenderName());
        holder.txtMessagePreview.setText(message.getMessagePreview());
        holder.txtMessageTime.setText(message.getTime());
        
        // Load avatar image
        if (message.getAvatarUrl() != null && !message.getAvatarUrl().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(message.getAvatarUrl())
                    .circleCrop()
                    .placeholder(android.R.drawable.sym_def_app_icon)
                    .error(android.R.drawable.sym_def_app_icon)
                    .into(holder.imgAvatar);
        }
        
        // Show/hide unread badge
        if (message.hasUnread()) {
            holder.badgeUnread.setVisibility(View.VISIBLE);
        } else {
            holder.badgeUnread.setVisibility(View.GONE);
        }
        
        // Set click listener to open chat
        holder.itemView.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(holder.itemView.getContext(), ChatActivity.class);
            intent.putExtra("USER_NAME", message.getSenderName());
            intent.putExtra("USER_AVATAR", message.getAvatarUrl());
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAvatar;
        TextView txtSenderName;
        TextView txtMessagePreview;
        TextView txtMessageTime;
        View badgeUnread;

        MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.imgMessageAvatar);
            txtSenderName = itemView.findViewById(R.id.txtSenderName);
            txtMessagePreview = itemView.findViewById(R.id.txtMessagePreview);
            txtMessageTime = itemView.findViewById(R.id.txtMessageTime);
            badgeUnread = itemView.findViewById(R.id.badgeUnread);
        }
    }
}

