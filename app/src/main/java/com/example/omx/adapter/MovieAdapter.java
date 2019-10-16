package com.example.omx.adapter;

import android.content.Context;
import android.graphics.Movie;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.omx.R;
import com.example.omx.model.GridItem;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {

    private ArrayList<GridItem> moviesList = new ArrayList<GridItem>();
    Context context;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView movieImageView;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.movieTitle);
            movieImageView = (ImageView) view.findViewById(R.id.movieImageView);
        }
    }


    public MovieAdapter( ArrayList<GridItem> moviesList,Context context) {
        this.moviesList = moviesList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        GridItem movie = moviesList.get(position);
        holder.title.setText(movie.getTitle());
        Glide.with(context)
                .load(movie.getImageId())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.movieImageView);

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}