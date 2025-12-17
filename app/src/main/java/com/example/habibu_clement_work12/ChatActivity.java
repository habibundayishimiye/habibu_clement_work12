package com.example.habibu_clement_work12;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerViewChatMessages;
    private ChatMessageAdapter chatMessageAdapter;
    private EditText editMessageInput;
    private ImageButton btnSendMessage;
    private String userName;
    private String userAvatarUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Get user info from intent
        userName = getIntent().getStringExtra("USER_NAME");
        userAvatarUrl = getIntent().getStringExtra("USER_AVATAR");

        if (userName == null) {
            userName = "User";
        }
        if (userAvatarUrl == null) {
            userAvatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=100&h=100&fit=crop";
        }

        // Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Setup user info in toolbar
        TextView txtChatUserName = findViewById(R.id.txtChatUserName);
        ImageView imgChatAvatar = findViewById(R.id.imgChatAvatar);
        
        txtChatUserName.setText(userName);
        Glide.with(this)
                .load(userAvatarUrl)
                .circleCrop()
                .placeholder(android.R.drawable.sym_def_app_icon)
                .error(android.R.drawable.sym_def_app_icon)
                .into(imgChatAvatar);

        // Initialize views
        recyclerViewChatMessages = findViewById(R.id.recyclerViewChatMessages);
        editMessageInput = findViewById(R.id.editMessageInput);
        btnSendMessage = findViewById(R.id.btnSendMessage);

        // Setup RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true); // Scroll to bottom
        recyclerViewChatMessages.setLayoutManager(layoutManager);

        // Create initial messages
        List<ChatMessage> messages = createInitialMessages();
        chatMessageAdapter = new ChatMessageAdapter(messages);
        recyclerViewChatMessages.setAdapter(chatMessageAdapter);

        // Scroll to bottom
        recyclerViewChatMessages.post(() -> recyclerViewChatMessages.smoothScrollToPosition(messages.size() - 1));

        // Setup send button
        btnSendMessage.setOnClickListener(v -> sendMessage());

        // Setup enter key to send
        editMessageInput.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == android.view.KeyEvent.ACTION_DOWN && keyCode == android.view.KeyEvent.KEYCODE_ENTER) {
                sendMessage();
                return true;
            }
            return false;
        });
    }

    private List<ChatMessage> createInitialMessages() {
        List<ChatMessage> messages = new ArrayList<>();
        // Add some initial conversation messages
        messages.add(new ChatMessage("Hello! Is this item still available?", false));
        messages.add(new ChatMessage("Yes, it's still available! Would you like to see it?", true));
        messages.add(new ChatMessage("That would be great! When are you available?", false));
        messages.add(new ChatMessage("I'm free tomorrow afternoon. Does that work for you?", true));
        return messages;
    }

    private void sendMessage() {
        String messageText = editMessageInput.getText().toString().trim();
        if (!messageText.isEmpty()) {
            // Create new message (sent by current user)
            ChatMessage newMessage = new ChatMessage(messageText, true);
            chatMessageAdapter.addMessage(newMessage);
            
            // Clear input
            editMessageInput.setText("");
            
            // Scroll to bottom
            recyclerViewChatMessages.post(() -> {
                recyclerViewChatMessages.smoothScrollToPosition(chatMessageAdapter.getItemCount() - 1);
            });

            // Simulate response after a delay (optional)
            simulateResponse(messageText);
        }
    }

    private void simulateResponse(String userMessage) {
        // Simulate an automatic response after 1 second
        recyclerViewChatMessages.postDelayed(() -> {
            String response = generateResponse(userMessage);
            ChatMessage responseMessage = new ChatMessage(response, false);
            chatMessageAdapter.addMessage(responseMessage);
            recyclerViewChatMessages.smoothScrollToPosition(chatMessageAdapter.getItemCount() - 1);
        }, 1000);
    }

    private String generateResponse(String userMessage) {
        String lowerMessage = userMessage.toLowerCase();
        if (lowerMessage.contains("price") || lowerMessage.contains("cost") || lowerMessage.contains("how much")) {
            return "The price is negotiable. What's your budget?";
        } else if (lowerMessage.contains("location") || lowerMessage.contains("where") || lowerMessage.contains("pick up")) {
            return "I'm located in San Francisco. We can meet at a public place.";
        } else if (lowerMessage.contains("available") || lowerMessage.contains("still")) {
            return "Yes, it's still available!";
        } else if (lowerMessage.contains("meet") || lowerMessage.contains("when") || lowerMessage.contains("time")) {
            return "I'm flexible with timing. What works best for you?";
        } else {
            return "Thanks for your message! I'll get back to you soon.";
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}

