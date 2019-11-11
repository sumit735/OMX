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
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.omx.adapter.GenreAdapter;

import com.example.omx.adapter.MovieAdapter;
import com.example.omx.adapter.AdsAdapter;
import com.example.omx.adapter.NewReleasesAdapter;
import com.example.omx.adapter.RecyclerViewDataAdapter;
import com.example.omx.model.AdsItem;
import com.example.omx.model.BannerItem;
import com.example.omx.model.GenreItem;
import com.example.omx.model.GetMenuItem;
import com.example.omx.model.GridItem;
import com.example.omx.model.HomeFeaturePageSectionDetailsModel;
import com.example.omx.model.HomeFeaturePageSectionModel;
import com.example.omx.model.RecentwatchItem;
import com.example.omx.model.SectionDataModel;
import com.example.omx.model.SharedPreferenceClass;
import com.example.omx.model.SingleItemModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
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

    ProgressBarHandler pDialog;
    Context context;
    ArrayList<GenreItem> genreItems = new ArrayList<GenreItem>();
    ArrayList<AdsItem> adsItems = new ArrayList<AdsItem>();
    ArrayList<BannerItem> bannerItems = new ArrayList<BannerItem>();
    int currentPage = 0;
    String section_name;
    String section_id;
    RecyclerView genreListView;
    Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000; // time in milliseconds between successive task executions.
    ArrayList<GetMenuItem> menuItems;
    RecyclerView my_recycler_view;
    RecyclerView adspager;
    RecyclerView bannerpager;
    LinearLayoutManager mLayoutManager;
    Button latest_songs, latest_shortfilms, latest_webseries, latest_movies;
    MovieAdapter mAdapter;
    GenreAdapter genreAdapter;
    RelativeLayout loadingPanel;
    SharedPreferenceClass sharedPreferenceClass;
    ArrayList<GridItem> itemData = new ArrayList<GridItem>();
    ArrayList<RecentwatchItem> recentwatchItems = new ArrayList<RecentwatchItem>();
    ArrayList<HomeFeaturePageSectionModel> homePageSectionModelArrayList = new ArrayList<HomeFeaturePageSectionModel>();
    int keepAliveTime = 10;
    int corePoolSize = 60;
    int maximumPoolSize = 80;
    String sectionName, sectionId;
    ArrayList<SingleItemModel> singleItems = new ArrayList<SingleItemModel>();
    ArrayList<SectionDataModel> allSampleData;
    GetUserDetails asyncReg;
    RecyclerViewDataAdapter adapter;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        menuItems = new ArrayList<GetMenuItem>();
        allSampleData = new ArrayList<SectionDataModel>();
        adspager = (RecyclerView) v.findViewById(R.id.adspager);
        my_recycler_view = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        bannerpager = (RecyclerView) v.findViewById(R.id.bannerpager);
        loadingPanel = (RelativeLayout) v.findViewById(R.id.loadingPanel);
        latest_movies = (Button) v.findViewById(R.id.latest_movies);
        latest_webseries = (Button) v.findViewById(R.id.latest_webseries);
        latest_shortfilms = (Button) v.findViewById(R.id.latest_shortfilms);
        latest_songs = (Button) v.findViewById(R.id.latest_songs);
        sharedPreferenceClass = new SharedPreferenceClass(getActivity());

        my_recycler_view.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

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
        latestreleasesBannerList();


        return v;


    }


    @Override
    public void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        super.onStop();
    }


    @Override
    public void onResume() {

        super.onResume();
        singleItems.clear();
        new GetUserDetails().execute();

    }

    public class GetUserDetails extends AsyncTask<Void, String, JSONArray> {
        JSONArray array;
        ProgressBarHandler pDialog;

        @SuppressLint("WrongThread")
        @Override
        protected JSONArray doInBackground(Void... params) {

            try {

                URL url = new URL("http://3.81.18.178/oflix/api/homepagev2.php?appid=735426");
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
                JSONArray myJson = new JSONArray(response);


                return myJson;

            } catch (Exception e) {

                e.printStackTrace();
            }
            return array;


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressBarHandler(getActivity());
            pDialog.show();
        }

        @Override
        protected void onPostExecute(JSONArray array) {
            super.onPostExecute(array);
            try {

                allSampleData.clear();

                if (array.length() > 0) {
                    for (int j = 0; j < array.length(); j++) {
                        JSONObject jsonChildNode = array.getJSONObject(j);


                        sectionId = jsonChildNode.optString("id");
                        sectionName = jsonChildNode.optString("name");

                        JSONArray section_item_list = jsonChildNode.optJSONArray("data");
                        ArrayList<HomeFeaturePageSectionDetailsModel> arrayList = new ArrayList<>();
                        SingleItemModel singleItemModel = new SingleItemModel();
                        if (section_item_list.length() > 0) {
                            for (int k = 0; k < section_item_list.length(); k++) {


                                JSONObject object = section_item_list.getJSONObject(k);


                                singleItemModel.setTitle(object.optString("name"));
                                singleItemModel.setBanner(object.optString("banner_image"));
                                singleItemModel.setDetails(object.optString("details"));
                                singleItemModel.setMovieGenre(object.optString("genre"));
                                singleItemModel.setImageId(object.optString("id"));
                                singleItemModel.setTotal_time(object.optString("total_time"));
                                singleItemModel.setVideoUrl(object.optString("url"));
                                singleItemModel.setImagedata(object.optString("image"));


                            }
                            singleItems.add(singleItemModel);

                        }

                        allSampleData.add(new SectionDataModel(sectionId, sectionName, singleItems));
                    }


                }
                adapter = new RecyclerViewDataAdapter(getActivity(), allSampleData);
                my_recycler_view.setLayoutManager(mLayoutManager);
                my_recycler_view.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {

            }
        }
    }

    private void adsBannerList() {


        String tag_json_req = "user_login";
        StringRequest data = new StringRequest(Request.Method.POST,
                "http://3.81.18.178/oflix/api/genre_list_api.php?appid=735426",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            Log.d(" response is ", response);

                            JSONArray jsonObject = new JSONArray(response);


                            Log.v("SUBHA", "api res == " + jsonObject);


                            int lengthJsonArr = jsonObject.length();
                            JSONObject jsonChildNode;
                            Log.v("SUBHA", "api res == " + lengthJsonArr);
                            for (int i = 0; i < lengthJsonArr; i++) {
                                AdsItem movie = new AdsItem();
                                jsonChildNode = jsonObject.getJSONObject(i);

                                movie.setId(jsonChildNode.getString("id"));
                                movie.setImage(jsonChildNode.getString("genre_image"));

                                adsItems.add(movie);


                            }

                            AdsAdapter adsAdapter = new AdsAdapter(adsItems, getActivity());
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // set Horizontal Orientation
                            adspager.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView
                            adspager.setItemAnimator(new DefaultItemAnimator());
                            adspager.setAdapter(adsAdapter);

                            //asyncReg = new GetUserDetails();
                            // asyncReg.executeOnExecutor(threadPoolExecutor);

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

    private void latestreleasesBannerList() {


        String tag_json_req = "user_login";
        StringRequest data = new StringRequest(Request.Method.POST,
                "http://3.81.18.178/oflix/api/banners.php?appid=735426",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d(" response is ", response);

                            JSONArray jsonObject = new JSONArray(response);

                            //JSONArray jsonMainNode = jsonObject.getJSONArray("res");


                            Log.v("SUBHA", "api res == " + jsonObject);


                            int lengthJsonArr = jsonObject.length();
                            JSONObject jsonChildNode;
                            Log.v("SUBHA", "api res == " + lengthJsonArr);
                            for (int i = 0; i < lengthJsonArr; i++) {
                                BannerItem movie = new BannerItem();
                                jsonChildNode = jsonObject.getJSONObject(i);

                                movie.setId(jsonChildNode.getString("id"));
                                movie.setImage(jsonChildNode.getString("imgurl"));

                                bannerItems.add(movie);


                            }

                            NewReleasesAdapter adsAdapter = new NewReleasesAdapter(bannerItems, getActivity());
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // set Horizontal Orientation
                            bannerpager.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView
                            bannerpager.setItemAnimator(new DefaultItemAnimator());
                            bannerpager.setAdapter(adsAdapter);

                            adsBannerList();


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




