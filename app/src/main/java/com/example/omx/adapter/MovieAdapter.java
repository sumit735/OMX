package com.example.omx.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Movie;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.omx.R;
import com.example.omx.SinglePartActivity;
import com.example.omx.model.GridItem;
import com.example.omx.model.RecentwatchItem;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {

    private ArrayList<RecentwatchItem> moviesList = new ArrayList<RecentwatchItem>();
    Context context;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView movieImageView;
        public RelativeLayout listViewLayout;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.movieTitle);
            movieImageView = (ImageView) view.findViewById(R.id.movieImageView);
            listViewLayout = (RelativeLayout) view.findViewById(R.id.listViewLayout);
        }
    }


    public MovieAdapter(ArrayList<RecentwatchItem> moviesList, Context context) {
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
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final RecentwatchItem movie = moviesList.get(position);
        holder.title.setText(movie.getTitle());
        Glide.with(context)
                .load(movie.getImageId())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.movieImageView);

        holder.listViewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Intent intent = new Intent(context, SinglePartActivity.class);
                intent.putExtra("MovieTitle",movie.getTitle());
                intent.putExtra("MovieImage",movie.getImageId());
                intent.putExtra("MovieDetails",movie.getShort_desc());
                intent.putExtra("MovieUrl",movie.getVideoUrl());
                intent.putExtra("MovieBanner",movie.getBannerImage());
                intent.putExtra("MovieGenre",movie.getMovieGenre());
                intent.putExtra("MovieDuration",movie.getMovieDuration());
                intent.putExtra("MovieID",movie.getId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}