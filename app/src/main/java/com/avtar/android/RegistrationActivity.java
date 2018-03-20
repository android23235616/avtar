package com.avtar.android;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.avtar.android.R;

import java.net.InetAddress;

public class RegistrationActivity extends AppCompatActivity {

    EditText name,adhar,phone,username;
    Button register;
    ProgressDialog progress;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initialize();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!check_internet())
                {

                       String uname=username.getText().toString();
                    String nme=name.getText().toString();
                    String phn=phone.getText().toString();
                    String adha=adhar.getText().toString();
                    if(uname.length()==0 || nme.length()==0 || phn.length()==0 || adha.length()==0 )
                    {
                        Toast.makeText(RegistrationActivity.this, "Kindly fill the Details Carefully", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        register_python(uname,nme,phn,adha);
                    }
                }
            }
        });

    }

    private void register_python(String uname, String nme, String phn, String adha) {

        progress.setMessage("Please Wait. Registration On Process");
        progress.setCancelable(false);
        progress.show();
        StringRequest st=new StringRequest(Request.Method.GET, Constants.url_registration+"?username="+uname+"&name="+nme+"&mobile="+phn+"&ssos="+adha+"&pwd="+"1234", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(progress.isShowing())
                {
                    progress.dismiss();
                }
                //Toast.makeText(RegistrationActivity.this, response, Toast.LENGTH_SHORT).show();
                if(response.equals("1"))
                {
                    Toast.makeText(RegistrationActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(progress.isShowing())
                {
                    progress.dismiss();
                }
                Toast.makeText(RegistrationActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue as= Volley.newRequestQueue(getApplicationContext());
        as.add(st);
    }

    private boolean check_internet() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }

    private void initialize() {
        name=(EditText)findViewById(R.id.name);
        adhar=(EditText)findViewById(R.id.aadhar);
        phone=(EditText)findViewById(R.id.phone);
        username=(EditText) findViewById(R.id.username);
        register=(Button)findViewById(R.id.register);
        progress=new ProgressDialog(RegistrationActivity.this);
        progress.setMessage("Please Wait");
    }
}
