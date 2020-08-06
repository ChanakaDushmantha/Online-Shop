package lk.chanaka.dushmantha.groceryonline.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import lk.chanaka.dushmantha.groceryonline.SessionManager;

public class ForgotActivity extends AppCompatActivity {
    private AwesomeValidation awesomeValidation;
    private EditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        SetToolbar();

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        setValidation();
    }

    private void setValidation() {
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.email, Patterns.EMAIL_ADDRESS, R.string.emailerror);
        awesomeValidation.addValidation(this, R.id.password, "[A-Za-z0-9\\.]{6,}", R.string.passworderror);
        awesomeValidation.addValidation(this, R.id.c_password, R.id.password, R.string.err_password_confirmation);
    }

    public void loginActivity(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void reset(View view) {
        String host = ((MyApp) this.getApplication()).getServiceURL();
        String URL = host+"/passwordReset";
        if(awesomeValidation.validate()){

            SweetAlertDialog pDialog = new SweetAlertDialog(ForgotActivity.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Loading ...");
            pDialog.setCancelable(false);
            pDialog.show();

            final String email = this.email.getText().toString().trim();
            final String password = this.password.getText().toString().trim();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            pDialog.dismissWithAnimation();
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success =  jsonObject.getString("success");

                                if(success.equals("true")){
                                    String message =  jsonObject.getString("message");
                                    SweetAlertDialog cDialog = new SweetAlertDialog(ForgotActivity.this, SweetAlertDialog.SUCCESS_TYPE);
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

                            } catch (JSONException e) {
                                e.printStackTrace();
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
                            Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
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
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("email", email);
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
    }
    private void SetToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle("Reset Password");
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //onBackPressed();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
