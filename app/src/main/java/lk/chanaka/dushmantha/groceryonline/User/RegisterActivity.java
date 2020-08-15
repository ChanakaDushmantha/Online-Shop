package lk.chanaka.dushmantha.groceryonline.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import lk.chanaka.dushmantha.groceryonline.Items.MainActivity;
import lk.chanaka.dushmantha.groceryonline.MyApp;
import lk.chanaka.dushmantha.groceryonline.NetworkConnection;
import lk.chanaka.dushmantha.groceryonline.R;
import lk.chanaka.dushmantha.groceryonline.SessionManager;

public class RegisterActivity extends AppCompatActivity {

    private EditText name, email, address, mobile, password;
    private ProgressBar loading;
    private CardView btn_register;
    private static String URL ;
    SessionManager sessionManager;
    private AwesomeValidation awesomeValidation;
    private String host;
    private String token;
    private boolean update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        sessionManager = new SessionManager(this);
        token = sessionManager.getToken();
        host = ((MyApp) this.getApplication()).getServiceURL();
        URL = host+"/register";
        Intent i = getIntent();
        update = i.getBooleanExtra("UPDATE", false);
        if(update){
            sessionManager.checkLogin();
            URL = "";
            URL = host + "/updateUser";
            TextView t = findViewById(R.id.tvSubmit);
            t.setText("Update");
            findViewById(R.id.login).setVisibility(View.INVISIBLE);
            SetToolbar();
            extractData(i.getStringExtra("TOKEN"));
        }

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        loading = findViewById(R.id.loading);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        address = findViewById(R.id.address);
        mobile = findViewById(R.id.mobile);
        password = findViewById(R.id.password);
        btn_register = findViewById(R.id.btn_register);

        try {
            if(sessionManager.getRegType().equals("google")||sessionManager.getRegType().equals("facebook")){
                email.setFocusable(false);
                email.setEnabled(true);
                email.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(RegisterActivity.this, "You are using Social Login", Toast.LENGTH_SHORT).show();
                    }
                });
                password.setVisibility(View.INVISIBLE);
                findViewById(R.id.c_password).setVisibility(View.INVISIBLE);
            }

            if(sessionManager.getRegType().equals("otp")){
                mobile.setFocusable(false);
                mobile.setEnabled(true);
                mobile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(RegisterActivity.this, "You are using Mobile Number Login", Toast.LENGTH_SHORT).show();
                    }
                });
                password.setVisibility(View.INVISIBLE);
                email.setVisibility(View.INVISIBLE);
                findViewById(R.id.c_password).setVisibility(View.INVISIBLE);
            }

            if(sessionManager.getRegType().equals("app")){
                awesomeValidation.addValidation(this, R.id.password, "[A-Za-z0-9\\.]{6,}", R.string.passworderror);
                awesomeValidation.addValidation(this, R.id.c_password, R.id.password, R.string.err_password_confirmation);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //adding validation to edit texts
        //String regexPassword = "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d]).{6,}";
        awesomeValidation.addValidation(this, R.id.name, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
        awesomeValidation.addValidation(this, R.id.email, Patterns.EMAIL_ADDRESS, R.string.emailerror);
        awesomeValidation.addValidation(this, R.id.address, RegexTemplate.NOT_EMPTY, R.string.addresserror);
        awesomeValidation.addValidation(this, R.id.mobile, "^[0]{1}[0-9]{9}$", R.string.mobileerror);


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(new NetworkConnection( RegisterActivity.this).isNetworkConnected())){
                    Toast.makeText(RegisterActivity.this, "Internet Connection Error", Toast.LENGTH_LONG).show();
                }
                else if (awesomeValidation.validate()) {
                    Regist();
                }
                /*Intent intent = new Intent(RegisterActivity.this, ProfilePicture.class);
                startActivity(intent);*/
            }
        });

    }

    private void extractData(String token) {

        String URLuserProofile = host + "/userProfile";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLuserProofile,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONObject data = jsonObject.getJSONObject("data");

                            if(success.equals("true")){
                                setData(data);
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(RegisterActivity.this, "RegisterActivity Error 1 ! "+e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorMsg = "Error";
                if (error instanceof NoConnectionError) {
                    errorMsg = getString(R.string.noConnectionError);
                } else if (error instanceof TimeoutError) {
                    errorMsg = getString(R.string.timeoutError);
                } else if (error instanceof AuthFailureError) {
                    errorMsg = getString(R.string.authFailureError);
                } else if (error instanceof ServerError) {
                    errorMsg = getString(R.string.serverError);
                } else if (error instanceof NetworkError) {
                    errorMsg = getString(R.string.networkError);
                } else if (error instanceof ParseError) {
                    errorMsg = getString(R.string.parseError);
                }
                Toast.makeText(RegisterActivity.this, errorMsg, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + token);
                return params;
            }
        };

        queue.add(stringRequest);
    }

    private void setData(JSONObject data) {
        try {
            this.name.setText(data.getString("name"));

            if(!data.getString("email").equals("null")){
                this.email.setText(data.getString("email"));
            }
            if(!data.getString("address").equals("null")){
                this.address.setText(data.getString("address"));
            }
            if(!data.getString("contact_no").equals("null")){
                this.mobile.setText(data.getString("contact_no"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
                /*"id": 3,
        "shop_id": null,
        "name": "Vihanga",
        "email": "vihanga@gmail.com",
        "address": "dfnjhjkfgjhjslf",
        "contact_no": "0710390283",
        "user_type": "user",
        "image_url": "http://10.0.2.2:8000ADGGHGHF456asdfre",
        "email_verified_at": null,
        "created_at": "2020-03-05 00:00:00",
        "updated_at": "2020-03-05 00:00:00"*/
    }

    private void Regist(){
        /*loading.setVisibility(View.VISIBLE);
        btn_register.setVisibility(View.INVISIBLE);*/
        SweetAlertDialog pDialog = new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading ...");
        pDialog.setCancelable(false);
        pDialog.show();

        final String name = this.name.getText().toString().trim();
        final String email = this.email.getText().toString().trim();
        final String address = this.address.getText().toString().trim();
        final String mobile = this.mobile.getText().toString().trim();
        final String password = this.password.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success =  jsonObject.getString("success");

                            if(success.equals("true")){

                                if(update){
                                    pDialog.dismissWithAnimation();
                                    sessionManager.createSession(name, email, address, token, mobile);
                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else{
                                    pDialog.dismissWithAnimation();
                                    /*JSONObject data = jsonObject.getJSONObject("data");
                                    String newtoken = data.getString("token");
                                    String reg_type = data.getString("reg_type");
                                    String mobile = data.getString("contact_no");
                                    sessionManager.createSession(name, email, address, newtoken, null, reg_type, mobile);*/
                                    String message =  jsonObject.getString("message");

                                    //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                    SweetAlertDialog cDialog = new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                                    cDialog.setCancelable(false);
                                    cDialog.setTitleText("Verification!")
                                            .setContentText(message)
                                            .setConfirmText("OK")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sDialog) {
                                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            })
                                            .show();

                                }
                                /*"success": true,
                                    "message": "Register Success, Please check your email to verify Your account",
                                    "data": null*/


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(RegisterActivity.this, "RegisterActivity Error 1 ! "+e.toString(), Toast.LENGTH_LONG).show();

                            /*loading.setVisibility(View.GONE);
                            btn_register.setVisibility(View.VISIBLE);*/
                            pDialog.dismissWithAnimation();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMsg = "Error";
                        if (error instanceof NoConnectionError) {
                            errorMsg = getString(R.string.noConnectionError);
                        } else if (error instanceof TimeoutError) {
                            errorMsg = getString(R.string.timeoutError);
                        } else if (error instanceof AuthFailureError) {
                            errorMsg = getString(R.string.authFailureError);
                        } else if (error instanceof ServerError) {
                            errorMsg = "Email is already exists";
                        } else if (error instanceof NetworkError) {
                            errorMsg = getString(R.string.networkError);
                        } else if (error instanceof ParseError) {
                            errorMsg = getString(R.string.parseError);
                        }
                        Toast.makeText(RegisterActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                        /*loading.setVisibility(View.GONE);
                        btn_register.setVisibility(View.VISIBLE);*/
                        pDialog.dismissWithAnimation();

                    }
                })
        {
            /*@Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                int mStatusCode = response.statusCode;
                Log.d("status", String.valueOf(mStatusCode));
                System.out.println("status");
                return super.parseNetworkResponse(response);
            }*/

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + token);
                return params;
            }


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("email", email);
                params.put("address", address);
                params.put("contact_no", mobile);
                params.put("password", password);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(  //5s need, without this TimeOut Error
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void register(View view) {
        //
    }

    public void loginActivity(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void SetToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle("");
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                //finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
