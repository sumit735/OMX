package com.example.omx.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.omx.R;
import com.example.omx.model.AdsItem;
import com.example.omx.model.BannerItem;

import java.util.ArrayList;

public class AdsAdapter extends RecyclerView.Adapter<AdsAdapter.MyViewHolder> {
    Context mContext;
    private ArrayList<AdsItem> moviesList = new ArrayList<AdsItem>();
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView movieImageView;

        public MyViewHolder(View view) {
            super(view);
            movieImageView = (ImageView) view.findViewById(R.id.movieImageView);
        }
    }


    public AdsAdapter(ArrayList<AdsItem> moviesList, Context context) {
        this.moviesList = moviesList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bannerlist_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final AdsItem movie = moviesList.get(position);

        Glide.with(context)
                .load(movie.getImage())
                .thumbnail(0.5f)
                .into(holder.movieImageView);
    }



    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}