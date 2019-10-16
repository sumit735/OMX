package com.example.omx;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.omx.model.GridItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_XLARGE;

public class ShortMoviesFragment extends Fragment {


    GridView gridView;
    ArrayList<String> mItems;
    Context context;
    ArrayList<GridItem> itemData = new ArrayList<GridItem>();
    private VideoFilterAdapter customGridAdapter;
    /*The Data to be posted*/
    int offset = 1;
    int limit = 10;
    int listSize = 0;
    int itemsInServer = 0;
    private boolean mIsScrollingUp;
    private int mLastFirstVisibleItem;
    int scrolledPosition=0;
    boolean scrolling;
    boolean firstTime = false;

    public ShortMoviesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_connect, container, false);

        gridView = (GridView)v.findViewById(R.id.imagesGridView);
        TextView categoryTitle = (TextView) v.findViewById(R.id.categoryTitle);

        categoryTitle.setText("Short Movies");
        callData();

        ViewGroup.LayoutParams layoutParams = gridView.getLayoutParams();
        layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT; //this is in pixels
        gridView.setLayoutParams(layoutParams);
        gridView.setNumColumns(2);
       /* gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        gridView.setGravity(Gravity.CENTER_HORIZONTAL);*/
        resetData();

        customGridAdapter = new VideoFilterAdapter(getActivity(), R.layout.list_item, itemData);

        gridView.setAdapter(customGridAdapter);



        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                if (gridView.getLastVisiblePosition() >= itemsInServer - 1) {

                    return;

                }

                if (view.getId() == gridView.getId()) {
                    final int currentFirstVisibleItem = gridView.getFirstVisiblePosition();

                    if (currentFirstVisibleItem > mLastFirstVisibleItem) {
                        mIsScrollingUp = false;

                    } else if (currentFirstVisibleItem < mLastFirstVisibleItem) {
                        mIsScrollingUp = true;

                    }

                    mLastFirstVisibleItem = currentFirstVisibleItem;
                }
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    scrolling = false;

                } else if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {

                    scrolling = true;

                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {


                if (scrolling == true && mIsScrollingUp == false) {

                    if (firstVisibleItem + visibleItemCount >= totalItemCount) {

                        listSize = itemData.size();
                        if (gridView.getLastVisiblePosition() >= itemsInServer - 1) {
                            return;

                        }
                        offset += 1;

                        callData();
                        scrolling = false;



                    }

                }

            }
        });


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                GridItem item = itemData.get(position);


                Intent intent = new Intent(getActivity(),SinglePartActivity.class);
                intent.putExtra("MovieTitle",item.getTitle());
                intent.putExtra("MovieImage",item.getImageId());
                intent.putExtra("MovieDetails",item.getShort_desc());
                intent.putExtra("MovieUrl",item.getVideoUrl());
                intent.putExtra("MovieBanner",item.getBannerImage());
                intent.putExtra("MovieGenre",item.getMovieGenre());
                intent.putExtra("MovieDuration",item.getMovieDuration());
                intent.putExtra("MovieID",item.getId());
                startActivity(intent);

            }
        });



        return v;
    }



    public void onResume() {
        super.onResume();
        //movieThirdPartyUrl = getResources().getString(R.string.no_data_str);


    }



    //load video urls as per resolution

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private MoviesFragment.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final MoviesFragment.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }


    public void resetData() {
        if (itemData != null && itemData.size() > 0) {
            itemData.clear();
        }
        firstTime = true;

        offset = 1;
        //  isLoading = false;
        listSize = 0;
        if (((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_LARGE) || ((getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) == SCREENLAYOUT_SIZE_XLARGE)) {
            limit = 20;
        } else {
            limit = 15;
        }
        itemsInServer = 0;
        //isSearched = false;
    }


    private void callData() {


        String tag_json_req = "user_login";
        StringRequest data = new StringRequest(Request.Method.POST,
                "http://3.81.18.178/oflix/api/get_shortmovies.php?appid=735426",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  progressDialog.dismiss();

                        try {
                            Log.d(" response is ", response);

                            JSONArray jsonObject = new JSONArray(response);

                            //JSONArray jsonMainNode = jsonObject.getJSONArray("res");


                            Log.v("SUBHA","api res == " + jsonObject);



                            int lengthJsonArr = jsonObject.length();
                            JSONObject jsonChildNode;
                            Log.v("SUBHA","api res == " + lengthJsonArr);
                            for (int i = 0; i < lengthJsonArr; i++) {
                                GridItem movie = new GridItem();
                                jsonChildNode = jsonObject.getJSONObject(i);


                                movie.setId(jsonChildNode.getString("id"));
                                movie.setTitle(jsonChildNode.getString("name"));
                                movie.setImageId(jsonChildNode.getString("image"));
                                movie.setShort_desc(jsonChildNode.getString("details"));
                                movie.setVideoUrl(jsonChildNode.getString("url"));
                                movie.setBannerImage(jsonChildNode.getString("banner_image"));
                                movie.setMovieDuration(jsonChildNode.getString("total_time"));
                                movie.setMovieGenre(jsonChildNode.getString("genre"));


                                itemData.add(movie);


                            }

                            customGridAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.getMessage() == null) {

                } else
                    Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
               /* params.put("method","citizenLogin");
                params.put("strUserName",regEmailStr);
                params.put("strPassword",regPasswordStr);
*/

                Log.d("params are :", "" + params);
                return params;
            }
        };
        data.setShouldCache(false);
        data.setRetryPolicy(new
                DefaultRetryPolicy(30000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance().getRequestQueue().add(data).addMarker(tag_json_req);
    }



}
