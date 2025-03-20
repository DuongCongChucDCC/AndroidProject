package com.example.myapplication.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Fragments.ExplorerFrag;
import com.example.myapplication.Fragments.FavoriteFrag;
import com.example.myapplication.Fragments.ProfileFrag;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

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
    }
}