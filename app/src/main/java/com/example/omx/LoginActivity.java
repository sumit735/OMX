package com.example.omx;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.omx.model.GridItem;
import com.example.omx.model.SharedPreferenceClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    EditText editPhoneStr,editPasswordStr;
    Button loginButton;
    TextView forgotPasswordTextView,signUpTextView;
    LinearLayout btnLogin;
    String  phoneStr, regPasswordStr;
    int corePoolSize = 60;
    int maximumPoolSize = 80;
    SharedPreferenceClass sharedPreferenceClass;
    int keepAliveTime = 10;
    GetUserDetails asyncReg;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_login);
        sharedPreferenceClass = new SharedPreferenceClass(LoginActivity.this);
        editPhoneStr = (EditText) findViewById(R.id.editPhoneStr);
        editPasswordStr = (EditText) findViewById(R.id.editPasswordStr);
        loginButton = (Button) findViewById(R.id.loginButton);
        forgotPasswordTextView = (TextView) findViewById(R.id.forgotPasswordTextView);
        signUpTextView = (TextView) findViewById(R.id.signUpTextView);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loginButtonClicked();
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                removeFocusFromViews();


            }
        });


        Typeface submitButtonTypeface = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.exo));
        editPasswordStr.setTypeface(submitButtonTypeface);
        editPhoneStr.setTypeface(submitButtonTypeface);
        loginButton.setTypeface(submitButtonTypeface);
        signUpTextView.setTypeface(submitButtonTypeface);


        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,ForgotPasswordActivity.class);
                startActivity(intent);

            }
        });

        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    public void loginButtonClicked() {

        phoneStr = editPhoneStr.getText().toString().trim();
        regPasswordStr = editPasswordStr.getText().toString().trim();


        if (!phoneStr.matches("") && (!regPasswordStr.matches(""))) {
            boolean isValidPhone = Util.isValidMobile(phoneStr);
            if (isValidPhone) {

                //API Calling
                asyncReg = new GetUserDetails();
                asyncReg.executeOnExecutor(threadPoolExecutor);
            } else {


                Toast.makeText(LoginActivity.this, "OOPS INVALID EMAIL", Toast.LENGTH_LONG).show();
            }
        } else {

            Toast.makeText(LoginActivity.this, "ENTER LOGIN FIELD DATA", Toast.LENGTH_LONG).show();

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

                URL url = new URL("http://3.81.18.178/rest/api/login.php");
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
                    postData.put("mnumber", phoneStr);
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

                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
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


        }

        @Override
        protected void onPostExecute(JSONArray array) {
            super.onPostExecute(array);


        }
    }


}
