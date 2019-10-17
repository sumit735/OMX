package com.example.omx;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.omx.adapter.DrawerItemCustomAdapter;
import com.example.omx.adapter.GenreAdapter;
import com.example.omx.adapter.HomeListAdapter;

import com.example.omx.adapter.MovieAdapter;
import com.example.omx.model.GenreItem;
import com.example.omx.model.GetMenuItem;
import com.example.omx.model.GridItem;
import com.example.omx.model.ListModel;
import com.example.omx.model.SharedPreferenceClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class HomeFragment extends Fragment {


    Context context;
    ArrayList<GenreItem> genreItems = new ArrayList<GenreItem>();
    int currentPage = 0;
    String section_name;
    String section_id;
    RecyclerView genreListView;
    Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000; // time in milliseconds between successive task executions.
    ArrayList<GetMenuItem> menuItems;
    RecyclerView my_recycler_view;
    ViewPager mViewPager;
    ViewPager bannerViewPager;
    LinearLayoutManager mLayoutManager;
    Button latest_songs, latest_shortfilms, latest_webseries, latest_movies;
    MovieAdapter mAdapter;
    GenreAdapter genreAdapter;
    RelativeLayout loadingPanel;
    SharedPreferenceClass sharedPreferenceClass;
    ArrayList<GridItem> itemData = new ArrayList<GridItem>();
    int keepAliveTime = 10;
    int corePoolSize = 60;
    int maximumPoolSize = 80;
    WatchHistoryList asyncReg;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        menuItems = new ArrayList<GetMenuItem>();
        my_recycler_view = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        mViewPager = (ViewPager) v.findViewById(R.id.pager);
        bannerViewPager = (ViewPager) v.findViewById(R.id.pager2);
        loadingPanel = (RelativeLayout) v.findViewById(R.id.loadingPanel);
        genreListView = (RecyclerView) v.findViewById(R.id.genreListView);
        latest_movies = (Button) v.findViewById(R.id.latest_movies);
        latest_webseries = (Button) v.findViewById(R.id.latest_webseries);
        latest_shortfilms = (Button) v.findViewById(R.id.latest_shortfilms);
        latest_songs = (Button) v.findViewById(R.id.latest_songs);
        sharedPreferenceClass = new SharedPreferenceClass(getActivity());


        latest_movies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LatestVideos.class);
                intent.putExtra("MOVIES", "movies");
                startActivity(intent);

            }
        });

        latest_webseries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LatestVideos.class);
                intent.putExtra("WEBSERIES", "web_series");
                startActivity(intent);
            }
        });

        latest_shortfilms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LatestVideos.class);
                intent.putExtra("SHORTFILMS", "short_films");
                startActivity(intent);
            }
        });

        latest_songs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LatestVideos.class);
                intent.putExtra("SONGS", "songs");
                startActivity(intent);
            }
        });


        callData();

        TransformerAdapter adapterView = new TransformerAdapter(getActivity());
        mViewPager.setAdapter(adapterView);
        mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

        asyncReg = new WatchHistoryList();
        asyncReg.executeOnExecutor(threadPoolExecutor);


        return v;


    }


    @Override
    public void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        super.onStop();
    }


    public class WatchHistoryList extends AsyncTask<Void, String, JSONArray> {
        JSONArray array;
        ProgressBarHandler pDialog;

        @SuppressLint("WrongThread")
        @Override
        protected JSONArray doInBackground(Void... params) {

            try {

                URL url = new URL("http://3.81.18.178/oflix/api/recently_watched_display.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                httpURLConnection.setRequestProperty("Accept", "application/json");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                JSONObject postData = new JSONObject();
                try {
                    postData.put("userId", sharedPreferenceClass.getValue_string("LOGIN_ID"));

                    Log.v("SUBHA", "json Data == " + postData.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                DataOutputStream outputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                outputStream.write(postData.toString().getBytes("UTF-8"));

                int code = httpURLConnection.getResponseCode();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

                String line = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                bufferedReader.close();

                String response = stringBuilder.toString();
                // JSONObject jsonResponse = new JSONObject(response);

                try {
                    Log.d(" response is ", response);

                    JSONArray jsonObject = new JSONArray(response);

                    //JSONArray jsonMainNode = jsonObject.getJSONArray("res");
                    if (response != null) {

                        Log.v("SUBHA", "api res == " + jsonObject);


                        int lengthJsonArr = jsonObject.length();
                        JSONObject jsonChildNode;
                        Log.v("SUBHA", "api res == " + lengthJsonArr);
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

                        mAdapter = new MovieAdapter(itemData, getContext());
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // set Horizontal Orientation
                        my_recycler_view.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView
                        my_recycler_view.setItemAnimator(new DefaultItemAnimator());
                        my_recycler_view.setAdapter(mAdapter);
                    } else {
                        Toast.makeText(context, "No Recently Watched Videos", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } catch (Exception e) {

                e.printStackTrace();
            }
            return array;


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected void onPostExecute(JSONArray array) {
            super.onPostExecute(array);


        }
    }

    private void callData() {


        String tag_json_req = "user_login";
        StringRequest data = new StringRequest(Request.Method.POST,
                "http://3.81.18.178/oflix/api/genre_list_api.php?appid=735426",
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
                                GenreItem movie = new GenreItem();
                                jsonChildNode = jsonObject.getJSONObject(i);

                                movie.setId(jsonChildNode.getString("id"));
                                movie.setTitle(jsonChildNode.getString("genre"));
                                movie.setImage(jsonChildNode.getString("genre_image"));

                                genreItems.add(movie);


                            }

                            genreAdapter = new GenreAdapter(genreItems, getContext());
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // set Horizontal Orientation
                            genreListView.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView
                            genreListView.setItemAnimator(new DefaultItemAnimator());
                            genreListView.setAdapter(genreAdapter);

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




