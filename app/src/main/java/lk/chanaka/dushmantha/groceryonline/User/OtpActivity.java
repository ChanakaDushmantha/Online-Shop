package lk.chanaka.dushmantha.groceryonline.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import lk.chanaka.dushmantha.groceryonline.Items.MainActivity;
import lk.chanaka.dushmantha.groceryonline.MyApp;
import lk.chanaka.dushmantha.groceryonline.R;

public class OtpActivity extends AppCompatActivity {
    private AwesomeValidation awesomeValidation;
    private EditText name, mobile;
    private CardView btn_next;
    private String URL, host;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        host = ((MyApp) this.getApplication()).getServiceURL();
        URL = host+"/mobileLogin";
        SetToolbar();

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        name = findViewById(R.id.name);
        mobile = findViewById(R.id.mobile);
        btn_next = findViewById(R.id.btn_next);

        awesomeValidation.addValidation(this, R.id.name, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
        awesomeValidation.addValidation(this, R.id.mobile, "^[0]{1}[0-9]{9}$", R.string.mobileerror);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (awesomeValidation.validate()) {
                    Next();
                }
            }
        });

    }

    private void Next() {
        SweetAlertDialog pDialog = new SweetAlertDialog(OtpActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading ...");
        pDialog.setCancelable(false);
        pDialog.show();

        final String name = this.name.getText().toString().trim();
        final String mobile = this.mobile.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("true")) {
                                pDialog.dismissWithAnimation();

                                String message = jsonObject.getString("message");
                                SweetAlertDialog cDialog = new SweetAlertDialog(OtpActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                                cDialog.setCancelable(false);
                                cDialog.setTitleText("Verification!")
                                        .setContentText(message)
                                        .setConfirmText("OK")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                Intent intent = new Intent(getApplicationContext(), OtpVerifyActivity.class);
                                                intent.putExtra("MOBILE", mobile);
                                                startActivity(intent);
                                                //finish();
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
                        Toast.makeText(OtpActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                        pDialog.dismissWithAnimation();

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("contact_no", mobile);
                return params;
            }
        };

        /*stringRequest.setRetryPolicy(new DefaultRetryPolicy(  //5s need, without this TimeOut Error
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
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
                onBackPressed();
                /*Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();*/
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
