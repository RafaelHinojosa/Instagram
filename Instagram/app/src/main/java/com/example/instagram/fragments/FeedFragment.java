package com.example.instagram.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.instagram.Post;
import com.example.instagram.PostsAdapter;
import com.example.instagram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class FeedFragment extends Fragment {

    private String TAG = "FeedFragment";

    protected List<Post> posts;
    protected PostsAdapter adapter;
    protected RecyclerView rvPosts;
    protected SwipeRefreshLayout swipeContainer;

    public FeedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvPosts = view.findViewById(R.id.rvPosts);
        swipeContainer = view.findViewById(R.id.swipeContainer);

        posts = new ArrayList<>();
        adapter = new PostsAdapter(getContext(), posts);

        // Recycler View setup
        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPosts.setAdapter(adapter);

        // Sets a Refresh Listener to the Swipe Container
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshFeed();
                swipeContainer.setRefreshing(false);
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        populateFeed();
    }

    // Initial population that will save last 20 posts in the Database to the list and the recycler view
    protected void populateFeed() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.addDescendingOrder(Post.CREATED_AT);
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