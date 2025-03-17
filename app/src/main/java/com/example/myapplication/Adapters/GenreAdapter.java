package com.example.myapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.ViewHolder> {
    ArrayList<String> genres;
    Context context;

    public GenreAdapter(ArrayList<String> genres) {
        this.genres = genres;
    }

    @NonNull
    @Override
    public GenreAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(context).inflate(R.layout.genre_viewholder, parent, false);
        return new GenreAdapter.ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreAdapter.ViewHolder holder, int position) {
        String genre = genres.get(position);
        holder.genreTxt.setText(genre);
    }

    @Override
    public int getItemCount() {
        return genres.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView genreTxt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            genreTxt = itemView.findViewById(R.id.genreTxt);
        }
    }
}
