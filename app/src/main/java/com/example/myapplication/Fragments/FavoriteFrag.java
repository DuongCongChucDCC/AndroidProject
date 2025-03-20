package com.example.myapplication.Fragments;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;


import com.example.myapplication.Adapters.MovieListAdapter;

import com.example.myapplication.Domains.Movies;

import com.example.myapplication.databinding.FragmentFavoriteBinding;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class FavoriteFrag extends Fragment {
    FragmentFavoriteBinding binding;
    private FirebaseDatabase database;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        database = FirebaseDatabase.getInstance("https://testproject-81cb6-default-rtdb.asia-southeast1.firebasedatabase.app/");
        Window w = getActivity().getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        FirebaseApp.initializeApp(getContext());
        DatabaseReference myRef = database.getReference("Favorites");
        ArrayList<Movies> items = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    items.clear();
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        items.add(issue.getValue(Movies.class));
                    }
                    if (!items.isEmpty()) {
                        // 2 cột, dọc (vertical)
                        binding.recyclerFavorite.setLayoutManager(new GridLayoutManager(getContext(), 2));
                        binding.recyclerFavorite.setAdapter(new MovieListAdapter(items));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}