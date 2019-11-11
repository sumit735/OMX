package com.example.omx.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.omx.R;
import com.example.omx.SinglePartActivity;
import com.example.omx.model.SingleItemModel;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class SectionListDataAdapter extends RecyclerView.Adapter<SectionListDataAdapter.SingleItemRowHolder> {





    int layoutname = 0;



    int corePoolSize = 60;
    int maximumPoolSize = 80;
    int keepAliveTime = 10;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
    ProgressDialog videoPDialog;
    String videoUrlStr;


    private TextView validationIndicatorTextView;



    private ArrayList<SingleItemModel> itemsList;
    private Context mContext;
    public static int sampleSize=100;

    public SectionListDataAdapter(Context context, ArrayList<SingleItemModel> itemsList, int layoutname) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.layoutname = layoutname;

    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

  View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item, viewGroup, false);

        return new SingleItemRowHolder(itemView);



    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, int i) {

        SingleItemModel singleItem = itemsList.get(i);

        holder.itemTitle.setText(singleItem.getTitle());
        holder.position = i;

        try{
            if (singleItem.getImagedata()!=null) {

                Picasso.with(mContext)
                        .load(singleItem.getImagedata())
                        .placeholder(R.drawable.logo)   // optional
                        .error(R.drawable.logo)      // optional
                        .into(holder.itemImage);

            }else{

                 Glide.with(mContext)
                        .load(singleItem.getImagedata())
                        .into(holder.itemImage);


            }
        }catch (Exception e){
        }



    }

    @Override
    public int getItemCount() {


            return itemsList.size();
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView itemTitle;

        protected ImageView itemImage;
        protected View temPV;
        protected  int position;

        public SingleItemRowHolder(View view) {
            super(view);

          this.itemTitle = (TextView) view.findViewById(R.id.movieTitle);
            this.itemImage = (ImageView) view.findViewById(R.id.movieImageView );





            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String movieTypeId = itemsList.get(position).getVideoTypeId();

                    Intent intent = new Intent(mContext, SinglePartActivity.class);
                    intent.putExtra("MovieTitle",itemsList.get(position).getTitle());
                    intent.putExtra("MovieImage",itemsList.get(position).getImagedata());
                    intent.putExtra("MovieDetails",itemsList.get(position).getDetails());
                    intent.putExtra("MovieUrl",itemsList.get(position).getVideoUrl());
                    intent.putExtra("MovieBanner",itemsList.get(position).getBanner());
                    intent.putExtra("MovieGenre",itemsList.get(position).getMovieGenre());
                    intent.putExtra("MovieDuration",itemsList.get(position).getTotal_time());
                    intent.putExtra("MovieID",itemsList.get(position).getImageId());
                    mContext.startActivity(intent);


                }
            });


        }

    }






            }