package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.instagram.databinding.ActivityFeedBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

// Class where we will see all the posts. Is our Instagram Feed
public class FeedActivity extends AppCompatActivity {

    private String TAG = "FeedActivity";

    List<Post> posts;
    PostsAdapter adapter;
    RecyclerView rvPosts;
    private SwipeRefreshLayout swipeContainer;
    FloatingActionButton btnCompose;  // Temporal

    // Initializes the Feed Activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Binding
        ActivityFeedBinding binding = ActivityFeedBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Initialize List and Adapter
        posts = new ArrayList<>();
        adapter = new PostsAdapter(this, posts);

        // Associates values to components in activity_feed.xml
        rvPosts = binding.rvPosts;
        swipeContainer = binding.swipeContainer;
        btnCompose = binding.btnCompose;

        // Recycler View setup
        rvPosts.setLayoutManager(new LinearLayoutManager(this));
        rvPosts.setAdapter(adapter);

        // OnClickListener to compose a new post
        btnCompose.setOnClickListener(new View.OnClickListener() {
            // Goes to compose activity to make a new post
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FeedActivity.this, ComposeActivity.class);
                startActivity(i);
            }
        });

        // Sets a Refresh Listener to the Swipe Container
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshFeed();
                swipeContainer.setRefreshing(false);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // Get the posts to populate the feed
        populateFeed();
    }

    // Initial population that will save last 20 posts in the Database to the list and the recycler view
    private void populateFeed() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.addDescendingOrder("createdAt");
        query.setLimit(20);
        query.findInBackground(new FindCallback<Post>() {
            // Result of the call to Parse
            @Override
            public void done(List<Post> queryPosts, ParseException e) {
                // There is an error
                if(e != null) {
                    Log.e(TAG, "Error in getting posts");
                    return;
                }
                for(Post post : queryPosts) {
                    // post.getUser() gets the Id... post.getUser().getUsername() gets the Username associated to the id of getUser in User's table
                    Log.i(TAG, "Post " + post.getDescription() + " username: " + post.getUser().getUsername());
                }
                posts.addAll(queryPosts);
                adapter.notifyDataSetChanged();
            }
        });
    }

    // Refresh 20 last posts
    private void refreshFeed() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.addDescendingOrder("createdAt");
        query.setLimit(20);
        query.findInBackground(new FindCallback<Post>() {
            // Result of the call to Parse
            @Override
            public void done(List<Post> queryPosts, ParseException e) {
                // There is an error
                if(e != null) {
                    Log.e(TAG, "Error in getting posts");
                    return;
                }
                for(Post post : queryPosts) {
                    // post.getUser() gets the Id... post.getUser().getUsername() gets the Username associated to the id of getUser in User's table
                    Log.i(TAG, "Post " + post.getDescription() + " username: " + post.getUser().getUsername());
                }

                adapter.clear();
                adapter.addAll(queryPosts);
            }
        });
    }
}