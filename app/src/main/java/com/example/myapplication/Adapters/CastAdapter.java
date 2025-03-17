package com.example.myapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.Domains.Casts;
import com.example.myapplication.R;

import java.util.ArrayList;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.ViewHolder> {
    ArrayList<Casts> casts;
    Context context;

    public CastAdapter(ArrayList<Casts> casts) {
        this.casts = casts;
    }

    @NonNull
    @Override
    public CastAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(context).inflate(R.layout.actor_viewholder, parent, false);
        return new CastAdapter.ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull CastAdapter.ViewHolder holder, int position) {
        Casts cast = casts.get(position);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transform(new CenterCrop(), new RoundedCorners(30));
        Glide.with(context).load(cast.getPicUrl()).apply(requestOptions).into(holder.castPic);
        holder.castName.setText(cast.getActor());
    }

    @Override
    public int getItemCount() {
        return casts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView castPic;
        TextView castName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            castPic = itemView.findViewById(R.id.castPic);
            castName = itemView.findViewById(R.id.castName);
        }
    }
}
