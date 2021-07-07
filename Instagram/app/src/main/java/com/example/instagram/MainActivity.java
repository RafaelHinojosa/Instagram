package com.example.instagram;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.instagram.databinding.ActivityLoginBinding;
import com.example.instagram.databinding.ActivityMainBinding;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    // Arbitrary value for the code to capture an image with the camera
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;

    private EditText etDescription;
    private Button btnCaptureImage;
    private ImageView ivPostImage;
    private Button btnSubmit;
    Button btnLogout;
    private File photoFile;
    private String photoFileName = "photo.jpg";

    // Starts the Main Activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Binding
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Associate variables with components in activity_main.xml
        etDescription = binding.etDescription;
        btnCaptureImage = binding.btnCaptureImage;
        ivPostImage = binding.ivPostImage;
        btnSubmit = binding.btnSubmit;
        btnLogout = binding.btnLogout;

        // On Click Listener to Log Out
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If user clicks, Logs Out and goes back (through an intent) to LoginActivity
                ParseUser.logOut();
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        //
        btnCaptureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
            }
        });

        // Query ALL Posts from DataBase
        // queryPosts();

        // btnSubmit on click listener to submit a new Post
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = etDescription.getText().toString();
                if(description.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Description can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(photoFile == null || ivPostImage.getDrawable() == null) {
                    Toast.makeText(MainActivity.this, "There is no image!", Toast.LENGTH_SHORT).show();
                    return;
                }
                ParseUser currentUser = ParseUser.getCurrentUser();
                savePost(description, currentUser, photoFile);

            }
        });
    }

    //
    private void savePost(String description, ParseUser currentUser, File photoFile) {
        Post post = new Post();
        post.setDescription(description);
        post.setImage(new ParseFile(photoFile));
        post.setUser(currentUser);
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null) {
                    Log.e(TAG, "Error while saving " + e);
                    return;
                }
                Log.i(TAG, "Post save was successful");
                // When submit, the post's description is restarted so the user is invited to write a new post
                etDescription.setText("");
                ivPostImage.setImageResource(0);    // Empty resource id
            }
        });
    }

    // Gets all posts uploaded  by users
    private void queryPosts() {
        // Make a query of Posts to get them from the DataBase
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        // The query will also include the user key
        query.include(Post.KEY_USER);
        query.findInBackground(new FindCallback<Post>() {
            // If done, a list with ALL the posts in the DB is returned
            @Override
            public void done(List<Post> posts, ParseException e) {
                if(e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                }
                // Log all obtained posts with their user
                for(Post post : posts) {
                    Log.i(TAG, "User " + post.getUser().getUsername() + " says: " + post.getDescription());
                }
            }
        });
    }

    // Launches Camera to take a picture
    public void launchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        // Uri = unified resource identifier. The file is the resource representing the captured image
        // Authority matches with the one in AndroidManifest.xml
        // Files created/modified to access to external storage directories (where photos are):
            // AndroidManifest.xml, fileprovider.xml
        Uri fileProvider = FileProvider.getUriForFile(MainActivity.this, "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    // Need to get a Result from "startActivityForResult"
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // If user took a photo
        if(requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                // Load the image located at photoUri into selectedImage
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP
                // Load the selected image into a preview
                ivPostImage.setImageBitmap(takenImage);
            }
            else {
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }
}