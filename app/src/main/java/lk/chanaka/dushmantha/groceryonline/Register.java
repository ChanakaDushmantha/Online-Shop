package lk.chanaka.dushmantha.groceryonline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
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
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    private EditText name, email, address, mobile, password, c_password;
    private ProgressBar loading;
    private CardView btn_register;
    private static String URL ;
    SessionManager sessionManager;
    private AwesomeValidation awesomeValidation;
    private String host;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        sessionManager = new SessionManager(this);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        host = ((MyApp) this.getApplication()).getServiceURL();
        URL = host+"/register";
        
        loading = findViewById(R.id.loading);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        address = findViewById(R.id.address);
        mobile = findViewById(R.id.mobile);
        password = findViewById(R.id.password);
        c_password = findViewById(R.id.c_password);
        btn_register = findViewById(R.id.btn_register);

        //adding validation to edit texts
        String regexPassword = "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d]).{6,}";
        awesomeValidation.addValidation(this, R.id.name, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
        awesomeValidation.addValidation(this, R.id.email, Patterns.EMAIL_ADDRESS, R.string.emailerror);
        awesomeValidation.addValidation(this, R.id.address, RegexTemplate.NOT_EMPTY, R.string.addresserror);
        awesomeValidation.addValidation(this, R.id.mobile, "^[0]{1}[0-9]{9}$", R.string.mobileerror);
        awesomeValidation.addValidation(this, R.id.password, regexPassword, R.string.passworderror);
        awesomeValidation.addValidation(this, R.id.c_password, R.id.password, R.string.err_password_confirmation);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(new NetworkConnection( Register.this).isNetworkConnected())){
                    Toast.makeText(Register.this, "Internet Connection Error", Toast.LENGTH_LONG).show();
                }
                else if (awesomeValidation.validate()) {
                    Regist();
                }
                /*Intent intent = new Intent(Register.this, ProfilePicture.class);
                startActivity(intent);*/
            }
        });

    }

    private void Regist(){
        loading.setVisibility(View.VISIBLE);
        btn_register.setVisibility(View.INVISIBLE);

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
                            String token = jsonObject.getString("data");

                            if(success.equals("true")){
                                Toast.makeText(Register.this, "Register Success!", Toast.LENGTH_SHORT).show();
                                sessionManager.createSession(name, email, token);

                                Intent intent = new Intent(Register.this, ProfilePicture.class);
                                startActivity(intent);
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Register.this, "Register Error 1 ! "+e.toString(), Toast.LENGTH_LONG).show();

                            loading.setVisibility(View.GONE);
                            btn_register.setVisibility(View.VISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Register.this, "Register Error 2 ! "+error.toString(), Toast.LENGTH_LONG).show();
                        System.out.println(error.toString());
                        loading.setVisibility(View.GONE);
                        btn_register.setVisibility(View.VISIBLE);

                    }
                })
        {
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
}
