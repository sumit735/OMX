package com.example.omx;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText editEmailStr;
    TextView loginTextView;
    Button submitButton;
    String regEmailStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        submitButton = (Button) findViewById(R.id.submitButton);
        loginTextView = (TextView) findViewById(R.id.loginTextView);
        editEmailStr = (EditText) findViewById(R.id.editEmailStr);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                regEmailStr = editEmailStr.getText().toString().trim();

                boolean isValidEmail = Util.isValidMail(regEmailStr);
                if (isValidEmail) {

                    //API Calling

                } else {


                    Toast.makeText(ForgotPasswordActivity.this, "OOPS INVALID EMAIL", Toast.LENGTH_LONG).show();
                }
            }
        });


    }
}
