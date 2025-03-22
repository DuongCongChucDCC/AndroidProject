package com.example.myapplication.Activities;

import android.app.ComponentCaller;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.myapplication.Domains.Movies;
import com.example.myapplication.Fragments.ExplorerFrag;
import com.example.myapplication.Fragments.FavoriteFrag;
import com.example.myapplication.Fragments.ProfileFrag;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.navigation.setItemSelected(R.id.explorer, true);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new ExplorerFrag()).commit();
        binding.navigation.setOnItemSelectedListener(i -> {
            if (i == R.id.explorer) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new ExplorerFrag()).commit();
            } else if (i == R.id.favorites) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new FavoriteFrag()).commit();
            } else if (i == R.id.profile) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new ProfileFrag()).commit();
            }
        });
        database = FirebaseDatabase.getInstance("https://testproject-81cb6-default-rtdb.asia-southeast1.firebasedatabase.app/");

        binding.searchBar.setOnEditorActionListener((v, actionId, event) -> {
            String text = binding.searchBar.getText().toString();
            if (text.length() < 3) {
                Toast.makeText(MainActivity.this, "Vui lòng nhập ít nhất 3 ký tự", Toast.LENGTH_SHORT).show();
                return false;
            }
            search(text);
            return false;
        });

        binding.micSearch.setOnClickListener(v -> {
            Intent iMic = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            iMic.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            iMic.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "vi-VN");
            try {
                startActivityForResult(iMic, 100);
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void search(String text) {
        ArrayList<Movies> items = new ArrayList<>();
        DatabaseReference myRef = database.getReference("Items");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        Movies movies = issue.getValue(Movies.class);
                        if (movies != null && movies.getTitle().toLowerCase().contains(text.toLowerCase())) {
                            items.add(movies);
                        }
                    }
                    if (!items.isEmpty()) {
                        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                        intent.putExtra("searchs", items);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "No movies found!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            Log.d("ZZZZZZZZZZZZZZ", "onActivityResult: " + data);
            ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            Log.d("ZZZZZZZZZZZZZZ", "onActivityResult: " + text);
            binding.searchBar.setText(text.get(0));
        }
    }
}