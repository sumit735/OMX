package com.example.omx.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.omx.GenreListActivity;
import com.example.omx.R;
import com.example.omx.model.GenreItem;
import com.example.omx.model.GridItem;

import java.util.ArrayList;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.MyViewHolder> {

    private ArrayList<GenreItem> moviesList = new ArrayList<GenreItem>();
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView movieImageView;
        public RelativeLayout genreViewLayout;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.movieTitle);
            movieImageView = (ImageView) view.findViewById(R.id.movieImageView);
            genreViewLayout = (RelativeLayout) view.findViewById(R.id.genreViewLayout);
        }
    }


    public GenreAdapter(ArrayList<GenreItem> moviesList, Context context) {
        this.moviesList = moviesList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.genre_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final GenreItem movie = moviesList.get(position);
        holder.title.setText(movie.getTitle());

        Glide.with(context)
                .load(movie.getImage())
                .thumbnail(0.5f)
                .into(holder.movieImageView);

        holder.genreViewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, GenreListActivity.class);
                intent.putExtra("genreName",movie.getTitle());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}