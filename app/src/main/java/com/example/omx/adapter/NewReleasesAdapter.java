package com.example.omx.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.omx.R;
import com.example.omx.model.BannerItem;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

public class NewReleasesAdapter extends RecyclerView.Adapter<NewReleasesAdapter.MyViewHolder> {
    Context mContext;
    private ArrayList<BannerItem> moviesList = new ArrayList<BannerItem>();
    Context context;
    CircularImageView circularImageView;
    public class MyViewHolder extends RecyclerView.ViewHolder {


        public CircularImageView circularImageView;

        public MyViewHolder(View view) {
            super(view);
            circularImageView = view.findViewById(R.id.circularImageView);

        }
    }


    public NewReleasesAdapter(ArrayList<BannerItem> moviesList, Context context) {
        this.moviesList = moviesList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.circularimglist_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final BannerItem movie = moviesList.get(position);

        Glide.with(context)
                .load(movie.getImage())
                .thumbnail(0.5f)

                .into(holder.circularImageView);
// Set Circle color for transparent image

    /*    circularImageView.setCircleColor(Color.WHITE);
        circularImageView.setBorderColor(Color.TRANSPARENT);
        circularImageView.setBorderWidth(10);
        circularImageView.setShadowEnable(true);
        circularImageView.setShadowRadius(15);
        circularImageView.setShadowColor(Color.RED);
        circularImageView.setBackgroundColor(Color.RED);
        circularImageView.setShadowGravity(CircularImageView.ShadowGravity.CENTER);*/


    }



    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}