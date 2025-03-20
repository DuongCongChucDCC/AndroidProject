package com.example.myapplication.Activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.Window;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.Adapters.CastAdapter;
import com.example.myapplication.Adapters.GenreAdapter;
import com.example.myapplication.Domains.Movies;
import com.example.myapplication.Domains.SliderItems;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityDetailBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import eightbitlab.com.blurview.RenderScriptBlur;

public class DetailActivity extends AppCompatActivity {
    private ActivityDetailBinding binding;
    private FirebaseDatabase database;
    boolean isFavorited = false;
    String matchedId = null;
    ArrayList<Movies> labelList = new ArrayList<>();
    String TAG = "zzzzzzzzzzzzzzz";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setVariable();
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    private void setVariable() {
        database = FirebaseDatabase.getInstance("https://testproject-81cb6-default-rtdb.asia-southeast1.firebasedatabase.app/");
        Movies movie = (Movies) getIntent().getSerializableExtra("object");
        checkList(movie);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transform(new CenterCrop(), new GranularRoundedCorners(0, 0, 50, 50));
        Glide.with(this).load(movie.getPoster()).apply(requestOptions).into(binding.filmImg);
        binding.titleTxt.setText(movie.getTitle());
        binding.imdbTxt.setText("IMDB: " + movie.getImdb());
        binding.movieTimes.setText(movie.getYear() + " - " + movie.getTime());
        binding.movieSummery.setText(movie.getDescription());
        binding.CastView.setLayoutManager(new LinearLayoutManager(DetailActivity.this, LinearLayoutManager.VERTICAL, false));
        binding.CastView.setAdapter(new CastAdapter(movie.getCasts()));
        binding.genreView.setLayoutManager(new LinearLayoutManager(DetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
        binding.genreView.setAdapter(new GenreAdapter(movie.getGenre()));
        binding.watchTrailerBtn.setOnClickListener(v -> {
            String id = movie.getTrailer().replace("http://www.youtube.com/watch?v=", "");
            Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(movie.getTrailer()));
            try {
                startActivity(appIntent);
            } catch (ActivityNotFoundException ex) {
                startActivity(webIntent);
            }
        });
        binding.backImg.setOnClickListener(v -> {
            finish();
        });
        binding.bookmark.setOnClickListener(v -> {
            if (isFavorited) {
                removeFavorite(database, movie);
                ImageViewCompat.setImageTintList(binding.bookmark, ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
                isFavorited = false;
            } else {
                addFavorite(database, movie);
                ImageViewCompat.setImageTintList(binding.bookmark, ColorStateList.valueOf(Color.parseColor("#FFD700")));
                isFavorited = true;
            }
        });

        float radius = 10f;
        View decorView = getWindow().getDecorView();
        ViewGroup rootView = (ViewGroup) decorView.findViewById(android.R.id.content);
        Drawable windowBackground = decorView.getBackground();

        binding.blurView.setupWith(rootView, new RenderScriptBlur(this)).setFrameClearDrawable(windowBackground).setBlurRadius(radius);
        binding.blurView.setOutlineProvider(ViewOutlineProvider.BACKGROUND);
        binding.blurView.setClipToOutline(true);
    }

    public void addFavorite(FirebaseDatabase database, Movies movie) {
        DatabaseReference myRef = database.getReference("Favorites");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long maxId = -1;
                for (DataSnapshot child : snapshot.getChildren()) {
                    try {
                        long id = Long.parseLong(child.getKey());
                        if (id > maxId) maxId = id;
                    } catch (NumberFormatException e) {
                        // Skip non-numeric keys
                    }
                }
                myRef.child(String.valueOf(maxId + 1)).setValue(movie);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("AddFavorite", "Failed: " + error.getMessage());
            }
        });

    }


    public void removeFavorite(FirebaseDatabase database, Movies movie) {
        DatabaseReference myRef = database.getReference("Favorites");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String targetId = null;
                for (DataSnapshot child : snapshot.getChildren()) {
                    Movies m = child.getValue(Movies.class);
                    if (m != null && m.getTitle().equals(movie.getTitle())) {
                        targetId = child.getKey();
                        break;
                    }
                }

                if (targetId != null) {
                    myRef.child(targetId).removeValue().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Removed successfully");
                        } else {
                            Log.d(TAG, "Failed to remove");
                        }
                    });
                } else {
                    Log.d(TAG, "Movie not found in Favorites");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "Failed to check favorites: " + error.getMessage());
            }
        });
    }


    public void checkList(Movies movie) {
        DatabaseReference myRef = database.getReference("Favorites");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        Movies m = issue.getValue(Movies.class);
                        if (m != null) {
                            labelList.add(m);
                            if (m.getTitle().equals(movie.getTitle())) {
                                matchedId = issue.getKey();
                                Log.d(TAG, "onDataChange: " + m.getTitle() + " - " + movie.getTitle());
                                Log.d(TAG, "match: " + matchedId);
                                if (matchedId != null) {
                                    ImageViewCompat.setImageTintList(binding.bookmark, ColorStateList.valueOf(Color.parseColor("#FFD700")));
                                    isFavorited = true;
                                } else {
                                    ImageViewCompat.setImageTintList(binding.bookmark, ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
                                    isFavorited = false;
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled: " + error.getMessage());
            }
        });
    }
}