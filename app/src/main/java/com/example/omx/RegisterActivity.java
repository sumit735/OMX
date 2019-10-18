package com.example.omx;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.omx.model.GetMenuItem;
import com.example.omx.model.SharedPreferenceClass;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RegisterActivity extends AppCompatActivity {

    EditText editNameStr,editEmailStr,editPasswordStr,editPhoneStr;
    Button registerButton;
    LinearLayout btnLogin;
    TextView alreadyHaveALoginButton;
    GetMenuItem menuItem;
    String regNameStr, regEmailStr, regPasswordStr, regPhoneStr;
    int corePoolSize = 60;
    int maximumPoolSize = 80;
    SharedPreferenceClass sharedPreferenceClass;
    int keepAliveTime = 10;
    ArrayList<String> listItems;
    JSONObject obj;
    GetUserDetails asyncReg;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        setContentView(R.layout.activity_register);

        sharedPreferenceClass = new SharedPreferenceClass(RegisterActivity.this);

        editNameStr = (EditText) findViewById(R.id.editNameStr);
        editEmailStr = (EditText) findViewById(R.id.editEmailStr);
        editPasswordStr = (EditText) findViewById(R.id.editPasswordStr);
        editPhoneStr = (EditText) findViewById(R.id.editPhoneStr);
        registerButton = (Button) findViewById(R.id.registerButton);
        alreadyHaveALoginButton = (TextView) findViewById(R.id.alreadyHaveALoginButton);

        Typeface submitButtonTypeface = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.exo));
        editNameStr.setTypeface(submitButtonTypeface);
        editPasswordStr.setTypeface(submitButtonTypeface);
        editPhoneStr.setTypeface(submitButtonTypeface);
        registerButton.setTypeface(submitButtonTypeface);
        alreadyHaveALoginButton.setTypeface(submitButtonTypeface);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                registerButtonClicked();
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                removeFocusFromViews();


            }
        });

        alreadyHaveALoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });


    }


    public void registerButtonClicked() {

        regNameStr = editNameStr.getText().toString().trim();
        regEmailStr = editEmailStr.getText().toString().trim();
        regPasswordStr = editPasswordStr.getText().toString().trim();
        regPhoneStr = editPhoneStr.getText().toString().trim();

        if (!regNameStr.matches("") &&  (!regPasswordStr.matches("")) && !regPhoneStr.equals("")) {
            boolean isValidEmail = Util.isValidMail(regEmailStr);
            boolean isValidMobile = Util.isValidMobile(regPhoneStr);
            if (isValidMobile) {

                //API Calling
                asyncReg = new GetUserDetails();
                asyncReg.executeOnExecutor(threadPoolExecutor);

            } else {


                Toast.makeText(RegisterActivity.this, "OOPS INVALID DATA", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(RegisterActivity.this, "Enter all the fields", Toast.LENGTH_LONG).show();
        }
    }


    public void removeFocusFromViews(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public class GetUserDetails extends AsyncTask<Void, String, JSONArray> {
        JSONArray array;
        ProgressBarHandler pDialog;
        @Override
        protected JSONArray doInBackground(Void... params) {

            try {

                URL url = new URL("http://3.81.18.178/rest/api/create_user.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                httpURLConnection.setRequestProperty("Accept", "application/json");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                JSONObject postData = new JSONObject();
                try {
                    postData.put("firstname", regNameStr);
                    postData.put("mnumber", regPhoneStr);
                    postData.put("email", regEmailStr);
                    postData.put("password", regPasswordStr);

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

                JSONObject object = jsonResponse.getJSONObject("data");

                String name = object.getString("firstname");
                String mobile = object.getString("mnumber");
                String user_id = object.getString("id");


                sharedPreferenceClass.setValue_string("LOGIN_STATUS","1");
                sharedPreferenceClass.setValue_string("LOGIN_ID",user_id);
                sharedPreferenceClass.setValue_string("MOBILE_ID",mobile);
                sharedPreferenceClass.setValue_string("NAME_STR",name);


                Log.v("SUBHA","Login ID == " + jsonResponse);
                // }




                    Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                    startActivity(intent);
               finish();




            } catch (Exception e) {

                e.printStackTrace();
            }
            return array;


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressBarHandler(RegisterActivity.this);
            pDialog.show();

        }

        @Override
        protected void onPostExecute(JSONArray array) {
            super.onPostExecute(array);


        }
    }

}

