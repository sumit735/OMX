
package com.example.omx;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.omx.model.GridItem;
import java.util.ArrayList;



public class VideoFilterAdapter extends ArrayAdapter<GridItem> {
    private Context context;
    private int layoutResourceId;
    private ArrayList<GridItem> data = new ArrayList<GridItem>();

    public VideoFilterAdapter(Context context, int layoutResourceId,
                              ArrayList<GridItem> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.title = (TextView) row.findViewById(R.id.movieTitle);
            holder.videoImageview = (ImageView) row.findViewById(R.id.movieImageView);

            Typeface submitButtonTypeface = Typeface.createFromAsset(context.getAssets(),context.getResources().getString(R.string.exo));
            holder.title.setTypeface(submitButtonTypeface);



            row.setTag(holder);

        } else {
            holder = (ViewHolder) row.getTag();
        }

        final GridItem item = data.get(position);
        holder.title.setText(item.getTitle());



            Glide.with(context)
                    .load(item.getImageId())
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.videoImageview);






        return row;
    }

    static class ViewHolder {
        public TextView title;
        public ImageView videoImageview,playButton;
        public LinearLayout listViewLayout;

    }
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight){
        final BitmapFactory.Options opt =new BitmapFactory.Options();
        opt.inJustDecodeBounds=true;
        BitmapFactory.decodeResource(res, resId, opt);
        opt.inSampleSize = calculateInSampleSize(opt,reqWidth,reqHeight);
        opt.inJustDecodeBounds=false;
        return BitmapFactory.decodeResource(res, resId, opt);
    }
    public static int calculateInSampleSize(BitmapFactory.Options opt, int reqWidth, int reqHeight){
        final int height = opt.outHeight;
        final int width = opt.outWidth;
        int sampleSize=1;
        if (height > reqHeight || width > reqWidth){
            final int halfWidth = width/2;
            final int halfHeight = height/2;
            while ((halfHeight/sampleSize) > reqHeight && (halfWidth/sampleSize) > reqWidth){
                sampleSize *=2;
            }

        }
        return sampleSize;
    }
}