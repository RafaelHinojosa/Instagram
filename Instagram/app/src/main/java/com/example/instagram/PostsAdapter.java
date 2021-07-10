package com.example.instagram;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instagram.databinding.ItemPostBinding;
import com.parse.ParseFile;

import org.parceler.Parcels;
import org.w3c.dom.Text;

import java.util.Date;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder>{

    private String TAG = "PostsAdapter";
    ItemPostBinding binding;
    Context context;
    List<Post> posts;

    // Initialize the context and list of tweets
    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    // For each element, inflate the layout for a tweet
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemPostBinding.inflate(LayoutInflater.from(context), parent, false);
        return new PostsAdapter.ViewHolder(binding.getRoot());
    }

    // Bind values based on the position of the element
    @Override
    public void onBindViewHolder(@NonNull PostsAdapter.ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    // Gets number of posts in Posts list
    @Override
    public int getItemCount() {
        return posts.size();
    }

    // Clears the adapter and the list
    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    // Adds a List to the posts List and adapter
    public void addAll(List<Post> newList) {
        posts.addAll(newList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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

        // Initializes the VH by associating variables with components in item_post.xml
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
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
            itemView.setOnClickListener(this);
        }

        // Binds (Sets) data into the components
        public void bind(Post post) {
            // Profile Picture
            // Username
            tvUsername.setText(post.getUser().getUsername());
            // Post Image
            ParseFile postImage = post.getImage();
            if (postImage != null) {
                Glide.with(context).load(postImage.getUrl()).fitCenter().into(ivPostImage);
            }
            // Description
            tvDescription.setText(post.getDescription());
            // Profile Picture Comment
            // Time Ago
            Date createdAt = post.getCreatedAt();
            String timeAgo = Post.calculateTimeAgo(createdAt);
            tvTimeAgo.setText(timeAgo);
        }

        // SEE POST DETAILS
        // 1. Implement View.OnClickListener on the ViewHolder Class
        // 2. Dependencies: Parcel in Gradle.app
        // 3. Make the Post Model Parcelable
        // 4. Create intent with the post
        // 5. Send the intent to DetailsActivity.class
        // 6. Receive it there and assign the post properties to the layout (activity_details.xml) components
        @Override
        public void onClick(View view) {
            // Get the clicked post
            int position = getAdapterPosition();
            // Valid Position
            if (position != RecyclerView.NO_POSITION) {
                Post post = posts.get(position);
                Intent intent = new Intent(context, DetailsActivity.class);
                // Doing the post parcelable means converting its attributes to a String
                intent.putExtra("postDetails", Parcels.wrap(post));
                context.startActivity(intent);
            }
        }
    }
}
