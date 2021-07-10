package com.example.instagram.fragments;

import android.util.Log;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.instagram.Post;
import com.example.instagram.PostsAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

// Made this class as a child of FeedFragment to avoid copying a lot of code into a new fragment
public class ProfileFragment extends FeedFragment {

    public static final String TAG = "ProfileFragment";

    // Gets the posts of the actual user
    @Override
    protected void populateFeed() {
        rvPosts.setLayoutManager(new GridLayoutManager(getContext(), 3));
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
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
}
