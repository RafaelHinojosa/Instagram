package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.instagram.databinding.ActivityDetailsBinding;
import com.parse.ParseFile;

import org.parceler.Parcels;

// Class to receive a post and see its details in the layout
public class DetailsActivity extends AppCompatActivity {

    public static final String TAG = "DetailsActivity";

    Post post;

    // Post's attributes
    ImageView ivProfilePicture;
    TextView tvUsername;
    ImageView ivThreeDots;
    ImageView ivPostImage;
    ImageView ivLike;
    ImageView ivComment;
    ImageView ivShare;
    ImageView ivSave;
    TextView tvLikeCounter;
    TextView tvDescription;
    ImageView ivProfilePictureComment;
    TextView tvComment;
    TextView tvTimeAgo;

    // Creating the Details Activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Binding
        // Put name as in layout file
        ActivityDetailsBinding binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Associate (find) attributes in the layout file (activity_details.xml)
        ivProfilePicture = binding.ivProfilePicture;
        tvUsername = binding.tvUsername;
        ivThreeDots = binding.ivThreeDots;
        ivPostImage = binding.ivPostImage;
        ivLike = binding.ivLike;
        ivComment = binding.ivComment;
        ivShare = binding.ivShare;
        ivSave = binding.ivSave;
        tvLikeCounter = binding.tvLikeCounter;
        tvDescription = binding.tvDescription;
        ivProfilePictureComment = binding.ivProfilePictureComment;
        tvComment = binding.tvComment;
        tvTimeAgo = binding.tvTimeAgo;

        post = Parcels.unwrap(getIntent().getParcelableExtra("postDetails"));
        bind(post);
    }

    // Set post's values to the layout components
    protected void bind(Post post) {
        // Profile Picture
        // Username
        tvUsername.setText(post.getUser().getUsername());
        // Post Image
        ParseFile postImage = post.getImage();
        if(postImage != null) {
            Glide.with(DetailsActivity.this).load(postImage.getUrl()).fitCenter().into(ivPostImage);
        }
        // Description
        tvDescription.setText(post.getDescription());
        // Profile Picture Comment
        // Time Ago
    }
}