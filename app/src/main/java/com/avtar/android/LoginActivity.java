package com.avtar.android;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.avtar.android.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;

public class LoginActivity extends AppCompatActivity {
    //phone number
    //email
    //name
    //mobile number
    EditText etMobile,etUserName,etOTP;
    Button btnLogin, btnVerifyOTP;
    String username, mMobile;
    RelativeLayout loginLayout, otpLayout;
    ProgressDialog progress;
    TextView txtMobile, txtRegister;
    ImageView imgBhamashah;
    LinearLayout grpMobile;
    
    final String TEST_USERNAME = "VPRANSH";
    final String TEST_MOBILE = "7064002332";
    final String TEST_NAME = "Pranshoo Verma";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initialize();


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!check_internet_connection())
                {
                    username = etUserName.getText().toString();
                    mMobile = etMobile.getText().toString();

                    if(!username.startsWith("av")){
                        //fetchBhamashah(username, mobile);
                        if(username.equals(TEST_USERNAME)){

                            //Toast.makeText(LoginActivity.this, "Name: "+TEST_NAME+"\nMobile: " + TEST_MOBILE, Toast.LENGTH_LONG).show();
                            //mobileLogin(username, TEST_MOBILE);
                            startActivity(new Intent(LoginActivity.this, QRScanningActivity.class));

                        }
                        else {
                            getResponse(username);
                        }
                    }
                    else {

                        mobileLogin(username, mMobile);

                    }

                }
            }
        });


        btnVerifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = etOTP.getText().toString();
                verifyOTP(etMobile.getText().toString(), otp);
            }
        });

        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });

        etUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(etUserName.getText().toString().length() == 7 && !etUserName.getText().toString().startsWith("av")){
                    imgBhamashah.setVisibility(View.VISIBLE);
                    grpMobile.setVisibility(View.INVISIBLE);
                    btnLogin.setText("Login using Bhamashah");
                }
                else {
                    imgBhamashah.setVisibility(View.GONE);
                    grpMobile.setVisibility(View.VISIBLE);
                    btnLogin.setText("Login to AVTAR");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void mobileLogin(final String username, final String mobile) {

        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Message");
        builder.setMessage("OTP will be sent to +91-" + mobile + " for verification. Do you want to continue?");
        builder.setPositiveButton("PROCEED", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startLogin(username, mobile);
            }
        });

        builder.setNegativeButton("CANCEL", null);

        builder.show();

    }



    private void getResponse(String familyID){
        
        progress.setMessage("Loading Bhamashah details...");
        progress.show();
        Toast.makeText(this, "getResponse called", Toast.LENGTH_SHORT).show();

        String URL = "https://apitest.sewadwaar.rajasthan.gov.in/app/live/Service/hofAndMember/ForApp/"+familyID+"?client_id=ad7288a4-7764-436d-a727-783a977f1fe1";

        StringRequest rq = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(LoginActivity.this, response, Toast.LENGTH_LONG).show();
                if(progress.isShowing())
                {
                    progress.dismiss();
                }
                try {
                    JSONObject a=new JSONObject(response);
                    JSONObject details = a.getJSONObject("hof_Details");
                    String aadharID = details.getString("AADHAR_ID");
                    String name = details.getString("NAME_ENG");
                    String mobile = details.getString("MOBILE_NO");
                    Toast.makeText(LoginActivity.this, "Mobile: "+mobile+"\nName: "+name, Toast.LENGTH_LONG).show();
                    
                    mobileLogin(username, mobile);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue q = Volley.newRequestQueue(LoginActivity.this);
        q.add(rq);

    }


    private void startLogin(String username, final String mobile) {
        ///////////////////To Do: CODE TO SEND OTP///////////////////


       if(!progress.isShowing())
       {
           progress.show();
       }

        StringRequest str=new StringRequest(Request.Method.GET, "https://control.msg91.com/api/sendotp.php?authkey=204505AOZvMjzt5aaff3e5&mobile="+mobile+"&message=Your%20OTP%20is%20%23%23OTP%23%23%20.%20It%20is%20Valid%20for%203%20minutes%20only.&sender=AVTARSS&otp_expiry=10", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(progress.isShowing())
                {
                    progress.dismiss();
                }
                try {
                    JSONObject a=new JSONObject(response);
                    String s=a.getString("type");
                    if(s.equalsIgnoreCase("success"))
                    {
                        Toast.makeText(LoginActivity.this, "OTP SENT SUCCESSFULLY", Toast.LENGTH_SHORT).show();
                        loginLayout.setVisibility(View.GONE);
                        otpLayout.setVisibility(View.VISIBLE);
                        txtMobile.setText(mobile);
                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this, "Please Try Internet Connection", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(progress.isShowing())
                {
                    progress.dismiss();
                }
                Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
        queue.add(str);

    }

    private void verifyOTP(String mobile, String otp){
        if(progress.isShowing())
        {
            progress.dismiss();
        }
        Toast.makeText(this, mobile+"\n"+otp, Toast.LENGTH_SHORT).show();
        progress.setMessage("Verifying OTP.Please Wait");
        progress.show();
        StringRequest st=new StringRequest(Request.Method.GET, "https://control.msg91.com/api/verifyRequestOTP.php?authkey=204505AOZvMjzt5aaff3e5&mobile="+mobile+"&otp="+ otp, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(progress.isShowing())
                { progress.dismiss();
                }
                try {
                    JSONObject a=new JSONObject(response);
                    String d=a.getString("type");
                    Toast.makeText(LoginActivity.this, d, Toast.LENGTH_SHORT).show();
                        if(d.equals("success"))
                    {
                        Intent a1=new Intent(getApplicationContext(),QRScanningActivity.class);
                        startActivity(a1);
                        finish();
                    }
                    else{
                        Toast.makeText(LoginActivity.this, "OTP IS NOT CORRECT OR IT IS EXPIRED", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(progress.isShowing())
                {
                    progress.dismiss();
                }
            }
        });

        RequestQueue a=Volley.newRequestQueue(getApplicationContext());
        a.add(st);
    }

    private boolean check_internet_connection() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }

    }

    private void initialize() {

        etUserName=(EditText) findViewById(R.id.name);
        etMobile = (EditText) findViewById(R.id.mobile);
        btnLogin=(Button) findViewById(R.id.btnLogin);
        etOTP = (EditText) findViewById(R.id.otp);
        btnVerifyOTP = (Button) findViewById(R.id.btnVerifyOTP);
        loginLayout = (RelativeLayout)findViewById(R.id.login_details_layout);
        otpLayout = (RelativeLayout)findViewById(R.id.otp_layout);
        txtMobile = (TextView) findViewById(R.id.txt_mobile);
        txtRegister = (TextView) findViewById(R.id.txt_register);
        imgBhamashah = (ImageView)findViewById(R.id.img_bhamashah);
        grpMobile = (LinearLayout)findViewById(R.id.grp_pass);

        // Set visibilities:
        loginLayout.setVisibility(View.VISIBLE);
        otpLayout.setVisibility(View.GONE);
        progress=new ProgressDialog(this);
        progress.setMessage("Please wait.");
    }
}
