package lk.chanaka.dushmantha.groceryonline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.UriMatcher;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    private CardView btn_login;
    private static String URL_LOGIN = "http://10.0.2.2:8000/api/login";
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(this);
        //sessionManager.checkLogin();

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btn_login = findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mEmail = email.getText().toString().trim();
                final String mPassword = password.getText().toString().trim();

                if(mEmail.isEmpty()){
                    email.setError("Please insert email");
                }
                else if(mPassword.isEmpty()){
                    password.setError("Please insert password");
                }
                else{
                    Login(mEmail, mPassword);
                    //System.out.println(mEmail);
                    //System.out.println(mPassword);
                }
            }
        });
    }

    private void Login(final String email, final String password) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String token = jsonObject.getString("data");

                            if(success.equals("true")){
                                Toast.makeText(LoginActivity.this, "Login Success!", Toast.LENGTH_SHORT).show();
                                sessionManager.createSession("MyName", email, token);
                                Intent intent = new Intent(LoginActivity.this, Itemlist.class);
                                startActivity(intent);
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, "Register Error 1 ! "+e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(LoginActivity.this, "Register Error 2 ! "+error.toString(), Toast.LENGTH_LONG).show();
                        Toast.makeText(LoginActivity.this, "Invalid email or password!", Toast.LENGTH_SHORT).show();
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

































