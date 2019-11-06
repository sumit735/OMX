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
    ArrayList<SingleItemModel> singleSectionItems;
    private ArrayList<SectionDataModel> dataList;
    private Context mContext;
    private ArrayList<String> bannerUrls = new ArrayList<String>();
    String pemalink;
    // String image;
    boolean loaded = false;
    //int counter=0;



    public RecyclerViewDataAdapter(Context context, ArrayList<SectionDataModel> dataList) {
        this.dataList = dataList;
        this.mContext = context;
        this.bannerUrls = bannerUrls;


    }

    /* public void swapItems(){
         loaded = true;
     }*/
    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_listitem, null);
            ItemRowHolder mh = new ItemRowHolder(v);
            return mh;

    }

    @Override
    public void onBindViewHolder(final ItemRowHolder itemRowHolder, final int i) {

        try {

            if (dataList.size() < 1) {
                itemRowHolder.section_title_layout.setVisibility(View.GONE);
                itemRowHolder.recycler_view_list.setVisibility(View.GONE);
                return;
            } else{
                itemRowHolder.section_title_layout.setVisibility(View.VISIBLE);
                itemRowHolder.recycler_view_list.setVisibility(View.VISIBLE);
            }


            final String sectionName = dataList.get(i).getHeaderTitle().trim();
            final String sectionId = dataList.get(i).getHeaderPermalink();

            singleSectionItems = dataList.get(i).getAllItemsInSection();
            pemalink = dataList.get(i).getHeaderPermalink();

            itemRowHolder.itemTitle.setText(sectionName.trim());
            SectionListDataAdapter itemListDataAdapter = null;




                itemListDataAdapter = new SectionListDataAdapter(mContext, singleSectionItems, R.layout.list_item);



            itemRowHolder.recycler_view_list.setHasFixedSize(true);
            itemRowHolder.recycler_view_list.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
            itemRowHolder.recycler_view_list.setAdapter(itemListDataAdapter);




        } catch (Exception e) {
            e.printStackTrace();


        }


    }

    @Override
//    public int getItemCount() {
//        return dataList.size();
//    }
    public int getItemCount() {
        return 0;

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    public class ItemRowHolder extends RecyclerView.ViewHolder  {

        protected TextView itemTitle;
        protected RecyclerView recycler_view_list;
        private RelativeLayout  section_title_layout;

        public ItemRowHolder(View view) {
            super(view);

            this.itemTitle = (TextView) view.findViewById(R.id.sectiontitle);
            this.section_title_layout = (RelativeLayout) view.findViewById(R.id.section_title_layout);
            this.recycler_view_list = (RecyclerView) view.findViewById(R.id.featureContent);





        }


    }


}