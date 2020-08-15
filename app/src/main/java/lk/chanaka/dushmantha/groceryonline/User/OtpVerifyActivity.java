package lk.chanaka.dushmantha.groceryonline.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import lk.chanaka.dushmantha.groceryonline.Items.MainActivity;
import lk.chanaka.dushmantha.groceryonline.MyApp;
import lk.chanaka.dushmantha.groceryonline.R;
import lk.chanaka.dushmantha.groceryonline.SessionManager;

public class OtpVerifyActivity extends AppCompatActivity {
    private EditText otp1, otp2, otp3, otp4;
    private CardView btn_verify;
    private String URL, host, mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verify);

        host = ((MyApp) this.getApplication()).getServiceURL();
        URL = host+"/mobileVerify";
        SetToolbar();

        Intent intent = getIntent();
        mobile = intent.getStringExtra("MOBILE");

        otp1 = findViewById(R.id.otp1);
        otp2 = findViewById(R.id.otp2);
        otp3 = findViewById(R.id.otp3);
        otp4 = findViewById(R.id.otp4);
        btn_verify = findViewById(R.id.btn_verify);

        otp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (count==1)
                    otp1.clearFocus();
                    otp2.requestFocus();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        otp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (count==1)
                    otp2.clearFocus();
                    otp3.requestFocus();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        otp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (count==1)
                    otp3.clearFocus();
                    otp4.requestFocus();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verify();
            }
        });
    }

    private void verify() {
        {
            SweetAlertDialog pDialog = new SweetAlertDialog(OtpVerifyActivity.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Loading ...");
            pDialog.setCancelable(false);
            pDialog.show();

            final String otp1 = this.otp1.getText().toString().trim();
            final String otp2 = this.otp2.getText().toString().trim();
            final String otp3 = this.otp3.getText().toString().trim();
            final String otp4 = this.otp4.getText().toString().trim();

            final String otp = otp1+otp2+otp3+otp4;

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            /*"success": true,
                                "message": "Verification Success",
                                "data": {
                                    "name": "Vihanga Gimhan",
                                    "contact_no": "0710390283",
                                    "reg_type": "otp",
                                    "image_url": null,
                                    "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUz"*/
                            SessionManager sessionManager = new SessionManager(getApplicationContext());
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                JSONObject data = jsonObject.getJSONObject("data");
                                String name = data.getString("name");
                                String contact_no = data.getString("contact_no");
                                String reg_type = data.getString("reg_type");
                                String image_url = data.getString("image_url");
                                String token = data.getString("token");

                                sessionManager.createSession(name, null, null, token, image_url, reg_type, contact_no);

                                if (success.equals("true")) {
                                    pDialog.dismissWithAnimation();

                                    String message = jsonObject.getString("message");
                                    SweetAlertDialog cDialog = new SweetAlertDialog(OtpVerifyActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                                    cDialog.setCancelable(false);
                                    cDialog.setTitleText("Verification!")
                                            .setContentText(message)
                                            .setConfirmText("OK")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sDialog) {
                                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            })
                                            .show();
                                }
                                else{
                                    pDialog.dismissWithAnimation();

                                    String message = jsonObject.getString("message");
                                    SweetAlertDialog cDialog = new SweetAlertDialog(OtpVerifyActivity.this, SweetAlertDialog.WARNING_TYPE);
                                    cDialog.setCancelable(false);
                                    cDialog.setTitleText("Verification!")
                                            .setContentText(message)
                                            .setConfirmText("OK")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sDialog) {
                                                    Intent intent = new Intent(getApplicationContext(), OtpActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            })
                                            .show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
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
                                errorMsg = getString(R.string.serverError);
                            } else if (error instanceof NetworkError) {
                                errorMsg = getString(R.string.networkError);
                            } else if (error instanceof ParseError) {
                                errorMsg = getString(R.string.parseError);
                            }
                            Toast.makeText(OtpVerifyActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                            pDialog.dismissWithAnimation();

                        }
                    })
            {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("verification_code", otp);
                    params.put("contact_no", mobile);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
    }

    private void SetToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        //this.getSupportActionBar().setTitle("LOGIN");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //onBackPressed();
                Intent intent = new Intent(this, OtpActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
