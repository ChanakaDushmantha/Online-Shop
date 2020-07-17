package lk.chanaka.dushmantha.groceryonline.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import lk.chanaka.dushmantha.groceryonline.Items.MainActivity;
import lk.chanaka.dushmantha.groceryonline.MyApp;
import lk.chanaka.dushmantha.groceryonline.R;
import lk.chanaka.dushmantha.groceryonline.SessionManager;

public class LoginActivity extends AppCompatActivity {

    private EditText email, password;
    private ProgressBar loading;
    private CardView btn_login;
    private static String URL;
    private String host;
    SessionManager sessionManager;
    GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 4526;
    LoginButton facebookLogin;
    CallbackManager callbackManager;

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

                /*if(!(new NetworkConnection( LoginActivity.this).isNetworkConnected())){
                    Toast.makeText(LoginActivity.this, "Internet Connection Error", Toast.LENGTH_LONG).show();
                    //System.out.println("no net");
                }
                else*/ if(mEmail.isEmpty()){
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

        googleSignIn();
        facebookLogin();
    }

    private void facebookLogin() {
        facebookLogin = findViewById(R.id.facebook_login_button);
        callbackManager = CallbackManager.Factory.create();
        facebookLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                String info = loginResult.getAccessToken().getUserId();
                String image_url = "https://graph.facebook.com/"+info+"/picture?return_ssl_resources=1";
                Log.d("Tag",info+"  "+image_url);
                String accessToken = loginResult.getAccessToken().getToken();
                System.out.println(accessToken+" token");
                RequestData();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, "Facebook login error "+error, Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void RequestData(){
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                JSONObject json = response.getJSONObject();
                Log.d("TAG",json.toString());
                try {
                    if(json != null){
                        String name = json.getString("name");
                        //String email = json.getString("email");
                        //String link = json.getString("link");
                        /*details_txt.setText(Html.fromHtml(text));
                        profile.setProfileId(json.getString("id"));*/
                        //System.out.println(link);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,email,picture");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void googleSignIn(){
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println("Google signin");
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount acct = completedTask.getResult(ApiException.class);
            if (acct != null) {
                String personName = acct.getDisplayName();
                String googleToken = acct.getIdToken();
                String personGivenName = acct.getGivenName();
                String personFamilyName = acct.getFamilyName();
                String personEmail = acct.getEmail();
                String personId = acct.getId();
                Uri personPhoto = acct.getPhotoUrl();
                System.out.println(personId);
                System.out.println(googleToken);

                sessionManager.createSession(personName, personEmail, "", "");
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            // Signed in successfully, show authenticated UI.
            //updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("TAG", "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
        }
    }

    private void Login(final String email, final String password) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String success = "";
                        String token = "";
                        String myName = "";
                        String myAddress = "";
                        String image_url = null;
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                             success = jsonObject.getString("success");
                            JSONObject data = jsonObject.getJSONObject("data");
                             token = data.getString("token");
                             myName = data.getString("name");
                             myAddress = data.getString("address");
                            try {
                                image_url = data.getString("image_url");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if(success.equals("true")){
                                Toast.makeText(LoginActivity.this, "Login Success!", Toast.LENGTH_SHORT).show();
                                sessionManager.createSession(myName, email, myAddress, token, image_url);
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
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
                        String errorMsg = "Error";
                        if (error instanceof NoConnectionError) {
                            //This indicates that the request has there is no connection
                            errorMsg = getString(R.string.noConnectionError);
                        } else if (error instanceof TimeoutError) {
                            //This indicates that the request has time out
                            errorMsg = getString(R.string.timeoutError);
                        } else if (error instanceof AuthFailureError) {
                            //Error indicating that there was an Authentication Failure while performing the request
                            errorMsg = getString(R.string.authFailureErrorLogin);
                        } else if (error instanceof ServerError) {
                            //Indicates that the server responded with a error response
                            errorMsg = getString(R.string.serverError);
                        } else if (error instanceof NetworkError) {
                            //Indicates that there was network error while performing the request
                            errorMsg = getString(R.string.networkError);
                        } else if (error instanceof ParseError) {
                            // Indicates that the server response could not be parsed
                            errorMsg = getString(R.string.parseError);
                        }
                        Toast.makeText(LoginActivity.this, errorMsg, Toast.LENGTH_LONG).show();
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

































