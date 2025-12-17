package com.example.habibu_clement_work12;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewProducts;
    private ProductAdapter productAdapter;
    private SearchView searchView;
    private ImageView imgProfile;
    private TextView txtLocation;
    
    // Content views
    private View viewProducts;
    private View viewCategories;
    private View viewProfile;
    private View viewMessenger;
    private View viewHome;
    private View viewWatch;
    private View viewExplore;
    private View viewSell;
    private View viewLocation;
    
    // RecyclerViews
    private RecyclerView recyclerViewCategories;
    private RecyclerView recyclerViewMessages;
    private MessageAdapter messageAdapter;
    private RecyclerView recyclerViewPosts;
    private PostAdapter postAdapter;
    private RecyclerView recyclerViewVideos;
    private VideoAdapter videoAdapter;
    private RecyclerView recyclerViewExplore;
    private ExploreAdapter exploreAdapter;
    
    // Camera and image selection
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private static final int REQUEST_STORAGE_PERMISSION = 101;
    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<Intent> galleryLauncher;
    private ImageView selectedPhotoImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Enable edge-to-edge display
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getWindow().setDecorFitsSystemWindows(false);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            );
        }
        
        setContentView(R.layout.activity_main);

        // Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        // Handle window insets to start content below status bar
        View mainLayout = findViewById(R.id.mainCoordinatorLayout);
        ViewCompat.setOnApplyWindowInsetsListener(mainLayout, (v, insets) -> {
            int topInset = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top;
            
            // Apply padding to AppBarLayout to account for status bar
            com.google.android.material.appbar.AppBarLayout appBarLayout = findViewById(R.id.appBarLayout);
            if (appBarLayout != null) {
                appBarLayout.setPadding(0, topInset, 0, 0);
            }
            
            // Also handle bottom navigation bar inset
            int bottomInset = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom;
            LinearLayout bottomNav = findViewById(R.id.bottomNavigationBar);
            if (bottomNav != null) {
                bottomNav.setPadding(bottomNav.getPaddingLeft(), bottomNav.getPaddingTop(), 
                    bottomNav.getPaddingRight(), bottomInset);
            }
            
            return insets;
        });

        // Initialize views
        searchView = findViewById(R.id.searchView);
        recyclerViewProducts = findViewById(R.id.recyclerViewProducts);
        imgProfile = findViewById(R.id.imgProfile);
        txtLocation = findViewById(R.id.txtLocation);
        
        // Initialize content views
        viewProducts = findViewById(R.id.recyclerViewProducts);
        viewCategories = findViewById(R.id.viewCategories);
        viewProfile = findViewById(R.id.viewProfile);
        viewMessenger = findViewById(R.id.viewMessenger);
        viewHome = findViewById(R.id.viewHome);
        viewWatch = findViewById(R.id.viewWatch);
        viewExplore = findViewById(R.id.viewExplore);
        viewSell = findViewById(R.id.viewSell);
        viewLocation = findViewById(R.id.viewLocation);
        
        recyclerViewCategories = findViewById(R.id.recyclerViewCategories);

        // Load profile image
        loadProfileImage();

        // Setup Products RecyclerView with GridLayoutManager (2 columns)
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerViewProducts.setLayoutManager(layoutManager);

        // Create sample products
        List<Product> products = createSampleProducts();
        productAdapter = new ProductAdapter(products);
        recyclerViewProducts.setAdapter(productAdapter);
        
        // Setup Categories RecyclerView
        setupCategoriesView();
        
        // Setup Profile view
        setupProfileView();
        
        // Setup Messenger view
        setupMessengerView();
        
        // Setup Home view
        setupHomeView();
        
        // Setup Watch view
        setupWatchView();
        
        // Setup Explore view
        setupExploreView();
        
        // Show products view by default
        showView(viewProducts);

        // Setup button click listeners
        setupButtonListeners();

        // Setup SearchView
        setupSearchView();
        
        // Initialize camera and gallery launchers
        setupCameraAndGallery();
    }
    
    private void setupCameraAndGallery() {
        // Camera launcher
        cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == android.app.Activity.RESULT_OK && result.getData() != null) {
                    Bundle extras = result.getData().getExtras();
                    if (extras != null) {
                        Bitmap imageBitmap = (Bitmap) extras.get("data");
                        if (imageBitmap != null) {
                            if (selectedPhotoImageView != null) {
                                selectedPhotoImageView.setImageBitmap(imageBitmap);
                                selectedPhotoImageView.setVisibility(View.VISIBLE);
                                View placeholder = findViewById(R.id.layoutPhotoPlaceholder);
                                if (placeholder != null) {
                                    placeholder.setVisibility(View.GONE);
                                }
                                Toast.makeText(this, "Photo captured successfully!", Toast.LENGTH_SHORT).show();
                            } else {
                                // Camera button clicked but no image view selected - open sell form
                                Toast.makeText(this, "Photo captured! Please add it to your listing.", Toast.LENGTH_SHORT).show();
                                showSellInterface();
                                // Set the image view for sell form
                                ImageView imgSellPhoto = findViewById(R.id.imgSellPhoto);
                                if (imgSellPhoto != null) {
                                    selectedPhotoImageView = imgSellPhoto;
                                    imgSellPhoto.setImageBitmap(imageBitmap);
                                    imgSellPhoto.setVisibility(View.VISIBLE);
                                    View placeholder = findViewById(R.id.layoutPhotoPlaceholder);
                                    if (placeholder != null) {
                                        placeholder.setVisibility(View.GONE);
                                    }
                                }
                            }
                        }
                    }
                } else if (result.getResultCode() == android.app.Activity.RESULT_CANCELED) {
                    Toast.makeText(this, "Camera action cancelled", Toast.LENGTH_SHORT).show();
                }
            }
        );
        
        // Gallery launcher - using GetContent for better compatibility
        galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == android.app.Activity.RESULT_OK && result.getData() != null) {
                    Uri selectedImageUri = result.getData().getData();
                    if (selectedImageUri != null) {
                        if (selectedPhotoImageView != null) {
                            selectedPhotoImageView.setVisibility(View.VISIBLE);
                            View placeholder = findViewById(R.id.layoutPhotoPlaceholder);
                            if (placeholder != null) {
                                placeholder.setVisibility(View.GONE);
                            }
                            Glide.with(this)
                                .load(selectedImageUri)
                                .centerCrop()
                                .placeholder(android.R.drawable.ic_menu_camera)
                                .error(android.R.drawable.ic_menu_report_image)
                                .into(selectedPhotoImageView);
                            Toast.makeText(this, "Photo selected successfully!", Toast.LENGTH_SHORT).show();
                        } else {
                            // Gallery opened but no image view selected - open sell form
                            Toast.makeText(this, "Photo selected! Please add it to your listing.", Toast.LENGTH_SHORT).show();
                            showSellInterface();
                            // Set the image view for sell form
                            ImageView imgSellPhoto = findViewById(R.id.imgSellPhoto);
                            if (imgSellPhoto != null) {
                                selectedPhotoImageView = imgSellPhoto;
                                imgSellPhoto.setVisibility(View.VISIBLE);
                                View placeholder = findViewById(R.id.layoutPhotoPlaceholder);
                                if (placeholder != null) {
                                    placeholder.setVisibility(View.GONE);
                                }
                                Glide.with(this)
                                    .load(selectedImageUri)
                                    .centerCrop()
                                    .placeholder(android.R.drawable.ic_menu_camera)
                                    .error(android.R.drawable.ic_menu_report_image)
                                    .into(imgSellPhoto);
                            }
                        }
                    }
                } else if (result.getResultCode() == android.app.Activity.RESULT_CANCELED) {
                    Toast.makeText(this, "Gallery selection cancelled", Toast.LENGTH_SHORT).show();
                }
            }
        );
    }

    private void loadProfileImage() {
        // Load profile image from URL
        String profileImageUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=200&h=200&fit=crop";
        Glide.with(this)
                .load(profileImageUrl)
                .circleCrop()
                .placeholder(android.R.drawable.sym_def_app_icon)
                .error(android.R.drawable.sym_def_app_icon)
                .into(imgProfile);
    }

    private List<Product> createSampleProducts() {
        List<Product> products = new ArrayList<>();
        // Using Unsplash images for real product photos
        products.add(new Product("Sofa", "$450", "https://images.unsplash.com/photo-1555041469-a586c61ea9bc?w=800&h=600&fit=crop"));
        products.add(new Product("Bicycle", "$280", "https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=800&h=600&fit=crop"));
        products.add(new Product("Coffee Maker", "$120", "https://images.unsplash.com/photo-1517487881594-2787fef5ebf7?w=800&h=600&fit=crop"));
        products.add(new Product("Table", "$85", "https://images.unsplash.com/photo-1532372320572-cda25653a26d?w=800&h=600&fit=crop"));
        products.add(new Product("Chair", "$320", "https://images.unsplash.com/photo-1506439773649-6e0eb8cfb237?w=800&h=600&fit=crop"));
        products.add(new Product("Lamp", "$150", "https://images.unsplash.com/photo-1507473885765-e6ed057f782c?w=800&h=600&fit=crop"));
        return products;
    }

    private void setupButtonListeners() {
        // Navigation buttons - wire Your Items button to open ListActivity
        findViewById(R.id.btnYourItems).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ListActivity.class);
            startActivity(intent);
        });
        
        // Add option to navigate to Activity3 (Database) from menu
        // This will be accessible via the menu options

        // Sell button - Show sell/create listing form
        findViewById(R.id.btnSell).setOnClickListener(v -> {
            showSellInterface();
        });

        // Categories button - Show categories view
        findViewById(R.id.btnCategories).setOnClickListener(v -> {
            showCategories();
        });

        // Search navigation button - Show products and focus search
        findViewById(R.id.btnSearchNav).setOnClickListener(v -> {
            showView(viewProducts);
            searchView.setIconified(false);
            searchView.requestFocus();
        });

        // Top bar buttons
        findViewById(R.id.btnMessenger).setOnClickListener(v -> {
            showMessengerContent();
        });

        findViewById(R.id.btnProfile).setOnClickListener(v -> {
            showProfileContent();
        });

        findViewById(R.id.btnMenu).setOnClickListener(v -> {
            showMenuOptions();
        });

        // Bottom navigation buttons
        findViewById(R.id.btnHome).setOnClickListener(v -> {
            showHomeContent();
        });

        findViewById(R.id.btnWatch).setOnClickListener(v -> {
            showWatchContent();
        });

        findViewById(R.id.btnMarketplace).setOnClickListener(v -> {
            refreshMarketplace();
        });

        findViewById(R.id.btnGlobe).setOnClickListener(v -> {
            showExploreContent();
        });

        findViewById(R.id.btnBottomMenu).setOnClickListener(v -> {
            Toast.makeText(this, "Menu: More options", Toast.LENGTH_SHORT).show();
            showBottomMenuOptions();
        });

        // Location change button
        findViewById(R.id.btnChangeLocation).setOnClickListener(v -> {
            changeLocation();
        });
        
        // Camera button in toolbar - opens camera/gallery picker
        findViewById(R.id.btnCamera).setOnClickListener(v -> {
            // Check if we're in sell view, if so use that image view
            View sellView = findViewById(R.id.viewSell);
            if (sellView != null && sellView.getVisibility() == View.VISIBLE) {
                ImageView imgSellPhoto = findViewById(R.id.imgSellPhoto);
                if (imgSellPhoto != null) {
                    selectedPhotoImageView = imgSellPhoto;
                }
            } else {
                selectedPhotoImageView = null; // Will open sell form after photo selection
            }
            openCameraOrGallery();
        });
    }

    private void setupCategoriesView() {
        recyclerViewCategories.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));
        List<Category> categories = createCategories();
        CategoryAdapter categoryAdapter = new CategoryAdapter(categories);
        recyclerViewCategories.setAdapter(categoryAdapter);
    }
    
    private void setupProfileView() {
        // Setup profile image
        ImageView imgProfileLarge = findViewById(R.id.imgProfileLarge);
        if (imgProfileLarge != null) {
            String profileImageUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=200&h=200&fit=crop";
            Glide.with(this)
                    .load(profileImageUrl)
                    .circleCrop()
                    .placeholder(android.R.drawable.sym_def_app_icon)
                    .error(android.R.drawable.sym_def_app_icon)
                    .into(imgProfileLarge);
        }
        
        // Setup profile name
        TextView txtProfileName = findViewById(R.id.txtProfileName);
        if (txtProfileName != null) {
            txtProfileName.setText("Your Name");
        }
        
        // Setup profile email
        TextView txtProfileEmail = findViewById(R.id.txtProfileEmail);
        if (txtProfileEmail != null) {
            txtProfileEmail.setText("your.email@example.com");
        }
        
        // Setup profile location (synchronize with current location)
        TextView txtProfileLocation = findViewById(R.id.txtProfileLocation);
        if (txtProfileLocation != null) {
            txtProfileLocation.setText("📍 Kigali, Rwanda");
        }
        
        // Setup profile stats
        TextView txtItemsCount = findViewById(R.id.txtItemsCount);
        if (txtItemsCount != null) {
            txtItemsCount.setText("12");
        }
        
        TextView txtSalesCount = findViewById(R.id.txtSalesCount);
        if (txtSalesCount != null) {
            txtSalesCount.setText("8");
        }
        
        TextView txtRating = findViewById(R.id.txtRating);
        if (txtRating != null) {
            txtRating.setText("4.8");
        }
        
        // Setup about text
        TextView txtAbout = findViewById(R.id.txtAbout);
        if (txtAbout != null) {
            txtAbout.setText("Active seller on Marketplace. Always looking for great deals and happy to help others find what they need! 📦✨");
        }
        
        // Setup Edit Profile button
        findViewById(R.id.btnEditProfile).setOnClickListener(v -> {
            Toast.makeText(this, "Edit Profile feature coming soon!", Toast.LENGTH_SHORT).show();
        });
        
        // Setup Share Profile button
        findViewById(R.id.btnShareProfile).setOnClickListener(v -> {
            Toast.makeText(this, "Sharing profile...", Toast.LENGTH_SHORT).show();
        });
        
        // Setup My Items RecyclerView
        RecyclerView recyclerViewMyItems = findViewById(R.id.recyclerViewMyItems);
        if (recyclerViewMyItems != null) {
            recyclerViewMyItems.setLayoutManager(new androidx.recyclerview.widget.GridLayoutManager(this, 2));
            List<Product> myItems = createMyItems();
            ProductAdapter myItemsAdapter = new ProductAdapter(myItems);
            recyclerViewMyItems.setAdapter(myItemsAdapter);
        }
    }
    
    private List<Product> createMyItems() {
        List<Product> items = new ArrayList<>();
        items.add(new Product("Vintage Coffee Table", "$45", "https://images.unsplash.com/photo-1586023492125-27b2c045efd7?w=400&h=400&fit=crop"));
        items.add(new Product("Wireless Headphones", "$75", "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=400&h=400&fit=crop"));
        items.add(new Product("Garden Chairs (Set of 4)", "$120", "https://images.unsplash.com/photo-1586023492125-27b2c045efd7?w=400&h=400&fit=crop"));
        items.add(new Product("Smartphone", "$250", "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=400&h=400&fit=crop"));
        return items;
    }
    
    private void setupMessengerView() {
        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
        if (recyclerViewMessages != null) {
            recyclerViewMessages.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));
            List<Message> messages = createSampleMessages();
            messageAdapter = new MessageAdapter(messages);
            recyclerViewMessages.setAdapter(messageAdapter);
        }
    }
    
    private List<Message> createSampleMessages() {
        List<Message> messages = new ArrayList<>();
        messages.add(new Message(
            "John Doe",
            "Hey! Are you still interested in the sofa?",
            "2h",
            "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=100&h=100&fit=crop",
            true
        ));
        messages.add(new Message(
            "Sarah Smith",
            "The bicycle is still available. When can you pick it up?",
            "5h",
            "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=100&h=100&fit=crop",
            true
        ));
        messages.add(new Message(
            "Mike Johnson",
            "Thanks for the coffee maker! It works perfectly.",
            "1d",
            "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=100&h=100&fit=crop",
            false
        ));
        messages.add(new Message(
            "Emma Wilson",
            "Is the table still available?",
            "1d",
            "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=100&h=100&fit=crop",
            false
        ));
        messages.add(new Message(
            "David Brown",
            "I'm interested in the chair. Can we meet tomorrow?",
            "2d",
            "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=100&h=100&fit=crop",
            false
        ));
        messages.add(new Message(
            "Lisa Anderson",
            "The lamp looks great in my living room. Thanks again!",
            "3d",
            "https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=100&h=100&fit=crop",
            false
        ));
        return messages;
    }
    
    private void setupHomeView() {
        recyclerViewPosts = findViewById(R.id.recyclerViewPosts);
        if (recyclerViewPosts != null) {
            recyclerViewPosts.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));
            List<Post> posts = createSamplePosts();
            postAdapter = new PostAdapter(posts);
            recyclerViewPosts.setAdapter(postAdapter);
        }
        
        // Setup composer avatar
        ImageView imgComposerAvatar = findViewById(R.id.imgComposerAvatar);
        if (imgComposerAvatar != null) {
            String profileImageUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=100&h=100&fit=crop";
            Glide.with(this)
                    .load(profileImageUrl)
                    .circleCrop()
                    .placeholder(android.R.drawable.sym_def_app_icon)
                    .error(android.R.drawable.sym_def_app_icon)
                    .into(imgComposerAvatar);
        }
        
        // Setup composer buttons
        findViewById(R.id.btnComposerPhoto).setOnClickListener(v -> {
            Toast.makeText(this, "Create a post with photo", Toast.LENGTH_SHORT).show();
        });
        findViewById(R.id.btnComposerVideo).setOnClickListener(v -> {
            Toast.makeText(this, "Create a post with video", Toast.LENGTH_SHORT).show();
        });
        findViewById(R.id.btnComposerFeeling).setOnClickListener(v -> {
            Toast.makeText(this, "Share how you're feeling", Toast.LENGTH_SHORT).show();
        });
    }
    
    private List<Post> createSamplePosts() {
        List<Post> posts = new ArrayList<>();
        
        posts.add(new Post(
            "Sarah Johnson",
            "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=100&h=100&fit=crop",
            "2 hours ago",
            "Just finished organizing my garage sale! Come by this weekend to check out great deals on furniture, electronics, and more! 🏠✨",
            "https://images.unsplash.com/photo-1586023492125-27b2c045efd7?w=800&h=600&fit=crop",
            124,
            23,
            8,
            false
        ));
        
        posts.add(new Post(
            "Michael Chen",
            "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=100&h=100&fit=crop",
            "5 hours ago",
            "Selling my vintage bicycle collection. All in excellent condition! DM for more details.",
            "https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=800&h=600&fit=crop",
            89,
            15,
            5,
            true
        ));
        
        posts.add(new Post(
            "Emma Williams",
            "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=100&h=100&fit=crop",
            "1 day ago",
            "Spring cleaning sale! Everything must go. Prices are negotiable. Feel free to message me!",
            "",
            67,
            12,
            3,
            false
        ));
        
        posts.add(new Post(
            "David Martinez",
            "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=100&h=100&fit=crop",
            "1 day ago",
            "Looking to buy a quality dining table. If you have one for sale, please reach out!",
            "https://images.unsplash.com/photo-1586023492125-27b2c045efd7?w=800&h=600&fit=crop",
            45,
            8,
            2,
            false
        ));
        
        posts.add(new Post(
            "Lisa Anderson",
            "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=100&h=100&fit=crop",
            "2 days ago",
            "Giving away some plants! They're growing too big for my apartment. Free to a good home! 🌱",
            "https://images.unsplash.com/photo-1466692476868-aef1dfb1e735?w=800&h=600&fit=crop",
            156,
            34,
            12,
            true
        ));
        
        posts.add(new Post(
            "James Wilson",
            "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=100&h=100&fit=crop",
            "3 days ago",
            "New items added to my marketplace! Check them out and let me know if you're interested.",
            "",
            92,
            18,
            6,
            false
        ));
        
        return posts;
    }
    
    private void setupWatchView() {
        recyclerViewVideos = findViewById(R.id.recyclerViewVideos);
        if (recyclerViewVideos != null) {
            recyclerViewVideos.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));
            List<Video> videos = createSampleVideos();
            videoAdapter = new VideoAdapter(videos);
            recyclerViewVideos.setAdapter(videoAdapter);
        }
    }
    
    private List<Video> createSampleVideos() {
        List<Video> videos = new ArrayList<>();
        
        videos.add(new Video(
            "Best Marketplace Finds of the Week",
            "Sarah Johnson",
            "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=100&h=100&fit=crop",
            "https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=800&h=600&fit=crop",
            "8:45",
            1520,
            "3 hours ago"
        ));
        
        videos.add(new Video(
            "How to Negotiate Prices on Marketplace",
            "Mike Chen",
            "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=100&h=100&fit=crop",
            "https://images.unsplash.com/photo-1586023492125-27b2c045efd7?w=800&h=600&fit=crop",
            "12:30",
            2340,
            "5 hours ago"
        ));
        
        videos.add(new Video(
            "Hidden Gems: Vintage Shopping Tips",
            "Emma Williams",
            "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=100&h=100&fit=crop",
            "https://images.unsplash.com/photo-1483985988355-763728e1935b?w=800&h=600&fit=crop",
            "6:20",
            890,
            "1 day ago"
        ));
        
        videos.add(new Video(
            "Marketplace Safety Guide - What You Need to Know",
            "David Martinez",
            "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=100&h=100&fit=crop",
            "https://images.unsplash.com/photo-1556742049-0cfed4f6a45d?w=800&h=600&fit=crop",
            "15:12",
            3420,
            "1 day ago"
        ));
        
        videos.add(new Video(
            "My Biggest Marketplace Haul Yet!",
            "Lisa Anderson",
            "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=100&h=100&fit=crop",
            "https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=800&h=600&fit=crop",
            "10:05",
            5670,
            "2 days ago"
        ));
        
        videos.add(new Video(
            "Furniture Flip: Before & After Transformation",
            "James Wilson",
            "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=100&h=100&fit=crop",
            "https://images.unsplash.com/photo-1586023492125-27b2c045efd7?w=800&h=600&fit=crop",
            "7:33",
            1890,
            "3 days ago"
        ));
        
        return videos;
    }
    
    private void setupExploreView() {
        recyclerViewExplore = findViewById(R.id.recyclerViewExplore);
        if (recyclerViewExplore != null) {
            recyclerViewExplore.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));
            List<ExploreItem> exploreItems = createSampleExploreItems();
            exploreAdapter = new ExploreAdapter(exploreItems);
            recyclerViewExplore.setAdapter(exploreAdapter);
        }
    }
    
    private List<ExploreItem> createSampleExploreItems() {
        List<ExploreItem> items = new ArrayList<>();
        
        items.add(new ExploreItem(
            "Top Deals in Your Area",
            "Discover amazing deals from sellers near you. Find furniture, electronics, and more at great prices!",
            "https://images.unsplash.com/photo-1586023492125-27b2c045efd7?w=800&h=600&fit=crop",
            "Deals",
            3420
        ));
        
        items.add(new ExploreItem(
            "Vintage & Collectibles",
            "Explore unique vintage items and collectibles. From antiques to retro furniture, find something special.",
            "https://images.unsplash.com/photo-1483985988355-763728e1935b?w=800&h=600&fit=crop",
            "Vintage",
            2890
        ));
        
        items.add(new ExploreItem(
            "Free Items Near You",
            "People are giving away free items in your neighborhood. Check out what's available!",
            "https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=800&h=600&fit=crop",
            "Free",
            5670
        ));
        
        items.add(new ExploreItem(
            "Electronics & Gadgets",
            "Browse the latest electronics and gadgets from local sellers. Great deals on phones, laptops, and more.",
            "https://images.unsplash.com/photo-1556742049-0cfed4f6a45d?w=800&h=600&fit=crop",
            "Electronics",
            4120
        ));
        
        items.add(new ExploreItem(
            "Home & Garden Essentials",
            "Transform your home and garden. Find everything from plants to furniture and decor items.",
            "https://images.unsplash.com/photo-1466692476868-aef1dfb1e735?w=800&h=600&fit=crop",
            "Home & Garden",
            3250
        ));
        
        items.add(new ExploreItem(
            "Sports & Outdoor Gear",
            "Get active with sports equipment and outdoor gear. Bikes, camping gear, and fitness equipment.",
            "https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=800&h=600&fit=crop",
            "Sports",
            2180
        ));
        
        return items;
    }
    
    private List<Category> createCategories() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category("Electronics", "📱", "1,234 items"));
        categories.add(new Category("Furniture", "🪑", "856 items"));
        categories.add(new Category("Clothing", "👕", "2,450 items"));
        categories.add(new Category("Vehicles", "🚗", "342 items"));
        categories.add(new Category("Home & Garden", "🏠", "1,678 items"));
        categories.add(new Category("Sports & Outdoors", "⚽", "923 items"));
        categories.add(new Category("Toys & Games", "🎮", "567 items"));
        categories.add(new Category("Books & Media", "📚", "789 items"));
        return categories;
    }
    
    private void showView(View viewToShow) {
        // Hide all views
        viewProducts.setVisibility(View.GONE);
        viewCategories.setVisibility(View.GONE);
        viewProfile.setVisibility(View.GONE);
        viewMessenger.setVisibility(View.GONE);
        viewHome.setVisibility(View.GONE);
        viewWatch.setVisibility(View.GONE);
        viewExplore.setVisibility(View.GONE);
        viewSell.setVisibility(View.GONE);
        viewLocation.setVisibility(View.GONE);
        
        // Show the selected view
        viewToShow.setVisibility(View.VISIBLE);
    }

    private void showCategories() {
        showView(viewCategories);
    }
    
    private void showSellInterface() {
        showView(viewSell);
        setupSellForm();
    }
    
    private void setupSellForm() {
        // Setup category spinner
        android.widget.Spinner spinnerCategory = findViewById(R.id.spinnerCategory);
        if (spinnerCategory != null) {
            String[] categories = {"Select Category", "Electronics", "Furniture", "Clothing", 
                    "Vehicles", "Home & Garden", "Sports & Outdoors", "Toys & Games", "Books & Media"};
            android.widget.ArrayAdapter<String> adapter = new android.widget.ArrayAdapter<>(
                    this, android.R.layout.simple_spinner_item, categories);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCategory.setAdapter(adapter);
        }
        
        // Setup photo button - for sell form
        ImageView imgSellPhoto = findViewById(R.id.imgSellPhoto);
        if (imgSellPhoto != null) {
            findViewById(R.id.btnAddPhoto).setOnClickListener(v -> {
                selectedPhotoImageView = imgSellPhoto;
                openCameraOrGallery();
            });
            
            // Also handle clicks on the photo itself to change it
            imgSellPhoto.setOnClickListener(v -> {
                selectedPhotoImageView = imgSellPhoto;
                openCameraOrGallery();
            });
        }
        
        // Setup publish button
        findViewById(R.id.btnPublishListing).setOnClickListener(v -> {
            publishListing();
        });
    }
    
    private void publishListing() {
        android.widget.EditText editTitle = findViewById(R.id.editProductTitle);
        android.widget.EditText editPrice = findViewById(R.id.editProductPrice);
        android.widget.EditText editDescription = findViewById(R.id.editProductDescription);
        android.widget.Spinner spinnerCategory = findViewById(R.id.spinnerCategory);
        
        String title = editTitle.getText().toString().trim();
        String price = editPrice.getText().toString().trim();
        String description = editDescription.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem() != null 
                ? spinnerCategory.getSelectedItem().toString() : "";
        
        // Validate required fields
        if (title.isEmpty()) {
            Toast.makeText(this, "Please enter a product title", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (price.isEmpty()) {
            Toast.makeText(this, "Please enter a price", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (category.equals("Select Category") || category.isEmpty()) {
            Toast.makeText(this, "Please select a category", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Create new product and add to list
        Product newProduct = new Product(title, price, android.R.drawable.ic_menu_gallery);
        productAdapter.addProduct(newProduct);
        
        // Clear form
        editTitle.setText("");
        editPrice.setText("");
        editDescription.setText("");
        spinnerCategory.setSelection(0);
        
        // Show success message and switch to products view
        Toast.makeText(this, "Listing published successfully!", Toast.LENGTH_LONG).show();
        showView(viewProducts);
        recyclerViewProducts.smoothScrollToPosition(0);
    }

    private void showMessengerContent() {
        showView(viewMessenger);
    }

    private void showProfileContent() {
        showView(viewProfile);
    }

    private void showMenuOptions() {
        // Show menu options dialog
        String[] menuOptions = {
            "Student Database",
            "Settings",
            "Help & Support",
            "Privacy Policy",
            "Terms of Service",
            "About",
            "Log Out"
        };
        
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Menu");
        builder.setItems(menuOptions, (dialog, which) -> {
            switch (which) {
                case 0:
                    // Navigate to Activity3 (Student Database)
                    Intent intent = new Intent(MainActivity.this, Activity3.class);
                    startActivity(intent);
                    break;
                case 1:
                    Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(this, "Help & Support", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(this, "Privacy Policy", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(this, "Terms of Service", Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    Toast.makeText(this, "About Marketplace", Toast.LENGTH_SHORT).show();
                    break;
                case 6:
                    Toast.makeText(this, "Log Out", Toast.LENGTH_SHORT).show();
                    break;
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void showHomeContent() {
        showView(viewHome);
    }

    private void showWatchContent() {
        showView(viewWatch);
    }

    private void refreshMarketplace() {
        showView(viewProducts);
        recyclerViewProducts.smoothScrollToPosition(0);
    }

    private void showExploreContent() {
        showView(viewExplore);
    }

    private void showBottomMenuOptions() {
        // Show bottom menu options
        Toast.makeText(this, "More options...", Toast.LENGTH_SHORT).show();
        // You can show a popup menu with more options
    }

    private void changeLocation() {
        showView(viewLocation);
        setupLocationView();
    }
    
    private void setupLocationView() {
        RecyclerView recyclerViewLocations = findViewById(R.id.recyclerViewLocations);
        android.widget.EditText editSearchLocation = findViewById(R.id.editSearchLocation);
        android.widget.Button btnSearchLocation = findViewById(R.id.btnSearchLocation);
        
        if (recyclerViewLocations != null) {
            recyclerViewLocations.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));
            List<Location> locations = createPopularLocations();
            LocationAdapter locationAdapter = new LocationAdapter(locations, location -> {
                // Update location text
                String locationText = location.getName() + " - " + location.getDistance();
                txtLocation.setText(locationText);
                
                // Update profile location as well
                TextView txtProfileLocation = findViewById(R.id.txtProfileLocation);
                if (txtProfileLocation != null) {
                    txtProfileLocation.setText("📍 " + location.getName());
                }
                
                // Show success message and return to products view
                Toast.makeText(this, "Location changed to " + location.getName(), Toast.LENGTH_SHORT).show();
                showView(viewProducts);
            });
            recyclerViewLocations.setAdapter(locationAdapter);
        }
        
        if (btnSearchLocation != null) {
            btnSearchLocation.setOnClickListener(v -> {
                String searchText = editSearchLocation.getText().toString().trim();
                if (!searchText.isEmpty()) {
                    // Update location with searched location
                    String locationText = searchText + " - Custom";
                    txtLocation.setText(locationText);
                    
                    // Update profile location as well
                    TextView txtProfileLocation = findViewById(R.id.txtProfileLocation);
                    if (txtProfileLocation != null) {
                        txtProfileLocation.setText("📍 " + searchText);
                    }
                    
                    Toast.makeText(this, "Location changed to " + searchText, Toast.LENGTH_SHORT).show();
                    showView(viewProducts);
                } else {
                    Toast.makeText(this, "Please enter a location", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    
    private List<Location> createPopularLocations() {
        List<Location> locations = new ArrayList<>();
        locations.add(new Location("Kigali, Rwanda", "5 km"));
        locations.add(new Location("Gisenyi, Rwanda", "125 km"));
        locations.add(new Location("Butare, Rwanda", "135 km"));
        locations.add(new Location("Ruhengeri, Rwanda", "95 km"));
        locations.add(new Location("Gitarama, Rwanda", "55 km"));
        locations.add(new Location("Kibungo, Rwanda", "75 km"));
        locations.add(new Location("Cyangugu, Rwanda", "225 km"));
        locations.add(new Location("Kibuye, Rwanda", "125 km"));
        locations.add(new Location("Byumba, Rwanda", "65 km"));
        locations.add(new Location("Rwamagana, Rwanda", "50 km"));
        return locations;
    }

    private void openCameraOrGallery() {
        // Show dialog to choose between camera and gallery
        String[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo");
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                // Take Photo - Open camera
                openCamera();
            } else if (which == 1) {
                // Choose from Gallery
                openGallery();
            }
            // Cancel does nothing
        });
        builder.show();
    }
    
    private void openCamera() {
        // Check camera permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) 
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, 
                new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            return;
        }
        
        // Open camera
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            try {
                cameraLauncher.launch(cameraIntent);
            } catch (Exception e) {
                Toast.makeText(this, "Failed to open camera: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Camera not available on this device", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void openGallery() {
        // Check storage permission for older Android versions (API 32 and below)
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) 
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, 
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
                return;
            }
        }
        // For Android 13+ (API 33+), READ_MEDIA_IMAGES permission is handled automatically by the system
        
        // Open gallery using ACTION_PICK
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        try {
            galleryLauncher.launch(galleryIntent);
        } catch (Exception e) {
            Toast.makeText(this, "Failed to open gallery: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Camera permission is required to take photos", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(this, "Storage permission is required to select photos", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Handle search query
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filter products as user types
                productAdapter.filter(newText);
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
