package com.example.omx.adapter;

import android.content.Context;
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
import com.example.omx.model.GenreItem;
import com.example.omx.model.GridItem;

import java.util.ArrayList;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.MyViewHolder> {

    private ArrayList<GenreItem> moviesList = new ArrayList<GenreItem>();
    Context context;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public RelativeLayout genreViewLayout;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.movieTitle);
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
    public void onBindViewHolder(MyViewHolder holder, int position) {
        GenreItem movie = moviesList.get(position);
        holder.title.setText(movie.getTitle());

        holder.genreViewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });


    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}