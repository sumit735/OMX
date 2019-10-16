package com.example.omx.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.omx.R;
import com.example.omx.model.DataModel;
import com.example.omx.model.ListModel;

import java.util.ArrayList;

public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.ViewHolder>{
    private ArrayList<ListModel> listdata;
Context context;
    int layoutResourceId;
    // RecyclerView recyclerView;
    public HomeListAdapter(Context mContext, int layoutResourceId, ArrayList<ListModel> listdata) {
        this.listdata = listdata;
        this.context = mContext;
        this.layoutResourceId = layoutResourceId;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(layoutResourceId, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
         final ListModel myListData = listdata.get(position);
        holder.textView.setText(myListData.getName());
        holder.imageView.setImageResource(myListData.getIcon());
        holder.listViewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"click on item: "+myListData.getName(),Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;
        public RelativeLayout listViewLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.homeImageView);
            this.textView = (TextView) itemView.findViewById(R.id.homeTitle);
            listViewLayout = (RelativeLayout)itemView.findViewById(R.id.listViewLayout);
        }
    }
}