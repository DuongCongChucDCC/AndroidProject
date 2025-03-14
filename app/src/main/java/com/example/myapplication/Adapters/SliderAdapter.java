package com.example.myapplication.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.Domains.SliderItems;
import com.example.myapplication.R;

import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {
    private List<SliderItems> sliderItems;
    private ViewPager2 viewPager2;

    public SliderAdapter(List<SliderItems> sliderItems, ViewPager2 viewPager2) {
        this.sliderItems = sliderItems;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public SliderAdapter.SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_viewholder, parent, false);
        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderAdapter.SliderViewHolder holder, int position) {
        holder.setInfo(sliderItems.get(position));
        Log.d("SliderAdapter", "onBindViewHolder: " + sliderItems.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return sliderItems.size();
    }

    public static class SliderViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView nameTxt, genreTxt, ageTxt, yearTxt, timeTxt;

        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageSlider);
            nameTxt = itemView.findViewById(R.id.nameTxt);
            genreTxt = itemView.findViewById(R.id.genreTxt);
            ageTxt = itemView.findViewById(R.id.ageTxt);
            yearTxt = itemView.findViewById(R.id.yearTxt);
            timeTxt = itemView.findViewById(R.id.timeTxt);
        }

        void setInfo(SliderItems sliderItems) {
            RequestOptions requestOptions = new RequestOptions().transform(new CenterCrop(), new RoundedCorners(60));

            Glide.with(itemView.getContext()).load(sliderItems.getImage()).apply(requestOptions).into(imageView);

            nameTxt.setText(sliderItems.getName() != null ? sliderItems.getName() : "N/A");
            genreTxt.setText(sliderItems.getGenre() != null ? sliderItems.getGenre() : "N/A");
            ageTxt.setText(sliderItems.getAge() != null ? sliderItems.getAge() : "N/A");
            yearTxt.setText(sliderItems.getYear() != null ? String.valueOf(sliderItems.getYear()) : "N/A");
            timeTxt.setText(sliderItems.getTime() != null ? sliderItems.getTime() : "N/A");
        }
    }
}
