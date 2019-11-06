package com.example.omx;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.omx.model.SharedPreferenceClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SinglePartActivity extends AppCompatActivity {

    ImageView bannerImageView;
    TextView movieDetailTitle,movieStory,genreValue,movieDuration;
    ImageButton playnow;
    int corePoolSize = 60;
    int maximumPoolSize = 80;
    SharedPreferenceClass sharedPreferenceClass;
    int keepAliveTime = 10;
    WatchHistoryStore asyncReg;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
    String bannerUrl, title,story,videoUrl,movieImage,duration,genre,movieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        setContentView(R.layout.activity_single_part);

        bannerImageView = (ImageView) findViewById(R.id.bannerImageView);
        movieDetailTitle = findViewById(R.id.movieDetailTitle);
        movieStory = findViewById(R.id.movieStory);
        genreValue = findViewById(R.id.genreValue);
        movieDuration = findViewById(R.id.movieDuration);
        playnow = findViewById(R.id.playnow);
        sharedPreferenceClass = new SharedPreferenceClass(this);
        /*Toolbar mActionBarToolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        mActionBarToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        mActionBarToolbar.setTitle("");
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*/

        movieImage = getIntent().getStringExtra("MovieImage");
        title = getIntent().getStringExtra("MovieTitle");
        story = getIntent().getStringExtra("MovieDetails");
        videoUrl = getIntent().getStringExtra("MovieUrl");
        bannerUrl = getIntent().getStringExtra("MovieBanner");
        duration = getIntent().getStringExtra("MovieDuration");
        genre = getIntent().getStringExtra("MovieGenre");
        movieId = getIntent().getStringExtra("MovieID");

        if (genre != null){
            genreValue.setText(genre);
        }else{
            genreValue.setVisibility(View.GONE);
        }

        Glide.with(SinglePartActivity.this)
                .load(bannerUrl)
                .thumbnail(0.5f)
                .into(bannerImageView);


        movieDetailTitle.setText(title);
        movieStory.setText(story);
        movieDuration.setText(duration);


        Typeface submitButtonTypeface = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.exo));
        movieDetailTitle.setTypeface(submitButtonTypeface);
        movieStory.setTypeface(submitButtonTypeface);
        movieDuration.setTypeface(submitButtonTypeface);
        genreValue.setTypeface(submitButtonTypeface);

        playnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                asyncReg = new WatchHistoryStore();
                asyncReg.executeOnExecutor(threadPoolExecutor);


            }
        });

    }

    public class WatchHistoryStore extends AsyncTask<Void, String, JSONArray> {
        JSONArray array;
        ProgressBarHandler pDialog;
        @Override
        protected JSONArray doInBackground(Void... params) {

            try {

                URL url = new URL("http://3.81.18.178/oflix/api/recently_watched_store_api.php");
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
                    postData.put("movieId", movieId);

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
                JSONObject jsonResponse = new JSONObject(response);

               /* JSONObject object = jsonResponse.getJSONObject("data");

                String name = object.getString("firstname");
                String mobile = object.getString("mnumber");
                String user_id = object.getString("id");


                sharedPreferenceClass.setValue_string("LOGIN_STATUS","1");
                sharedPreferenceClass.setValue_string("LOGIN_ID",user_id);
                sharedPreferenceClass.setValue_string("MOBILE_ID",mobile);
                sharedPreferenceClass.setValue_string("NAME_STR",name);*/


                Log.v("SUBHA","Login ID == " + jsonResponse);
                // }

                Intent intent = new Intent(SinglePartActivity.this,PlayerActivity.class);
                intent.putExtra("VideoUrl",videoUrl);
                startActivity(intent);



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

}
