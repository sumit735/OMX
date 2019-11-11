package com.example.omx.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.example.omx.R;
import com.example.omx.Util;
import com.example.omx.model.SectionDataModel;
import com.example.omx.model.SingleItemModel;


import java.util.ArrayList;


public class RecyclerViewDataAdapter extends RecyclerView.Adapter<RecyclerViewDataAdapter.ItemRowHolder> {
    private ArrayList<SingleItemModel> moviesList = new ArrayList<SingleItemModel>();
    private ArrayList<SectionDataModel> dataList;
    private Context mContext;
    String pemalink;
    boolean loaded = false;


    public RecyclerViewDataAdapter(Context context, ArrayList<SectionDataModel> dataList) {
        this.dataList = dataList;
        this.mContext = context;
        //notifyDataSetChanged();

    }


    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_listitem, parent, false);

        return new ItemRowHolder(itemView);


    }

    @Override
    public void onBindViewHolder(final ItemRowHolder itemRowHolder, final int i) {

       // try {



            final String sectionName =  dataList.get(i).getHeaderPermalink().trim();
            final String sectionId =  dataList.get(i).getHeaderTitle();

            moviesList = dataList.get(i).getAllItemsInSection();
            pemalink = dataList.get(i).getHeaderPermalink();

            itemRowHolder.itemTitle.setText(sectionName.trim());
            SectionListDataAdapter itemListDataAdapter = null;


            itemListDataAdapter = new SectionListDataAdapter(mContext, moviesList, R.layout.list_item);


            itemRowHolder.recycler_view_list.setHasFixedSize(true);
            itemRowHolder.recycler_view_list.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
            itemRowHolder.recycler_view_list.setAdapter(itemListDataAdapter);




    }

    public int getItemCount() {
        return dataList.size();

    }

   /* @Override
    public int getItemViewType(int position) {
        return position;
    }*/


    public class ItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView itemTitle;
        protected RecyclerView recycler_view_list;
        private RelativeLayout section_title_layout;

        public ItemRowHolder(View view) {
            super(view);

            this.itemTitle = (TextView) view.findViewById(R.id.sectiontitle);
            this.recycler_view_list = (RecyclerView) view.findViewById(R.id.featureContent);


        }


    }


}