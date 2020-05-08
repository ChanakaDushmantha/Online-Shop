package lk.chanaka.dushmantha.groceryonline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.UriMatcher;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText email, password;
    private ProgressBar loading;
    private CardView btn_login;
    private static String URL;
    private String host;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(this);
        host = ((MyApp) this.getApplication()).getServiceURL();
        URL = host+"/login";
        //URL = HOST+"/api/login";

        loading = findViewById(R.id.loading);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btn_login = findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mEmail = email.getText().toString().trim();
                final String mPassword = password.getText().toString().trim();

                if(!(new NetworkConnection( LoginActivity.this).isNetworkConnected())){
                    Toast.makeText(LoginActivity.this, "Internet Connection Error", Toast.LENGTH_LONG).show();
                    //System.out.println("no net");
                }
                else if(mEmail.isEmpty()){
                    email.setError("Please insert email");
                }
                else if(mPassword.isEmpty()){
                    password.setError("Please insert password");
                }
                else{
                    loading.setVisibility(View.VISIBLE);
                    btn_login.setVisibility(View.INVISIBLE);
                    Login(mEmail, mPassword);
                    //System.out.println(mEmail);
                    //System.out.println(mPassword);
                }
            }
        });
    }

    private void Login(final String email, final String password) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONObject data = jsonObject.getJSONObject("data");
                            String token = data.getString("token");
                            String myName = data.getString("name");

                            if(success.equals("true")){
                                Toast.makeText(LoginActivity.this, "Login Success!", Toast.LENGTH_SHORT).show();
                                sessionManager.createSession(myName, email, token);
                                Intent intent = new Intent(LoginActivity.this, Shops.class);
                                startActivity(intent);
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, "Register Error 1 ! "+e.toString(), Toast.LENGTH_LONG).show();
                            loading.setVisibility(View.GONE);
                            btn_login.setVisibility(View.VISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(LoginActivity.this, "Register Error 2 ! "+error.toString(), Toast.LENGTH_LONG).show();
                        Toast.makeText(LoginActivity.this, "Invalid email or password!", Toast.LENGTH_SHORT).show();
                        loading.setVisibility(View.GONE);
                        btn_login.setVisibility(View.VISIBLE);
                    }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void registerActivity(View view) {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

}

































