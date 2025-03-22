package com.example.myapplication.Activities;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.myapplication.Adapters.MovieListAdapter;
import com.example.myapplication.Domains.Movies;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivitySearchBinding;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    ActivitySearchBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ArrayList<Movies> items = (ArrayList<Movies>) getIntent().getSerializableExtra("searchs");
        binding.recyclerFavorite.setLayoutManager(new GridLayoutManager(this, 2));
        binding.recyclerFavorite.setAdapter(new MovieListAdapter(items));
    }
}